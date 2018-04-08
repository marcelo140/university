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

(assert (not (and (and (and (and (not mythical) (not immortal)) mammal) horned) magical)))
(assert (not (and (and (and (and mythical immortal) mammal) horned) magical)))
(assert (not (and (and (and (and mythical immortal) (not mammal)) horned) magical)))

(check-sat)
(get-value (mythical immortal mammal horned magical))
