package com.eshop.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

/**

 * 生成验证码

 * 

 * @author:xiaojingjing

 *

 */

public class VerifyCodeUtils {
	private final static String VERFY_CODE = "23456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random random = new Random();

	/**

	 * 使用系统默认字符源生成验证码

	 * 

	 * @param verfycodeSize

	 *            验证码长度

	 * @return

	 * */
	public static String generateVerifyCode(int verifyCodeSize) {
		return generateVerifyCode(verifyCodeSize, VERFY_CODE);
	}

	/**

	 * 使用指定字符源生成验证码

	 * 

	 * @param verifycodeSize

	 *            验证码长度

	 * @param 指定字符源

	 * @return String 验证码

	 * */
	public static String generateVerifyCode(int verifyCodeSize,
			String charSource) {
		if (null == charSource || charSource.length() == 0) {
			charSource = VERFY_CODE;
		}
		StringBuffer verifyCode = new StringBuffer(verifyCodeSize);
		Random localRandom = new Random(System.currentTimeMillis());
		int sourceLength = charSource.length();
		for (int i = 0; i < verifyCodeSize; i++) {
			verifyCode.append(charSource.charAt(localRandom
					.nextInt(sourceLength - 1)));
		}
		return verifyCode.toString();
	}

	/**

	 * 生成随机验证码文件，并返回验证码的值

	 * 

	 * @param int width

	 * @param int height

	 * @param int verifySize

	 * @param File

	 *            outputFile

	 * @return

	 * */
	private static String outputVerfyImage(int width, int height,	
			int verifySize, File outputFile) {
		String randomCode = generateVerifyCode(verifySize);
		outputImage(width, height,randomCode, outputFile);
		return randomCode;
	}

	/**

	 * 生成指定验证码图像文件

	 * 

	 * @param int width 图形验证码宽度

	 * @param int height 图形验证码高度

	 * @param String

	 *            code 验证字符集

	 * @param File

	 *            outputFile 验证码字符文件

	 * @return

	 * */
	public static void outputImage(int width, int height, String code,
			File outputFile) {
		if (outputFile == null) {
			return;
		}
		File dir = outputFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			outputFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(outputFile);
			outputImage(width, height, code, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**

	 * 输出指定验证码图片流

	 * 

	 * @param int width

	 * @param int height

	 * @param String

	 *            code

	 * @param FileOutputStream

	 *            fos

	 * @return

	 * @throws IOException 

	 * */
	public static void outputImage(int width, int height, String code,
			OutputStream os) throws IOException {
		// 图片buffer 流

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 2

		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Color[] colors = new Color[5];
		Color[] colorSpace = new Color[] { Color.WHITE, Color.CYAN,
				Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,Color.PINK,Color.YELLOW };
		float[] fractions = new float[colors.length]; 
		
		for(int i=0;i<colors.length;i++){
			colors[i] = colorSpace[random.nextInt(colorSpace.length)];
			fractions[i] = random.nextFloat();
		}	
		Arrays.sort(fractions);
		// 设置边框颜色

		graphics2d.setColor(Color.GRAY);
		graphics2d.fillRect(0,0,width,height);
		
		// 设置背景颜色

		Color backgroundColor  = getRandomColor(200, 250);
		graphics2d.setColor(backgroundColor);
		graphics2d.fillRect(0,2, width, height-4);
		// 绘制干扰线

		
		graphics2d.setColor(getRandomColor(160,200));
		for(int i=0;i<20;i++){
			int x = random.nextInt(width-1);
			int y = random.nextInt(height-1);
			int x1 = random.nextInt(6)+1;
			int y1 = random.nextInt(12)+1;
			graphics2d.drawLine(x, y,x+40+x1, y+20+y1);
		}
		// 添加噪点

		float noiseRate = 0.05f;
		int area=(int)(width*height*noiseRate);
		for(int i = 0;i<area;i++){
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int rgb = getRandomIntColor();
			image.setRGB(x, y, rgb);
		}
		
	    // 是图片扭曲

		shear(graphics2d, width, height, backgroundColor);
		
		// 设置字体

		graphics2d.setColor(getRandomColor(100, 160));
		int fontSize = height-4;
		Font font = new Font("Algerian",Font.ITALIC,fontSize);
		graphics2d.setFont(font);
		// 把code 放进图片里

		int codeLength = code.length();
		char[] chars = code.toCharArray();
		for(int i=0;i<codeLength;i++){
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.setToRotation(Math.PI/4*random.nextDouble()*(random.nextBoolean()?1:-1),(width/codeLength)*i+fontSize/2,height/2);
			graphics2d.setTransform(affineTransform);
			graphics2d.drawChars(chars,i,1, ((width-10)/codeLength)*i+5, height/2+fontSize/2-10);
		}
		graphics2d.dispose();
		ImageIO.write(image, "jpg", os);
		
	}

	private static Color getRandomColor(int fc, int bc) {
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int gap = bc - fc;
		int r = fc + random.nextInt(gap);
		int g = fc + random.nextInt(gap);
		int b = fc + random.nextInt(gap);
		return new Color(r, g, b);
	}

	private static int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	private static int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	public static void shear(Graphics g, int width, int height, Color color) {
		shearX(g, width, height, color);
		shearY(g, width, height, color);
	}

	public static void shearX(Graphics g, int width, int height, Color color) {
		int period = random.nextInt(2);
		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);
		for (int i = 0; i < height; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(0, i, width, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + width, i,width, i);
			}
		}
	}

	public static void shearY(Graphics g, int width, int height, Color color) {
		int period = random.nextInt(40) + 10;
		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < width; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(i, 0, 1, height, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + height, i, height);
			}
		}

	}
	
	public static void main(String args[]){
			File dir = new File("E:/verify");
			int width = 200,height = 80;
			for(int i = 0;i<10;i++){
				String verifyCode = generateVerifyCode(4);
				File file = new File(dir,verifyCode+".jpg");
				outputImage(width, height, verifyCode, file);
			}
	}
}
