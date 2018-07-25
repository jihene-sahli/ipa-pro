package com.imaginepartners.imagineworkflow.controller;

import com.imaginepartners.imagineworkflow.models.IwFile;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.*;


/**
 * this class is used to get images, documents
 * as a refactoring of the way we grab those streamedcontent(pdf,png..) and decouple it from other beans
 *
 * @author {@link "mailto:islam.drissi@imaginepartners.com" "islam drissi"
 * @version 0.1
 * @since 2016-10-04
 */

@Controller
@ApplicationScoped
public class MediaController {

	private static final Logger log = LogManager.getLogger(MediaController.class);

	private IwFile iwFile;

	public MediaController() {
	}

	@PostConstruct
	public void init() {
	}

	/**
	 * return image from iwFile property
	 *
	 * @return
	 * @throws IOException
	 */
	public StreamedContent getImage() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		String path = context.getExternalContext().getRequestParameterMap().get("path");
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			return getStreamedContent(new IwFile(1L, path, 1L, "", ""));
		}
	}

	/**
	 * used by jsp files calling this getter
	 *
	 * @throws IOException
	 * @return StreamedContent
	 */
	public StreamedContent getPreview() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		//log.debug("path: " + context.getExternalContext().getRequestParameterMap().get("iwPath"));
		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {
			return getPdfPreview(iwFile);
		}

	}

	/**
	 * converts given file to pdf and returns Streamedcontent
	 *
	 * @param iwFile
	 * @return StreamedContent
	 */
	StreamedContent getPdfPreview(IwFile iwFile) {
		log.debug("pdfpreview:" + iwFile.getIwMime() + " " + iwFile.getIwPath());

		try {
			switch (iwFile.getIwMime()) {
				case "application/pdf":
					//throw new UnsupportedOperationException();
					InputStream file = null;
					try {
						file = new FileInputStream(iwFile.getIwPath());
					} catch (FileNotFoundException ex) {
						log.error(null, ex);
					}
					if (file == null) {
						return null;
					}
					return new DefaultStreamedContent(file, "application/pdf", iwFile.getIwName());

				case "text/plain":
					return convertTextToPDF(new File(iwFile.getIwPath()));

				case "image/jpeg":
				case "image/png":
					return convertImageToPDF(new File(iwFile.getIwPath()));

				case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
				case "application/msword":
					return convertDocToPDF(new File(iwFile.getIwPath()));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		//TODO: return Default document with 'preview not available'
		return new DefaultStreamedContent(null, "application/pdf", iwFile.getIwName());
	}

	/**
	 * return the file based on iwFile path
	 *
	 * @param iwFile
	 * @return StreamedContent
	 */
	public StreamedContent getStreamedContent(IwFile iwFile) {
		InputStream file = null;
		try {
			file = new FileInputStream(iwFile.getIwPath());
		} catch (FileNotFoundException ex) {
			log.error(null, ex);
		}
		if (file == null) {
			return null;
		}
		return new DefaultStreamedContent(file, iwFile.getIwMime(), iwFile.getIwName());
	}

	/**
	 * converts img file to pdf
	 *
	 * @param imgFile
	 * @return
	 * @throws Exception
	 */
	private StreamedContent convertImageToPDF(File imgFile) throws Exception {
		FileInputStream fis = null;
		DataInputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Document pdfDoc = new Document();
			PdfWriter writer = PdfWriter.getInstance(pdfDoc, baos);

			pdfDoc.open();
			pdfDoc.addTitle("preview");
			pdfDoc.setMarginMirroring(true);
			pdfDoc.setMargins(36, 72, 108, 180);
			pdfDoc.topMargin();

			Image image = Image.getInstance(imgFile.getPath());

			pdfDoc.add(image);

			pdfDoc.close();
			writer.close();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (in != null) {
				in.close();
			}
			if (isr != null) {
				isr.close();
			}
		}
		//TODO: return document with 'preview not available'
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return new DefaultStreamedContent(is, "application/pdf", iwFile.getIwName());
	}

	private StreamedContent convertDocToPDF(File file) throws IOException, Docx4JException {
		//TODO


		//TODO: return document with 'preview not available'
		//InputStream is = new ByteArrayInputStream(bo.toByteArray());
		//return new DefaultStreamedContent(is, "application/pdf", iwFile.getIwName());
		return null;
	}

	/**
	 * delete this method after
	 *
	 * @param file
	 * @param todelete
	 * @return
	 * @throws IOException
	 */
	private StreamedContent convertDocToPDF(File file, Boolean todelete) throws IOException {
		//TODO
		FileInputStream fis = null;
		DataInputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		;
		try {
			Document pdfDoc = new Document();

			PdfWriter writer = PdfWriter.getInstance(pdfDoc, baos);
			pdfDoc.open();
			pdfDoc.addTitle("preview");


			pdfDoc.add(new Paragraph(org.apache.commons.io.FileUtils.readFileToString(file)));

			pdfDoc.close();
			writer.close();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (in != null) {
				in.close();
			}
			if (isr != null) {
				isr.close();
			}
		}
		//TODO: return document with 'preview not available'
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return new DefaultStreamedContent(is, "application/pdf", iwFile.getIwName());
	}

	/**
	 * convert text file to pdf
	 *
	 * @param txtFile
	 * @return
	 * @throws Exception
	 */
	private StreamedContent convertTextToPDF(File txtFile) throws Exception {
		FileInputStream fis = null;
		DataInputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {

			// String output_file = file.getParent() + "//" + "sample.pdf";
			//String newFileName = file.getName().replace(".txt", "");
			//String output_file = file.getParent() + "//" + newFileName + ".pdf";
			Document pdfDoc = new Document();

			PdfWriter writer = PdfWriter.getInstance(pdfDoc, baos);
			pdfDoc.open();
			pdfDoc.addTitle("preview");
			pdfDoc.setMarginMirroring(true);
			pdfDoc.setMargins(36, 72, 108, 180);
			pdfDoc.topMargin();

			BaseFont courier = BaseFont.createFont(BaseFont.COURIER,
			BaseFont.CP1252, BaseFont.EMBEDDED);
			Font myfont = new Font(courier);

			// Font myfont = new Font();
			Font bold_font = new Font();

			bold_font.setStyle(Font.BOLD);
			bold_font.setSize(10);

			myfont.setStyle(Font.NORMAL);
			myfont.setSize(9);

			pdfDoc.add(new Paragraph("\n"));

			if (txtFile.exists()) {
				fis = new FileInputStream(txtFile);
				in = new DataInputStream(fis);
				isr = new InputStreamReader(in);
				br = new BufferedReader(isr);

				String strLine;

				while ((strLine = br.readLine()) != null) {
					Paragraph para = new Paragraph(strLine + "\n", myfont);
					para.setAlignment(Element.ALIGN_JUSTIFIED);

					pdfDoc.add(para);
				}
			} else {
				//TODO: return document with 'file doesn't exist'
				log.error("no file exists!");
				return new DefaultStreamedContent();
			}
			pdfDoc.close();
			writer.close();
		} catch (Exception ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (in != null) {
				in.close();
			}
			if (isr != null) {
				isr.close();
			}
		}
		//TODO: return document with 'preview not available'
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return new DefaultStreamedContent(is, "application/pdf", iwFile.getIwName());
		//return new DefaultStreamedContent();
	}

	@SuppressWarnings("unused")
	public IwFile getIwFile() {
		return iwFile;
	}

	@SuppressWarnings("unused")
	public void setIwFile(IwFile iwFile) {
		this.iwFile = iwFile;
	}
}
