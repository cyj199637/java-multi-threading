package performanceOptimization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static performanceOptimization.ImageProcessor.recolorImage;

public class SingleThreadedImageProcessing {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {

        BufferedImage original = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();

        recolorSingleThreaded(original, result);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        File output = new File(DESTINATION_FILE);
        ImageIO.write(result, "jpg", output);

        System.out.println(duration);
    }

    public static void recolorSingleThreaded(BufferedImage original, BufferedImage result) {
        recolorImage(original, result, 0, 0, original.getWidth(), original.getHeight());
    }
}
