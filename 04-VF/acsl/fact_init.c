// Note axiomatization with integer type, rather than int. 


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
  @ ensures fact(n) == \result;
  @ assigns \nothing;
  @*/
int factf (int n)
{
  int f = 1; 
  int i = 1 ; 
  
  /*@ loop invariant 1 <= i <= n+1 && f == fact(i-1);
    @ loop assigns i, f;
    @ loop variant n-i;
    @*/
  while (i <= n) {
    f = f * i; 
    i = i + 1;
  }
  return f;
}

