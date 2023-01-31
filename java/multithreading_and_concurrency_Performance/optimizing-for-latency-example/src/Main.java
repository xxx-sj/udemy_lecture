/*
 * MIT License
 *
 * Copyright (c) 2019 Michael Pogrebinsky
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Optimizing for Latency Part 2 - Image Processing
 * https://www.udemy.com/java-multithreading-concurrency-performance-optimization
 */
public class Main {
    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {

        //이미지 IO를 통해 resource가 있는 path를 전달하여 읽어들여 buffedImage를 만든다.
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        //결과 이미지 버퍼를 만드는데, 기존 이미지의 넓이, 높이를 넘기고, type_int_rgb를 넘긴다.
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        //recolorSingleThreaded(originalImage, resultImage);
        int numberOfThreads = 1;
        recolorMultithreaded(originalImage, resultImage, numberOfThreads);
        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);

        System.out.println(String.valueOf(duration));
    }

    public static void recolorMultithreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        //width는 모든 thread 가동일하며,
        int width = originalImage.getWidth();
        //height 같은 경우 각각의 스레드가 나누어 할 영역을 정하기 위해 나누었다.
        int height = originalImage.getHeight() / numberOfThreads;

        for(int i = 0; i < numberOfThreads ; i++) {
            //thread multiplier 제곱 수
            final int threadMultiplier = i;

            //task를 추가한다.
            Thread thread = new Thread(() -> {
                //시작점
                int xOrigin = 0 ; 
                int yOrigin = height * threadMultiplier;

                recolorImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
            });
            //thread array에 추가한다.
            threads.add(thread);
        }

        for(Thread thread : threads) {
            thread.start();
        }

        for(Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner,
                                    int width, int height) {
        for(int x = leftCorner ; x < leftCorner + width && x < originalImage.getWidth() ; x++) {
            for(int y = topCorner ; y < topCorner + height && y < originalImage.getHeight() ; y++) {
                recolorPixel(originalImage, resultImage, x , y);
            }
        }
    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        //각 픽셀의 x,y 값에서 rgb를 추출하여 rgb에 넣는다.
        int rgb = originalImage.getRGB(x, y);

        //값 추출
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        int newRed;
        int newGreen;
        int newBlue;

        if(isShadeOfGray(red, green, blue)) {
            //만약 gray라면, 색상을 바이올렛으로 변경한다.
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }
        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs( green - blue) < 30;
    }

    //값을 넘긴 색상에서 rgb를 만드는 method
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0; //기본적으로 0을 기본으로 하며,

        // |= 연산은 a |= b -> a = a | b; 연산과 같다. 즉,
        // rgb |= blue ->      rgb = rgb | blue 이다.
        rgb |= blue;  //blue  0과 blu
        rgb |= green << 8; //green 1byte 왼쪽으로 8비트 시프트 연산을 한 이유는 grenn의 값을 오른쪽 기준 
        // 8비트 지나서 있기 때문에
        rgb |= red << 16; //red 2byte
        //red도 동일하게 16비트 왼쪽에 존재하기 때문이다.

        rgb |= 0xFF000000;  //rgb에 0xff 한 이유는 argb의 값 중 a [opacity]의 값이 ff = 1 이여야 하기 때문에

        return rgb;
    }

    //아래와 동일하며, 시프트 연산자로 16비트를 오른쪽으로 밀게되면
    // 0000 0000 0000 0000 0000 0000 ? ? 이 되게된다.
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    //아래와 동일하며 가운데 g를 남기기 위해
    // rgb & 0000 0000 0000 0000 1111 1111 0000 0000
    //에 쉬프트 연산자로 오른쪽으로 8비트를 밀게되면
    // 0000 0000 0000 0000 0000 0000 1111 1111 이 되게된다.
    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8;
    }

    // bit연산을 통해 blue [arg(b)]에 해당하는 값만 꺼내기 위해 나머지는 모두 0으로 처리
    // 0x는 16진수 0x [00 00 00 FF]
    // 16진수를 2진수로 변경하면
    // 0000 0000 0000 0000 0000 0000 0000 0000
    // rgb & 0000 0000 0000 0000 0000 0000 1111 1111
    // rgb 중에 b만 남게된다.
    public static int getBlue(int rgb) {
        return rgb & 0x000000FF;
    }
}
