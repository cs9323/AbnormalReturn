package util.models;

import java.io.Serializable;

import component.abnormalreturns.AbnormalreturnServiceStub.CredentialsHeader;

public class AbnormalreturnModel implements Serializable{
	   
    private static final long serialVersionUID = 1L;
    public AbnormalreturnModel(){}
       
       public AbnormalreturnModel(CredentialsHeader ch, String eventID, String modelType, String dayWindow){
    	   this.ch=ch;
    	   this.EventID=eventID;
    	   this.modelType=modelType;
    	   this.dayWindow=dayWindow;
       }
       
       CredentialsHeader ch;
       public CredentialsHeader getCredentialsHeader(){return ch;}
       public void setCredentialsHeader(CredentialsHeader value){ch = value;}
       
       String EventID;
       public String getEventID(){return EventID;}
       public void setEventID(String value){EventID = value;}
       
       String modelType;
       public String getModelType(){return modelType;}
       public void setModelType(String value){modelType = value;}

       String dayWindow;
       public String getDayWindow(){return dayWindow;}
       public void setDayWindow(String value){dayWindow = value;}
}
