;; Exercise 1

(defn assembly-line []
  (let [body-chan (chan 10)
        wheel-chan (chan 20)
        body+wheel-chan (chan 10)
        body+2-wheels-chan (chan 10)
        box-chan (chan 10)]
    (go-body body-chan)
    (go-wheel1 wheel-chan)
    (go-wheel2 wheel-chan)

    (dotimes [x 2]
      (go-attach-wheel1 body-chan wheel-chan body+wheel-chan))

    (dotimes [x 2]
      (go-attach-wheel2 body+wheel-chan wheel-chan body+2-wheels-chan))

    (dotimes [x 2]
      (go-box-up body+2-wheels-chan box-chan))

    (go-put-in-truck box-chan)))

;; Exercise 2

(defn go-wheel [wheel-chan]
  (go
    (loop []
      (let [wheel (loop []
                     (let [part (take-part)]
                       (if (wheel? part)
                         part
                         (recur))))]
        (println "Got first wheel")
        (when (>! wheel-chan wheel)
          (recur))))))

(defn assembly-line []
  (let [body-chan (chan 10)
        wheel-chan (chan 20)
        body+wheel-chan (chan 10)
        body+2-wheels-chan (chan 10)
        box-chan (chan 10)]
    (go-body body-chan)
    (go-wheel wheel-chan)

    (dotimes [x 2]
      (go-attach-wheel1 body-chan wheel-chan body+wheel-chan))

    (dotimes [x 2]
      (go-attach-wheel2 body+wheel-chan wheel-chan body+2-wheels-chan))

    (dotimes [x 2]
      (go-box-up body+2-wheels-chan box-chan))

    (go-put-in-truck box-chan)))

;; Exercise 3

(defn go-parts [body-chan wheel-chan]
  (go
    (loop []
      (let [part (take-part)]
        (println part)
        (if (body? part)
          (when (>! body-chan part)
            (recur))
          (when (>! wheel-chan part)
            (recur)))))))

(defn assembly-line []
  (let [body-chan (chan 10)
        wheel-chan (chan 20)
        body+wheel-chan (chan 10)
        body+2-wheels-chan (chan 10)
        box-chan (chan 10)]
    (go-parts body-chan wheel-chan)

    (dotimes [x 2]
      (go-attach-wheel1 body-chan wheel-chan body+wheel-chan))

    (dotimes [x 2]
      (go-attach-wheel2 body+wheel-chan wheel-chan body+2-wheels-chan))

    (dotimes [x 2]
      (go-box-up body+2-wheels-chan box-chan))

    (go-put-in-truck box-chan)))

;; Exercise 4

(defn assembly-line []
  (let [body-chan (chan 10)
        wheel-chan (chan 20)
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

;; Exercise 5

(assembly-line)
