import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Student Delegeanu Alexandru-Gabriel
 * @Grupa 1146
 */

class ThreadSumaVarsteiStudentiPromovati extends Thread {
        private final Student[] listaStudenti;
        private final int begin;
        private final int end;

        private double sumaVarstei;
        private long numarStudenti;

        public ThreadSumaVarsteiStudentiPromovati(Student[] listaStudenti, int begin, int end) {
                this.listaStudenti = listaStudenti;
                this.begin = begin;
                this.end = end;
                this.sumaVarstei = 0;
                this.numarStudenti = 0;
        }

        @Override
        public void run() {
                this.sumaVarstei = 0;
                this.numarStudenti = 0;

                for (int i = this.begin; i < this.end; i += 1) {
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

}

class ThreadNumaraNumeleStudentilor extends Thread {
        private final Student[] listaStudenti;
        private final int begin;
        private final int end;

        private Map<String, Integer> numarAparitiiNume;

        public ThreadNumaraNumeleStudentilor(Student[] listaStudenti, int begin, int end) {
                this.listaStudenti = listaStudenti;
                this.begin = begin;
                this.end = end;

                this.numarAparitiiNume = new HashMap<>();
        }

        @Override
        public void run() {
                this.numarAparitiiNume = new HashMap<>();

                for (int i = this.begin; i < this.end; i += 1) {
                        Integer count = this.numarAparitiiNume.getOrDefault(this.listaStudenti[i].getNume(), -1);
                        this.numarAparitiiNume.put(this.listaStudenti[i].getNume(), count + 1);
                }
        }

        public Map<String, Integer> getNumarAparitiiNume() {
                return this.numarAparitiiNume;
        }
}

class ThreadNumaraStudenti extends Thread {
        private final Student[] listaStudenti;
        private final int begin;
        private final int end;

        private int numarStudenti;

        public ThreadNumaraStudenti(Student[] listaStudenti, int begin, int end) {
                this.listaStudenti = listaStudenti;
                this.begin = begin;
                this.end = end;
                this.numarStudenti = 0;
        }

        @Override
        public void run() {
                this.numarStudenti = 0;

                for (int i = this.begin; i < this.end; i += 1) {
                        if (this.listaStudenti[i].getNota() < 5 && this.listaStudenti[i].getVarsta() > 20) {
                                this.numarStudenti += 1;
                        }
                }
        }

        public int getNumarStudenti() {
                return this.numarStudenti;
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
                final int CHUNK_SIZE = listaStudenti.length / NUMAR_PROCESOARE;

                List<ThreadSumaVarsteiStudentiPromovati> threads = new ArrayList<>();
                for (int i = 0; i < NUMAR_PROCESOARE; ++i) {
                        int begin = i * CHUNK_SIZE;
                        int end = i == (NUMAR_PROCESOARE - 1) ? listaStudenti.length : (i + 1) * CHUNK_SIZE;

                        threads.add(new ThreadSumaVarsteiStudentiPromovati(listaStudenti, begin, end));
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
                final int NUMAR_PROCESOARE = Solutie.getNrProcesoare();
                final int CHUNK_SIZE = listaStudenti.length / NUMAR_PROCESOARE;

                List<ThreadNumaraNumeleStudentilor> threads = new ArrayList<>();
                for (int i = 0; i < NUMAR_PROCESOARE; ++i) {
                        int begin = i * CHUNK_SIZE;
                        int end = i == (NUMAR_PROCESOARE - 1) ? listaStudenti.length : (i + 1) * CHUNK_SIZE;

                        threads.add(new ThreadNumaraNumeleStudentilor(listaStudenti, begin, end));
                }

                for (var thread : threads) {
                        thread.start();
                }

                Map<String, Integer> mergedMaps = new HashMap<>();
                for (var thread : threads) {
                        try {
                                thread.join();
                                mergedMaps.putAll(thread.getNumarAparitiiNume());
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }

                String name = "";
                Integer max = Integer.MAX_VALUE;
                for (var entry : mergedMaps.entrySet()) {
                        if (entry.getValue() > max) {
                                max = entry.getValue();
                                name = entry.getKey();
                        }
                }

                return name;
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
                final int NUMAR_PROCESOARE = Solutie.getNrProcesoare();
                final int CHUNK_SIZE = studentArray.length / NUMAR_PROCESOARE;

                List<ThreadNumaraStudenti> threads = new ArrayList<>();
                for (int i = 0; i < NUMAR_PROCESOARE; ++i) {
                        int begin = i * CHUNK_SIZE;
                        int end = i == (NUMAR_PROCESOARE - 1) ? studentArray.length : (i + 1) * CHUNK_SIZE;

                        threads.add(new ThreadNumaraStudenti(studentArray, begin, end));
                }

                for (var thread : threads) {
                        thread.start();
                }

                int numarTotal = 0;
                for (var thread : threads) {
                        try {
                                thread.join();
                                numarTotal += thread.getNumarStudenti();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }

                return numarTotal;
        }
}
