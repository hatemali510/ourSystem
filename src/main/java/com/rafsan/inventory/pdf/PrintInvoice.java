package com.rafsan.inventory.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rafsan.inventory.entity.Item;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PrintInvoice {

    private final ObservableList<Item> items;
    private final String barcode;
    private double paid;
    private double retail;
    private double netPrice;

    public PrintInvoice(ObservableList<Item> items, String barcode, 
    		double paid, double retail, double netPrice) {
        this.items = FXCollections.observableArrayList(items);
        this.barcode = barcode;
        this.paid = paid;
        this.retail = retail;
        this.netPrice = netPrice;
    }

    public void generateReport() {

        try {
            Document document = new Document();
            
            File invcFl = new File("Invoice.pdf");
            if(invcFl.exists()) {
            	invcFl.delete();
            }
            
            invcFl.createNewFile();
            FileOutputStream fs = new FileOutputStream(invcFl, false);

            PdfWriter writer = PdfWriter.getInstance(document, fs);
            document.open();

            BaseFont bf = BaseFont.createFont("C:\\windows\\fonts\\arabtype.ttf", 
            		BaseFont.IDENTITY_H, true);
            Font font = new Font(bf, 20);
            
            //Paragraph paragraph = new Paragraph(new StringBuilder("رقم    الفاتورة").reverse().toString(), font);
            Paragraph paragraph = new Paragraph("Invoice No.", font);
            document.add(paragraph);
            //addEmptyLine(document, 1);

            PdfContentByte cb = writer.getDirectContent();
            BarcodeEAN codeEAN = new BarcodeEAN();
            codeEAN.setCodeType(codeEAN.EAN13);
            codeEAN.setCode(barcode);
            document.add(codeEAN.createImageWithBarcode(cb, BaseColor.BLACK, BaseColor.DARK_GRAY));
            addEmptyLine(document, 1);

            PdfPTable table = new PdfPTable(2);

            PdfPCell c1 = new PdfPCell(new Phrase("اليسر للتجارة والتوزيع", font));
            c1.setColspan(2);
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("01000000000", font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("التليفون", font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        	Date date = new Date();
        	
            c1 = new PdfPCell(new Phrase(dateFormat.format(date), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("تاريخ الإصدار", font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setWidthPercentage(30);
            document.add(table);
            addEmptyLine(document, 1);

            table = createTable();
            document.add(table);
            
            addEmptyLine(document, 1);

            table = new PdfPTable(2);
            c1 = new PdfPCell(new Phrase(new Double(netPrice).toString(), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("المبلغ المستحق", font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(new Double(paid).toString(), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("المبلغ المدفوع", font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(new Double(retail).toString(), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase("الباقى", font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setWidthPercentage(30);
            document.add(table);
            document.close();
            
           
        } catch (DocumentException | FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException e) {
        	System.out.println(e.getMessage());
		}
    }

    private PdfPTable createTable() {
    	BaseFont bf = null;
		try {
			bf = BaseFont.createFont("C:\\windows\\fonts\\arabtype.ttf", 
					BaseFont.IDENTITY_H, true);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Font font = new Font(bf, 20);
        
        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase("الإجمالى", font));
        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        
        c1 = new PdfPCell(new Phrase("الكمية", font));
        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("السعر", font));
        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("السلعة", font));
        c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        for (Item i : items) {
        	c1 = new PdfPCell(new Phrase(String.valueOf(i.getTotal()), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(String.valueOf(i.getQuantity()), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(String.valueOf(i.getUnitPrice()), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            
            c1 = new PdfPCell(new Phrase(i.getItemName(), font));
            c1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        return table;
    }

    private void addEmptyLine(Document document, int number) {
        for (int i = 0; i < number; i++) {
        	try {
				document.add(Chunk.NEWLINE);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
        }
    }
}
