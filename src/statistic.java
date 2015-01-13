import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * Рассчёт статистики - средних значений прогона
 */
public class statistic implements Observer {
    // Значения от каждого потока-нагрузки
    private LinkedList <Long> Larges = new LinkedList<Long>();
    private LinkedList <Long> Numbers = new LinkedList<Long>();
    private LinkedList <Long> Maths = new LinkedList<Long>();
    // Статистические значения
    private long middleLarges = 0;
    private long middleNumbers = 0;
    private long middleMaths = 0;
    // Ожидаемое число результатов
    private int awaitCount;
    // Число полученных результатов
    private int receivedCount = 0;

    /**
     * Инициализация статистики
     * @param awaitCount    Ожидаемое число результатов
     */
    public statistic(int awaitCount) { this.awaitCount = awaitCount; }

    /**
     * Добавление в статистику
     * @param mathTr    Объект типа mathThread, поток
     * @param result    Null
     */
    synchronized public void update(Observable mathTr, Object result) {
        Larges.add(((mathThread) mathTr).getLargesResult());
        Numbers.add(((mathThread) mathTr).getNumbersResult());
        Maths.add(((mathThread) mathTr).getMathResult());
        receivedCount++;
    }

    /**
     * Готовность полученных данных
     * Необходим для обхода неожиданных ситуаций, когда
     * ожидали завершения не всех потоков из группы.
     * @return  Получены ли данные со всех потоков
     */
    synchronized public boolean ready() { return (awaitCount == receivedCount);}

    /**
     * Рассчёт средней временной статистики по времени исполнения потоков
     */
    synchronized public void calculateMiddleData() {
        // Подсчёт среднего по вещественным
        middleLarges = 0;
        for (Long item : Larges)
            middleLarges += item;
        if (Larges.size() > 0)
            middleLarges /= Larges.size();

        // Подсчёт среднего по целочисленным
        middleNumbers = 0;
        for (Long item : Numbers)
            middleNumbers += item;
        if (Numbers.size() > 0)
            middleNumbers /= Numbers.size();

        // Подсчёт среднего по мат.функциям
        middleMaths = 0;
        for (Long item : Maths)
            middleMaths += item;
        if (Maths.size() > 0)
            middleMaths /= Maths.size();
    }

    /**
     * Получение среднего значения рассчёта матриц с вещественными данными
     * @return  Среднее значение
     */
    synchronized public long getMiddleLarges() { return middleLarges; }

    /**
     * Получение среднего значения рассчёта матриц с целочисленными данными
     * @return  Среднее значение
     */
    synchronized public long getMiddleNumbers() { return middleNumbers; }

    /**
     * Получение среднего значение рассчёта мате
     * @return  Среднее значение
     */
    synchronized public long getMiddleMaths() { return middleMaths; }
}
