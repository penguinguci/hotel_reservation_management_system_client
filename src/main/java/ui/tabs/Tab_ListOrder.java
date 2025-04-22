/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.tabs;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.OrderDAOImpl;
import entities.OrderDetails;
import entities.Orders;
import entities.Reservation;
import interfaces.OrderDAO;
import lombok.SneakyThrows;
import ui.components.table.CustomTableButton;
import ultilities.GenerateString;
import utils.DateUtil;
import utils.MoneyUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 *
 * @author TRAN LONG VU
 */
public class Tab_ListOrder extends JPanel {
    private OrderDAO orderDAO;


    /**
     * Creates new form Tab_ListOrder
     */
    @SneakyThrows
    public Tab_ListOrder() {
        orderDAO = new OrderDAOImpl();
        initComponents();
        loadOrderStatuses();
        loadOrders();
        setupTableListener();
    }

    private void loadOrderStatuses() {
        cbx_StatusOrder.addItem("Tất cả");
        cbx_StatusOrder.addItem("Chờ thanh toán");
        cbx_StatusOrder.addItem("Đã thanh toán");
    }

    @SneakyThrows
    private void loadOrders() {
        CustomTableButton.CustomTableModel model = table_ListOrder.getTableModel();
        model.clearData();
        List<Orders> orders = orderDAO.getAllOrders();
        for (Orders order : orders) {
            Object[] rowData = new Object[]{
                    model.getRowCount() + 1,
                    order.getOrderId(),
                    order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName(),
                    order.getStaff().getFirstName() + " " + order.getStaff().getLastName(),
                    order.getOrderDate(),
                    convertStatusToString(order.getStatus()),
                    MoneyUtil.formatCurrency(order.getTotalPrice())
            };
            model.addRow(rowData, null);
        }
    }

    private String convertStatusToString(int status) {
        switch (status) {
            case 0: return "Chờ thanh toán";
            case 1: return "Đã thanh toán";
            case 2: return "Hủy";
            default: return "Không xác định";
        }
    }

    private void setupTableListener() {
        table_ListOrder.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table_ListOrder.getTable().getSelectedRow();
                if (row >= 0) {
                    String orderId = (String) table_ListOrder.getTableModel().getValueAt(row, 1);
                    displayOrderDetails(orderId);
                }
            }
        });

        btn_Search.addActionListener(e -> searchOrders());
        btn_Clear.addActionListener(e -> clearFields());
        btn_PrintOrder.addActionListener(e -> printOrder());
        btn_Reset.addActionListener(e -> loadOrders());
    }

    @SneakyThrows
    private void displayOrderDetails(String orderId) {
        Orders order = orderDAO.getOrderDetails(orderId);
        if (order == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hiển thị thông tin phòng
        displayRoomInfo(order);

        // Hiển thị thông tin khách hàng và hóa đơn
        displayCustomerAndOrderInfo(order);

        // Hiển thị thông tin đặt phòng
        displayReservationInfo(order);

        // Hiển thị danh sách dịch vụ
        displayServiceList(order);
    }

    private void displayRoomInfo(Orders order) {
        if (order.getRoom() != null) {
            lbl_RoomID_Value.setText(order.getRoom().getRoomId());
            lbl_Floor_Value.setText(String.valueOf(order.getRoom().getFloor()));
            lbl_TypeRoom_Value.setText(order.getRoom().getRoomType() != null ?
                    order.getRoom().getRoomType().getTypeName() : "N/A");
            lbl_Capacity_Value.setText(String.valueOf(order.getRoom().getCapacity()));
        } else {
            lbl_RoomID_Value.setText("N/A");
            lbl_Floor_Value.setText("N/A");
            lbl_TypeRoom_Value.setText("N/A");
            lbl_Capacity_Value.setText("N/A");
        }
    }

    private void displayCustomerAndOrderInfo(Orders order) {
        if (order.getCustomer() != null) {
            lbl_CustomerName_Value.setText(
                    order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        } else {
            lbl_CustomerName_Value.setText("N/A");
        }
        lbl_OrderDate_Value.setText(order.getOrderDate() != null ?
                DateUtil.formatDate(order.getOrderDate()) : "N/A");
    }

    @SneakyThrows
    private void displayReservationInfo(Orders order) {
        Reservation reservation = orderDAO.findReservationForOrder(order);
        order.recalculateTotalPrice(reservation);

        if (order.getRoom() != null) {
            boolean isHourly = order.getCheckInTime() != null && order.getCheckOutTime() != null;
            if (!isHourly) {
                // Nightly booking
                lbl_CheckInDateOrTime_Value.setText(order.getCheckInDate() != null ?
                        DateUtil.formatDate(order.getCheckInDate()) : "N/A");
                lbl_CheckoutDateOrTime_Value.setText(order.getCheckOutDate() != null ?
                        DateUtil.formatDate(order.getCheckOutDate()) : "N/A");
                lbl_NumOfNightOrHour_Value.setText(String.valueOf(
                        order.getNumberOfNights() != null ? order.getNumberOfNights() : 1));
                lbl_PriceOfNightOrHour_Value.setText(MoneyUtil.formatCurrency(
                        order.getRoom().getPrice()));
            } else {
                // Hourly booking
                lbl_CheckInDateOrTime_Value.setText(order.getCheckInTime() != null ?
                        DateUtil.formatDateTime(order.getCheckInTime()) : "N/A");
                lbl_CheckoutDateOrTime_Value.setText(order.getCheckOutTime() != null ?
                        DateUtil.formatDateTime(order.getCheckOutTime()) : "N/A");
                long diffInMillis = order.getCheckOutTime().getTime() - order.getCheckInTime().getTime();
                int hours = (int) Math.ceil(diffInMillis / (60.0 * 60 * 1000));
                lbl_NumOfNightOrHour_Value.setText(String.valueOf(hours));
                lbl_PriceOfNightOrHour_Value.setText(MoneyUtil.formatCurrency(
                        order.getRoom().calculateHourlyRate(order.getCheckInTime())));
            }
            lbl_NumOfFloating_Value.setText(reservation != null ?
                    String.valueOf(reservation.getOverstayUnits()) : "0");
        } else {
            clearReservationInfo();
        }
    }

    private void clearReservationInfo() {
        lbl_CheckInDateOrTime_Value.setText("N/A");
        lbl_CheckoutDateOrTime_Value.setText("N/A");
        lbl_NumOfNightOrHour_Value.setText("N/A");
        lbl_PriceOfNightOrHour_Value.setText("N/A");
        lbl_NumOfFloating_Value.setText("N/A");
    }

    private void displayServiceList(Orders order) {
        CustomTableButton.CustomTableModel model = table_ListService.getTableModel();
        model.clearData();

        // Chỉ hiển thị orderDetails
        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
            int index = 1;
            for (OrderDetails detail : order.getOrderDetails()) {
                if (detail.getService() != null) {
                    Object[] rowData = new Object[]{
                            index++,
                            detail.getService().getServiceId(),
                            detail.getService().getName(),
                            detail.getQuantity(),
                            MoneyUtil.formatCurrency(detail.getService().getPrice()),
                            MoneyUtil.formatCurrency(detail.getLineTotalAmount())
                    };
                    model.addRow(rowData, null);
                }
            }
        }
    }

    @SneakyThrows
    private void searchOrders() {
        String orderId = txt_OrderID.getText().trim();
        String customerId = txt_CustomerID.getText().trim();
        String staffId = txt_StaffID.getText().trim();
        Date fromDate = calendar_Orderdate_From.getSelectedDate();
        Date toDate = calendar_Orderdate_To.getSelectedDate();
        Integer status = cbx_StatusOrder.getSelectedIndex() == 0 ? null : cbx_StatusOrder.getSelectedIndex() - 1;
        Double priceFrom = parsePrice(txt_Price_From.getText().trim());
        Double priceTo = parsePrice(txt_Price_To.getText().trim());

        List<Orders> orders = orderDAO.searchOrders(orderId, customerId, staffId, fromDate, toDate,
                status, priceFrom, priceTo);

        updateOrderTable(orders);
    }

    private Double parsePrice(String priceStr) {
        if (priceStr.isEmpty()) return null;
        try {
            return Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền không hợp lệ: " + priceStr,
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void updateOrderTable(List<Orders> orders) {
        CustomTableButton.CustomTableModel model = table_ListOrder.getTableModel();
        model.clearData();

        for (int i = 0; i < orders.size(); i++) {
            Orders order = orders.get(i);
            Object[] rowData = new Object[]{
                    i + 1,
                    order.getOrderId(),
                    order.getCustomer() != null ?
                            order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName() : "N/A",
                    order.getStaff() != null ?
                            order.getStaff().getFirstName() + " " + order.getStaff().getLastName() : "N/A",
                    DateUtil.formatDateTime(order.getOrderDate()),
                    convertStatusToString(order.getStatus()),
                    MoneyUtil.formatCurrency(order.getTotalPrice())
            };
            model.addRow(rowData, null);
        }
    }

    private void clearFields() {
        txt_OrderID.setText("");
        txt_CustomerID.setText("");
        txt_StaffID.setText("");
        txt_Price_From.setText("");
        txt_Price_To.setText("");
        calendar_Orderdate_From.setSelectedDate(null);
        calendar_Orderdate_To.setSelectedDate(null);
        cbx_StatusOrder.setSelectedIndex(0);
        table_ListOrder.getTable().clearSelection();
        table_ListService.getTableModel().clearData();
        clearDetailLabels();
    }

    private void clearDetailLabels() {
        lbl_RoomID_Value.setText("");
        lbl_Floor_Value.setText("");
        lbl_TypeRoom_Value.setText("");
        lbl_Capacity_Value.setText("");
        lbl_CustomerName_Value.setText("");
        lbl_OrderDate_Value.setText("");
        lbl_CheckInDateOrTime_Value.setText("");
        lbl_CheckoutDateOrTime_Value.setText("");
        lbl_NumOfNightOrHour_Value.setText("");
        lbl_PriceOfNightOrHour_Value.setText("");
        lbl_NumOfFloating_Value.setText("");
    }

    @SneakyThrows
    private void printOrder() {
        int row = table_ListOrder.getTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để in!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String orderId = (String) table_ListOrder.getTableModel().getValueAt(row, 1);
        Orders order = orderDAO.getOrderDetails(orderId);
        if (order == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Reservation reservation = orderDAO.findReservationForOrder(order);
        order.recalculateTotalPrice(reservation);

        Document document = new Document(PageSize.A4);
        File invoicesDir = new File("invoices");
        invoicesDir.mkdirs();

        String fileName = "invoices/Order_" + GenerateString.generateNameFileOrder() + ".pdf";
        File pdfFile = new File(fileName);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            BaseFont baseFont = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 20, Font.BOLD);
            Font headerFont = new Font(baseFont, 12, Font.BOLD);
            Font normalFont = new Font(baseFont, 10);
            Font smallFont = new Font(baseFont, 8);

            Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            PdfPTable companyTable = new PdfPTable(1);
            companyTable.setWidthPercentage(100);
            companyTable.addCell(createCell("KHÁCH SẠN MELODY\nĐịa chỉ: 12 Nguyễn Văn Bảo, Quận Gò Vấp, TP.HCM\nHotline: 0915 020 803\nMã số thuế: 1234567890",
                    normalFont, Element.ALIGN_CENTER, 5, Rectangle.NO_BORDER));
            document.add(companyTable);
            document.add(new Paragraph("\n"));

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 2});

            infoTable.addCell(createCell("Mã hóa đơn:", headerFont, Element.ALIGN_LEFT, 5));
            infoTable.addCell(createCell(order.getOrderId(), normalFont, Element.ALIGN_LEFT, 5));

            infoTable.addCell(createCell("Khách hàng:", headerFont, Element.ALIGN_LEFT, 5));
            infoTable.addCell(createCell(
                    order.getCustomer() != null ?
                            order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName() : "N/A",
                    normalFont, Element.ALIGN_LEFT, 5));

            infoTable.addCell(createCell("Số điện thoại:", headerFont, Element.ALIGN_LEFT, 5));
            infoTable.addCell(createCell(
                    order.getCustomer() != null ? order.getCustomer().getPhoneNumber() : "N/A",
                    normalFont, Element.ALIGN_LEFT, 5));

            infoTable.addCell(createCell("Nhân viên:", headerFont, Element.ALIGN_LEFT, 5));
            infoTable.addCell(createCell(
                    order.getStaff() != null ?
                            order.getStaff().getFirstName() + " " + order.getStaff().getLastName() : "N/A",
                    normalFont, Element.ALIGN_LEFT, 5));

            infoTable.addCell(createCell("Ngày lập:", headerFont, Element.ALIGN_LEFT, 5));
            infoTable.addCell(createCell(
                    order.getOrderDate() != null ? DateUtil.formatDateTime(order.getOrderDate()) : "N/A",
                    normalFont, Element.ALIGN_LEFT, 5));

            infoTable.addCell(createCell("Phòng:", headerFont, Element.ALIGN_LEFT, 5));
            infoTable.addCell(createCell(
                    order.getRoom() != null ? order.getRoom().getRoomId() + " (" +
                            (order.getRoom().getRoomType() != null ? order.getRoom().getRoomType().getTypeName() : "N/A") + ")" : "N/A",
                    normalFont, Element.ALIGN_LEFT, 5));

            document.add(infoTable);
            document.add(new Paragraph("\n"));

            PdfPTable reservationTable = new PdfPTable(2);
            reservationTable.setWidthPercentage(100);
            reservationTable.setWidths(new float[]{1, 2});

            boolean isHourly = order.getCheckInTime() != null && order.getCheckOutTime() != null;

            reservationTable.addCell(createCell("Loại đặt phòng:", headerFont, Element.ALIGN_LEFT, 5));
            reservationTable.addCell(createCell(
                    isHourly ? "Theo giờ" : "Theo đêm",
                    normalFont, Element.ALIGN_LEFT, 5));

            if (!isHourly) {
                reservationTable.addCell(createCell("Check-in Date:", headerFont, Element.ALIGN_LEFT, 5));
                reservationTable.addCell(createCell(
                        order.getCheckInDate() != null ?
                                DateUtil.formatDate(order.getCheckInDate()) : "N/A",
                        normalFont, Element.ALIGN_LEFT, 5));

                reservationTable.addCell(createCell("Check-out Date:", headerFont, Element.ALIGN_LEFT, 5));
                reservationTable.addCell(createCell(
                        order.getCheckOutDate() != null ?
                                DateUtil.formatDate(order.getCheckOutDate()) : "N/A",
                        normalFont, Element.ALIGN_LEFT, 5));
            } else {
                reservationTable.addCell(createCell("Check-in Time:", headerFont, Element.ALIGN_LEFT, 5));
                reservationTable.addCell(createCell(
                        order.getCheckInTime() != null ?
                                DateUtil.formatDateTime(order.getCheckInTime()) : "N/A",
                        normalFont, Element.ALIGN_LEFT, 5));

                reservationTable.addCell(createCell("Check-out Time:", headerFont, Element.ALIGN_LEFT, 5));
                reservationTable.addCell(createCell(
                        order.getCheckOutTime() != null ?
                                DateUtil.formatDateTime(order.getCheckOutTime()) : "N/A",
                        normalFont, Element.ALIGN_LEFT, 5));
            }

            reservationTable.addCell(createCell("Thời gian:", headerFont, Element.ALIGN_LEFT, 5));
            reservationTable.addCell(createCell(
                    isHourly ?
                            "Số giờ: " + (order.getCheckOutTime() != null && order.getCheckInTime() != null ?
                                    String.valueOf((int) Math.ceil(
                                            (order.getCheckOutTime().getTime() - order.getCheckInTime().getTime()) /
                                                    (60.0 * 60 * 1000))) : "1")
                            : "Số đêm: " + (order.getNumberOfNights() != null ? order.getNumberOfNights() : 1),
                    normalFont, Element.ALIGN_LEFT, 5));

            reservationTable.addCell(createCell("Giá phòng:", headerFont, Element.ALIGN_LEFT, 5));
            reservationTable.addCell(createCell(
                    isHourly ?
                            MoneyUtil.formatCurrency(order.getRoom().calculateHourlyRate(order.getCheckInTime())) + "/giờ"
                            : MoneyUtil.formatCurrency(order.getRoom().getPrice()) + "/đêm",
                    normalFont, Element.ALIGN_LEFT, 5));

            reservationTable.addCell(createCell("Phụ phí trễ:", headerFont, Element.ALIGN_LEFT, 5));
            reservationTable.addCell(createCell(
                    MoneyUtil.formatCurrency(order.getOverstayFee()) +
                            " (" + (reservation != null ? reservation.getOverstayUnits() : 0) +
                            (isHourly ? " giờ)" : " ngày)"),
                    normalFont, Element.ALIGN_LEFT, 5));

            document.add(reservationTable);
            document.add(new Paragraph("\n"));

            PdfPTable serviceTable = new PdfPTable(5);
            serviceTable.setWidthPercentage(100);
            serviceTable.setWidths(new float[]{0.5f, 3, 1, 2, 2});

            serviceTable.addCell(createCell("STT", headerFont, Element.ALIGN_CENTER, 5));
            serviceTable.addCell(createCell("Dịch vụ", headerFont, Element.ALIGN_CENTER, 5));
            serviceTable.addCell(createCell("SL", headerFont, Element.ALIGN_CENTER, 5));
            serviceTable.addCell(createCell("Đơn giá", headerFont, Element.ALIGN_CENTER, 5));
            serviceTable.addCell(createCell("Thành tiền", headerFont, Element.ALIGN_CENTER, 5));

            double totalServiceAmount = 0;
            int index = 1;
            if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
                for (OrderDetails detail : order.getOrderDetails()) {
                    if (detail.getService() != null) {
                        serviceTable.addCell(createCell(String.valueOf(index++), normalFont, Element.ALIGN_CENTER, 5));
                        serviceTable.addCell(createCell(detail.getService().getName(), normalFont, Element.ALIGN_LEFT, 5));
                        serviceTable.addCell(createCell(String.valueOf(detail.getQuantity()), normalFont, Element.ALIGN_CENTER, 5));
                        serviceTable.addCell(createCell(MoneyUtil.formatCurrency(detail.getService().getPrice()),
                                normalFont, Element.ALIGN_RIGHT, 5));
                        serviceTable.addCell(createCell(MoneyUtil.formatCurrency(detail.getLineTotalAmount()),
                                normalFont, Element.ALIGN_RIGHT, 5));
                        totalServiceAmount += detail.getLineTotalAmount();
                    }
                }
            }

            document.add(serviceTable);
            document.add(new Paragraph("\n"));

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            summaryTable.addCell(createCell("Tổng tiền phòng:", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.getTotalPrice()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Phụ phí trễ:", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.getOverstayFee()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Thuế (10%):", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.getTaxAmount()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Tổng tiền dịch vụ:", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(totalServiceAmount),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Phí dịch vụ (5%):", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.getServiceFee()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Tổng cộng:", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.calculateTotalPrice()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Tiền cọc:", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.getDepositAmount()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Số tiền đã trả:", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.getDepositAmount()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            summaryTable.addCell(createCell("Số tiền còn lại phải trả:", headerFont, Element.ALIGN_LEFT, 5));
            summaryTable.addCell(createCell(MoneyUtil.formatCurrency(order.calculateRemainingPayment()),
                    normalFont, Element.ALIGN_RIGHT, 5));

            document.add(summaryTable);

            Paragraph footer = new Paragraph("\nCảm ơn quý khách đã sử dụng dịch vụ!\nVui lòng kiểm tra kỹ hóa đơn ^_^", smallFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            if (Desktop.isDesktopSupported() && pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
                JOptionPane.showMessageDialog(this, "Hóa đơn đã được in và mở thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hóa đơn đã được in nhưng không thể mở tự động!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (DocumentException | IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in hoặc mở hóa đơn: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private PdfPCell createCell(String content, Font font, int alignment, float padding, int border) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(padding);
        cell.setBorder(border);
        return cell;
    }

    private PdfPCell createCell(String content, Font font, int alignment, float padding) {
        return createCell(content, font, alignment, padding, Rectangle.BOX);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new JPanel();
        jPanel5 = new JPanel();
        jPanel7 = new JPanel();
        jPanel8 = new JPanel();
        jLabel2 = new JLabel();
        jLabel4 = new JLabel();
        calendar_Orderdate_From = new ui.components.calendar.CustomCalendar();
        jPanel9 = new JPanel();
        jLabel3 = new JLabel();
        jLabel5 = new JLabel();
        calendar_Orderdate_To = new ui.components.calendar.CustomCalendar();
        jLabel11 = new JLabel();
        jPanel10 = new JPanel();
        jLabel6 = new JLabel();
        cbx_StatusOrder = new ui.components.combobox.StyledComboBox();
        jPanel11 = new JPanel();
        jPanel12 = new JPanel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        txt_Price_From = new JTextField();
        jPanel13 = new JPanel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        txt_Price_To = new JTextField();
        jLabel12 = new JLabel();
        jPanel14 = new JPanel();
        jLabel13 = new JLabel();
        txt_OrderID = new JTextField();
        jPanel15 = new JPanel();
        jLabel14 = new JLabel();
        txt_CustomerID = new JTextField();
        jPanel16 = new JPanel();
        jLabel15 = new JLabel();
        txt_StaffID = new JTextField();
        jLabel1 = new JLabel();
        btn_Search = new ui.components.button.ButtonCustom();
        btn_Clear = new ui.components.button.ButtonCancelCustom();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        btn_PrintOrder = new ui.components.button.ButtonCustom();
        btn_Reset = new ui.components.button.ButtonCustom();
        jPanel4 = new JPanel();
        table_ListOrder = new CustomTableButton();
        jPanel6 = new JPanel();
        jPanel17 = new JPanel();
        jLabel16 = new JLabel();
        jLabel17 = new JLabel();
        jLabel18 = new JLabel();
        jLabel19 = new JLabel();
        jPanel18 = new JPanel();
        lbl_RoomID_Value = new JLabel();
        lbl_Floor_Value = new JLabel();
        lbl_TypeRoom_Value = new JLabel();
        lbl_Capacity_Value = new JLabel();
        jPanel19 = new JPanel();
        jLabel20 = new JLabel();
        jLabel21 = new JLabel();
        jLabel22 = new JLabel();
        jLabel34 = new JLabel();
        jPanel20 = new JPanel();
        lbl_CustomerName_Value = new JLabel();
        lbl_OrderDate_Value = new JLabel();
        lbl_CheckInDateOrTime_Value = new JLabel();
        lbl_CheckoutDateOrTime_Value = new JLabel();
        jPanel21 = new JPanel();
        jLabel35 = new JLabel();
        jLabel36 = new JLabel();
        jLabel25 = new JLabel();
        jPanel22 = new JPanel();
        lbl_NumOfNightOrHour_Value = new JLabel();
        lbl_PriceOfNightOrHour_Value = new JLabel();
        lbl_NumOfFloating_Value = new JLabel();
        jPanel23 = new JPanel();
        jLabel23 = new JLabel();
        jPanel24 = new JPanel();
        jLabel24 = new JLabel();
        jPanel25 = new JPanel();
        table_ListService = new CustomTableButton();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));

        jPanel5.setBackground(new Color(255, 255, 255));

        jPanel7.setBackground(new Color(255, 255, 255));

        jPanel8.setBackground(new Color(255, 255, 255));
        jPanel8.setLayout(new GridLayout(3, 1, 0, 6));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel2.setText("Ngày lập hóa đơn");
        jPanel8.add(jLabel2);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13));
        jLabel4.setText("Từ ngày:");
        jPanel8.add(jLabel4);
        jPanel8.add(calendar_Orderdate_From);

        jPanel9.setBackground(new Color(255, 255, 255));
        jPanel9.setLayout(new GridLayout(3, 1, 0, 6));
        jPanel9.add(jLabel3);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13));
        jLabel5.setText("Đến ngày:");
        jPanel9.add(jLabel5);
        jPanel9.add(calendar_Orderdate_To);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel11.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel11.setText("-");

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel9, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11)
                                .addContainerGap())
        );

        jPanel10.setBackground(new Color(255, 255, 255));
        jPanel10.setLayout(new GridLayout(2, 1, 0, 5));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel6.setText("Trạng thái:");
        jPanel10.add(jLabel6);
        jPanel10.add(cbx_StatusOrder);

        jPanel11.setBackground(new Color(255, 255, 255));

        jPanel12.setBackground(new Color(255, 255, 255));
        jPanel12.setLayout(new GridLayout(3, 1, 0, 5));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel7.setText("Giá trị hóa đơn");
        jPanel12.add(jLabel7);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 13));
        jLabel8.setText("Từ:");
        jPanel12.add(jLabel8);

        txt_Price_From.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txt_Price_From.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel12.add(txt_Price_From);

        jPanel13.setBackground(new Color(255, 255, 255));
        jPanel13.setLayout(new GridLayout(3, 1, 0, 5));
        jPanel13.add(jLabel9);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 13));
        jLabel10.setText("Đến:");
        jPanel13.add(jLabel10);

        txt_Price_To.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txt_Price_To.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel13.add(txt_Price_To);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel12.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel12.setText("-");

        GroupLayout jPanel11Layout = new GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel12, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel13, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel12, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12)
                                .addContainerGap())
        );

        jPanel14.setBackground(new Color(255, 255, 255));
        jPanel14.setLayout(new GridLayout(2, 1, 0, 5));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel13.setText("Mã hóa đơn:");
        jPanel14.add(jLabel13);

        txt_OrderID.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txt_OrderID.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel14.add(txt_OrderID);

        jPanel15.setBackground(new Color(255, 255, 255));
        jPanel15.setLayout(new GridLayout(2, 1, 0, 5));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel14.setText("Mã khách hàng:");
        jPanel15.add(jLabel14);

        txt_CustomerID.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txt_CustomerID.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel15.add(txt_CustomerID);

        jPanel16.setBackground(new Color(255, 255, 255));
        jPanel16.setLayout(new GridLayout(2, 1, 0, 5));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel15.setText("Mã nhân viên bán hàng:");
        jPanel16.add(jLabel15);

        txt_StaffID.setFont(new java.awt.Font("Segoe UI", 0, 13));
        txt_StaffID.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel16.add(txt_StaffID);

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel11, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel10, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel15, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel14, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel16, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel10, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel11, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel14, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel15, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel16, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 17));
        jLabel1.setForeground(new Color(153, 153, 255));
        jLabel1.setText("Tìm kiếm hóa đơn");

        btn_Search.setText("Tìm kiếm");

        btn_Clear.setText("Xóa rỗng");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btn_Search, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btn_Clear, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel1)
                                .addGap(20, 20, 20)
                                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btn_Search, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_Clear, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(10, Short.MAX_VALUE))
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 700));

        jPanel2.setBackground(new Color(255, 255, 255));

        jPanel3.setBackground(new Color(255, 255, 255));

        btn_PrintOrder.setText("In hóa đơn");

        btn_Reset.setText("Tải lại");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(btn_Reset, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
                                .addGap(59, 59, 59)
                                .addComponent(btn_PrintOrder, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btn_Reset, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_PrintOrder, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8))
        );

        jPanel4.setBackground(new Color(255, 255, 255));
        jPanel4.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel4.setLayout(new BorderLayout());

        table_ListOrder.setColumnNames(new String[] {"STT", "Mã hóa đơn", "Tên khách hàng", "Nhân viên xử lý", "Thời gian lập HĐ", "Trạng thái", "Tổng tiền"});
        table_ListOrder.setHeaderBackgroundColor(new Color(153, 153, 255));
        jPanel4.add(table_ListOrder, BorderLayout.CENTER);

        jPanel6.setBackground(new Color(255, 255, 255));
        jPanel6.setBorder(BorderFactory.createTitledBorder(""));

        jPanel17.setBackground(new Color(255, 255, 255));
        jPanel17.setLayout(new GridLayout(4, 1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel16.setText("Số phòng:");
        jPanel17.add(jLabel16);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel17.setText("Tầng:");
        jPanel17.add(jLabel17);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel18.setText("Loại phòng:");
        jPanel17.add(jLabel18);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel19.setText("Sức chứa:");
        jPanel17.add(jLabel19);

        jPanel18.setBackground(new Color(255, 255, 255));
        jPanel18.setLayout(new GridLayout(4, 1));

        lbl_RoomID_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel18.add(lbl_RoomID_Value);

        lbl_Floor_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel18.add(lbl_Floor_Value);

        lbl_TypeRoom_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel18.add(lbl_TypeRoom_Value);

        lbl_Capacity_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel18.add(lbl_Capacity_Value);

        jPanel19.setBackground(new Color(255, 255, 255));
        jPanel19.setLayout(new GridLayout(4, 1));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel20.setText("Khách hàng:");
        jPanel19.add(jLabel20);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel21.setText("Ngày lập:");
        jPanel19.add(jLabel21);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel22.setText("Ngày/giờ checkin:");
        jPanel19.add(jLabel22);

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel34.setText("Ngày/giờ checkout:");
        jPanel19.add(jLabel34);

        jPanel20.setBackground(new Color(255, 255, 255));
        jPanel20.setLayout(new GridLayout(4, 1));

        lbl_CustomerName_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel20.add(lbl_CustomerName_Value);

        lbl_OrderDate_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel20.add(lbl_OrderDate_Value);

        lbl_CheckInDateOrTime_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel20.add(lbl_CheckInDateOrTime_Value);

        lbl_CheckoutDateOrTime_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel20.add(lbl_CheckoutDateOrTime_Value);

        jPanel21.setBackground(new Color(255, 255, 255));
        jPanel21.setLayout(new GridLayout(4, 1));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel35.setText("Số đêm/số giờ:");
        jPanel21.add(jLabel35);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel36.setText("Giá đêm/giá giờ:");
        jPanel21.add(jLabel36);

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel25.setText("Số giờ trội:");
        jPanel21.add(jLabel25);

        jPanel22.setBackground(new Color(255, 255, 255));
        jPanel22.setLayout(new GridLayout(4, 1));

        lbl_NumOfNightOrHour_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel22.add(lbl_NumOfNightOrHour_Value);

        lbl_PriceOfNightOrHour_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel22.add(lbl_PriceOfNightOrHour_Value);

        lbl_NumOfFloating_Value.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jPanel22.add(lbl_NumOfFloating_Value);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jPanel17, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel18, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel19, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel20, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel21, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel22, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(118, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel17, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                        .addComponent(jPanel18, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel19, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel20, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel21, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel22, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel23.setBackground(new Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jLabel23.setForeground(new Color(153, 153, 255));
        jLabel23.setText("Chi tiết đặt phòng:");

        GroupLayout jPanel23Layout = new GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
                jPanel23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel23)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel23)
                                .addContainerGap())
        );

        jPanel24.setBackground(new Color(255, 255, 255));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jLabel24.setForeground(new Color(153, 153, 255));
        jLabel24.setText("Danh sách dịch vụ:");

        GroupLayout jPanel24Layout = new GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
                jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel24)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
                jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel24)
                                .addContainerGap())
        );

        jPanel25.setBackground(new Color(255, 255, 255));
        jPanel25.setLayout(new BorderLayout());

        table_ListService.setColumnNames(new String[] {"STT", "Mã dịch vụ", "Tên dịch vụ", "Giá", "Số lượng", "Thành tiền"});
        table_ListService.setHeaderBackgroundColor(new Color(153, 153, 255));
        jPanel25.add(table_ListService, BorderLayout.CENTER);

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel23, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel24, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel25, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel23, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel24, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel25, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 930, 700));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel jPanel1;
    private JPanel jPanel10;
    private JPanel jPanel11;
    private JPanel jPanel12;
    private JPanel jPanel13;
    private JPanel jPanel14;
    private JPanel jPanel15;
    private JPanel jPanel16;
    private JPanel jPanel17;
    private JPanel jPanel18;
    private JPanel jPanel19;
    private JPanel jPanel2;
    private JPanel jPanel20;
    private JPanel jPanel21;
    private JPanel jPanel22;
    private JPanel jPanel23;
    private JPanel jPanel24;
    private JPanel jPanel25;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel15;
    private JLabel jLabel16;
    private JLabel jLabel17;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel20;
    private JLabel jLabel21;
    private JLabel jLabel22;
    private JLabel jLabel23;
    private JLabel jLabel24;
    private JLabel jLabel25;
    private JLabel jLabel3;
    private JLabel jLabel34;
    private JLabel jLabel35;
    private JLabel jLabel36;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JLabel lbl_Capacity_Value;
    private JLabel lbl_CheckInDateOrTime_Value;
    private JLabel lbl_CheckoutDateOrTime_Value;
    private JLabel lbl_CustomerName_Value;
    private JLabel lbl_Floor_Value;
    private JLabel lbl_NumOfFloating_Value;
    private JLabel lbl_NumOfNightOrHour_Value;
    private JLabel lbl_OrderDate_Value;
    private JLabel lbl_PriceOfNightOrHour_Value;
    private JLabel lbl_RoomID_Value;
    private JLabel lbl_TypeRoom_Value;
    private CustomTableButton table_ListOrder;
    private CustomTableButton table_ListService;
    private JTextField txt_CustomerID;
    private JTextField txt_OrderID;
    private JTextField txt_Price_From;
    private JTextField txt_Price_To;
    private JTextField txt_StaffID;
    private ui.components.button.ButtonCancelCustom btn_Clear;
    private ui.components.button.ButtonCustom btn_PrintOrder;
    private ui.components.button.ButtonCustom btn_Reset;
    private ui.components.button.ButtonCustom btn_Search;
    private ui.components.calendar.CustomCalendar calendar_Orderdate_From;
    private ui.components.calendar.CustomCalendar calendar_Orderdate_To;
    private ui.components.combobox.StyledComboBox cbx_StatusOrder;
    // End of variables declaration//GEN-END:variables
}