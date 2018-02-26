(set-logic QF_UF)

(declare-sort Person 0)
(declare-sort Chair 0)

(declare-fun anne () Person)
(declare-fun peter () Person)
(declare-fun susan () Person)

(declare-fun left () Chair)
(declare-fun middle () Chair)
(declare-fun right () Chair)

(declare-fun sits (Person Chair) Bool)

(assert (or (sits anne left) (sits anne middle) (sits anne right)))
(assert (or (sits peter left) (sits peter middle) (sits peter right)))
(assert (or (sits susan left) (sits susan middle) (sits susan right)))


(assert (=> (sits anne left) (and (not (sits peter left)) (not (sits susan left)))))
(assert (=> (sits peter left) (and (not (sits anne left)) (not (sits susan left)))))
(assert (=> (sits susan left) (and (not (sits peter left)) (not (sits anne left)))))

(assert (=> (sits anne middle) (and (not (sits peter middle)) (not (sits susan middle)))))
(assert (=> (sits peter middle) (and (not (sits anne middle)) (not (sits susan middle)))))
(assert (=> (sits susan middle) (and (not (sits peter middle)) (not (sits anne middle)))))

(assert (=> (sits anne right) (and (not (sits peter right)) (not (sits susan right)))))
(assert (=> (sits peter right) (and (not (sits anne right)) (not (sits susan right)))))
(assert (=> (sits susan right) (and (not (sits peter right)) (not (sits anne right)))))


(assert (and (or (sits anne left) (sits anne right)) (not (sits peter middle))))
(assert (not (sits anne left)))
(assert (and (=> (sits peter left) (sits susan right)) (=> (sits peter middle) (sits susan left))))

(check-sat)
(get-model)
