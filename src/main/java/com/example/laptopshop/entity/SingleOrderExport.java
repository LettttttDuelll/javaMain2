package com.example.laptopshop.entity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.awt.Color;
import java.util.Locale;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;


public class SingleOrderExport {
    private Order hoaDon;
    private Font fontRegular;
    private Font fontTitle;
    private Font fontHeader;

    public SingleOrderExport(Order hoaDon) {
        this.hoaDon = hoaDon;
        loadFonts();
    }

    private void loadFonts() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/arial.ttf");
            assert is != null;
            BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, is.readAllBytes(), null);

            // ↓ Giảm font
            fontRegular = new Font(baseFont, 9);
            fontHeader = new Font(baseFont, 9, Font.BOLD, Color.WHITE);
            fontTitle = new Font(baseFont, 13, Font.BOLD, Color.BLUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A6);
        PdfWriter.getInstance(document, response.getOutputStream());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        document.open();

        Paragraph title = new Paragraph("Hóa đơn #" + hoaDon.getId(), fontTitle);
        title.setSpacingAfter(10);
        document.add(title);

        document.add(new Paragraph("Khách Hàng: " + hoaDon.getUser().getFullName(), fontRegular));
        document.add(new Paragraph("Ngày đặt: " + formatter.format(hoaDon.getOrderDate()), fontRegular));
        document.add(new Paragraph("Địa chỉ: " + hoaDon.getAddress(), fontRegular));
        document.add(new Paragraph("SDT: " + hoaDon.getPhone(), fontRegular));

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(8);
        table.setSpacingAfter(6);
        table.setWidths(new float[]{1.3f, 5f, 1.2f, 2.2f, 2.3f}); // Rút gọn

        writeTableHeader(table);
        writeTableData(table);
        document.add(table);

        double tongTien = hoaDon.getOrderItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        String tongTienStr = numberFormat.format(tongTien);

        String qrUrl = "https://img.vietqr.io/image/ICB-101876816062-only-qr.png"
                + "?amount=" + (int) tongTien
                + "&addInfo=DH" + hoaDon.getId();
        //compact2.png
        com.lowagie.text.Image qrImage = com.lowagie.text.Image.getInstance(new java.net.URL(qrUrl));
        qrImage.scaleAbsolute(100, 100); // ↓ nhỏ hơn nữa

        PdfPTable bottomTable = new PdfPTable(2);
        bottomTable.setWidthPercentage(100);
        bottomTable.setWidths(new float[]{5.5f, 3.5f});
        bottomTable.setSpacingBefore(8);

        PdfPCell qrCell = new PdfPCell(qrImage);
        qrCell.setBorder(PdfPCell.NO_BORDER);
        qrCell.setPadding(3);
        bottomTable.addCell(qrCell);

        Paragraph tongTienParagraph = new Paragraph("Tổng tiền: " + tongTienStr + " VND", fontRegular);
        PdfPCell textCell = new PdfPCell(tongTienParagraph);
        textCell.setBorder(PdfPCell.NO_BORDER);
        textCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        bottomTable.addCell(textCell);

        document.add(bottomTable);
        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(3); // ↓ nhỏ hơn
        cell.setPhrase(new Phrase("ID", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Tên", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("SL", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Đơn giá", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Thành tiền", fontHeader));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        List<OrderItem> ds = hoaDon.getOrderItems();
        for (OrderItem item : ds) {
            table.addCell(new Phrase(String.valueOf(item.getId()), fontRegular));
            table.addCell(new Phrase(item.getLaptop().getName(), fontRegular));
            table.addCell(new Phrase(String.valueOf(item.getQuantity()), fontRegular));
            table.addCell(new Phrase(numberFormat.format(item.getPrice()), fontRegular));
            double thanhTien = item.getQuantity() * item.getPrice();
            table.addCell(new Phrase(numberFormat.format(thanhTien), fontRegular));
        }
    }
}
