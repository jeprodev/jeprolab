package com.jeprolab.models.document;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.models.JeproLabRequestModel;

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
    public static  void createTicketDocument(int sampleId){
        JeproLabRequestModel.JeproLabSampleModel sample = new JeproLabRequestModel.JeproLabSampleModel(sampleId);
        String outputFileName = JeproLabConfig.appInstallDirectory + "/documents/pdf/SAMPLE_" + sample.reference + "_TICKET.pdf";
        try{
            ticket_document = new Document();
            PdfWriter ticketWriter = PdfWriter.getInstance(ticket_document, new FileOutputStream(outputFileName));
            //ticketWriter.setPdfVersion(PdfWriter.VERSION_1_6);
            ticket_document.open();
            ticket_document.add(new Paragraph("Hello word"));
            ticket_document.close();
        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }
}
