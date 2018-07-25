package com.imaginepartners.imagineworkflow.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "IW_INPUT")
public class IwInput implements Serializable {

	private static final int defaultMaxInputSize = 60;

    @JoinColumn(name = "IW_FORM", referencedColumnName = "IW_FORM_ID", nullable = false)
    @ManyToOne(optional = false)
    private IwForm iwForm;

    @JoinColumn(name = "IW_VARIABLE_PROCESS", referencedColumnName = "IW_VARIABLE_PROCESS_ID", nullable = true)
    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private IwVariableProcess iwVariableProcess;

    private static final long serialVersionUID = 1L;

    @Column(name = "IW_INPUT_ID", nullable = false)
    private String id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "IW_INPUT_TYPE", nullable = false, length = 64)
    private String component;

    @Column(name = "IW_EDITABLE")
    private Boolean editable;

    @NotNull
    @Column(name = "IW_INDEX")
    private Integer index;

    @Size(min = 1, max = 250)
    @Column(name = "IW_LABEL", length = 250)
    private String label;

    @Size(min = 1, max = 512)
    @Column(name = "IW_DESCRIPTION", length = 512)
    private String description;

    @Size(min = 1, max = 250)
    @Column(name = "IW_PLACEHOLDER", length = 250)
    private String placeholder;

    @Column(name = "IW_REQUIRED")
    private Boolean required;
    @Column(name = "IW_VALIDATION", length = 250)
    private String validation;

    @Size(min = 1, max = 512)
    @Column(name = "IW_VALUE", length = 512)
    private String value;

    @Column(name = "IW_DATE_FORMAT", length = 64)
    private String dateformat;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_OPTIONS", length = 65535)
    private ArrayList<String> options;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_OPTIONS2", length = 65535)
    private ArrayList<String> options2;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_OPTIONS3", length = 65535)
    private ArrayList<String> options3;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_ENTITY_FILTER", length = 65535)
    private ArrayList<String> entityFilter;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_SUB_ROWS", length = 65535)
    private ArrayList<String> subrows;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_SUB_COLUMNS", length = 65535)
    private ArrayList<String> subcolumns;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_NUMBER_SUB_ROWS", length = 65535)
    private ArrayList<Integer> numbersubrows;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_NUMBER_SUB_COLOMNS", length = 65535)
    private ArrayList<Integer> numbersubcolumns;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_READ_GROUPS", length = 65535)
    private ArrayList<String> readgroups;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_READ_USERS", length = 65535)
    private ArrayList<String> readusers;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_WRITE_GROUPS", length = 65535)
    private ArrayList<String> writegroups;

    @Lob
    @Size(max = 65535)
    @Column(name = "IW_WRITE_USERS", length = 65535)
    private ArrayList<String> writeusers;

	@Lob
	@Size(max=65535)
	@Column(name = "IW_DISABLED_FOR_TASKS", length = 65535)
	private ArrayList<String> disabledfortaches;

	@Lob
    @Size(max = 65535)
    @Column(name = "IW_WIDTH_SUB_COLUMNS", length = 65535)
    private ArrayList<Integer> widthsubcolumns;

    @Column(name = "IW_TAB1", length = 254)
    private String onglet1;

    @Column(name = "IW_TAB2", length = 254)
    private String onglet2;

    @Column(name = "IW_IMPORT", columnDefinition = "boolean default false")
    private Boolean allowImport;

    @Column(name = "IW_EXPORT", columnDefinition = "boolean default false")
    private Boolean allowExport;

    @Column(name = "IW_SEARCH", columnDefinition = "boolean default false")
    private Boolean allowSearch;

    @Column(name = "IW_ALLOW_CREATE", columnDefinition = "boolean default false")
    private Boolean allowCreate;

    @Column(name = "IW_UPDATE", columnDefinition = "boolean default false")
    private Boolean allowUpdate;

    @Column(name = "IW_DELETE", columnDefinition = "boolean default false")
    private Boolean allowDelete;

    @Column(name = "IW_FILTER", columnDefinition = "boolean default false")
    private Boolean allowFilter;

    @Column(name = "IW_MULTIPLE")
    private Boolean multple;

    @Column(name = "IW_VALIDATION_REGEX")
    private String regex;

    @Column(name = "IW_VALIDATION_ERROR_MSG")
    private String errorMessage;

	@Column(name = "IW_INPUT_SIZE")
	private String inputSize;

	@Column(name = "IW_DATE_PATTENRN")
	private String datePattern;

    @Lob
    @Size(max = 65535)
    @Column(name= "IW_EXP_LAST_COLUMNS", length = 65535)
    private ArrayList<String> explastcolumn;

    @Column(name = "IW_DATA_BASE_REQUEST")
    private String databaseRequest;

    @Column(name= "IW_USE_REQUEST")
    private Boolean useRequest;

 	@Column(name= "IW_MAX_SELECT")
    private String maxSelect;


    /*@Lob
     @Size(max = 65535)
     @Column(name = "IW_TREE_CHECKLIST_OPTIONS", length = 65535)*/
    transient private ArrayList<LinkedHashMap<String, Object>> treechecklistoptions;
    transient private ArrayList<LinkedHashMap<String, Object>> multilinerows;
    transient private ArrayList<LinkedHashMap<String, Object>> multilinecols;
    transient private LinkedHashMap<String, List<String>> tachesbyprocess;
    transient private List<LinkedHashMap<String, List<String>>> colTachesbyprocess;

    transient private List<List<String>> entityUserRights;
    transient private List<List<String>> entityGroupRights;
    transient private List<List<String>> entityUserWriteRights;
    transient private List<List<String>> entityGroupWriteRights;
    @Id
    @Basic(optional = false)
    @Column(name = "IW_INPUT_REAL_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iwinputrealid;

    public String getComponent() {
        return component;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDateformat() {
        return dateformat;
    }

    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    public IwInput() {
    }

    public IwForm getIwForm() {
        return iwForm;
    }

    public void setIwForm(IwForm iwForm) {
        this.iwForm = iwForm;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public ArrayList<String> getOptions2() {
        return options2;
    }

    public void setOptions2(ArrayList<String> options2) {
        this.options2 = options2;
    }

    public ArrayList<String> getOptions3() {
        return options3;
    }

    public void setOptions3(ArrayList<String> options3) {
        this.options3 = options3;
    }

    public ArrayList<String> getEntityFilter() {
        return entityFilter;
    }

    public void setEntityFilter(ArrayList<String> entityFilter) {
        this.entityFilter = entityFilter;
    }

    public ArrayList<String> getSubrows() {
        return subrows;
    }

    public void setSubrows(ArrayList<String> subrows) {
        this.subrows = subrows;
    }

    public ArrayList<String> getSubcolumns() {
        return subcolumns;
    }

    public void setSubcolumns(ArrayList<String> subcolumns) {
        this.subcolumns = subcolumns;
    }

    public ArrayList<Integer> getNumbersubrows() {
        return numbersubrows;
    }

    public void setNumbersubrows(ArrayList<Integer> numbersubrows) {
        this.numbersubrows = numbersubrows;
    }

    public ArrayList<Integer> getNumbersubcolumns() {
        return numbersubcolumns;
    }

    public void setNumbersubcolumns(ArrayList<Integer> numbersubcolumns) {
        this.numbersubcolumns = numbersubcolumns;
    }

    public ArrayList<String> getReadgroups() {
        return readgroups;
    }

    public void setReadgroups(ArrayList<String> readgroups) {
        this.readgroups = readgroups;
    }

    public ArrayList<String> getReadusers() {
        return readusers;
    }

    public void setReadusers(ArrayList<String> readusers) {
        this.readusers = readusers;
    }

    public ArrayList<String> getWritegroups() {
        return writegroups;
    }

    public void setWritegroups(ArrayList<String> writegroups) {
        this.writegroups = writegroups;
    }

    public ArrayList<String> getWriteusers() {
        return writeusers;
    }

    public void setWriteusers(ArrayList<String> writeusers) {
        this.writeusers = writeusers;
    }

	public ArrayList<String> getDisabledfortaches() {
		return disabledfortaches;
	}

	public void setDisabledfortaches(ArrayList<String> disabledfortaches) {
		this.disabledfortaches = disabledfortaches;
	}

	public ArrayList<Integer> getWidthsubcolumns() {
        return widthsubcolumns;
    }

    public void setWidthsubcolumns(ArrayList<Integer> widthsubcolumns) {
        this.widthsubcolumns = widthsubcolumns;
    }

    public String getOnglet1() {
        return onglet1;
    }

    public void setOnglet1(String onglet1) {
        this.onglet1 = onglet1;
    }

    public String getOnglet2() {
        return onglet2;
    }

    public void setOnglet2(String onglet2) {
        this.onglet2 = onglet2;
    }

    public ArrayList<LinkedHashMap<String, Object>> getTreechecklistoptions() {
        return treechecklistoptions;
    }

    public void setTreechecklistoptions(ArrayList<LinkedHashMap<String, Object>> treechecklistoptions) {
        this.treechecklistoptions = treechecklistoptions;
    }

    public ArrayList<LinkedHashMap<String, Object>> getMultilinerows() {
        return multilinerows;
    }

    public void setMultilinerows(ArrayList<LinkedHashMap<String, Object>> multilinerows) {
        this.multilinerows = multilinerows;
    }

    public ArrayList<LinkedHashMap<String, Object>> getMultilinecols() {
        return multilinecols;
    }

    public void setMultilinecols(ArrayList<LinkedHashMap<String, Object>> multilinecols) {
        this.multilinecols = multilinecols;
    }

    public LinkedHashMap<String, List<String>> getTachesbyprocess() {
        return tachesbyprocess;
    }

    public void setTachesbyprocess(LinkedHashMap<String, List<String>> tachesbyprocess) {
        this.tachesbyprocess = tachesbyprocess;
    }

    public List<LinkedHashMap<String, List<String>>> getColTachesbyprocess() {
        return colTachesbyprocess;
    }

    public void setColTachesbyprocess(List<LinkedHashMap<String, List<String>>> colTachesbyprocess) {
        this.colTachesbyprocess = colTachesbyprocess;
    }

    public IwVariableProcess getIwVariableProcess() {
        return iwVariableProcess;
    }

    public void setIwVariableProcess(IwVariableProcess iwVariableProcess) {
        this.iwVariableProcess = iwVariableProcess;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iwinputrealid != null ? iwinputrealid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IwInput)) {
            return false;
        }
        IwInput other = (IwInput) object;
        if ((this.iwinputrealid == null && other.iwinputrealid != null) || (this.iwinputrealid != null && !this.iwinputrealid.equals(other.iwinputrealid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.imaginepartners.imagineworkflow.models.IwInput[ id=" + iwinputrealid + " ]";
    }

    public Boolean getAllowImport() {
        return allowImport;
    }

    public void setAllowImport(Boolean allowImport) {
        this.allowImport = allowImport;
    }

    public Boolean getAllowExport() {
        return allowExport;
    }

    public void setAllowExport(Boolean allowExport) {
        this.allowExport = allowExport;
    }

    public Boolean getMultple() {
        return multple;
    }

    public void setMultple(Boolean multple) {
        this.multple = multple;
    }

    public List<List<String>> getEntityUserRights() {
        return entityUserRights;
    }

    public void setEntityUserRights(List<List<String>> entityUserRights) {
        this.entityUserRights = entityUserRights;
    }

    public List<List<String>> getEntityGroupRights() {
        return entityGroupRights;
    }

    public void setEntityGroupRights(List<List<String>> entityGroupRights) {
        this.entityGroupRights = entityGroupRights;
    }

    public Boolean getAllowSearch() {
        return allowSearch;
    }

    public void setAllowSearch(Boolean allowSearch) {
        this.allowSearch = allowSearch;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

	public String getInputSize() {
		return inputSize;
	}

	public void setInputSize(String inputSize) {
		this.inputSize = inputSize;
	}

	/**
	 *
	 * @author rafik.rebahi
	 */
	public String getDatePattern() {
		if(this.datePattern == null) {
			if ("Date".equals(this.dateformat))
				this.datePattern = "dd/MM/yyyy";
			else if ("Time".equals(this.dateformat))
				this.datePattern = "HH:mm";
			else if ("DateTime".equals(this.dateformat))
				this.datePattern = "dd/MM/yyyy HH:mm";
		}
		return this.datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

    public List<List<String>> getEntityUserWriteRights() {
        return entityUserWriteRights;
    }

    public void setEntityUserWriteRights(List<List<String>> entityUserWriteRights) {
        this.entityUserWriteRights = entityUserWriteRights;
    }

    public List<List<String>> getEntityGroupWriteRights() {
        return entityGroupWriteRights;
    }

    public void setEntityGroupWriteRights(List<List<String>> entityGroupWriteRights) {
        this.entityGroupWriteRights = entityGroupWriteRights;
    }

    public Boolean getAllowCreate() {
        return allowCreate;
    }

    public void setAllowCreate(Boolean allowCreate) {
        this.allowCreate = allowCreate;
    }

    public Boolean getAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(Boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public Boolean getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(Boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public Boolean getAllowFilter() {
        return allowFilter;
    }

    public void setAllowFilter(Boolean allowFilter) {
        this.allowFilter = allowFilter;
    }

    public Long getIwinputrealid() {
        return iwinputrealid;
    }

    public void setIwinputrealid(Long iwinputrealid) {
        this.iwinputrealid = iwinputrealid;
    }

    public ArrayList<String> getExplastcolumn() {
        return explastcolumn;
    }

    public void setExplastcolumn(ArrayList<String> explastcolumn) {
        this.explastcolumn = explastcolumn;
    }

    public String getDatabaseRequest() {
        return databaseRequest;
    }

    public void setDatabaseRequest(String databaseRequest) {
        this.databaseRequest = databaseRequest;
    }

    public Boolean getUseRequest() {
        return useRequest;
    }

    public void setUseRequest(Boolean useRequest) {
        this.useRequest = useRequest;
    }

	public String getMaxSelect() {
		return maxSelect;
	}

	public void setMaxSelect(String maxSelect) {
		this.maxSelect = maxSelect;
	}

	/**
	 *
	 * @author rafik.rebahi
	 */
	public int getMaxInputSize() {

		try {
			return Integer.parseInt(inputSize);
		} catch (Exception e) {
			return defaultMaxInputSize;
		}
	}
}
