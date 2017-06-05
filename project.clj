(defproject lead-indicators "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.13.0"]
                 [incanter "1.5.7"]] ;; 1.9.1 results in clojure:pom:1.3.0-alpha2 error && 1.9.0 $order is not working
  :main lead-indicators.core)