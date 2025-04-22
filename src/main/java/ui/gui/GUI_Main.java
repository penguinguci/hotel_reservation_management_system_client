/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui.gui;

import entities.Role;
import lombok.SneakyThrows;
import ui.components.menu.MenuEvent;
import ui.forms.*;
import ui.forms.Form_HomePage;
import ui.forms.Form_StaffManagement;
import utils.CurrentAccount;

import javax.swing.*;
import java.rmi.RemoteException;


public class GUI_Main extends JFrame {
    private JPanel body;
    private ui.components.header.Header header;
    private ui.components.menu.Menu menu3;
    private JPanel pnlConstrain;
    private ui.components.scroll.win11.ScrollPaneWin11 scrollPaneWin111;
    private Form_CustomerManagement formCustomerManagement;
    @SneakyThrows
    public GUI_Main() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        Form_HomePage homePageForm = new Form_HomePage();
        Form_StaffManagement staffManagementForm = new Form_StaffManagement();
        Form_RoomManagement roomManagement = new Form_RoomManagement();
        Form_Statistics statisticsForm = new Form_Statistics();
        showForm(homePageForm);
        formCustomerManagement = new Form_CustomerManagement();
        menu3.setEvent(new MenuEvent() {
            @Override
            public void selected(int index, int subIndex) throws RemoteException {
                if(CurrentAccount.getCurrentAccount().getRole() ==  Role.MANAGER){
                    if(index == -1){
                        logout();
                    } else if (index == 1) {
                        showForm(new Form_Booking());
                    }
                    else if(index == 2){
                        showForm(roomManagement);
                        roomManagement.loadRoomData();
                    }else if(index == 3){
                        showForm(formCustomerManagement);
//                    formCustomerManagement.loadCustomerData();
//                    formCustomerManagement.clearSearchFields();
                    }
                    else if (index == 4) {
                        showForm(staffManagementForm);
                    }
                    else if (index == 5) {
                        showForm(statisticsForm);
                    }
                    else{
                        showForm(new Form_HomePage());
                    }
                }else {
                    if(index == -1){
                        logout();
                    } else if (index == 1) {
                        showForm(new Form_Booking());
                    }
                    else if(index == 2){
                        showForm(roomManagement);
                        roomManagement.loadRoomData();
                    }else if(index == 3){
                        showForm(formCustomerManagement);
//                    formCustomerManagement.loadCustomerData();
//                    formCustomerManagement.clearSearchFields();
                    }
                    else if (index == 4) {
                        showForm(homePageForm);
                    }
                    else if (index == 5) {
                        showForm(homePageForm);
                    }
                    else{
                        showForm(new Form_HomePage());
                    }
                }
            }
        });
    }

    private void showForm(JPanel com) {
        body.removeAll();
        body.add(com);
        body.repaint();
        body.revalidate();
    }

    @SuppressWarnings("unchecked")

    private void initComponents() {
        pnlConstrain = new JPanel();
        scrollPaneWin111 = new ui.components.scroll.win11.ScrollPaneWin11();
        menu3 = new ui.components.menu.Menu();
        header = new ui.components.header.Header();
        body = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pnlConstrain.setBorder(BorderFactory.createLineBorder(new java.awt.Color(164, 164, 164)));
        pnlConstrain.setPreferredSize(new java.awt.Dimension(1920, 720));
    
        scrollPaneWin111.setBorder(null);
        scrollPaneWin111.setViewportView(menu3);

        body.setBackground(new java.awt.Color(255, 255, 255));
        body.setLayout(new java.awt.BorderLayout());

        GroupLayout pnlConstrainLayout = new GroupLayout(pnlConstrain);
        pnlConstrain.setLayout(pnlConstrainLayout);
        pnlConstrainLayout.setHorizontalGroup(
                pnlConstrainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(header, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1918, Short.MAX_VALUE)
                        .addGroup(pnlConstrainLayout.createSequentialGroup()
                                .addComponent(scrollPaneWin111, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(body, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pnlConstrainLayout.setVerticalGroup(
                pnlConstrainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, pnlConstrainLayout.createSequentialGroup()
                                .addComponent(header, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlConstrainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPaneWin111, GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                                        .addGroup(pnlConstrainLayout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(body, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlConstrain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlConstrain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Main().setVisible(true);
            }
        });
    }

    private void logout() {
        int response = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {

            new GUI_Login().setVisible(true);
            this.dispose();
        }
    }
}
