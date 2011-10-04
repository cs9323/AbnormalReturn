package component.abnormalreturns;

import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.axis2.AxisFault;

import util.exceptions.ComputingServiceException;
import util.models.AbnormalreturnModel;
import util.models.AbnormalreturnResponseModel;
import component.abnormalreturns.AbnormalreturnServiceStub.Abnormalreturn;
import component.abnormalreturns.AbnormalreturnServiceStub.AbnormalreturnResponse;

public class AbnormalReturnsImpl implements AbnormalReturns {

    public AbnormalreturnResponseModel calculate(AbnormalreturnModel inputs)
            throws ComputingServiceException {
        return doCalculate(inputs);
    	//return dummy(inputs);
    }

    private AbnormalreturnResponseModel dummy(AbnormalreturnModel inputs)
            throws ComputingServiceException {
        System.out.println("Invoking AbnormalReturn...\n");
        System.out.println("Input EventSetID:" + inputs.getEventID()
                + " received...\n");
        UUID uuid = UUID.randomUUID();
        AbnormalreturnResponseModel arr = new AbnormalreturnResponseModel();
        arr.setEventSetID("abn-" + uuid + ".csv");
        arr.setStatus("Ok");
        return arr;
    }

    private AbnormalreturnResponseModel doCalculate(AbnormalreturnModel inputs)
            throws ComputingServiceException {
    	
    	System.out.println("Input EventSetID:" + inputs.getEventID());
    	
        AbnormalreturnServiceStub arsStub;

        try {
            arsStub = new AbnormalreturnServiceStub(
                    "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/AbnormalreturnService");
        } catch (AxisFault e) {
            // TODO Auto-generated catch block
            throw new ComputingServiceException(e.getMessage());
        }

        Abnormalreturn ar = new Abnormalreturn();
        int dayWin = 0; 
        try{
        	dayWin = Integer.parseInt(inputs.getDayWindow());
        }catch(NumberFormatException e){
        	throw new ComputingServiceException(e.getMessage());
        }
        if (dayWin < 0)
            dayWin = 30;
        String modelType = inputs.getModelType();
        if (modelType == null || modelType.length() == 0)
            modelType = "marketmodel";
        ar.setCredentials(inputs.getCredentialsHeader());
        ar.setEventSetId(inputs.getEventID());
        ar.setModelType(modelType);
        ar.setNbDaysWindow(dayWin);
        AbnormalreturnResponse response = null;
        try {
            response = arsStub.abnormalreturn(ar);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            throw new ComputingServiceException(e.getMessage());
        }
        
        if(response.getStatus().equals("er"))
        	throw new ComputingServiceException("In abnormalReturn service, " + response.getMessage());
        
        AbnormalreturnResponseModel arr = new AbnormalreturnResponseModel();
        arr.setEventSetID(response.getMessage());
        arr.setStatus(response.getStatus());
        System.out.println(arr.getStatus() + ": " + arr.getEventSetID());
        return arr;
    }
}
