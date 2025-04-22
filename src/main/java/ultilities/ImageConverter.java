package ultilities;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

public class ImageConverter {

    /**
     * Chuyển đổi BufferedImage thành chuỗi Base64
     * @param image Ảnh cần chuyển đổi
     * @param format Định dạng ảnh (jpg, png,...)
     * @return Chuỗi Base64 biểu diễn ảnh
     */
    public static String imageToBase64String(BufferedImage image, String format) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển đổi file ảnh thành chuỗi Base64
     * @param imagePath Đường dẫn file ảnh
     * @return Chuỗi Base64 biểu diễn ảnh
     */
    public static String imageFileToBase64String(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            String format = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            return imageToBase64String(image, format);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Chuyển đổi chuỗi Base64 trở lại thành BufferedImage
     * @param base64String Chuỗi Base64 biểu diễn ảnh
     * @return BufferedImage
     */
    public static BufferedImage base64StringToImage(String base64String) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(bais);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertImageToBase64(ImageIcon icon) {
        try {
            BufferedImage image = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();

            // Giảm kích thước ảnh trước khi chuyển đổi
            image = Thumbnails.of(image)
                    .size(200, 200) // Giảm kích thước ảnh
                    .outputQuality(0.7) // Giảm chất lượng ảnh
                    .asBufferedImage();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos); // Dùng JPG thay vì PNG để giảm kích thước
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
