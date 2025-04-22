package ui.components.label;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TestLabelImage {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LabelImage Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            try {
                BufferedImage image = ImageIO.read(new File("src/main/java/images/logo.png")); // Đường dẫn đến ảnh
                LabelImage labelImage = new LabelImage(image);
                labelImage.setBorderWidth(10); // Tùy chỉnh độ rộng viền

                frame.add(labelImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            frame.setSize(200, 200);
            frame.setVisible(true);
        });
    }
}
