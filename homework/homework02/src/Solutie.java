import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ThreadSumaVarsteiStudentiPromovati extends Thread {
        private final Student[] listaStudenti;
        private final int begin;
        private final int step;

        private double sumaVarstei;
        private long numarStudenti;
        private long dbg;

        public ThreadSumaVarsteiStudentiPromovati(Student[] listaStudenti, int begin, int step) {
                this.listaStudenti = listaStudenti;
                this.begin = begin;
                this.step = step;
                this.sumaVarstei = 0;
                this.numarStudenti = 0;
        }

        @Override
        public void run() {
                this.sumaVarstei = 0;
                this.numarStudenti = 0;

                for (int i = this.begin; i < this.listaStudenti.length; i += this.step) {
                        if (this.listaStudenti[i].verificaEsteInregistrat()) {
                                this.sumaVarstei += this.listaStudenti[i].getVarsta();
                                this.numarStudenti += 1;
                        }
                }
        }

        public double getSumaVarstei() {
                return this.sumaVarstei;
        }

        public long getNumarStudenti() {
                return this.numarStudenti;
        }

        public long getDbg() {
                return this.dbg;
        }
}

public final class Solutie {
        private static int getNrProcesoare() {
                return Runtime.getRuntime().availableProcessors();
        }

        /**
         * @Task 1.
         * @Brief: Calculeaza secvential media varstei studentilor
         *         care sunt inregistrati pentru curs
         * 
         * @param listaStudenti datele tuturor studentilor
         * @return varsta medie a studentilor inregistrati la acest curs
         */
        public double calculSecventialMediaVarsteiStudentilorInregistrati(final Student[] listaStudenti) {
                double suma = 0;
                long nrStudentiInregistrati = 0;
                for (var student : listaStudenti) {
                        if (student.verificaEsteInregistrat()) {
                                suma += student.getVarsta();
                                nrStudentiInregistrati += 1;
                        }
                }
                return suma / nrStudentiInregistrati;
        }

        /**
         * @Task 2.
         * @Brief: Calcul paralel pentru media varstei studentilor
         *         care sunt inregistrati pentru curs
         *
         * @param listaStudenti datele tuturor studentilor
         * @return varsta medie a studentilor inregistrati la acest curs
         */
        public double calculParalel_MediaVarsteiStudentilorInregistrati(final Student[] listaStudenti) {
                final int NUMAR_PROCESOARE = Solutie.getNrProcesoare();

                List<ThreadSumaVarsteiStudentiPromovati> threads = new ArrayList<>();
                for (int i = 0; i < NUMAR_PROCESOARE; ++i) {
                        threads.add(new ThreadSumaVarsteiStudentiPromovati(listaStudenti, i, NUMAR_PROCESOARE));
                }

                for (var thread : threads) {
                        thread.start();
                }

                double sumaTotala = 0;
                long numarTotal = 0;
                for (var thread : threads) {
                        try {
                                thread.join();
                                sumaTotala += thread.getSumaVarstei();
                                numarTotal += thread.getNumarStudenti();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }

                return sumaTotala / numarTotal;
        }

        /**
         * @Task 3.
         * @Brief: Calcul secvential al celui mai comun prenume pentru studentii
         *         care nu sunt inregistrati la curs
         * 
         * @param studentArray datele tuturor studentilor
         * @return cel mai comun prenume
         */
        public String calculSecvential_CelMaiComunPrenumePentruStudentiiNeinregistrati(final Student[] studentArray) {
                Map<String, Integer> names = new HashMap<>();

                for (var student : studentArray) {
                        Integer count = names.getOrDefault(student.getNume(), -1);
                        names.put(student.getNume(), count + 1);
                }

                String name = "";
                Integer max = Integer.MAX_VALUE;
                for (var entry : names.entrySet()) {
                        if (entry.getValue() > max) {
                                max = entry.getValue();
                                name = entry.getKey();
                        }
                }

                return name;
        }

        /**
         * @Task 4.
         * @Brief: Calcul paralel al celui mai comun prenume pentru studentii
         *         care nu sunt inregistrati la curs
         * 
         * @param listaStudenti datele tuturor studentilor
         * @return cel mai comun prenume
         */
        public String calculParalel_CelMaiComunPrenumePentruStudentiiNeinregistrati(final Student[] listaStudenti) {

                return "";
        }

        /**
         * @Task 5.
         * @Brief: Determina secvential numarul studentilor
         *         - care au picat examenul si
         *         - care au o varsta > 20 ani.
         *         Un student a picat examenul daca are o nota < 5
         *
         * @param listaStudenti Student data for the class.
         * @return Number of failed grades from students older than 20 years old.
         */
        public int calculSecvential_NumarulStudentiNepromovatiCuVarstaPeste20(final Student[] listaStudenti) {
                int numar = 0;
                for (var student : listaStudenti) {
                        if (student.getNota() < 5 && student.getVarsta() > 20) {
                                numar += 1;
                        }
                }
                return numar;
        }

        /**
         * @Task 6.
         * @Brief: Determina paralel numarul studentilor
         *         - care au picat examenul si
         *         - care au o varsta > 20 ani.
         *         Un student a picat examenul daca are o nota < 5
         * 
         *         Trebuie utilizate cel putin 2 thread-uri
         *
         * @param studentArray Student data for the class.
         * @return Number of failed grades from students older than 20 years old.
         */
        public int calculParalel_NumarulStudentiNepromovatiCuVarstaPeste20(final Student[] studentArray) {
                return 0;
        }
}
