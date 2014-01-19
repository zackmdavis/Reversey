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