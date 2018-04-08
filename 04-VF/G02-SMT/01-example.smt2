(set-logic QF_UF)
(declare-fun a1 () Bool)
(declare-fun a2 () Bool)
(declare-fun p () Bool)

(assert a1)
(assert (or a1 p))
(assert (or (not a1) (or (not p) a2)))
(assert (or a1 (not a2)))

(check-sat)
(get-model)
