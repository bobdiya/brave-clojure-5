(ns brave-clojure-5.core
  (:gen-class))

;; Exercises for Chapter 5 of the book
;; Clojure for the Brave and True
;; https://www.braveclojure.com/functional-programming/#Exercises


;; 1. You used (comp :intelligence :attributes) to create a function that
;; returns a character’s intelligence. Create a new function, attr, that you can
;; call like (attr :intelligence) and that does the same thing.

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(def c-int (comp :intelligence :attributes))

(c-int character)
;; => 10


(defn attr [attr-key] (comp attr-key :attributes))

((attr :intelligence) character)
;; => 10
;;; Nice!

;; 2. Implement the comp function
(defn my-comp [& fs]
  (let [gs (drop 1 (reverse fs))]
    (fn [& args]
      (reduce #(%2 %1)
            (apply (last fs) args)
            gs))))

((my-comp str +) 8 8 8)
;; => "24"

;;; If you want to avoid having to handle the "zero case", you can use the
;;; `identity` function. That also lets you handle the case where comp is called
;;; with zero arguments, which the original `comp` allows:

(let [f (comp)]
  (f 3))
;; => 3

;;; I took a stab at it, but but it way down at the bottom of the file in case
;;; you want to try for yourself first.

;; 3. Implement the assoc-in function. Hint: use the assoc function and define
;; its parameters as [m [k & ks] v].
(defn my-assoc-in [m [k & ks] v]
  (if (empty? ks)
    (assoc m k v)
    (assoc m k (my-assoc-in (get m k) ks v))))


(def users [{:name "James" :age {:gender "male"}}  {:name "John" :age 43}])
(my-assoc-in users [0 :age :gender1] "hello")
;; => [{:name "James", :age {:gender "male", :gender1 "hello"}}
;;     {:name "John", :age 43}]

;;; Looking good!

;; 4. Look up and use the update-in function.
(update-in {:a {:b {:c 35}}} [:a :b :c] / 4 5)
;; => {:a {:b {:c 7/4}}}

;; 5. Implement update-in.
(def users [{:name "James" :details {:age 33}}  {:name "John" :age 43}])

(defn my-update-in [m ks f & args]
  (let [nested-val (get-in m ks)]
    (if (nil? nested-val)
      (assoc-in m ks (apply f args)) 
      (assoc-in m ks (apply f (cons nested-val args))))))

(my-update-in users [2 :details :age] / 25 4)
;; => [{:name "James", :details {:age 33}}
;;     {:name "John", :age 43}
;;     {:details {:age 25/4}}]

;;; Nice!


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;;; My stab at comp below ↓








































(comment
  (let [my-comp-2
        (fn [& fns]
          (reduce (fn [f g]
                    (fn [& args]
                      (f (apply g args))))
                  identity
                  fns))]
    [((my-comp-2) 'initial-value)
     ((my-comp-2 str +) 8 8 8)
     ((my-comp-2 (partial + 1) (partial * 10)) 0)]
    )
  ;; => [initial-value "24" 1]
  )
