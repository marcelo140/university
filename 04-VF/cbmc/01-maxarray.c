#define LENGTH 2
int vec[LENGTH];

int maxarray(int u[], int size) {
  int i = 1, max = 0;

  /* __CPROVER_assume (size > 0); */

  while (i < size) {
    if (u[i] > u[max]) { max = i; }
    i++;
  }

  /* assert(0 <= max && max < size); */
  return max;
}

int main() {
  for (int i = 0; i < LENGTH; i++) {
    vec[i] = i;
  }

  int r = maxarray(vec, LENGTH);

  /* for(int i = 0; i < LENGTH; i++) */
  /*     assert(vec[i] <= vec[r]); */

  int k = nondet_int();
  __CPROVER_assume(k >= 0 && k < LENGTH);

  assert(vec[k] <= vec[r]);

  return 0;
}
