(ns lispcast-clojure-core-async.exercises
  (:require
   [clojure.core.async :as async
    :refer [>! <! alts! chan put! go]]))

(defonce in-chan (chan))

(defonce __in-chan-go (go (while true (>! in-chan (rand-int 10)))))

(defonce print-chan (chan))

(defonce __print-chan-go (go (while true (println (<! print-chan)))))
