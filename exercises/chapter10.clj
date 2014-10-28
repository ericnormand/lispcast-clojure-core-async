;; Exercise 1

(defn work []
  (go
    (let [[val ch] (alts! [telephone-chan
                           email-chan])]
      (cond
       (= ch telephone-chan)
       (println "Ring-ring!")
       (= ch email-chan)
       (println "You've got mail!")))))

;; Exercise 2

(defn work []
  (go
    (let [[val ch] (alts! [telephone-chan
                           email-chan
                           todo-chan])]
      (cond
       (= ch telephone-chan)
       (println "Ring-ring!")
       (= ch email-chan)
       (println "You've got mail!")
       (= ch todo-chan)
       (println "Work work work!")))))

;; Exercise 3

(defn work []
  (go
    (dotimes [x 5]
      (let [[val ch] (alts! [telephone-chan
                             email-chan
                             todo-chan])]
        (cond
         (= ch telephone-chan)
         (println "Ring-ring!")
         (= ch email-chan)
         (println "You've got mail!")
         (= ch todo-chan)
         (println "Work work work!"))))))

;; Exercise 4

(defn work []
  (go
    (let [t (async/timeout 10000)]
     (loop []
       (let [[val ch] (alts! [t
                              telephone-chan
                              email-chan
                              todo-chan])]
         (if (= ch t)
           (println "Break time!")
           (do
            (cond
             (= ch telephone-chan)
             (println "Ring-ring!")
             (= ch email-chan)
             (println "You've got mail!")
             (= ch todo-chan)
             (println "Work work work!"))
            (recur))))))))

;; Exercise 5

(defn pomodoro []
  (go
    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (cond
               (= ch telephone-chan)
               (println "Ring-ring!")
               (= ch email-chan)
               (println "You've got mail!")
               (= ch todo-chan)
               (println "Work work work!"))
              (recur))))))))

;; Exercise 6

(defn do-task [v ch]
  (cond
   (= ch telephone-chan)
   (println "Ring-ring!")
   (= ch email-chan)
   (println "You've got mail!")
   (= ch todo-chan)
   (println "Work work work!")))

(defn pomodoro []
  (go
    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))))

;; Exercise 7

(defn pomodoro []
  (go
    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan])]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Pushup!")
              (recur))))))))

;; Exercise 8

(defn pomodoro []
  (go
    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan])]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Pushup!")
              (recur))))))

    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan])]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Jump!")
              (recur))))))))

;; Exercise 9

(defn pomodoro []
  (go
    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan])]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Pushup!")
              (recur))))))

    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan])]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Jump!")
              (recur))))))

    (let [t (async/timeout 10000)]
      (loop []
        (let [[val ch] (alts! [t
                               telephone-chan
                               email-chan
                               todo-chan])]
          (if (= ch t)
            (println "Break time!")
            (do
              (do-task val ch)
              (recur))))))
    (<! (async/timeout 10000))
    (println "Work is done!")))

;; Exercise 10

(defn pomodoro []
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
              (recur))))))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan] :priority true)]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Pushup!")
              (recur))))))

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
              (recur))))))
    (let [t (async/timeout 5000)]
      (loop []
        (let [[v c] (alts! [t reps-chan] :priority true)]
          (if (= c t)
            (println "Work time!")
            (do
              (println "Jump!")
              (recur))))))

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
              (recur))))))
    (<! (async/timeout 10000))
    (println "Work is done!")))
