package util.models;

import java.io.Serializable;

public class AbnormalreturnResponseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public AbnormalreturnResponseModel() {
    }

    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        status = value;
    }

    String eventSetID;

    public String getEventSetID() {
        return eventSetID;
    }

    public void setEventSetID(String value) {
        eventSetID = value;
    }
}
