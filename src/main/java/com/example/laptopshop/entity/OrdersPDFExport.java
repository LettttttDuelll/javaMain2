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

public class OrdersPDFExport {
    private List<Order> listOrders;
    private Font fontRegular;
    private Font fontTitle;
    private Font fontHeader;

    public OrdersPDFExport(List<Order> listOrders) {
        this.listOrders = listOrders;
        loadFonts(); // Khởi tạo font ở đây
    }

    private void loadFonts() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/arial.ttf");
            assert is != null;
            BaseFont baseFont = BaseFont.createFont("arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, is.readAllBytes(), null);

            fontRegular = new Font(baseFont, 12);
            fontHeader = new Font(baseFont, 12, Font.BOLD, Color.WHITE);
            fontTitle = new Font(baseFont, 18, Font.BOLD, Color.BLUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Paragraph title = new Paragraph("Danh sách đơn hàng", fontTitle);
        title.setAlignment("center");
        document.add(title);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[]{1f, 3.5f, 3f, 3f, 2f, 2f, 3f});

        writeTableHeader(table);
        writerTableData(table);

        document.add(table);
        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        cell.setPhrase(new Phrase("ID", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Tên khách hàng", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Ngày đặt", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Tổng tiền", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Trạng thái", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Địa chỉ", fontHeader));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Số điện thoại", fontHeader));
        table.addCell(cell);
    }

    private void writerTableData(PdfPTable table) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        Collections.reverse(listOrders);
        for (Order order : listOrders) {
            table.addCell(new Phrase(String.valueOf(order.getId()), fontRegular));
            table.addCell(new Phrase(order.getUser().getFullName(), fontRegular));
            table.addCell(new Phrase(order.getOrderDate().format(formatter),fontRegular));
            table.addCell(new Phrase(numberFormat.format(order.getTotalPrice()), fontRegular));
            table.addCell(new Phrase(order.getStatus(), fontRegular));
            table.addCell(new Phrase(order.getAddress(), fontRegular));
            table.addCell(new Phrase(order.getPhone(), fontRegular));
        }
    }
}
