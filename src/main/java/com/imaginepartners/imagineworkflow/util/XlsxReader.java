package com.imaginepartners.imagineworkflow.util;

import com.imaginepartners.imagineworkflow.services.BusinessService;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;
import javax.xml.bind.JAXBElement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.DocPropsExtendedPart;
import org.docx4j.openpackaging.parts.JaxbXmlPart;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.SpreadsheetML.SharedStrings;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.samples.PartsList;
import org.xlsx4j.sml.*;
import org.docx4j.openpackaging.packages.OpcPackage;


public class XlsxReader {
	private Logger log = Logger.getLogger(PartsList.class);

	private List<WorksheetPart> worksheets = new ArrayList<WorksheetPart>();

	private SharedStrings sharedStrings = null;

	private OpcPackage xlsxPkg;

	public XlsxReader(String filePath) throws Docx4JException {
		xlsxPkg = OpcPackage.load(new File(filePath));
	}

	public XlsxReader(InputStream inputStreamXlsx) throws Docx4JException {
		xlsxPkg = OpcPackage.load(inputStreamXlsx);
	}

	public List<Object> saveXlxs(String entitieName, List columns, BusinessService businessService) throws Docx4JException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InstantiationException {

		List<Object> result = new ArrayList<Object>();
		try {
			// Open a document from the file system
			// 1. Load the Package - .docx or Flat OPC .xml
			// List the parts by walking the rels tree
			Class entityClass = Class.forName(entitieName);
			RelationshipsPart rp = xlsxPkg.getRelationshipsPart();
			DocPropsExtendedPart docPropsExtendedPart = new DocPropsExtendedPart(rp.getPartName());
			docPropsExtendedPart.getPartName().getName();
			StringBuilder sb = new StringBuilder();
			printInfo(rp, sb, "");
			traverseRelationships(xlsxPkg, rp, sb, "    ");
			List<String> cells = new ArrayList<String>();
			HashMap<Long, List<String>> rowdata = new HashMap<Long, List<String>>();
			Long rowNumber = null;
			// Now lets print the cell content
			// for (WorksheetPart sheet : worksheets) {
			WorksheetPart sheet = worksheets.get(0);
			Worksheet ws = sheet.getContents();
			Object entity;
			org.xlsx4j.sml.SheetData data = ws.getSheetData();
			int i;
			Field f = null;
			data.getRow().remove(0);
			for (Row r : data.getRow()) {
				entity = entityClass.newInstance();
				i = 0;
				System.out.println("row " + r.getR());
				rowNumber = r.getR();
				for (Cell c : r.getC()) {
					String valuecell;
					if (c.getT().equals(STCellType.S)) {
						valuecell = sharedStrings.getContents().getSi().get(Integer.parseInt(c.getV())).getT().getValue();
					} else {
						// TODO: handle other cell types
						valuecell = c.getV();
					}

					if(i >= columns.size()) break;
					f = entityClass.getDeclaredField((String) columns.get(i));
					f.setAccessible(true);
					f.set(entity, valuecell);
					i++;
				}
				result.add(entity);
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(XlsxReader.class.getName()).log(Level.SEVERE, "Entity not found: " + entitieName, ex);
		}
		return result;
	}

	public void printInfo(Part p, StringBuilder sb, String indent) throws Docx4JException {
		sb.append("\n" + indent + "Part " + p.getPartName() + " [" + p.getClass().getName() + "] ");
		if (p instanceof JaxbXmlPart) {
			Object o = ((JaxbXmlPart) p).getContents();
			if (o instanceof javax.xml.bind.JAXBElement) {
				sb.append(" containing JaxbElement:" + XmlUtils.JAXBElementDebug((JAXBElement) o));
			} else {
				sb.append(" containing JaxbElement:" + o.getClass().getName());
			}
		}
		if (p instanceof WorksheetPart) {
			worksheets.add((WorksheetPart) p);
		} else if (p instanceof SharedStrings) {
			sharedStrings = (SharedStrings) p;
		}
	}
	public HashMap<Part, Part> handled = new HashMap<Part, Part>();

	public void traverseRelationships(org.docx4j.openpackaging.packages.OpcPackage wordMLPackage,
									  RelationshipsPart rp,
									  StringBuilder sb, String indent) throws Docx4JException {
		// TODO: order by rel id
		for (Relationship r : rp.getRelationships().getRelationship()) {
			if (r.getTargetMode() != null
			&& r.getTargetMode().equals("External")) {
				sb.append("\n" + indent + "external resource " + r.getTarget()
				+ " of type " + r.getType());
				continue;
			}
			Part part = rp.getPart(r);
			printInfo(part, sb, indent);
			if (handled.get(part) != null) {
				sb.append(" [additional reference] ");
				continue;
			}
			handled.put(part, part);
			if (part.getRelationshipsPart() != null) {
				traverseRelationships(wordMLPackage, part.getRelationshipsPart(), sb, indent + "    ");
			}
		}
	}

	/**
	 * Method read and parse all xlsx Document into a three level LinkedHashMap that contains:
	 * 	- LinkedHashMap of all Document's Sheets
	 * 	- each sheet Map contains:
	 * 		- LinkedHashMap of rows
	 * 		- each row contain a LinkedHashMap of cells with cell key and cell value
	 * @author medKhelifi
	 * @return Map<String, Map<String,Map<String,String>>> allSheets
	 * @throws Docx4JException
	 */
	public Map<String, Map<String,Map<String,String>>> getXlsxContent() throws Docx4JException {
		// List the parts by walking the rels tree
		RelationshipsPart rp 	= xlsxPkg.getRelationshipsPart();
		StringBuilder sb 		= new StringBuilder();
		traverseRelationships(xlsxPkg, rp, sb, "    ");
		Map<String,Map<String,Map<String,String>>> allSheets	= new LinkedHashMap<String,Map<String,Map<String,String>>>();
		Map<String,Map<String,String>> allSheetContent			= new LinkedHashMap<String,Map<String,String>>();
		// Now lets print the cell content
		int i = 0;
		for(WorksheetPart sheet: worksheets) {
			i++;
			Worksheet ws 	= sheet.getJaxbElement();
			SheetData data 	= ws.getSheetData();
			int rowNbr		=0;
			for (Row r : data.getRow() ) {
				Map<String,String> sheetContent							= new LinkedHashMap<String,String>();
				for (Cell c : r.getC() ) {
					if (c.getT().equals(STCellType.S)) {
						CTXstringWhitespace lineContent = sharedStrings.getJaxbElement().getSi().get(Integer.parseInt(c.getV())).getT();
						sheetContent.put(c.getR(),lineContent.getValue());
					} else {
						// TODO: handle other cell types
						sheetContent.put(c.getR(),c.getV());
					}
				}
				allSheetContent.put(String.valueOf(rowNbr),sheetContent);
				rowNbr++;
			}
			allSheets.put("Sheet_"+i,allSheetContent);
		}
		return allSheets;
	}
}
