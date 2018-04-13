(set-logic QF_AUFLIA)

; y = y + 3;
; a[i] = y; 
; tmp = a[i];
; a[i] = a[j];
; a[j] = tmp;

(declare-const y0 Int)
(declare-const y1 Int)
(declare-const i0 Int)
(declare-const j0 Int)
(declare-const tmp Int)
(declare-const a0 (Array Int Int))
(declare-const a1 (Array Int Int))
(declare-const a2 (Array Int Int))
(declare-const a3 (Array Int Int))

(assert (= y1 (+ y0 3)))
(assert (= a1 (store a0 i0 y1)))
(assert (= tmp (select a1 i0)))
(assert (= a2 (store a1 i0 (select a1 j0))))
(assert (= a3 (store a2 j0 tmp)))

(push)
(echo "O valor de a[j] e igual ao valor inicial de y?")
(assert (not (= y0 (select a3 i0))))

(check-sat)
(get-model)
(pop)

(push)
(echo "E verdade que, no final da execucao, o valor de a[i] e igual ao valor inicial de y+3")
(assert (= (select a3 i0) y1))

(check-sat)
(get-model)
(pop)
