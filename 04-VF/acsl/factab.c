
/* inductive isfact(integer n, integer r) { 
  @  case isfact0:  
  @    isfact(0,1); 
  @  case isfactn:  
  @    \forall integer n, integer f;
  @    isfact(n-1,f) ==> isfact(n,f*n);
  @ } 
  @*/ 


/* axiomatic factorial {
  @
  @ logic integer fact (integer n);
  @
  @ axiom fact1: 
  @ \forall integer n; isfact (n,fact(n));
  @
  @ axiom fact2: 
  @ \forall integer n, integer f; isfact (n,f) ==> f==fact(n);
  @ }
  @
  @*/ 




/*@ logic integer fact (integer n) = 
  @ (n == 0) ? 1 : n * fact(n-1);
  @ 
  @*/ 






/*@ requires n >= 0;
  @ ensures \result == fact(n);
  @ assigns \nothing;
  @*/
int factf (int n);




/*@ requires \valid(factable+(0..size-1)) && size>0;
  @ assigns  factable[0..size-1];
  @ ensures 
  @    \forall integer a ; 0 <= a < size ==>  factable[a] == fact (a);
  @*/
void factab (int factable[], int size)
{
  int k = 0 ; 

  /*@ loop invariant 0 <= k <= size && 
    @      (\forall integer a ; 0 <= a < k ==>  factable[a] == fact (a));
    @ loop assigns k, factable[0..size-1];
    @ loop variant size-k;
    @*/
  while (k < size) {
    factable[k] = factf(k) ; 
    k++;
  }
}








/*@ requires \valid(factable+(0..size-1)) && size>0;
  @ assigns  factable[0..size-1];
  @ ensures 
  @    \forall integer a ; 0 <= a < size ==>  factable[a] == fact (a);
  @*/
void factab_efficient (int factable[], int size)
{
  int k;

  factable[0] = 1;

  /*@ loop invariant 1 <= k <= size && 
    @      (\forall integer a ; 0 <= a < k ==>  factable[a] == fact (a));
    @ loop assigns k, factable[1..size-1];
    @ loop variant size-k;
    @*/  
  for (k=1; k < size; k++) 
    factable[k] = factable[k-1]*k ; 

}




