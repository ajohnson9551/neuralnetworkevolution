package neuralevolution;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class DataLineChart extends ApplicationFrame {

    int[][] data;
    int cycles;

    public DataLineChart(String title, String chartTitle, int[][] data, int minScore, int maxScore, int scoreStep, int cycles) {
        super(title);
        this.data = data;
        this.cycles = cycles;
        Font font = Font.getFont("Arial");

        XYSeriesCollection scoreData = createScoreDataset();

        XYPlot plot = new XYPlot();
        plot.setDataset(0, scoreData);
        plot.setBackgroundPaint(Color.BLACK);


        XYSplineRenderer sr1 = new XYSplineRenderer();
        sr1.setSeriesShapesVisible(0, false);
        sr1.setSeriesShapesVisible(1, false);
        plot.setRenderer(0, sr1);


        NumberAxis range1 = new NumberAxis("Score");
        range1.setRange(minScore, maxScore);
        range1.setTickUnit(new NumberTickUnit(scoreStep));

        NumberAxis domain = new NumberAxis("Cycle");
        domain.setRange(0, cycles);
        domain.setTickUnit(new NumberTickUnit(cycles/10));

        plot.setRangeAxis(0, range1);
        plot.setDomainAxis(domain);

        plot.mapDatasetToRangeAxis(0, 0);

        JFreeChart lineChart = new JFreeChart(chartTitle, font, plot, true);
        lineChart.setBackgroundPaint(Color.GRAY);
        lineChart.getLegend().setBackgroundPaint(Color.GRAY);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        setContentPane(chartPanel);
    }

    private XYSeriesCollection createScoreDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Max Score");
        XYSeries series2 = new XYSeries("Avg Score");
        for (int i = 1; i <= data[0].length; i++) {
            if (i % (cycles / 100) == 0) {
                series1.add(i, data[0][i-1]);
                series2.add(i, data[1][i-1]);
            }
        }
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }
}
