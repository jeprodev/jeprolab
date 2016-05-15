package com.jeprolab.models.document.certificate;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jeprolab.JeproLab;
import com.jeprolab.assets.config.JeproLabConfig;
import com.jeprolab.assets.tools.JeproLabTools;
import com.jeprolab.models.JeproLabAddressModel;
import com.jeprolab.models.JeproLabCustomerModel;
import com.jeprolab.models.JeproLabRequestModel;
import com.jeprolab.models.document.JeproLabDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 *
 * Created by jeprodev on 11/05/2016.
 */
public class JeproLabCertificateDocument {
    protected Font font10, font10b, font11, font11b, font14;

    private boolean certificateDirectoryCreated = false;
    private Document certificate_document;
    private int numberOfColumns = 48;
    private PdfPTable certificateTable, sampleTable;
    private BaseFont certificateBaseFont, arialBasefont;

    PdfWriter certificateWriter;

    public void createDocument(int requestId){
        JeproLabRequestModel request = new JeproLabRequestModel(requestId);
        String outputFileName = JeproLabConfig.appInstallDirectory + "/documents/pdf/cert/REQUEST_" + request.reference + "_CERTIFICATE.pdf" ;
        File certificateFile = new File(outputFileName);

        if(!certificateFile.getParentFile().exists() || !certificateDirectoryCreated){
            certificateDirectoryCreated = certificateFile.getParentFile().mkdirs();
        }

        try {
            BaseFont baseFont = BaseFont.createFont(); //FONT, BaseFont.WINANSI, BaseFont.EMBEDDED);
            BaseFont baseFontBold = BaseFont.createFont(); //FONT_B, BaseFont.WINANSI, BaseFont.EMBEDDED);
            certificateBaseFont = BaseFont.createFont(JeproLab.class.getResource("resources/fonts/Calibri.ttf").toString(), BaseFont.WINANSI, true);
            arialBasefont = BaseFont.createFont(JeproLab.class.getResource("resources/fonts/Arial.ttf").toString(), BaseFont.WINANSI, true);

            font10 = new Font(certificateBaseFont, 10);
            font10b = new Font(certificateBaseFont, 10, Font.BOLD);
            font11 = new Font(certificateBaseFont, 11);
            font11b = new Font(certificateBaseFont, 11, Font.BOLD);
            font14 = new Font(certificateBaseFont, 14);

            certificateTable = new PdfPTable(numberOfColumns);
            certificateTable.setWidthPercentage(100);
            createCustomerAddressWrapper(request.customer_id);

            certificate_document = new Document(); //PageSize.A4, 36, 36, 16, 16);
            //Rectangle docRect = PageSize.A4;

            certificateWriter = PdfWriter.getInstance(certificate_document, new FileOutputStream(outputFileName));
            certificateWriter.setPdfVersion(PdfWriter.VERSION_1_7);
            certificateWriter.createXmpMetadata();

            JeproLabCertificateDocumentEvent event = new JeproLabCertificateDocumentEvent(requestId);
            certificateWriter.setBoxSize("certificate", PageSize.A4);
            certificateWriter.setPageEvent(event);

            /**
             * prepare document file
             */
            for(Integer sampleId : request.samples){
                createSampleResult(sampleId);
                /** separator **/
                PdfPCell separator = new PdfPCell();
                separator.setColspan(numberOfColumns);
                separator.addElement(Chunk.NEWLINE);
                separator.setBorder(PdfPCell.NO_BORDER);
                certificateTable.addCell(separator);
            }

            certificate_document.open();
            createObservationAndSignature();
            certificate_document.add(certificateTable);
            /*certificate_document.newPage();
            certificate_document.newPage();
            certificate_document.newPage();*/

            certificate_document.close();
        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }

    private void createSampleResult(int sampleId){
        JeproLabRequestModel.JeproLabSampleModel sample = new JeproLabRequestModel.JeproLabSampleModel(sampleId);
        Font arialFont10 = new Font(arialBasefont, 10);
        Font arialFont10b = new Font(arialBasefont, 10, Font.BOLD);
        sampleTable = new PdfPTable(numberOfColumns);
        sampleTable.setWidthPercentage(100);
        PdfPCell sampleTitleCell = new PdfPCell();
        sampleTitleCell.setColspan(38);
        sampleTitleCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_SAMPLE_NAME_LABEL") + " : " + sample.designation, font10b));
        sampleTitleCell.setPaddingTop(-4f);
        sampleTitleCell.setBorder(PdfPCell.NO_BORDER);
        sampleTitleCell.setBackgroundColor(new BaseColor(218, 218, 218));
        PdfPCell sampleLotCell = new PdfPCell();
        sampleLotCell.setColspan(10);
        sampleLotCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_LOT_LABEL") + " : " + sample.lot.toUpperCase(), font11b));
        sampleLotCell.setPaddingTop(-4f);
        sampleLotCell.setBorder(PdfPCell.NO_BORDER);
        sampleLotCell.setBackgroundColor(new BaseColor(218, 218, 218));
        sampleTable.addCell(sampleTitleCell);
        sampleTable.addCell(sampleLotCell);

        /**
         * Add sample information
         */
        Paragraph dateLabel = new Paragraph( JeproLab.getBundle().getString("JEPROLAB_REMOVAL_DATE_LABEL") + " : ", font10);
        dateLabel.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell sampleInfoCell = new PdfPCell();
        sampleInfoCell.setColspan(13);
        PdfPTable sampleInfoTable = new PdfPTable(7);
        sampleInfoTable.setWidthPercentage(100);
        PdfPCell sampleRemovalDateCellLabel = new PdfPCell();
        sampleRemovalDateCellLabel.setColspan(4);
        sampleRemovalDateCellLabel.addElement(dateLabel);
        sampleRemovalDateCellLabel.setBorder(PdfPCell.NO_BORDER);
        PdfPCell sampleRemovalDateCell = new PdfPCell();
        sampleRemovalDateCell.setColspan(3);
        sampleRemovalDateCell.addElement(new Chunk(JeproLabTools.date("dd-MM-yyy", sample.removal_date), arialFont10b));
        sampleRemovalDateCell.setBorder(PdfPCell.NO_BORDER);
        sampleInfoTable.addCell(sampleRemovalDateCellLabel);
        sampleInfoTable.addCell(sampleRemovalDateCell);

        PdfPCell sampleReceptionCellLabel = new PdfPCell();
        dateLabel = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_RECEIVED_DATE_LABEL") + " : ", font10);
        dateLabel.setAlignment(Element.ALIGN_RIGHT);
        sampleReceptionCellLabel.setColspan(4);
        sampleReceptionCellLabel.setBorder(PdfPCell.NO_BORDER);
        sampleReceptionCellLabel.addElement(dateLabel);
        //sampleReceptionCellLabel.setPadding(-2f);
        sampleInfoTable.addCell(sampleReceptionCellLabel);
        PdfPCell sampleReceptionCell = new PdfPCell();
        sampleReceptionCell.setBorder(PdfPCell.NO_BORDER);
        //sampleReceptionCell.setPadding(-2f);
        sampleReceptionCell.setColspan(3);
        sampleReceptionCell.addElement(new Chunk(JeproLabTools.date("dd-MM-yyyy", sample.received_date), arialFont10b));
        sampleInfoTable.addCell(sampleReceptionCell);

        PdfPCell sampleTestDateCellLabel = new PdfPCell();
        dateLabel = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_TEST_DATE_LABEL") + " : ", font10);
        dateLabel.setAlignment(Element.ALIGN_RIGHT);
        sampleTestDateCellLabel.addElement(dateLabel);
        sampleTestDateCellLabel.setBorder(PdfPCell.NO_BORDER);
        //sampleTestDateCellLabel.setPadding(0f);
        sampleTestDateCellLabel.setColspan(4);
        sampleInfoTable.addCell(sampleTestDateCellLabel);
        PdfPCell sampleTestDateCell = new PdfPCell();
        sampleTestDateCell.addElement(new Chunk( JeproLabTools.date("dd-MM-yyyy", sample.test_date), arialFont10b));
        //sampleTestDateCell.setPadding(0f);
        sampleTestDateCell.setColspan(3);
        sampleTestDateCell.setBorder(PdfPCell.NO_BORDER);
        sampleInfoTable.addCell(sampleTestDateCell);

        sampleInfoCell.addElement(sampleInfoTable);
        sampleInfoCell.setPadding(0f);
        sampleInfoCell.setBorder(PdfPCell.NO_BORDER);
        sampleTable.addCell(sampleInfoCell);

        /**
         * Middle
         */
        PdfPCell sampleMiddleCell = new PdfPCell();
        sampleMiddleCell.setColspan(2);
        sampleMiddleCell.setBorder(PdfPCell.NO_BORDER);
        sampleTable.addCell(sampleMiddleCell);

        createSampleAnalyzeResultBox(sample);
    }

    private void createSampleAnalyzeResultBox(JeproLabRequestModel.JeproLabSampleModel sample){
        /**
         * Add sample requested analyze and result
         */
        PdfPCell sampleResultCell = new PdfPCell();
        sampleResultCell.setColspan(33);
        sampleResultCell.setPadding(0);
        sampleResultCell.setBorder(PdfPCell.NO_BORDER);

        PdfPTable analyzeResultTable = new PdfPTable(48);
        analyzeResultTable.setWidthPercentage(100);
        PdfPCell analyzeResultTitle = new PdfPCell();
        analyzeResultTitle.setColspan(48);
        analyzeResultTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        analyzeResultTitle.setVerticalAlignment(Element.ALIGN_CENTER);
        Paragraph sampleAnalyzesLabel = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_ANALYTIC_RESULTS_LABEL"), font10b);
        sampleAnalyzesLabel.setAlignment(Element.ALIGN_CENTER);
        analyzeResultTitle.setPaddingTop(-2f);
        analyzeResultTitle.setPaddingBottom(2f);
        analyzeResultTitle.addElement(sampleAnalyzesLabel);
        analyzeResultTitle.setBorder(PdfPCell.NO_BORDER);
        analyzeResultTable.addCell(analyzeResultTitle);

        PdfPHeaderCell resultCellLabel = new PdfPHeaderCell();
        resultCellLabel.setColspan(18);
        resultCellLabel.setPaddingTop(-1f);
        resultCellLabel.setPaddingRight(5f);
        resultCellLabel.setPaddingBottom(3f);
        resultCellLabel.setPaddingLeft(8f);
        resultCellLabel.setBackgroundColor(new BaseColor(218, 218, 218));
        resultCellLabel.setBorderWidthTop(0f);
        resultCellLabel.setBorderWidthLeft(0f);
        resultCellLabel.setBorderWidthRight(0f);
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_NAME_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPHeaderCell();
        resultCellLabel.setColspan(6);
        resultCellLabel.setPaddingTop(-1f);
        resultCellLabel.setPaddingRight(5f);
        resultCellLabel.setPaddingBottom(3f);
        resultCellLabel.setPaddingLeft(8f);
        resultCellLabel.setBackgroundColor(new BaseColor(218, 218, 218));
        resultCellLabel.setBorderWidthTop(0f);
        resultCellLabel.setBorderWidthLeft(0f);
        resultCellLabel.setBorderWidthRight(0f);
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_DETECTION_THRESHOLD_ABBREVIATION_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPHeaderCell();
        resultCellLabel.setColspan(5);
        resultCellLabel.setPaddingTop(-1f);
        resultCellLabel.setPaddingRight(5f);
        resultCellLabel.setPaddingBottom(3f);
        resultCellLabel.setPaddingLeft(8f);
        resultCellLabel.setBackgroundColor(new BaseColor(218, 218, 218));
        resultCellLabel.setBorderWidthTop(0f);
        resultCellLabel.setBorderWidthLeft(0f);
        resultCellLabel.setBorderWidthRight(0f);
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_UNIT_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPHeaderCell();
        resultCellLabel.setColspan(8);
        resultCellLabel.setPaddingTop(-1f);
        resultCellLabel.setPaddingRight(5f);
        resultCellLabel.setPaddingBottom(3f);
        resultCellLabel.setPaddingLeft(8f);
        resultCellLabel.setBackgroundColor(new BaseColor(218, 218, 218));
        resultCellLabel.setBorderWidthTop(0f);
        resultCellLabel.setBorderWidthLeft(0f);
        resultCellLabel.setBorderWidthRight(0f);
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_METHOD_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPHeaderCell();
        resultCellLabel.setColspan(11);
        resultCellLabel.setPaddingTop(-1f);
        resultCellLabel.setPaddingRight(5f);
        resultCellLabel.setPaddingBottom(3f);
        resultCellLabel.setPaddingLeft(8f);
        resultCellLabel.setBackgroundColor(new BaseColor(218, 218, 218));
        resultCellLabel.setBorderWidthTop(0f);
        resultCellLabel.setBorderWidthLeft(0f);
        resultCellLabel.setBorderWidthRight(0f);
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_RESULT_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        PdfPCell analyzeNameCell, analyzeUnitCell, analyzeThresholdCell, analyzeMethodCell, analyzeResultCell;
        BaseColor evenColor = new BaseColor(218, 238, 243);
        boolean isEven = false;
        Paragraph cellContent;
        Font arialFont11B = new Font(arialBasefont, 9, Font.BOLD);
        for(Integer analyzeId : sample.analyzes){

            Map<String, String> sampleAnalyze = JeproLabRequestModel.JeproLabSampleModel.getSampleResult(sample.sample_id, analyzeId);
            analyzeNameCell = new PdfPCell();
            analyzeNameCell.setColspan(18);
            analyzeNameCell.setBorder(PdfPCell.NO_BORDER);
            analyzeNameCell.setPaddingTop(-3f);
            analyzeNameCell.setPaddingBottom(2f);
            analyzeNameCell.addElement(new Chunk(sampleAnalyze.get("name"), font10));

            analyzeUnitCell = new PdfPCell();
            analyzeUnitCell.setColspan(6);
            analyzeUnitCell.setPaddingTop(-3f);
            analyzeUnitCell.setPaddingBottom(2f);
            analyzeUnitCell.setBorder(PdfPCell.NO_BORDER);
            cellContent =  new Paragraph(sampleAnalyze.get("unit"), arialFont11B);
            cellContent.setAlignment(Element.ALIGN_CENTER);
            analyzeUnitCell.addElement(cellContent);

            analyzeThresholdCell = new PdfPCell();
            analyzeThresholdCell.setColspan(5);
            analyzeThresholdCell.setBorder(PdfPCell.NO_BORDER);
            analyzeThresholdCell.setPaddingTop(-3f);
            analyzeThresholdCell.setPaddingBottom(2f);
            cellContent = new Paragraph(sampleAnalyze.get("threshold"), font10);
            cellContent.setAlignment(Element.ALIGN_CENTER);
            analyzeThresholdCell.addElement(cellContent);

            analyzeMethodCell = new PdfPCell();
            analyzeMethodCell.setColspan(8);
            analyzeMethodCell.setPaddingTop(-3f);
            analyzeMethodCell.setPaddingBottom(2f);
            analyzeMethodCell.setBorder(PdfPCell.NO_BORDER);
            cellContent = new Paragraph(sampleAnalyze.get("method"), arialFont11B);
            cellContent.setAlignment(Element.ALIGN_CENTER);
            analyzeMethodCell.addElement(cellContent);

            analyzeResultCell = new PdfPCell();
            analyzeResultCell.setColspan(11);
            analyzeResultCell.setPadding(-3f);
            analyzeResultCell.setPaddingBottom(2f);
            analyzeResultCell.setBorder(PdfPCell.NO_BORDER);
            cellContent = new Paragraph(sampleAnalyze.get("result"), arialFont11B);
            cellContent.setAlignment(Element.ALIGN_CENTER);
            analyzeResultCell.addElement(cellContent);

            if(isEven){
                analyzeNameCell.setBackgroundColor(evenColor);
                analyzeUnitCell.setBackgroundColor(evenColor);
                analyzeThresholdCell.setBackgroundColor(evenColor);
                analyzeMethodCell.setBackgroundColor(evenColor);
                analyzeResultCell.setBackgroundColor(evenColor);
            }
            isEven = !isEven;

            analyzeResultTable.addCell(analyzeNameCell);
            analyzeResultTable.addCell(analyzeThresholdCell);
            analyzeResultTable.addCell(analyzeUnitCell);
            analyzeResultTable.addCell(analyzeMethodCell);
            analyzeResultTable.addCell(analyzeResultCell);
        }

        sampleResultCell.addElement(analyzeResultTable);
        sampleTable.addCell(sampleResultCell);
        PdfPCell sampleContent = new PdfPCell();
        sampleContent.setColspan(numberOfColumns);
        sampleContent.addElement(sampleTable);
        sampleContent.setBorder(PdfPCell.NO_BORDER);
        sampleContent.setPadding(0f);
        certificateTable.addCell(sampleContent);
    }

    /**
     *
     * @param customerId main customer id
     */
    private void createCustomerAddressWrapper(int customerId){
        /**
         * Add Customer address
         */
         JeproLabCustomerModel customer = new JeproLabCustomerModel(customerId);
         JeproLabAddressModel address = JeproLabAddressModel.getAddressByCustomerId(customer.customer_id, true);

         Paragraph content;
         PdfPCell cellContent;
         PdfPCell customerWrapper = new PdfPCell();
         customerWrapper.setColspan(22);
         customerWrapper.setBorder(PdfPCell.NO_BORDER);
         //customerWrapper.addElement();

        PdfPTable customerAddressWrapper = new PdfPTable(5);
        customerAddressWrapper.setWidthPercentage(100);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        cellContent.setBorder(PdfPCell.NO_BORDER);
        content = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_CUSTOMER_NAME_LABEL") + " : ", font11b);
        content.setAlignment(Element.ALIGN_RIGHT);
        cellContent.addElement(content);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Paragraph(customer.company, font11b));
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        content  = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_CONTACT_LABEL") + " : ", font11b);
        content.setAlignment(Element.ALIGN_RIGHT);
        cellContent.addElement(content);
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Chunk(customer.firstname + " " + customer.lastname.toUpperCase(), font11b));
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        content = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_ADDRESS_LABEL") + " : ", font11b);
        content.setAlignment(Element.ALIGN_RIGHT);
        cellContent.addElement(content);
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Paragraph(address.address1, font10));
        cellContent.addElement(new Paragraph(address.address2, font10));
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        customerWrapper.addElement(customerAddressWrapper);

        PdfPCell customerSeparatorWrapper = new PdfPCell();
        customerSeparatorWrapper.setColspan(6);
        customerSeparatorWrapper.setBorder(PdfPCell.NO_BORDER);
        customerSeparatorWrapper.addElement(new Chunk(" ", font10));

        customerAddressWrapper = new PdfPTable(5);
        customerAddressWrapper.setWidthPercentage(100);
        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        cellContent.setBorder(PdfPCell.NO_BORDER);
        content = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_SAMPLING_SITE_LABEL") + " : ", font11b);
        content.setAlignment(Element.ALIGN_RIGHT);
        cellContent.addElement(content);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Chunk("hgdfcg"));
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        content = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_BY_LABEL") + " : ", font11b);
        content.setAlignment(Element.ALIGN_RIGHT);
        cellContent.addElement(content);
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Chunk("sdfgwdfg"));
        cellContent.setBorder(PdfPCell.NO_BORDER);
        customerAddressWrapper.addCell(cellContent);

        PdfPCell customerSamplerWrapper = new PdfPCell();
        customerSamplerWrapper.setColspan(20);
        customerSamplerWrapper.setBorder(PdfPCell.NO_BORDER);
        customerSamplerWrapper.setPadding(0f);
        customerSamplerWrapper.addElement(customerAddressWrapper);

        certificateTable.addCell(customerWrapper);
        certificateTable.addCell(customerSeparatorWrapper);
        certificateTable.addCell(customerSamplerWrapper);
        cellContent = new PdfPCell();
        cellContent.setBorder(PdfPCell.NO_BORDER);
        cellContent.setColspan(numberOfColumns);
        cellContent.addElement(Chunk.NEWLINE);
        certificateTable.addCell(cellContent);
    }


    /**
     * Observation and Signature
     */
    private void createObservationAndSignature(){
        PdfPCell contentCell = new PdfPCell();
        contentCell.setColspan(numberOfColumns);
        contentCell.setBorder(PdfPCell.NO_BORDER);
        contentCell.addElement(Chunk.NEWLINE);
        certificateTable.addCell(contentCell);

        contentCell = new PdfPCell();
        contentCell.setColspan(24);
        PdfPTable observationTable = new PdfPTable(1);
        observationTable.setWidthPercentage(100);

        Font observationFont = new Font(certificateBaseFont, 9, Font.NORMAL);
        Font observationFontBold = new Font(certificateBaseFont, 9, Font.BOLD);

        PdfPCell observationCell = new PdfPCell();
        observationCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_OBSERVATIONS_LABEL") + ":", observationFontBold));
        observationCell.setPaddingTop(-4f);
        observationCell.setBackgroundColor(new BaseColor(182, 221, 232));
        observationCell.setBorder(PdfPCell.NO_BORDER);
        observationTable.addCell(observationCell);

        observationCell = new PdfPCell();
        observationCell.addElement(new Chunk(""));
        observationCell.setBorder(PdfPCell.NO_BORDER);
        observationCell.setBackgroundColor(new BaseColor(218, 238, 243));
        observationTable.addCell(observationCell);

        observationCell = new PdfPCell();
        observationCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_DETECTION_THRESHOLD_ABBREVIATION_LABEL") +  " : " +  JeproLab.getBundle().getString("JEPROLAB_DETECTION_THRESHOLD_LABEL"), observationFont));
        observationCell.setBorder(PdfPCell.NO_BORDER);
        observationCell.setPaddingTop(-4f);
        contentCell.addElement(observationTable);
        contentCell.setBorder(PdfPCell.NO_BORDER);
        certificateTable.addCell(contentCell);

        contentCell = new PdfPCell();
        contentCell.setColspan(5);
        contentCell.addElement(new Chunk(" "));
        contentCell.setBorder(PdfPCell.NO_BORDER);
        certificateTable.addCell(contentCell);

        contentCell = new PdfPCell();
        contentCell.setColspan(19);
        contentCell.setPadding(0);
        contentCell.setBorder(PdfPCell.NO_BORDER);
        PdfPTable signatureTable = new PdfPTable(1);
        signatureTable.setWidthPercentage(100);
        PdfPCell signatureContentCell = new PdfPCell();
        signatureContentCell.addElement(new Chunk(" "));
        signatureContentCell.setPaddingTop(-5f);
        signatureContentCell.setBorder(PdfPCell.NO_BORDER);
        signatureTable.addCell(signatureContentCell);

        Font signatureFont = new Font(certificateBaseFont, 9, Font.ITALIC);

        signatureContentCell = new PdfPCell();
        Paragraph signatureName = new Paragraph("Gracienne B. Motingea, microbiologiste, Mcb.A", signatureFont);
        signatureContentCell.setPaddingTop(-6f);
        signatureContentCell.setBorderWidthRight(0f);
        signatureContentCell.setBorderWidthLeft(0f);
        signatureContentCell.setBorderWidthBottom(0f);
        signatureName.setAlignment(Element.ALIGN_CENTER);
        signatureContentCell.addElement(signatureName);
        signatureTable.addCell(signatureContentCell);
        contentCell.addElement(signatureTable);
        certificateTable.addCell(contentCell);
    }

    /**
     *
     */
    protected class JeproLabCertificateDocumentEvent extends PdfPageEventHelper{
        private Image certificateLogo;
        private PdfPCell logoWrapper, certificateReference;
        private PdfPTable headerWrapper;
        private JeproLabRequestModel request;
        private Paragraph pageContent;
        private Font calibriFontBold12, calibriFont6;

        public JeproLabCertificateDocumentEvent(int requestId){
            String path = JeproLab.class.getResource("../../config/" + JeproLabConfig.certificate_logo).getPath();
            request = new JeproLabRequestModel(requestId);
            try {
                BaseFont calibriBaseFont = BaseFont.createFont(JeproLab.class.getResource("resources/fonts/Calibri.ttf").toString(), BaseFont.WINANSI, true);
                calibriFontBold12 = new Font(calibriBaseFont, 12, Font.BOLD);
                calibriFont6 = new Font(calibriBaseFont, 7, Font.NORMAL);
                Font calibriFont6Bold = new Font(calibriBaseFont, 7, Font.BOLD);


                certificateLogo = Image.getInstance(path);
                logoWrapper = new PdfPCell(certificateLogo, true);
                logoWrapper.setBorder(PdfPCell.NO_BORDER);
                logoWrapper.setColspan(5);
                headerWrapper = new PdfPTable(12);
                headerWrapper.setWidthPercentage(100);
                //headerWrapper.setWidths(new int[]{5, 2, 5});

                certificateReference = new PdfPCell();
                certificateReference.setColspan(5);
                certificateReference.setBackgroundColor(new BaseColor(218, 218, 218));
                certificateReference.setBorder(PdfPCell.NO_BORDER);

                PdfPTable referenceLayout = new PdfPTable(48);
                referenceLayout.setWidthPercentage(100);

                PdfPCell cellContent = JeproLabDocument.textAlignedCell(JeproLab.getBundle().getString("JEPROLAB_ANALYZES_CERTIFICATE_LABEL").toUpperCase(), calibriFontBold12);
                cellContent.setColspan(36);
                /**cellContent.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellContent.setHorizontalAlignment(Element.ALIGN_CENTER); */
                Paragraph content = new Paragraph(new Phrase(JeproLab.getBundle().getString("JEPROLAB_ANALYZES_CERTIFICATE_LABEL").toUpperCase(), calibriFontBold12));
                //cellContent.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_ANALYZES_CERTIFICATE_LABEL").toUpperCase(), calibriFontBold12)); */
                content.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(content);
                cellContent.setPaddingTop(-4f);
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                cellContent = new PdfPCell();
                cellContent.setColspan(10);
                content = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_VERSION_LABEL") + "* : ", calibriFont6);
                content.setAlignment(Element.ALIGN_RIGHT);
                cellContent.addElement(content);
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                cellContent = new PdfPCell();
                cellContent.setColspan(2);
                cellContent.addElement(new Chunk(""));
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                cellContent = new PdfPCell();
                cellContent.setColspan(1);
                cellContent.addElement(new Chunk(""));
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                cellContent = new PdfPCell();
                cellContent.setColspan(13);
                cellContent.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_ISSUE_DATE_LABEL"), calibriFont6));
                cellContent.addElement(new Chunk(""));
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                cellContent = new PdfPCell();
                cellContent.setColspan(15);
                content = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_REQUEST_VOUCHER_LABEL"), calibriFont6);
                content.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(content);
                content = new Paragraph();
                content.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(content);
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                cellContent = new PdfPCell();
                cellContent.setColspan(13);
                content = new Paragraph(JeproLab.getBundle().getString("JEPROLAB_PROJECT_ID_LABEL"), calibriFont6);
                content.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(content);
                content = new Paragraph(request.reference, calibriFont6Bold);
                content.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(content);
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                cellContent = new PdfPCell();
                cellContent.setColspan(6);
                content = new Paragraph("#" + JeproLab.getBundle().getString("JEPROLAB_PAGES_LABEL"), calibriFont6);
                content.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(content); //todo
                pageContent = new Paragraph();
                pageContent.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(pageContent);
                cellContent.setBorder(PdfPCell.NO_BORDER);
                referenceLayout.addCell(cellContent);

                certificateReference.addElement(referenceLayout);

                PdfPCell separatorCell = new PdfPCell();
                separatorCell.setColspan(2);
                separatorCell.setBorder(PdfPCell.NO_BORDER);
                headerWrapper.addCell(logoWrapper);
                headerWrapper.addCell(separatorCell);
                headerWrapper.addCell(certificateReference);
                PdfPCell spacer = new PdfPCell();
                spacer.setColspan(12);
                spacer.addElement(Chunk.NEWLINE);
                spacer.setBorder(PdfPCell.NO_BORDER);
                headerWrapper.addCell(spacer);
            } catch (IOException | DocumentException ignored){
                ignored.printStackTrace();
            }
        }

        public void onStartPage(PdfWriter writer, Document document){
            try {
                document.add(headerWrapper);
                pageContent.add(writer.getCurrentPageNumber() + "/" + certificateWriter.getPageNumber());
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        public void onEndPage(PdfWriter writer, Document document){
            Rectangle rectangle = writer.getBoxSize("certificate");

            PdfPTable table = new PdfPTable(numberOfColumns);
            table.setWidthPercentage(100);
            table.setTotalWidth(rectangle.getWidth() - document.leftMargin() - document.rightMargin());
            table.setLockedWidth(true);
            PdfPCell footerCell = new PdfPCell();
            footerCell.setColspan(32);
            footerCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_THIS_CERTIFICATE_MUST_NOT_BE_REPRODUCED_MESSAGE"), font10b));
            footerCell.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_THIS_RESULTS_REEFER_ONLY_TO_SUPPLIED_SAMPLE_FOR_ANALYSES_MESSAGE"), font10b));

            footerCell = new PdfPCell();
            footerCell.setColspan(16);
            footerCell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(footerCell);

            table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
        }
    }
}