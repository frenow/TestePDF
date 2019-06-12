package com.example.testepdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MainActivity extends Activity {
	private Button BtnGerar;
	private BaseFont bfBold;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		BtnGerar = (Button) findViewById(R.id.BtnGerar);
		BtnGerar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BtnGerarClick();
			}
		});
	}

	private void BtnGerarClick() {
		Document doc = new Document();
		try {
			PdfWriter docWriter = PdfWriter.getInstance(doc,
					new FileOutputStream(Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/teste.pdf"));

			doc.open();

			PdfContentByte cb = docWriter.getDirectContent();
			initializeFonts();

			// creating a sample invoice with some customer data
			createHeadings(cb, 400, 780, "Company Name");
			createHeadings(cb, 400, 765, "Address Line 1");
			createHeadings(cb, 400, 750, "Address Line 2");
			createHeadings(cb, 400, 735, "City, State - ZipCode");
			createHeadings(cb, 400, 720, "Country");

			// list all the products sold to the customer
			float[] columnWidths = { 1.5f, 2f, 5f, 2f, 2f };
			// create PDF table with the given widths
			PdfPTable table = new PdfPTable(columnWidths);
			// set table width a percentage of the page width
			table.setTotalWidth(500f);

			PdfPCell cell = new PdfPCell(new Phrase("Qty"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Item Number"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Item Description"));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Price"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Ext Price"));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			table.setHeaderRows(1);

			DecimalFormat df = new DecimalFormat("0.00");
			for (int i = 0; i < 15; i++) {
				double price = Double.valueOf(df.format(Math.random() * 10));
				double extPrice = price * (i + 1);
				table.addCell(String.valueOf(i + 1));
				table.addCell("ITEM" + String.valueOf(i + 1));
				table.addCell("Product Description - SIZE "	+ String.valueOf(i + 1));
				table.addCell(df.format(price));
				table.addCell(df.format(extPrice));
			}

			// absolute location to print the PDF table from
			table.writeSelectedRows(0, -1, doc.leftMargin(), 650, docWriter.getDirectContent());
			

		} catch (Exception e) {
			Log.e("MainActivity", e.toString());
		}
		doc.close();
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/teste.pdf");

		Intent it = new Intent();
		it.setAction(android.content.Intent.ACTION_VIEW);
		it.setDataAndType(Uri.fromFile(file), "application/pdf");
		try {
			startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "PDF não encontrado.", Toast.LENGTH_SHORT).show();
		}
	}

	private void createHeadings(PdfContentByte cb, float x, float y, String text) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 8);
		cb.setTextMatrix(x, y);
		cb.showText(text.trim());
		cb.endText();

	}

	private void initializeFonts(){
	try {
	bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	
	} catch (DocumentException e) {
		e.printStackTrace();
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
