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

(defn go-box-up [body+2-wheels-chan box-chan]
  (go
    (loop []
      (let [box (box-up (<! body+2-wheels-chan))]
        (println "Boxed up car")
        (when (>! box-chan box)
          (recur))))
    (async/close! body+2-wheels-chan)))

(defn go-put-in-truck [box-chan]
  (go
    (time
     (dotimes [x 10]
       (time
        (put-in-truck (<! box-chan)))
       (println "Done!!")))
    (async/close! box-chan)))

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
