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
    protected Font font10, font10b, font12, font12b, font14;

    private boolean certificateDirectoryCreated = false;
    private Document certificate_document;
    private int numberOfColumns = 48;
    private PdfPTable certificateTable;

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

            font10 = new Font(Font.FontFamily.TIMES_ROMAN, 10);
            font10b = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            font12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
            font12b = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            font14 = new Font(Font.FontFamily.TIMES_ROMAN, 14);

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

            certificate_document.add(certificateTable);
            certificate_document.newPage();
            certificate_document.newPage();
            certificate_document.newPage();
            certificate_document.close();
        }catch(DocumentException | IOException ignored){
            ignored.printStackTrace();
        }
    }

    private void createSampleResult(int sampleId){
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
        PdfPCell sampleInfoCell = new PdfPCell();
        sampleInfoCell.setColspan(3);
        PdfPTable sampleInfoTable = new PdfPTable(2);
        sampleInfoTable.setWidthPercentage(100);
        PdfPCell sampleRemovalDateCellLabel = new PdfPCell();
        sampleRemovalDateCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_REMOVAL_DATE_LABEL"), font10));
        PdfPCell sampleRemovalDateCell = new PdfPCell();
        sampleRemovalDateCell.addElement(new Chunk(JeproLabTools.date("dd-MM-yyy", sample.removal_date)));
        sampleInfoTable.addCell(sampleRemovalDateCellLabel);
        sampleInfoTable.addCell(sampleRemovalDateCell);

        PdfPCell sampleReceptionCellLabel = new PdfPCell();
        sampleReceptionCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_RECEIVED_DATE_LABEL"), font10));
        sampleInfoTable.addCell(sampleReceptionCellLabel);
        PdfPCell sampleReceptionCell = new PdfPCell();
        sampleReceptionCell.addElement(new Chunk(JeproLabTools.date("dd-MM-yyyy", sample.received_date)));
        sampleInfoTable.addCell(sampleReceptionCell);

        PdfPCell sampleTestDateCellLabel = new PdfPCell();
        sampleTestDateCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_TEST_DATE_LABEL"), font10));
        sampleInfoTable.addCell(sampleTestDateCellLabel);
        PdfPCell sampleTestDateCell = new PdfPCell();
        sampleTestDateCell.addElement(new Chunk(JeproLabTools.date("dd-MM-yyyy", sample.test_date)));
        sampleInfoTable.addCell(sampleTestDateCell);

        sampleInfoCell.addElement(sampleInfoTable);
        certificateTable.addCell(sampleInfoCell);

        /**
         * Middle
         */
        PdfPCell sampleMiddleCell = new PdfPCell();
        sampleMiddleCell.setColspan(1);
        sampleMiddleCell.setBorder(PdfPCell.NO_BORDER);
        certificateTable.addCell(sampleMiddleCell);

        createSampleAnalyzeResultBox(sample);
    }

    private void createSampleAnalyzeResultBox(JeproLabRequestModel.JeproLabSampleModel sample){
        /**
         * Add sample requested analyze and result
         */
        PdfPCell sampleResultCell = new PdfPCell();
        sampleResultCell.setColspan(8);


        PdfPTable analyzeResultTable = new PdfPTable(5);
        analyzeResultTable.setWidthPercentage(100);
        PdfPCell analyzeResultTitle = new PdfPCell();
        analyzeResultTitle.setColspan(5);
        analyzeResultTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
        analyzeResultTitle.setVerticalAlignment(Element.ALIGN_CENTER);
        analyzeResultTitle.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_ANALYSES_LABEL"), font10b));
        analyzeResultTable.addCell(analyzeResultTitle);
        PdfPCell resultCellLabel = new PdfPCell();
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_NAME_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPCell();
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_UNIT_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPCell();
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_THRESHOLD_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPCell();
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_METHOD_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        resultCellLabel = new PdfPCell();
        resultCellLabel.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_RESULT_LABEL"), font10b));
        analyzeResultTable.addCell(resultCellLabel);

        PdfPCell analyzeNameCell, analyzeUnitCell, analyzeThresholdCell, analyzeMethodCell, analyzeResultCell;
        for(Integer analyzeId : sample.analyzes){

            Map<String, String> sampleAnalyze = JeproLabRequestModel.JeproLabSampleModel.getSampleResult(sample.sample_id, analyzeId);
            analyzeNameCell = new PdfPCell();
            analyzeNameCell.addElement(new Chunk(sampleAnalyze.get("name"), font10));

            analyzeUnitCell = new PdfPCell();
            analyzeUnitCell.addElement(new Chunk(sampleAnalyze.get("unit"), font10));
            analyzeThresholdCell = new PdfPCell();
            analyzeThresholdCell.addElement(new Chunk(sampleAnalyze.get("threshold"), font10));
            analyzeMethodCell = new PdfPCell();
            analyzeMethodCell.addElement(new Chunk(sampleAnalyze.get("method"), font10));
            analyzeResultCell = new PdfPCell();
            analyzeResultCell.addElement(new Chunk(sampleAnalyze.get("result"), font10));

            analyzeResultTable.addCell(analyzeNameCell);
            analyzeResultTable.addCell(analyzeUnitCell);
            analyzeResultTable.addCell(analyzeThresholdCell);
            analyzeResultTable.addCell(analyzeMethodCell);
            analyzeResultTable.addCell(analyzeResultCell);
        }

        sampleResultCell.addElement(analyzeResultTable);
        certificateTable.addCell(sampleResultCell);
    }

    private void createCustomerAddressWrapper(int customerId){
        /**
         * Add Customer address
         */
         JeproLabCustomerModel customer = new JeproLabCustomerModel(customerId);
         JeproLabAddressModel address = JeproLabAddressModel.getAddressByCustomerId(customer.customer_id, true);

         PdfPCell cellContent;
         PdfPCell customerWrapper = new PdfPCell();
         customerWrapper.setColspan(5);
         customerWrapper.setBorder(PdfPCell.NO_BORDER);
         //customerWrapper.addElement();

        PdfPTable customerAddressWrapper = new PdfPTable(5);
        customerAddressWrapper.setWidthPercentage(100);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        cellContent.setBorder(PdfPCell.NO_BORDER);
        cellContent.addElement(new Paragraph(JeproLab.getBundle().getString("JEPROLAB_CUSTOMER_NAME_LABEL") + " : ", font12b));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Paragraph(customer.company, font12b));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        cellContent.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_CONTACT_LABEL") + " : ", font12b));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Chunk(customer.firstname + " " + customer.lastname.toUpperCase(), font12b));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        cellContent.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_ADDRESS_LABEL") + " : ", font12b));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Paragraph(address.address1, font10));
        cellContent.addElement(new Paragraph(address.address2, font10));
        customerAddressWrapper.addCell(cellContent);

        customerWrapper.addElement(customerAddressWrapper);

        PdfPCell customerSeparatorWrapper = new PdfPCell();
        customerSeparatorWrapper.setColspan(2);
        customerSeparatorWrapper.setBorder(PdfPCell.NO_BORDER);
        customerSeparatorWrapper.addElement(new Chunk(" ", font10));

        customerAddressWrapper = new PdfPTable(5);
        customerAddressWrapper.setWidthPercentage(100);
        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        cellContent.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_SAMPLING_SITE_LABEL") + " : ", font12b));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Chunk("hgdfcg"));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(2);
        cellContent.addElement(new Chunk(JeproLab.getBundle().getString("JEPROLAB_BY_LABEL") + " : ", font12b));
        customerAddressWrapper.addCell(cellContent);

        cellContent = new PdfPCell();
        cellContent.setColspan(3);
        cellContent.addElement(new Chunk("sdfgwdfg"));
        customerAddressWrapper.addCell(cellContent);

        PdfPCell customerSamplerWrapper = new PdfPCell();
        customerSamplerWrapper.setColspan(5);
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
     *
     */
    protected class JeproLabCertificateDocumentEvent extends PdfPageEventHelper{
        private Image certificateLogo;
        private PdfPCell logoWrapper, certificateReference;
        private PdfPTable headerWrapper;
        private JeproLabRequestModel request;
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
                cellContent.addElement(content);
                content.setAlignment(Element.ALIGN_CENTER);
                cellContent.addElement(new Chunk());
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
            } catch (IOException | DocumentException ignored) {
                ignored.printStackTrace();
            }
        }

        public void onStartPage(PdfWriter writer, Document document){
            //Rectangle rectangle = writer.getBoxSize("art");
            //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, headerWrapper, rectangle.getLeft(), rectangle.getTop(), 0);
            try {
                document.add(headerWrapper);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        public void onEndPage(PdfWriter writer, Document document){
            Rectangle rectangle = writer.getBoxSize("certificate");
            Phrase footer = new Phrase();
            footer.add(headerWrapper);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, footer, rectangle.getLeft(), rectangle.getBottom(), 0);
        }
    }
}