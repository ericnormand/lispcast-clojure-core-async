;; Exercise 1

(go
  (<! (async/timeout 1000))
  (println "Finished!"))

;; Exercise 2

(defn pomodoro []
  (go
    (do-work)
    (<! (async/timeout 5000))

    (do-work)
    (<! (async/timeout 5000))

    (do-work)
    (<! (async/timeout 5000))

    (do-work)
    (<! (async/timeout 5000))

    (println "Finished!")))
