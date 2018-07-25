/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.controller;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author chafik.gouasmia
 */
public class SheetToSave {

    private String sheetName;
    private HashMap<Long, List<String>> rowdata = new HashMap<Long, List<String>>();

    public String getSheetName() {
        return sheetName;
    }

    public HashMap<Long, List<String>> getRowdata() {
        return rowdata;
    }

    public void setRowdata(HashMap<Long, List<String>> rowdata) {
        this.rowdata = rowdata;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

}
