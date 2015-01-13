/**
 * Базовый класс
 */
public class init {
    public static void main(String args[]) {
        // Умолчания: число потоков и размер матриц
        int threads = 1;
        int size = 100;

        // Парсинг переданных параметров
        for (int i = 0; i < args.length; i++) {
            String item = args[i].toLowerCase();
            if (item.endsWith("-threads") || item.endsWith("-t")) {
                if ((i + 1) < args.length) {
                    try {
                        threads = Integer.parseInt(args[i+1]);
                    } catch (Exception exc) {
                        System.out.println("Invalid number format. Threads count set to 1");
                    }
                }
            }
            if (item.endsWith("-size") || item.endsWith("-s")) {
                if ((i + 1) < args.length) {
                    try {
                        size = Integer.parseInt(args[i+1]);
                    } catch (Exception exc) {
                        System.out.println("Invalid number format. Matrix size set to 100");
                    }
                }
            }
            if (item.endsWith("-help") || item.endsWith("-h")) {
                System.out.println(
                        "Lousy benchmark v.0.3a\n"
                        + "Very stupid and very lousy\n"
                        + "Avaliable keys:\n"
                        + "--help or -h -- this help, srsly\n"
                        + "--size or -s -- matrix sizes\n"
                        + "--threads or -t -- threads count. All threads independent and calculate it's own matrix\n"
                        + "This benchmark:\n"
                        + "1. Calculate integer matrix (with size -s)\n"
                        + "2. Same with real matrix\n"
                        + "3. Same with math equals (cos, sin, tan, exp etc)\n"
                        + "4. Mark start and stop calculation\n"
                        + "5. Print average time in nanos"
                );
		System.exit(0);
            }
        }

        // Заводим статистика
        statistic stat = new statistic(threads);
        long start, stop;

        // Создаём потоки согласно настройкам
        // Заодно запоминаем общее время запуска и исполнения
        start = System.nanoTime();
        for (int i = 1; i<= threads; i++) {
            new mathThread(size, stat);
        }

        // Ожидаем завершения
        try {
            while (!stat.ready())
                Thread.sleep(100);
        } catch (InterruptedException exc) {
            System.exit(0);
        }

        stop = System.nanoTime();

        // Проводим рассчёты
        long commonTime = stop - start;
        stat.calculateMiddleData();

        // И выводим результаты
        System.out.println("Stats:");
        System.out.println("Average integer equals: " + stat.getMiddleNumbers() + " ns.");
        System.out.println("Average real equals: " + stat.getMiddleLarges() + " ns.");
        System.out.println("Average maths equals: " + stat.getMiddleMaths() + " ns.");
        System.out.println("Common time: " + commonTime + " ns.");
        System.out.println("With matrix sizes: " + size + ", and threads count: " + threads);
        System.out.println("With CPU core/thread numbers: " + Runtime.getRuntime().availableProcessors());
    }
}
