package com.invoice.papaInvoice.Service;

import com.invoice.papaInvoice.Entity.*;
import com.invoice.papaInvoice.Repositories.InvoiceRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.colors.ColorConstants;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.UnitValue;

import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.VerticalAlignment;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.Random;

@Service
public class PdfService {

    @Autowired
    private InvoiceService invoiceService;

    public ByteArrayInputStream generateQuotationPdf(Quotation quotation) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(20, 20, 20, 20); // Adjust margins (top, right, bottom, left)

        String logoPath = "C:\\Users\\shark\\OneDrive\\Desktop\\Client Images\\LOGP-fotor-bg-remover-2023032231220-2.png";
        ImageData imageData = ImageDataFactory.create(logoPath);
        Image logo = new Image(imageData);

        // Adjust the logo size if needed
        logo.scaleToFit(100, 100); // or any size

        // Calculate position for the logo
        float x = pdf.getDefaultPageSize().getWidth() - logo.getImageScaledWidth() - 20; // 20 is margin
        float y = pdf.getDefaultPageSize().getTop() - logo.getImageScaledHeight() - 20; // Adjust according to your needs

        // Add logo at calculated position
        logo.setFixedPosition(1, x, y);
        document.add(logo);
        // Company Information
        Paragraph companyInfo = new Paragraph("INDITECH PACKAGING\n12/2, HDIL Industrial park,\n" +
                "AN ISO 9001:2015 Company \n" +
                "(Type - III US FDA DMB COMPANY)\n" +
                "Chandansar Rd, Virar East,\n" +
                "Maharashtra 401303\n" +
                "GST NO:-27AAHFI3008H1ZL")
                .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT);
        document.add(companyInfo);

        // Customer Information
        Paragraph customerInfo = new Paragraph(String.format("To,\n%s\n%s\nGSTIN: %s",
                quotation.getCustomer().getName(),
                quotation.getCustomer().getAddress(),
                quotation.getCustomer().getGstNumber()))
                .setFontSize(10).setMarginTop(10);
        document.add(customerInfo);
        document.add(new Paragraph("\n"));
        Random random = new Random();
        String creationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Paragraph creationDateParagraph = new Paragraph("Date: " + creationDate)
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10);
        document.add(creationDateParagraph);

        // Generate a random number, for example between 0 and 999

        int randomNumber = random.nextInt(1000); //
        String randomLetter = ("A" + random.nextInt(26)); //
        document.add(new Paragraph(String.format("Quotation No: %s",quotation.getId()+randomLetter+randomNumber)).setBold().setTextAlignment(TextAlignment.CENTER));


        // Define table column widths
        float[] columnWidths = {1, 3, 2, 2, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100)); // Use 100% of page width

        // Define the header background color
        Color headerBgColor = new DeviceRgb(63, 169, 219); // Light blue background

        int fontSize = 12; // Example of smaller font size
        float padding = 4; // Reduced padding
        // Add headers to the table
        table.addHeaderCell(createStyledCell("S.No", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Cap Size", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Wad Thickness", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Structure", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Rate per 1000", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("MOQ", headerBgColor, true,fontSize, padding));

        // NumberFormat instance for formatting the rate as currency
        Locale locale = new Locale("en", "IN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Iterate over each QuotationItem and add a row to the table
        int sNo = 1;
        for (QuotationItem item : quotation.getItems()) {
            table.addCell(createStyledCell(String.valueOf(sNo++), ColorConstants.WHITE, false,fontSize, padding));
            table.addCell(createStyledCell(item.getCapSize(), ColorConstants.WHITE, false,fontSize, padding));
            table.addCell(createStyledCell(item.getWadThickness(), ColorConstants.WHITE, false,fontSize, padding));
            table.addCell(createStyledCell(item.getStructure(), ColorConstants.WHITE, false,fontSize, padding));
            table.addCell(createStyledCell(currencyFormat.format(item.getRatePer100()), ColorConstants.WHITE, false,fontSize, padding));
            table.addCell(createStyledCell(String.valueOf(item.getMoq()), ColorConstants.WHITE, false,fontSize, padding));
        }

        // Add the table to the document
        document.add(table);

        // Footer - Terms & Conditions
        Paragraph footer = new Paragraph("Terms & Conditions:\n")
                .add("1. Responsibility ceases once goods dispatched to the buyer.\n")
                .add("2. No complain/claim in receipt of the product of the company shall be entertained.\n")
                .add("3. Interest @24% per annum shall be charged if the payment is not made within due date.\n")
                .add("4. All disputes are Subject to Palghar Jurisdiction only.")
                .setFontSize(10).setMarginTop(20);
        document.add(footer);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
    public ByteArrayInputStream generateInvoicePdf(Invoice invoice) throws MalformedURLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(20, 20, 20, 20); // Adjust margins (top, right, bottom, left)

        String logoPath = "C:\\Users\\shark\\OneDrive\\Desktop\\Client Images\\LOGP-fotor-bg-remover-2023032231220-2.png";
        ImageData imageData = ImageDataFactory.create(logoPath);
        Image logo = new Image(imageData);

        // Adjust the logo size if needed
        logo.scaleToFit(100, 100); // or any size

        // Calculate position for the logo
        float x = pdf.getDefaultPageSize().getWidth() - logo.getImageScaledWidth() - 20; // 20 is margin
        float y = pdf.getDefaultPageSize().getTop() - logo.getImageScaledHeight() - 20; // Adjust according to your needs

        // Add logo at calculated position
        logo.setFixedPosition(1, x, y);
        document.add(logo);
        // Company Information
        Paragraph companyInfo = new Paragraph("INDITECH PACKAGING\n12/2, HDIL Industrial park,\n" +
                "AN ISO 9001:2015 Company \n" +
                "(Type - III US FDA DMB COMPANY)\n" +
                "Chandansar Rd, Virar East,\n" +
                "Maharashtra 401303\n" +
                "GST NO:-27AAHFI3008H1ZL")
                .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT);
        document.add(companyInfo);

        // Customer Information
        Paragraph customerInfo = new Paragraph(String.format("To,\n%s\n%s\nGSTIN: %s",
                invoice.getCustomer().getName(),
                invoice.getCustomer().getAddress(),
                invoice.getCustomer().getGstNumber()))
                .setFontSize(12).setMarginTop(10);
        document.add(customerInfo);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        Random random = new Random();
        int randomNumber = random.nextInt(1000); //
        String randomLetter = ("A" + random.nextInt(26)); //
        document.add(new Paragraph(String.format("Invoice No: %s",invoice.getId()+randomLetter+randomNumber)).setBold().setTextAlignment(TextAlignment.CENTER));

        // Invoice Table
        float[] columnWidths = {1, 3, 2, 2, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100)); // Use 100% of page width
        int fontSize = 12; // Example of smaller font size
        float padding = 4; // Reduced padding
        // Header Cells
        Color headerBgColor = new DeviceRgb(63, 169, 219); // Light blue background
        table.addHeaderCell(createStyledCell("Product", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Description", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Quantity (Pcs)", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Rate Per 1000", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("GST", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Amount", headerBgColor, true,fontSize, padding));

        // Mock-up for Invoice Items (replace with actual items logic)
        Locale locale = new Locale("en", "IN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Add a single row for demonstration. Loop through your items instead.
        table.addCell(createStyledCell(invoice.getProductName(),ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(invoice.getDescription(),ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(String.valueOf(invoice.getQuantity()), ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(currencyFormat.format(invoice.getRate()), ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(currencyFormat.format(invoice.getGstRate()), ColorConstants.WHITE, false,fontSize, padding));
        BigDecimal total =invoice.getTotalAmount();
        table.addCell(createStyledCell(currencyFormat.format(total), ColorConstants.WHITE, false,fontSize, padding));

        document.add(table);
        DispatchDetail dispatchDetail = invoice.getDispatchDetail();
        // Assuming dispatchDetail is an instance of DispatchDetail
        LocalDate challanDate = dispatchDetail.getChallanDate();
        LocalDate lrDate =dispatchDetail.getLrDate();
        String formattedChallanDate = challanDate != null ? challanDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Date not available";
        String formattedlrDatee = lrDate != null ? lrDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Date not available";
        String freightString = Optional.ofNullable(dispatchDetail.getFreight())
                .map(BigDecimal::toString)
                .orElse("Not Provided");


        if (dispatchDetail != null) {
            //document.add(new AreaBreak(AreaBreakType.NEXT_AREA));


            document.add(new Paragraph("Dispatch Details").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));

            Table dispatchTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();


            // Sample Dispatch Details
            addDispatchDetailRow(dispatchTable, "Challan No", dispatchDetail.getChallanNo());
            addDispatchDetailRow(dispatchTable, "Challan Date",formattedChallanDate);
            addDispatchDetailRow(dispatchTable, "Party Name", invoice.getCustomer().getName());
            addDispatchDetailRow(dispatchTable, "Place", dispatchDetail.getPlace());
            addDispatchDetailRow(dispatchTable, "PO No", dispatchDetail.getPoNo());
            addDispatchDetailRow(dispatchTable, "Size", dispatchDetail.getSize());
            addDispatchDetailRow(dispatchTable, "No. of Boxes", String.valueOf(dispatchDetail.getBoxNumber()));
            addDispatchDetailRow(dispatchTable, "Dispatch Qty", String.valueOf(dispatchDetail.getDispatchQty()));
            addDispatchDetailRow(dispatchTable, "Transport", dispatchDetail.getTransport());
            addDispatchDetailRow(dispatchTable, "LR No", dispatchDetail.getLrNo());
            addDispatchDetailRow(dispatchTable, "LR Date", formattedlrDatee);
            addDispatchDetailRow(dispatchTable, "Freight", freightString);
            addDispatchDetailRow(dispatchTable, "Delivery Type", dispatchDetail.getDeliveryType());


            document.add(dispatchTable);
        }

        // Footer - Terms & Conditions
        Paragraph footer = new Paragraph("Terms & Conditions:\n")
                .add("1. Responsibility ceases once goods dispatched to the buyer.\n")
                .add("2. No complain/claim in receipt of the product of the company shall be entertained.\n")
                .add("3. Interest @24% per annum shall be charged if the payment is not made within due date.\n")
                .add("4. All disputes are Subject to Palghar Jurisdiction only.")
                .setFontSize(10).setMarginTop(20);
        document.add(footer);

        // Assuming the last row might be incomplete
        int cellsInLastRow = 3; // You'll need to dynamically determine or track this based on your logic
        int totalColumns = 5; // Total number of columns in your table
        for (int i = 0; i < (totalColumns - cellsInLastRow); i++) {
            table.addCell(new Cell().add(new Paragraph(""))); // Adding empty cells to complete the row
        }

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    public ByteArrayInputStream generateProformaInvoicePdf(ProformaInvoice invoice) throws MalformedURLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(20, 20, 20, 20); // Adjust margins (top, right, bottom, left)

        String logoPath = "C:\\Users\\shark\\OneDrive\\Desktop\\Client Images\\LOGP-fotor-bg-remover-2023032231220-2.png";
        ImageData imageData = ImageDataFactory.create(logoPath);
        Image logo = new Image(imageData);

        // Adjust the logo size if needed
        logo.scaleToFit(100, 100); // or any size

        // Calculate position for the logo
        float x = pdf.getDefaultPageSize().getWidth() - logo.getImageScaledWidth() - 20; // 20 is margin
        float y = pdf.getDefaultPageSize().getTop() - logo.getImageScaledHeight() - 20; // Adjust according to your needs

        // Add logo at calculated position
        logo.setFixedPosition(1, x, y);
        document.add(logo);
        // Company Information
        Paragraph companyInfo = new Paragraph("INDITECH PACKAGING\n12/2, HDIL Industrial park,\n" +
                "AN ISO 9001:2015 Company \n" +
                "(Type - III US FDA DMB COMPANY)\n" +
                "Chandansar Rd, Virar East,\n" +
                "Maharashtra 401303\n" +
                "GST NO:-27AAHFI3008H1ZL")
                .setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT);
        document.add(companyInfo);

        // Customer Information
        Paragraph customerInfo = new Paragraph(String.format("To,\n%s\n%s\nGSTIN: %s",
                invoice.getCustomer().getName(),
                invoice.getCustomer().getAddress(),
                invoice.getCustomer().getGstNumber()))
                .setFontSize(12).setMarginTop(10);
        document.add(customerInfo);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        Random random = new Random();
        int randomNumber = random.nextInt(1000); //
        String randomLetter = ("A" + random.nextInt(26)); //
        document.add(new Paragraph(String.format("Invoice No: %s",invoice.getId()+randomLetter+randomNumber)).setBold().setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {1, 3, 2, 2, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100)); // Use 100% of page width
        int fontSize = 12; // Example of smaller font size
        float padding = 4; // Reduced padding

        // Header Cells
        Color headerBgColor = new DeviceRgb(63, 169, 219); // Light blue background
        table.addHeaderCell(createStyledCell("Product", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Description", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Quantity (Pcs)", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("GST", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Rate Per 1000", headerBgColor, true,fontSize, padding));
        table.addHeaderCell(createStyledCell("Amount", headerBgColor, true,fontSize, padding));

        // Mock-up for Invoice Items (replace with actual items logic)
        Locale locale = new Locale("en", "IN");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        // Add a single row for demonstration. Loop through your items instead.
        table.addCell(createStyledCell(invoice.getProductName(),ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(invoice.getDescription(),ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(String.valueOf(invoice.getQuantity()), ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(currencyFormat.format(invoice.getGstRate()), ColorConstants.WHITE, false,fontSize, padding));
        table.addCell(createStyledCell(currencyFormat.format(invoice.getRate()), ColorConstants.WHITE, false,fontSize, padding));
        BigDecimal total =invoice.getTotalAmount();
        table.addCell(createStyledCell(currencyFormat.format(total), ColorConstants.WHITE, false,fontSize, padding));

        document.add(table);


        // Footer - Terms & Conditions
        Paragraph footer = new Paragraph("Terms & Conditions:\n")
                .add("1. Responsibility ceases once goods dispatched to the buyer.\n")
                .add("2. No complain/claim in receipt of the product of the company shall be entertained.\n")
                .add("3. Interest @24% per annum shall be charged if the payment is not made within due date.\n")
                .add("4. All disputes are Subject to Palghar Jurisdiction only.")
                .setFontSize(10).setMarginTop(20);
        document.add(footer);

        // Assuming the last row might be incomplete
        int cellsInLastRow = 3; // You'll need to dynamically determine or track this based on your logic
        int totalColumns = 5; // Total number of columns in your table
        for (int i = 0; i < (totalColumns - cellsInLastRow); i++) {
            table.addCell(new Cell().add(new Paragraph(""))); // Adding empty cells to complete the row
        }

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private Cell createStyledCell(String content, Color bgColor, boolean isBold, int fontSize, float padding) {
        Paragraph p = new Paragraph(content);
        p.setFontSize(fontSize);
        if (isBold) {
            p.setBold();
        }
        Cell cell = new Cell().add(p);
        cell.setBackgroundColor(bgColor);
        cell.setPadding(padding);
        return cell;
    }


    private void addDispatchDetailRow(Table table, String heading, String detail) {
//        Cell titleCell = createStyledCell(title, ColorConstants.WHITE, false);
//        Cell contentCell = createStyledCell(content, ColorConstants.WHITE, false);
        DeviceRgb headingBgColor = new DeviceRgb(216, 227, 231); // Light blue background for headings
        // Ensure detail is not null
        String safeDetail = detail != null ? detail : "Not available";

        Cell headingCell = new Cell().add(new Paragraph(heading))
                .setBackgroundColor(headingBgColor)
                .setBold();
        Cell detailCell = new Cell().add(new Paragraph(safeDetail));

        table.addCell(headingCell);
        table.addCell(detailCell);
    }



}

