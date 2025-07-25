package com.trtm.sy.registerfile.tool;



import com.itextpdf.text.pdf.qrcode.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MatrixToImageWriter {

    private static final int BLACK = 0xFF000000;
     private static final int WHITE = 0xFFFFFFFF;

             private MatrixToImageWriter() {
             }

             public static BufferedImage toBufferedImage(BitMatrix matrix) {
                 int width = matrix.getWidth();
                 int height = matrix.getHeight();
                 BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                 for (int x = 0; x < width; x++) {
                         for (int y = 0; y < height; y++) {
                                 image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
                             }
                     }
                 return image;
             }

             public static boolean writeToFile(BitMatrix matrix, String format, File file) throws IOException {
                 BufferedImage image = toBufferedImage(matrix);
                 if (!ImageIO.write(image, format, file)) {
                     return false;
                 }else {
                     return true;
                 }
             }
}
