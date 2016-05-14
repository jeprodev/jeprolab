package com.jeprolab.models.document;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabAnalyzeModel;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import com.jeprolab.models.document.certificate.JeproLabCertificateData;
import com.jeprolab.models.document.certificate.JeproLabCertificateDocument;
import com.jeprolab.models.document.certificate.JeproLabCertificateInterface;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;


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

    private static Document ticket_document;


    /**
     *
     */
    public static  void createCertificateDocument(int requestId){
        /*JeproLabRequestModel request = new JeproLabRequestModel(requestId);
        String outputFileName = JeproLabConfig.appInstallDirectory + "/documents/pdf/cert/REQUEST_" + request.reference + "_CERTIFICATE.pdf" ;
        File certificateFile = new File(outputFileName);

        if(!certificateFile.getParentFile().exists() || !certificateDirectoryCreated){
            certificateDirectoryCreated = certificateFile.getParentFile().mkdirs();
        }*/

/*
        try {
            /**
             * Create font
             * /
            BaseFont baseFont = BaseFont.createFont(); //FONT, BaseFont.WINANSI, BaseFont.EMBEDDED);
            BaseFont baseFontBold = BaseFont.createFont(); //FONT_B, BaseFont.WINANSI, BaseFont.EMBEDDED);

            font10 = new Font(baseFont, 10);
            font10b = new Font(baseFontBold, 10);
            font12 = new Font(baseFont, 12);
            font12b = new Font(baseFontBold, 12);
            font14 = new Font(baseFont, 14);
            String path = JeproLab.class.getResource("../../config/" + JeproLabConfig.certificate_logo).getPath();
            //Image certificateLogo = Image.getInstance(JeproLab.class.getResource("../../config/" + JeproLabConfig.certificate_logo).toString());
            //String path = "https://avatars0.githubusercontent.com/u/5889165?v=3&s=400";
            Image certificateLogo = Image.getInstance(path);
            //certificateLogo.setRotation(180f);

            /**
             * Create certificate table
             * /
            PdfPTable certificateTable = new PdfPTable(12);


            //logoWrapper.addElement(new Chunk(certificateLogo, 1, 24));
            //certificateTable.addCell(logoWrapper);

            /**
             * certificate reference
             * /
            PdfPCell headerSeparator = new PdfPCell();
            headerSeparator.setColspan(2);
            certificateTable.addCell(headerSeparator);

            PdfPCell certificateReference = new PdfPCell();
            certificateReference.setColspan(5);
            certificateTable.addCell(certificateReference);




            //certificateTable.addCell(customerWrapper);
            //new Rectangle(216f, 720f), 36f, 72f, 108f, 180f
            certificate_document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter

            for(Integer sampleId : request.samples){






            } * /


        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }*/
        JeproLabCertificateDocument certificateCreator = new JeproLabCertificateDocument();
        certificateCreator.createDocument(requestId);

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

    public static PdfPCell textAlignedCell(String text, Font f){
        return textAlignedCell(text, f, 2f);
    }

    public static PdfPCell textAlignedCell(String text, Font f, float padding){
        Phrase content = new Phrase(text, f);
        float fontSize = content.getFont().getSize();
        float capHeight = content.getFont().getBaseFont().getFontDescriptor(BaseFont.CAPHEIGHT, fontSize);

        PdfPCell cell = new PdfPCell(content);
        //cell.setPadding(padding);
        System.out.println(capHeight -(fontSize + padding));
        //cell.setPaddingTop(capHeight -(fontSize + padding));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    public static void horizontalCenterCellText(String text, Font font, PdfPCell cell){
        float cellWidth = cell.getWidth();
        //Phrase content = new
    }
}