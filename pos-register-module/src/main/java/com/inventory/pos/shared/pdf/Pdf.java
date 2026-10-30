package com.inventory.pos.shared.pdf;

import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;

import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Fonts, cells and money formatting shared by the PDFs. The built-in PDF fonts have no naira sign, so amounts print
 * as "NGN 1,234.00".
 */
public final class Pdf {

    public static final Color INK = new Color(0x14, 0x16, 0x3A);
    public static final Color MUTED = new Color(0x6B, 0x71, 0x94);
    public static final Color LINE = new Color(0xE8, 0xEB, 0xF3);
    public static final Color HEADER_FILL = new Color(0xF4, 0xF5, 0xFA);

    public static final Font TITLE = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.NORMAL, INK);
    public static final Font HEADING = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.NORMAL, INK);
    public static final Font BODY = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, INK);
    public static final Font BODY_BOLD = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.NORMAL, INK);
    public static final Font SMALL = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, MUTED);

    private Pdf() {
    }

    public static String money(BigDecimal value) {
        return "NGN " + new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static PdfPCell cell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorderWidth(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderColor(LINE);
        cell.setPadding(6);
        return cell;
    }

    public static PdfPCell headerCell(String text, int alignment) {
        PdfPCell cell = cell(text, HEADING, alignment);
        cell.setBackgroundColor(HEADER_FILL);
        return cell;
    }

    public static PdfPCell plain(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorderWidth(0);
        cell.setPadding(2);
        return cell;
    }

    public static PdfPTable table(float... widths) {
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        return table;
    }

    public static int left() {
        return Element.ALIGN_LEFT;
    }

    public static int right() {
        return Element.ALIGN_RIGHT;
    }
}
