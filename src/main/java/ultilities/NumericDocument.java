package ultilities;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumericDocument extends PlainDocument {
    private final int maxLength;

    public NumericDocument(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        if (str == null) return;

        String currentText = getText(0, getLength());
        String newText = currentText.substring(0, offs) + str + currentText.substring(offs);

        // Chỉ cho phép số và không vượt quá maxLength
        if (newText.matches("\\d*") && newText.length() <= maxLength) {
            super.insertString(offs, str, a);
        }
    }
}
