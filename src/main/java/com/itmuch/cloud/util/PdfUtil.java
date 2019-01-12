package com.itmuch.cloud.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {

    private static String path = "d:/docs/"; // 生成PDF后的存放路径
    
    private static BaseFont bfChinese;
	static {
		try {
			// 中文字体，要有itext-asian.jar支持(默认的itext.jar是不支持中文的)
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public static void main(String[] args) throws Exception {
//        T t = new T();
//    	initPDF(initData());;
    	
    	String data = "[{\r\n" + 
    			"	\"idcard\": \"身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员16\",\r\n" + 
    			"	\"callNum\": \"13588888888\"	\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员二\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员一\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员15\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员14\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员13\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员11\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}, {\r\n" + 
    			"	\"idcard\": \"居民身份证\",\r\n" + 
    			"	\"jobNAME\": \"大学专科\",\r\n" + 
    			"	\"peopleNAME\": \"人员51\",\r\n" + 
    			"	\"callNum\": \"13588888888\"\r\n" + 
    			"}]";
    	
    	JSONArray parseArray = JSONObject.parseArray(data);
    	export(null, parseArray);
    }
    
    /**
     * 初始化PDF
     * 
     * @param apis
     * @throws Exception 
     */
    public static void export(HttpServletResponse response, JSONArray jsonArray) {
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs(); // 创建目录
        Document document = null;
        try {
            Rectangle pageSize = new Rectangle(PageSize.A4); // 页面大小设置为A4
            document = new Document(pageSize, 20, 20, 40, 40); // 创建doc对象并设置边距
            
//          PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(folder.getAbsolutePath() + File.separator + "API文档"+ genFileName() +".pdf"));
            setResponseHeader(response, genFileName());
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            
            writer.setPageEvent(new SdkPdfPageEvent());
            document.open();
            document.addAuthor("Ares-xby");
            document.addSubject("SDK附属API文档");
            document.addTitle("API文档");
            BaseColor borderColor = new BaseColor(90, 140, 200);
            BaseColor bgColor = new BaseColor(80, 130, 180);
            
            // 插入图片
    		Resource resource = new ClassPathResource("static/img/123.jpg");
            File file = resource.getFile();
            
            PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.2f, 0.2f, 0.2f, 0.2f });
			for (int index = 0; index < jsonArray.size(); index++) {
				if (index != 0 && index % 5 == 0) {
					document.add(table);
					//加入表格之间的空行
		            Paragraph blankRow41 = new Paragraph(18f, " ", getPdfChineseFont(11)); 
		            document.add(blankRow41);
					table = new PdfPTable(5);
				}
				
				table.setWidthPercentage(100); // 设置table宽度为100%
				table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER); // 设置table居中显示
				
				JSONObject jsonObject = (JSONObject) jsonArray.get(index);
				table.addCell(addSubTable(jsonObject, file));
				if (index % 5 != 0 && index == jsonArray.size() - 1) {
					// 插入最后几个空白格子
					for (int i = 0; i < jsonArray.size() - index % 5; i++) {
						PdfPCell nullCell = createCell("", null, null);
						nullCell.setBorder(0);
						table.addCell(nullCell);
					}
					document.add(table);
				}
			}
            
            document.close();
            System.out.println("init pdf over.");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null)
                document.close();
        }
    }

    /**
     * 初始化PDF
     * 
     * @param apis
     * @throws Exception 
     */
//    public static void initPDF(List<Api> apis) throws Exception {
//        File folder = new File(path);
//        if (!folder.exists())
//            folder.mkdirs(); // 创建目录
//        Document doc = null;
//        try {
//            // 中文字体，要有itext-asian.jar支持(默认的itext.jar是不支持中文的)
//            BaseFont bfchinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            Rectangle pageSize = new Rectangle(PageSize.A4); // 页面大小设置为A4
//            doc = new Document(pageSize, 20, 20, 40, 40); // 创建doc对象并设置边距
//            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(folder.getAbsolutePath() + File.separator + "API文档"+ genFileName() +".pdf"));
//            writer.setPageEvent(new SdkPdfPageEvent());
//            doc.open();
//            doc.addAuthor("Ares-xby");
//            doc.addSubject("SDK附属API文档");
//            doc.addTitle("API文档");
//            BaseColor borderColor = new BaseColor(90, 140, 200);
//            BaseColor bgColor = new BaseColor(80, 130, 180);
//            
//            // 插入图片
//    		Resource resource = new ClassPathResource("static/img/123.jpg");
//            File file = resource.getFile();
//            
//            for (Api api : apis) {
//				PdfPTable table = new PdfPTable(new float[] { 0.2f, 0.2f, 0.2f, 0.2f, 0.2f });
//				table.setWidthPercentage(100); // 设置table宽度为100%
//				table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER); // 设置table居中显示
//                for (int i = 0; i < api.getParams().size(); i++) {
//                    table.addCell(addPicture(file));
//                    table.addCell(addPicture(file));
//                    table.addCell(addPicture(file));
//                    table.addCell(addPicture(file));
//                    table.addCell(addPicture(file));
////                    table.addCell(createCell(api.getParams().get(i).getParamName(), 10, bfchinese, null, null, null));
//                    table.addCell(addSubTable());
//                    table.addCell(addSubTable());
//                    table.addCell(addSubTable());
//                    table.addCell(addSubTable());
//                    table.addCell(addSubTable());
//                    table.addCell(createCell("", 12, bfchinese, 5, null, null, null));
//                }
//                doc.add(table);
//            }
//            
//            doc.close();
//            System.out.println("init pdf over.");
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (doc != null)
//                doc.close();
//        }
//
//    }
    
	private static Font getPdfChineseFont(int fontsize) {
		// 中文字体，要有itext-asian.jar支持(默认的itext.jar是不支持中文的)
		Font fontChinese = new Font(bfChinese, fontsize, Font.NORMAL);
		
		// 设置字体样式
//		fontChinese.setColor(new BaseColor(0xff0000));// 颜色
//		fontChinese.setSize(16);// 大小
//		fontChinese.setStyle("bold");// 加粗
//		fontChinese.setStyle("italic");// 斜体
//		fontChinese.setStyle("underline");// 下划线
		return fontChinese;
	}
    
    private static PdfPCell addSubTable(JSONObject jsonObject, File file) {
		PdfPCell cell = new PdfPCell();
		
		PdfPTable subTable = new PdfPTable(new float[] {1f});
		subTable.setWidthPercentage(100);
		PdfPCell subPdfCell = addPicture(file);
		subPdfCell.setBorder(0);
		subTable.addCell(subPdfCell);
		
		subPdfCell = new PdfPCell();
		subPdfCell.setBorder(0);
		subPdfCell.setBorderWidthBottom(0.5f);
		subPdfCell.setPaddingLeft(0f);
		subPdfCell.setPaddingRight(0f);
		subTable.addCell(subPdfCell);
		
		subPdfCell = new PdfPCell();
		subPdfCell.setBorder(0);
		subPdfCell.setPhrase(new Paragraph("idcard：" + jsonObject.getString("idcard"), getPdfChineseFont(8)));
		subTable.addCell(subPdfCell);
		
		subPdfCell = new PdfPCell();
		subPdfCell.setBorder(0);
		subPdfCell.setPhrase(new Paragraph("jobNAME：" + jsonObject.getString("jobNAME"), getPdfChineseFont(8)));
		subTable.addCell(subPdfCell);
		
		subPdfCell = new PdfPCell();
		subPdfCell.setBorder(0);
		subPdfCell.setPhrase(new Paragraph("peopleNAME：" + jsonObject.getString("peopleNAME"), getPdfChineseFont(8)));
		subTable.addCell(subPdfCell);
		
		subPdfCell = new PdfPCell();
		subPdfCell.setBorder(0);
		subPdfCell.setPhrase(new Paragraph("callNum：" + jsonObject.getString("callNum"), getPdfChineseFont(8)));
		subTable.addCell(subPdfCell);

		cell.addElement(subTable);
		return cell;
	}
    
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
		response.setContentType("application/octet-stream;charset=utf-8");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
	}
    
    private static String genFileName () {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	String excelFileName =  sdf.format(new Date()) + ".pdf";
    	excelFileName = StringUtil.trimFileName(excelFileName).replaceAll(" ", "");
        try {
			excelFileName = new String(excelFileName.getBytes("utf-8"), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return excelFileName;
    }
    
	/**
	 * 添加图片
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static PdfPCell addPicture(File file) {
		//单元格插入图片
		PdfPCell pdfCell = new PdfPCell();
		try {
			byte[] bt = FileUtils.readFileToByteArray(file);
			Image image = Image.getInstance(bt);
			image.scalePercent(8); // 按照百分比缩放
			pdfCell.setImage(image);//插入图片
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadElementException e) {
			e.printStackTrace();
		}
		return pdfCell;
	}

    // 用於生成cell
    private static PdfPCell createCell(String text, BaseFont font, BaseColor borderColor) {
        return createCell(text, 12, font, null, null, borderColor, null);
    }
    // 用於生成cell
    private static PdfPCell createCell(String text, BaseFont font, BaseColor borderColor, BaseColor bgColor) {
        return createCell(text, 12, font, null, null, borderColor, bgColor);
    }
    // 用於生成cell
    private static PdfPCell createCell(String text, int fontsize, BaseFont font, Integer colspan, Integer align, BaseColor borderColor) {
        return createCell(text, fontsize, font, colspan, align, borderColor, null);
    }

    /**
     * 用於生成cell
     * @param text          Cell文字内容
     * @param fontsize      字体大小
     * @param font          字体
     * @param colspan       合并列数量
     * @param align         显示位置(左中右，Paragraph对象)
     * @param borderColor   Cell边框颜色
     * @param bgColor       Cell背景色
     * @return
     */
    private static PdfPCell createCell(String text, int fontsize, BaseFont font, Integer colspan, Integer align, BaseColor borderColor, BaseColor bgColor) {
        Paragraph pagragraph = new Paragraph(text, new Font(font, fontsize));
        PdfPCell cell = new PdfPCell(pagragraph);
        cell.setFixedHeight(20);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // 上中下，Element对象
        if (align != null)
            cell.setHorizontalAlignment(align);
        if (colspan != null && colspan > 1)
            cell.setColspan(colspan);
        if (borderColor != null)
            cell.setBorderColor(borderColor);
        if (bgColor != null)
            cell.setBackgroundColor(bgColor);
        return cell;
    }

    /**
     * SDK中PDF相关的PageEvent
     */
    static class SdkPdfPageEvent extends PdfPageEventHelper {

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            // 水印(water mark)
            PdfContentByte pcb = writer.getDirectContent();
            pcb.saveState();
            BaseFont bf;
            try {
                bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                pcb.setFontAndSize(bf, 36);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 透明度设置
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.2f);
            pcb.setGState(gs);

            pcb.beginText();
            pcb.setTextMatrix(60, 90);
            // 这里可以设置水印
            pcb.showTextAligned(Element.ALIGN_LEFT, "", 200, 300, 45);

            pcb.endText();
            pcb.restoreState();
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            // 页眉、页脚
            PdfContentByte pcb = writer.getDirectContent();
            try {
                pcb.setFontAndSize(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED), 10);
            } catch (Exception e) {
                e.printStackTrace();
            } // 支持中文字体
            pcb.saveState();
            
            /*InputStream logoIs = null;
            try {
                // 注意：pcb.addImage()方法要在pcb.beginText();pcb.endText();之外调用，
                // 否则生成的PDF打开时会报错: An error exists on this page. Acrobat may not display the page correctly. Please contact the person who created the PDF document to correct the problem.
            	Resource resource = new ClassPathResource("static/img/123.jpg");
                File file = resource.getFile();
                logoIs = new FileInputStream(file);
                byte[] logoBytes = new byte[1000 * 1024]; // 此处数组大小要比logo图片大小要大, 否则图片会损坏；能够直接知道图片大小最好不过.
                if(logoIs != null){
                    int logoSize = logoIs.read(logoBytes); // 尝试了一下，此处图片复制不完全，需要专门写个方法，将InputStream转换成Byte数组，详情参考org.apache.io.IOUtils.java的toByteArray(InputStream in)方法
                    if(logoSize > 0){
                        byte[] logo = new byte[logoSize];
                        System.arraycopy(logoBytes, 0, logo, 0, logoSize);
                        Image image = Image.getInstance(logo);// 如果直接使用logoBytes，并且图片是jar包中的话，会报图片损坏异常；本地图片可直接getInstance时候使用路径。
                        image.setAbsolutePosition(document.left(), document.top(-5)); // 设置图片显示位置
                        image.scalePercent(12);                                       // 按照百分比缩放
                        pcb.addImage(image);
                    }
                 }
            } catch (Exception e) {
                System.err.println(e);
            } finally {
            	try {
            		logoIs.close();
				} catch (Exception e2) {
				}
            }*/
            
            pcb.beginText();

            // Header
            float top = document.top(-15);
            // 这里在页眉加文字
            pcb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "", document.right(), top, 0);
            
            // Footer
            float bottom = document.bottom(-15);
            pcb.showTextAligned(PdfContentByte.ALIGN_CENTER, "第 " + writer.getPageNumber() + " 页", (document.right() + document.left()) / 2, bottom, 0);
            pcb.endText();
            
            pcb.restoreState();
            pcb.closePath();
        }
    }
    
}