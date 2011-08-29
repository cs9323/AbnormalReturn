package component.merge;

import java.rmi.RemoteException;

import util.models.MergeModel;

public class MergeImpl implements Merge {
	
	public String MergeData(MergeModel request) throws Exception {
		String wsURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/MergeService";
		MergeServiceStub stub = new MergeServiceStub(wsURL);
		MergeServiceStub.Merge msg = new MergeServiceStub.Merge();
		msg.setEventSet1Id(request.getEventSet1Id());
		msg.setEventSet2Id(request.getEventSet2Id());
		
		MergeServiceStub.MergeResponse response = new MergeServiceStub.MergeResponse();
		try{
			response = stub.merge(msg);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		
		if(response.getStatus().equals("ok"))
			return response.getMessage();
		
		return null;
	}
}
