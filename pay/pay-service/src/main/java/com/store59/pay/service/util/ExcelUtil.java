package com.store59.pay.service.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.store59.pay.model.PayDetail;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 导出excel工具类
 * @author liutongbin
 *
 */
public class ExcelUtil {
	/***************************************************************************
	  * @param fileName EXCEL文件名称
	  * @param listTitle EXCEL文件第一行列标题集合
	  * @param listContent EXCEL文件正文数据集合
	  * @return
	  */
	public  final static String exportExcel(String fileName,String[] Title, List<PayDetail> listContent) {
	  String result="系统提示：Excel文件导出成功！";  
	  // 以下开始输出到EXCEL
	  try {    
	   /** **********创建工作簿************ */
	   File file = new File(fileName);
	   if(!file.exists())
	   {
	      file.createNewFile();
	   }
	   WritableWorkbook workbook = Workbook.createWorkbook(file);
	   /** **********创建工作表************ */
	 
	   WritableSheet sheet = workbook.createSheet("Sheet1", 0);
	 
	   /** **********设置纵横打印（默认为纵打）、打印纸***************** */
	   jxl.SheetSettings sheetset = sheet.getSettings();
	   sheetset.setProtected(false);
	 
	 
	   /** ************设置单元格字体************** */
	   WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
	   WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);
	 
	   /** ************以下设置三种单元格样式，灵活备用************ */
	   // 用于标题居中
	   WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
	   wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
	   wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
	   wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
	   wcf_center.setWrap(false); // 文字是否换行
	    
	   // 用于正文居左
	   WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
	   wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
	   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
	   wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
	   wcf_left.setWrap(false); // 文字是否换行   
	  
	 
	   /** ***************以下是EXCEL开头大标题，暂时省略********************* */
	   //sheet.mergeCells(0, 0, colWidth, 0);
	   //sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
	   /** ***************以下是EXCEL第一行列标题********************* */
	   for (int i = 0; i < Title.length; i++) {
	    sheet.addCell(new Label(i, 0,Title[i],wcf_center));
	   }   
	   /** ***************以下是EXCEL正文数据********************* */
	   int i=1;
	   for(PayDetail detail:listContent){
	       int j=0;
	       sheet.addCell(new Label(j++, i,detail.getReceivorNo(),wcf_left));
	       sheet.addCell(new Label(j++, i,detail.getReceivorName(),wcf_left));
	       sheet.addCell(new Label(j++, i,detail.getReceivorOpenBankName(),wcf_left));
	       sheet.addCell(new Label(j++, i,String.valueOf(detail.getAmount()),wcf_left));
	       sheet.addCell(new Label(j++, i,detail.getOpenBankCode(),wcf_left));
	       
	       sheet.addCell(new Label(j++, i,detail.getReceivorBranchName(),wcf_left));
	       sheet.addCell(new Label(j++, i,detail.getReceivorCertNo(),wcf_left));
	       sheet.addCell(new Label(j++, i,detail.getReceivorMobile(),wcf_left));
	       sheet.addCell(new Label(j++, i,detail.getReceivorAlipayNo(),wcf_left));
	       sheet.addCell(new Label(j++, i,detail.getRemark(),wcf_left));
	       
	       sheet.addCell(new Label(j++, i,detail.getCheckingId(),wcf_left));
	       i++;
	   }
	   /** **********将以上缓存中的内容写到EXCEL文件中******** */
	   workbook.write();
	   /** *********关闭文件************* */
	   workbook.close();   
	 
	  } catch (Exception e) {
	   result="系统提示：Excel文件导出失败，原因："+ e.toString();
	   e.printStackTrace();
	  }
	  return result;
	 }
	
	public static List<PayDetail> readExcel(InputStream is, String batchTransNo, String batchNo) throws Exception{
		List<PayDetail> list = new ArrayList<>();
		Workbook workbook = Workbook.getWorkbook(is);
		 // Sheet的下标是从0开始的
		 // 获取第一张Sheet表
		 Sheet sheets = workbook.getSheet(0);
		 // 获取Sheet表中所包含的总行数
		 int sheetRows = sheets.getRows();
		 // 获取指这下单元格的对象引用(从第二行开始，第一行是标题)
		 for (int i = 1; i < sheetRows; i++) {
			 Cell cellCheckingId = sheets.getCell(10, i);
			 Cell cellTradeStatus = sheets.getCell(11, i);
			 Cell cellTradeDesc = sheets.getCell(12, i);
			 PayDetail detail = new PayDetail();
			 detail.setTradeNo(batchNo);
			 detail.setCheckingId(cellCheckingId.getContents());
			 detail.setTradeStatus("SUCCESS".equals(cellTradeStatus.getContents()) ? 1 : 2);
			 detail.setTradeDesc(cellTradeDesc == null ? "" : cellTradeDesc.getContents());
			 list.add(detail);
			 detail = null;
		 }
		 workbook.close();
		 return list;
	}
}