(set-logic QF_UF)

(declare-fun mythical () Bool)
(declare-fun immortal () Bool)
(declare-fun mammal () Bool)
(declare-fun horned () Bool)
(declare-fun magical () Bool)

(assert (=> mythical immortal))
(assert (=> (not mythical) (and (not immortal) mammal)))
(assert (=> (or immortal mammal) horned))
(assert (=> horned magical))

(check-sat)
(get-value (mythical immortal mammal horned magical))
