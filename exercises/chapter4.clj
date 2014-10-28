;; Exercise 1

(def chan5 (chan 5))

(go
  (doseq [x 10]
    (>! chan5 x)
    (println x)))

;; Exercise 2

(defn assembly-line []
  (let [body-chan (chan 10)
        wheel1-chan (chan 10)
        wheel2-chan (chan 10)
        body+wheel-chan (chan 10)
        body+2-wheels-chan (chan 10)
        box-chan (chan 10)]
    (go-body body-chan)
    (go-wheel1 wheel1-chan)
    (go-wheel2 wheel2-chan)

    (dotimes [x 2]
      (go-attach-wheel1 body-chan wheel1-chan body+wheel-chan))

    (dotimes [x 2]
      (go-attach-wheel2 body+wheel-chan wheel2-chan body+2-wheels-chan))

    (dotimes [x 2]
      (go-box-up body+2-wheels-chan box-chan))

    (go-put-in-truck box-chan)))

;; Exercise 3

(assembly-line)
