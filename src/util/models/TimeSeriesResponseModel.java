package util.models;

import java.io.Serializable;

public class TimeSeriesResponseModel implements Serializable{
	
	public String[] result = null;
	
	public TimeSeriesResponseModel() {}
	
	//public TimeSeriesResponseModel()
	
	public void setResponseModel(String[] result){
		this.result=result;
	}
	
}
