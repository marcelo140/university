
void swap(int t[], int i, int j, int start, int end);

/*@ predicate Swap{L1,L2} (int *a, integer i, integer j) =
  @ \at(a[i], L1) == \at(a[j], L2) &&
  @ \at(a[j], L1) == \at(a[i], L2) ;
  @*/

/*@ requires \valid(t+(start..end)) && start <= i <= end && start <= j <= end;
  @ assigns t[i], t[j];
  @ ensures Swap{Old,Here}(t,i,j);
  @*/
void swap(int t[], int i, int j, int start, int end) {
    int tmp = t[i];
    t[i] = t[j];
    t[j] = tmp;
}

/*@ inductive Permuta{L1,L2}(int *a, integer l, integer h) { 
  @  case Permut_refl{L}:  
  @   \forall int *a, integer l, h; Permuta{L,L}(a, l, h) ; 
  @  case Permut_sym{L1,L2}:  
  @    \forall int *a, integer l, h;  
  @      Permuta{L1,L2}(a, l, h) ==> Permuta{L2,L1}(a, l, h) ; 
  @  case Permut_trans{L1,L2,L3}:  
  @    \forall int *a, integer l, h;  
  @      Permuta{L1,L2}(a, l, h) && Permuta{L2,L3}(a, l, h) ==>  
  @        Permuta{L1,L3}(a, l, h) ; 
  @  case Permut_swap{L1,L2}:  
  @    \forall int *a, integer l, h, i, j;  
  @       l <= i <= h && l <= j <= h && Swap{L1,L2}(a, i, j) ==>  
  @     Permuta{L1,L2}(a, l, h) ; 
  @ } 
  @*/ 

/*@ requires 0 <= p <= r && \valid(A+(p..r));
  @ assigns A[p..r];
  @ behavior partition: 
  @ ensures  
  @      p <= \result <= r && 
  @      (\forall int l; p <= l < \result ==> A[l] <= A[\result]) && 
  @      (\forall int l; \result < l <= r ==> A[l] >  A[\result]) && 
  @      A[\result] == \old(A[r]) ;
  @ behavior permutation: 
  @ ensures
  @      Permuta{Old,Here}(A,p,r);
  @*/
int partition (int A[], int p, int r) 
{ 
  int x = A[r]; 
  int j, i = p-1; 

  /*@ loop invariant 
    @   p <= j <= r && p-1 <= i < j;
    @ loop assigns i, j, A[p..r-1];
    @ for partition: 
    @   loop invariant
    @     (\forall int k; (p <= k <= i) ==> A[k] <= x) &&
    @     (\forall int k; (i <  k <  j) ==> A[k] >  x) &&
    @     A[r] == x;
    @ for permutation: 
    @   loop invariant
    @     Permuta{Pre,Here}(A,p,r);
    @ loop variant (r-j);
    @*/
  for (j=p; j<r; j++) 
    if (A[j] <= x) { 
      i++; 
      swap(A,i,j,p,r);
    } 
  swap(A,i+1,r,p,r);
  return i+1; 
}
