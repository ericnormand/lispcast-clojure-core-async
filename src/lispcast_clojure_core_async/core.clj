(ns lispcast-clojure-core-async.core
  (:require
   [lispcast-clojure-core-async.exercises :as ex]
   [lispcast-clojure-core-async.factory :refer :all]
   [clojure.core.async :as async
    :refer [>! <! alts! chan put! go]]))

(defn build-car []
  (let [body (loop []
               (let [part (take-part)]
                 (if (body? part)
                   part
                   (recur))))
        wheel1 (loop []
                 (let [part (take-part)]
                   (if (wheel? part)
                     part
                     (recur))))
        wheel2 (loop []
                 (let [part (take-part)]
                   (if (wheel? part)
                     part
                     (recur))))
        bw (attach-wheel body wheel1)
        bww (attach-wheel bw wheel2)
        box (box-up bww)]
    (put-in-truck box)))
