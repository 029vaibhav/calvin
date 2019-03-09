package main

import (
	"log"
	"github.com/go-chi/chi"
	"net/http"
	"encoding/json"
	"github.com/network/peers/structs"
	"github.com/network/peers/logic"
	// "fmt"
)


func respondwithJSON(w http.ResponseWriter, code int, payload interface{}) {
    response, _ := json.Marshal(payload)
    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(code)
    w.Write(response)
}

func CreateViewHandler(w http.ResponseWriter, r *http.Request) {
	var view structs.View 
	json.NewDecoder(r.Body).Decode(&view) 
	horizontalRoad := view.Horizontal
	verticalRoad := view.Vertical
	hRoute1 := logic.ComputeCoords(horizontalRoad[0].Source, horizontalRoad[0].Destination)
	hRoute2 := logic.ComputeCoords(horizontalRoad[1].Source, horizontalRoad[1].Destination)

	vRoute1 := logic.ComputeCoords(verticalRoad[0].Source, verticalRoad[0].Destination)
	vRoute2 := logic.ComputeCoords(verticalRoad[1].Source, verticalRoad[1].Destination)
	
	cells := [][]structs.Cell{}
	a, b := logic.GetIntersectionPoint(hRoute1, vRoute1, 10.0)
	
	cells = append(cells, MakeResponse(hRoute1, a, 1))
	cells = append(cells, MakeResponse(hRoute2, a, -1))

	cells = append(cells, MakeResponse(vRoute1, b, 1))
	cells = append(cells, MakeResponse(vRoute2, b, -1))

	respondwithJSON(w, http.StatusOK, cells)
}

func MakeResponse(route [][]float64, intersection int, direction int) []structs.Cell {
	cells := []structs.Cell{}
	for i, location := range route {
		cell := structs.Cell{
			Lat: location[0],
			Lon: location[1],
			Intersection: intersection == i,
			Direction: direction,
		}

		cells = append(cells, cell)
	}

	return cells
}

func main() {
	r := chi.NewRouter()
	r.Route("/view", func(r chi.Router) {
		r.Post("/generate", CreateViewHandler)
	})
	log.Fatal(http.ListenAndServe(":8087", r))
}