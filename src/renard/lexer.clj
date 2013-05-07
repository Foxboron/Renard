(ns renard.lexer
  (:require [instaparse.core :as insta]))


(def parser
  (insta/parser
   "
   <code> = space* exprs space*
   <exprs> = (expr space+)* expr
   <expr> =  (defn / def / lambda / list / vector / string / bool / symbol / number)

   lambda = <'('> <lambda_kw> space+ expr space* <')'>
   defn = <'('> <defn_kw> space+ symbol space* vector space* exprs* space* <')'>
   def = <'('> <def_kw> space+ symbol space* exprs* <')'>

   vector = <'['> space* exprs* space* '& args'? space* <']'>
   list = <'('> space* !((defn_kw | lambda_kw) (space+ | <')'>)) exprs* space* <')'>

   def_kw = 'def'
   lambda_kw = 'lambda'
   defn_kw = 'defn'

   bool = 'true' | 'false'
   number = (integer | float)
   <float> = #'[\\d]*\\.[\\d]*'
   <integer> = number_re+
   <number_re> = #'[\\d]*'
   <space> = <#'[ \\t\\n,]+'>
   symbol = #'[a-zA-Z:][^ \\(\\)\\[\\]]*'
   string = <'\\\"'> #'[^\".]*' <'\\\"'>
"))

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
  {:vector vector
   :symbol str
   :number read-string
   :string str
   :bool read-string
   :list vector})

(defn parse [input]
  (->> (parser input) (insta/transform transform-options)))
