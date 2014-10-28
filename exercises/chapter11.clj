;; Exercise 1

(defn work-session []
  (go
    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan] :priority true)]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))))

(defn pomodoro []
  (go
    (work-session)
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan] :priority true)]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Pushup!")
              (recur))))))

    (work-session)
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan] :priority true)]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Jump!")
              (recur))))))

    (work-session)
    (<! (async/timeout 10000))
    (println "Work is done!")))

;; Example

(defn pomodoro []
  (go
    (<! (work-session))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan] :priority true)]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Pushup!")
              (recur))))))

    (<! (work-session))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan] :priority true)]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Jump!")
              (recur))))))

    (<! (work-session))
    (<! (async/timeout 10000))
    (println "Work is done!")))

;; Exercise 2

(defn break-session [do-exercise]
  (go
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan] :priority true)]
          (if (= c t)
            (println "Work time!")
            (do
              (do-exercise)
              (recur))))))))

(defn pomodoro []
  (go
    (<! (work-session))
    (<! (break-session (fn [] (println "Pushup!"))))

    (<! (work-session))
    (<! (break-session (fn [] (println "Jump!"))))

    (<! (work-session))
    (<! (async/timeout 10000))
    (println "Work is done!")))

;; Exercise 3

(defn work-day []
  (go
    (<! (pomodoro))
    (<! (pomodoro))
    (<! (pomodoro))))
