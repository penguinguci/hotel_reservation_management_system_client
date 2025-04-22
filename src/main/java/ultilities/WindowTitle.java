package ultilities;

import javax.swing.*;
import java.awt.*;

public class WindowTitle {
    private static JFrame jf;
    public static final String VERSION = "25.04.22";
    public static WindowTitle instance = new WindowTitle();

    public static String getTitleContent(String x) {
        return x + " - MELODY HOTEL | " + VERSION;
    }

    public static void setTitle(String x) {
        jf.setTitle(getTitleContent(x));
    }

    public WindowTitle(JFrame jf) {
        super();
        this.jf = jf;
    }

    public WindowTitle() {
        super();
    }

    public JFrame getJf() {
        return jf;
    }

    public void setJf(JFrame jf) {
        this.jf = jf;
    }

    public static WindowTitle getInstance(){
        return instance;
    }

    @Override
    public String toString() {
        return "WindowTitle [jf=" + jf + "]";
    }
}
