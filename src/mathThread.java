import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Поток-нагрузка
 * Производит перемножение вещественных и целочисленных матриц
 * размерности N (передаётся параметром --size|-s)
 * и сохраняет время вычисления каждой матрицы
 */
public class mathThread extends Observable implements Runnable {
    // Размер матрицы, по-умолчанию 100
    private int size;
    // Время рассчёта
    private long N = 0;
    private long D = 0;
    private long M = 0;

    /**
     * Инициализация потока-нагрузки
     * Поток запускается сразу после инициализации
     * @param size          Размер матриц
     * @param stat          Класс-статистика
     */
    public mathThread(int size, Observer stat) {
        this.size = size;
        this.addObserver(stat);
        new Thread(this).start();
    }

    /**
     * Получение результата умножения целочисленной матрицы
     * @return  Значение
     */
    public long getNumbersResult() { return N; }

    /**
     * Получение значения умножения вещественной матрицы
     * @return  Значение
     */
    public long getLargesResult() { return D; }

    /**
     * Получение значения вычисления математических функций
     * @return  Значение
     */
    public long getMathResult() { return M; }

    /**
     * Непосредственные рассчёты скорости умножения
     */
    public void run() {
        long Number1[][], Number2[][];
        double Large1[][], Large2[][];
        try {
            Random rndNum = new Random();
            // Целочисленные массивы
            Number1 = new long[size][size];
            Number2 = new long[size][size];
            // Вещественные массивы
            Large1 = new double[size][size];
            Large2 = new double[size][size];
            // Заполнение рандомом
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Number1[i][j] = rndNum.nextInt();
                    Number2[i][j] = rndNum.nextInt();
                    Large1[i][j] = rndNum.nextDouble();
                    Large2[i][j] = rndNum.nextDouble();
                }
            }
            long start, stop;

            // Перемножение целочисленных матриц
            // Засекаем время
            start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Number1[i][j] *= Number2[i][j];
                }
            }
            stop = System.nanoTime();
            N = stop - start;

            // Перемножение вещественных матриц
            start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Large1[i][j] *= Large2[i][j];
                }
            }
            stop = System.nanoTime();
            D = stop - start;

            // Математические рассчёты из библиотеки Math
            // Взял все подряд для double, до которых дотянулись мои
            // грязные ручонки
            start = System.nanoTime();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    // Тригонометрические функции
                    Large1[i][j] = Math.sin(Large2[i][j]);
                    Large1[i][j] = Math.cos(Large2[i][j]);
                    Large1[i][j] = Math.tan(Large2[i][j]);
                    Large1[i][j] = Math.asin(Large1[i][j]);
                    Large1[i][j] = Math.acos(Large1[i][j]);
                    Large1[i][j] = Math.atan(Large1[i][j]);
                    // Логарифмы
                    Large1[i][j] = Math.log(Large2[i][j]);
                    Large1[i][j] = Math.log10(Large2[i][j]);
                    Large1[i][j] = Math.log1p(Large2[i][j]);
                    // Квадратные и кубические корни
                    Large1[i][j] = Math.sqrt(Large2[i][j]);
                    Large1[i][j] = Math.cbrt(Large2[i][j]);
                    // Степень
                    Large1[i][j] = Math.pow(Large1[i][j], Large2[i][j]);
                    // Экспонента
                    Large1[i][j] = Math.exp(Large2[i][j]);
                    Large1[i][j] = Math.expm1(Large2[i][j]);
                    System.out.print("");
                }
            }
            stop = System.nanoTime();
            M = stop - start;

            // Уведомляет статистику
            setChanged();
            notifyObservers();
        } catch (OutOfMemoryError exc) {
            System.out.println("Zomg! Out of memory.");
            System.out.println("Try use: -Xmx parameter to set more memory (or diminish matrix size/threads count)");
            System.exit(1);
        }
    }
}
