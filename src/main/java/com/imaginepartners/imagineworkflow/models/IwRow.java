/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ibrahimhammani
 */
public class IwRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean isHeader;
    private List<IwCell> cells;

    public IwRow() {
    }

    public IwRow(Boolean isHeader, List<IwCell> cells) {
        this.isHeader = isHeader;
        this.cells = cells;
    }

    public IwRow(List<IwCell> cells) {
        this.cells = cells;
    }

    public Boolean getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(Boolean isHeader) {
        this.isHeader = isHeader;
    }

    public List<IwCell> getCells() {
        return cells;
    }

    public void setCells(List<IwCell> cells) {
        this.cells = cells;
    }

}
