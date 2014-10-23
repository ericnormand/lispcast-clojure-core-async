(ns lispcast-clojure-core-async.factory
  (:require
   [clojure.core.async.impl.concurrent :as conc]
   [clojure.core.async.impl.exec.threadpool :as tp]
   [clojure.core.async :as async
    :refer [>! <! alts! chan put! go]]))

;; increase the size of the thread pool

(defonce my-executor
  (java.util.concurrent.Executors/newFixedThreadPool
   1000
   (conc/counted-thread-factory "toy-car-factory-%d" true)))

(alter-var-root #'clojure.core.async.impl.dispatch/executor
                (constantly (delay (tp/thread-pool-executor my-executor))))

;; define our factory operations

(defn try-to-take [[_ new]]
  [new (if (= :free new)
         :taken
         new)])

(def part-box (atom [:free :free]))

(defn take-part []
  (let [status (swap! part-box try-to-take)]
    (if (not= [:free :taken] status)
      (do
        (Thread/sleep 100)
        (recur))
      (let [r (rand-nth [:wheel :wheel (atom {:body []
                                              :status [:free :free]})])]
        (Thread/sleep 1000)
        (reset! part-box [:taken :free])
        r))))

(defn attach-wheel [body wheel]
  (if (= :body body)
    (do
      (Thread/sleep 4000)
      :body)
    (loop []
      (let [{status :status} (swap! body update-in [:status] try-to-take)]
        (if (not= [:free :taken] status)
          (do
            (Thread/sleep 100)
            (recur))
          (do
            (Thread/sleep 4000)
            (swap! body #(-> %
                             (assoc :status [:taken :free])
                             (update-in [:body] conj wheel)))
            body))))))

(defn box-up [body]
  (Thread/sleep 3000)
  :box)

(def truck (atom [:free :free]))

(defn put-in-truck [body]
  (let [status (swap! truck try-to-take)]
    (if (not= [:free :taken] status)
      (do
        (Thread/sleep 100)
        (recur body))
      (do
        (Thread/sleep 2000)
        (reset! truck [:taken :free])
        :done))))

(defn body? [part]
  (if (keyword? part)
    (= :body part)
    (contains? @part :body)))

(defn wheel? [part]
  (= :wheel part))


(defn do-work []
  (println "Working...")
  (Thread/sleep 500)
  (println "Working...")
  (Thread/sleep 5200)
  (println "Still working...")
  (Thread/sleep 500)
  (println "Done working."))

(defonce telephone-chan (chan))

(defonce __1 (go (while true
                   (<! (async/timeout 3000))
                   (>! telephone-chan :ring))))

(defonce email-chan (chan))

(defonce __2 (go (while true
                   (<! (async/timeout 1000))
                   (>! email-chan :email))))

(defonce todo-chan (chan))

(defonce __3 (go (while true
                   (>! todo-chan :todo))))
