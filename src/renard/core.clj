(ns renard.core
  (:require [renard.lexer :as lexer]))


(defn repl []
  (do
    (print "Renard >> ")
    (flush))
  (let [input (read-line)]
    (println (lexer/parse input))
    (recur)))

(defn -main [& args]
  (println "Renard 0.0.1")
  (println "============")
  (flush)
  (repl))
