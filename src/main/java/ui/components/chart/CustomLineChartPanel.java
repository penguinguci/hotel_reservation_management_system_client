package ui.components.chart;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLineChartPanel extends JPanel {
    private String chartTitle = "DOANH THU THEO THÁNG - NĂM 2024";
    private List<String> labels;
    private List<Double> totalRevenueData; // Doanh thu tổng
    private List<Double> roomRevenueData;  // Doanh thu phòng
    private List<Double> serviceRevenueData; // Doanh thu dịch vụ
    private XChartPanel<CategoryChart> chartPanel;

    public CustomLineChartPanel(List<String> labels, List<Double> totalRevenueData,
                                List<Double> roomRevenueData, List<Double> serviceRevenueData) {
        this.labels = labels;
        this.totalRevenueData = totalRevenueData;
        this.roomRevenueData = roomRevenueData;
        this.serviceRevenueData = serviceRevenueData;
        setLayout(new BorderLayout());
        createChart();
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
        updateChart();
    }

    public void setChartData(List<String> labels, List<Double> totalRevenueData,
                             List<Double> roomRevenueData, List<Double> serviceRevenueData) {
        this.labels = labels;
        this.totalRevenueData = totalRevenueData;
        this.roomRevenueData = roomRevenueData;
        this.serviceRevenueData = serviceRevenueData;
        updateChart();
    }

    public CategoryChart getChart() {
        return chartPanel != null ? chartPanel.getChart() : null;
    }

    private void createChart() {
        System.out.println("Creating Line Chart with labels: " + labels +
                ", total revenue: " + totalRevenueData +
                ", room revenue: " + roomRevenueData +
                ", service revenue: " + serviceRevenueData);

        if (labels == null || totalRevenueData == null || roomRevenueData == null ||
                serviceRevenueData == null || labels.isEmpty() || totalRevenueData.isEmpty() ||
                roomRevenueData.isEmpty() || serviceRevenueData.isEmpty()) {
            JLabel errorLabel = new JLabel("Không có dữ liệu để hiển thị", SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            add(errorLabel, BorderLayout.CENTER);
            return;
        }

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title(chartTitle)
                .xAxisTitle("Thời gian")
                .yAxisTitle("Doanh thu (triệu VND)")
                .build();

        Styler styler = chart.getStyler();
        styler.setLegendPosition(Styler.LegendPosition.InsideNW);
        styler.setChartBackgroundColor(Color.WHITE);
        styler.setPlotBorderVisible(true);
        styler.setToolTipsEnabled(true);
        ((org.knowm.xchart.style.CategoryStyler) styler).setDefaultSeriesRenderStyle(org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle.Line);

        // Tùy chỉnh định dạng trục Y
        DecimalFormat df = new DecimalFormat("#,###");
        Map<Double, String> yAxisLabels = new HashMap<>();
        double maxRevenue = Math.max(
                totalRevenueData.stream().max(Double::compare).orElse(24000000.0),
                Math.max(
                        roomRevenueData.stream().max(Double::compare).orElse(24000000.0),
                        serviceRevenueData.stream().max(Double::compare).orElse(24000000.0)
                )
        );
        double step = maxRevenue / 12;
        for (double i = 0; i <= maxRevenue + step; i += step) {
            yAxisLabels.put(i, df.format(i / 1000000));
        }
       // styler.setYAxisLabelOverrideMap(yAxisLabels);

        // Series 1: Doanh thu tổng (màu đỏ cam)
        org.knowm.xchart.CategorySeries totalSeries = chart.addSeries("Doanh thu tổng", labels, totalRevenueData);
        totalSeries.setLineStyle(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        totalSeries.setMarker(SeriesMarkers.DIAMOND);
        totalSeries.setMarkerColor(new Color(255, 99, 71)); // Đỏ cam
        totalSeries.setLineColor(new Color(255, 99, 71));

        // Series 2: Doanh thu phòng (màu xanh dương)
        org.knowm.xchart.CategorySeries roomSeries = chart.addSeries("Doanh thu phòng", labels, roomRevenueData);
        roomSeries.setLineStyle(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        roomSeries.setMarker(SeriesMarkers.CIRCLE);
        roomSeries.setMarkerColor(new Color(70, 130, 180)); // Xanh dương
        roomSeries.setLineColor(new Color(70, 130, 180));

        // Series 3: Doanh thu dịch vụ (màu xanh lá)
        org.knowm.xchart.CategorySeries serviceSeries = chart.addSeries("Doanh thu dịch vụ", labels, serviceRevenueData);
        serviceSeries.setLineStyle(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        serviceSeries.setMarker(SeriesMarkers.SQUARE);
        serviceSeries.setMarkerColor(new Color(50, 205, 50)); // Xanh lá
        serviceSeries.setLineColor(new Color(50, 205, 50));

        if (chartPanel != null) {
            remove(chartPanel);
        }
        chartPanel = new XChartPanel<>(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void updateChart() {
        removeAll();
        createChart();
        revalidate();
        repaint();
    }

    @Override
    public String getToolTipText() {
        return "Biểu đồ đường thống kê doanh thu";
    }
}