package com.rafsan.inventory.controller.report;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.controller.pos.InvoiceController;
import com.rafsan.inventory.entity.Invoice;
import com.rafsan.inventory.entity.Item;
import com.rafsan.inventory.entity.Sale;
import com.rafsan.inventory.pdf.PrintInvoice;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ViewController implements Initializable {
    
    private static Session session;

    @FXML
    private TextField employeeField, totalField, vatField, discountField, payableField, paidField, returnedField;
    @FXML
    private Label serialLabel, dateLabel;
    private Invoice invoice;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void setReport(Invoice invoice){
        this.invoice = invoice;
        setData();
    }
    
    private void setData() {
        serialLabel.setText("Transcation ID# " + invoice.getId());
        dateLabel.setText("Date: " + invoice.getDate());
        employeeField.setText(invoice.getEmployee().getUserName());
        totalField.setText(String.valueOf(invoice.getTotal()));
        vatField.setText(String.valueOf(invoice.getVat()));
        discountField.setText(String.valueOf(invoice.getDiscount()));
        payableField.setText(String.valueOf(invoice.getPayable()));
        paidField.setText(String.valueOf(invoice.getPaid()));
        returnedField.setText(String.valueOf(invoice.getReturned()));
    }
    
   
    @FXML
    public void InvoiceDetails(ActionEvent event) throws IOException {

    	 ObservableList<Sale> list = FXCollections.observableArrayList();
         session = HibernateUtil.getSessionFactory().getCurrentSession();
         session.beginTransaction();
         List<Sale> products = (List<Sale>) session.createCriteria(Sale.class)
                 .add(Restrictions.eq("invoice.id", invoice.getId())).list();
         session.beginTransaction().commit();
         products.stream().forEach(list::add);
         ObservableList<Item> items=FXCollections.observableArrayList();
         for(int i=0;i<list.size();i++) {
        	 double quantity=list.get(i).getQuantity();
        	double price= list.get(i).getPrice();
        	 String name=list.get(i).getProduct().getProductName();
        	 double total=list.get(i).getTotal();
        	items.add(new Item(name, price, quantity, total));
         }
         
    	 PrintInvoice pi = new PrintInvoice(items, invoice.getId());
         pi.generateReport();
         Desktop desktop = Desktop.getDesktop();
         File invcFl = new File("Invoice.pdf");
         desktop.open(invcFl);
    	
    }
    
    @FXML
    public void closeAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
