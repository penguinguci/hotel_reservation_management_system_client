/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.gui;

import dao.AccountDAOImpl;
import entities.Account;
import interfaces.AccountDAO;
import org.mindrot.jbcrypt.BCrypt;
import utils.CurrentAccount;
import utils.UpdatePasswords;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Random;

public class GUI_Login extends JFrame {

    private String verificationCode; // Lưu mã xác thực
    private String verifiedEmail; // Lưu email đã được xác thực

    public GUI_Login() {
        setUndecorated(true); // Ẩn các nút điều khiển
        setShape(new RoundRectangle2D.Double(0, 0, 800, 500, 30, 30));
        UpdatePasswords hashPass = new UpdatePasswords();
        hashPass.hashedPassword();
        initComponents();
        setLocationRelativeTo(null);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black); // Màu đường viền
        g2.setStroke(new BasicStroke(4)); // Độ dày của đường viền
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); // Vẽ đường viền
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        jPanel1 = new JPanel();
        Left = new JPanel();
        lb_Welcome = new JLabel();
        lb_Logo = new JLabel();
        Right = new JPanel();
        lb_Title = new JLabel();
        lb_Username = new JLabel();
        lb_Password = new JLabel();
        btn_Login = new ui.components.button.ButtonCustom();
        btn_Exit = new ui.components.button.ButtonCustom();
        roundedTextField1 = new ui.components.textfield.RoundedTextField();
        roundedPasswordField1 = new ui.components.textfield.RoundedPasswordField();
        btn_ForgotPassword = new ui.components.button.ButtonCustom();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login to Melody System");

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setPreferredSize(new Dimension(800, 500));
        jPanel1.setLayout(null);

        Left.setBackground(new Color(149, 145, 239));
        Left.setPreferredSize(new Dimension(400, 500));

        lb_Welcome.setFont(new Font("Segoe UI", 3, 24)); // NOI18N
        lb_Welcome.setForeground(new Color(255, 255, 255));
        lb_Welcome.setText("Melody Hotel xin chào!!!");

        lb_Logo.setIcon(new ImageIcon(getClass().getResource("/logo.png"))); // NOI18N

        GroupLayout LeftLayout = new GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
                LeftLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LeftLayout.createSequentialGroup()
                                .addGroup(LeftLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(LeftLayout.createSequentialGroup()
                                                .addGap(55, 55, 55)
                                                .addComponent(lb_Welcome))
                                        .addGroup(LeftLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addComponent(lb_Logo, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(34, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
                LeftLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(LeftLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(lb_Welcome)
                                .addGap(44, 44, 44)
                                .addComponent(lb_Logo)
                                .addContainerGap(130, Short.MAX_VALUE))
        );

        jPanel1.add(Left);
        Left.setBounds(0, 0, 400, 500);

        Right.setBackground(new Color(255, 255, 255));
        Right.setMinimumSize(new Dimension(400, 500));

        lb_Title.setFont(new Font("Segoe UI", 1, 24)); // NOI18N
        lb_Title.setForeground(new Color(149, 145, 239));
        lb_Title.setText("Đăng nhập");

        lb_Username.setBackground(new Color(102, 102, 102));
        lb_Username.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        lb_Username.setText("Tài khoản:");

        lb_Password.setBackground(new Color(102, 102, 102));
        lb_Password.setFont(new Font("Segoe UI", 0, 16)); // NOI18N
        lb_Password.setText("Mật khẩu:");

        btn_Login.setBackground(new Color(149, 145, 239));
        btn_Login.setText("Đăng nhập");
        btn_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_LoginActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btn_Exit.setText("Thoát");
        btn_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ExitActionPerformed(evt);
            }
        });

        roundedTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedTextField1ActionPerformed(evt);
            }
        });
        roundedPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    roundedPasswordField1ActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btn_ForgotPassword.setBackground(new Color(149, 145, 239));
        btn_ForgotPassword.setText("Quên mật khẩu");
        btn_ForgotPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btn_ForgotPasswordActionPerformed(evt);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        GroupLayout RightLayout = new GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
                RightLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lb_Title)
                                .addGap(131, 131, 131))
                        .addGroup(RightLayout.createSequentialGroup()
                                .addGroup(RightLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(RightLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btn_Exit, GroupLayout.PREFERRED_SIZE, 317, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.LEADING, RightLayout.createSequentialGroup()
                                                .addGap(39, 39, 39)
                                                .addGroup(RightLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(roundedTextField1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(roundedPasswordField1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(RightLayout.createSequentialGroup()
                                                                .addGroup(RightLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lb_Username)
                                                                        .addComponent(lb_Password))
                                                                .addGap(247, 247, 247))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                                                                .addComponent(btn_ForgotPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(btn_Login, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(44, Short.MAX_VALUE))
        );
        RightLayout.setVerticalGroup(
                RightLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(RightLayout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(lb_Title)
                                .addGap(38, 38, 38)
                                .addComponent(lb_Username)
                                .addGap(18, 18, 18)
                                .addComponent(roundedTextField1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lb_Password)
                                .addGap(18, 18, 18)
                                .addComponent(roundedPasswordField1, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addGroup(RightLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btn_Login, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btn_ForgotPassword, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btn_Exit, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel1.add(Right);
        Right.setBounds(400, 0, 400, 500);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void btn_LoginActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        String username = roundedTextField1.getText();
        String password = new String(roundedPasswordField1.getPassword());

        AccountDAO accountDAO = new AccountDAOImpl();
        Account account = accountDAO.getAccount(username);
        if (account != null && BCrypt.checkpw(password, account.getPassword())) {
            CurrentAccount.setCurrentAccount(account);
            //JOptionPane.showMessageDialog(this, "Login Successful", "Notification", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            GUI_Main guiMain = new GUI_Main();
            guiMain.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Đăng nhập thất bại", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void btn_ExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void roundedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == roundedTextField1) {
            roundedPasswordField1.requestFocus();
        }
    }


    private void btn_ForgotPasswordActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        final boolean[] continueTrying = {true};

        while (continueTrying[0]) {
            // Hiển thị dialog nhập email
            JTextField emailField = new JTextField();
            Object[] message = {
                    "Nhập email của bạn:", emailField
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Quên mật khẩu", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String email = emailField.getText().trim();
                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập email.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    continue; // Hiện lại dialog
                }

                AccountDAO accountDAO = new AccountDAOImpl();
                Account account = accountDAO.getAccountByEmail(email);

                if (account != null) {
                    // Tạo mã xác thực ngẫu nhiên
                    verificationCode = generateVerificationCode();
                    verifiedEmail = email;

                    // Tạo dialog tiến trình
                    JDialog progressDialog = new JDialog(this, "Đang xử lý", true);
                    progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                    progressDialog.setLayout(new BorderLayout());
                    JLabel progressLabel = new JLabel("Đang gửi mã xác thực...", SwingConstants.CENTER);
                    progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    progressDialog.add(progressLabel, BorderLayout.CENTER);
                    progressDialog.setSize(300, 100);
                    progressDialog.setLocationRelativeTo(this);

                    // Gửi email trong luồng riêng
                    SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                        @Override
                        protected Boolean doInBackground() {
                            return sendVerificationEmail(email, verificationCode);
                        }

                        @Override
                        protected void done() {
                            try {
                                boolean sent = get();
                                progressDialog.dispose(); // Đóng dialog tiến trình
                                if (sent) {
                                    // Hiển thị dialog nhập mã xác thực và mật khẩu mới
                                    showVerificationDialog(account);
                                    continueTrying[0] = false; // Thoát vòng lặp
                                } else {
                                    JOptionPane.showMessageDialog(GUI_Login.this, "Lỗi khi gửi email. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    // Tiếp tục vòng lặp để hiện lại dialog
                                }
                            } catch (Exception e) {
                                progressDialog.dispose();
                                JOptionPane.showMessageDialog(GUI_Login.this, "Lỗi hệ thống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    };

                    // Hiển thị dialog tiến trình và chạy worker
                    worker.execute();
                    progressDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Email không tồn tại trong hệ thống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    // Tiếp tục vòng lặp để hiện lại dialog
                }
            } else {
                continueTrying[0] = false;
            }
        }
    }


    private void roundedPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) throws RemoteException {
        if (evt.getSource() == roundedPasswordField1) {
            btn_LoginActionPerformed(evt);
        }
    }

    // Tạo mã xác thực ngẫu nhiên
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Mã 6 chữ số
        return String.valueOf(code);
    }

    // Gửi email chứa mã xác thực
    private boolean sendVerificationEmail(String toEmail, String code) {
        final String fromEmail = "vinhthai.2612@gmail.com"; // Thay bằng email của bạn
        final String password = "spkl cubi udxk zjmz"; // Thay bằng mật khẩu ứng dụng

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã xác thực đặt lại mật khẩu");
            message.setText("Mã xác thực của bạn là: " + code + "\nVui lòng sử dụng mã này để đặt lại mật khẩu.");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hiển thị dialog để nhập mã xác thực và mật khẩu mới
    private void showVerificationDialog(Account account) throws RemoteException {
        JTextField codeField = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        Object[] message = {
                "Nhập mã xác thực:", codeField,
                "Nhập mật khẩu mới:", newPasswordField,
                "Xác nhận mật khẩu mới:", confirmPasswordField
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Xác thực và đặt lại mật khẩu", JOptionPane.OK_CANCEL_OPTION);
        // Đoạn mã xử lý khi người dùng nhấn OK trong dialog
        if (option == JOptionPane.OK_OPTION) {
            boolean isCodeValid = false;

            // Vòng lặp để hiện dialog nhập mã xác thực cho đến khi mã hợp lệ hoặc người dùng hủy
            while (!isCodeValid) {
                String enteredCode = codeField.getText().trim();
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                // Kiểm tra mật khẩu xác nhận
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return; // Thoát nếu mật khẩu không khớp
                }

                // Kiểm tra mã xác thực
                if (enteredCode.equals(verificationCode)) {
                    AccountDAO accountDAO = new AccountDAOImpl();
                    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    account.setPassword(hashedPassword);
                    accountDAO.updateAccount(account);
                    JOptionPane.showMessageDialog(this, "Đặt lại mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    isCodeValid = true; // Mã hợp lệ, thoát vòng lặp
                } else {
                    // Hiển thị thông báo mã không hợp lệ
                    JOptionPane.showMessageDialog(this, "Mã xác thực không hợp lệ", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    // Tạo lại dialog để nhập mã xác thực
                    JTextField newCodeField = new JTextField(10);
                    JPasswordField newPasswordFieldAgain = new JPasswordField(10);
                    JPasswordField confirmPasswordFieldAgain = new JPasswordField(10);

                    JPanel panel = new JPanel(new GridLayout(3, 2));
                    panel.add(new JLabel("Nhập mã xác thực:"));
                    panel.add(newCodeField);
                    panel.add(new JLabel("Mật khẩu mới:"));
                    panel.add(newPasswordFieldAgain);
                    panel.add(new JLabel("Xác nhận mật khẩu:"));
                    panel.add(confirmPasswordFieldAgain);

                    // Hiện lại dialog nhập mã
                    int newOption = JOptionPane.showConfirmDialog(
                            this,

                            panel,
                            "Nhập mã xác thực",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    // Nếu người dùng nhấn Cancel hoặc đóng dialog, thoát vòng lặp
                    if (newOption != JOptionPane.OK_OPTION) {
                        return; // Thoát nếu người dùng hủy
                    }

                    // Cập nhật các giá trị từ dialog mới
                    codeField = newCodeField;
                    newPasswordField = newPasswordFieldAgain;
                    confirmPasswordField = confirmPasswordFieldAgain;
                }
            }
        }
    }

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new GUI_Login().setVisible(true));
    }

    // Variables declaration
    private JPanel Left;
    private JPanel Right;
    private ui.components.button.ButtonCustom btn_Exit;
    private ui.components.button.ButtonCustom btn_ForgotPassword;
    private ui.components.button.ButtonCustom btn_Login;
    private JPanel jPanel1;
    private JLabel lb_Logo;
    private JLabel lb_Password;
    private JLabel lb_Title;
    private JLabel lb_Username;
    private JLabel lb_Welcome;
    private ui.components.textfield.RoundedPasswordField roundedPasswordField1;
    private ui.components.textfield.RoundedTextField roundedTextField1;
}