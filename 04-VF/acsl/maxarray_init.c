#define LENGTH 100

int vec[LENGTH];


/*@ requires \valid(u+(0..size-1)) && size > 0;
  @ ensures 0 <= \result < size &&
  @     (\forall integer a;
  @         0 <= a < size ==> u[\result] >= u[a]);
  @ assigns \nothing;
  @*/
int maxarray(int u[], int size) {
  int i = 1;
  int max = 0;
  
  /*@ loop invariant 1 <= i <= size && 0 <= max <= size-1 &&
    @   (\forall integer a;
    @       0 <= a < i ==> u[max] >= u[a]);
    @ loop assigns max, i;
    @ loop variant size-i;
    @*/
  while (i < size) {
    if (u[i] > u[max]) { max = i; }
    i++;
  }
  return max;
}




void main() {
  int max = maxarray(vec, LENGTH);

  /*@ assert 0 <= max < LENGTH &&
    @   \forall integer a; 
    @       0 <= a < LENGTH ==> vec[a] <= vec[max];
    @*/
}

