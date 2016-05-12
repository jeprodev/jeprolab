package com.jeprolab.models.document;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import com.jeprolab.models.document.certificate.JeproLabCertificateData;
import com.jeprolab.models.document.certificate.JeproLabCertificateInterface;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;


/**
 *
 * Created by jeprodev on 02/02/2014.
 */
public class JeproLabDocument {
    protected static Font font10, font10b, font12, font12b, font14;
    protected static final String FONT = "resources/fonts/OpenSans-Regular.ttf";
    protected static final String FONT_B = "resources/fonts/OpenSans-Bold.ttf";
    protected static final String ICC = "resources/data/sGRB_CS_profile.icm";

    private static Document invoice_document;
    private static Document certificate_document;
    private static Document ticket_document;
    private static boolean certificateDirectoryCreated = false;

    /**
     *
     */
    public static  void createCertificateDocument(int requestId){
        JeproLabRequestModel request = new JeproLabRequestModel(requestId);
        String outputFileName = JeproLabConfig.appInstallDirectory + "/documents/pdf/cert/REQUEST_" + request.reference + "_CERTIFICATE.pdf" ;
        File certificateFile = new File(outputFileName);

        if(!certificateFile.getParentFile().exists() || !certificateDirectoryCreated){
            certificateDirectoryCreated = certificateFile.getParentFile().mkdirs();
        }

        try {
            /**
             * Create font
             */
            BaseFont baseFont = BaseFont.createFont(); //FONT, BaseFont.WINANSI, BaseFont.EMBEDDED);
            BaseFont baseFontBold = BaseFont.createFont(); //FONT_B, BaseFont.WINANSI, BaseFont.EMBEDDED);

            font10 = new Font(baseFont, 10);
            font10b = new Font(baseFontBold, 10);
            font12 = new Font(baseFont, 12);
            font12b = new Font(baseFontBold, 12);
            font14 = new Font(baseFont, 14);
           // byte[] logoByteArray = IOUtils.toByteArray(JeproLab.class.getResource("../../config/" + JeproLabConfig.certificate_logo));
            //Image certificateLogo = Image.getInstance(JeproLab.class.getResource("../../config/" + JeproLabConfig.certificate_logo).toString());
            String path = "https://avatars0.githubusercontent.com/u/5889165?v=3&s=400";
            Image certificateLogo = Image.getInstance(new URL(path));

            /**
             * Create certificate table
             */
            PdfPTable certificateTable = new PdfPTable(12);
            certificateTable.setWidthPercentage(100);
            PdfPCell logoWrapper = new PdfPCell();
            logoWrapper.setColspan(5);
            logoWrapper.addElement(new Chunk(certificateLogo, 0, 24));

            /**
             * Add Customer address
             */
            JeproLabCustomerModel customer = new JeproLabCustomerModel(request.customer_id);
            JeproLabAddressModel address = JeproLabAddressModel.getAddressByCustomerId(customer.customer_id, true);
            PdfPTable customerAddressWrapper = new PdfPTable(2);
            PdfPCell customerWrapper = new PdfPCell();
            customerWrapper.setBorder(PdfPCell.NO_BORDER);
            customerWrapper.addElement(new Paragraph(JeproLab.getBundle().getString("JEPROLAB_CUSTOMER_LABEL"), font12b));

            //certificateTable.addCell(logoWrapper);
            //certificateTable.addCell(customerWrapper);
            //new Rectangle(216f, 720f), 36f, 72f, 108f, 180f
            certificate_document = new Document();
            PdfWriter certificateWriter = PdfWriter.getInstance(certificate_document, new FileOutputStream(outputFileName));
            certificateWriter.setPdfVersion(PdfWriter.VERSION_1_7);
            certificateWriter.createXmpMetadata();

            for(Integer sampleId : request.samples){
                JeproLabRequestModel.JeproLabSampleModel sample = new JeproLabRequestModel.JeproLabSampleModel(sampleId);
                PdfPCell sampleTitleCell = new PdfPCell();
                sampleTitleCell.setColspan(8);
                sampleTitleCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_SAMPLE_NAME_LABEL") + " : " + sample.designation, font10b));
                PdfPCell sampleLotCell = new PdfPCell();
                sampleLotCell.setColspan(4);
                sampleLotCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_LOT_LABEL") + " : " + sample.lot.toUpperCase(), font12b));
                certificateTable.addCell(sampleTitleCell);
                certificateTable.addCell(sampleLotCell);

                /**
                 * Add sample information
                 */


                /**
                 * Add sample requested analyze and resut
                 */

                /** separator **/
                PdfPCell separator = new PdfPCell();
                separator.setColspan(12);
                separator.addElement(Chunk.NEWLINE);
                separator.setBorder(PdfPCell.NO_BORDER);
                certificateTable.addCell(separator);
            }

            certificate_document.open();
            certificate_document.add(logoWrapper);
            certificate_document.add(certificateTable);
            certificate_document.close();
        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }

    /**
     *
     */
    public static  void createTicketDocument(int sampleId){
        try {
            /**
             * Create font
             */
            BaseFont baseFont = BaseFont.createFont(); //FONT, BaseFont.WINANSI, BaseFont.EMBEDDED);
            BaseFont baseFontBold = BaseFont.createFont(); //FONT_B, BaseFont.WINANSI, BaseFont.EMBEDDED);

            font10 = new Font(baseFont, 10);
            font10b = new Font(baseFontBold, 10);
            font12 = new Font(baseFont, 12);
            font12b = new Font(baseFontBold, 12);
            font14 = new Font(baseFont, 14);
        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }
}