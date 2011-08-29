package util.models;

import component.abnormalreturns.AbnormalreturnServiceStub.CredentialsHeader;

public class AbnormalreturnModel {
	   
       public AbnormalreturnModel(){}
       
       public AbnormalreturnModel(CredentialsHeader ch,String eventID,String modelType,int dayWindow){
    	   this.ch=ch;
    	   this.EventID=eventID;
    	   this.modelType=modelType;
    	   this.dayWindow=dayWindow;
       }
       
       CredentialsHeader ch;
       public CredentialsHeader getCredentialsHeader(){return ch;}
       public void setCredentialsHeader(CredentialsHeader value){ch=value;}
       
       String EventID;
       public String getEventID(){return EventID;}
       public void setEventID(String value){EventID=value;}
       
       String modelType;
       public String getModelType(){return modelType;}
       public void setModelType(String value){modelType=value;}

       int dayWindow;
       public int getDayWindow(){return dayWindow;}
       public void setDayWindow(int value){dayWindow=value;}
}
