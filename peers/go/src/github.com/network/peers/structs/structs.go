package structs

type Location struct {
	Lat float64
	Lon float64
}

type Route struct {
	Source Location
	Destination Location
	Direction int32
}

// type Road struct {
// 	Routes []Route
// }

type View struct {
	Horizontal []Route
	Vertical []Route
}

type Cell struct {
	Lat float64 `json:"lat"`
	Lon float64 `json:"lon"`
	Intersection bool `json:"intersection"`
	Direction int `json:"direcion"`
}

type Grid struct {
	HorizontalCells [][]Cell `json:"horizontalCells"`
	VerticalCells [][]Cell `json:"verticalCells"`	
	HorizontalY int `json:"horizontalY"`
	VerticalX int `json:"verticalX"`
}