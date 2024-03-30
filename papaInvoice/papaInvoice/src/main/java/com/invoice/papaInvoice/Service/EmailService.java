package com.invoice.papaInvoice.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendInvoiceEmail(String to, String subject, byte[] pdfBytes, String invoiceId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Please find attached your invoice.");

        // Attachment
        helper.addAttachment("Invoice-" + invoiceId + ".pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }

    public void sendQuotationEmail(String to, String subject, byte[] pdfBytes, String quotationId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Please find attached your Quotation.");

        // Attachment
        helper.addAttachment("Quotation-" + quotationId + ".pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }

    public void sendPerformaInvoiceEmail(String to, String subject, byte[] pdfBytes, String invoiceId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Please find attached your Performa invoice.");

        // Attachment
        helper.addAttachment("Invoice-" + invoiceId + ".pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }
}

