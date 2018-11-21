/*@ inductive isfact(integer n, integer r) { 
  @  case isfact0:  
  @    isfact(0,1); 
  @  case isfactn:  
  @    \forall integer n, integer f;
  @    isfact(n-1,f) ==> isfact(n,f*n);
  @ } 
  @*/ 


/*@ axiomatic factorial {
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
    /*@ loop assigns i, factable[0..size-1];
      @ loop invariant 0 <= i <= size && 
      @     (\forall integer a; 0 <= a < i ==> factable[a] == fact(a));
      @ loop variant size-i;
     */
    for(int i = 0; i < size; i++) {
        factable[i] = factf(i);
    }
}




/*@ requires \valid(factable+(0..size-1)) && size>0;
  @ assigns  factable[0..size-1];
  @ ensures 
  @    \forall integer a ; 0 <= a < size ==>  factable[a] == fact (a);
  @*/
void factab_efficient (int factable[], int size)
{
    /*@ loop assigns i, factable[0..size-1];
      @ loop invariant 0 <= i <= size && 
      @     (\forall integer a; 0 <= a < i ==> factable[a] == fact(a));
      @ loop variant size-i;
     */
    for(int i = 0; i < size; i++) {
        factable[i] = factable[i-1] * i;
    }
}
