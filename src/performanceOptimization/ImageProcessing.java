package performanceOptimization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {
    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {

        BufferedImage original = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

        recolorSingleThreaded(original, result);

        File output = new File(DESTINATION_FILE);
        ImageIO.write(result, "jpg", output);
    }

    public static void recolorSingleThreaded(BufferedImage original, BufferedImage result) {
        recolorImage(original, result, 0, 0, original.getWidth(), original.getHeight());
    }

    // recolorImage : 이미지의 전체 픽셀에 대해서 회색 -> 보라색 으로 변경(왼쪽 상단부터 변경)
    public static void recolorImage(BufferedImage original, BufferedImage result, int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < original.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < original.getHeight(); y++) {
                recolorPixel(original, result, x, y);
            }
        }
    }

    // recolorPixel : 기존 픽셀의 색상(회색)을 보라색으로 변경
    // -> 회색이 아닌 경우 흰색 꽃 이미지의 픽셀이 아니므로 그대로 반환
    public static void recolorPixel(BufferedImage original, BufferedImage result, int x, int y) {
        int rgb = original.getRGB(x, y);

        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(result, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    // isShadeOfGray : 픽셀의 특정 값을 취하고, 픽셀에 넣을 회색을 결정
    // -> 모든 컴포넌트가 같은 색상 강도를 갖는지 확인
    public static boolean isShadeOfGray(int red, int green, int blue) {
        // 색상 간의 근접성을 체크하기 위한 임의 거리 지정 = 30
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
    }

    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        // |= : 논리연산자 OR
        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        // 픽셀을 불투명하게 하기 위해 Alpha 값을 최대로 설정
        rgb |= 0xFF000000;

        return rgb;
    }

    // getRed : 빨강색만 픽셀에서 추출
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    // getGreen : 초록색만 픽셀에서 추출
    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    // getBlue : 파랑색만 픽셀에서 추출
    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
