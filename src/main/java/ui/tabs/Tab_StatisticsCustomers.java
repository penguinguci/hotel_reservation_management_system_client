package ui.tabs;

import dao.AmountCustomerDAOImpl;
import interfaces.AmountCustomerDAO;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ui.components.chart.CustomerChartPanel;
import ui.components.combobox.StyledComboBox;
import utils.CurrentAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Lenovo
 */
public class Tab_StatisticsCustomers extends JPanel {
    private AmountCustomerDAO customerDAO;
    private StyledComboBox yearComboBox;
    private JLabel yearLabel;
    private List<String> currentLabels;
    private List<Integer> currentCustomerData;
    private int currentTotalCustomers;
    private String currentChartTitle;

    @SneakyThrows
    public Tab_StatisticsCustomers() {
        customerDAO = new AmountCustomerDAOImpl();
        initComponents();

        yearLabel = new JLabel("Chọn năm:     ");
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearLabel.setVisible(false);

        List<Integer> availableYears;
        try {
            System.out.println("Bắt đầu lấy danh sách năm...");
            long startTime = System.currentTimeMillis();
            availableYears = customerDAO.getAvailableYears();
            long endTime = System.currentTimeMillis();
            System.out.println("Hoàn tất lấy danh sách năm, thời gian: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            System.err.println("Error loading years: " + e.getMessage());
            e.printStackTrace();
            availableYears = Collections.singletonList(2025);
        }

        if (availableYears.isEmpty()) {
            availableYears = Collections.singletonList(2025);
        }
        yearComboBox = new StyledComboBox(availableYears.toArray(new Integer[0]));
        yearComboBox.setPreferredSize(new Dimension(160, 30));
        yearComboBox.setVisible(false);
        yearComboBox.addActionListener(e -> updateStatistics());

        GroupLayout pFindLayout = (GroupLayout) pFind.getLayout();
        pFindLayout.setHorizontalGroup(
                pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pFindLayout.createSequentialGroup()
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pFindLayout.createSequentialGroup()
                                                .addGap(92, 92, 92)
                                                .addComponent(jLabel1))
                                        .addGroup(pFindLayout.createSequentialGroup()
                                                .addGap(25, 25, 25)
                                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel3)
                                                        .addGroup(pFindLayout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(styledComboBox1, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                                        .addGroup(pFindLayout.createSequentialGroup()
                                                                .addComponent(yearLabel)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(yearComboBox, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                                        .addComponent(buttonCustom1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(customCalendar2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(customCalendar1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(buttonCustom2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(buttonCustom3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pFindLayout.setVerticalGroup(
                pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, pFindLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel1)
                                .addGap(41, 41, 41)
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(styledComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(yearLabel)
                                        .addComponent(yearComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(33, 33, 33)
                                .addComponent(customCalendar1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(jLabel4)
                                .addGap(32, 32, 32)
                                .addComponent(customCalendar2, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                                .addComponent(buttonCustom2, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(buttonCustom3, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(buttonCustom1, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
        );

        styledComboBox1.setModel(new DefaultComboBoxModel<>(new String[]{
                "Tùy chọn", "Theo tháng", "Theo quý", "Theo năm"
        }));
        styledComboBox1.setSelectedItem("Theo tháng");

        customCalendar1.setVisible(false);
        customCalendar2.setVisible(false);
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);

        styledComboBox1.addActionListener(e -> toggleDateRangePanel());
        buttonCustom2.addActionListener(e -> updateStatistics());
        buttonCustom3.addActionListener(e -> toggleChartType());
        buttonCustom1.addActionListener(e -> exportToExcel());

        updateCustomerPanel(0); // Đặt panel số lượng khách hàng về 0 ban đầu
    }

    private void toggleDateRangePanel() {
        String selectedCriteria = (String) styledComboBox1.getSelectedItem();
        boolean isCustom = "Tùy chọn".equals(selectedCriteria);
        boolean isMonthOrQuarter = "Theo tháng".equals(selectedCriteria) || "Theo quý".equals(selectedCriteria);

        jLabel3.setVisible(isCustom);
        jLabel4.setVisible(isCustom);
        customCalendar1.setVisible(isCustom);
        customCalendar2.setVisible(isCustom);

        yearLabel.setVisible(isMonthOrQuarter);
        yearComboBox.setVisible(isMonthOrQuarter);

        revalidate();
        repaint();
    }

    private void toggleChartType() {
        customerChartPanel1.toggleChartType();
    }

    private void updateStatistics() {
        String criteria = (String) styledComboBox1.getSelectedItem();

        customerChartPanel1.setChartTitle("Đang tải dữ liệu...");
        customerChartPanel1.clearChart();
        updateCustomerPanel(0);

        try {
            if ("Tùy chọn".equals(criteria)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date startDate = customCalendar1.getSelectedDate();
                Date endDate = customCalendar2.getSelectedDate();
                List<Integer> customerData;
                String chartTitle;
                int totalCustomers;

                if (startDate == null || endDate == null || startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng thời gian hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    customerChartPanel1.setChartTitle("Chọn khoảng thời gian hợp lệ");
                    customerChartPanel1.clearChart();
                    return;
                }

                List<String> labels = customerDAO.getDateRangeLabels(startDate, endDate);
                System.out.println("Labels: " + labels);

                chartTitle = "Số lượng khách hàng từ " + sdf.format(startDate) + " đến " + sdf.format(endDate);
                boolean shouldUpdateChart = true;

                if (labels.isEmpty()) {
                    chartTitle += " (Không có dữ liệu)";
                    customerData = Collections.emptyList();
                    shouldUpdateChart = false;
                    System.out.println("Không có nhãn, không cập nhật biểu đồ");
                } else {
                    customerData = customerDAO.getCustomerCountByDateRange(startDate, endDate);
                    System.out.println("Customer Data: " + customerData);

                    if (customerData == null || customerData.isEmpty()) {
                        chartTitle += " (Không có dữ liệu khách hàng)";
                        customerData = Collections.emptyList();
                        shouldUpdateChart = false;
                        System.out.println("Không có dữ liệu khách hàng, không cập nhật biểu đồ");
                    } else {
                        boolean allZero = customerData.stream().allMatch(value -> value == 0);
                        if (allZero) {
                            chartTitle += " (Không có khách hàng)";
                            shouldUpdateChart = false;
                            System.out.println("Dữ liệu toàn 0, không cập nhật biểu đồ");
                        }
                    }
                }

                totalCustomers = customerDAO.getTotalCustomerCountByDateRange(startDate, endDate);

                // Lưu trữ dữ liệu hiện tại để xuất Excel
                currentLabels = labels;
                currentCustomerData = customerData;
                currentTotalCustomers = totalCustomers;
                currentChartTitle = chartTitle;

                customerChartPanel1.setChartTitle(chartTitle);
                if (shouldUpdateChart) {
                    System.out.println("Cập nhật biểu đồ với dữ liệu: " + customerData);
                    customerChartPanel1.setChartData(labels, customerData);
                    System.out.println("Hoàn tất cập nhật biểu đồ");
                } else {
                    System.out.println("Hiển thị biểu đồ trống do không có dữ liệu hợp lệ...");
                    customerChartPanel1.clearChart();
                    System.out.println("Đã hiển thị biểu đồ trống");
                }

                System.out.println("Bắt đầu cập nhật panel số lượng khách hàng...");
                updateCustomerPanel(totalCustomers);
                System.out.println("Hoàn tất cập nhật panel số lượng khách hàng");
            } else if ("Theo năm".equals(criteria)) {
                List<Integer> customerData = customerDAO.getYearlyCustomerCount();
                List<String> labels = Arrays.asList("2023", "2024", "2025");

                int[] yearlyTotals = new int[3];
                int startYear = 2023;
                for (int i = 0; i < 3; i++) {
                    int y = startYear + i;
                    yearlyTotals[i] = customerDAO.getTotalCustomerCount(y);
                }
                int totalCustomers = Arrays.stream(yearlyTotals).sum();

                boolean allZero = customerData.stream().allMatch(d -> d == 0);

                String chartTitle = "Số lượng khách hàng theo từng năm";
                // Lưu trữ dữ liệu hiện tại để xuất Excel
                currentLabels = labels;
                currentCustomerData = customerData;
                currentTotalCustomers = totalCustomers;
                currentChartTitle = chartTitle;

                if (customerData == null || customerData.isEmpty() || allZero) {
                    chartTitle += " (Không có dữ liệu)";
                    customerChartPanel1.setChartTitle(chartTitle);
                    customerChartPanel1.clearChart();
                } else {
                    customerChartPanel1.setChartTitle(chartTitle);
                    customerChartPanel1.setChartData(labels, customerData);
                }
                updateCustomerPanel(totalCustomers);
            } else {
                int year = (int) yearComboBox.getSelectedItem();
                String chartTitle = switch (criteria) {
                    case "Theo tháng" -> "Số lượng khách hàng theo tháng - Năm " + year;
                    case "Theo quý" -> "Số lượng khách hàng theo quý - Năm " + year;
                    default -> "Số lượng khách hàng năm " + year;
                };

                List<Integer> customerData;
                List<String> labels;
                int totalCustomers;

                switch (criteria) {
                    case "Theo quý":
                        customerData = customerDAO.getQuarterlyCustomerCount(year);
                        labels = Arrays.asList("Q1", "Q2", "Q3", "Q4");

                        int[] quarterlyTotals = new int[4];
                        for (int i = 0; i < 4; i++) {
                            quarterlyTotals[i] = customerDAO.getTotalCustomerCountByQuarter(year, i + 1);
                        }
                        totalCustomers = Arrays.stream(quarterlyTotals).sum();
                        break;

                    default: // Theo tháng
                        customerData = customerDAO.getMonthlyCustomerCount(year);
                        labels = Arrays.asList("Th1", "Th2", "Th3", "Th4", "Th5", "Th6",
                                "Th7", "Th8", "Th9", "Th10", "Th11", "Th12");

                        totalCustomers = customerData.stream().mapToInt(Integer::intValue).sum();
                        break;
                }

                boolean allZero = customerData.stream().allMatch(d -> d == 0);
                // Lưu trữ dữ liệu hiện tại để xuất Excel
                currentLabels = labels;
                currentCustomerData = customerData;
                currentTotalCustomers = totalCustomers;
                currentChartTitle = chartTitle;

                if (customerData == null || customerData.isEmpty() || allZero) {
                    chartTitle += " (Không có khách hàng)";
                    customerChartPanel1.setChartTitle(chartTitle);
                    customerChartPanel1.clearChart();
                } else {
                    customerChartPanel1.setDisplayYear(year);
                    customerChartPanel1.setChartTitle(chartTitle);
                    customerChartPanel1.setChartData(labels, customerData);
                }
                updateCustomerPanel(totalCustomers);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thống kê: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            customerChartPanel1.setChartTitle("Lỗi khi tải dữ liệu");
            customerChartPanel1.clearChart();
        }
    }

    private void updateCustomerPanel(int totalCustomers) {
        jLabel8.setText(String.valueOf(totalCustomers));
    }

    private void exportToExcel() {
        if (currentLabels == null || currentCustomerData == null || currentLabels.isEmpty() || currentCustomerData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để xuất báo cáo. Vui lòng thực hiện thống kê trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy ngày xuất báo cáo
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String exportDate = now.format(formatter);

        // Tạo Workbook mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customer Report");

        // Tạo style cho tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        // Tạo style cho thông tin (Ngày xuất, Nhân viên)
        CellStyle infoStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font infoFont = workbook.createFont();
        infoFont.setFontHeightInPoints((short) 12);
        infoStyle.setFont(infoFont);
        infoStyle.setAlignment(HorizontalAlignment.LEFT);

        // Tạo style cho header
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // Tạo style cho dữ liệu
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Tạo style cho tổng cộng
        CellStyle totalStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font totalFont = workbook.createFont();
        totalFont.setBold(true);
        totalStyle.setFont(totalFont);
        totalStyle.setBorderTop(BorderStyle.THIN);
        totalStyle.setBorderBottom(BorderStyle.THIN);
        totalStyle.setBorderLeft(BorderStyle.THIN);
        totalStyle.setBorderRight(BorderStyle.THIN);

        // Dòng tiêu đề
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(currentChartTitle);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        // Dòng thông tin: Ngày xuất báo cáo
        Row dateRow = sheet.createRow(1);
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Ngày xuất báo cáo: " + exportDate);
        dateCell.setCellStyle(infoStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));

        // Dòng thông tin: Nhân viên xuất báo cáo
        Row employeeRow = sheet.createRow(2);
        Cell employeeCell = employeeRow.createCell(0);
        employeeCell.setCellValue("Nhân viên xuất báo cáo: " + CurrentAccount.getCurrentAccount().getStaff().getFirstName() + " " + CurrentAccount.getCurrentAccount().getStaff().getLastName());
        employeeCell.setCellStyle(infoStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));

        // Dòng header
        Row headerRow = sheet.createRow(4);
        String[] headers = {"Thời gian", "Số lượng khách hàng"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        // Ghi dữ liệu
        for (int i = 0; i < currentLabels.size(); i++) {
            Row dataRow = sheet.createRow(i + 5);
            dataRow.createCell(0).setCellValue(currentLabels.get(i));
            dataRow.createCell(1).setCellValue(currentCustomerData.get(i));

            for (int j = 0; j < 2; j++) {
                dataRow.getCell(j).setCellStyle(dataStyle);
            }
        }

        // Dòng tổng cộng
        Row totalRow = sheet.createRow(currentLabels.size() + 6);
        totalRow.createCell(0).setCellValue("Tổng cộng");
        totalRow.createCell(1).setCellValue(currentTotalCustomers);

        for (int j = 0; j < 2; j++) {
            totalRow.getCell(j).setCellStyle(totalStyle);
        }

        // Tự động điều chỉnh kích thước cột
        for (int i = 0; i < 2; i++) {
            sheet.autoSizeColumn(i);
        }

        // Lưu file Excel
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setSelectedFile(new File("Customer_Report.xlsx"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(this, "Xuất báo cáo thành công tại: " + fileToSave.getAbsolutePath(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        try {
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        pChart = new JPanel();
        jPanel4 = new JPanel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        pFind = new JPanel();
        buttonCustom1 = new ui.components.button.ButtonCustom();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        styledComboBox1 = new StyledComboBox();
        jLabel3 = new JLabel();
        customCalendar1 = new ui.components.calendar.CustomCalendar();
        jLabel4 = new JLabel();
        customCalendar2 = new ui.components.calendar.CustomCalendar();
        buttonCustom2 = new ui.components.button.ButtonCustom();
        buttonCustom3 = new ui.components.button.ButtonCustom();
        jPanel1 = new JPanel();
        customerChartPanel1 = new CustomerChartPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new BorderLayout());

        pChart.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), java.awt.Color.lightGray, new java.awt.Color(153, 153, 153)));

        jLabel7.setFont(new Font("Segoe UI Black", 0, 14));
        jLabel7.setForeground(new java.awt.Color(153, 153, 255));
        jLabel7.setText("TỔNG SỐ LƯỢNG KHÁCH HÀNG");

        jLabel8.setFont(new Font("Segoe UI", 0, 14));
        jLabel8.setText("0");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8))
                                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addGap(26, 26, 26))
        );

        pFind.setBackground(new java.awt.Color(255, 255, 255));
        pFind.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        buttonCustom1.setText("Xuất báo cáo");

        jLabel1.setFont(new Font("Segoe UI", 1, 14));
        jLabel1.setForeground(new java.awt.Color(153, 153, 255));
        jLabel1.setText("TÙY CHỌN THỐNG KÊ");

        jLabel2.setFont(new Font("Segoe UI", 0, 14));
        jLabel2.setText("Chọn tiêu chí: ");

        jLabel3.setFont(new Font("Segoe UI", 0, 14));
        jLabel3.setText("Thời gian bắt đầu:");

        jLabel4.setFont(new Font("Segoe UI", 0, 14));
        jLabel4.setText("Thời gian kết thúc:");

        buttonCustom2.setText("Thống kê");

        buttonCustom3.setText("Chuyển biểu đồ");

        GroupLayout pFindLayout = new GroupLayout(pFind);
        pFind.setLayout(pFindLayout);
        pFindLayout.setHorizontalGroup(
                pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pFindLayout.createSequentialGroup()
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pFindLayout.createSequentialGroup()
                                                .addGap(92, 92, 92)
                                                .addComponent(jLabel1))
                                        .addGroup(pFindLayout.createSequentialGroup()
                                                .addGap(31, 31, 31)
                                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel3)
                                                        .addGroup(pFindLayout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(styledComboBox1, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                                        .addComponent(buttonCustom1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(customCalendar2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(buttonCustom2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(buttonCustom3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(customCalendar1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pFindLayout.setVerticalGroup(
                pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, pFindLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel1)
                                .addGap(29, 29, 29)
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(styledComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addGap(36, 36, 36)
                                .addComponent(customCalendar1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel4)
                                .addGap(36, 36, 36)
                                .addComponent(customCalendar2, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                                .addComponent(buttonCustom2, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(buttonCustom3, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(buttonCustom1, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(customerChartPanel1, GroupLayout.PREFERRED_SIZE, 904, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(customerChartPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        GroupLayout pChartLayout = new GroupLayout(pChart);
        pChart.setLayout(pChartLayout);
        pChartLayout.setHorizontalGroup(
                pChartLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pChartLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pChartLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(pFind, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pChartLayout.setVerticalGroup(
                pChartLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, pChartLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pFind, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64))
                        .addGroup(GroupLayout.Alignment.TRAILING, pChartLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        add(pChart, BorderLayout.PAGE_START);
    }
    // </editor-fold>

    // Variables declaration - do not modify
    private ui.components.button.ButtonCustom buttonCustom1;
    private ui.components.button.ButtonCustom buttonCustom2;
    private ui.components.button.ButtonCustom buttonCustom3;
    private ui.components.calendar.CustomCalendar customCalendar1;
    private ui.components.calendar.CustomCalendar customCalendar2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JPanel jPanel1;
    private JPanel jPanel4;
    private JPanel pChart;
    private JPanel pFind;
    private CustomerChartPanel customerChartPanel1;
    private StyledComboBox styledComboBox1;
    // End of variables declaration
}