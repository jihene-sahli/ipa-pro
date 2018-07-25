package com.imaginepartners.imagineworkflow.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellAddress;

public class XlsxConverter implements Serializable{

	public static Workbook WORKBOOK;

	public int starting_header_row = 0;

	private InputStream inputStream;

	private String password = "";

	private List<Map<String, Object>> entries= new ArrayList<Map<String, Object>>();

	private List<String> columns;

	/**
	 * Initial default header row number.
	 *
	 * @param startHeaderLineNumber the header row number.
	 */
	public XlsxConverter(int startHeaderLineNumber) {
		this.starting_header_row = startHeaderLineNumber;
	}

	/**
	 * Build a document using workbook.
	 *
	 * @param inputStream providing the inputStream for creating the workbook object.
	 */
	public XlsxConverter(InputStream inputStream)
	throws IOException, InvalidFormatException, EncryptedDocumentException {
		this.inputStream = inputStream;
		LOAD_WORKBOOK(inputStream);
	}

	/**
	 * @param is providing the inputStream for creating the workbook object.
	 * @param startHeaderLineNumber the header row number.
	 */
	public XlsxConverter(InputStream is, int startHeaderLineNumber)
	throws IOException, InvalidFormatException, EncryptedDocumentException {
		this.inputStream = inputStream;
		this.starting_header_row = startHeaderLineNumber;
		LOAD_WORKBOOK(is);
	}

	public XlsxConverter(InputStream is, int startHeaderLineNumber, String filePassword)
	throws IOException, InvalidFormatException, EncryptedDocumentException {
		this.inputStream = inputStream;
		this.starting_header_row = startHeaderLineNumber;
		this.password = filePassword;
		LOAD_WORKBOOK(is, filePassword);
	}

	public XlsxConverter(InputStream is, String filePassword)
	throws IOException, InvalidFormatException, EncryptedDocumentException {
		this.inputStream = inputStream;
		this.password = filePassword;
		LOAD_WORKBOOK(is, filePassword);
	}

	/**
	 * try to create workBook object for given inputStream instance.
	 * @param is InputStream instance
	 */
	public static void LOAD_WORKBOOK(InputStream is)
	throws IOException, InvalidFormatException, EncryptedDocumentException {
		WORKBOOK = WorkbookFactory.create(is);
	}

	public static void LOAD_WORKBOOK(InputStream is, String filePassword)
	throws IOException, InvalidFormatException, EncryptedDocumentException {
		WORKBOOK = WorkbookFactory.create(is, filePassword);
	}

	/**
	 *
	 * @param index number of page.
	 * @return page as a sheet object.
	 */
	public static Sheet getSheetByIndex(int index) {
		if (index > -1 && WORKBOOK != null) {
			if (WORKBOOK.getNumberOfSheets() <= index)
				throw new IndexOutOfBoundsException("index " + index + " out of bound exception. "
				+ "number of sheets in the given document is: " + WORKBOOK.getNumberOfSheets());
			else {
				return WORKBOOK.getSheetAt(index);
			}
		}
		return null;
	}

	/**
	 * @param index number of page.
	 * @return page as a sheet object.
	 */
	public static Sheet getSheetByIndex(int index, InputStream is)
	throws IOException, InvalidFormatException {
		LOAD_WORKBOOK(is);
		if (index > -1) {
			if (WORKBOOK.getNumberOfSheets() <= index)
				throw new IndexOutOfBoundsException("index " + index + " out of bound exception. "
				+ "number of sheets in the given document is: " + WORKBOOK.getNumberOfSheets());
			else {
				return WORKBOOK.getSheetAt(index);
			}
		}
		return null;
	}

	/**
	 * read and write the xlsx document.
	 *
	 * @param is InputStream ref to create the document using apache.POI API.
	 */
	public static void fileToLogs(InputStream is) throws IOException, InvalidFormatException {
		Sheet firstSheet = getSheetByIndex(0, is);
		int rowStart = Math.min(0, firstSheet.getFirstRowNum());
		int rowEnd = Math.max(firstSheet.getLastRowNum(), firstSheet.getLastRowNum());
		for (int rowNumber = rowStart; rowNumber <= rowEnd; rowNumber++) {
			Row row = firstSheet.getRow(rowNumber);
			int lastColumnNumber = Math.max(row.getLastCellNum(), 15);
			for (int columnNumber = 0; columnNumber < lastColumnNumber; columnNumber++) {
				Cell cell = row.getCell(columnNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
				if (cell == null)
					LogManager.getLogger(XlsxConverter.class.getSimpleName()).trace("cell is null");
				else {
					writeLogs(cell);
				}
			}
		}
	}


	/**
	 * read the document as an xlsx document.
	 * establish the class implementation source code when reading the xlsFile.
	 *
	 * @param startFromLine InputStream ref to create the document using apache.POI API.
	 */
	public void readDocument(int startFromLine) {
		Sheet firstSheet = getSheetByIndex(0);
		// Establish the rowStart and rowEnd from sheet
		int rowStart = Math.min(startFromLine, startFromLine);
		int rowEnd = Math.min(firstSheet.getLastRowNum(), firstSheet.getLastRowNum());
		int lastColumnNumber = 0;
		// Iterate over lines, or rows. and replacing the source code of classes.
		for(int rowNumber= rowStart; rowNumber <= rowEnd; rowNumber++){
			Row row= firstSheet.getRow(rowNumber);
			entries.add(new LinkedHashMap<String, Object>());
			lastColumnNumber = Math.max(row.getLastCellNum(), 1);
			for(int columnNumber= 0; columnNumber<lastColumnNumber; columnNumber++){
				Cell cell = row.getCell(columnNumber, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
				if (cell == null) { // cell is null when the content is blank
					entries.get(entries.size()-1).put(getCellContentByCellAddress(new CellAddress(starting_header_row, columnNumber)).toString(),
					new String() );
				} else {
					entries.get(entries.size()-1).put(getCellContentByCellAddress(new CellAddress(starting_header_row, columnNumber)).toString(),
					getCellContentByCellAddress(cell.getAddress()) );
					writeLogs(cell);
				}
			}
		}
		columns= new ArrayList<String>(
		entries.get(starting_header_row).keySet()
		);
		entries.remove(0);
	}

	public static boolean isDate(String str){
		Date date;
		try {
			date = new SimpleDateFormat("dd/MM/yy").parse(str);
			return true;
		} catch (ParseException ex) {
			LogManager.getLogger(XlsxConverter.class.getSimpleName()).debug("cant convert to Date object with given str");
			return false;
		}
	}

	public static Long StringToDate(String str){
		try {
			Date date= new SimpleDateFormat("dd/MM/yy").parse(str);
			return date.getTime();
		} catch (ParseException ex) {
			LogManager.getLogger(XlsxConverter.class.getSimpleName()).warn("cant convert to date", ex);
			return null;
		}
	}

	public static final char extractColumnKey(CellAddress cellAddress) {
		return cellAddress.formatAsString().charAt(0);
	}

	public static Cell getCellByCellAddress(CellAddress cellAddress) {
		Cell cell;
		if (WORKBOOK != null) {
			return getSheetByIndex(0).getRow(cellAddress.getRow()).getCell(cellAddress.getColumn());
		}
		return null;
	}

	/**
	 * get cell by address and sheet number.
	 *
	 * @param cellAddress
	 * @param pageNumber  an integer resolving to page number in the given xlsx file.
	 * @return Cell reference
	 */
	public static Cell getCellByCellAddress(CellAddress cellAddress, int pageNumber) {
		Cell cell;
		if (WORKBOOK != null) {
			return getSheetByIndex(pageNumber).getRow(cellAddress.getRow()).getCell(cellAddress.getColumn());
		}
		return null;
	}

	/**
	 * Write the content of the given column.
	 * @param cell
	 */
	private static void writeLogs(Cell cell){
		switch ( cell.getCellType() ){
			case Cell.CELL_TYPE_STRING:
				LogManager.getLogger(XlsxConverter.class.getSimpleName())
				.trace("cell: " + cell.getAddress().formatAsString() + " equals to: " +cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				LogManager.getLogger(XlsxConverter.class.getSimpleName())
				.trace("cell: " + cell.getAddress().formatAsString() + " equals to: " + cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				LogManager.getLogger(XlsxConverter.class.getSimpleName())
				.trace("cell: " + cell.getAddress().formatAsString() + " equals to: " + cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_BLANK:
				LogManager.getLogger(XlsxConverter.class.getSimpleName())
				.debug("cell: "+cell.getAddress().formatAsString()+" is empty");
				break;
			case Cell.CELL_TYPE_ERROR:
				LogManager.getLogger(XlsxConverter.class.getSimpleName())
				.error("reading cell: "+cell.getAddress().formatAsString() + " caused an error ", new IllegalArgumentException());
				break;
		}
	}

	public static Class getCellContentType(Cell cell) {
		if (cell != null) {
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					return String.class;
				case Cell.CELL_TYPE_NUMERIC:
					return Double.class;
				case Cell.CELL_TYPE_BOOLEAN:
					return Boolean.class;
				case Cell.CELL_TYPE_ERROR:
					return Error.class;
				case Cell.CELL_TYPE_BLANK:
					return null;
				default:
					return null;
			}
		}
		return null;
	}

	public static Object getCellContentByCellAddress(InputStream is, CellAddress cellAddress)
	throws IOException, InvalidFormatException {
		Cell cell = getSheetByIndex(0, is)
		.getRow(cellAddress.getRow())
		.getCell(cellAddress.getColumn(),
		Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
		if(getCellContentType(cell).isAssignableFrom(String.class))
			return cell.getStringCellValue();
		if(getCellContentType(cell).isAssignableFrom(Double.class))
			return cell.getNumericCellValue();
		if(getCellContentType(cell).isAssignableFrom(Boolean.class))
			return cell.getBooleanCellValue();
		if (getCellContentType(cell).isAssignableFrom(Error.class))
			return cell.getErrorCellValue();
		return null;
	}

	public static Object getCellContentByCellAddress(CellAddress cellAddress) {
		Cell cell = getSheetByIndex(0)
		.getRow(cellAddress.getRow())
		.getCell(cellAddress.getColumn(),
		Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
		String str = "";
		if (getCellContentType(cell).isAssignableFrom(String.class))
			return cell.getStringCellValue().replaceAll("(^ )|( $)", "");
		if (getCellContentType(cell).isAssignableFrom(Double.class))
			return cell.getNumericCellValue();
		if (getCellContentType(cell).isAssignableFrom(Boolean.class))
			return cell.getBooleanCellValue();
		if (getCellContentType(cell).isAssignableFrom(Error.class))
			return cell.getErrorCellValue();
		return null;
	}


	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public int getStarting_header_row() {
		return starting_header_row;
	}

	public void setStarting_header_row(int starting_header_row) {
		this.starting_header_row = starting_header_row;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Map<String, Object>> getEntries() {
		return entries;
	}

	public void setEntries(List<Map<String, Object>> entries) {
		this.entries = entries;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
}
