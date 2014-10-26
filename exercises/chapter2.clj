;; Examples

(do
  (go
    (dotimes [x 10]
      (println "First go block" x)
      (Thread/sleep 100)))
  (go
    (dotimes [x 10]
      (println "Second go block" x)
      (Thread/sleep 100))))

;; Exercise 1

(time
 (go
   (dotimes [x 10]
     (println x))))

;; Exercise 2

(go
  (time
   (dotimes [x 10]
     (println x))))

;; Instrumenting build-car

(defn build-car [n]
  (println n "Starting build")
  (let [body (loop []
               (let [part (take-part)]
                 (if (body? part)
                   part
                   (recur))))
        _ (println n "Got body")
        wheel1 (loop []
                 (let [part (take-part)]
                   (if (wheel? part)
                     part
                     (recur))))
        _ (println n "Got first wheel")
        wheel2 (loop []
                 (let [part (take-part)]
                   (if (wheel? part)
                     part
                     (recur))))
        _ (println n "Got second wheel")
        bw (attach-wheel body wheel1)
        _ (println n "Attached first wheel")
        bww (attach-wheel bw wheel2)
        _ (println n "Attached second wheel")
        box (box-up bww)]
    (println n "Boxed up car")
    (put-in-truck box)
    (println n "Done!!")))

;; Exercise 3

(defn start-ten []
  (dotimes [x 10]
    (go
      (time (build-car x)))))

;; Exercise 4

(start-ten)

;; Exercise 5

(defn start-three []
  (dotimes [x 3]
    (go
      (time (println (take-part))))))

;; Exercise 6

(dotimes [x 3]
  (go
    (time (attach-wheel :body :wheel))))

(dotimes [x 3]
  (go
    (time (box-up :body))))

(dotimes [x 3]
  (go
    (time (put-in-truck :box))))
