package util.models;

import java.io.*;
import java.util.*;

import component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString;
import component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeRange;


public class TimeSeriesModel implements Serializable{
	
	
	private CredentialsHeader header;
	private TRTHImportResponseModel request;
	private ArrayOfString measures;
	private ArrayOfString rics;
	private TimeRange timeRange;
	private String intervalDuration;
	private String intervalUnit;
	private String useGMT;
	
	public TimeSeriesModel() {}
	
	public TimeSeriesModel(CredentialsHeader header, TRTHImportResponseModel request, ArrayOfString measures,
							ArrayOfString rics, String intervalDuration,
							String intervalUnit, String useGMT){
		
		this.header = header;
		this.request = request;
		this.measures = measures;
		this.rics = rics;
		timeRange = new TimeRange();
		timeRange.setStart(request.getTimeRange().getStart());
		timeRange.setEnd(request.getTimeRange().getEnd());
		this.intervalDuration = intervalDuration;
		this.intervalUnit = intervalUnit;
		this.useGMT = useGMT;
	}
	
	
	public CredentialsHeader getCredentialsHeader() {
        return header;
    }
    public void setCredentialsHeader(CredentialsHeader header) {
        this.header = header;
    }
    
    public TRTHImportResponseModel getRequest(){
    	return request;
    }
    
    public void setReuest(TRTHImportResponseModel request){
    	this.request = request;
    }
    
    public ArrayOfString getMeasures(){
    	return measures;
    }
    public void setMeasures(ArrayOfString measures){
    	this.measures = measures;
    }
    
    public ArrayOfString getRics(){
    	return rics;
    }
    public void setRics(ArrayOfString rics){
    	this.rics = rics;
    }
    
    public TimeRange getTimeRange(){
    	return timeRange;
    }
    public void setTimeRange(TimeRange timeRange){
    	this.timeRange = timeRange;
    }
    
    public String getIntervalDuration(){
    	return intervalDuration;
    }
    
    public void setIntervalDuration(String intervalDuration){
    	this.intervalDuration = intervalDuration;
    }
    
    public String getIntervalUnit(){
    	return intervalUnit;
    }
    
    public void setIntervalUnit(String intervalUnit){
    	this.intervalUnit = intervalUnit;
    }
    
    public String getUseGMT(){
    	return useGMT;
    }
    
    public void setUseGMT(String useGMT){
    	this.useGMT = useGMT;
    }
    
    
}
