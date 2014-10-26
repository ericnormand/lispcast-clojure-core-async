;; Exercise 1

(def hand-off (chan))

;; Example

(def hand-off (chan))

(go
  (dotimes [x 10]
    (Thread/sleep 1000)
    (>! hand-off x)))

(go
  (while true
    (println (<! hand-off))))

;; Exercise 2

(def hand-off (chan))

(<! hand-off)

(>! hand-off 10)

;; Exercise 3

(def c (chan))

(go
  (dotimes [_ 5]
    (>! c (rand-int 100))))

(go
  (while true
    (let [v (<! c)]
      (Thread/sleep 1000)
      (println v))))

;; Exercise 4

(let [lc (chan)
      uc (chan)
      st (chan)]
  (go
    (doseq [c "abcdefghij"]
      (>! lc c)))

  (go
    (while true
      (let [c (<! lc)]
        (>! uc (Character/toUpperCase c)))))

  (go
    (while true
      (>! st (str (<! uc)))))

  (go
    (while true
      (println (<! st)))))

;; Exercise 5

(defn assembly-line []
  (let [body-chan (chan)
        wheel1-chan (chan)
        wheel2-chan (chan)
        body+wheel-chan (chan)
        body+2-wheels-chan (chan)
        box-chan (chan)]
    (go
      (while true
        (let [body (loop []
                     (let [part (take-part)]
                       (if (body? part)
                         part
                         (recur))))]
          (println "Got body")
          (>! body-chan body))))
    (go
      (while true
        (let [wheel1 (loop []
                       (let [part (take-part)]
                         (if (wheel? part)
                           part
                           (recur))))]
          (println "Got first wheel")
          (>! wheel1-chan wheel1))))
    (go
      (while true
        (let [wheel2 (loop []
                       (let [part (take-part)]
                         (if (wheel? part)
                           part
                           (recur))))]
          (println "Got second wheel")
          (>! wheel2-chan wheel2))))

    (go
      (while true
        (let [body (<! body-chan)
              wheel1 (<! wheel1-chan)
              bw (attach-wheel body wheel1)]
          (println "Attached first wheel")
          (>! body+wheel-chan bw))))

    (go
      (while true
        (let [bw (<! body+wheel-chan)
              wheel2 (<! wheel2-chan)
              bww (attach-wheel bw wheel2)]
          (println "Attached second wheel")
          (>! body+2-wheels-chan bww))))

    (go
      (while true
        (let [box (box-up (<! body+2-wheels-chan))]
          (println "Boxed up car")
          (>! box-chan box))))

    (go
      (time
       (dotimes [x 10]
         (time
          (put-in-truck (<! box-chan)))
         (println "Done!!"))))))

;; With ten go blocks

(defn assembly-line []
  (let [body-chan (chan)
        wheel1-chan (chan)
        wheel2-chan (chan)
        body+wheel-chan (chan)
        body+2-wheels-chan (chan)
        box-chan (chan)]
    (go
      (while true
        (let [body (loop []
                     (let [part (take-part)]
                       (if (body? part)
                         part
                         (recur))))]
          (println "Got body")
          (>! body-chan body))))
    (go
      (while true
        (let [wheel1 (loop []
                       (let [part (take-part)]
                         (if (wheel? part)
                           part
                           (recur))))]
          (println "Got first wheel")
          (>! wheel1-chan wheel1))))
    (go
      (while true
        (let [wheel2 (loop []
                       (let [part (take-part)]
                         (if (wheel? part)
                           part
                           (recur))))]
          (println "Got second wheel")
          (>! wheel2-chan wheel2))))

    (dotimes [x 2]
      (go
        (while true
          (let [body (<! body-chan)
                wheel1 (<! wheel1-chan)
                bw (attach-wheel body wheel1)]
            (println "Attached first wheel")
            (>! body+wheel-chan bw)))))

    (dotimes [x 2]
      (go
        (while true
          (let [bw (<! body+wheel-chan)
                wheel2 (<! wheel2-chan)
                bww (attach-wheel bw wheel2)]
            (println "Attached second wheel")
            (>! body+2-wheels-chan bww)))))

    (dotimes [x 2]
      (go
        (while true
          (let [box (box-up (<! body+2-wheels-chan))]
            (println "Boxed up car")
            (>! box-chan box)))))

    (go
      (time
       (dotimes [x 10]
         (time
          (put-in-truck (<! box-chan)))
         (println "Done!!"))))))

;; Refactoring

(defn go-body [body-chan]
  (go
    (while true
      (let [body (loop []
                   (let [part (take-part)]
                     (if (body? part)
                       part
                       (recur))))]
        (println "Got body")
        (>! body-chan body)))))

(defn go-wheel1 [wheel1-chan]
  (go
    (while true
      (let [wheel1 (loop []
                     (let [part (take-part)]
                       (if (wheel? part)
                         part
                         (recur))))]
        (println "Got first wheel")
        (>! wheel1-chan wheel1)))))

(defn go-wheel2 [wheel2-chan]
  (go
    (while true
      (let [wheel2 (loop []
                     (let [part (take-part)]
                       (if (wheel? part)
                         part
                         (recur))))]
        (println "Got second wheel")
        (>! wheel2-chan wheel2)))))

(defn go-attach-wheel1 [body-chan wheel1-chan body+wheel-chan]
  (go
    (while true
      (let [body (<! body-chan)
            wheel1 (<! wheel1-chan)
            bw (attach-wheel body wheel1)]
        (println "Attached first wheel")
        (>! body+wheel-chan bw)))))

(defn go-attach-wheel2 [body+wheel-chan wheel2-chan body+2-wheels-chan]
  (go
        (while true
          (let [bw (<! body+wheel-chan)
                wheel2 (<! wheel2-chan)
                bww (attach-wheel bw wheel2)]
            (println "Attached second wheel")
            (>! body+2-wheels-chan bww)))))

(defn go-box-up [body+2-wheels-chan box-chan]
  (go
    (while true
      (let [box (box-up (<! body+2-wheels-chan))]
        (println "Boxed up car")
        (>! box-chan box)))))

(defn go-put-in-truck [box-chan]
  (go
    (time
     (dotimes [x 10]
       (time
        (put-in-truck (<! box-chan)))
       (println "Done!!")))))

(defn assembly-line []
  (let [body-chan (chan)
        wheel1-chan (chan)
        wheel2-chan (chan)
        body+wheel-chan (chan)
        body+2-wheels-chan (chan)
        box-chan (chan)]
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

;; Exercise 6

(assembly-line)
