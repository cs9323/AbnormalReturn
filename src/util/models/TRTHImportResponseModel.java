package util.models;

public class TRTHImportResponseModel {
    
    private String eventSetID;
    private String status;
    
    public TRTHImportResponseModel() {}
    
    public TRTHImportResponseModel(String eventSetID, String status) {
        this.eventSetID = eventSetID;
        this.status = status;
    }

    public String getEventSetID() {
        return eventSetID;
    }

    public void setEventSetID(String eventSetID) {
        this.eventSetID = eventSetID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
