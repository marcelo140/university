/*@ predicate sorted(int *t,integer i,integer j) =
  @   \forall integer k, integer l; i <= k < l <= j ==> t[k] <= t[l];
  @*/

/*@ requires N >= 1 && \valid(A+(0..N-1));
  @ assigns A[0..N-1];
  @ ensures sorted(A,0,N-1);
  @*/
void insertionSort(int A[], int N) {
    int i, j, key;

    /*@ loop invariant 
      @     1 <= j <= N && sorted(A, 0, j-1);
      @ loop assigns
      @     j, key, i, A[0..N-1];
      @ loop variant
      @     N-j;
      @*/
    for (j=1 ; j<N ; j++) {
        key = A[j];
        i = j-1;

        /*@ loop invariant -1 <= i <= j-1;
          @ loop invariant i == j-1 ==> sorted(A, 0, j-1);
          @ loop invariant i < j-1 ==> sorted(A, 0, j);
          @ loop invariant \forall int k; i < k < j ==> A[k] > key;
          @ loop assigns i, A[1..j];
          @ loop variant i;
          @*/
        while (i>=0 && A[i] > key) {
            A[i+1] = A[i];
            i--;
        }

        A[i+1] = key;
    }
}
