;; N-Queens problem for N=4

(set-logic QF_BV)

; The 4 rows are represented by 4 bitvectors of length 4
(declare-fun r1 () (_ BitVec 4))
(declare-fun r2 () (_ BitVec 4))
(declare-fun r3 () (_ BitVec 4))
(declare-fun r4 () (_ BitVec 4))

(assert (distinct r1 r2 r3 r4))

(assert (= #b0000 (bvand r1 (bvsub r1 #b0001))))
(assert (= #b0000 (bvand r2 (bvsub r2 #b0001))))
(assert (= #b0000 (bvand r3 (bvsub r3 #b0001))))
(assert (= #b0000 (bvand r4 (bvsub r4 #b0001))))

(check-sat)
(get-model)
