package util.models;

import java.io.*;
import java.util.*;

import component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString;
import component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeRange;


public class TimeSeriesModel implements Serializable{
	
	
	private CredentialsHeader header;
	private String EventSetID = null;
	private ArrayOfString measures = null;
	private ArrayOfString rics = null;
	private TimeRange timeRange = null;
	private String intervalDuration = null;
	private String intervalUnit = null;
	private String useGMT = null;
	
	public TimeSeriesModel() {}
	
	public TimeSeriesModel(CredentialsHeader header, String EventSetID, ArrayOfString measures,
							ArrayOfString rics, TimeRange timeRange, String intervalDuration,
							String intervalUnit, String useGMT){
		
		this.header = header;
		this.EventSetID = EventSetID;
		this.measures = measures;
		this.rics = rics;
		this.timeRange = timeRange;
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
    
    public String getEventSetID(){
    	return EventSetID;
    }
    
    public void setEventSetID(String EventSetID){
    	this.EventSetID = EventSetID;
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
