/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui.components.header;

import dao.AccountDAOImpl;
import interfaces.AccountDAO;
import ui.gui.GUI_Login;
import utils.CurrentAccount;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Header extends javax.swing.JPanel {

    public Header() {
        initComponents();
        initInfoStaff();
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    void initInfoStaff(){
        String infoStaff = CurrentAccount.getCurrentAccount().getStaff().getFirstName()+" "+CurrentAccount.getCurrentAccount().getStaff().getLastName();
        btnUser.setText("Chào, " + infoStaff);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(0, 0, new Color(113, 105, 246), 0, getHeight(), new Color(149, 145, 239)));
        g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
        super.paintComponent(g);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImageLogo = new javax.swing.JLabel();
        lblTitleLogo = new javax.swing.JLabel();
        btnUser = new ui.components.button.ButtonCustom();

        setName("lbImageLogo"); // NOI18N

        lblImageLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hotel.png"))); // NOI18N

        lblTitleLogo.setFont(new Font("Segoe UI", 2, 22)); // NOI18N
        lblTitleLogo.setForeground(new Color(236, 236, 236));
        lblTitleLogo.setText("Hotel Melody");

        btnUser.setText("Nhân viên: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblImageLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitleLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 199, Short.MAX_VALUE)
                .addComponent(btnUser, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTitleLogo)
                        .addComponent(btnUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblImageLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.components.button.ButtonCustom btnUser;
    private javax.swing.JLabel lblImageLogo;
    private javax.swing.JLabel lblTitleLogo;
    // End of variables declaration//GEN-END:variables
}
