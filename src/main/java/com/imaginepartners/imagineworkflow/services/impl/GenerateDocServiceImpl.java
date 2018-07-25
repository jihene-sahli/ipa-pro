package com.imaginepartners.imagineworkflow.services.impl;

import com.imaginepartners.imagineworkflow.services.GenerateDocService;
import com.imaginepartners.imagineworkflow.util.AppConstants;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.jaxb.Context;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.samples.AbstractSample;
import org.docx4j.wml.*;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 31/01/2017.
 */
@Service
public class GenerateDocServiceImpl extends AbstractSample implements GenerateDocService, Serializable {

	private WordprocessingMLPackage wordMLPackage =null;
    private OutputStream os;
	private HTMLSettings htmlSettings;
    private  boolean save = true;
   // private  String regex = null;
	@Override
	public void loadDoc(String inputfilepath) throws Exception {

		wordMLPackage = Docx4J.load(new File(inputfilepath));

		// HTML exporter setup (required)
		// .. the HTMLSettings object
		 htmlSettings = Docx4J.createHTMLSettings();

		htmlSettings.setImageDirPath(inputfilepath + "_files");
		htmlSettings.setImageTargetUri(inputfilepath.substring(inputfilepath.lastIndexOf("/") + 1)
		+ "_files");
		htmlSettings.setWmlPackage(wordMLPackage);
        //CSS reset
		String userCSS = "html, body, div, span, h1, h2, h3, h4, h5, h6, p, a, img,  ol, ul, li, table, caption, tbody, tfoot, thead, tr, th, td " +
		"{ margin: 0; padding: 0; border: 0;}" +
		"body {line-height: 1;} ";
		htmlSettings.setUserCSS(userCSS);
		SdtWriter.registerTagHandler("HTML_ELEMENT", new SdtToListSdtTagHandler());
		// output to an OutputStream.
		if (save) {
			os = new FileOutputStream(inputfilepath + ".html");
		} else {
			os = new ByteArrayOutputStream();
		}
        // If you want XHTML output
		Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);
		Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

		// Clean up, so any ObfuscatedFontPart temp files can be deleted
		if (wordMLPackage.getMainDocumentPart().getFontTablePart() != null) {
			wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
		}
		// This would also do it, via finalize() methods
		htmlSettings = null;
		wordMLPackage = null;

	}

	@Override
	public String docxToPdf(InputStream inputfilepath,String fileName) throws Exception {
		String regex = null;
		regex = ".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
		PhysicalFonts.setRegex(regex);
		wordMLPackage = WordprocessingMLPackage.load(inputfilepath);
// Refresh the values of DOCPROPERTY fields
		FieldUpdater updater = new FieldUpdater(wordMLPackage);
		updater.update(true);
		String outputfilepath;
		outputfilepath = AppConstants.REPORTS_PDFS_FOLDER + fileName.replace("docx", "pdf");
// All methods write to an output stream
		os = new java.io.FileOutputStream(outputfilepath);
		if (!Docx4J.pdfViaFO()) {
			// Since 3.3.0, Plutext's PDF Converter is used by default
			Docx4J.toPDF(wordMLPackage, os);
		}
		// Set up font mapper (optional)
		Mapper fontMapper = new IdentityPlusMapper();
		wordMLPackage.setFontMapper(fontMapper);
		PhysicalFont font = PhysicalFonts.get("Arial Unicode MS");
		// This would also do it, via finalize() methods
		updater = null;
		wordMLPackage = null;
		return outputfilepath;
	}

	@Override
	public void addIdsToDocx(String inputfilepath, String toFind, String replacer) throws Exception {

		wordMLPackage = WordprocessingMLPackage.load(new File(inputfilepath));

		List<Object> paragraphs = getAllElementFromObject(wordMLPackage.getMainDocumentPart(), P.class);
		for(Object par : paragraphs){
			P p = (P) par;
			List<Object> texts = getAllElementFromObject(p, Text.class);
			for(Object text : texts){
				Text t = (Text)text;
				if(t.getValue().contains(toFind)){
					t.setValue(t.getValue().replace(toFind, replacer));
				}
			}
		}
		wordMLPackage.save(new File(inputfilepath));


	}

	@Override
	public void addImageToDocx(String inputfilepath, String imagePath, String toFind) throws Exception {
		wordMLPackage = WordprocessingMLPackage.load(new File(inputfilepath));
		File file = new File(imagePath);
		byte[] bytes = convertImageToByteArray(file);
		addImageToPackage(wordMLPackage, bytes,toFind);
		wordMLPackage.save(new File(inputfilepath));

	}

	@Override
	public List<String> getPlaceholderImageList(String inputfilepath) throws Exception {
      List <String> placeholders = new ArrayList<String>();
		wordMLPackage = WordprocessingMLPackage.load(new File(inputfilepath));

		List elemetns = getAllElementFromObject(wordMLPackage.getMainDocumentPart(), Tbl.class);

		for(Object obj : elemetns){
			if(obj instanceof Tbl){
				Tbl table = (Tbl) obj;
				List rows = getAllElementFromObject(table, Tr.class);
				for(Object trObj : rows){
					Tr tr = (Tr) trObj;
					List cols = getAllElementFromObject(tr, Tc.class);
					for(Object tcObj : cols){
						Tc tc = (Tc) tcObj;
						List texts = getAllElementFromObject(tc, Text.class);
						for(Object textObj : texts){
							Text text = (Text) textObj;
							if(text.getValue().toLowerCase().contains("image")){
								placeholders.add(text.getValue());
							}
						}
					}
				}
			}
		}

    return placeholders;
	}

	private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}
		}
		return result;
	}

	private static byte[] convertImageToByteArray(File file)
	throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		// You cannot create an array using a long, it needs to be an int.
		if (length > Integer.MAX_VALUE) {
			System.out.println("File too large!!");
		}
		byte[] bytes = new byte[(int)length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length  && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}
		// Ensure all the bytes have been read
		if (offset < bytes.length) {
			System.out.println("Could not completely read file "
			+file.getName());
		}
		is.close();
		return bytes;
	}

	private static void addImageToPackage(WordprocessingMLPackage wordMLPackage,
										  byte[] bytes, String toFind) throws Exception {
		BinaryPartAbstractImage imagePart =
		BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);
		String filenameHint = null;
		String altText = null;
		int docPrId = 1;
		int cNvPrId = 2;

		List elemetns = getAllElementFromObject(wordMLPackage.getMainDocumentPart(), Tbl.class);

		for(Object obj : elemetns){
			if(obj instanceof Tbl){
				Tbl table = (Tbl) obj;
				List rows = getAllElementFromObject(table, Tr.class);
				for(Object trObj : rows){
					Tr tr = (Tr) trObj;
					List cols = getAllElementFromObject(tr, Tc.class);
					for(Object tcObj : cols){
						Tc tc = (Tc) tcObj;
						List texts = getAllElementFromObject(tc, Text.class);
						for(Object textObj : texts){
							Text text = (Text) textObj;
							if(text.getValue().equalsIgnoreCase(toFind)){
								P paragraph = addInlineImageToParagraph( wordMLPackage, bytes,
								filenameHint, altText,
								docPrId, cNvPrId, 5000 );
								tc.getContent().remove(0);

								tc.getContent().add(paragraph);
							}
						}
					}
				}
			}
		}

	}

	private static P addInlineImageToParagraph( WordprocessingMLPackage wordMLPackage,
												byte[] bytes,
												String filenameHint, String altText,
												int id1, int id2, long cx) throws Exception {

		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

		Inline inline = imagePart.createImageInline( filenameHint, altText,
		id1, id2, cx,false);

		// Now add the inline in w:p/w:r/w:drawing
		org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
		org.docx4j.wml.P  p = factory.createP();
		Br br = factory.createBr();
		p.getContent().add(br);
		org.docx4j.wml.R  run = factory.createR();
		p.getContent().add(run);
		org.docx4j.wml.Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);

		return p;

	}
	public WordprocessingMLPackage getWordMLPackage() {
		return wordMLPackage;
	}

	public void setWordMLPackage(WordprocessingMLPackage wordMLPackage) {
		this.wordMLPackage = wordMLPackage;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}

	public HTMLSettings getHtmlSettings() {
		return htmlSettings;
	}

	public void setHtmlSettings(HTMLSettings htmlSettings) {
		this.htmlSettings = htmlSettings;
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;
	}

	/*public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}*/
}
