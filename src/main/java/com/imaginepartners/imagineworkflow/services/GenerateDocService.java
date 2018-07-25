package com.imaginepartners.imagineworkflow.services;

import java.io.InputStream;
import java.util.List;

/**
 * Created by lenovo on 31/01/2017.
 */
public interface GenerateDocService {

	public void loadDoc(String inputfilepath) throws Exception;
	public String docxToPdf(InputStream inputfilepath, String fileName) throws Exception;
	public void addIdsToDocx(String inputfilepath, String toFind, String replacer) throws Exception;
	public void addImageToDocx(String inputfilepath, String imagePath,String toFind) throws Exception;
	public List<String> getPlaceholderImageList(String inputfilepath) throws Exception;

}
