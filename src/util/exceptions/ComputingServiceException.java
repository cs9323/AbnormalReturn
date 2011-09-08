package util.exceptions;

public class ComputingServiceException extends Exception {
    
    private String faultMessage;
    
    public ComputingServiceException() {
        super("Computing Service Exception");
    }
    
    public ComputingServiceException(String faultMessage) {
        this.faultMessage = faultMessage;
    }
    
    public ComputingServiceException(Throwable cause) {
        super(cause);
    }
    
    public String getFaultMessage() {
        return faultMessage;
    }
    
    public void setFaultMessage(String faultMessage) {
        this.faultMessage = faultMessage;
    }
    
}
