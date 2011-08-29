package util.models;

import java.io.Serializable;

import component.trthimport.TRTHImportWrapperServiceStub.DateRange;
import component.trthimport.TRTHImportWrapperServiceStub.TimeRange;

public class TRTHImportModel implements Serializable{
    
    private static final long serialVersionUID = 365321055071992507L;
    
    public static String END_OF_DAY = "EndOfDay";
    public static String TIME_AND_SALES = "Time & Scales";
    
    private String messageType;
    private String RIC;
    private DateRange dateRange;
    private TimeRange timeRange;
    private String useGMT;
    private String useCorporateActions;
    
    public TRTHImportModel() {}
    
    public TRTHImportModel(String messageType, String RIC, 
                            String startTime, String endTime, 
                            String startDate, String endDate, 
                            String useGMT,
                            String useCorporateActions) 
    {
        this.messageType = (messageType.equals("0"))?"EndOfDay":"Time & Scales";
        
        this.RIC = RIC;
        
        dateRange = new DateRange();
        
        this.dateRange.setStart(startDate);
        this.dateRange.setEnd(endDate);
        
        timeRange = new TimeRange();
        
        this.timeRange.setStart(startTime);
        this.timeRange.setEnd(endTime);
        
        this.useGMT = useGMT;
        
        this.useCorporateActions = useCorporateActions;
        
    }



    public String getMessageType() {
        return messageType;
    }
    public void setMessageType(Integer messageType) {
        this.messageType = messageType==0?"EndOfDay":"Time & Scales";
    }
    public String getRIC() {
        return RIC;
    }
    public void setRIC(String rIC) {
        RIC = rIC;
    }
    
    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(TimeRange timeRange) {
        this.timeRange = timeRange;
    }

    public String getUseGMT() {
        return useGMT;
    }
    public void setUseGMT(String useGMT) {
        this.useGMT = useGMT;
    }
    public String getUseCorporateActions() {
        return useCorporateActions;
    }
    public void setUseCorporateActions(String useCorporateActions) {
        this.useCorporateActions = useCorporateActions;
    }
    
}
