/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.tabs;

import dao.CustomerDAOImpl;
import dao.ReservationDAOImpl;
import entities.BookingMethod;
import entities.Orders;
import entities.Reservation;
import entities.ReservationDetails;
import interfaces.CustomerDAO;
import interfaces.ReservationDAO;
import lombok.SneakyThrows;
import ui.components.table.CustomTableButton;
import ui.dialogs.Dialog_PaymentInfo;
import utils.DateUtil;
import utils.MoneyUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author TRAN LONG VU
 */
public class Tab_ListBooking extends JPanel {
    private CustomerDAO customerDAO;
    private ReservationDAO reservationDAO;
    private List<Reservation> currentReservations;
    private Reservation selectedReservation;
    private double floatingFee = 0.0;
    private double serviceFee = 0.0;
    private double taxAmount = 0.0;

    /**
     * Creates new form Tab_ListBooking
     */
    public Tab_ListBooking() {
        initComponents();
        initComboboxCustomerID();
        currentReservations = new ArrayList<>();
        loadReservations();
        setupListeners();
    }

    @SneakyThrows
    private void initComboboxCustomerID() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Chọn mã khách hàng");
        customerDAO = new CustomerDAOImpl();
        List<String> customerids = customerDAO.getAllCustomerIds();
        for (String customerID : customerids) {
            model.addElement(customerID);
        }
        cbx_CustomerID.setModel(model);
    }

    @SneakyThrows
    private void loadReservations() {
        reservationDAO = new ReservationDAOImpl();
        currentReservations = reservationDAO.getAllReservations();
        updateReservationTable(currentReservations);
    }

    private void updateReservationTable(List<Reservation> listReservations) {
        CustomTableButton.CustomTableModel model = table_ListReservation.getTableModel();
        model.clearData();
        for (Reservation r : listReservations) {
            Object[] dataRow = new Object[]{
                    r.getReservationId(),
                    r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName(),
                    r.getRoom().getRoomId(),
                    r.getBookingDate(),
                    String.format("%.0f VND", r.getTotalPrice()),
                    String.format("%.0f VND", r.getDepositAmount()),
                    String.format("%.0f VND", r.getRemainingAmount())
            };
            model.addRow(dataRow, null);
        }
    }

    private void updateServiceTable(Reservation reservation) {
        CustomTableButton.CustomTableModel model = table_ListService.getTableModel();
        model.clearData();
        if (reservation != null && reservation.getReservationDetails() != null) {
            for (ReservationDetails details : reservation.getReservationDetails()) {
                Object[] dataRow = new Object[]{
                        model.getRowCount() + 1,
                        details.getService().getServiceId(),
                        details.getService().getName(),
                        String.format("%.0f VND", details.getService().getPrice()),
                        details.getQuantity(),
                        String.format("%.0f VND", details.getLineTotalAmount())
                };
                model.addRow(dataRow, null);
            }
        }
    }

    private void updateReservationsDetails(Reservation reservation) {
        if (reservation == null) {
            clearReservationDetails();
            return;
        }

        // Cập nhật thông tin cơ bản
        lbl_RoomID_Value.setText(reservation.getRoom().getRoomId());
        lbl_Floor_Value.setText(String.valueOf(reservation.getRoom().getFloor()));
        lbl_TypeRoom_Value.setText(reservation.getRoom().getRoomType().getTypeName());
        lbl_Capacity_Value.setText(String.valueOf(reservation.getRoom().getCapacity()));
        lbl_CustomerName_Value.setText(reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName());
        lbl_BookingDate_Value.setText(DateUtil.formatDate(reservation.getBookingDate()));

        // Cập nhật thông tin thời gian tùy theo loại đặt phòng
        if (reservation.getBookingType() == Reservation.BookingType.NIGHT) {
            lbl_CheckInDateOrTime_Value.setText(DateUtil.formatDate(reservation.getCheckInDate()));
            lbl_CheckoutDateOrTime_Value.setText(DateUtil.formatDate(reservation.getCheckOutDate()));
            lbl_NumOfNightOrHour_Value.setText(reservation.getNumberOfNights() + " đêm");
            lbl_PriceOfNightOrHour_Value.setText(MoneyUtil.formatCurrency(reservation.getRoom().getPrice()) + "/đêm");
        } else {
            lbl_CheckInDateOrTime_Value.setText(DateUtil.formatDateTime(reservation.getCheckInTime()));
            lbl_CheckoutDateOrTime_Value.setText(DateUtil.formatDateTime(reservation.getCheckOutTime()));
            lbl_NumOfNightOrHour_Value.setText(reservation.getDurationHours() + " giờ");
            lbl_PriceOfNightOrHour_Value.setText(MoneyUtil.formatCurrency(reservation.getHourlyRate()) + "/giờ");
        }

        // Hiển thị thông tin phụ trội nếu có
        if (reservation.getOverstayUnits() > 0) {
            lbl_NumOfFloating_Value.setText(reservation.getOverstayUnits() +
                    (reservation.getBookingType() == Reservation.BookingType.NIGHT ? " ngày" : " giờ"));
        } else {
            lbl_NumOfFloating_Value.setText("0");
        }
    }

    private void clearReservationDetails() {
        lbl_RoomID_Value.setText("");
        lbl_Floor_Value.setText("");
        lbl_TypeRoom_Value.setText("");
        lbl_Capacity_Value.setText("");
        lbl_CustomerName_Value.setText("");
        lbl_BookingDate_Value.setText("");
        lbl_CheckInDateOrTime_Value.setText("");
        lbl_CheckoutDateOrTime_Value.setText("");
        lbl_NumOfNightOrHour_Value.setText("");
        lbl_PriceOfNightOrHour_Value.setText("");
        lbl_NumOfFloating_Value.setText("");
    }

    private void updateBookingInfo() {
        if (selectedReservation == null) {
            clearPaymentInfo();
            return;
        }

        selectedReservation.calculateTotalPrice();
        double roomPrice = selectedReservation.getTotalPrice() - selectedReservation.calculateTotalServicePrice();
        double serviceTotal = selectedReservation.calculateTotalServicePrice();
        double depositAmount = selectedReservation.getDepositAmount();
        double remainingAmount = selectedReservation.getRemainingAmount();
        double overstayFee = selectedReservation.getOverstayFee();
        double tax = (roomPrice + overstayFee) * 0.1;
        double serviceFee = serviceTotal * 0.05;
        double finalTotal = remainingAmount + overstayFee + tax + serviceFee;

        // Update UI
        lbl_LastTotalPrice_Value.setText(MoneyUtil.formatCurrency(roomPrice + serviceTotal));
        lbl_BookingMethod_Value.setText(selectedReservation.getBookingMethod() == BookingMethod.AT_THE_COUNTER ? "Tại quầy" : "Trực tuyến");
        lbl_Deposit_Value.setText(MoneyUtil.formatCurrency(depositAmount));
        lbl_RemainingAmount_Value.setText(MoneyUtil.formatCurrency(remainingAmount));


        lbl_FloatingFee_Value.setText(MoneyUtil.formatCurrency(overstayFee));
        lbl_ServiceFee_Value.setText(MoneyUtil.formatCurrency(serviceFee));
        lbl_Tax_Value.setText(MoneyUtil.formatCurrency(tax));
        lbl_TotalPrice_Value.setText(MoneyUtil.formatCurrency(finalTotal));
    }

    private void clearPaymentInfo() {
        lbl_LastTotalPrice_Value.setText("");
        lbl_BookingMethod_Value.setText("");
        lbl_Deposit_Value.setText("");
        lbl_RemainingAmount_Value.setText("");

        lbl_FloatingFee_Value.setText("");
        lbl_ServiceFee_Value.setText("");
        lbl_Tax_Value.setText("");
        lbl_TotalPrice_Value.setText("");
    }

    private List<Reservation> getSelectedReservations() {
       int[] selectedRows = table_ListReservation.getTable().getSelectedRows();
       List<Reservation> selectedReservations = new ArrayList<>();
       for (int row : selectedRows) {
           int modelRow = table_ListReservation.getTable().convertRowIndexToModel(row);
           Object[] rowData = table_ListReservation.getTableModel().getRowData(modelRow);
           String reservationId = (String) rowData[0];
           currentReservations.stream()
                   .filter(r -> r.getReservationId().equals(reservationId))
                   .findFirst()
                   .ifPresent(selectedReservations::add);
       }
       return selectedReservations;
    }

    private void updateButtonStates() {
        int selectedCount = table_ListReservation.getTable().getSelectedRowCount();

        if (selectedCount == 1) {
            int selectedRow = table_ListReservation.getTable().getSelectedRow();
            int modelRow = table_ListReservation.getTable().convertRowIndexToModel(selectedRow);
            Object[] rowData = table_ListReservation.getTableModel().getRowData(modelRow);
            String reservationId = (String) rowData[0];

            selectedReservation = currentReservations.stream()
                    .filter(r -> r.getReservationId().equals(reservationId))
                    .findFirst()
                    .orElse(null);

            if (selectedReservation != null) {
                btn_Checkin.setEnabled(selectedReservation.canCheckIn());
                btn_Checkout.setEnabled(selectedReservation.canCheckOut());
                btn_CacelBooking.setEnabled(selectedReservation.canCancel());
                btn_Pay.setEnabled(selectedReservation.getReservationStatus() == Reservation.STATUS_CHECKED_OUT);
                return;
            }
        }

        // Nếu không chọn hoặc chọn nhiều hơn 1
        btn_Checkin.setEnabled(false);
        btn_Checkout.setEnabled(false);
        btn_CacelBooking.setEnabled(false);
        btn_Pay.setEnabled(false);
    }

    @SneakyThrows
    private void searchReservations() {
        String keyword = txt_Search.getText().trim();
        if (!keyword.isEmpty()) {
            currentReservations = reservationDAO.searchReservations(keyword);
            updateReservationTable(currentReservations);
            updateButtonStates();
        } else {
            loadReservations();
            currentReservations.clear();
            updateButtonStates();
        }
    }

    private void setupListeners() {
        txt_Search.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchReservations();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchReservations();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchReservations();
            }
        });

        cbx_CustomerID.addActionListener(e -> {
            String selectedCustomerId = (String) cbx_CustomerID.getSelectedItem();
            if (selectedCustomerId != null && !selectedCustomerId.equals("Chọn mã khách hàng")) {
                try {
                    currentReservations = reservationDAO.getReservationsByCustomerId(selectedCustomerId);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                updateReservationTable(currentReservations);
                updateButtonStates();
            } else {
                loadReservations();
                currentReservations.clear();
                updateButtonStates();
            }
        });

        table_ListReservation.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table_ListReservation.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = table_ListReservation.getTable().convertRowIndexToModel(selectedRow);
                    Object[] rowData = table_ListReservation.getTableModel().getRowData(modelRow);
                    String reservationId = (String) rowData[0];
                    selectedReservation = currentReservations.stream()
                            .filter(r -> r.getReservationId().equals(reservationId))
                            .findFirst()
                            .orElse(null);
                    updateReservationsDetails(selectedReservation);
                    updateServiceTable(selectedReservation);
                    updateBookingInfo();
                    updateButtonStates();
                }
            }
        });

        btn_Clear.addActionListener(e -> {
            txt_Search.setText("");
            cbx_CustomerID.setSelectedIndex(0);
            loadReservations();
            clearReservationDetails();
            updateBookingInfo();
            updateButtonStates();
            initComboboxCustomerID();

            table_ListService.getTableModel().clearData();
            table_ListReservation.getTable().clearSelection();
            lbl_FloatingFee_Value.setText("");
            lbl_ServiceFee_Value.setText("");
            lbl_Tax_Value.setText("");
            lbl_TotalPrice_Value.setText("");
        });


        btn_Checkin.addActionListener(e -> {
            if (selectedReservation == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn đặt phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!selectedReservation.canCheckIn()) {
                JOptionPane.showMessageDialog(this, "Đơn đặt phòng không thể check-in!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn check-in đơn đặt phòng này?",
                    "Xác nhận check-in", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (reservationDAO.checkIn(selectedReservation.getReservationId())) {
                        JOptionPane.showMessageDialog(this,
                                "Check-in thành công!",
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadReservations();
                        updateButtonStates();
                        updateBookingInfo();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Check-in thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Lỗi khi check-in: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btn_Checkout.addActionListener(e -> {
            if (selectedReservation == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn đặt phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!selectedReservation.canCheckOut()) {
                JOptionPane.showMessageDialog(this, "Đơn đặt phòng không thể check-out!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn check-out đơn đặt phòng này?",
                    "Xác nhận check-out", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Date actualCheckOutTime = new Date();
                    if (reservationDAO.checkOut(selectedReservation.getReservationId(), actualCheckOutTime)) {
                        double overstayFee = selectedReservation.getOverstayFee();
                        String message = "Check-out thành công!";
                        if (overstayFee > 0) {
                            message += "\nPhí phụ trội: " + MoneyUtil.formatCurrency(overstayFee);
                        }
                        JOptionPane.showMessageDialog(this, message,
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadReservations();
                        updateBookingInfo();
                        updateButtonStates();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Check-out thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Lỗi khi check-out: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btn_CacelBooking.addActionListener(e -> {
            List<Reservation> selectedReservations = getSelectedReservations();
            if (selectedReservations.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một đơn đặt phòng để hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kiểm tra xem tất cả đơn đặt phòng có thuộc về cùng một khách hàng không
            String customerId = selectedReservations.get(0).getCustomer().getCustomerId();
            boolean sameCustomer = selectedReservations.stream()
                    .allMatch(r -> r.getCustomer().getCustomerId().equals(customerId));
            if (!sameCustomer) {
                JOptionPane.showMessageDialog(this, "Chỉ có thể hủy các đơn đặt phòng của cùng một khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra xem tất cả đơn đặt phòng có thể hủy không
            boolean allCanCancel = selectedReservations.stream().allMatch(Reservation::canCancel);
            if (!allCanCancel) {
                JOptionPane.showMessageDialog(this, "Một số đơn đặt phòng không thể hủy!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Xác nhận hủy
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn hủy " + selectedReservations.size() + " đơn đặt phòng?",
                    "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = reservationDAO.cancelMultipleReservations(selectedReservations);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Hủy thành công " + selectedReservations.size() + " đơn đặt phòng!",
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadReservations(); // Làm mới bảng
                        clearReservationDetails();
                        updateBookingInfo();
                        updateButtonStates();
                    } else {
                        JOptionPane.showMessageDialog(this, "Hủy thất bại. Vui lòng thử lại!",
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi hủy đơn đặt phòng: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btn_Pay.addActionListener(e -> {
            showPaymentDialog();
        });
    }

    private void showPaymentDialog() {
        if (table_ListReservation.getTable().getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn 1 đơn đặt phòng để thanh toán",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = table_ListReservation.getTable().getSelectedRow();
        int modelRow = table_ListReservation.getTable().convertRowIndexToModel(selectedRow);
        Object[] rowData = table_ListReservation.getTableModel().getRowData(modelRow);
        String reservationId = (String) rowData[0];

        Reservation selectedReservation = currentReservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);

        if (selectedReservation == null ||
                selectedReservation.getReservationStatus() != Reservation.STATUS_CHECKED_OUT) {
            JOptionPane.showMessageDialog(this,
                    "Đơn đặt phòng phải ở trạng thái CHECKED_OUT để thanh toán",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle("Thanh toán");
        dialog.setSize(650, 550);
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        Dialog_PaymentInfo paymentInfo = new Dialog_PaymentInfo(List.of(selectedReservation));
        dialog.add(paymentInfo, BorderLayout.CENTER);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        if (paymentInfo.isPaymentSuccessful()) {
            loadReservations();
            updateBookingInfo();
            updateButtonStates();
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new JPanel();
        txt_Search = new ui.components.textfield.SearchTextField();
        cbx_CustomerID = new ui.components.combobox.StyledComboBox();
        jPanel4 = new JPanel();
        jLabel1 = new JLabel();
        btn_Clear = new ui.components.button.ButtonCancelCustom();
        jPanel1 = new JPanel();
        table_ListReservation = new CustomTableButton();
        jPanel2 = new JPanel();
        table_ListService = new CustomTableButton();
        jPanel5 = new JPanel();
        jLabel2 = new JLabel();
        jPanel6 = new JPanel();
        jPanel14 = new JPanel();
        jLabel12 = new JLabel();
        jLabel14 = new JLabel();
        jLabel13 = new JLabel();
        jLabel19 = new JLabel();
        jPanel15 = new JPanel();
        lbl_RoomID_Value = new JLabel();
        lbl_Floor_Value = new JLabel();
        lbl_TypeRoom_Value = new JLabel();
        lbl_Capacity_Value = new JLabel();
        jPanel16 = new JPanel();
        jLabel18 = new JLabel();
        jLabel20 = new JLabel();
        jLabel21 = new JLabel();
        jLabel34 = new JLabel();
        jPanel17 = new JPanel();
        lbl_CustomerName_Value = new JLabel();
        lbl_BookingDate_Value = new JLabel();
        lbl_CheckInDateOrTime_Value = new JLabel();
        lbl_CheckoutDateOrTime_Value = new JLabel();
        jPanel18 = new JPanel();
        jLabel35 = new JLabel();
        jLabel36 = new JLabel();
        jLabel25 = new JLabel();
        jPanel19 = new JPanel();
        lbl_NumOfNightOrHour_Value = new JLabel();
        lbl_PriceOfNightOrHour_Value = new JLabel();
        lbl_NumOfFloating_Value = new JLabel();
        jPanel7 = new JPanel();
        jLabel3 = new JLabel();
        jPanel8 = new JPanel();
        pnl_Left_InforBooking = new JPanel();
        lbl_LastTotalPrice = new JLabel();
        lbl_BookingMethod = new JLabel();
        lbl_Deposit = new JLabel();
        lbl_RemainingAmount = new JLabel();
        pnl_Right_InforBooking = new JPanel();
        lbl_LastTotalPrice_Value = new JLabel();
        lbl_BookingMethod_Value = new JLabel();
        lbl_Deposit_Value = new JLabel();
        lbl_RemainingAmount_Value = new JLabel();
        jPanel9 = new JPanel();
        btn_Pay = new ui.components.button.ButtonCustom();
        btn_CacelBooking = new ui.components.button.ButtonCancelCustom();
        jPanel10 = new JPanel();
        jPanel12 = new JPanel();
        jLabel4 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jPanel13 = new JPanel();
        lbl_FloatingFee_Value = new JLabel();
        lbl_ServiceFee_Value = new JLabel();
        lbl_Tax_Value = new JLabel();
        lbl_TotalPrice_Value = new JLabel();
        jPanel11 = new JPanel();
        btn_Checkin = new ui.components.button.ButtonCustom();
        btn_Checkout = new ui.components.button.ButtonCustom();

        setBackground(new Color(255, 255, 255));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new Color(255, 255, 255));

        txt_Search.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        txt_Search.setPlaceholder("Nhập SĐT, CCCD hoặc tên khách hàng...");
        txt_Search.setPlaceholderColor(new Color(102, 102, 102));

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txt_Search, GroupLayout.PREFERRED_SIZE, 604, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbx_CustomerID, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_Search, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbx_CustomerID, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 45));

        jPanel4.setBackground(new Color(255, 255, 255));

        jLabel1.setFont(new Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new Color(153, 153, 255));
        jLabel1.setText("Danh sách đặt phòng:");

        btn_Clear.setText("Làm mới");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 526, Short.MAX_VALUE)
                .addComponent(btn_Clear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btn_Clear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 800, 30));

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setLayout(new BorderLayout());

        table_ListReservation.setColumnNames(new String[] {"Mã đặt phòng", "Khách hàng", "Số phòng", "Ngày đặt phòng", "Tổng tiền", "Cọc", "Còn lại"});
        table_ListReservation.setHeaderBackgroundColor(new Color(153, 153, 255));
        jPanel1.add(table_ListReservation, BorderLayout.CENTER);

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 85, 800, 250));

        jPanel2.setBackground(new Color(255, 255, 255));
        jPanel2.setLayout(new BorderLayout());

        table_ListService.setColumnNames(new String[] {"STT", "Mã dịch vụ", "Tên dịch vụ", "Giá", "Số lượng", "Thành tiền"});
        table_ListService.setHeaderBackgroundColor(new Color(153, 153, 255));
        jPanel2.add(table_ListService, BorderLayout.CENTER);

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 800, 150));

        jPanel5.setBackground(new Color(255, 255, 255));

        jLabel2.setFont(new Font("Segoe UI", 1, 16)); // NOI18N
        jLabel2.setForeground(new Color(153, 153, 255));
        jLabel2.setText("Chi tiết đặt phòng:");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(652, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 800, 30));

        jPanel6.setBackground(new Color(255, 255, 255));
        jPanel6.setBorder(BorderFactory.createTitledBorder(""));

        jPanel14.setBackground(new Color(255, 255, 255));
        jPanel14.setLayout(new GridLayout(4, 1));

        jLabel12.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Số phòng:");
        jPanel14.add(jLabel12);

        jLabel14.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Tầng:");
        jPanel14.add(jLabel14);

        jLabel13.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Loại phòng:");
        jPanel14.add(jLabel13);

        jLabel19.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Sức chứa:");
        jPanel14.add(jLabel19);

        jPanel15.setBackground(new Color(255, 255, 255));
        jPanel15.setLayout(new GridLayout(4, 1));

        lbl_RoomID_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel15.add(lbl_RoomID_Value);

        lbl_Floor_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel15.add(lbl_Floor_Value);

        lbl_TypeRoom_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel15.add(lbl_TypeRoom_Value);

        lbl_Capacity_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel15.add(lbl_Capacity_Value);

        jPanel16.setBackground(new Color(255, 255, 255));
        jPanel16.setLayout(new GridLayout(4, 1));

        jLabel18.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Khách hàng:");
        jPanel16.add(jLabel18);

        jLabel20.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Ngày đặt:");
        jPanel16.add(jLabel20);

        jLabel21.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Ngày/giờ checkin:");
        jPanel16.add(jLabel21);

        jLabel34.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setText("Ngày/giờ checkout:");
        jPanel16.add(jLabel34);

        jPanel17.setBackground(new Color(255, 255, 255));
        jPanel17.setLayout(new GridLayout(4, 1));

        lbl_CustomerName_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel17.add(lbl_CustomerName_Value);

        lbl_BookingDate_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel17.add(lbl_BookingDate_Value);

        lbl_CheckInDateOrTime_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel17.add(lbl_CheckInDateOrTime_Value);

        lbl_CheckoutDateOrTime_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel17.add(lbl_CheckoutDateOrTime_Value);

        jPanel18.setBackground(new Color(255, 255, 255));
        jPanel18.setLayout(new GridLayout(4, 1));

        jLabel35.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel35.setText("Số đêm/số giờ:");
        jPanel18.add(jLabel35);

        jLabel36.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel36.setText("Giá đêm/giá giờ:");
        jPanel18.add(jLabel36);

        jLabel25.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Số giờ trội:");
        jPanel18.add(jLabel25);

        jPanel19.setBackground(new Color(255, 255, 255));
        jPanel19.setLayout(new GridLayout(4, 1));

        lbl_NumOfNightOrHour_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel19.add(lbl_NumOfNightOrHour_Value);

        lbl_PriceOfNightOrHour_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel19.add(lbl_PriceOfNightOrHour_Value);

        lbl_NumOfFloating_Value.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jPanel19.add(lbl_NumOfFloating_Value);

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel14, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel15, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel16, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel17, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel18, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel19, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
            .addComponent(jPanel15, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel16, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel18, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel19, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 800, 150));

        jPanel7.setBackground(new Color(255, 255, 255));

        jLabel3.setFont(new Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new Color(153, 153, 255));
        jLabel3.setText("Danh sách dịch vụ:");

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(652, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 800, 30));

        jPanel8.setBackground(new Color(255, 255, 255));
        jPanel8.setBorder(BorderFactory.createTitledBorder(null, "Thông tin đặt phòng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 16), new Color(153, 153, 255))); // NOI18N

        pnl_Left_InforBooking.setBackground(new Color(255, 255, 255));
        pnl_Left_InforBooking.setLayout(new GridLayout(4, 1));

        lbl_LastTotalPrice.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_LastTotalPrice.setText("Tổng tiền:");
        pnl_Left_InforBooking.add(lbl_LastTotalPrice);

        lbl_BookingMethod.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_BookingMethod.setText("Hình thức đặt phòng:");
        pnl_Left_InforBooking.add(lbl_BookingMethod);

        lbl_Deposit.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Deposit.setText("Số tiền cọc:");
        pnl_Left_InforBooking.add(lbl_Deposit);

        lbl_RemainingAmount.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_RemainingAmount.setText("Số tiền còn lại:");
        pnl_Left_InforBooking.add(lbl_RemainingAmount);

        pnl_Right_InforBooking.setBackground(new Color(255, 255, 255));
        pnl_Right_InforBooking.setLayout(new GridLayout(4, 1, 0, 10));

        lbl_LastTotalPrice_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_LastTotalPrice_Value.setForeground(new Color(255, 0, 51));
        lbl_LastTotalPrice_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        pnl_Right_InforBooking.add(lbl_LastTotalPrice_Value);

        lbl_BookingMethod_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_BookingMethod_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        pnl_Right_InforBooking.add(lbl_BookingMethod_Value);

        lbl_Deposit_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_Deposit_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        pnl_Right_InforBooking.add(lbl_Deposit_Value);

        lbl_RemainingAmount_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_RemainingAmount_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        pnl_Right_InforBooking.add(lbl_RemainingAmount_Value);

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnl_Left_InforBooking, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_Right_InforBooking, GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnl_Left_InforBooking, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                    .addComponent(pnl_Right_InforBooking, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 10, 490, 260));

        jPanel9.setBackground(new Color(255, 255, 255));

        btn_Pay.setText("Thanh toán");

        btn_CacelBooking.setText("Hủy đặt phòng");

        GroupLayout jPanel9Layout = new GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Pay, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_CacelBooking, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(btn_Pay, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_CacelBooking, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 570, 490, 140));

        jPanel10.setBackground(new Color(255, 255, 255));
        jPanel10.setBorder(BorderFactory.createTitledBorder(null, "Thông tin thanh toán", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", 1, 16), new Color(153, 153, 255))); // NOI18N

        jPanel12.setBackground(new Color(255, 255, 255));
        jPanel12.setLayout(new GridLayout(4, 1));

        jLabel4.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("Phí phụ trội:");
        jPanel12.add(jLabel4);

        jLabel7.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Phí dịch vụ:");
        jPanel12.add(jLabel7);

        jLabel8.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setText("Thuế:");
        jPanel12.add(jLabel8);

        jLabel9.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setText("Thành tiền:");
        jPanel12.add(jLabel9);

        jPanel13.setBackground(new Color(255, 255, 255));
        jPanel13.setLayout(new GridLayout(4, 1, 0, 10));

        lbl_FloatingFee_Value.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        lbl_FloatingFee_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel13.add(lbl_FloatingFee_Value);

        lbl_ServiceFee_Value.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        lbl_ServiceFee_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel13.add(lbl_ServiceFee_Value);

        lbl_Tax_Value.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        lbl_Tax_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel13.add(lbl_Tax_Value);

        lbl_TotalPrice_Value.setFont(new Font("Segoe UI", 1, 16)); // NOI18N
        lbl_TotalPrice_Value.setForeground(new Color(255, 0, 51));
        lbl_TotalPrice_Value.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jPanel13.add(lbl_TotalPrice_Value);

        GroupLayout jPanel10Layout = new GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(jPanel13, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 320, 490, 240));

        jPanel11.setBackground(new Color(255, 255, 255));

        btn_Checkin.setText("Checkin");

        btn_Checkout.setText("Checkout");

        GroupLayout jPanel11Layout = new GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Checkin, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Checkout, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Checkin, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btn_Checkout, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 270, 490, 50));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.components.button.ButtonCancelCustom btn_CacelBooking;
    private ui.components.button.ButtonCustom btn_Checkin;
    private ui.components.button.ButtonCustom btn_Checkout;
    private ui.components.button.ButtonCancelCustom btn_Clear;
    private ui.components.button.ButtonCustom btn_Pay;
    private ui.components.combobox.StyledComboBox cbx_CustomerID;
    private JLabel jLabel1;
    private JLabel jLabel12;
    private JLabel jLabel13;
    private JLabel jLabel14;
    private JLabel jLabel18;
    private JLabel jLabel19;
    private JLabel jLabel2;
    private JLabel jLabel20;
    private JLabel jLabel21;
    private JLabel jLabel25;
    private JLabel jLabel3;
    private JLabel jLabel34;
    private JLabel jLabel35;
    private JLabel jLabel36;
    private JLabel jLabel4;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
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
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JLabel lbl_BookingDate_Value;
    private JLabel lbl_BookingMethod;
    private JLabel lbl_BookingMethod_Value;
    private JLabel lbl_Capacity_Value;
    private JLabel lbl_CheckInDateOrTime_Value;
    private JLabel lbl_CheckoutDateOrTime_Value;
    private JLabel lbl_CustomerName_Value;
    private JLabel lbl_Deposit;
    private JLabel lbl_Deposit_Value;
    private JLabel lbl_FloatingFee_Value;
    private JLabel lbl_Floor_Value;
    private JLabel lbl_LastTotalPrice;
    private JLabel lbl_LastTotalPrice_Value;
    private JLabel lbl_NumOfFloating_Value;
    private JLabel lbl_NumOfNightOrHour_Value;
    private JLabel lbl_PriceOfNightOrHour_Value;
    private JLabel lbl_RemainingAmount;
    private JLabel lbl_RemainingAmount_Value;
    private JLabel lbl_RoomID_Value;
    private JLabel lbl_ServiceFee_Value;
    private JLabel lbl_Tax_Value;
    private JLabel lbl_TotalPrice_Value;
    private JLabel lbl_TypeRoom_Value;
    private JPanel pnl_Left_InforBooking;
    private JPanel pnl_Right_InforBooking;
    private CustomTableButton table_ListReservation;
    private CustomTableButton table_ListService;
    private ui.components.textfield.SearchTextField txt_Search;
    // End of variables declaration//GEN-END:variables
}
