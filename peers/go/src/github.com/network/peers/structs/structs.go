package structs

type Location struct {
	Lat float64
	Lon float64
}

type Route struct {
	Source Location
	Destination Location
}

type View struct {
	Routes []Route
}