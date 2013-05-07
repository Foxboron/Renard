(ns renard.core-test
  (:use midje.sweet)
  (:require [renard.lexer :as lexer]))



(facts "string"
       (lexer/parser "\"Hello World\"")
        => '([:string "Hello World"])
        (lexer/parser "\"Hello_?=)(/&%¤#world\"")
        => '([:string "Hello_?=)(/&%¤#world"]))

(facts "numbers"
       (fact "integers"
              (lexer/parser "1")
              => '([:number "1"])
              (lexer/parser "10")
              => '([:number "10"]))
       (fact "floats - FIX ME"
              (lexer/parser "1.0")
              => '([:number "1" "." "0"])
              (lexer/parser "34.42")
              => '([:number "34" "." "42"])))




(facts "symbols"
       (lexer/parser "symbol")
       => '([:symbol "symbol"])
       (lexer/parser "symbol?!")
       => '([:symbol "symbol?!"]))


(facts "keys"
;       (lexer/parser ":key")
;       => '([:key ":key"])
;       (lexer/parser ":set!")
;       => '([:key ":set!"])
;       (lexer/parser ":bool?")
;       => '([:key ":bool?"])
       )


(facts "vector"
       (lexer/parser "[1 2 3]")
       => '([:vector [:number "1"] [:number "2"] [:number "3"]])
       ;;Because....commas
       (lexer/parser "[1,2,3]")
       => '([:vector [:number "1"] [:number "2"] [:number "3"]])
       (lexer/parser "[1 symbol :symbol true 100 4.2 [1 2 3] (\"hey\")]")
       => '([:vector [:number "1"] [:symbol "symbol"] [:symbol ":symbol"] [:bool "true"] [:number "100"] [:number "4" "." "2"] [:vector [:number "1"] [:number "2"] [:number "3"]] [:list [:string "hey"]]])
       (lexer/parser "[& args]")
       => '([:vector "& args"])
       (lexer/parser "[x y z & args]")
       => '([:vector [:symbol "x"] [:symbol "y"] [:symbol "z"] "& args"]))

(facts "functions"
       (lexer/parser "(defn test [x] (println \"hello world\"))")
       => '([:defn [:symbol "test"] [:vector [:symbol "x"]] [:list [:symbol "println"] [:string "hello world"]]]
              ))


(facts "define"
       (lexer/parser "(def test (lambda (println \"Hello World\")))")
       => '([:def [:symbol "test"] [:lambda [:list [:symbol "println"] [:string "Hello World"]]]])
       (lexer/parser "(def bool true)")
       => '([:def [:symbol "bool"] [:bool "true"]])
       (lexer/parser "(def number 1.0)")
       => '([:def [:symbol "number"] [:number "1" "." "0"]])
       (lexer/parser "(def word \"Test Test\")")
       => '([:def [:symbol "word"] [:string "Test Test"]]))
