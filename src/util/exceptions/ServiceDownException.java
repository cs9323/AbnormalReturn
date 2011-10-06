package util.exceptions;

public class ServiceDownException extends Exception {

    private String faultMessage;
    
    public ServiceDownException() {
        super("Computing Service Exception");
    }
    
    public ServiceDownException(String faultMessage) {
        this.faultMessage = faultMessage;
    }
    
    public ServiceDownException(Throwable cause) {
        super(cause);
    }
    
    public String getFaultMessage() {
        return faultMessage;
    }
    
    public void setFaultMessage(String faultMessage) {
        this.faultMessage = faultMessage;
    }
}
