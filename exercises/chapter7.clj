;; Exercise 1

(defn assembly-line []
  (let [body-chan (chan (async/dropping-buffer 10))
        wheel-chan (chan (async/dropping-buffer 20))
        body+wheel-chan (chan 10)
        body+2-wheels-chan (chan 10)
        box-chan (chan 10)]
    (go-parts body-chan wheel-chan)

    (dotimes [x 3]
      (go-attach-wheel1 body-chan wheel-chan body+wheel-chan))

    (dotimes [x 3]
      (go-attach-wheel2 body+wheel-chan wheel-chan body+2-wheels-chan))

    (dotimes [x 2]
      (go-box-up body+2-wheels-chan box-chan))

    (go-put-in-truck box-chan)))

;; Exercise 2

(assembly-line)
