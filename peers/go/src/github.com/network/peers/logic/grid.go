package logic

import (
	"github.com/network/peers/structs"
	"net/http"
	"io/ioutil"
	"fmt"
	"encoding/json"
	"math"
)

func Radians(degrees float64) float64 {
	return degrees * math.Pi / 180
}

func Degrees(radians float64) float64 {
	return radians * (180 / math.Pi)
}

func FetchRoutePoints(source structs.Location, destination structs.Location) ([][]float64, error) {
	url := "https://api.openrouteservice.org/directions"
	req, _ := http.NewRequest("GET", url, nil)
	query := req.URL.Query()

	coordinateStr := fmt.Sprintf("%f,%f|%f,%f", source.Lat, source.Lon, destination.Lat, destination.Lon)

	query.Add("api_key", "5b3ce3597851110001cf6248c274b8cf4812435bb9ce8ffc04e26110")
	query.Add("coordinates", coordinateStr)
	query.Add("profile", "driving-car")
	query.Add("geometry_format", "polyline")

	req.URL.RawQuery = query.Encode()

	res, err := http.DefaultClient.Do(req)

	if (err != nil) {
		return nil, err
	}

	defer res.Body.Close()
	body, _ := ioutil.ReadAll(res.Body)

	var anyJson map[string]interface{}
	json.Unmarshal(body, &anyJson)
	geometries := anyJson["routes"].([]interface{})[0].(map[string]interface{})["geometry"].([]interface{})
	edgePoints := [][]float64{}

	for _, geometry := range geometries {
		edgePoint := []float64{}
		for _, vi := range geometry.([]interface{}) {
			edgePoint = append(edgePoint, vi.(float64))
		}
		edgePoints = append(edgePoints, edgePoint)
	}

	return edgePoints, nil
}

func CalculateBearing(source structs.Location, destination structs.Location) float64 {
	startLat := Radians(source.Lat)
	startLon := Radians(source.Lon)
	endLat := Radians(destination.Lat)
	endLon := Radians(destination.Lon)
	dLon := endLon - startLon
	dPhi := math.Log(math.Tan(endLat/2.0 + math.Pi/4.0) / math.Tan(startLat/2.0 + math.Pi/4.0))
	if math.Abs(dLon) > math.Pi {
		if dLon > 0.0 {
			dLon = -(2.0 * math.Pi - dLon)
		} else {
			dLon = (2.0 * math.Pi + dLon)
		}
	}
	bearing := math.Mod((Degrees(math.Atan2(dLon, dPhi)) + 360.0), 360.0)
	return bearing
}
	
func GetPathLength(source structs.Location, destination structs.Location) float64 {
	R := 6371000.0 // Radius of earth in meters in float
	startLat := Radians(source.Lat)
	endLat := Radians(destination.Lat)
	deltaLat := Radians(destination.Lat-source.Lat)
	deltaLon := Radians(destination.Lon-source.Lon)
    a := math.Sin(deltaLat/2) * math.Sin(deltaLat/2) + math.Cos(startLat) * math.Cos(endLat) * math.Sin(deltaLon/2) * math.Sin(deltaLon/2)
	c := 2 * math.Atan2(math.Sqrt(a), math.Sqrt(1-a))
	d := R * c
	return d
}

func GetDestinationLatLong(destination structs.Location, azimuth float64, distance float64) []float64 {
	R := 6378.1 // Radius of the Earth in km
	brng := Radians(azimuth)
	d := distance/1000
	lat1 := Radians(destination.Lat)
	lon1 := Radians(destination.Lon)
	lat2 := math.Asin(math.Sin(lat1) * math.Cos(d/R) + math.Cos(lat1)* math.Sin(d/R)* math.Cos(brng))
    lon2 := lon1 + math.Atan2(math.Sin(brng) * math.Sin(d/R)* math.Cos(lat1), math.Cos(d/R)- math.Sin(lat1)* math.Sin(lat2))
	lat2 = Degrees(lat2)
    lon2 = Degrees(lon2)
    return []float64{lat2, lon2}
}

func GenerateCoords(interval float64, azimuth float64, source structs.Location, destination structs.Location) ([][]float64) {
	d := GetPathLength(source, destination)
	_, dist := math.Modf(d/interval)
	counter := float64(interval)
	coords := [][]float64{}
	coords = append(coords, []float64{source.Lat, source.Lon})
	for i := 0; i < int(dist); i++ {
		coord := GetDestinationLatLong(source, azimuth, counter)
		counter = counter + float64(interval)
		coords = append(coords, coord)
	}
	return coords
}

func ComputeCoords(source structs.Location, destination structs.Location) ([][]float64) {
	ranges, _ := FetchRoutePoints(source, destination)
	coords := [][]float64{}
	azimuth := CalculateBearing(source, destination)
	interval := 10.0
	for i := 0; i < len(ranges); i++ {
		coords = append(coords, GenerateCoords(interval, azimuth, source, destination)...)
	}
	return coords
}