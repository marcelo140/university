#define LENGTH 100

int vec[LENGTH];
int max;

/*@ requires \valid(u+(0..size-1)) && 0 < size;
  @ ensures 0 <= \result < size &&
  @     (\forall integer a; 0 <= a < size ==> u[a] <= u[\result]);
  @ assigns max; 
  @*/
int maxarray(int u[], int size) {
    int i = 1;
    max = 0;

    /*@ loop invariant
      @     0 <= max <  i <= size &&
      @     (\forall integer a;
      @         0 <= a < i ==> u[a] <= u[max]);
      @ loop assigns i, max;
      @ loop variant size-i;
      @*/
    while (i < size) {
        if (u[i] > u[max]) { max = i; }
        i++;
    }
    return max;
}

void main() {
    max = maxarray(vec, LENGTH);

    /*@ assert 0 <= max < LENGTH &&
      @     (\forall integer a; 
      @         0 <= a < LENGTH ==> vec[a] <= vec[max]);
      @*/
}

