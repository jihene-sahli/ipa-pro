/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;

/**
 *
 * @author ibrahimhammani
 */
public class IwMultiLine implements Serializable {

    private transient static final long serialVersionUID = 1L;

    private transient List<String> columnsNames;
    private transient List<String> rowsNames;
    private transient List<String> columnsTypes;
    private transient List<String> subRows;
    private transient List<String> subColumns;
    private transient List<Integer> subRowsNbrs;
    private transient List<Integer> subColumnsNbrs;
    private transient List<Integer> widthSubColumns;

    private List<IwRow> rows;

    public IwMultiLine(List<String> columnsNames, List<String> rowsNames, List<String> columnsTypes, List<String> subRows, List<String> subColumns, List<Integer> subRowsNbrs, List<Integer> subColumnsNbrs, List<Integer> widthSubColumns) {

        this.columnsNames = columnsNames;
        this.rowsNames = rowsNames;
        this.columnsTypes = columnsTypes;
        this.subRows = subRows;
        this.subColumns = subColumns;
        this.subRowsNbrs = subRowsNbrs;
        this.subColumnsNbrs = subColumnsNbrs;
        this.widthSubColumns = widthSubColumns;
        init();
    }

    public IwMultiLine(ArrayList<LinkedHashMap<String, Object>> multilinecols, ArrayList<LinkedHashMap<String, Object>> multilinerows, List<String> headerColsSize) {

        columnsNames = new ArrayList<String>();
        subColumns = new ArrayList<String>();
        columnsTypes = new ArrayList<String>();
        subColumnsNbrs = new ArrayList<Integer>();
        widthSubColumns = new ArrayList<Integer>();

        for (String colSize : headerColsSize) {
            if (StringUtils.isNotBlank(colSize)) {
                widthSubColumns.add(Integer.valueOf(colSize));
            } else {
                widthSubColumns.add(30);
            }
        }
        rowsNames = new ArrayList<String>();
        subRows = new ArrayList<String>();
        subRowsNbrs = new ArrayList<Integer>();

        for (LinkedHashMap<String, Object> col : multilinecols) {
            columnsNames.add((String) col.get("title"));
            for (LinkedHashMap<String, Object> subCol : (ArrayList<LinkedHashMap<String, Object>>) col.get("nodes")) {
                subColumns.add((String) subCol.get("title"));
                columnsTypes.add((String) subCol.get("input"));
                if (StringUtils.isNotBlank((String) subCol.get("size"))) {
                    widthSubColumns.add(Integer.valueOf((String) subCol.get("size")));
                } else {
                    widthSubColumns.add(30);
                }
            }
            subColumnsNbrs.add(((ArrayList<LinkedHashMap<String, Object>>) col.get("nodes")).size());
        }

        for (LinkedHashMap<String, Object> row : multilinerows) {

            rowsNames.add((String) row.get("title"));

            for (LinkedHashMap<String, Object> subRow : (ArrayList<LinkedHashMap<String, Object>>) row.get("nodes")) {
                subRows.add((String) subRow.get("title"));
            }
            subRowsNbrs.add(((ArrayList<LinkedHashMap<String, Object>>) row.get("nodes")).size());
        }

        init();
    }

    public List<String> getColumnsNames() {
        return columnsNames;
    }

    public void setColumnsNames(List<String> columnsNames) {
        this.columnsNames = columnsNames;
    }

    public List<String> getRowsNames() {
        return rowsNames;
    }

    public void setRowsNames(List<String> rowsNames) {
        this.rowsNames = rowsNames;
    }

    public List<String> getColumnsTypes() {
        return columnsTypes;
    }

    public void setColumnsTypes(List<String> columnsTypes) {
        this.columnsTypes = columnsTypes;
    }

    public List<String> getSubRows() {
        return subRows;
    }

    public void setSubRows(List<String> subRows) {
        this.subRows = subRows;
    }

    public List<String> getSubColumns() {
        return subColumns;
    }

    public void setSubColumns(List<String> subColumns) {
        this.subColumns = subColumns;
    }

    public List<Integer> getSubRowsNbrs() {
        return subRowsNbrs;
    }

    public void setSubRowsNbrs(List<Integer> subRowsNbrs) {
        this.subRowsNbrs = subRowsNbrs;
    }

    public List<Integer> getSubColumnsNbrs() {
        return subColumnsNbrs;
    }

    public void setSubColumnsNbrs(List<Integer> subColumnsNbrs) {
        this.subColumnsNbrs = subColumnsNbrs;
    }

    public void addRow() {
        IwRow newRow = new IwRow();
        newRow.setIsHeader(Boolean.FALSE);

        List<IwCell> cells = new ArrayList<IwCell>();
        IwCell titleCell = new IwCell(null, 1, 1);
        titleCell.setColType("text");
        cells.add(titleCell);

        if (subRows != null && subRows.size() > 0) {
            IwCell subRowCell = new IwCell(null, 1, 1);
            subRowCell.setColType("text");
            cells.add(subRowCell);
        }
        if (subColumns != null && subColumns.size() > 0) {
            int index = 0;
            for (String col : subColumns) {
                IwCell newCell = new IwCell();
                cells.add(newCell);
                newCell.setColType(getStringValueFromArray(columnsTypes, index));
                index++;
            }
        } else {
            int index = 0;
            for (String col : columnsNames) {
                IwCell newCell = new IwCell();
                cells.add(newCell);
                newCell.setColType(getStringValueFromArray(columnsTypes, index));
                index++;
            }
        }
        newRow.setCells(cells);
        rows.add(newRow);

    }

    public List<IwRow> getRows() {
        LogManager.getLogger(IwMultiLine.class).debug("getRows() !!!!!!");
        return rows;
    }

    public void setRows(List<IwRow> rows) {
        this.rows = rows;
    }

    public void init() {

        rows = new ArrayList<IwRow>();

        //first row w're talking about headers :)
        IwRow header = new IwRow();

        header.setIsHeader(Boolean.TRUE);
        List<IwCell> headerCells = new ArrayList<IwCell>();

        //corner cell 2X2
        IwCell titleCell;
        if (subColumns != null && subColumns.size() > 0) {
            titleCell = new IwCell(null, 2, 2);
        } else {
            titleCell = new IwCell(null, 1, 1);
        }
        titleCell.setColType("corner");
        if (subColumns != null && subColumns.size() > 0) {
            titleCell.setColWidth(getIntValueFromArray(widthSubColumns, 0, 0) + getIntValueFromArray(widthSubColumns, 1, 0));
        } else {
            titleCell.setColWidth(getIntValueFromArray(widthSubColumns, 0));
        }
        headerCells.add(titleCell);

        //Header level 1
        int index = 0;
        int firstColIndex = 0;
        for (String col : columnsNames) {
            IwCell cell = new IwCell();
            cell.setColSpan(getIntValueFromArray(subColumnsNbrs, index));

            int width = 0;
            if (subColumns != null && subColumns.size() > 0) {
                for (int i = firstColIndex, j = firstColIndex + getIntValueFromArray(subColumnsNbrs, index, 0); i < j; i++) {
                    width += getIntValueFromArray(widthSubColumns, firstColIndex + 2, 0);
                }
            } else {
                width = getIntValueFromArray(widthSubColumns, index + 1, 0);
            }
            if (width == 0) {
                width = 50;
            }
            cell.setColWidth(width);

            firstColIndex += getIntValueFromArray(subColumnsNbrs, index, 0);

            cell.setValue(col);
            cell.setColType("header");
            index++;
            headerCells.add(cell);
        }

        header.setCells(headerCells);

        rows.add(header);

        //header level2 (sub header)
        if (subColumns != null && subColumns.size() > 0) {
            IwRow subHeader = new IwRow();

            subHeader.setIsHeader(Boolean.TRUE);
            List<IwCell> subHeaderCells = new ArrayList<IwCell>();

            index = 0;
            for (String col : subColumns) {
                IwCell cell = new IwCell();
                cell.setValue(col);
                cell.setColWidth(getIntValueFromArray(widthSubColumns, index));
                cell.setColType("subHeader");
                subHeaderCells.add(cell);
                index++;

            }
            subHeader.setCells(subHeaderCells);

            rows.add(subHeader);
        }

        index = 0;
        for (String rowName : rowsNames) {

            IwRow row = new IwRow();
            List<IwCell> cells = new ArrayList<IwCell>();
            IwCell cell = new IwCell(null, null, getIntValueFromArray(subRowsNbrs, index));
            cell.setValue(rowName);
            cell.setIsEditable(Boolean.FALSE);
            cell.setColWidth(getIntValueFromArray(widthSubColumns, 0));

            IwCell firstSubCell = new IwCell(null, getStringValueFromArray(subRows, 0));
            firstSubCell.setIsEditable(Boolean.FALSE);
            firstSubCell.setColWidth(getIntValueFromArray(widthSubColumns, 1));
            cells.add(cell);
            cells.add(firstSubCell);

            for (int x = 0; x < subColumns.size(); x++) {
                IwCell contentCell = new IwCell();
                contentCell.setColType(getStringValueFromArray(columnsTypes, x));
                contentCell.setColWidth(getIntValueFromArray(widthSubColumns, 0 + 2));
                cells.add(contentCell);
            }

            row.setCells(cells);
            rows.add(row);

            for (int i = 1; i < getIntValueFromArray(subRowsNbrs, index, 0); i++) {

                IwRow subRow = new IwRow();
                IwCell subRowCell = new IwCell(null, getStringValueFromArray(subRows, i));
                subRowCell.setIsEditable(Boolean.FALSE);
                List<IwCell> subRowCells = new ArrayList<IwCell>();

                subRowCells.add(subRowCell);

                for (int j = 0; j < subColumns.size(); j++) {
                    IwCell contentCell = new IwCell();
                    contentCell.setColType(getStringValueFromArray(columnsTypes, j));
                    subRowCells.add(contentCell);
                }
                subRow.setCells(subRowCells);
                rows.add(subRow);
            }
            index++;

        }
        LogManager.getLogger(IwMultiLine.class).debug(rows);

    }

    private String getStringValueFromArray(List<String> list, int position) {
        if (list.size() > position) {
            return list.get(position);
        }
        return "";
    }

    private Integer getIntValueFromArray(List<Integer> list, int position) {
        if (list != null && list.size() > position) {
            return list.get(position);
        }
        return null;
    }

    private Integer getIntValueFromArray(List<Integer> list, int position, int defaultValue) {
        if (list != null && list.size() > position) {
            return list.get(position);
        }
        return defaultValue;
    }

    public List<Integer> getWidthSubColumns() {
        return widthSubColumns;
    }

    public void setWidthSubColumns(List<Integer> widthSubColumns) {
        this.widthSubColumns = widthSubColumns;
    }

}
