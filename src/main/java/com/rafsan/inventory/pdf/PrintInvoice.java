package com.rafsan.inventory.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rafsan.inventory.entity.Item;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.PrinterName;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PrintInvoice {

    private final ObservableList<Item> items;
    private final String barcode;
    
   
    public PrintInvoice(ObservableList<Item> items, String barcode) {
        this.items = FXCollections.observableArrayList(items);
        this.barcode = barcode;
    }

    public void generateReport() {

        try {
            Document document = new Document();
            
            //from
            File invcFl = new File("Invoice.pdf");
            if(invcFl.exists()) {
            	invcFl.delete();
            }
            
            invcFl.createNewFile();
            FileOutputStream fs = new FileOutputStream(invcFl, false);
            //to
            PdfWriter writer = PdfWriter.getInstance(document, fs);
            document.open();

            Paragraph paragraph = new Paragraph("Product ID");
            document.add(paragraph);
            addEmptyLine(paragraph, 5);

            PdfContentByte cb = writer.getDirectContent();
            BarcodeEAN codeEAN = new BarcodeEAN();
            codeEAN.setCodeType(codeEAN.EAN13);
            codeEAN.setCode(barcode);
            document.add(codeEAN.createImageWithBarcode(cb, BaseColor.BLACK, BaseColor.DARK_GRAY));
            addEmptyLine(paragraph, 5);

            PdfPTable table = createTable();
            document.add(table);

            document.close();
        } catch (DocumentException | FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException e) {
        	System.out.println(e.getMessage());
		}
    }

    private PdfPTable createTable() {

        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase("Item"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Price"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Quantity"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        for (Item i : items) {
            table.addCell(i.getItemName());
            table.addCell(String.valueOf(i.getUnitPrice()));
            table.addCell(String.valueOf(i.getQuantity()));
            table.addCell(String.valueOf(i.getTotal()));
        }

        return table;
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
    
   
}
