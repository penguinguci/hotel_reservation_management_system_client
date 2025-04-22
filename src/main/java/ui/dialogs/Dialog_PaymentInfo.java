/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.dialogs;

import dao.OrderDAOImpl;
import dao.ReservationDAOImpl;
import dao.StaffDAOImpl;
import entities.*;
import interfaces.OrderDAO;
import interfaces.ReservationDAO;
import interfaces.StaffDAO;
import lombok.SneakyThrows;
import ultilities.GenerateString;
import utils.CurrentAccount;
import utils.MoneyUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author TRAN LONG VU
 */
public class Dialog_PaymentInfo extends JPanel {
    private List<Reservation> reservations;
    private double totalPrice;       // Tổng tiền ban đầu (phòng + dịch vụ)
    private double serviceTotal;     // Tổng tiền dịch vụ
    private double depositAmount;   // Tiền cọc
    private double remainingAmount; // Tiền còn lại
    private double overstayFee;     // Phí phụ trội
    private double taxPrice;        // Thuế
    private double serviceFee;      // Phí dịch vụ
    private double finalPrice;      // Tổng tiền cuối cùng
    private double customerPayment; // Tiền khách đưa
    private boolean paymentSuccessful;
    private ReservationDAO reservationDAO;
    private OrderDAO orderDAO;
    private StaffDAO staffDAO;
    private boolean isUpdating = false;

    @SneakyThrows
    public Dialog_PaymentInfo(List<Reservation> reservations) {
        this.reservations = reservations;
        this.reservationDAO = new ReservationDAOImpl();
        this.orderDAO = new OrderDAOImpl();
        this.staffDAO = new StaffDAOImpl();
        this.paymentSuccessful = false;
        calculatePaymentDetails();
        initComponents();
        setupPaymentMethodComboBox();
        updatePaymentLabels();
        setupListeners();
        txt_CustomerPayment.setEnabled(false);
    }

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    private void calculatePaymentDetails() {
        Reservation reservation = reservations.get(0);

        // Room price + services
        totalPrice = reservation.calculateTotalPrice() - reservation.calculateTotalServicePrice();
        serviceTotal = reservation.calculateTotalServicePrice();
        depositAmount = reservation.getDepositAmount();
        remainingAmount = reservation.getRemainingAmount();
        overstayFee = reservation.getOverstayFee();

        serviceFee = serviceTotal * 0.05;

        taxPrice = (totalPrice + overstayFee) * 0.1;

        // Final total
        finalPrice = remainingAmount + overstayFee + serviceFee + taxPrice;

        customerPayment = 0;
    }

    private void setupPaymentMethodComboBox() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Chọn phương thức thanh toán");
        model.addElement("Tiền mặt");
        model.addElement("Thẻ ngân hàng");
        model.addElement("Chuyển khoản");
        model.addElement("Ví điện tử");
        cbx_PaymentMethod.setModel(model);
    }

    private void updatePaymentLabels() {
        lbl_TotalPrice_Value.setText(MoneyUtil.formatCurrency(totalPrice));
        lbl_TaxPrice_Value.setText(MoneyUtil.formatCurrency(taxPrice));
        lbl_FeeService_Value.setText(MoneyUtil.formatCurrency(serviceFee));
        lbl_overstayFee_Value.setText(MoneyUtil.formatCurrency(overstayFee));
        lbl_FinalPrice_Value.setText(MoneyUtil.formatCurrency(finalPrice));
        lbl_NeedToPay_Value.setText(MoneyUtil.formatCurrency(finalPrice));
        lbl_RemainingMoney_Value.setText(MoneyUtil.formatCurrency(customerPayment - finalPrice));
    }


    private void updateCustomerPayment(double amount) {
        customerPayment = amount;
        isUpdating = true; // Đánh dấu đang cập nhật
        txt_CustomerPayment.setText(String.format("%.0f", customerPayment));
        isUpdating = false; // Kết thúc cập nhật
        updatePaymentLabels();
    }

    private void processPayment() {
        if (reservations.size() != 1) {
            JOptionPane.showMessageDialog(this,
                    "Chỉ có thể thanh toán 1 đơn đặt phòng mỗi lần",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Reservation reservation = reservations.get(0);

        String selectedPaymentMethod = (String) cbx_PaymentMethod.getSelectedItem();
        if (selectedPaymentMethod == null || selectedPaymentMethod.equals("Chọn phương thức thanh toán")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phương thức thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (selectedPaymentMethod.equals("Tiền mặt") && customerPayment < finalPrice) {
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa không đủ để thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Tạo đơn hàng
            Orders order = new Orders();
            order.setOrderId(GenerateString.generateOrderID());
            order.setCustomer(reservation.getCustomer());
            order.setOrderDate(new Date());
            order.setStatus(1); // Đã thanh toán
            order.setPaymentMethod(PaymentMethod.CASH);
            order.setDepositAmount(depositAmount);
            order.setRemainingAmount(0);
            order.setOverstayFee(overstayFee);
            order.setServiceFee(serviceFee);
            order.setTaxAmount(taxPrice);
            order.setTotalPrice(finalPrice);
            order.setRoom(reservation.getRoom());

            if (reservation.getBookingType() == Reservation.BookingType.HOUR) {
                order.setCheckInTime(reservation.getCheckInTime());
                order.setCheckOutTime(reservation.getCheckOutTime());
            } else {
                order.setCheckInTime(reservation.getCheckInDate());
                order.setCheckOutTime(reservation.getCheckOutDate());
            }
            order.setNumberOfNights(reservation.getNumberOfNights());
            order.setDurationHours(reservation.getDurationHours());

            // Lưu thông tin nhân viên
            Account account = CurrentAccount.getCurrentAccount();
            Staff staff = staffDAO.findById(account.getStaff().getStaffId());
            order.setStaff(staff);

            // Lưu chi tiết dịch vụ
            Set<OrderDetails> orderDetails = new HashSet<>();
            if (reservation.getReservationDetails() != null) {
                for (ReservationDetails rd : reservation.getReservationDetails()) {
                    OrderDetails od = new OrderDetails();
                    od.setOrders(order);
                    od.setService(rd.getService());
                    od.setQuantity(rd.getQuantity());
                    od.setLineTotalAmount(rd.calculateLineTotal());
                    orderDetails.add(od);
                }
            }
            order.setOrderDetails(orderDetails);

            // Lưu đơn hàng vào cơ sở dữ liệu
            if (!orderDAO.createOrder(order)) {
                throw new RuntimeException("Không thể tạo đơn hàng!");
            }

            // Cập nhật trạng thái đặt phòng
            reservation.setRemainingAmount(0);
            reservation.setReservationStatus(1); // Đã thanh toán
            reservationDAO.update(reservation);

            paymentSuccessful = true;
            String message = "Thanh toán thành công!";
            if (selectedPaymentMethod.equals("Tiền mặt") && customerPayment > finalPrice) {
                message += "\nTiền trả lại: " + MoneyUtil.formatCurrency(customerPayment - finalPrice);
            }
            JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            Window dialog = SwingUtilities.getWindowAncestor(this);
            dialog.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thanh toán: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupListeners() {
        // Listener cho các nút preset
        btn_1k.addActionListener(e -> updateCustomerPayment(customerPayment + 1000));
        btn_2k.addActionListener(e -> updateCustomerPayment(customerPayment + 2000));
        btn_5k.addActionListener(e -> updateCustomerPayment(customerPayment + 5000));
        btn_10k.addActionListener(e -> updateCustomerPayment(customerPayment + 10000));
        btn_20k.addActionListener(e -> updateCustomerPayment(customerPayment + 20000));
        btn_50k.addActionListener(e -> updateCustomerPayment(customerPayment + 50000));
        btn_100k.addActionListener(e -> updateCustomerPayment(customerPayment + 100000));
        btn_200k.addActionListener(e -> updateCustomerPayment(customerPayment + 200000));
        btn_500k.addActionListener(e -> updateCustomerPayment(customerPayment + 500000));
        btn_Sufficient.addActionListener(e -> updateCustomerPayment(finalPrice));
        btn_Delete.addActionListener(e -> updateCustomerPayment(0));

        // Listener cho nút Back và Complete
        btn_Back.addActionListener(e -> {
            Window dialog = SwingUtilities.getWindowAncestor(this);
            dialog.dispose();
        });
        btn_Complete.addActionListener(e -> processPayment());

        // Listener cho txt_CustomerPayment
        txt_CustomerPayment.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUpdating) {
                    updateCustomerPaymentFromText();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUpdating) {
                    updateCustomerPaymentFromText();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!isUpdating) {
                    updateCustomerPaymentFromText();
                }
            }

            private void updateCustomerPaymentFromText() {
                try {
                    String text = txt_CustomerPayment.getText().trim();
                    double amount = text.isEmpty() ? 0 : Double.parseDouble(text);
                    if (amount < 0) {
                        JOptionPane.showMessageDialog(Dialog_PaymentInfo.this, "Số tiền không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        updateCustomerPayment(0);
                    } else {
                        customerPayment = amount;
                        updatePaymentLabels(); // Chỉ cập nhật labels, không setText
                    }
                } catch (NumberFormatException ex) {
                    if (!txt_CustomerPayment.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(Dialog_PaymentInfo.this, "Vui lòng nhập số tiền hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        updateCustomerPayment(0);
                    }
                }
            }
        });

        // Listener cho combobox PaymentMethod
        cbx_PaymentMethod.addActionListener(e -> {
            String selectedMethod = (String) cbx_PaymentMethod.getSelectedItem();
            if ("Tiền mặt".equals(selectedMethod)) {
                txt_CustomerPayment.setEnabled(true);
                btn_1k.setEnabled(true);
                btn_2k.setEnabled(true);
                btn_5k.setEnabled(true);
                btn_10k.setEnabled(true);
                btn_20k.setEnabled(true);
                btn_50k.setEnabled(true);
                btn_100k.setEnabled(true);
                btn_200k.setEnabled(true);
                btn_500k.setEnabled(true);
                btn_Sufficient.setEnabled(true);
                btn_Delete.setEnabled(true);
            } else {
                txt_CustomerPayment.setEnabled(false);
                btn_1k.setEnabled(false);
                btn_2k.setEnabled(false);
                btn_5k.setEnabled(false);
                btn_10k.setEnabled(false);
                btn_20k.setEnabled(false);
                btn_50k.setEnabled(false);
                btn_100k.setEnabled(false);
                btn_200k.setEnabled(false);
                btn_500k.setEnabled(false);
                btn_Sufficient.setEnabled(false);
                btn_Delete.setEnabled(false);
                updateCustomerPayment(finalPrice); // Đặt số tiền bằng số tiền cần thanh toán
            }
        });
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jPanel3 = new JPanel();
        jPanel5 = new JPanel();
        jLabel2 = new JLabel();
        lbl_TotalPrice_Value = new JLabel();
        jLabel3 = new JLabel();
        lbl_overstayFee_Value = new JLabel();
        jLabel4 = new JLabel();
        lbl_TaxPrice_Value = new JLabel();
        jLabel6 = new JLabel();
        lbl_FeeService_Value = new JLabel();
        jLabel8 = new JLabel();
        lbl_FinalPrice_Value = new JLabel();
        jLabel10 = new JLabel();
        cbx_PaymentMethod = new ui.components.combobox.StyledComboBox();
        jPanel4 = new JPanel();
        jPanel6 = new JPanel();
        jLabel11 = new JLabel();
        lbl_NeedToPay_Value = new JLabel();
        jLabel13 = new JLabel();
        lbl_RemainingMoney_Value = new JLabel();
        jPanel7 = new JPanel();
        jPanel9 = new JPanel();
        btn_1k = new ui.components.button.ButtonCustom();
        btn_2k = new ui.components.button.ButtonCustom();
        btn_5k = new ui.components.button.ButtonCustom();
        btn_10k = new ui.components.button.ButtonCustom();
        btn_20k = new ui.components.button.ButtonCustom();
        btn_50k = new ui.components.button.ButtonCustom();
        btn_100k = new ui.components.button.ButtonCustom();
        btn_200k = new ui.components.button.ButtonCustom();
        btn_500k = new ui.components.button.ButtonCustom();
        jPanel10 = new JPanel();
        btn_Sufficient = new ui.components.button.ButtonCustom();
        btn_Back = new ui.components.button.ButtonCancelCustom();
        btn_Complete = new ui.components.button.ButtonCompleteCustom();
        jPanel8 = new JPanel();
        txt_CustomerPayment = new JTextField();
        btn_Delete = new ui.components.button.ButtonCustom();
        jLabel15 = new JLabel();

        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(600, 520));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new Color(255, 255, 255));

        jLabel1.setFont(new Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new Color(102, 102, 255));
        jLabel1.setText("Thông tin thanh toán");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(411, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 60));

        jPanel2.setBackground(new Color(255, 255, 255));

        jPanel3.setBackground(new Color(255, 255, 255));

        jPanel5.setBackground(new Color(255, 255, 255));
        jPanel5.setLayout(new GridLayout(12, 1));

        jLabel2.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Tổng tiền:");
        jPanel5.add(jLabel2);

        lbl_TotalPrice_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_TotalPrice_Value.setForeground(new Color(153, 153, 255));
        jPanel5.add(lbl_TotalPrice_Value);

        jLabel3.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Phí phụ trội:");
        jPanel5.add(jLabel3);

        lbl_overstayFee_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_overstayFee_Value.setForeground(new Color(153, 153, 255));
        jPanel5.add(lbl_overstayFee_Value);

        jLabel4.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Thuế (10%):");
        jPanel5.add(jLabel4);

        lbl_TaxPrice_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_TaxPrice_Value.setForeground(new Color(153, 153, 255));
        jPanel5.add(lbl_TaxPrice_Value);

        jLabel6.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Phí dich vụ (5%):");
        jPanel5.add(jLabel6);

        lbl_FeeService_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_FeeService_Value.setForeground(new Color(153, 153, 255));
        jPanel5.add(lbl_FeeService_Value);

        jLabel8.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Tổng cộng:");
        jPanel5.add(jLabel8);

        lbl_FinalPrice_Value.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        lbl_FinalPrice_Value.setForeground(new Color(153, 153, 255));
        jPanel5.add(lbl_FinalPrice_Value);

        jLabel10.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Phương thức thanh toán:");
        jPanel5.add(jLabel10);
        jPanel5.add(cbx_PaymentMethod);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jPanel5, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new Color(255, 255, 255));

        jPanel6.setBackground(new Color(255, 255, 255));
        jPanel6.setLayout(new GridLayout(4, 0));

        jLabel11.setFont(new Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Cần thanh toán:");
        jPanel6.add(jLabel11);

        lbl_NeedToPay_Value.setFont(new Font("Segoe UI", 1, 16)); // NOI18N
        lbl_NeedToPay_Value.setForeground(new Color(255, 0, 51));
        jPanel6.add(lbl_NeedToPay_Value);

        jLabel13.setFont(new Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Trả lại khách:");
        jPanel6.add(jLabel13);

        lbl_RemainingMoney_Value.setFont(new Font("Segoe UI", 1, 16)); // NOI18N
        lbl_RemainingMoney_Value.setForeground(new Color(255, 0, 51));
        jPanel6.add(lbl_RemainingMoney_Value);

        jPanel7.setBackground(new Color(255, 255, 255));

        jPanel9.setBackground(new Color(255, 255, 255));
        jPanel9.setLayout(new GridLayout(3, 3, 10, 10));

        btn_1k.setText("1,000");
        btn_1k.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_1kActionPerformed(evt);
            }
        });
        jPanel9.add(btn_1k);

        btn_2k.setText("2,000");
        jPanel9.add(btn_2k);

        btn_5k.setText("5,000");
        jPanel9.add(btn_5k);

        btn_10k.setText("10,000");
        jPanel9.add(btn_10k);

        btn_20k.setText("20,000");
        jPanel9.add(btn_20k);

        btn_50k.setText("50,000");
        jPanel9.add(btn_50k);

        btn_100k.setText("100,000");
        jPanel9.add(btn_100k);

        btn_200k.setText("200,000");
        jPanel9.add(btn_200k);

        btn_500k.setText("500,000");
        jPanel9.add(btn_500k);

        jPanel10.setBackground(new Color(255, 255, 255));
        jPanel10.setLayout(new GridLayout(1, 0, 10, 0));

        btn_Sufficient.setText("Vừa đủ");
        jPanel10.add(btn_Sufficient);

        btn_Back.setText("Quay lại");
        jPanel10.add(btn_Back);

        btn_Complete.setText("Hoàn tất");
        jPanel10.add(btn_Complete);

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel10, GroupLayout.PREFERRED_SIZE, 379, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel10, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBackground(new Color(255, 255, 255));

        txt_CustomerPayment.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        txt_CustomerPayment.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        txt_CustomerPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CustomerPaymentActionPerformed(evt);
            }
        });

        btn_Delete.setText("Xóa");

        GroupLayout jPanel8Layout = new GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(txt_CustomerPayment)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Delete, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(txt_CustomerPayment)
                    .addComponent(btn_Delete, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel15.setFont(new Font("Segoe UI", 0, 15)); // NOI18N
        jLabel15.setText("Tiền khách đưa:");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addGap(4, 4, 4)
                .addComponent(jPanel8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 640, 460));
    }// </editor-fold>//GEN-END:initComponents

    private void txt_CustomerPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CustomerPaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_CustomerPaymentActionPerformed

    private void btn_1kActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_1kActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_1kActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.components.button.ButtonCustom btn_100k;
    private ui.components.button.ButtonCustom btn_10k;
    private ui.components.button.ButtonCustom btn_1k;
    private ui.components.button.ButtonCustom btn_200k;
    private ui.components.button.ButtonCustom btn_20k;
    private ui.components.button.ButtonCustom btn_2k;
    private ui.components.button.ButtonCustom btn_500k;
    private ui.components.button.ButtonCustom btn_50k;
    private ui.components.button.ButtonCustom btn_5k;
    private ui.components.button.ButtonCancelCustom btn_Back;
    private ui.components.button.ButtonCompleteCustom btn_Complete;
    private ui.components.button.ButtonCustom btn_Delete;
    private ui.components.button.ButtonCustom btn_Sufficient;
    private ui.components.combobox.StyledComboBox cbx_PaymentMethod;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel13;
    private JLabel jLabel15;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel6;
    private JLabel jLabel8;
    private JPanel jPanel1;
    private JPanel jPanel10;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JPanel jPanel7;
    private JPanel jPanel8;
    private JPanel jPanel9;
    private JLabel lbl_FeeService_Value;
    private JLabel lbl_FinalPrice_Value;
    private JLabel lbl_NeedToPay_Value;
    private JLabel lbl_RemainingMoney_Value;
    private JLabel lbl_TaxPrice_Value;
    private JLabel lbl_TotalPrice_Value;
    private JLabel lbl_overstayFee_Value;
    private JTextField txt_CustomerPayment;
    // End of variables declaration//GEN-END:variables
}
