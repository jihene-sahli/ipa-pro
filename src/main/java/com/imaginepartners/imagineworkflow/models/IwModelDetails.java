package com.imaginepartners.imagineworkflow.models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Wassim Mansouri on 3/1/2016.
 */

@Entity
@Table(name = "IW_MODEL_DETAILS")

public class IwModelDetails implements Serializable {

	// Get models IDs and store them as the ID field in the table
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IW_MODEL_DETAILS_ID")
	private Integer iwModeDetailsId;

	// Add an index for listing vues, so we can implement drag and drop reorder
	@Column(name = "IW_LISTING_INDEX")
	private Integer iwListingIndex;

	@Column(name = "IW_ACT_MODEL_ID")
	private String iwActModelId;

	public IwModelDetails() {
	}

	// Getter and setter for the model ID
	public String getIwActModelId() {
		return iwActModelId;
	}

	public void setIwActModelId(String iwActModelId) {
		this.iwActModelId = iwActModelId;
	}

	// Getter and setter for the ID
	public Integer getIwModeDetailsId() {
		return iwModeDetailsId;
	}

	public void setIwModeDetailsId(Integer iwModeDetailslId) {
		this.iwModeDetailsId = iwModeDetailslId;
	}

	// Getter and setter for the index in vue needing a drag and drop reorder
	public Integer getIwListingIndex() {
		return iwListingIndex;
	}
	public void setIwListingIndex(Integer iwListingIndex) {
		this.iwListingIndex = iwListingIndex;
	}

}
