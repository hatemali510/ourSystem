package com.rafsan.inventory.model;

import com.rafsan.inventory.HibernateUtil;
import com.rafsan.inventory.dao.InvoiceDao;
import com.rafsan.inventory.entity.Invoice;
import com.rafsan.inventory.entity.Sale;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

public class InvoiceModel implements InvoiceDao {

    private static Session session;

    @Override
    public ObservableList<Invoice> getInvoices() {

        ObservableList<Invoice> list = FXCollections.observableArrayList();

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Invoice> products = session.createQuery("from Invoice").list();
        session.beginTransaction().commit();
        products.stream().forEach(list::add);

        return list;
    }

    @Override
    public Invoice getInvoice(String id) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Invoice invoice = session.get(Invoice.class, id);
        session.getTransaction().commit();

        return invoice;
    }

    @Override
    public void saveInvoice(Invoice invoice) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(invoice);
        session.getTransaction().commit();
    }

    @Override
    public void deleteCategory(Invoice invoice) {

        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Invoice i = session.get(Invoice.class, invoice.getId());
        session.delete(i);
        session.getTransaction().commit();
    }
    
//    public ObservableList<Sale> getInvoiceSales(String id) {
//
//
//        ObservableList<Sale> list = FXCollections.observableArrayList();
//
//        session = HibernateUtil.getSessionFactory().getCurrentSession();
//        session.beginTransaction();
//
//        Query query = session.createQuery("from Sale where invoiceID=:id");
//        query.setParameter("id", id);
//        query.setResultTransformer(Transformers.aliasToBean(Sale.class));
//        list=query.list();
//        session.beginTransaction().commit();
//        return list;
//    }

}
