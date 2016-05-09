package com.jeprolab.models.document;

import com.itextpdf.text.DocumentException;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabDocument {
    private static Document invoice_document;
    private static Document certificate_document;
    private static Document ticket_document;

    /**
     *
     */
    public static  void createInvoiceDocument(String outputFileName){
        try{
            invoice_document = new Document();

            PdfWriter.getInstance(invoice_document, new FileOutputStream(outputFileName));
            invoice_document.open();
            invoice_document.close();
        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }

    /**
     *
     */
    public static  void createCertificateDocument(String outputFileName){
        try{
            certificate_document = new Document();

            PdfWriter.getInstance(certificate_document, new FileOutputStream(outputFileName));
            certificate_document.open();
            certificate_document.close();
        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }

    /**
     *
     */
    public static  void createTicketDocument(String outputFileName){
        try{

        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }
}
