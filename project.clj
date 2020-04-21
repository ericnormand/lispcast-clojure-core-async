(defproject lispcast-clojure-core-async "0.1.0-SNAPSHOT"
  :description "Part of the Clojure core.async course on PurelyFunctional.tv"
  :url "https://purelyfunctional.tv/courses/clojure-core-async/"
  :license {:name "CC0 1.0 Universal (CC0 1.0) Public Domain Dedication"
            :url "http://creativecommons.org/publicdomain/zero/1.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.1.587"]]
  :repl-options {:init-ns lispcast-clojure-core-async.core}
  :jvm-opts ["-Dclojure.core.async.pool-size=1000"])
