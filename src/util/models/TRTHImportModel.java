package util.models;

import java.io.Serializable;
import java.util.Date;

public class TRTHImportModel implements Serializable{
    
    private static final long serialVersionUID = 365321055071992507L;
    
    public static int END_OF_DAY = 1;
    public static int TIME_AND_SALES = 2;
    
    private Integer messageType;
    private String RIC;
    private Date startTime;
    private Date endTime;
    private Date startDate;
    private Date endDate;
    private Boolean useGMT;
    private Boolean useCorporateActions;
    
    public TRTHImportModel() {}
    
    public TRTHImportModel(Integer messageType, String RIC, 
                            Date startTime, Date endTime, 
                            Date startDate, Date endDate, 
                            Boolean useGMT,
                            Boolean useCorporateActions) 
    {
        this.messageType = messageType;
        this.RIC = RIC;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.useGMT = useGMT;
        this.useCorporateActions = useCorporateActions;
    }



    public Integer getMessageType() {
        return messageType;
    }
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }
    public String getRIC() {
        return RIC;
    }
    public void setRIC(String rIC) {
        RIC = rIC;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Boolean getUseGMT() {
        return useGMT;
    }
    public void setUseGMT(Boolean useGMT) {
        this.useGMT = useGMT;
    }
    public Boolean getUseCorporateActions() {
        return useCorporateActions;
    }
    public void setUseCorporateActions(Boolean useCorporateActions) {
        this.useCorporateActions = useCorporateActions;
    }
    
}
