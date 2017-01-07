package com.store59.printapi.common.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;

/**
 * 图片工具类，完成图片的截取
 */
public class IamgesResize {
	private static Log log = LogFactory.getLog(IamgesResize.class);
	private volatile static Font font_ch=null;
	public static byte[] getPic(InputStream in, int width, int hight) throws IOException {
		BufferedImage srcImage = ImageIO.read(in);
		if (width > 0 || hight > 0) {
			int sw = srcImage.getWidth();
			int sh = srcImage.getHeight();
			// 如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去
			if (sw > width || sh > hight) {
				srcImage = rize(srcImage, width, hight);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(srcImage, "jpeg", os);
				return os.toByteArray();
			} else {
				log.info("原图片的大小小于要缩放的大小，不需要缩小");
				srcImage = rizeMiddle(srcImage, width, hight);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(srcImage, "jpeg", os);
				return os.toByteArray();
			}
		}
		log.error("要转换的大小不能为负数：width：" + width + ";heigh" + hight + ";不做转换处理");
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(srcImage, "jpeg", os);
		return os.toByteArray();
	}

	/**
	 * 
	 * @param srcBufImage
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage rize(BufferedImage srcBufImage, int width, int height) {
		int x = 0, y = 0;
		int width1 = 0, height1 = 0;
		if (srcBufImage.getWidth() >= srcBufImage.getHeight()) { // 根据像素大的一方进行等比缩放
//			width1 = width;
//			height1 = (int) (srcBufImage.getHeight() * width / srcBufImage.getWidth());
			//旋转90
			srcBufImage=Rotate(srcBufImage,90);
			height1 = height;
			width1 = (int) (srcBufImage.getWidth() * height / srcBufImage.getHeight());
		} else {
			height1 = height;
			width1 = (int) (srcBufImage.getWidth() * height / srcBufImage.getHeight());
		}
		BufferedImage src = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 根据计算好的宽高新建画布
		Graphics2D g = src.createGraphics();
		g.setColor(Color.WHITE);// 设置笔刷白色
		g.fillRect(0, 0, width, height);
		x = (int) (width - width1) / 2;
		y = (int) (height - height1) / 2;
		g.drawImage(srcBufImage, x < 0 ? 0 : x, y < 0 ? 0 : y, width1, height1, null);
		log.info("大图居中x:" + x + "y:" + y);
		return src;
	}

	public static BufferedImage rizeMiddle(BufferedImage srcBufImage, int width, int height) {
		int x = 0, y = 0;
		BufferedImage src = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // 根据计算好的宽高新建画布
		Graphics2D g = src.createGraphics();
		g.setColor(Color.WHITE);// 设置笔刷白色
		g.fillRect(0, 0, width, height);
		int width1 = (int) (srcBufImage.getWidth());
		int height1 = (int) (srcBufImage.getHeight());
		x = (int) (width - width1) / 2;
		y = (int) (height - height1) / 2;
		g.drawImage(srcBufImage.getScaledInstance(width1, height1, Image.SCALE_SMOOTH), x, y, null);
		log.info("小图居中x:" + x + "y:" + y);
		return src;
	}

	/**
	 * 判断网址是不是图片类型。
	 * 
	 * @param fromUrl
	 * @return
	 */
	public static boolean isFromImgUrl(String imgfuffix) {
		boolean isImage = false;

		// 支持的图片后缀。
		String[] imgSuffixs = { "jpg", "JPG", "jpeg", "JPEG", "gif", "GIF", "png", "PNG", "bmp", "BMP" };
		for (int i = 0; i < imgSuffixs.length; i++) {
			if (imgfuffix.equals(imgSuffixs[i])) {
				isImage = true;
				break;
			}
		}
		return isImage;
	}
    public static BufferedImage Rotate(BufferedImage src, int angel) {  
        int src_width = src.getWidth();  
        int src_height = src.getHeight();  
        // calculate the new image size  
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(  
                src_width, src_height)), angel);  
  
        BufferedImage res = null;  
        res = new BufferedImage(rect_des.width, rect_des.height,  
                BufferedImage.TYPE_INT_RGB);  
        Graphics2D g2 = res.createGraphics();  
        // transform  
        g2.translate((rect_des.width - src_width) / 2,  
                (rect_des.height - src_height) / 2);  
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);  
  
        g2.drawImage(src, null, null);  
        return res;  
    }  
  
    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {  
        if (angel >= 90) {  
            if(angel / 90 % 2 == 1){  
                int temp = src.height;  
                src.height = src.width;  
                src.width = temp;  
            }  
            angel = angel % 90;  
        }  
  
        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;  
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;  
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;  
        double angel_dalta_width = Math.atan((double) src.height / src.width);  
        double angel_dalta_height = Math.atan((double) src.width / src.height);  
  
        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_width));  
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha  
                - angel_dalta_height));  
        int des_width = src.width + len_dalta_width * 2;  
        int des_height = src.height + len_dalta_height * 2;  
        return new java.awt.Rectangle(new Dimension(des_width, des_height));  
    }  
	/**
	 * 二维码内嵌生成每个店铺的
	 * @param Url
	 * @param name
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static byte[] createImage(String Url,String name) throws MalformedURLException, IOException {
		BufferedImage srcImage = ImageIO.read(new URL(Url));
		Double width=srcImage.getWidth()*(1.375);//(60+320+60)/320
		Double height=srcImage.getHeight()*1.375;//(80+320+40)/320
		BufferedImage src = new BufferedImage(width.intValue(), height.intValue(), BufferedImage.TYPE_INT_RGB); // 根据计算好的宽高新建画布
		Graphics2D g = src.createGraphics();
		g.setColor(Color.WHITE);// 设置笔刷白色
		g.fillRect(0, 0, width.intValue(), height.intValue());
		g.setColor(Color.BLACK);
//		g.setFont( new Font("宋体",Font.PLAIN,40));
		if(font_ch==null){
			Font font=getFont();
			g.setFont(font.deriveFont(Font.PLAIN, 40));
		}else{
			g.setFont(font_ch.deriveFont(Font.PLAIN, 40));
		}
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                RenderingHints.VALUE_ANTIALIAS_ON);
		if(name.length()<=12){
			Double length=getStringWidth(name);
			Double x=(width*60/440)+(width*320/440)*(1-(length)/12.00)/2+width*10/440;//width*10/440 为字体的一半宽度
			Double y=height*(40+20)/440;//20为字体高度
			g.drawString(name, x.intValue(), y.intValue());
		}else{
			String name_up=name.substring(0,12);
			String name_down=name.substring(12);
			Double length_up=getStringWidth(name_up);
			Double length_down=getStringWidth(name_down);
			Double x_up=(width*60/440)+(width*320/440)*(1-(length_up)/12.00)/2+width*10/440;//width*10/440 为字体的一半宽度
			Double x_down=(width*60/440)+(width*320/440)*(1-(length_down)/12.00)/2+width*10/440;//width*10/440 为字体的一半宽度
			Double y_up=height*(45)/440;
			Double y_down=height*(40+35)/440;
			g.drawString(name_up, x_up.intValue(), y_up.intValue());
			g.drawString(name_down, x_down.intValue(), y_down.intValue());
		}
		Double imgx=width*60/440;
		Double imgy=height*80/440;
		g.drawImage(srcImage, imgx.intValue(), imgy.intValue(), null);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(src, "png", os);
		return os.toByteArray();
	}
	public static void main(String[] args) throws Exception {
//		createImage("","123456二三四五六七八九");
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//				String[] fontFamilies = ge.getAvailableFontFamilyNames();
//				for (String s : fontFamilies) {
//				    System.out.println(s);
//				}
		System.out.println(getStringWidth("666691234的打印"));
	}
	private static Double getStringWidth(String name){
		Double length=0D;
		for(int i=0;i<(name.length()<12?name.length():12);i++){
			if(IamgesResize.isChineseCharacter(name.substring(i,i+1))){
				length=length+1;
			}else{
				length=length+0.5;
			}
		}
		return length;
	}
	private  static Font getFont(){
	    if(font_ch==null){
	    	createFont();
	    }
		return font_ch;
	}
	private synchronized static Font createFont(){
		Font font;
        try  
        {  
        	if(font_ch!=null)return font_ch;
            font = Font.createFont(Font.TRUETYPE_FONT, getOssObj());  
            font_ch=font;
        }  
        catch (Exception e)  
        {  
        	log.error(e.getMessage());
            return null;
        }  
	return font;
	}
	private static InputStream getOssObj(){
		String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
		String bucketName="print-identification";
		String accessKeyId = "vxHYcjeAQgjiUa1H";
		String accessKeySecret = "I2TIn74UydTjOM7XMgsyqktsOXn7ke";
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		OSSObject ossObject=ossClient.getObject(bucketName, "1d79c5767453099a7801de78209b4544.ttc");
		InputStream objStream = ossObject.getObjectContent();
		return objStream;
	}
    public static final boolean isChineseCharacter(String chineseStr) {  
        char[] charArray = chineseStr.toCharArray();  
        for (int i = 0; i < charArray.length; i++) {  
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {  
                return true;  
            }  
        }  
        return false;  
    }  
}
