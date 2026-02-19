package main.utils;

import main.model.*;

import java.util.*;

public class Statistics {

    // ============= ОСНОВНЫЕ ДАННЫЕ =============
    private final List<Soul> allSouls = new ArrayList<>();
    private final List<Soul> completedSouls = new ArrayList<>();
    private final List<Soul> rejectedSouls = new ArrayList<>();

    private final Map<Integer, Integer> sourceGenerated = new HashMap<>();
    private final Map<Integer, Integer> sourceRejected = new HashMap<>();
    private final Map<Integer, Integer> sourceCompleted = new HashMap<>();

    // ============= ДАННЫЕ ДЛЯ ГРАФИКОВ =============
    private final List<Double> timePoints = new ArrayList<>();

    // 1️⃣ Процент отказов (общий и по источникам)
    private final List<Double> rejectionRateHistory = new ArrayList<>();
    private final List<Double> rejectionRateSource1 = new ArrayList<>();
    private final List<Double> rejectionRateSource2 = new ArrayList<>();
    private final List<Double> rejectionRateSource3 = new ArrayList<>();

    private int totalArrived = 0;
    private int totalRejected = 0;
    private int arrived1 = 0, arrived2 = 0, arrived3 = 0;
    private int rejected1 = 0, rejected2 = 0, rejected3 = 0;

    // 2️⃣ Загрузка Харонов
    private final List<Double> charonLoadHistory = new ArrayList<>();
    private final List<Charon> charons;
    private final int charonCount;

    // 3️⃣ Заполненность буфера
    private final List<Double> bufferUsageHistory = new ArrayList<>();
    private final Buffer buffer;
    private final int bufferCapacity;

    // ============= ДАННЫЕ ДЛЯ ОЧЕРЕДИ =============
    private final List<Double> queueLengthHistory = new ArrayList<>();
    private final List<Double> queueTimeHistory = new ArrayList<>();
    private double totalBusyTime = 0.0;
    private double lastTime = 0.0;
    private int currentQueueLength = 0;

    public Statistics(List<Charon> charons, int charonCount, Buffer buffer, int bufferCapacity) {
        this.charons = charons;
        this.charonCount = charonCount;
        this.buffer = buffer;
        this.bufferCapacity = bufferCapacity;
    }

    // ============= ОБНОВЛЕНИЕ МЕТРИК =============
    private void updateAllMetrics(double time) {
        if (timePoints.isEmpty() || timePoints.getLast() < time) {
            timePoints.add(time);
        }

        // 1️⃣ Общий процент отказов
        rejectionRateHistory.add(totalArrived > 0 ? (100.0 * totalRejected / totalArrived) : 0);

        // 2️⃣ Процент отказов по источникам
        rejectionRateSource1.add(arrived1 > 0 ? (100.0 * rejected1 / arrived1) : 0);
        rejectionRateSource2.add(arrived2 > 0 ? (100.0 * rejected2 / arrived2) : 0);
        rejectionRateSource3.add(arrived3 > 0 ? (100.0 * rejected3 / arrived3) : 0);

        // 3️⃣ Загрузка Харонов
        charonLoadHistory.add(calculateCharonLoad());

        // 4️⃣ Заполненность буфера
        bufferUsageHistory.add(buffer != null ? (buffer.getCurrentSize() / (double) bufferCapacity) * 100 : 0);
    }

    private double calculateCharonLoad() {
        if (charons.isEmpty() || charonCount == 0) return 0;
        int busy = 0;
        for (Charon c : charons) if (c.isBusy()) busy++;
        return (busy / (double) charonCount) * 100;
    }

    // ============= ГЕТТЕРЫ ДЛЯ ГРАФИКОВ =============
    public List<Double> getTimePoints() { return new ArrayList<>(timePoints); }
    public List<Double> getRejectionRateSource1() { return new ArrayList<>(rejectionRateSource1); }
    public List<Double> getRejectionRateSource2() { return new ArrayList<>(rejectionRateSource2); }
    public List<Double> getRejectionRateSource3() { return new ArrayList<>(rejectionRateSource3); }
    public List<Double> getCharonLoadHistory() { return new ArrayList<>(charonLoadHistory); }
    public List<Double> getBufferUsageHistory() { return new ArrayList<>(bufferUsageHistory); }

    // ============= ФИНАЛЬНЫЙ ОТЧЕТ =============
    public void printFinalReport(double totalTime) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 ФИНАЛЬНЫЙ СТАТИСТИЧЕСКИЙ ОТЧЕТ");
        System.out.println("=".repeat(60));

        int totalGenerated = allSouls.size();
        int totalCompleted = completedSouls.size();
        int totalRejected = rejectedSouls.size();

        System.out.printf("\n📈 ОБЩАЯ СТАТИСТИКА:\n");
        System.out.printf("   Время симуляции: %.3f с\n", totalTime);
        System.out.printf("   Всего сгенерировано: %d душ\n", totalGenerated);
        System.out.printf("   Обслужено: %d (%.2f%%)\n", totalCompleted,
                totalGenerated > 0 ? 100.0 * totalCompleted / totalGenerated : 0);
        System.out.printf("   Отказано: %d (%.2f%%)\n", totalRejected,
                totalGenerated > 0 ? 100.0 * totalRejected / totalGenerated : 0);
        System.out.printf("   Загрузка Харонов: %.1f%%\n", calculateCharonLoad());
        System.out.printf("   Заполненность буфера: %.1f%%\n",
                buffer != null ? (buffer.getCurrentSize() / (double) bufferCapacity) * 100 : 0);

        System.out.printf("\n🎯 СТАТИСТИКА ПО ИСТОЧНИКАМ:\n");
        System.out.printf("   %-10s %-12s %-12s %-12s %-12s\n",
                "Источник", "Сгенер.", "Обслуж.", "Отказ.", "Pотказа");

        for (int id = 1; id <= 3; id++) {
            int gen = sourceGenerated.getOrDefault(id, 0);
            int comp = sourceCompleted.getOrDefault(id, 0);
            int rej = sourceRejected.getOrDefault(id, 0);
            System.out.printf("   %-10s %-12d %-12d %-12d %.2f%%\n",
                    "Source " + id, gen, comp, rej, gen > 0 ? 100.0 * rej / gen : 0);
        }

        double avgQueueTime = queueTimeHistory.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double maxQueueTime = queueTimeHistory.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double avgSystemTime = completedSouls.stream().mapToDouble(Soul::getTimeInSystem).average().orElse(0);

        System.out.printf("\n⏱️ ВРЕМЕННЫЕ ХАРАКТЕРИСТИКИ:\n");
        System.out.printf("   Среднее время в очереди: %.3f с\n", avgQueueTime);
        System.out.printf("   Макс. время в очереди: %.3f с\n", maxQueueTime);
        System.out.printf("   Среднее время в системе: %.3f с\n", avgSystemTime);

        double avgQueueLength = totalTime > 0 ? totalBusyTime / totalTime : 0;
        double maxQueueLength = queueLengthHistory.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        System.out.printf("\n📊 ХАРАКТЕРИСТИКИ ОЧЕРЕДИ:\n");
        System.out.printf("   Средняя длина очереди: %.3f\n", avgQueueLength);
        System.out.printf("   Макс. длина очереди: %.0f\n", maxQueueLength);
    }

    // ============= МЕТОДЫ ДЛЯ РЕГИСТРАЦИИ СОБЫТИЙ =============

    /**
     * Обновление статистики перед событием (для очереди)
     */
    public void updateBeforeEvent(double currentTime, Buffer buffer, List<Charon> charons) {
        // Обновляем время занятости очереди
        if (lastTime > 0 && currentTime > lastTime) {
            double timeDelta = currentTime - lastTime;
            totalBusyTime += currentQueueLength * timeDelta;
        }

        // Текущая длина очереди
        currentQueueLength = buffer != null ? buffer.getCurrentSize() : 0;

        // Сохраняем историю
        if (timePoints.isEmpty() || timePoints.get(timePoints.size() - 1) < currentTime) {
            timePoints.add(currentTime);
            queueLengthHistory.add((double) currentQueueLength);
            bufferUsageHistory.add(buffer != null ?
                    (buffer.getCurrentSize() / (double) buffer.getCapacity()) * 100 : 0);

            // Загрузка Харонов
            int busy = 0;
            for (Charon c : charons) if (c.isBusy()) busy++;
            charonLoadHistory.add(charons.size() > 0 ? (busy / (double) charons.size()) * 100 : 0);
        }

        lastTime = currentTime;
    }

    /**
     * Обновление статистики после события
     */
    public void updateAfterEvent(double currentTime, Buffer buffer, List<Charon> charons) {
        // Можем добавить дополнительную статистику после события
        updateRejectionRates(currentTime);
    }

    /**
     * Регистрация создания души
     */
    public void registerSoulCreated(Soul soul) {
        allSouls.add(soul);

        int sourceId = soul.getSourceId();
        sourceGenerated.put(sourceId, sourceGenerated.getOrDefault(sourceId, 0) + 1);

        // Обновляем счетчики для источников
        switch (sourceId) {
            case 1 -> arrived1++;
            case 2 -> arrived2++;
            case 3 -> arrived3++;
        }
        totalArrived++;
    }

    /**
     * Регистрация помещения души в буфер
     */
    public void registerSoulBuffered(Soul soul) {
        // Время в очереди начнем считать позже
    }

    /**
     * Регистрация отказа
     */
    public void registerSoulRejected(Soul soul, double currentTime) {
        rejectedSouls.add(soul);

        int sourceId = soul.getSourceId();
        sourceRejected.put(sourceId, sourceRejected.getOrDefault(sourceId, 0) + 1);

        // Обновляем счетчики для источников
        switch (sourceId) {
            case 1 -> rejected1++;
            case 2 -> rejected2++;
            case 3 -> rejected3++;
        }
        totalRejected++;

        // Время в очереди для отказавшей души
        if (soul.getBufferEntryTime() > 0) {
            double queueTime = currentTime - soul.getBufferEntryTime();
            queueTimeHistory.add(queueTime);
        }
    }

    /**
     * Регистрация начала обслуживания
     */
    public void registerServiceStarted(Soul soul, double currentTime) {
        if (soul.getBufferEntryTime() > 0) {
            double queueTime = currentTime - soul.getBufferEntryTime();
            queueTimeHistory.add(queueTime);
        }
    }

    /**
     * Регистрация завершения обслуживания
     */
    public void registerServiceCompleted(Soul soul, double currentTime) {
        completedSouls.add(soul);

        int sourceId = soul.getSourceId();
        sourceCompleted.put(sourceId, sourceCompleted.getOrDefault(sourceId, 0) + 1);
    }

    /**
     * Обновление процентов отказов для графиков
     */
    private void updateRejectionRates(double time) {
        if (timePoints.isEmpty()) return;

        // Общий процент отказов
        rejectionRateHistory.add(totalArrived > 0 ? (100.0 * totalRejected / totalArrived) : 0);

        // Процент отказов по источникам
        rejectionRateSource1.add(arrived1 > 0 ? (100.0 * rejected1 / arrived1) : 0);
        rejectionRateSource2.add(arrived2 > 0 ? (100.0 * rejected2 / arrived2) : 0);
        rejectionRateSource3.add(arrived3 > 0 ? (100.0 * rejected3 / arrived3) : 0);
    }
}