package main

import "fmt"

type location struct {
	Lat float32
	Lon float32
}

type broadcastPkt struct {
	Id string
	Speed float32
	Location location
}

// Output to this function will be randomly generated data
// in follong structure
/*
 location: (lat, lon)
 timestamp: 0
 speed: speed
 directions: - - - -
*/
func main() {
    fmt.Println("hello world")
	fmt.Println(location{3, 4})
}