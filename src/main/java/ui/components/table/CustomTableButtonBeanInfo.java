package ui.components.table;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * BeanInfo class cho CustomTableButton để hỗ trợ kéo thả trong NetBeans
 */
public class CustomTableButtonBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor descriptor = new BeanDescriptor(CustomTableButton.class);
        descriptor.setDisplayName("Custom Table Button");
        descriptor.setShortDescription("Bảng tùy chỉnh với các nút trong hàng");
        descriptor.setValue("isContainer", Boolean.FALSE);
        descriptor.setValue("containerDelegate", "getScrollPane");
        return descriptor;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor headerBackgroundColor = new PropertyDescriptor("headerBackgroundColor", CustomTableButton.class);
            headerBackgroundColor.setDisplayName("Header Background Color");
            headerBackgroundColor.setShortDescription("Màu nền của phần tiêu đề bảng");

            PropertyDescriptor headerForegroundColor = new PropertyDescriptor("headerForegroundColor", CustomTableButton.class);
            headerForegroundColor.setDisplayName("Header Foreground Color");
            headerForegroundColor.setShortDescription("Màu chữ của phần tiêu đề bảng");

            PropertyDescriptor headerHeight = new PropertyDescriptor("headerHeight", CustomTableButton.class);
            headerHeight.setDisplayName("Header Height");
            headerHeight.setShortDescription("Chiều cao của phần tiêu đề bảng");

            PropertyDescriptor rowHeight = new PropertyDescriptor("rowHeight", CustomTableButton.class);
            rowHeight.setDisplayName("Row Height");
            rowHeight.setShortDescription("Chiều cao của mỗi hàng trong bảng");

            PropertyDescriptor columnNames = new PropertyDescriptor("columnNames", CustomTableButton.class);
            columnNames.setDisplayName("Column Names");
            columnNames.setShortDescription("Tên các cột trong bảng");

            return new PropertyDescriptor[] {
                    headerBackgroundColor,
                    headerForegroundColor,
                    headerHeight,
                    rowHeight,
                    columnNames
            };
        } catch (IntrospectionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getDefaultPropertyIndex() {
        return 0; // headerBackgroundColor là thuộc tính mặc định
    }

    @Override
    public Image getIcon(int iconKind) {
        // Bạn có thể thêm icon cho bean tại đây nếu cần
        return null;
    }
}