package ui.components.chart;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 */
public class CustomerCustomLineChartPanel extends JPanel {
    private List<String> labels;
    private List<Integer> customerData;
    private String chartTitle;
    private XChartPanel<CategoryChart> chartPanel;
    private Color seriesColor = new Color(149, 153, 239); // Màu giống RevenueChartPanel

    public CustomerCustomLineChartPanel() {
        this.labels = Collections.emptyList();
        this.customerData = Collections.emptyList();
        this.chartTitle = "BIỂU ĐỒ SỐ LƯỢNG KHÁCH HÀNG";
        initComponents();
    }

    public CustomerCustomLineChartPanel(List<String> labels, List<Integer> customerData) {
        this.labels = labels != null ? labels : Collections.emptyList();
        this.customerData = customerData != null ? customerData : Collections.emptyList();
        this.chartTitle = "BIỂU ĐỒ SỐ LƯỢNG KHÁCH HÀNG";
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));
        updateChart();
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
        updateChart();
    }

    public void setChartData(List<String> labels, List<Integer> customerData) {
        this.labels = labels != null ? labels : Collections.emptyList();
        this.customerData = customerData != null ? customerData : Collections.emptyList();
        updateChart();
    }

    public CategoryChart getChart() {
        return chartPanel != null ? chartPanel.getChart() : null;
    }

    public void toggleChartType() {
        // Không cần toggleChartType vì CustomerChartPanel đã xử lý việc chuyển đổi
        // Phương thức này giữ lại để tương thích với giao diện trước đó, nhưng không làm gì
    }

    private void updateChart() {
        // Xóa biểu đồ cũ
        removeAll();

        // Đảm bảo labels và data không null
        if (labels == null || labels.isEmpty()) {
            labels = Collections.singletonList("");
        }
        if (customerData == null || customerData.isEmpty()) {
            customerData = Collections.singletonList(0);
        }

        // Đảm bảo kích thước của labels và data khớp nhau
        if (labels.size() != customerData.size()) {
            System.err.println("Kích thước của labels và data không khớp. Labels: " + labels.size() + ", Data: " + customerData.size());
            customerData = Collections.nCopies(labels.size(), 0);
        }

        // Tạo biểu đồ đường bằng XChart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title(chartTitle)
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

        // Đặt kiểu biểu đồ là đường
        ((org.knowm.xchart.style.CategoryStyler) styler).setDefaultSeriesRenderStyle(org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle.Line);

        // Đặt giá trị trục Y cố định để hiển thị đẹp mắt ngay cả khi dữ liệu toàn 0
        DecimalFormat df = new DecimalFormat("#,###");
        Map<Double, String> yAxisLabels = new HashMap<>();
        double maxCustomers = customerData.stream().mapToDouble(Integer::doubleValue).max().orElse(0.0);
        maxCustomers = Math.max(maxCustomers, 100.0); // Giá trị tối đa mặc định: 100 khách hàng
        double step = maxCustomers / 12;
        for (double i = 0; i <= maxCustomers + step; i += step) {
            yAxisLabels.put(i, df.format(i));
        }
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax(maxCustomers);

        // Thêm dữ liệu vào biểu đồ (chỉ một dòng: số lượng khách hàng)
        chart.addSeries("Số lượng khách hàng", labels, customerData);

        // Tạo panel chứa biểu đồ và thêm vào panel chính
        chartPanel = new XChartPanel<>(chart);
        add(chartPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // Kích thước giống RevenueChartPanel
    }
}