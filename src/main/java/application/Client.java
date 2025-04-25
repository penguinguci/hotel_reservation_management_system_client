package application;

import interfaces.*;
import loader.SplashLoading;
import ui.gui.GUI_Login;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements ClientCallback {
    private static final String HOST = "localhost"; // Thay bằng IP của máy server
    private static final int PORT = 2004;
    private static final int STEP_DELAY = 800;

    // RMI Service references
    public static AccountDAO accountDAO;
    public static AmountCustomerDAO amountCustomerDAO;
    public static CustomerDAO customerDAO;
    public static OrderDAO orderDAO;
    public static ReservationDAO reservationDAO;
    public static RevenueDAO revenueDAO;
    public static RoomDAO roomDAO;
    public static RoomTypesDAO roomTypesDAO;
    public static ServicesDAO servicesDAO;
    public static StaffDAO staffDAO;
    public static GenericDAO genericDAO;

    public Client() throws RemoteException {
        super();
    }

    @Override
    public void onDataChange(String message) throws RemoteException {
        // Hiển thị thông báo hoặc cập nhật giao diện
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, "Dữ liệu đã thay đổi: " + message,
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            // TODO: Cập nhật giao diện (ví dụ: làm mới bảng dữ liệu)
        });
    }

    public static void main(String[] args) {
        // Show splash screen while initializing connection
        SplashLoading splash = new SplashLoading();
        splash.setVisible(true);

        // Initialize connection in background thread
        new Thread(() -> {
            try {
                Client client = new Client();
                initializeConnection(splash, client);
                Thread.sleep(1000);
                splash.dispose();

                // After successful connection, show login screen
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(GUI_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

                EventQueue.invokeLater(() -> new GUI_Login().setVisible(true));

            } catch (Exception e) {
                splash.dispose();
                JOptionPane.showMessageDialog(null,
                        "Không thể kết nối đến server!\n" + e.getMessage(),
                        "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }).start();
    }

    private static void initializeConnection(SplashLoading splash, Client client) throws Exception {
        splash.processBarUpdate(5, "Đang khởi tạo ứng dụng...");
        Thread.sleep(STEP_DELAY);

        splash.processBarUpdate(10, "Đang kết nối đến server...");
        Thread.sleep(STEP_DELAY);

        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);
            Context context = new InitialContext();

            splash.processBarUpdate(20, "Đang tải dịch vụ tài khoản...");
            accountDAO = (AccountDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/AccountDAO");
            Thread.sleep(STEP_DELAY);

            splash.processBarUpdate(30, "Đang tải dịch vụ khách hàng...");
            customerDAO = (CustomerDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/CustomerDAO");
            Thread.sleep(STEP_DELAY);

            splash.processBarUpdate(40, "Đang tải dịch vụ đặt phòng...");
            reservationDAO = (ReservationDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/ReservationDAO");
            Thread.sleep(STEP_DELAY);

            splash.processBarUpdate(50, "Đang tải dịch vụ phòng...");
            roomDAO = (RoomDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/RoomDAO");
            Thread.sleep(STEP_DELAY);

            splash.processBarUpdate(60, "Đang tải dịch vụ loại phòng...");
            roomTypesDAO = (RoomTypesDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/RoomTypesDAO");
            Thread.sleep(STEP_DELAY);

            splash.processBarUpdate(70, "Đang tải dịch vụ đặt dịch vụ...");
            orderDAO = (OrderDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/OrderDAO");
            Thread.sleep(STEP_DELAY);

            splash.processBarUpdate(80, "Đang tải dịch vụ nhân viên...");
            staffDAO = (StaffDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/StaffDAO");
            Thread.sleep(STEP_DELAY);

            splash.processBarUpdate(90, "Đang tải dịch vụ chung...");
            genericDAO = (GenericDAO) context.lookup("rmi://" + HOST + ":" + PORT + "/GenericDAO");
            Thread.sleep(STEP_DELAY);

            // Đăng ký client để nhận thông báo
            splash.processBarUpdate(95, "Đang đăng ký nhận thông báo...");
            genericDAO.registerClient(client);
            Thread.sleep(STEP_DELAY * 2);

            splash.processBarUpdate(100, "Kết nối thành công!");
            Thread.sleep(1000);

        } catch (Exception e) {
            splash.processBarUpdate(0, "Lỗi kết nối: " + e.getMessage());
            Thread.sleep(2000);
            throw e;
        }
    }
}