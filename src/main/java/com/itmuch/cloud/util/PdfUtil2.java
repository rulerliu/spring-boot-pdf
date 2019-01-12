package com.itmuch.cloud.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil2 {
	
	private static BaseFont bfChinese;
	static {
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void export (HttpServletResponse response, Object[][] datas) throws Exception {
		String fileName = genFileName();
		float[] widths = {100, 100, 100, 100, 100};
		
		// 页面大小设置为A4
		Rectangle pageSize = new Rectangle(PageSize.A4);
		// 创建doc对象并设置边距
		Document document = new Document(pageSize, 20, 20, 40, 40); 
		//document.setPageSize(PageSize.A4);

		setResponseHeader(response, fileName);
		PdfWriter pw = PdfWriter.getInstance(document, response.getOutputStream());
		pw.setViewerPreferences(PdfWriter.PageModeUseAttachments);
		
		document.open();
		document.addTitle("文档Title");
		document.addSubject("文档Subject");
		document.addAuthor("Robbie");
		
		// 边框颜色
		BaseColor borderColor = new BaseColor(90, 140, 200);
		// 背景颜色
		BaseColor bgColor = new BaseColor(80, 130, 180);
		
		PdfPTable pdfTable = new PdfPTable(widths);
		//pdfTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		//pdfTable.setTotalWidth(458);
		pdfTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER); // 设置table居中显示
		pdfTable.setWidthPercentage(100); // 设置table宽度为100%                
		
		PdfPCell cell = null;
		// 第一行：标题
		cell = createCell("报表标题", 24, 3, PdfPTable.ALIGN_CENTER, 50, borderColor);
		pdfTable.addCell(cell);
		
		// 表格数据开始
		for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < datas[i].length; j++) {
				if (i == 0) {
					// 第一行居左
					cell = createCell(String.valueOf(datas[i][j]), 12, PdfPTable.ALIGN_LEFT, 25, borderColor);
				}
				if (i == 1) {
					// 第二行居中
					cell = createCell(String.valueOf(datas[i][j]), 12, PdfPTable.ALIGN_CENTER, 25, borderColor, bgColor);
				}
				if (i == 2) {
					// 第三行居右
					cell = createCell(String.valueOf(datas[i][j]), 12, null, PdfPTable.ALIGN_RIGHT, 25, borderColor);
				}
				pdfTable.addCell(cell);
			}
		}
		
		// 插入图片
		Resource resource = new ClassPathResource("static/img/123.jpg");
        File file = resource.getFile();
		cell = addPicture(file);
		pdfTable.addCell(cell);
		
		//插入子表格
        pdfTable.addCell(addSubTable());
        
        for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < datas[i].length; j++) {
				if (i == 0) {
					// 第一行居左
					cell = createCell(String.valueOf(datas[i][j]), 12, PdfPTable.ALIGN_LEFT, 25, null);
				}
				if (i == 1) {
					// 第二行居中
					cell = createCell(String.valueOf(datas[i][j]), 12, PdfPTable.ALIGN_LEFT, 25, null);
				}
				if (i == 2) {
					// 第三行居右
					cell = createCell(String.valueOf(datas[i][j]), 12, PdfPTable.ALIGN_LEFT, 25, null);
				}
				pdfTable.addCell(cell);
			}
		}
        
        //文档插入绝对位置图片
        byte[] bt = FileUtils.readFileToByteArray(file);
        Image image = Image.getInstance(bt);
        int x = 30; 
        int y = 630;
        image.scaleAbsolute(mmTopx(10), mmTopx(10));// 直接设定显示尺寸
        image.setAbsolutePosition(x + document.leftMargin(),  PageSize.A4.getHeight() - y - image.getHeight() - document.topMargin());
        image.setAlt("haha");
		document.add(pdfTable);
		
		// 二维码
		BarcodeQRCode qrcode = new BarcodeQRCode("http://www.baidu.com", 1, 1, null);
		Image qrcodeImage = qrcode.getImage();
		int x2 = 4300; 
        int y2 = 6300;
        // x，y的坐标
		image.setAbsolutePosition(x2 + document.leftMargin(),  PageSize.A4.getHeight() - y2 - image.getHeight() - document.topMargin());
		qrcodeImage.scalePercent(200);
		document.add(qrcodeImage);
		
		document.close();
	}
	
	private static PdfPCell addSubTable() throws Exception {
		PdfPCell cell = new PdfPCell(); 
		PdfPTable subTable = new PdfPTable(new float[] {60});
		PdfPCell subPdfCell = new PdfPCell();
		subPdfCell.setPhrase(new Paragraph("sub1", getPdfChineseFont(12)));
		subTable.addCell(subPdfCell);
		subPdfCell = new PdfPCell();
		subPdfCell.setPhrase(new Paragraph("sub2", getPdfChineseFont(12)));
		subTable.addCell(subPdfCell);

		cell.addElement(subTable);
		return cell;
	}
	
	/**
	 * 添加图片
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static PdfPCell addPicture(File file) throws Exception {
		//单元格插入图片
		byte[] bt = FileUtils.readFileToByteArray(file);
		PdfPCell pdfCell = new PdfPCell();
		pdfCell.setImage(Image.getInstance(bt));//插入图片
		return pdfCell;
	}
	
	/**
	 * 设置字体格式
	 * @param fontsize
	 * @return
	 * @throws Exception
	 */
	private static Font getPdfChineseFont(int fontsize) throws Exception {
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
	
	/**
	 * 1  用於生成cell
	 * @param text
	 * @param fontsize
	 * @param align
	 * @param fixedHeight
	 * @param borderColor
	 * @return
	 * @throws Exception
	 */
	private static PdfPCell createCell(String text, int fontsize, Integer align, Integer fixedHeight, BaseColor borderColor) throws Exception {
	    return createCell(text, fontsize, null, align, fixedHeight, borderColor, null);
	}
	/**
	 * 2  用於生成cell
	 * @param text
	 * @param fontsize
	 * @param align
	 * @param fixedHeight
	 * @param borderColor
	 * @param bgColor
	 * @return
	 * @throws Exception
	 */
	private static PdfPCell createCell(String text, int fontsize, Integer align, Integer fixedHeight, BaseColor borderColor, BaseColor bgColor) throws Exception {
	    return createCell(text, fontsize, null, align, fixedHeight, borderColor, bgColor);
	}
	/**
	 * 3  用於生成cell
	 * @param text
	 * @param fontsize
	 * @param colspan
	 * @param align
	 * @param fixedHeight
	 * @param borderColor
	 * @return
	 * @throws Exception
	 */
	private static PdfPCell createCell(String text, int fontsize, Integer colspan, Integer align, Integer fixedHeight, BaseColor borderColor) throws Exception {
	   return createCell(text, fontsize, colspan, align, fixedHeight, borderColor, null);
	}
	
	/**
	* 用於生成cell
	* @param text          Cell文字内容
	* @param fontsize      字体大小
	* @param colspan       合并列数量
	* @param align         显示位置(左中右，Paragraph对象)
	* @param borderColor   Cell边框颜色
	* @param bgColor       Cell背景色
	* @return
	 * @throws Exception 
	*/
	private static PdfPCell createCell(String text, int fontsize, Integer colspan, Integer align, Integer fixedHeight,
			BaseColor borderColor, BaseColor bgColor) throws Exception {
		Paragraph pagragraph = new Paragraph(text, getPdfChineseFont(fontsize));
		//PdfPCell cell = new PdfPCell(pagragraph);
		PdfPCell cell = new PdfPCell();
		
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // 上中下，Element对象
		// 设置表格边框
//		cell.setBorderWidthTop(0.1f);
//		cell.setBorderWidthBottom(0.1f);
//		cell.setBorderWidthLeft(0.1f);
//		cell.setBorderWidthRight(0.1f);
//		
//		cell.setBorderColorBottom(new BaseColor(0x674ea7));
//		cell.setBorderColorLeft(new BaseColor(0x674ea7));
//		cell.setBorderColorRight(new BaseColor(0x674ea7));
//		cell.setBorderColorTop(new BaseColor(0x674ea7));
		
		if (align != null)
			cell.setHorizontalAlignment(align);
		if (colspan != null && colspan > 1) {
			cell.setRowspan(1);
			cell.setColspan(colspan);
		}
		if (fixedHeight != null) {
			cell.setFixedHeight(fixedHeight);
		}
		if (borderColor != null) {
			cell.setBorderColor(borderColor);
		}
		if (bgColor != null) {
		   cell.setBackgroundColor(bgColor);
		}
		cell.setPhrase(pagragraph);
		return cell;
	}
	
	public static void setResponseHeader(HttpServletResponse response, String fileName) throws Exception {
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
     * 毫米转像素
     * @param mm
     * @return
     */
    private static float mmTopx(float mm){
     mm = (float) (mm *3.33) ;
     return mm ;
    }
}
