;; Exercise 1

(def c (chan 10))

(go
  (println (>! c 1)))

;; Exercise 2

(go
  (println (<! c)))

;; Exercise 3

(async/close! c)

(go
  (println (>! c 2)))

;; Exercise 4

(go
  (println (<! c)))

;; Exercise 5

(async/close! c)

;; Exercise 6

(defn go-put-in-truck [box-chan]
  (go
    (time
     (dotimes [x 10]
       (time
        (put-in-truck (<! box-chan)))
       (println "Done!!")))
    (async/close! box-chan)))

;; Exercise 7

(defn go-box-up [body+2-wheels-chan box-chan]
  (go
    (loop []
      (let [box (box-up (<! body+2-wheels-chan))]
        (println "Boxed up car")
        (when (>! box-chan box)
          (recur))))
    (async/close! body+2-wheels-chan)))

;; Exercise 8

(defn go-attach-wheel2 [body+wheel-chan wheel2-chan body+2-wheels-chan]
  (go
    (loop []
      (let [bw (<! body+wheel-chan)
            wheel2 (<! wheel2-chan)
            bww (attach-wheel bw wheel2)]
        (println "Attached second wheel")
        (when (>! body+2-wheels-chan bww)
          (recur))))
    (async/close! body+wheel-chan)
    (async/close! wheel2-chan)))

(defn go-attach-wheel1 [body-chan wheel1-chan body+wheel-chan]
  (go
    (loop []
      (let [body (<! body-chan)
            wheel1 (<! wheel1-chan)
            bw (attach-wheel body wheel1)]
        (println "Attached first wheel")
        (when (>! body+wheel-chan bw)
          (recur))))
    (async/close! body-chan)
    (async/close! wheel1-chan)))

(defn go-wheel2 [wheel2-chan]
  (go
    (loop []
      (let [wheel2 (loop []
                     (let [part (take-part)]
                       (if (wheel? part)
                         part
                         (recur))))]
        (println "Got second wheel")
        (when (>! wheel2-chan wheel2)
          (recur))))))

(defn go-wheel1 [wheel1-chan]
  (go
    (loop []
      (let [wheel1 (loop []
                     (let [part (take-part)]
                       (if (wheel? part)
                         part
                         (recur))))]
        (println "Got first wheel")
        (when (>! wheel1-chan wheel1)
          (recur))))))

(defn go-body [body-chan]
  (go
    (loop []
      (let [body (loop []
                   (let [part (take-part)]
                     (if (body? part)
                       part
                       (recur))))]
        (println "Got body")
        (when (>! body-chan body)
          (recur))))))

;; Exercise 9

(assembly-line)
