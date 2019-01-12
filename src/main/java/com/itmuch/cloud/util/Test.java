package com.itmuch.cloud.util;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Test {
	
	private static String path = "d:/docs/hello10.pdf";

	public static void main(String[] args) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(path));
		document.open();
		
		PdfPTable table = new PdfPTable(5);
		for (int aw = 0; aw < 5; aw++) {
			// 构建每一格
			table.addCell("cell");
		}
		document.add(table);
		
		PdfPTable table2 = new PdfPTable(5);
		for (int aw = 5; aw < 10; aw++) {
			if (aw == 8 || aw == 9) {
				PdfPCell pdfPCell = new PdfPCell();
				pdfPCell.setBorder(0);
				table2.addCell(pdfPCell);
			} else {
				// 构建每一格
				table2.addCell("cell");
			}
		}
		document.add(table2);
		
		document.close();
	}

	
}
