package main.utils;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChartGenerator {

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 12);

    /**
     * 📊 ГРАФИК 1: Процент отказов от времени
     */
    /**
     * 📊 ГРАФИК: Процент отказов по источникам (все на одном графике)
     */
    public static void showRejectionRateBySourceChart(Statistics stats) {
        XYSeries series1 = new XYSeries("Источник 1 (высокий)");
        XYSeries series2 = new XYSeries("Источник 2 (средний)");
        XYSeries series3 = new XYSeries("Источник 3 (низкий)");

        List<Double> times = stats.getTimePoints();
        List<Double> rejRate1 = stats.getRejectionRateSource1();
        List<Double> rejRate2 = stats.getRejectionRateSource2();
        List<Double> rejRate3 = stats.getRejectionRateSource3();

        int size = Math.min(times.size(), Math.min(rejRate1.size(),
                Math.min(rejRate2.size(), rejRate3.size())));

        for (int i = 0; i < size; i++) {
            series1.add(times.get(i), rejRate1.get(i));
            series2.add(times.get(i), rejRate2.get(i));
            series3.add(times.get(i), rejRate3.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Процент отказов по источникам (Д2Б4)",
                "Время (с)",
                "Отказы (%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        customizeMultiSourceChart(chart);
        showChart(chart, "Отказы по источникам");
    }

    private static void customizeMultiSourceChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 14));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.getRangeAxis().setRange(0, 100);

        XYLineAndShapeRenderer renderer = getXyLineAndShapeRenderer();

        plot.setRenderer(renderer);

        // Подзаголовок
        chart.addSubtitle(new org.jfree.chart.title.TextTitle(
                "Высокий приоритет (1) → меньше отказов, Низкий приоритет (3) → больше отказов",
                new Font("Arial", Font.ITALIC, 11)));
    }

    private static XYLineAndShapeRenderer getXyLineAndShapeRenderer() {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(46, 204, 113)); // Зеленый - высокий приоритет
        renderer.setSeriesPaint(1, new Color(241, 196, 15)); // Желтый - средний
        renderer.setSeriesPaint(2, new Color(231, 76, 60));  // Красный - низкий
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesStroke(2, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        return renderer;
    }

    /**
     * 📊 ГРАФИК 2: Загрузка Харонов (процент занятых в момент времени)
     */
    public static void showCharonLoadChart(Statistics stats, int charonCount) {
        XYSeries series = new XYSeries("% занятых Харонов");

        List<Double> times = stats.getTimePoints();
        List<Double> charonLoad = stats.getCharonLoadHistory();

        // Убедимся, что размеры совпадают
        int size = Math.min(times.size(), charonLoad.size());

        for (int i = 0; i < size; i++) {
            series.add(times.get(i), charonLoad.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Загрузка Харонов (Д2П2)",
                "Время (с)",
                "Занятость (%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Настройка
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 14));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        // Линия 0% и 100%
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Ось Y от 0 до 100
        plot.getRangeAxis().setRange(0, 100);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(41, 128, 185)); // Синий
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);

        // Подзаголовок
        chart.addSubtitle(new org.jfree.chart.title.TextTitle(
                charonCount + " Харонов, μ=" + String.format("%.2f", stats.getMu()),
                new Font("Arial", Font.ITALIC, 11)));

        showChart(chart, "Загрузка Харонов");
    }

    /**
     * 📊 ГРАФИК 3: Заполненность буфера от времени
     */
    public static void showBufferUsageChart(Statistics stats, int bufferCapacity) {
        XYSeries series = new XYSeries("Заполненность буфера");

        List<Double> times = stats.getTimePoints();
        List<Double> bufferUsage = stats.getBufferUsageHistory();

        for (int i = 0; i < times.size(); i++) {
            series.add(times.get(i), bufferUsage.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Заполненность буфера (Д2Б4)",
                "Время (с)",
                "Заполненность (%)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        customizeChart(chart, "Буфер на " + bufferCapacity + " мест, вытеснение последней", Color.GREEN);
        showChart(chart, "Заполненность буфера");
    }

    private static void customizeChart(JFreeChart chart, String subtitle, Color lineColor) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(TITLE_FONT);

        // Добавляем подзаголовок
        chart.addSubtitle(new org.jfree.chart.title.TextTitle(subtitle,
                new Font("Arial", Font.ITALIC, 11)));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        // Линия 0% и 100%
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, lineColor);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);

        // Ось Y от 0 до 100
        plot.getRangeAxis().setRange(0, 100);
    }

    private static void showChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);

        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


}