package util.models;

import java.io.File;
import java.io.Serializable;

public class DownloadResponseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private File returnFile;
	
	public DownloadResponseModel() {}

	public void setReturnFile(File returnFile) {
		this.returnFile = returnFile;
	}

	public File getReturnFile() {
		return returnFile;
	}

}
