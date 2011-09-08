package util.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import component.download.DowloadEventSetStub.DownloadBinaryFile;

public class DownloadResponseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	File request;
	public DownloadResponseModel() {}
	
	public void set_return(File request) throws Exception{
		this.request = request;
	
	}
}
