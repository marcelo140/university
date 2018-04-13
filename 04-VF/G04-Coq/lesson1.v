(* ================================================================== *)
Section EX.

Variables (A:Set) (P : A->Prop).
Variable Q:Prop.

(* Check the type of an expression. *)
Check P.

Lemma trivial : forall x:A, P x -> P x.
Proof.
intros.
assumption.
Qed.


(* Prints the definition of an identifier. *)
Print trivial.


Lemma example : forall x:A, (Q -> Q -> P x) -> Q -> P x.
Proof.
intros x H H0.
apply H.
assumption.
assumption.
Qed.

Print example.

End EX.

(* Print trivial.*)

Print example.
(* ================================================================== *)




(* ================================================================== *)
(* ====================== Propositional Logic ======================= *)
(* ================================================================== *)

Section ExamplesPL.

Variables Q P :Prop.

Lemma ex1 : (P -> Q) -> ~Q -> ~P.
Proof.
tauto.
Qed.

Print ex1.

Lemma ex1' : (P -> Q) -> ~Q -> ~P.
Proof.
intros.
intro.
apply H0.
apply H.
assumption.
Qed.

Print ex1'.


Lemma ex2 : P /\ Q -> Q /\ P.
Proof.
intro H.
split.
destruct H as [H1 H2].
exact H2.
destruct H; assumption.
Qed.


Lemma ex3 : P \/ Q -> Q \/ P.
Proof.
intros.
destruct H as [h1 | h2].
right.
assumption.
left; assumption.
Qed.



Theorem ex4 : forall A:Prop, A -> ~~A.
Proof.
intros.
intro.
apply H0. 
exact H.
Qed.

Lemma ex4' : forall A:Prop, A -> ~~A.
Proof.
intros.
red.     (* does only the unfolding of the head of the goal *)
intro.
unfold not in H0.   (* unfold – applies the delta rule for a transparent constant. *)
apply H0; assumption.
Qed.




Axiom double_neg_law : forall A:Prop, ~~A -> A.  (* classical *)

(* CAUTION: Axiom is a global declaration. 
   Even after the section is closed double_neg_law is assume in the enviroment, and can be used. 
   If we want to avoid this we should decalre double_neg_law using the command Hypothesis. 
*)  


Lemma ex5 : (~Q -> ~P) -> P -> Q.   (* this result is only valid classically *)
Proof.
intros.
apply double_neg_law.
intro.
(* apply H; assumption. *)
apply H.
assumption.
assumption.
Qed.


Lemma ex6 : (P\/Q)/\~P -> Q.
Proof.
intros.
destruct H.
destruct H.
contradiction.
assumption.
Qed.


Lemma ex7 : ~(P \/ Q) <-> ~P /\ ~Q.   
Proof.
red.
split.
intros.
split.
unfold not in H.
intro H1.
apply H.
left; assumption.
intro H1.
apply H.
right; assumption.

intros H H1.
destruct H.
destruct H1.
contradiction.
contradiction.
Qed.


Lemma ex7' : ~(P \/ Q) <-> ~P /\ ~Q.
Proof.
tauto.
Qed.



Variable B :Prop.
Variable C :Prop.


(* exercise *)
Lemma ex8 : (P->Q) /\ (B->C) /\ P /\ B -> Q/\C.
Proof.
intros.
destruct H as [H1 H2].
destruct H2 as [H3 H4].
split.
apply H1.
destruct H4 as [H5 H6].
assumption.
apply H3.
destruct H4.
exact H0.
Qed.
 
(* exercise *)
Lemma ex9 : ~ (P /\ ~P).   
Proof.
intro.
destruct H.
contradiction.
Qed.


End ExamplesPL.




(* ================================================================== *)
(* =======================  First-Order Logic ======================= *)
(* ================================================================== *)


Section ExamplesFOL.

Variable X :Set.
Variable t :X.
Variables R W : X -> Prop.

Lemma ex10 : R(t) -> (forall x, R(x) -> ~W(x)) -> ~W(t).
Proof.
intros.
apply H0.
exact H.
Qed.


Lemma ex11 : forall x, R x -> exists x, R x.
Proof.
intros.
exists x.
assumption.
Qed.


Lemma ex11' : forall x, R x -> exists x, R x.
Proof.
firstorder.
Qed.



Lemma ex12 : (exists x, ~(R x)) -> ~ (forall x, R x).
Proof.
intros H H1.
destruct H as [x0 H0].
apply H0.
apply H1.
Qed.






(* Exercise *)
Lemma ex13 : (forall x, R x) \/ (forall x, W x) -> forall x, (R x) \/ (W x).
Proof.
intros.
destruct H.
left.
apply H.
right.
apply H.
Qed.

Variable G : X->X->Prop.



(* Exercise *)
Lemma ex14 : (exists x, exists y, (G x y)) -> exists y, exists x, (G x y).
Proof.
intros.
destruct H. destruct H.
exists x0.
exists x.
assumption.
Qed.




(* Exercise *)
Proposition ex15: (forall x, W x)/\(forall x, R x) -> (forall x, W x /\ R x).
Proof.
intros.
destruct H.
split.
apply H.
apply H0.
Qed.






(* ------- Note that we can have nested sections ----------- *)
Section Relations.       

Variable D : Set.
Variable Rel : D->D->Prop.

Hypothesis R_symmetric : forall x y:D, Rel x y -> Rel y x.
Hypothesis R_transitive : forall x y z:D, Rel x y -> Rel y z -> Rel x z.


Lemma refl_if : forall x:D, (exists y, Rel x y) -> Rel x x.
intros.
destruct H.
(* try "apply R_transitive" to see de error message *)
apply R_transitive with x0. 
assumption.
apply R_symmetric.
assumption.
Qed.

Check refl_if.

End Relations.

Check refl_if. (* Note the difference after the end of the section Relations. *)





(* ====== OTHER USES OF AXIOMS ====== *)

(* --- A stack abstract data type --- *)
Section Stack.

Variable U:Type.

Parameter stack : Type -> Type.
Parameter emptyS : stack U. 
Parameter push : U -> stack U -> stack U.
Parameter pop : stack U -> stack U.
Parameter top : stack U -> U.
Parameter isEmpty : stack U -> Prop.

Axiom emptyS_isEmpty : isEmpty emptyS.
Axiom push_notEmpty : forall x s, ~isEmpty (push x s).
Axiom pop_push : forall x s, pop (push x s) = s.
Axiom top_push : forall x s, top (push x s) = x.



(* Exercício *)
Lemma exstack : 
forall s  x y, top (pop (push x (push y s))) =  y.
Proof.
intros.
rewrite pop_push.
rewrite top_push.
trivial.
Qed.



End Stack.

Check pop_push.

(* Now we can make use of stacks in our formalisation!!! *)


Lemma exstack2 : 
forall T s  x y, top T (pop T (push T x (push T y s))) =  y.
intros.
rewrite pop_push.
apply top_push.
Qed.





(* A NOTE OF CAUTION!!! *)
(* The capability to extend the underlying theory with arbitary axiom 
   is a powerful but dangerous mechanism. We must avoid inconsistency. 
*)
Section Caution.

Check False_ind.

Hypothesis ABSURD : False.

Theorem oops : forall (P:Prop), P /\ ~P.
tauto.
Qed.

End Caution. (* We have declared ABSURD as an hypothesis to avoid its use outside this section. *)



