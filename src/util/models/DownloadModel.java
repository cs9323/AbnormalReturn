package util.models;

import java.io.Serializable;

public class DownloadModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String eventSetId;
	
	public DownloadModel() {}
	
	public DownloadModel(String eventSetId){
		this.eventSetId = eventSetId;
	}
	
	public String geteventSetId(){
		return eventSetId;
	}

	public void seteventSetId(String eventSetId){
		this.eventSetId = eventSetId;
	}
}
