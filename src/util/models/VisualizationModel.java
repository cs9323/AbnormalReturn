package util.models;

import component.visualization.VisualizationServiceStub.ArrayOfString;
import component.visualization.VisualizationServiceStub.CredentialsHeader;

public class VisualizationModel {
	
	private String EventSetId;
	private CredentialsHeader Credentials;
	private ArrayOfString Columns;
	private String URI;
	
	public VisualizationModel(){}
	
	public void setCredentials(CredentialsHeader credentials) {
		Credentials = credentials;
	}
	public CredentialsHeader getCredentials() {
		return Credentials;
	}

	public void setEventSetId(String eventSetId) {
		EventSetId = eventSetId;
	}

	public String getEventSetId() {
		return EventSetId;
	}

	public void setColumns(ArrayOfString columns2) {
		Columns = columns2;
	}

	public ArrayOfString getColumns() {
		return Columns;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public String getURI() {
		return URI;
	}

}
