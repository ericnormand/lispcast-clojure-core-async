;; Exercise 1

(defn go-car [body-chan wheel-chan box-chan]
  (go
    (loop []
      (let [body (<! body-chan)
            wheel1 (<! wheel-chan)
            bw (attach-wheel body wheel1)
            wheel2 (<! wheel-chan)bww (attach-wheel bw wheel2)box (box-up bww)]
        (println "Finished car")
        (when (>! box-chan box)
          (recur))))
    (async/close! body-chan)
    (async/close! wheel-chan)))

;; Exercise 2

(defn assembly-line []
  (let [body-chan (chan (async/dropping-buffer 10))
        wheel-chan (chan (async/dropping-buffer 20))
        box-chan (chan 10)]
    (go-parts body-chan wheel-chan)

    (dotimes [_ 8]
      (go-car body-chan wheel-chan box-chan))

    (go-put-in-truck box-chan)))

;; Exercise 3

(assembly-line)
