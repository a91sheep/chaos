package com.store59.printapi.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RandomValidateCode {

	public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";// 放到cookie中的key
	private static Random random = new Random();

	private static int width = 120;// 图片宽
	private static int height = 40;// 图片高
	private static int lineSize = 40;// 干扰图形数量

	/*
	 * 获得字体
	 */
	private static Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 30);
	}

	/*
	 * 获得颜色
	 */
	private static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}

	/**
	 * 生成随机图片
	 */
	public static void getRandcode(HttpServletRequest request, HttpServletResponse response, String randomStr,
			String key, int timeout) {
		// BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		g.setColor(getRandColor(180, 249));
		// 绘制干扰线
		for (int i = 0; i <= lineSize; i++) {
			drowLine(g);
		}
		// 绘制随机字符
		for (int i = 0; i < randomStr.length(); i++) {
			drowString(g, randomStr, i);
		}
		Cookie cookie = new Cookie(RANDOMCODEKEY, key);
		CookieUtil.updateCookie(response, timeout, key, cookie);
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());// 将内存中的图片通过流动形式输出到客户端
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 绘制字符串
	 */
	private static String drowString(Graphics g, String randomString, int i) {
		g.setFont(getFont());
		g.setColor(new Color(100+random.nextInt(101), 100+random.nextInt(111), 100+random.nextInt(121)));
		g.translate(random.nextInt(3), random.nextInt(3));
		if (i == 0) {
			g.drawString(String.valueOf(randomString.charAt(i)), 10, 28);
		} else {
			g.drawString(String.valueOf(randomString.charAt(i)), 25 * i + 10, 28);
		}
		return randomString;
	}

	/*
	 * 绘制干扰线
	 */
	private static void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(20);
		int yl = random.nextInt(20);
//		g.drawLine(x, y, x + xl, y + yl);
		g.drawRect(x, y, x+xl, y+yl);
//		g.drawOval(x, y, xl+5, yl+5);
	}

	/* 产生一个随机的字符串 */
	public static String RandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
}