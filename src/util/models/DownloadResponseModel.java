package util.models;

import java.io.File;
import java.io.Serializable;

public class DownloadResponseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	File request;
	public DownloadResponseModel() {}
	
	public void set_return(File request){
		this.request = request;
	}
}
