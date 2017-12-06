package com.boces.black_stanton_boces.report;

import android.os.Environment;

import com.boces.black_stanton_boces.persistence.PersistenceInteractor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class ReportGenerator {
    public static void taskReport(PersistenceInteractor persistence, Date startDate, Date endDate) {
        Document document = new Document();


        File tempFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "HelloWorld.pdf");

        try {
            PdfWriter.getInstance(document, new FileOutputStream(tempFile));
            document.open();

            PdfPTable table = new PdfPTable(3);
            table.addCell(new PdfPCell(new Paragraph("Cell 1")));
            table.addCell(new PdfPCell(new Paragraph("Cell 2")));
            table.addCell(new PdfPCell(new Paragraph("Cell 3")));

            document.add(table);
            document.close();
        } catch (Exception ignored) {
            int i = 1; // noop
        }
    }
}
