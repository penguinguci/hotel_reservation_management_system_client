package ui.components.menu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.miginfocom.swing.MigLayout;

public class MenuAnimation {
    private static final int DURATION = 200;
    private static final int STEPS = 12;

    public static void showMenu(JPanel panel, Component menuItem, MigLayout layout) {
        int height = panel.getPreferredSize().height;
        panel.setVisible(true);

        Timer timer = new Timer(DURATION / STEPS, new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep < STEPS) {
                    float progress = (float) currentStep / STEPS;
                    // Sử dụng hàm easing để có hiệu ứng mượt hơn
                    float easedProgress = easeOutQuad(progress);
                    int animatedHeight = (int) (height * easedProgress);
                    layout.setComponentConstraints(panel, "h " + animatedHeight + "!");
                    panel.revalidate();
                    panel.repaint();
                    currentStep++;
                } else {
                    ((Timer) e.getSource()).stop();
                    layout.setComponentConstraints(panel, "h " + height + "!");
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    public static void hideMenu(JPanel panel, Component menuItem, MigLayout layout, Runnable onFinished) {
        int height = panel.getPreferredSize().height;

        Timer timer = new Timer(DURATION / STEPS, new ActionListener() {
            private int currentStep = STEPS;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep > 0) {
                    float progress = (float) currentStep / STEPS;
                    // Sử dụng hàm easing để có hiệu ứng mượt hơn
                    float easedProgress = easeInQuad(progress);
                    int animatedHeight = (int) (height * easedProgress);
                    layout.setComponentConstraints(panel, "h " + animatedHeight + "!");
                    panel.revalidate();
                    panel.repaint();
                    currentStep--;
                } else {
                    ((Timer) e.getSource()).stop();
                    panel.setVisible(false);
                    if (onFinished != null) {
                        onFinished.run();
                    }
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    // Hàm easing cho chuyển động mượt hơn
    private static float easeOutQuad(float t) {
        return t * (2 - t);
    }

    private static float easeInQuad(float t) {
        return t * t;
    }
}