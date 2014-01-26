(defproject Reversey "development"
  :description "the classic abstract strategy board game, misspelled"
  :url "http://github.com/zackmdavis/Reversey"
  :license {:name "MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"}
  :plugins [[lein-cljsbuild "0.2.7"]]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojurescript "0.0-1450"]]
  :source-paths ["src"]
  :cljsbuild {:builds 
              [{:source-path "src"
               :compiler 
                {:output-to "resources/reversey.js"
                 :optimizations :whitespace
                 :pretty-print true}}]})
