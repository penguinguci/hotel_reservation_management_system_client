package ui.components.chart;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerChartPanel extends JPanel {
    private int displayYear = 2024;
    private Color seriesColor = new Color(149, 153, 239); // Màu giống RevenueChartPanel
    private String chartTitle = "BIỂU ĐỒ SỐ LƯỢNG KHÁCH HÀNG";
    private List<String> labels = Arrays.asList("Th1", "Th2", "Th3", "Th4", "Th5", "Th6",
            "Th7", "Th8", "Th9", "Th10", "Th11", "Th12");
    private List<Integer> customerData;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private XChartPanel<CategoryChart> barChartPanel;
    private CustomerCustomLineChartPanel customerLineChartPanel; // Sử dụng CustomerCustomLineChartPanel
    private JPanel chartContainer;
    private CardLayout cardLayout;
    private static final String BAR_CHART = "BarChart";
    private static final String LINE_CHART = "LineChart";
    private boolean isBarChartShowing = true;

    public CustomerChartPanel() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        chartContainer = new JPanel(cardLayout);

        // Hiển thị biểu đồ trống ban đầu
        clearChart();
        add(chartContainer, BorderLayout.CENTER);
    }

    private boolean isDesignTime() {
        return java.beans.Beans.isDesignTime() || System.getProperty("java.awt.headless") != null;
    }

    public int getDisplayYear() {
        return displayYear;
    }

    public void setDisplayYear(int displayYear) {
        int oldYear = this.displayYear;
        this.displayYear = displayYear;
        this.chartTitle = "SỐ LƯỢNG KHÁCH HÀNG THEO THÁNG - NĂM " + displayYear;
        pcs.firePropertyChange("displayYear", oldYear, displayYear);
        updateCharts();
    }

    public Color getSeriesColor() {
        return seriesColor;
    }

    public void setSeriesColor(Color seriesColor) {
        Color oldColor = this.seriesColor;
        this.seriesColor = seriesColor;
        pcs.firePropertyChange("seriesColor", oldColor, seriesColor);
        updateCharts();
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        String oldTitle = this.chartTitle;
        this.chartTitle = chartTitle;
        pcs.firePropertyChange("chartTitle", oldTitle, chartTitle);
        updateCharts();
    }

    public void setChartData(List<String> labels, List<Integer> customerData) {
        this.labels = labels;
        this.customerData = customerData;
        updateCharts();
    }

    public CategoryChart getChart() {
        if (isBarChartShowing && barChartPanel != null) {
            return barChartPanel.getChart();
        } else if (customerLineChartPanel != null) {
            return customerLineChartPanel.getChart();
        }
        return null;
    }

    public void toggleChartType() {
        // Nếu không có dữ liệu (biểu đồ trống), không cho phép chuyển đổi
        if (customerData == null || customerData.isEmpty() ||
                customerData.stream().allMatch(d -> d == 0)) {
            System.out.println("Không thể chuyển đổi biểu đồ: Hiện đang hiển thị biểu đồ trống");
            return;
        }

        isBarChartShowing = !isBarChartShowing;
        System.out.println("Toggling chart: Showing " + (isBarChartShowing ? "Bar Chart" : "Line Chart"));
        cardLayout.show(chartContainer, isBarChartShowing ? BAR_CHART : LINE_CHART);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private XChartPanel<CategoryChart> createBarChartPanel(List<String> labels, List<Integer> data, String title) {
        // Đảm bảo labels và data không null
        if (labels == null || labels.isEmpty()) {
            labels = Collections.singletonList("");
        }
        if (data == null || data.isEmpty()) {
            data = Collections.singletonList(0);
        }

        // Đảm bảo kích thước của labels và data khớp nhau
        if (labels.size() != data.size()) {
            System.err.println("Kích thước của labels và data không khớp. Labels: " + labels.size() + ", Data: " + data.size());
            // Điều chỉnh data để khớp với labels
            data = Collections.nCopies(labels.size(), 0);
        }

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title(title)
                .xAxisTitle("Thời gian")
                .yAxisTitle("Số lượng khách hàng")
                .build();

        Styler styler = chart.getStyler();
        styler.setLegendPosition(Styler.LegendPosition.InsideNW);
        styler.setChartBackgroundColor(Color.WHITE);
        styler.setSeriesColors(new Color[]{seriesColor});
        styler.setPlotBorderVisible(true);
        styler.setToolTipsEnabled(true);
        styler.setYAxisLeftWidthHint(50);

        ((org.knowm.xchart.style.CategoryStyler) styler).setDefaultSeriesRenderStyle(org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle.Bar);

        // Đặt giá trị trục Y cố định để hiển thị đẹp mắt ngay cả khi dữ liệu toàn 0
        DecimalFormat df = new DecimalFormat("#,###");
        Map<Double, String> yAxisLabels = new HashMap<>();
        double maxCustomers = data.stream().mapToDouble(Integer::doubleValue).max().orElse(0.0);
        maxCustomers = Math.max(maxCustomers, 100.0); // Giá trị tối đa mặc định: 100 khách hàng
        double step = maxCustomers / 12;
        for (double i = 0; i <= maxCustomers + step; i += step) {
            yAxisLabels.put(i, df.format(i));
        }
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax(maxCustomers);

        chart.addSeries("Số lượng khách hàng", labels, data);

        return new XChartPanel<>(chart);
    }

    public void updateCharts() {
        chartContainer.removeAll();

        System.out.println("Updating charts with customer data: " + customerData);

        // Kiểm tra nếu dữ liệu không hợp lệ (null, rỗng, hoặc toàn 0)
        boolean allZero = (customerData == null || customerData.stream().allMatch(d -> d == 0));

        if (customerData == null || customerData.isEmpty() || allZero) {
            System.out.println("Dữ liệu không hợp lệ hoặc toàn 0, hiển thị biểu đồ trống...");
            clearChart();
            return;
        }

        // Nếu có dữ liệu hợp lệ, vẽ biểu đồ
        try {
            barChartPanel = createBarChartPanel(labels, customerData, chartTitle);
            chartContainer.add(barChartPanel, BAR_CHART);
            System.out.println("Successfully created Bar Chart");

            System.out.println("Creating Line Chart with labels: " + labels +
                    ", customer data: " + customerData);
            customerLineChartPanel = new CustomerCustomLineChartPanel(labels, customerData);
            customerLineChartPanel.setChartTitle(chartTitle);
            chartContainer.add(customerLineChartPanel, LINE_CHART);
            System.out.println("Successfully created Line Chart");
        } catch (Exception e) {
            System.err.println("Error creating charts: " + e.getMessage());
            e.printStackTrace();
            clearChart(); // Nếu có lỗi khi vẽ biểu đồ, hiển thị biểu đồ trống
            return;
        }

        cardLayout.show(chartContainer, isBarChartShowing ? BAR_CHART : LINE_CHART);
        chartContainer.revalidate();
        chartContainer.repaint();
        System.out.println("Charts updated successfully");
    }

    public void clearChart() {
        System.out.println("Bắt đầu xóa biểu đồ...");

        chartContainer.removeAll();
        // Tạo dữ liệu trống (toàn 0) để vẽ biểu đồ trống
        List<String> displayLabels = (labels != null && !labels.isEmpty()) ? labels : Collections.singletonList("");
        List<Integer> emptyData = Collections.nCopies(displayLabels.size(), 0);

        // Chỉ tạo biểu đồ cột trống, tạm thời bỏ qua biểu đồ đường để tránh lỗi
        try {
            barChartPanel = createBarChartPanel(displayLabels, emptyData, chartTitle);
            chartContainer.add(barChartPanel, BAR_CHART);
            System.out.println("Successfully created empty Bar Chart");

            // Tạm thời không tạo biểu đồ đường trống để tránh lỗi
            JLabel placeholder = new JLabel("Biểu đồ đường không khả dụng", SwingConstants.CENTER);
            placeholder.setForeground(Color.WHITE);
            chartContainer.add(placeholder, LINE_CHART);
        } catch (Exception e) {
            System.err.println("Error creating empty Bar Chart: " + e.getMessage());
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Không thể tạo biểu đồ trống", SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            chartContainer.add(errorLabel, BAR_CHART);
            chartContainer.add(errorLabel, LINE_CHART);
        }

        cardLayout.show(chartContainer, isBarChartShowing ? BAR_CHART : LINE_CHART);
        chartContainer.revalidate();
        chartContainer.repaint();
        System.out.println("Hoàn tất xóa biểu đồ - Hiển thị biểu đồ trống");
    }

    @Override
    public String getToolTipText() {
        return "Biểu đồ thống kê số lượng khách hàng";
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(904, 450); // Giữ nguyên kích thước như cũ
    }
}