(ns clojurescript.core
  (:require-macros [clojurescript.core :refer [dump-analysis-cache dump-analysis-cache-cljs-js dump-analysis-cache-cljs-reader]]
                   [cljs.core.async.macros :refer [go go-loop]])
  (:require cljs.js
            cljs.reader
            [cljs.core.async :refer [put! <! chan]]
            ;clojure.browser.repl
            [cljs.analyzer :as ana]
            [goog.object]))

(enable-console-print!)
(set! js/goog.isProvided_ (fn [] false))

(def compiler-state
  (cljs.js/empty-state))

#_(def compiler-state
    (doto (cljs.js/empty-state)
      (swap!
       (fn [state]
         (-> state
             (assoc-in [::ana/namespaces] (dump-analysis-cache)))))))
#_(-> @compiler-state
      (get :cljs.analyzer/namespaces)
      keys
      prn)

(def root-classpath "/.cljs/")

(defn load [url]
  (-> (js/fetch url)
      (.then
       (fn [d]
         (if-not (.-ok d)
           (throw (js/Error. (str "failed loading " url)))
           (.text d))))))

(defn eval-str
  ([s]
   (eval-str s 'cljs.user))
  ([s ns]
   (eval-str s ns js/eval))
  ([s ns js-eval]
   (cljs.js/analyze-str
    compiler-state
    s
    ""
    {:ns ns
     :context :statment}
    identity)
  ;(prn ('cg.core (:cljs.analyzer/namespaces @compiler-state)))
   (cljs.js/eval
    compiler-state
    (cljs.reader/read-string (str "((fn [] (do " s ")))"))
      ;""
    {:ns ns
     :context :expr
       ;:context :statment
     :def-emits-var true
     :eval #(js-eval (:source %))
     :load (fn [{n :name path :path macros :macros :as opts} cb]
             (let [rpath  (str root-classpath path)
                   path1 (str rpath (if macros ".clj" ".cljs"))
                   path2 (str rpath ".cljc")
                   path3 (str rpath ".js")
                   ;path4 (when (.startsWith path "goog/") (str rpath (.substring path (.lastIndexOf path "/")) ".js"))
                   load-cb (fn [s] (cb {:lang :clj :source s :cache (get-in @compiler-state [:cljs.analyzer/namespaces n])}))
                   load-js-cb (fn [s] (cb {:lang :js :source s :cache (get-in @compiler-state [:cljs.analyzer/namespaces n])}))]
               (-> (load path1)
                   (.then load-cb)
                   (.catch (fn []
                             (-> (load path2)
                                 (.then load-cb)
                                 (.catch (fn []
                                           (if-not macros
                                             (-> (load path3)
                                                 (.then load-js-cb)
                                                 (.catch #(do
                                                            (cb {:lang :js :source ""})
                                                            (.error js/console (str "path: " path1 " , " path2 " , " path3 " not found"))
                                                            #_(if (.startsWith path "goog/")
                                                                (cb {:lang :js :source ""})
                                                                (.error js/console (str "path: " path1 " , " path2 " , " path3 " not found"))))))
                                             (.error js/console (str "path: " path1 " , " path2 " not found")))))))))))}
    (fn [{:keys [value error]}]
      (if error
        (throw error)
        value)))))

(defn load-script [script]
  (let [c (chan)]
    (if (not= (. script -src) "")
      (.then (load  (. script -src)) #(put! c %))
      (put! c (. script -innerHTML)))
    c))

(defn run-scripts []
  (let [scripts (. js/document getElementsByTagName "script")]
    (go-loop [i 0]
      (when (< i (. scripts -length))
        (let [script (aget scripts i)]
          (cond
            (= (. script -type) "text/cljs")
            (do
              (eval-str (<! (load-script script)))
              (recur (inc i)))
            (= (. script -type) "text/js")
            (do
              (js/eval (<! (load-script script)))
              (recur (inc i)))
            :else (recur (inc i))))))))

#_(js/addEventListener "DOMContentLoaded" run-scripts false)
(js/addEventListener "load" run-scripts false)
(goog.object/set js/window "e" (fn [strings] (:value (eval-str (. strings join "")))))
