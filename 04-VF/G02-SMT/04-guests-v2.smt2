(set-logic UF)

(declare-sort Person 0)
(declare-sort Chair 0)

(declare-fun anne () Person)
(declare-fun susan () Person)
(declare-fun peter () Person)

(declare-fun left () Chair)
(declare-fun middle () Chair)
(declare-fun right () Chair)

(declare-fun chairof (Person) Chair)
(assert (distinct (chairof anne) (chairof susan) (chairof peter)))

(assert (distinct left middle right))
(assert (distinct anne susan peter))

(assert (and (or (= (chairof anne) left) (= (chairof anne) right)) (not (= (chairof peter) middle))))
; (assert (not (chairof anne left)))
(assert (and (=> (= (chairof peter) left) (= (chairof susan) right)) (=> (= (chairof peter) middle) (= (chairof susan) left))))

(assert (forall ((p Person))
                (or (= (chairof p) left)
                    (= (chairof p) middle)
                    (= (chairof p) right)))
)

(check-sat)
(get-model)
