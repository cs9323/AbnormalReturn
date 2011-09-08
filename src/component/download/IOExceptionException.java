
/**
 * IOExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

package component.download;

public class IOExceptionException extends java.lang.Exception{
    
    private component.download.DowloadEventSetStub.IOExceptionE faultMessage;

    
        public IOExceptionException() {
            super("IOExceptionException");
        }

        public IOExceptionException(java.lang.String s) {
           super(s);
        }

        public IOExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public IOExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(component.download.DowloadEventSetStub.IOExceptionE msg){
       faultMessage = msg;
    }
    
    public component.download.DowloadEventSetStub.IOExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    