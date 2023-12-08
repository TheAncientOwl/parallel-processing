- Pornind de la exercitiul dat sa se implementeze urmatoarele solutii paralele pentru problema determinarii minimului unui vector (solutia secventiala propusa dureaza aprox 40s).

1. o solutie care sa foloseasca thread-uri din libraria C++ 11 thread. Solutia poate avea orice abordare

2. o solutie care sa foloseasca directiva #pragma omp parallel din OMP si care sa foloseasca o variabila shared pentru a obtine minimul (fara a avea race condition)

3. o solutie care sa foloseasca directiva #pragma omp parallel din OMP si care sa foloseasca varaibile private pe fiecare thread

- Modul cum se face load balancing-ul ramane la latitudinea autorului.

- Se va incarca un fisier .cpp ce contine solutia

- Soluțiile vor fi verificate încrucișat cu MOSS de la Stanford. Soluțiile care au un grad de similaritate de cel putin 30% vor fi anulate.

- Solutiile care contin erori de compilare specifice bibliotecii OMP nu vor fi luate in considerare
