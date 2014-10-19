(ns lispcast-clojure-core-async.core
  (:require
   [lispcast-clojure-core-async.exercises :as ex]
   [lispcast-clojure-core-async.factory :refer :all]
   [clojure.core.async :as async
    :refer [>! <! alts! chan put! go]]))

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

(defn start-ten []
  (dotimes [x 10]
    (go
      (time (build-car x)))))

(defn start-three []
  (dotimes [x 3]
    (go
      (time (println (take-part))))))

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
