#include <stdio.h>

void gone() {
  int a[2];
  a[3] = 'x'; // It writes past buffer, likely onto return address
}

int main() {
  gone();	    // likely never comes back
  printf("done\n");
}

