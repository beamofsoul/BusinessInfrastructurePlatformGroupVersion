package com.beamofsoul.bip.management.util;

import static com.beamofsoul.bip.management.util.ConfigurationReader.*;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * @ClassName QRCodeUtils
 * @Description 二维码生成、转换工具类
 * @author MingshuJian
 * @Date 2017年3月25日 下午1:34:00
 * @version 1.0.0
 */
public class QRCodeUtils {

	private static final int BLACK;
	private static final int WHITE;
	private static final int WIDTH;
	private static final int HEIGHT;
	private static final int MARGIN;
	private static final String SUFFIX;
	private static final String DEFAULT_IMAGE;
	static {
		Integer width = asInteger(getValue(PROJECT_COMPONENT_QRCODE_WIDTH));
		Integer height = asInteger(getValue(PROJECT_COMPONENT_QRCODE_HEIGHT));
		Integer margin = asInteger(getValue(PROJECT_COMPONENT_QRCODE_MARGIN)); 
		String suffix = asString(getValue(PROJECT_COMPONENT_QRCODE_SUFFIX));
		Boolean reversalColor = asBoolean(getValue(PROJECT_COMPONENT_QRCODE_REVERSAL_COLOR));
		
		BLACK = reversalColor ? 0xFFFFFFFF : 0xFF000000;
		WHITE = reversalColor ? 0xFF000000 : 0xFFFFFFFF;
		WIDTH = width != null ? width.intValue() : 400;
		HEIGHT = height != null ? height.intValue() : 400;
		MARGIN = margin != null ? margin.intValue() : 1;
		SUFFIX = StringUtils.isNotBlank(suffix) ? suffix.replace(".", "") : "png";
		DEFAULT_IMAGE = asString(getValue(PROJECT_COMPONENT_QRCODE_DEFAULT_IMAGE));
	}
	
	private QRCodeUtils() {}
	
	public static void generate(String textOrURL, OutputStream out) throws WriterException, IOException {
		generate(textOrURL, WIDTH, HEIGHT, out);
	}
	
	public static void generate(String textOrURL, int width, int height, OutputStream out) throws WriterException, IOException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, new Integer(MARGIN));
        BufferedImage image = convertToBufferedImage(new MultiFormatWriter().encode(textOrURL, BarcodeFormat.QR_CODE, width, height, hints));
        if (StringUtils.isNotBlank(DEFAULT_IMAGE)) applyImage(image);
        write(image, SUFFIX, out);
    }

    private static void applyImage(BufferedImage bufferedImage) throws IOException {
        Graphics2D gs = bufferedImage.createGraphics();
        ClassPathResource resource = new ClassPathResource(DEFAULT_IMAGE);
        Image image = ImageIO.read(resource.getFile());
        int left = bufferedImage.getWidth() / 2 - image.getWidth(null) / 2;
        int top = bufferedImage.getHeight() / 2 - image.getHeight(null) / 2;
        gs.drawImage(image, left, top, null);
        gs.dispose();
        image.flush();

    }

    private static BufferedImage convertToBufferedImage(BitMatrix matrix) {
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

    public static void write(BufferedImage image, String suffix, File file) throws IOException {
        if (!ImageIO.write(image, suffix, file)) {
            throw new IOException("Could not write an image of format " + suffix + " to " + file);
        }
    }

    public static void write(BufferedImage image, String suffix, OutputStream out) throws IOException {
        if (!ImageIO.write(image, suffix, out)) {
            throw new IOException("Could not write an image of format " + suffix);
        }
    }

}