package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ui.components.table.CustomTableButton;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {

    /**
     * Exports table data to Excel file
     * @param table The JTable to export
     * @param title Dialog title
     * @param sheetName Excel sheet name
     * @param parentComponent Parent component for dialogs
     * @return true if export succeeded, false otherwise
     */
    public static boolean exportToExcel(CustomTableButton table, String title, String sheetName, java.awt.Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(title);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));

        int userSelection = fileChooser.showSaveDialog(parentComponent);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();

        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            // Create header
            Row headerRow = sheet.createRow(0);
            CustomTableButton.CustomTableModel model = table.getTableModel();

            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }

            // Add data
            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        row.createCell(j).setCellValue(value.toString());
                    } else {
                        row.createCell(j).setCellValue("");
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write file
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                JOptionPane.showMessageDialog(parentComponent,
                        "Xuất dữ liệu thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent,
                    "Lỗi khi xuất dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Imports data from Excel file using a row processor
     * @param parentComponent Parent component for dialogs
     * @param rowProcessor Function to process each row
     * @return true if import succeeded, false otherwise
     */
    public static boolean importFromExcel(java.awt.Component parentComponent, ExcelRowProcessor rowProcessor) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));

        int returnValue = fileChooser.showOpenDialog(parentComponent);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File selectedFile = fileChooser.getSelectedFile();

        try (Workbook workbook = WorkbookFactory.create(selectedFile)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (!rowProcessor.processRow(row)) {
                    // Stop processing if row processor returns false
                    break;
                }
            }

            JOptionPane.showMessageDialog(parentComponent,
                    "Nhập dữ liệu thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parentComponent,
                    "Lỗi khi nhập dữ liệu: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @FunctionalInterface
    public interface ExcelRowProcessor {
        /**
         * Processes a single row from Excel
         * @param row The row to process
         * @return true to continue processing, false to stop
         */
        boolean processRow(Row row);
    }
}