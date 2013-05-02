(ns renard.lexer
  (:require [instaparse.core :as insta]))


(def parser
  (insta/parser
   "expr = (string | reload-fun | keyword | number | vector | operation)
    operation = <'('> operator space+ vector operation? <')'>

    vector = <'['>? items+ <']'>?
    <list> = <'('> items+ <')'>

    <items> = snumber+ | number+ | string+ | keyword

    string = string-literal sword+ string-literal
    <snumber> = number space?
    <skeyword> = keyword space?

    <string-literal> = <#'[\\\"]+'>
    operator = '+' | '-' | '*' | '/'
    <sword> = #'[a-zA-Z\\.\\ ]+'
    keyword = #'[a-zA-Z]+'
    number = #'[0-9]+'
    <space> = <#'[ ]+'>

    reload-fun = #'reload'"))

(defn choose-operator [op]
  (case op
    "+" +
    "-" -
    "*" *
    "/" /))

(defn reload-renard [x]
 (try
   (load-file "src/renard/lexer.clj")
   (catch Exception e (println (str "Exception: " (.getMessage e))))))

(def transform-options
  {:reload-fun reload-renard
   :string str
   :keyword str
   :number read-string
   :vector (comp vec list)
   :operator choose-operator
   :operation (fn [& args] (str args))
   :expr identity})

(defn parse [input]
  (->> (parser input) (insta/transform transform-options)))

(parse "(lll)")

(parse "(+ 1 2 3 (- 2 3))")

(parse "\"word word\"")

(parse "[12 2 3 4 \"lol\"]")
