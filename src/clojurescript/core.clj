(ns clojurescript.core
  (:require [cljs.env :as env]
            [cljs.analyzer :as ana]))

(defmacro dump-analysis-cache 
  ([]
  (let [state @env/*compiler*]
    `(quote ~(get-in state [::ana/namespaces]))))
  ([ns]
  (let [state @env/*compiler*]
    `(quote ~(get-in state [::ana/namespaces ns])))))

(defmacro dump-analysis-cache-cljs-reader []
  (let [state @env/*compiler*]
    `(quote ~(get-in state [::ana/namespaces 'cljs.reader]))))

(defmacro dump-analysis-cache-cljs-js []
  (let [state @env/*compiler*]
    `(quote ~(get-in state [::ana/namespaces 'cljs.js]))))
