(defproject raoof/cljs-inline "0.0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 ;[org.clojure/clojurescript "1.10.896"]
                 [org.clojure/clojurescript "1.11.4"]
                 [org.clojure/core.async "1.5.644"]
                 ]
  :plugins [[lein-cljsbuild "1.1.8"]]
  :min-lein-version "2.5.3"
  :clean-targets ^{:protect false} [".stuff" "target" "resources/public/.stuff" "compilers/cljs"]
  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src"]
     :compiler     {:main            clojurescript.core
                    :output-to       "compilers/cljs.js"
                    :output-dir      "compilers/cljs"
                    :optimizations   :whitespace
                    :source-map      "compilers/cljs.js.map"
                    ;:asset-path           "cljs"
                    :closure-defines {goog.DEBUG false}
                    :closure-output-charset "US-ASCII"
                    :pretty-print    false
                    :optimize-constants true
                    :static-fns true
                    :warnings {:single-segment-namespace false}}}]})
