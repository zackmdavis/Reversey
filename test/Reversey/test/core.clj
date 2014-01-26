(ns Reversey.test.core
  (:use [Reversey.core])
  (:use [clojure.test]))

(deftest test_can_place_disc 
  (def test_board (make_board 8))
  (place_disc! test_board [2 2] (first colors))
  (is (= (first colors)
         (deref (lookup test_board [2 2])))))

(deftest test_can_flip_disc
  (def test_board (make_board 8))
  (place_disc! test_board [2 2] (first colors))
  (flip! test_board [2 2])
  (is (= (second colors)
         (deref (lookup test_board [2 2])))))

(deftest test_displacement
  (is (= (displacement [1 1] [1 6]) [0 5]))
  (is (= (displacement [1 1] [4 1]) [3 0]))
  (is (= (displacement [1 1] [4 4]) [3 3]))
  (is (= (displacement [1 1] [3 6]) [2 5]))
  (is (= (displacement [3 6] [1 1]) [-2 -5])))

(deftest test_displaced_by
  (is (= (displaced_by [2 3] [1 1]) [3 4]))
  (is (= (displaced_by [4 4] [-1 2]) [3 6])))

(deftest test_normalized_displacement
  (is (= (normalized [4 0]) [1 0]))
  (is (= (normalized [0 5]) [0 1]))
  (is (= (normalized [3 -3]) [1 -1]))
  (is (= (normalized [1 -2]) nil)))

(deftest test_positions_between
  (is (= (positions_between_positions [0 0] [0 3])
         [[0 1] [0 2]]))
  (is (= (positions_between_positions [1 2] [3 4])
         [[2 3]]))
  (is (= (positions_between_positions [0 0] [1 2])
         [])))

(deftest test_flip_between
  (let [test_board (make_board 8)
        positions [[1 0] [2 0] [3 0]]]
    (doseq [position positions]
      (place_disc! test_board position (first colors)))
    (flip_between_positions! test_board [0 0] [4 0])
    (let [resulting_colors
          (for [position positions]
            (deref (lookup test_board position)))]
      (is (every? (fn [color] (= color (second colors)))
                  resulting_colors)))))

(deftest test_in_bounds
  (is (= (in_bounds 8 [7 7]) true))
  (is (= (in_bounds 8 [1 0]) true))
  (is (= (in_bounds 8 [-1 9]) false)))

(deftest test_positions_in_direction
  (is (positions_in_direction 8 [7 2] [-1 1])
      [[6 3] [5 4] [4 5] [3 6] [2 7]])
  (is (positions_in_direction 8 [0 0] [-1 -1])
      []))

(comment "not yet implemented"
(deftest test_flip_between_on_relevant_move
  (let [test_board (make_board 8)
        initial_color_positions [[0 0]]
        initial_opposing_positions [[0 1] [0 2] [0 3]]]
    (doseq [position initial_color_positions]
      (place_disc! test_board position (first colors)))
    (doseq [position initial_opposing_positions]
      (place_disc! test_board position (second colors)))
    (move! test_board [0 4] (first colors))
    (let [resulting_opposing
          (for [position initial_opposing_positions]
            (deref (lookup test_board position)))]
      (is (every? (fn [color] (= color (first colors)))
                  resulting_opposing)))))
)
