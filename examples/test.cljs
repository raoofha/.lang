
(let [content (js/document.createElement "div")]
  (.append content "hello from ClojureScript ")
  (.append content *clojurescript-version*)
  (.appendChild (. js/document -body) content)
)
