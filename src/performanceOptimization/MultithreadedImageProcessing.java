package performanceOptimization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static performanceOptimization.ImageProcessor.recolorImage;

public class MultithreadedImageProcessing {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {

        BufferedImage original = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();

        int numberOfThreads = 2;
        recolorMultithreaded(original, result, numberOfThreads);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        File output = new File(DESTINATION_FILE);
        ImageIO.write(result, "jpg", output);

        System.out.println(duration);
    }

    public static void recolorMultithreaded(BufferedImage original, BufferedImage result, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = original.getWidth();
        int height = original.getHeight();

        for (int i = 0;i < numberOfThreads; i++) {
            final int threadMultiplier = i;

            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                recolorImage(original, result, leftCorner, topCorner, width, height);
            });

            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
    }
}
