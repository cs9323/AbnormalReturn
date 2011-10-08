package util.models;

import java.io.Serializable;

public class DownloadResponseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String eventSetId;
	
	public DownloadResponseModel() {}

	public void setEventSetId(String eventSetId) {
		this.eventSetId = eventSetId;
	}

	public String getEventSetId() {
		return eventSetId;
	}

}
