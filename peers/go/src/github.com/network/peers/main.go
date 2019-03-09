package main

import (
	"log"
	"github.com/go-chi/chi"
	"net/http"
	"encoding/json"
	"github.com/network/peers/structs"
	"github.com/network/peers/logic"
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
	routes := [][][]float64{}
	for _, route := range view.Routes {
		coords := logic.ComputeCoords(route.Source, view.Routes[0].Destination)
		routes = append(routes, coords)
	}
	respondwithJSON(w, http.StatusOK, map[string]([][][]float64){"data": routes})
}

func main() {
	r := chi.NewRouter()
	r.Route("/view", func(r chi.Router) {
		r.Post("/generate", CreateViewHandler)
	})
	log.Fatal(http.ListenAndServe(":8087", r))
}