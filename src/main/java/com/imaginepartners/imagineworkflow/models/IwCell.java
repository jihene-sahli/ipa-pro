/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;

/**
 *
 * @author ibrahimhammani
 */
public class IwCell implements Serializable {

    private static final long serialVersionUID = 1L;

    private String colType;
    private String value;
    private Integer colSpan;
    private Integer rowSpan;
    private Integer colWidth;

    private Boolean isEditable;

    public IwCell() {

    }

    public IwCell(String colType, Integer colSpan, Integer rowSpan, String value) {
        this.colType = colType;
        this.value = value;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public IwCell(String colType, String value) {
        this.colType = colType;
        this.value = value;
    }

    public IwCell(String colType, Integer colSpan, Integer rowSpan) {
        this.colType = colType;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public IwCell(String colType) {
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Integer getColWidth() {
        return colWidth;
    }

    public void setColWidth(Integer colWidth) {
        this.colWidth = colWidth;
    }

}
