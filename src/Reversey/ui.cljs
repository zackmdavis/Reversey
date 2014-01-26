(ns Reversey.ui
  (:require [Reversey.core :as r]))

(def board (r/make_board 8))
(r/place_disc! board [3 3] :white)
(r/place_disc! board [3 4] :black)
(r/place_disc! board [4 3] :black)
(r/place_disc! board [4 4] :white)

(doseq [row board]
  (let [symbols (map #(cond (= :white (deref %)) " &#9675; "
                            (= :black (deref %)) " &#9679; "
                            :else " _ ")
                     row)]
    (.write js/document (str (str symbols) " <br>"))))