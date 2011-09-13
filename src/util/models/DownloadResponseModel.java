package util.models;

import java.io.File;
import java.io.Serializable;

public class DownloadResponseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private File request;
	
	public DownloadResponseModel() {}

	public void setRequest(File request) {
		this.request = request;
	}

	public File getRequest() {
		return request;
	}
	

}
