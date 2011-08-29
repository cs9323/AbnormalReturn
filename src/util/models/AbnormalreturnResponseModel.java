package util.models;

public class AbnormalreturnResponseModel {
	    
	    public AbnormalreturnResponseModel(){}
	
         String status;
         public String getStatus(){return status;}
         public void setStatus(String value){status=value;}
         
         String message;
         public String getMessage(){return message;}
         public void setMessage(String value){message=value;}
}
