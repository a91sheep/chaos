package com.yunyin.print.main.api.common.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.yunyin.print.common.model.PrintOrder;
import com.yunyin.print.common.model.PrintOrderDetail;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:linxh@59store.com">linxiaohui</a>
 * @version 1.0 16/12/19
 * @since 1.0
 */
public class PDFUtil {

    public static byte[] manipulatePdf(InputStream inputStream, int w, int h)
            throws IOException, DocumentException {
        int sum = w * h;
        byte[] bbb = toByteArray(inputStream);
        // Creating a reader
//        PdfReader reader = new PdfReader(inputStream);
        PdfReader reader = new PdfReader(bbb);

        float page_width = reader.getPageSize(1).width();
        float page_height = reader.getPageSize(1).height();

        boolean is_ppt = false;
        boolean is_pptx = false;
        boolean is_widescreen = false;
        //pptx 宽:720.0  高:405.0
        //ppt  宽:720   高:540
        if (page_width == 720.0f) {
            if (page_height < 500) {
                is_pptx = true;
            } else {
                is_ppt = true;
            }
        } else if (page_width == 960.0f && page_height == 540.0f) {
            reader.close();
            is_widescreen = true;
            reader = new PdfReader(scaleppt_widescreen(bbb));
            page_width = reader.getPageSize(1).width();
            page_height = reader.getPageSize(1).height();
        }

        int add_width = 0;
        int add_height = 0;
        if (sum == 6) {
            add_width = 600;
        }
        // step 1
        Document document = null;
        if (is_ppt || is_pptx || is_widescreen) {
            document = new Document(new Rectangle(595.0F * w + add_width, 842.0F * h + add_height).rotate());
        } else {
            document = new Document(new Rectangle(595.0F * w + add_width, 842.0F * h + add_height));
        }

        // step 2
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer
                = PdfWriter.getInstance(document, out);
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();

        int n = reader.getNumberOfPages();
        int p = 1;

        if (sum == 2) {
            while (p <= n) {
                if (is_pptx) {
                    addPage(canvas, reader, p + 1, 61, 130);
                    addPage(canvas, reader, p, 61, page_height + 250);
                    document.newPage();
                } else if (is_ppt) {
                    addPage(canvas, reader, p + 1, 61, 36);
                    addPage(canvas, reader, p, 61, page_height + 72);
                    document.newPage();
                } else if (is_widescreen) {
                    addPage(canvas, reader, p + 1, 11, 89);
                    addPage(canvas, reader, p, 11, page_height + 178);
                    document.newPage();
                } else {
                    addPage(canvas, reader, p, 0, 30);
                    addPage(canvas, reader, p + 1, page_width, 30);
                    document.newPage();
                }
                p += 2;
            }
        } else if (sum == 4) {
            while (p <= n) {
                if (is_pptx) {
                    addPage(canvas, reader, p + 2, 81, 130);
                    addPage(canvas, reader, p + 3, page_width + 162, 130);
                    addPage(canvas, reader, p, 81, page_height + 250);
                    addPage(canvas, reader, p + 1, page_width + 162, page_height + 250);
                    document.newPage();
                } else if (is_ppt) {
                    addPage(canvas, reader, p + 2, 81, 36);
                    addPage(canvas, reader, p + 3, page_width + 162, 36);
                    addPage(canvas, reader, p, 81, page_height + 72);
                    addPage(canvas, reader, p + 1, page_width + 162, page_height + 72);
                    document.newPage();
                } else if (is_widescreen) {
                    addPage(canvas, reader, p + 2, 13, 89);
                    addPage(canvas, reader, p + 3, 26 + page_width, 89);
                    addPage(canvas, reader, p, 13, page_height + 178);
                    addPage(canvas, reader, p + 1, 26 + page_width, page_height + 178);
                    document.newPage();
                } else {
                    addPage(canvas, reader, p + 2, 0, 10);
                    addPage(canvas, reader, p + 3, page_width, 10);
                    addPage(canvas, reader, p, 0, page_height + 30);
                    addPage(canvas, reader, p + 1, page_width, page_height + 30);
                    document.newPage();
                }
                p += 4;
            }
        } else if (sum == 6) {
            while (p <= n) {
                if (is_pptx) {
                    addPage(canvas, reader, p + 4, 81, 292);
                    addPage(canvas, reader, p + 5, page_width + 162, 292);
                    addPage(canvas, reader, p + 2, 81, page_height + 584);
                    addPage(canvas, reader, p + 3, page_width + 162, page_height + 584);
                    addPage(canvas, reader, p, 81, page_height * 2 + 876);
                    addPage(canvas, reader, p + 1, page_width + 162, page_height * 2 + 876);
                    document.newPage();
                } else if (is_ppt) {
                    addPage(canvas, reader, p + 4, 81, 191);
                    addPage(canvas, reader, p + 5, page_width + 162, 191);
                    addPage(canvas, reader, p + 2, 81, page_height + 382);
                    addPage(canvas, reader, p + 3, page_width + 162, page_height + 382);
                    addPage(canvas, reader, p, 81, page_height * 2 + 573);
                    addPage(canvas, reader, p + 1, page_width + 162, page_height * 2 + 573);
                    document.newPage();
                } else if (is_widescreen) {
                    addPage(canvas, reader, p + 4, 15, 250);
                    addPage(canvas, reader, p + 5, page_width + 30, 250);
                    addPage(canvas, reader, p + 2, 15, page_height + 500);
                    addPage(canvas, reader, p + 3, page_width + 30, page_height + 500);
                    addPage(canvas, reader, p, 15, page_height * 2 + 750);
                    addPage(canvas, reader, p + 1, page_width + 30, page_height * 2 + 750);
                    document.newPage();
                } else {
                    addPage(canvas, reader, p + 3, 150, 10);
                    addPage(canvas, reader, p + 4, 300 + page_width, 10);
                    addPage(canvas, reader, p + 5, 450 + page_width * 2, 10);
                    addPage(canvas, reader, p, 150, page_height + 30);
                    addPage(canvas, reader, p + 1, 300 + page_width, page_height + 30);
                    addPage(canvas, reader, p + 2, 450 + page_width * 2, page_height + 30);
                    document.newPage();
                }
                p += 6;
            }
        } else if (sum == 9) {
            while (p <= n) {
                if (is_pptx) {
                    addPage(canvas, reader, p + 6, 91, 142);
                    addPage(canvas, reader, p + 7, page_width + 182, 142);
                    addPage(canvas, reader, p + 8, page_width * 2 + 273, 142);
                    addPage(canvas, reader, p + 3, 91, page_height + 284);
                    addPage(canvas, reader, p + 4, page_width + 182, page_height + 284);
                    addPage(canvas, reader, p + 5, page_width * 2 + 273, page_height + 284);
                    addPage(canvas, reader, p, 91, page_height * 2 + 426);
                    addPage(canvas, reader, p + 1, page_width + 182, page_height * 2 + 426);
                    addPage(canvas, reader, p + 2, page_width * 2 + 273, page_height * 2 + 426);
                    document.newPage();
                } else if (is_ppt) {
                    addPage(canvas, reader, p + 6, 91, 41);
                    addPage(canvas, reader, p + 7, page_width + 182, 41);
                    addPage(canvas, reader, p + 8, page_width * 2 + 273, 41);
                    addPage(canvas, reader, p + 3, 91, page_height + 82);
                    addPage(canvas, reader, p + 4, page_width + 182, page_height + 82);
                    addPage(canvas, reader, p + 5, page_width * 2 + 273, page_height + 82);
                    addPage(canvas, reader, p, 91, page_height * 2 + 123);
                    addPage(canvas, reader, p + 1, page_width + 182, page_height * 2 + 123);
                    addPage(canvas, reader, p + 2, page_width * 2 + 273, page_height * 2 + 123);
                    document.newPage();
                } else if (is_widescreen) {
                    addPage(canvas, reader, p + 6, 16, 100);
                    addPage(canvas, reader, p + 7, page_width + 32, 100);
                    addPage(canvas, reader, p + 8, page_width * 2 + 48, 100);
                    addPage(canvas, reader, p + 3, 16, page_height + 200);
                    addPage(canvas, reader, p + 4, page_width + 32, page_height + 200);
                    addPage(canvas, reader, p + 5, page_width * 2 + 48, page_height + 200);
                    addPage(canvas, reader, p, 16, page_height * 2 + 300);
                    addPage(canvas, reader, p + 1, page_width + 32, page_height * 2 + 300);
                    addPage(canvas, reader, p + 2, page_width * 2 + 48, page_height * 2 + 300);
                    document.newPage();
                } else {
                    addPage(canvas, reader, p + 6, 0, 10);
                    addPage(canvas, reader, p + 7, page_width, 10);
                    addPage(canvas, reader, p + 8, page_width * 2, 10);
                    addPage(canvas, reader, p + 3, 0, page_height + 10);
                    addPage(canvas, reader, p + 4, page_width, page_height + 10);
                    addPage(canvas, reader, p + 5, page_width * 2, page_height + 10);
                    addPage(canvas, reader, p, 0, page_height * 2 + 30);
                    addPage(canvas, reader, p + 1, page_width, page_height * 2 + 30);
                    addPage(canvas, reader, p + 2, page_width * 2, page_height * 2 + 30);
                    document.newPage();
                }
                p += 9;
            }
        }
        // step 5
        document.close();
        reader.close();

        if (sum != 1) {
            return scalePDF(out.toByteArray(), sum, is_ppt, is_pptx);
        } else {
            return out.toByteArray();
        }
    }

    public static byte[] scalePDF(byte[] stream, int sum, boolean is_ppt, boolean is_pptx) {

        PdfReader reader = null;
        try {
            reader = new PdfReader(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float scale_width = 0.5f;
        float scale_height = 0.5f;

        Document document = new Document();
        if (sum == 2) {
            float page_width = reader.getPageSize(1).width();
            float page_height = reader.getPageSize(1).height();
            scale_width = 0.706f;
            scale_height = 0.706f;
            document = new Document(new Rectangle(page_width * scale_width, page_height * scale_height));
        }
        if (sum == 4) {
            scale_width = 0.5f;
            scale_height = 0.5f;
        }
        if (sum == 6) {
            scale_width = 0.353f;
            scale_height = 0.353f;
            document = new Document(new Rectangle(PageSize.A4.rotate()));
        }
        if (sum == 9) {
            scale_width = 0.333f;
            scale_height = 0.333f;
        }

        PdfWriter writer = null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            writer = PdfWriter.getInstance(document, out);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();

        //--------------
        int n = reader.getNumberOfPages();
        Image page = null;

        ScaleEvent event = new ScaleEvent(0.5f);
        writer.setPageEvent(event);

        for (int p = 1; p <= n; p++) {
            try {
                page = Image.getInstance(writer.getImportedPage(reader, p));
            } catch (BadElementException e) {
                e.printStackTrace();
            }
            page.setAbsolutePosition(0, 0);
            page.scalePercent(scale_width * 100, scale_height * 100);
            try {
                document.add(page);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            if (p < n) {
                event.setPageDict(reader.getPageN(p + 1));
            }
            if (n == 1) {
                event.setPageDict(reader.getPageN(p));
            }
            document.newPage();
        }
        document.close();

        return out.toByteArray();
    }

    public static byte[] scaleppt_widescreen(byte[] inputStream) {

        PdfReader reader = null;
        try {
            reader = new PdfReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float scale_width = 0.854f;
        float scale_height = 0.854f;

        //宽屏缩放后比例
        Document document = new Document(new Rectangle(820, 461));

        PdfWriter writer = null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            writer = PdfWriter.getInstance(document, out);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();

        //--------------
        int n = reader.getNumberOfPages();
        Image page = null;

        ScaleEvent event = new ScaleEvent(0.5f);
        writer.setPageEvent(event);

        for (int p = 1; p <= n; p++) {
            try {
                page = Image.getInstance(writer.getImportedPage(reader, p));
            } catch (BadElementException e) {
                e.printStackTrace();
            }
            page.setAbsolutePosition(0, 0);
            page.scalePercent(scale_width * 100, scale_height * 100);
            try {
                document.add(page);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            if (p < n) {
                event.setPageDict(reader.getPageN(p + 1));
            }
            if (n == 1) {
                event.setPageDict(reader.getPageN(p));
            }
            document.newPage();
        }
        document.close();

        System.out.println("宽屏缩放好拉啊!!!!!");
        return out.toByteArray();
    }

    private static void addPage(PdfContentByte canvas,
                                PdfReader reader, int p, float x, float y) {
        if (p > reader.getNumberOfPages()) return;
        PdfImportedPage page = canvas.getPdfWriter().getImportedPage(reader, p);
        if (page != null) {
            canvas.addTemplate(page, x, y);
        }
    }

    //    TEST DEMO
//    public static void main(String[] args) {
//        try {
//            List<PrintOrderDetail> list = new ArrayList<>();
//            PrintOrderDetail detail1 = new PrintOrderDetail();
//            detail1.setReprintType((byte) 0);
//            detail1.setPrintType((byte) 1);
//            detail1.setNum(2);
//            detail1.setPageNum(10);
//            detail1.setFileName("西游记.doc");
//            list.add(detail1);
//            PrintOrderDetail detail2 = new PrintOrderDetail();
//            detail2.setReprintType((byte) 1);
//            detail2.setPrintType((byte) 2);
//            detail2.setFileName("红楼梦.pdf");
//            detail2.setNum(3);
//            detail2.setPageNum(5);
//            list.add(detail2);
//
//            PrintOrder printOrder = new PrintOrder();
//            printOrder.setAddress("59二楼");
//            printOrder.setRemark("此人危险,优先配送!此人危险,优先配送!此人危险,优先配送!");
//            printOrder.setPhone("13688889999");
//            printOrder.setUname("好人");
//            printOrder.setOrderId(20151229102938001L);
//            printOrder.setCreateTime(1459830192L);
//            printOrder.setDetails(list);
//            printOrder.setTotalAmount(1.1);
//            printOrder.setDeliveryAmount(0.51);
//
//            createPdf(printOrder, "http://print-identification.oss-cn-hangzhou.aliyuncs.com/4cf66e64cf7e8323937950eac39b9004.jpg",
//                    "http://print-identification.oss-cn-hangzhou.aliyuncs.com/38a29602d3bf7f3b9866b921f58d224e.jpg",
//                    "http://print-identification.oss-cn-hangzhou.aliyuncs.com/9d5a14bd09416d063d45c6c26918829f.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }

    public static byte[] createPdf(PrintOrder printOrder, String picUrl, String deliveryImage, String imageLogoUrl) throws IOException, DocumentException {
        //TODO 根据业务需求调整
        return null;
//        int fontSize = 10;
//        int fontEnglish = 4;
//        Map<String, String> printStyleMap = new HashMap<>();
//        printStyleMap.put("1", "黑白单面");
//        printStyleMap.put("2", "黑白双面");
//        printStyleMap.put("3", "彩色单面");
//        printStyleMap.put("4", "彩色双面");
//
//        Map<String, String> scaleStyleMap = new HashMap<>();
//        scaleStyleMap.put("0", "不缩印");
//        scaleStyleMap.put("1", "一页两面");
//        scaleStyleMap.put("2", "一页四面");
//        scaleStyleMap.put("3", "一页六面");
//        scaleStyleMap.put("4", "一页九面");
//
//        Document document = new Document();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PdfWriter writer = PdfWriter.getInstance(document, out);
//        document.open();
//
//        BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, BaseFont.NOT_EMBEDDED);
//
//        //step1 广告页
////        PdfPTable adTable = new PdfPTable(new float[]{1});
////        adTable.setWidthPercentage(100);
////        PdfPCell adImageCell = createImageCell(picUrl, Element.ALIGN_CENTER);
////        adImageCell.setBorder(Rectangle.NO_BORDER);
////        adImageCell.setFixedHeight(600f);
////        adTable.addCell(adImageCell);
////        document.add(adTable);
//
//        Image img = Image.getInstance(picUrl);
//        img.scaleToFit(532, 600);
//        img.setAlignment(1);
//        document.add(img);
//
////        Paragraph p1 = new Paragraph("我操测试下", new Font(baseFont, fontSize, 1));
////        p1.setAlignment(Element.ALIGN_CENTER);
////        document.add(p1);
//
//        //step 2 订单统计信息
//        PdfPTable orderLineTable = new PdfPTable(new float[]{3, 3, 9, 4, 8, 7, 3});
//        orderLineTable.setWidthPercentage(100);
//        //配送方式
//        PdfPCell deliveryCell = createImageCell(deliveryImage, Element.ALIGN_LEFT);
//        deliveryCell.setBorder(Rectangle.NO_BORDER);
//        orderLineTable.addCell(deliveryCell);
//        //订单号
//        PdfPCell orderIdCell = createTextCell("订单号: ", Element.ALIGN_RIGHT, fontSize);
//        orderIdCell.setBorder(Rectangle.NO_BORDER);
//        orderLineTable.addCell(orderIdCell);
//
//        PdfPCell cell = new PdfPCell();
//        cell.setBorder(Rectangle.NO_BORDER);
//        cell.setPadding(0);
//        Paragraph idP = new Paragraph(printOrder.getOrderId().substring(0, 17) + "...");
//        idP.setAlignment(Element.ALIGN_LEFT);
//        cell.addElement(idP);
//        orderLineTable.addCell(cell);
//
//        //下单时间
//        PdfPCell createTimeIdCell = createTextCell("下单时间: "
//                , Element.ALIGN_RIGHT, fontSize);
//        createTimeIdCell.setBorder(Rectangle.NO_BORDER);
//        orderLineTable.addCell(createTimeIdCell);
//
//        PdfPCell timeCell = new PdfPCell();
//        timeCell.setBorder(Rectangle.NO_BORDER);
//        timeCell.setPadding(0);
//        Paragraph timeP = new Paragraph(DateUtil.timestampToDateStr(printOrder.getCreateTime().intValue()) + "");
//        timeP.setAlignment(Element.ALIGN_LEFT);
//        timeCell.addElement(timeP);
//        orderLineTable.addCell(timeCell);
//
//        //金额
//        int totalPageNum = 0;
//        int detailPageNum = 0;
//        double totalAmount = 0;
//        BigDecimal documentAmount = new BigDecimal(printOrder.getTotalAmount());
//        BigDecimal deliveryAmount = new BigDecimal(printOrder.getDeliveryAmount());
//        Double couponDiscount = printOrder.getCouponDiscount() == null ? 0.00 : printOrder.getCouponDiscount();
//        BigDecimal oldCouponAmount = new BigDecimal(-couponDiscount); //优惠券的钱需要减去
//        totalAmount = documentAmount.add(deliveryAmount).add(oldCouponAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
//
//        for (PrintOrderDetail detail : printOrder.getDetails()) {
//            detailPageNum = detail.getPageNum() * detail.getNum();
//            totalPageNum += detailPageNum;
//        }
//
//        PdfPCell sumCell = createTextCell("共打印" + totalPageNum + "页 合计:￥"
//                , Element.ALIGN_LEFT, fontSize);
//        sumCell.setBorder(Rectangle.NO_BORDER);
//        orderLineTable.addCell(sumCell);
//
//        PdfPCell sCell = new PdfPCell();
//        sCell.setBorder(Rectangle.NO_BORDER);
//        sCell.setPadding(0);
//        Paragraph sP = new Paragraph("" + totalAmount);
//        sP.setAlignment(Element.ALIGN_RIGHT);
//        sCell.addElement(sP);
//        orderLineTable.addCell(sCell);
//
//        document.add(orderLineTable);
//
//        //订单详情列表
//        PdfPTable detailTable = new PdfPTable(new float[]{5, 2, 2, 2});
//        detailTable.setWidthPercentage(100);
//        //表头
//        PdfPCell fileName = new PdfPCell();
//        Paragraph p_file = new Paragraph("文件名称", new Font(baseFont, fontSize, 1));
//        p_file.setAlignment(Element.ALIGN_CENTER);
//        fileName.addElement(p_file);
//        PdfPCell printStyle = new PdfPCell();
//        Paragraph p_printStyle = new Paragraph("打印样式", new Font(baseFont, fontSize, 1));
//        p_printStyle.setAlignment(Element.ALIGN_CENTER);
//        printStyle.addElement(p_printStyle);
//        PdfPCell scaleSet = new PdfPCell();
//        Paragraph p_scaleSet = new Paragraph("缩印设置", new Font(baseFont, fontSize, 1));
//        p_scaleSet.setAlignment(Element.ALIGN_CENTER);
//        scaleSet.addElement(p_scaleSet);
//        PdfPCell printNum = new PdfPCell();
//        Paragraph p_printNum = new Paragraph("打印数量", new Font(baseFont, fontSize, 1));
//        p_printNum.setAlignment(Element.ALIGN_CENTER);
//        printNum.addElement(p_printNum);
//
//        detailTable.addCell(fileName);
//        detailTable.addCell(printStyle);
//        detailTable.addCell(scaleSet);
//        detailTable.addCell(printNum);
//
//        PdfPCell blankCell = createTextCell(" ", Element.ALIGN_CENTER, fontSize);
//        blankCell.setBorder(Rectangle.NO_BORDER);
//
//        int nameLength = 20;
//        if (printOrder.getDetails().size() == 1) {
//            PrintOrderDetail detail = printOrder.getDetails().get(0);
//            // row 1
////            String docName = detail.getFileName().substring(0,detail.getFileName().lastIndexOf("."));
//            String docName = detail.getFileName();
//            if (docName.length() > nameLength) {
//                docName = docName.substring(0, nameLength) + "...";
//            }
//            detailTable.addCell(new Paragraph(docName, new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(printStyleMap.get(detail.getPrintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(scaleStyleMap.get(detail.getReprintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(detail.getPageNum() + "页 x " + detail.getNum() + "份", new Font(baseFont, fontSize)));
//            // row 2
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//            // row 3
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//        } else if (printOrder.getDetails().size() == 2) {
//            // row 1
//            PrintOrderDetail detail1 = printOrder.getDetails().get(0);
//            String docName1 = detail1.getFileName();
//            if (docName1.length() > nameLength) {
//                docName1 = docName1.substring(0, nameLength) + "...";
//            }
//            detailTable.addCell(new Paragraph(docName1, new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(printStyleMap.get(detail1.getPrintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(scaleStyleMap.get(detail1.getReprintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(detail1.getPageNum() + "页 x " + detail1.getNum() + "份", new Font(baseFont, fontSize)));
//            // row 2
//            PrintOrderDetail detail2 = printOrder.getDetails().get(1);
//            String docName2 = detail2.getFileName();
//            if (docName2.length() > nameLength) {
//                docName2 = docName2.substring(0, nameLength) + "...";
//            }
//            detailTable.addCell(new Paragraph(docName2, new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(printStyleMap.get(detail2.getPrintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(scaleStyleMap.get(detail2.getReprintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(detail2.getPageNum() + "页 x " + detail2.getNum() + "份", new Font(baseFont, fontSize)));
//            // row 3
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//            detailTable.addCell(blankCell);
//        } else {
//            // row 1s
//            PrintOrderDetail detail1 = printOrder.getDetails().get(0);
//            String docName1 = detail1.getFileName();
//            if (docName1.length() > nameLength) {
//                docName1 = docName1.substring(0, nameLength) + "...";
//            }
//            detailTable.addCell(new Paragraph(docName1, new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(printStyleMap.get(detail1.getPrintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(scaleStyleMap.get(detail1.getReprintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(detail1.getPageNum() + "页 x " + detail1.getNum() + "份", new Font(baseFont, fontSize)));
//            // row 2
//            PrintOrderDetail detail2 = printOrder.getDetails().get(1);
//            String docName2 = detail2.getFileName();
//            if (docName2.length() > nameLength) {
//                docName2 = docName2.substring(0, nameLength) + "...";
//            }
//            detailTable.addCell(new Paragraph(docName2, new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(printStyleMap.get(detail2.getPrintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(scaleStyleMap.get(detail2.getReprintType() + ""), new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph(detail2.getPageNum() + "页 x " + detail2.getNum() + "份", new Font(baseFont, fontSize)));
//            // row 3
//
//            detailTable.addCell(new Paragraph("...", new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph("...", new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph("...", new Font(baseFont, fontSize)));
//            detailTable.addCell(new Paragraph("...", new Font(baseFont, fontSize)));
//        }
//        document.add(detailTable);
//        //配送文字信息
//        PdfPTable bootomTable = new PdfPTable(2);
//        bootomTable.setWidths(new float[]{1, 1});
//        bootomTable.setWidthPercentage(100);
//        StringBuilder uname = new StringBuilder();
//        uname.append("用户名:");
//        if (!StringUtil.isBlank(printOrder.getUname())) {
//            uname.append(printOrder.getUname());
//        }
//        StringBuilder phoneSb = new StringBuilder();
//        phoneSb.append("联系电话:");
//        if (!StringUtil.isBlank(printOrder.getPhone())) {
//            phoneSb.append(printOrder.getPhone());
//        }
//        StringBuilder addressSb = new StringBuilder();
//        addressSb.append("寝室地址:");
//        if (!StringUtil.isBlank(printOrder.getAddress())) {
//            addressSb.append(printOrder.getAddress());
//        }
//        StringBuilder remarkSb = new StringBuilder();
//        remarkSb.append("备注:");
//        if (!StringUtil.isBlank(printOrder.getRemark())) {
//            remarkSb.append(printOrder.getRemark());
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(uname.toString() + "\n");
//        sb.append(phoneSb.toString() + "\n");
//        sb.append(addressSb.toString() + "\n");
//        System.out.println("remarkSb length:" + remarkSb.toString().length());
//        sb.append(remarkSb.toString());
//        PdfPCell userInfoCell = createTextCell(sb.toString(), Element.ALIGN_LEFT, fontSize);
//        userInfoCell.setBorder(Rectangle.NO_BORDER);
//        bootomTable.addCell(userInfoCell);
//        PdfPCell logoImage = createImageCell(imageLogoUrl, Element.ALIGN_RIGHT);
//        logoImage.setBorder(Rectangle.NO_BORDER);
//        bootomTable.addCell(logoImage);
//        document.add(bootomTable);
//
//        document.close();
//
//        return out.toByteArray();
    }

    //TODO  页脚添加广告
//    public static byte[] headerAndFooter(InputStream inputStream, List<AdFooterMsg> adList, int index, int num) throws IOException, DocumentException {
//        PdfReader reader = new PdfReader(inputStream);
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PdfStamper stamper = new PdfStamper(reader, out);
//        BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, BaseFont.NOT_EMBEDDED);
//
//        for (int i = 1; i <= num; i++) {
////            Phrase footer = new Phrase(adList.get(index).getSlogan(), new Font(baseFont, 10));
////            index += 1;
////            float x = reader.getPageSize(i).width() / 2;
////            float y2 = reader.getPageSize(i).bottom(20);
////            ColumnText.showTextAligned(
////                    stamper.getOverContent(i), Element.ALIGN_CENTER,
////                    footer, x, y2, 0);
//            float y = reader.getPageSize(i).bottom(50);
////        	String content = adList.get(i-1).getSlogan();
////        	if(adList.get(i-1).getImg()!=null&&content==null){
//            Image imf = Image.getInstance(adList.get(i - 1).getImg());
//            imf.setAlignment(Image.MIDDLE);
//            imf.scaleAbsoluteHeight(10);
//            imf.scaleAbsoluteWidth(10);
//            if (reader.getPageSize(i).width() < imf.width()) {
//                float p = reader.getPageSize(i).width() / imf.width();
//                imf.scalePercent(p * 100);
//            } else {
//                imf.scalePercent(40);
//            }
//            Chunk chunk = new Chunk(imf, 0, -35);
//            Phrase footer = new Phrase(chunk);
//            ColumnText.showTextAligned(
//                    stamper.getOverContent(i), Element.ALIGN_LEFT,
//                    footer, 0, y, 0);
////        	}
////        	if(adList.get(i-1).getImg()!=null&&content!=null){
////        		Image imf = Image.getInstance(adList.get(i-1).getImg());
////        		imf.setAlignment(Image.MIDDLE);
////        		imf.scaleAbsoluteHeight(10);
////        		imf.scaleAbsoluteWidth(10);
////        		imf.scalePercent(40);
////        		Chunk chunk = new Chunk(imf, 0, -45);
////        		Phrase footer = new Phrase(chunk);
////        		ColumnText.showTextAligned(
////                        stamper.getOverContent(i), Element.ALIGN_LEFT,
////                        footer, 0, y, 0);
////        	}
//
////            if(content!=null){
////            	System.out.println("length:" + content.length());
////            	if (content.length() > 150) {
////            		content = content.substring(0, 150);
////            	}
////            	if (content.length() <= 50) {
////            		float x=(reader.getPageSize(i).width()-60)*(50-content.length())/100f;
////            		y=reader.getPageSize(i).bottom(25);
////            		Phrase footer1 = new Phrase(content, new Font(baseFont, 10));
////            		ColumnText.showTextAligned(
////            				stamper.getOverContent(i), Element.ALIGN_LEFT,
////            				footer1, 60+x, y, 0);
////            	}
////            	if (content.length() > 50 && content.length() <= 100) {
////            		String text1 = content.substring(0, 50);
////            		String text2 = content.substring(50);
////            		Phrase footer1 = new Phrase(text1, new Font(baseFont, 10));
////            		ColumnText.showTextAligned(
////            				stamper.getOverContent(i), Element.ALIGN_LEFT,
////            				footer1, 60, y, 0);
////            		Phrase footer2 = new Phrase(text2, new Font(baseFont, 10));
////            		ColumnText.showTextAligned(
////            				stamper.getOverContent(i), Element.ALIGN_LEFT,
////            				footer2, 60, y - 15, 0);
////            	}
////            	if (content.length() > 100 && content.length() <= 150) {
////            		String text1 = content.substring(0, 50);
////            		String text2 = content.substring(50, 100);
////            		String text3 = content.substring(100);
////            		Phrase footer1 = new Phrase(text1, new Font(baseFont, 10));
////            		ColumnText.showTextAligned(
////            				stamper.getOverContent(i), Element.ALIGN_LEFT,
////            				footer1, 60, y, 0);
////            		Phrase footer2 = new Phrase(text2, new Font(baseFont, 10));
////            		ColumnText.showTextAligned(
////            				stamper.getOverContent(i), Element.ALIGN_LEFT,
////            				footer2, 60, y - 15, 0);
////            		Phrase footer3 = new Phrase(text3, new Font(baseFont, 10));
////            		ColumnText.showTextAligned(
////            				stamper.getOverContent(i), Element.ALIGN_LEFT,
////            				footer3, 60, y - 30, 0);
////            	}
////            }
//        }
//        stamper.close();
//        reader.close();
//        return out.toByteArray();
//    }

    public static void main(String[] args) throws IOException, DocumentException {
        headerAndFooter_test("", "");
    }

    private static void headerAndFooter_test(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, BaseFont.NOT_EMBEDDED);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            Image imf = Image.getInstance("http://print-identification.oss-cn-hangzhou.aliyuncs.com/a88813292d0f7c799a6d637369ddaef1.png");
            imf.setAlignment(Image.MIDDLE);
            imf.scaleAbsoluteHeight(10);
            imf.scaleAbsoluteWidth(reader.getPageSize(i).width());
            if (reader.getPageSize(i).width() < imf.width()) {
                float p = reader.getPageSize(i).width() / imf.width();
                imf.scalePercent(p * 100);
            } else {
                imf.scalePercent(40);
            }
            Chunk chunk = new Chunk(imf, 0, -35);
            Phrase footer = new Phrase(chunk);
            float y = reader.getPageSize(i).bottom(50);
            ColumnText.showTextAligned(
                    stamper.getOverContent(i), Element.ALIGN_LEFT,
                    footer, 0, y, 0);

//            String content = "扫码关注“59云印”。进入“服务>投放广告”，这个广告位在等你哦~";
//            System.out.println("length:" + content.length());
//            if (content.length() > 150) {
//                content = content.substring(0, 150);
//            }
//
//            if (content.length() <= 50) {
//            	float x=(reader.getPageSize(i).width()-60)*(50-content.length())/100f;
//            	y=reader.getPageSize(i).bottom(25);
//                Phrase footer1 = new Phrase(content, new Font(baseFont, 10));
//                ColumnText.showTextAligned(
//                        stamper.getOverContent(i), Element.ALIGN_LEFT,
//                        footer1, 60+x, y, 0);
//            }
//            if (content.length() > 50 && content.length() <= 100) {
//                String text1 = content.substring(0, 50);
//                String text2 = content.substring(50);
//                Phrase footer1 = new Phrase(text1, new Font(baseFont, 10));
//                ColumnText.showTextAligned(
//                        stamper.getOverContent(i), Element.ALIGN_LEFT,
//                        footer1, 60, y, 0);
//                Phrase footer2 = new Phrase(text2, new Font(baseFont, 10));
//                ColumnText.showTextAligned(
//                        stamper.getOverContent(i), Element.ALIGN_LEFT,
//                        footer2, 60, y - 15, 0);
//            }
//            if (content.length() > 100 && content.length() <= 150) {
//                String text1 = content.substring(0, 50);
//                String text2 = content.substring(50, 100);
//                String text3 = content.substring(100);
//                Phrase footer1 = new Phrase(text1, new Font(baseFont, 10));
//                ColumnText.showTextAligned(
//                        stamper.getOverContent(i), Element.ALIGN_LEFT,
//                        footer1, 60, y, 0);
//                Phrase footer2 = new Phrase(text2, new Font(baseFont, 10));
//                ColumnText.showTextAligned(
//                        stamper.getOverContent(i), Element.ALIGN_LEFT,
//                        footer2, 60, y - 15, 0);
//                Phrase footer3 = new Phrase(text3, new Font(baseFont, 10));
//                ColumnText.showTextAligned(
//                        stamper.getOverContent(i), Element.ALIGN_LEFT,
//                        footer3, 60, y - 30, 0);
//            }
        }
        stamper.close();
        reader.close();
    }

    private static void scalePDF_test(String src, String dest) {
        PdfReader reader = null;
        try {
            reader = new PdfReader(src);
            System.out.println("scale前---pdf-width:" + reader.getPageSize(1).width());
            System.out.println("scale前---pdf-height:" + reader.getPageSize(1).height());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        float scale_width = 0.471f;
//        float scale_height = 0.353f;
        float scale_width = 0.362f;
        float scale_height = 0.345f;
        float page_width = reader.getPageSize(1).width();
        float page_height = reader.getPageSize(1).height();

//        event.setPageDict(reader.getPageN(1));

        //-------------
        Document document = new Document(new Rectangle(page_width * scale_width, page_height * scale_height));
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();

        //--------------
        int n = reader.getNumberOfPages();
        Image page = null;

        ScaleEvent event = new ScaleEvent(0.5f);
        writer.setPageEvent(event);

        for (int p = 1; p <= n; p++) {
            try {
                page = Image.getInstance(writer.getImportedPage(reader, p));
            } catch (BadElementException e) {
                e.printStackTrace();
            }
            page.setAbsolutePosition(0, 0);
//            page.scalePercent(scale * 100);
            page.scalePercent(scale_width * 100, scale_height * 100);
            try {
                document.add(page);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            if (p < n) {
                event.setPageDict(reader.getPageN(p + 1));
            }
            document.newPage();
        }
        document.close();

        try {
            PdfReader reader2 = new PdfReader(dest);
            System.out.println("scale后---pdf-width:" + reader2.getPageSize(1).width());
            System.out.println("scale后---pdf-height:" + reader2.getPageSize(1).height());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path
     * @param align 0:左对齐 , 1:中   2:右
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    private static PdfPCell createImageCell(String path, int align) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
//        PdfPCell cell = new PdfPCell(img, true);
        PdfPCell cell = new PdfPCell();
        cell.addElement(img);
        if (align == Element.ALIGN_LEFT) {
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        } else if (align == Element.ALIGN_CENTER) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        } else {
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }
        return cell;

    }

    private static PdfPCell createTextCell(String text, int align, int fontSize) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, BaseFont.NOT_EMBEDDED);
        PdfPCell cell = new PdfPCell();
        cell.setPadding(0);
        Paragraph p = new Paragraph(text, new Font(baseFont, fontSize));
        p.setAlignment(align);
        cell.addElement(p);
//        cell.setHorizontalAlignment(align);
        return cell;
    }

    private static void manipulatePdf22(String src, String dest, int w, int h)
            throws IOException, DocumentException {
        // Creating a reader
        PdfReader reader = new PdfReader(src);

        float page_width = reader.getPageSize(1).width();
        float page_height = reader.getPageSize(1).height();

        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer
                = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        document.newPage();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();

        int n = reader.getNumberOfPages();
        int p = 1;
        int sum = w * h;

        if (sum == 2) {
            while (p <= n) {
                addPage(canvas, reader, p, 0, 0);
                addPage(canvas, reader, p + 1, page_width, 0);
                document.newPage();
                p += 2;
            }
        } else if (sum == 4) {
            while (p <= n) {
                addPage(canvas, reader, p + 2, 0, 0);
                addPage(canvas, reader, p + 3, page_width, 0);
                addPage(canvas, reader, p, 0, page_height);
                addPage(canvas, reader, p + 1, page_width, page_height);
                document.newPage();
                p += 4;
            }
        } else if (sum == 6) {
            while (p <= n) {
                addPage(canvas, reader, p + 3, 90, 10);
                addPage(canvas, reader, p + 4, 270 + page_width, 10);
                addPage(canvas, reader, p + 5, 450 + page_width * 2, 10);
                addPage(canvas, reader, p, 90, page_height + 30);
                addPage(canvas, reader, p + 1, 270 + page_width, page_height + 30);
                addPage(canvas, reader, p + 2, 450 + page_width * 2, page_height + 30);
                document.newPage();
                p += 6;
            }
        } else if (sum == 9) {
            while (p <= n) {
                addPage(canvas, reader, p + 6, 0, 0);
                addPage(canvas, reader, p + 7, page_width, 0);
                addPage(canvas, reader, p + 8, page_width * 2, 0);
                addPage(canvas, reader, p + 3, 0, page_height);
                addPage(canvas, reader, p + 4, page_width, page_height);
                addPage(canvas, reader, p + 5, page_width * 2, page_height);
                addPage(canvas, reader, p, 0, page_height * 2);
                addPage(canvas, reader, p + 1, page_width, page_height * 2);
                addPage(canvas, reader, p + 2, page_width * 2, page_height * 2);
                document.newPage();
                p += 9;
            }
        }
        // step 5
        document.close();
        reader.close();
    }

    public static byte[] toByteArray(InputStream input)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output)
            throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}