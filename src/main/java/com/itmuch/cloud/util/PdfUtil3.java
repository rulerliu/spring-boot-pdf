package com.itmuch.cloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil3 {

    private static String path = "d:/docs/"; // 生成PDF后的存放路径

    public static void main(String[] args) {
//        T t = new T();
        initPDF(initData());
    }

    /**
     * 初始化PDF
     * 
     * @param apis
     */
    public static void initPDF(List<Api> apis) {
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdirs(); // 创建目录
        Document doc = null;
        try {
            // 中文字体，要有itext-asian.jar支持(默认的itext.jar是不支持中文的)
            BaseFont bfchinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Rectangle pageSize = new Rectangle(PageSize.A4); // 页面大小设置为A4
            doc = new Document(pageSize, 20, 20, 40, 40); // 创建doc对象并设置边距
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(folder.getAbsolutePath() + File.separator + "API文档.pdf"));
            writer.setPageEvent(new SdkPdfPageEvent());
            doc.open();
            doc.addAuthor("Ares-xby");
            doc.addSubject("SDK附属API文档");
            doc.addTitle("API文档");
            BaseColor borderColor = new BaseColor(90, 140, 200);
            BaseColor bgColor = new BaseColor(80, 130, 180);
            for (Api api : apis) {
                PdfPTable table = new PdfPTable(new float[] {0.25f, 0.25f, 0.25f, 0.25f});
                // table.setWidthPercentage(100); // 设置table宽度为100%
                // table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER); // 设置table居中显示
                for (int i = 0; i < api.getParams().size(); i++) {
                    if (i == 0) {
                        // row 1
                        table.addCell(createCell("API", bfchinese, borderColor, bgColor));
                        table.addCell(createCell(api.getApiName(), 12, bfchinese, 3, null, borderColor, bgColor));
                        // row 2
                        table.addCell(createCell("描述", bfchinese, borderColor));
                        table.addCell(createCell(api.getApiDesc(), 12, bfchinese, 3, null, borderColor));
                    } else {
                        table.addCell(createCell(api.getParams().get(i).getParamName(), 10, bfchinese, null, Paragraph.ALIGN_RIGHT, borderColor));
                        table.addCell(createCell(api.getParams().get(i).getParamName(), 10, bfchinese, null, null, borderColor));
                        table.addCell(createCell(api.getParams().get(i).getParamType(), 10, bfchinese, null, null, borderColor));
                        table.addCell(createCell(api.getParams().get(i).getParamDesc(), 10, bfchinese, null, null, borderColor));
                    }
                }
                doc.add(table);
            }
            // 二维码
            BarcodeQRCode qrcode = new BarcodeQRCode("http://www.baidu.com", 1, 1, null);
            Image qrcodeImage = qrcode.getImage();
            qrcodeImage.setAbsolutePosition(10, 600);
            qrcodeImage.scalePercent(200);
            doc.add(qrcodeImage);
            doc.close();
            System.out.println("init pdf over.");
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (doc != null)
                doc.close();
        }

    }

    public static List<Api> initData() {
        List<Api> list = new ArrayList<Api>();
        for (int i = 0; i < 100; i++) {
            Api api = new Api();
            api.setApiName("api-" + i);
            api.setApiDesc("描述-" + i);
            int paramSize = new Random().nextInt(20);
            List<Params> paramList = new ArrayList<Params>();
            for (int j = 0; j < paramSize; j++) {
                Params param = new Params();
                param.setParamName("param-" + i + "-" + j);
                param.setParamType("paramType-" + i + "-" + j);
                param.setParamDesc("描述-" + i + "-" + j);
                paramList.add(param);
            }
            api.setParams(paramList);
            list.add(api);
        }
        System.out.println("init data over. size=" + list.size());
        return list;
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
            pcb.showTextAligned(Element.ALIGN_LEFT, "XX公司有限公司", 200, 300, 45);

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
            try {
                // pcb.addImage()方法要在pcb.beginText();pcb.endText();之外调用，
                // 否则生成的PDF打开时会报错: An error exists on this page. Acrobat may not display the page correctly. Please contact the person who created the PDF document to correct the problem.
            	Resource resource = new ClassPathResource("static/img/123.jpg");
                File file = resource.getFile();
                InputStream logoIs = new FileInputStream(file);
                 byte[] logoBytes = new byte[1000 * 1024]; // 此处数组大小要比logo图片大小要大, 否则图片会损坏；能够直接知道图片大小最好不过.
//                 InputStream logoIs = getClass().getResourceAsStream(logoPath);
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
            }
            pcb.beginText();

            // Header
            float top = document.top(-15);
            pcb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "XX开放平台API文档", document.right(), top, 0);
            // Footer
            float bottom = document.bottom(-15);
            pcb.showTextAligned(PdfContentByte.ALIGN_CENTER, "第 " + writer.getPageNumber() + " 页", (document.right() + document.left()) / 2, bottom, 0);
            pcb.endText();
            
            pcb.restoreState();
            pcb.closePath();
        }
    }
    /**
     * POJO for init Data.
     */
    static class Api {

        private String apiName;
        private String apiDesc;
        private List<Params> params;

        public String getApiName() {
            return apiName;
        }
        public void setApiName(String apiName) {
            this.apiName = apiName;
        }
        public String getApiDesc() {
            return apiDesc;
        }
        public void setApiDesc(String apiDesc) {
            this.apiDesc = apiDesc;
        }
        public List<Params> getParams() {
            return params;
        }
        public void setParams(List<Params> params) {
            this.params = params;
        }
    }

    /**
     * POJO for init Data.
     */
    static class Params {

        private String paramName;
        private String paramType;
        private String paramDesc;

        public String getParamName() {
            return paramName;
        }
        public void setParamName(String paramName) {
            this.paramName = paramName;
        }
        public String getParamType() {
            return paramType;
        }
        public void setParamType(String paramType) {
            this.paramType = paramType;
        }
        public String getParamDesc() {
            return paramDesc;
        }
        public void setParamDesc(String paramDesc) {
            this.paramDesc = paramDesc;
        }
    }
}