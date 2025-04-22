package ui.forms;

import dao.*;
import entities.*;
import interfaces.*;
import jakarta.persistence.EntityManagerFactory;
import lombok.SneakyThrows;
import ui.components.button.ButtonCustom;
import ui.components.combobox.StyledComboBox;
import ui.components.table.CustomTable;
import ui.components.textfield.CustomRoundedTextField;
import ui.tabs.Tab_ServicesManagement;
import ui.tabs.Tab_RoomTypeManagement;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

import static com.twelvemonkeys.lang.StringUtil.valueOf;

/**
 * @author Lenovo
 */
public class Form_RoomManagement extends JPanel {

    private RoomDAO roomDAO = new RoomDAOImpl();
    private RoomTypesDAO roomTypeDAO = new RoomTypeDAOImpl();
    EntityManagerFactory entityManagerFactory = null;
    private Room selectedRoom;
    Tab_RoomTypeManagement tabRTM = new Tab_RoomTypeManagement();
    Tab_ServicesManagement tabSM = new Tab_ServicesManagement();

    public Form_RoomManagement() throws RemoteException {
        initComponents();
        initializeTableModels();
        loadRoomTypes();
        loadRoomData();
        loadAmenityData();
        setupListeners();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        pnl_North = new JPanel();
        lbl_Title = new JLabel();
        pnl_Center = new JPanel();
        pnl_Left = new JPanel();
        pInfo = new JPanel();
        jLabel5 = new JLabel();
        txtRoomName = new CustomRoundedTextField();
        jLabel6 = new JLabel();
        txtPrice = new CustomRoundedTextField();
        jLabel12 = new JLabel();
        txtHourlyPrice = new CustomRoundedTextField();
        jLabel7 = new JLabel();
        txtPosition = new CustomRoundedTextField();
        jLabel8 = new JLabel();
        cb_RoomTypeSearch = new StyledComboBox();
        btnAdd = new ButtonCustom();
        btnUpdate = new ButtonCustom();
        jLabel9 = new JLabel();
        txtNumOfPeople = new CustomRoundedTextField();
        btnReset = new ButtonCustom();
//        jLabel11 = new javax.swing.JLabel();
//        txtPrice1 = new ui.components.textfield.CustomRoundedTextField();
        pFind = new JPanel();
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        cb_Price = new StyledComboBox();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        cb_Position = new StyledComboBox();
        jPanel3 = new JPanel();
        jLabel3 = new JLabel();
        cb_RoomType = new StyledComboBox();
        jPanel4 = new JPanel();
        jLabel4 = new JLabel();
        cb_Status = new StyledComboBox();
        pAmentities = new JPanel();
        jScrollPane4 = new JScrollPane();
        customTable1 = new CustomTable();
        jPanel5 = new JPanel();
        jScrollPane3 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jLabel10 = new JLabel();
        btnAddAmentity = new ButtonCustom();
        btnUpdateAmentity = new ButtonCustom();
        btnResetAmentity = new ButtonCustom();
        jPanel6 = new JPanel();
        btnManageRoomType = new ButtonCustom();
        btnManageService = new ButtonCustom();
        jScrollPane2 = new JScrollPane();
        customTable = new CustomTable();

        setBackground(new Color(255, 255, 255));
        setLayout(new BorderLayout());

        pnl_North.setBackground(new Color(255, 255, 255));
        pnl_North.setLayout(new BorderLayout());

        lbl_Title.setBackground(new Color(255, 255, 255));
        lbl_Title.setFont(new Font("Segoe UI", 1, 16));
        lbl_Title.setForeground(new Color(127, 122, 239));
        lbl_Title.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_Title.setText(" QUẢN LÝ PHÒNG");
        pnl_North.add(lbl_Title, BorderLayout.CENTER);

        add(pnl_North, BorderLayout.PAGE_START);

        pnl_Center.setLayout(new BorderLayout());

        pnl_Left.setBackground(new Color(255, 255, 255));
        pnl_Left.setLayout(null);

        pInfo.setBackground(new Color(255, 255, 255));
        pInfo.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));

        jLabel5.setText("Phòng");

        txtRoomName.setEditable(false);
        txtRoomName.setDisabledTextColor(new Color(255, 255, 255));
        txtRoomName.setEnabled(false);
        txtRoomName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtRoomNameActionPerformed(evt);
            }
        });

        jLabel6.setText("Giá theo đêm:");

        jLabel12.setText("Giá theo giờ:");

        jLabel7.setText("Tầng");

        jLabel8.setText("Loại phòng:");

        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel9.setText("Số người:");

        btnReset.setText("Làm mới");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });


        GroupLayout pInfoLayout = new GroupLayout(pInfo);
        pInfo.setLayout(pInfoLayout);
        pInfoLayout.setHorizontalGroup(
                pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pInfoLayout.createSequentialGroup()
                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, pInfoLayout.createSequentialGroup()
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, pInfoLayout.createSequentialGroup()
                                                                .addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(txtNumOfPeople, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, pInfoLayout.createSequentialGroup()
                                                                .addComponent(cb_RoomTypeSearch, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(21, 21, 21))))
                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                .addComponent(jLabel8)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                        .addComponent(jLabel12, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                                                                                        )
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                                .addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                .addGap(25, 25, 25))
                                                                        .addGroup(pInfoLayout.createSequentialGroup()
                                                                                .addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtRoomName, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtPrice, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtHourlyPrice, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtPosition, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
                                                                        )))))
                                .addContainerGap())
        );
        pInfoLayout.setVerticalGroup(
                pInfoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pInfoLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtRoomName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtPosition, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(txtPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(txtHourlyPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)

                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(txtNumOfPeople, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(cb_RoomTypeSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pInfoLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnReset, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                .addGap(204, 204, 204))
        );

        pnl_Left.add(pInfo);
        pInfo.setBounds(870, 20, 400, 420);

        pFind.setBackground(new Color(255, 255, 255));
        pFind.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        jPanel1.setBackground(new Color(255, 255, 255));

        jLabel1.setText("Giá:");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_Price, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_Price, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel2.setBackground(new Color(255, 255, 255));

        jLabel2.setText("Tầng:");

        GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_Position, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_Position, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel3.setBackground(new Color(255, 255, 255));

        jLabel3.setBackground(new Color(255, 255, 255));
        jLabel3.setText("Loại phòng:");

        cb_RoomType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cb_RoomTypeActionPerformed(evt);
            }
        });

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_RoomType, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_RoomType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        jPanel4.setBackground(new Color(255, 255, 255));

        jLabel4.setText("Trạng thái:");

        cb_Status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cb_StatusActionPerformed(evt);
            }
        });

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(cb_Status, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                                        .addComponent(cb_Status, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        GroupLayout pFindLayout = new GroupLayout(pFind);
        pFind.setLayout(pFindLayout);
        pFindLayout.setHorizontalGroup(
                pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pFindLayout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32))
        );
        pFindLayout.setVerticalGroup(
                pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, pFindLayout.createSequentialGroup()
                                .addContainerGap(21, Short.MAX_VALUE)
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(pFindLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        pnl_Left.add(pFind);
        pFind.setBounds(10, 20, 840, 160);
        pFind.getAccessibleContext().setAccessibleDescription("");

        pAmentities.setBackground(new Color(255, 255, 255));
        pAmentities.setBorder(BorderFactory.createTitledBorder("Tiện nghi"));

        customTable1.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                },
                new String [] {
                        "STT", "Tiện nghi"
                }
        ));
        jScrollPane4.setViewportView(customTable1);

        jPanel5.setBackground(new Color(255, 255, 255));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jLabel10.setText("Nhập tiện nghi:");

        GroupLayout jPanel5Layout = new GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel10)
                                        .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap(14, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );


        btnAddAmentity.setText("Thêm");
        btnAddAmentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddAmentityActionPerformed(evt);
            }
        });


        btnUpdateAmentity.setText("Cập nhật");
        btnUpdateAmentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnUpdateAmentityActionPerformed(evt);
            }
        });

        btnResetAmentity.setText("Làm mới");
        btnResetAmentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnResetAmentityActionPerformed(evt);
            }
        });

        GroupLayout pAmentitiesLayout = new GroupLayout(pAmentities);
        pAmentities.setLayout(pAmentitiesLayout);
        pAmentitiesLayout.setHorizontalGroup(
                pAmentitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pAmentitiesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jPanel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pAmentitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAddAmentity, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUpdateAmentity, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnResetAmentity, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        pAmentitiesLayout.setVerticalGroup(
                pAmentitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pAmentitiesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pAmentitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pAmentitiesLayout.createSequentialGroup()
                                                .addComponent(btnAddAmentity, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnUpdateAmentity, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnResetAmentity, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pAmentitiesLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(jScrollPane4, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jPanel5, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        pnl_Left.add(pAmentities);
        pAmentities.setBounds(0, 450, 850, 240);

        jPanel6.setBackground(new Color(255, 255, 255));
        jPanel6.setBorder(BorderFactory.createTitledBorder("Quản lí loại phòng - dịch vụ"));


        btnManageRoomType.setText("Quản lý loại phòng");
        btnManageRoomType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnManageRoomTypeActionPerformed(evt);
            }
        });


        btnManageService.setText("Cập nhật");
        btnManageService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnManageServiceActionPerformed(evt);
            }
        });

        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addGroup(jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnManageRoomType, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                                        .addComponent(btnManageService, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addContainerGap(39, Short.MAX_VALUE)
                                .addComponent(btnManageRoomType, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(btnManageService, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );

        pnl_Left.add(jPanel6);
        jPanel6.setBounds(870, 450, 400, 230);

        customTable.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null},
                        {null, null, null, null, null, null, null, null}
                },
                new String [] {
                        "Mã phòng", "Loại phòng", "Trạng thái", "Giá/Đêm", "Giá/Giờ", "Số người", "Giờ tối thiểu", "Giờ tối đa"
                }
        ));
        jScrollPane2.setViewportView(customTable);

        pnl_Left.add(jScrollPane2);
        jScrollPane2.setBounds(10, 180, 840, 260);

        pnl_Center.add(pnl_Left, BorderLayout.CENTER);

        add(pnl_Center, BorderLayout.CENTER);
    }

    private void cb_StatusActionPerformed(ActionEvent evt) {
    }

    private void cb_RoomTypeActionPerformed(ActionEvent evt) {
    }

    @SneakyThrows
    private void btnAddActionPerformed(ActionEvent evt) {
        String newRoomId = generateRoomId();
        if (newRoomId == null) {
            JOptionPane.showMessageDialog(this, "Không thể sinh mã phòng mới! Đã đạt giới hạn mã phòng (R999).");
            return;
        }
        txtRoomName.setText(newRoomId);

        if (!validData(false)) {
            return;
        }

        Room room = new Room();
        room.setRoomId(txtRoomName.getText());
        room.setPrice(Double.parseDouble(txtPrice.getText()));

        // Xử lý hourlyBaseRate
        String hourlyPriceText = txtHourlyPrice.getText().trim();
        room.setHourlyBaseRate(hourlyPriceText.isEmpty() ? 0.0 : Double.parseDouble(hourlyPriceText));

        // Mặc định minHours và maxHours
        room.setMinHours(1);
        room.setMaxHours(12);

        // Xử lý capacity
        String capacityText = txtNumOfPeople.getText().trim();
        room.setCapacity(capacityText.isEmpty() ? 0 : Integer.parseInt(capacityText));

        // Xử lý floor
        String floorText = txtPosition.getText().trim();
        room.setFloor(floorText.isEmpty() ? 0 : Integer.parseInt(floorText));

        room.setStatus(Room.STATUS_AVAILABLE);
        room.setRoomSize(0.0);
        room.setRoomImage("");
        room.setAmenities(new ArrayList<>());

        String roomTypeName = (String) cb_RoomTypeSearch.getSelectedItem();
        RoomType roomType = roomDAO.getAllRoomTypes().stream()
                .filter(rt -> rt.getRoomType().getTypeName().equals(roomTypeName))
                .findFirst()
                .orElse(null).getRoomType();
        if (roomType != null) {
            room.setRoomType(roomType);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy loại phòng!");
            return;
        }

        try {
            roomDAO.create(room);
            loadRoomData();
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phòng: " + e.getMessage());
        }
    }

    @SneakyThrows
    private void btnUpdateActionPerformed(ActionEvent evt) {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng để cập nhật!");
            return;
        }

        if (!validData(true)) {
            return;
        }

        selectedRoom.setRoomId(txtRoomName.getText());
        selectedRoom.setPrice(Double.parseDouble(txtPrice.getText()));

        // Cập nhật hourlyBaseRate
        String hourlyPriceText = txtHourlyPrice.getText().trim();
        selectedRoom.setHourlyBaseRate(hourlyPriceText.isEmpty() ? 0.0 : Double.parseDouble(hourlyPriceText));

        // Xử lý capacity
        String capacityText = txtNumOfPeople.getText().trim();
        selectedRoom.setCapacity(capacityText.isEmpty() ? 0 : Integer.parseInt(capacityText));

        // Xử lý floor
        String floorText = txtPosition.getText().trim();
        selectedRoom.setFloor(floorText.isEmpty() ? 0 : Integer.parseInt(floorText));

        String roomTypeName = (String) cb_RoomTypeSearch.getSelectedItem();
        RoomType roomType = roomDAO.getAllRoomTypes().stream()
                .filter(rt -> rt.getRoomType().getTypeName().equals(roomTypeName))
                .findFirst()
                .orElse(null).getRoomType();
        if (roomType != null) {
            selectedRoom.setRoomType(roomType);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy loại phòng!");
            return;
        }

        try {
            roomDAO.update(selectedRoom);
            loadRoomData();
            JOptionPane.showMessageDialog(this, "Cập nhật phòng thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phòng: " + e.getMessage());
        }
    }

    private void btnResetActionPerformed(ActionEvent evt) {
        String newRoomId = generateRoomId();
        if (newRoomId == null) {
            JOptionPane.showMessageDialog(this, "Không thể sinh mã phòng mới! Đã đạt giới hạn mã phòng (R999).");
            txtRoomName.setText("");
        } else {
            txtRoomName.setText(newRoomId);
        }
        cb_RoomTypeSearch.setSelectedIndex(0);
        txtPrice.setText("");
        txtHourlyPrice.setText("");
        txtPosition.setText("");
        txtNumOfPeople.setText("");
        selectedRoom = null;
        cb_Status.setSelectedItem(0);
        cb_Position.setSelectedItem(0);
        cb_Price.setSelectedItem(0);
        cb_RoomType.setSelectedIndex(0);
        loadRoomData();
        jTextArea1.setText("");
    }

    private void btnManageServiceActionPerformed(ActionEvent evt) {
        tabSM.setLocationRelativeTo(null);
        tabSM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tabSM.setVisible(true);
    }

    private void btnManageRoomTypeActionPerformed(ActionEvent evt) {
        tabRTM.setLocationRelativeTo(null);
        tabRTM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tabRTM.setVisible(true);
    }

    private void txtRoomNameActionPerformed(ActionEvent evt) {
    }

    private void btnAddAmentityActionPerformed(ActionEvent evt) {
        if (selectedRoom != null) {
            String amenity = jTextArea1.getText().trim();
            if (!amenity.isEmpty()) {
                try {
                    List<String> updatedAmenities = roomDAO.addAmenity(selectedRoom.getRoomId(), amenity.trim());
                    if (updatedAmenities == null) {
                        throw new IllegalStateException("Không thể thêm tiện nghi!");
                    }
                    selectedRoom.setAmenities(updatedAmenities);
                    updateAmenitiesTable(updatedAmenities);
                    customTable1.repaint();
                    customTable1.revalidate();
                    JOptionPane.showMessageDialog(this, "Thêm tiện nghi thành công!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi thêm tiện nghi: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tiện nghi!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng!");
        }
    }

    private void btnUpdateAmentityActionPerformed(ActionEvent evt) {
        if (selectedRoom == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng trước khi cập nhật tiện nghi!");
            return;
        }

        int selectedRow = customTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tiện nghi để cập nhật!");
            return;
        }

        String currentAmenity = (String) customTable1.getValueAt(selectedRow, 1);
        String updatedAmenity = jTextArea1.getText().trim();
        if (updatedAmenity == null || updatedAmenity.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tiện nghi không được để trống!");
            return;
        }

        try {
            List<String> updatedAmenities = roomDAO.updateAmenity(selectedRoom.getRoomId(), selectedRow, updatedAmenity.trim());
            if (updatedAmenities == null) {
                throw new IllegalStateException("Không thể cập nhật tiện nghi!");
            }
            selectedRoom.setAmenities(updatedAmenities);
            updateAmenitiesTable(updatedAmenities);
            customTable1.repaint();
            customTable1.revalidate();
            jTextArea1.setText(updatedAmenity);
            JOptionPane.showMessageDialog(this, "Cập nhật tiện nghi thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật tiện nghi: " + e.getMessage());
        }
    }

    private void btnResetAmentityActionPerformed(ActionEvent evt) {
        jTextArea1.setText("");
        updateAmenitiesTable(selectedRoom != null ? selectedRoom.getAmenities() : null);
        loadRoomData();
        loadAmenityData();
    }

    private void initializeTableModels() {
        ((DefaultTableModel) customTable.getModel()).setRowCount(0);
        ((DefaultTableModel) customTable1.getModel()).setRowCount(0);
    }

    @SneakyThrows
    private void loadRoomTypes() {
        List<RoomType> roomTypes = roomTypeDAO.getAllRoomTypes();
        cb_RoomType.removeAllItems();
        cb_RoomTypeSearch.removeAllItems();
        cb_RoomType.addItem("Tất cả");
        cb_RoomTypeSearch.addItem("Tất cả");
        for (RoomType rt : roomTypes) {
            cb_RoomType.addItem(rt.getTypeName());
            cb_RoomTypeSearch.addItem(rt.getTypeName());
        }
    }

    private void updateRoomTable(List<Room> rooms) {
        DefaultTableModel model = (DefaultTableModel) customTable.getModel();
        model.setRowCount(0);
        for (Room room : rooms) {
            String statusText;
            switch (room.getStatus()) {
                case Room.STATUS_AVAILABLE:
                    statusText = "Có sẵn";
                    break;
                case Room.STATUS_RESERVED:
                    statusText = "Đã đặt trước";
                    break;
                case Room.STATUS_OCCUPIED:
                    statusText = "Đang sử dụng";
                    break;
                case Room.STATUS_MAINTENANCE:
                    statusText = "Bảo trì";
                    break;
                default:
                    statusText = "Không xác định";
            }
            model.addRow(new Object[]{
                    room.getRoomId(),
                    room.getRoomType() != null ? room.getRoomType().getTypeName() : "N/A",
                    statusText,
                    room.getPrice(),
                    room.getHourlyBaseRate(),
                    room.getCapacity(),
                    room.getMinHours(),
                    room.getMaxHours()
            });
        }
    }

    @SneakyThrows
    public void loadRoomData() {
        List<Room> rooms = roomDAO.getAllRooms();
        updateRoomTable(rooms);
        cb_Status.removeAllItems();
        cb_Status.addItem("Tất cả");
        cb_Status.addItem("Có sẵn");
        cb_Status.addItem("Đang sử dụng");
        cb_Status.addItem("Bảo trì");
        cb_Status.addItem("Đã đặt trước");

        cb_Price.removeAllItems();
        cb_Price.addItem("Tất cả");
        cb_Price.addItem("Dưới 500,000");
        cb_Price.addItem("500,000 - 1,000,000");
        cb_Price.addItem("Trên 1,000,000");

        cb_Position.removeAllItems();
        cb_Position.addItem("Tất cả");
        List<Integer> floors = roomDAO.getAllFloors();
        for (Integer floor : floors) {
            cb_Position.addItem("Tầng " + floor);
        }
    }

    private void setupListeners() {
        customTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @SneakyThrows
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = customTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String roomId = (String) customTable.getValueAt(selectedRow, 0);
                    selectedRoom = roomDAO.findById(roomId);
                    if (selectedRoom != null) {
                        txtRoomName.setText(selectedRoom.getRoomId());
                        txtPosition.setText(valueOf(selectedRoom.getFloor()));
                        txtPrice.setText(String.valueOf(selectedRoom.getPrice()));
                        txtHourlyPrice.setText(String.valueOf(selectedRoom.getHourlyBaseRate()));
                        txtNumOfPeople.setText(valueOf(selectedRoom.getCapacity()));
                        cb_RoomTypeSearch.setSelectedItem(selectedRoom.getRoomType() != null ? selectedRoom.getRoomType().getTypeName() : "N/A");
                        updateAmenitiesTable(selectedRoom.getAmenities());
                        jTextArea1.setText("");
                    } else {
                        updateAmenitiesTable(null);
                        jTextArea1.setText("");
                    }
                } else {
                    updateAmenitiesTable(null);
                    jTextArea1.setText("");
                }
            }
        });
        customTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = customTable1.getSelectedRow();
                    if (selectedRow >= 0) {
                        String selectedAmenity = (String) customTable1.getValueAt(selectedRow, 1);
                        jTextArea1.setText(selectedAmenity != null ? selectedAmenity : "");
                    } else {
                        jTextArea1.setText("");
                    }
                }
            }
        });
        cb_Status.addActionListener(e -> searchRooms());
        cb_RoomType.addActionListener(e -> searchRooms());
        cb_Price.addActionListener(e -> searchRooms());
        cb_Position.addActionListener(e -> searchRooms());
    }

    void updateAmenitiesTable(List<String> amenities) {
        DefaultTableModel model = (DefaultTableModel) customTable1.getModel();
        model.setRowCount(0);
        int i = 0;
        if (amenities != null && !amenities.isEmpty()) {
            for (String amenity : amenities) {
                i++;
                model.addRow(new Object[]{i, amenity});
            }
        }
        customTable1.repaint();
        customTable1.revalidate();
    }

    void loadAmenityData() {
        if (selectedRoom != null) {
            loadRoomData();
            setupListeners();
        }
    }

    @SneakyThrows
    private void searchRooms() {
        Map<String, Object> criteria = new HashMap<>();

        String priceFilter = (String) cb_Price.getSelectedItem();
        if (priceFilter != null && !priceFilter.equals("Tất cả")) {
            if (priceFilter.equals("Dưới 500,000")) {
                criteria.put("priceMin", 0.0);
                criteria.put("priceMax", 500000.0);
            } else if (priceFilter.equals("500,000 - 1,000,000")) {
                criteria.put("priceMin", 500000.0);
                criteria.put("priceMax", 1000000.0);
            } else {
                criteria.put("priceMin", 1000000.0);
                criteria.put("priceMax", Double.MAX_VALUE);
            }
        }

        String positionFilter = (String) cb_Position.getSelectedItem();
        if (positionFilter != null && !positionFilter.equals("Tất cả")) {
            criteria.put("position", positionFilter.replace("Tầng ", ""));
        }

        String roomTypeFilter = (String) cb_RoomType.getSelectedItem();
        if (roomTypeFilter != null && !roomTypeFilter.equals("Tất cả")) {
            criteria.put("roomType", roomTypeFilter);
        }

        String statusFilter = (String) cb_Status.getSelectedItem();
        if (statusFilter != null && !statusFilter.equals("Tất cả")) {
            Integer status = null;
            switch (statusFilter) {
                case "Có sẵn":
                    status = Room.STATUS_AVAILABLE;
                    break;
                case "Đang sử dụng":
                    status = Room.STATUS_OCCUPIED;
                    break;
                case "Bảo trì":
                    status = Room.STATUS_MAINTENANCE;
                    break;
                case "Đã đặt trước":
                    status = Room.STATUS_RESERVED;
                    break;
            }
            if (status != null) {
                criteria.put("status", status);
            }
        }

        List<Room> rooms = roomDAO.findByCriteria(criteria);
        updateRoomTable(rooms);
    }

    @SneakyThrows
    private String generateRoomId() {
        List<Room> rooms = roomDAO.getAllRooms();
        int maxNumber = 0;

        for (Room room : rooms) {
            String roomId = room.getRoomId();
            if (roomId != null && roomId.matches("R\\d{3}")) {
                int number = Integer.parseInt(roomId.substring(1));
                maxNumber = Math.max(maxNumber, number);
            }
        }

        maxNumber++;
        if (maxNumber > 999) {
            return null;
        }
        return String.format("R%03d", maxNumber);
    }

    @SneakyThrows
    private boolean validData(boolean isUpdate) {
        String roomId = txtRoomName.getText().trim();
        String priceText = txtPrice.getText().trim();
        String hourlyPriceText = txtHourlyPrice.getText().trim();
        String floorText = txtPosition.getText().trim();
        String capacityText = txtNumOfPeople.getText().trim();
        String roomType = (String) cb_RoomTypeSearch.getSelectedItem();

        if (roomId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được để trống!");
            txtRoomName.requestFocus();
            return false;
        }

        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá phòng không được để trống!");
            txtPrice.requestFocus();
            return false;
        }
        try {
            double price = Double.parseDouble(priceText);
            if (price <= 100000) {
                JOptionPane.showMessageDialog(this, "Giá phòng phải lớn hơn 100,000!");
                txtPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá phòng phải là số hợp lệ!");
            txtPrice.requestFocus();
            return false;
        }

        if (!hourlyPriceText.isEmpty()) {
            try {
                double hourlyPrice = Double.parseDouble(hourlyPriceText);
                if (hourlyPrice < 0) {
                    JOptionPane.showMessageDialog(this, "Giá theo giờ không được âm!");
                    txtHourlyPrice.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Giá theo giờ phải là số hợp lệ!");
                txtHourlyPrice.requestFocus();
                return false;
            }
        }

        if (floorText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tầng không được để trống!");
            txtPosition.requestFocus();
            return false;
        }
        try {
            int floor = Integer.parseInt(floorText);
            if (floor <= 0) {
                JOptionPane.showMessageDialog(this, "Tầng phải là số nguyên dương!");
                txtPosition.requestFocus();
                return false;
            }
            List<Integer> floors = roomDAO.getAllFloors();
            if (!floors.contains(floor) && !isUpdate) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tầng " + floor + " chưa tồn tại. Bạn có muốn thêm tầng mới không?",
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    txtPosition.requestFocus();
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tầng phải là số nguyên hợp lệ!");
            txtPosition.requestFocus();
            return false;
        }

        if (!capacityText.isEmpty()) {
            try {
                int capacity = Integer.parseInt(capacityText);
                if (capacity < 0) {
                    JOptionPane.showMessageDialog(this, "Sức chứa không được âm!");
                    txtNumOfPeople.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên hợp lệ!");
                txtNumOfPeople.requestFocus();
                return false;
            }
        }

        if (roomType == null || roomType.equals("Tất cả")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!");
            cb_RoomTypeSearch.requestFocus();
            return false;
        }

        return true;
    }

    private ButtonCustom btnAdd;
    private ButtonCustom btnAddAmentity;
    private ButtonCustom btnManageRoomType;
    private ButtonCustom btnManageService;
    private ButtonCustom btnReset;
    private ButtonCustom btnResetAmentity;
    private ButtonCustom btnUpdate;
    private ButtonCustom btnUpdateAmentity;
    private StyledComboBox cb_Position;
    private StyledComboBox cb_Price;
    private StyledComboBox cb_RoomType;
    private StyledComboBox cb_RoomTypeSearch;
    private StyledComboBox cb_Status;
    private CustomTable customTable;
    private CustomTable customTable1;
    private JLabel jLabel1;
    private JLabel jLabel10;
   // private javax.swing.JLabel jLabel11;
    private JLabel jLabel12;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JPanel jPanel6;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JTextArea jTextArea1;
    private JLabel lbl_Title;
    private JPanel pAmentities;
    private JPanel pFind;
    private JPanel pInfo;
    private JPanel pnl_Center;
    private JPanel pnl_Left;
    private JPanel pnl_North;
    private CustomRoundedTextField txtNumOfPeople;
    private CustomRoundedTextField txtPosition;
    private CustomRoundedTextField txtPrice;
    //private ui.components.textfield.CustomRoundedTextField txtPrice1;
    private CustomRoundedTextField txtHourlyPrice;
    private CustomRoundedTextField txtRoomName;
}