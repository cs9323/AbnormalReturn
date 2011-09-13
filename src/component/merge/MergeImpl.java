package component.merge;

import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.axis2.AxisFault;

import util.exceptions.ComputingServiceException;
import util.models.MergeModel;
import util.models.MergeResponseModel;
import component.merge.MergeServiceStub.ArrayOfString;
import component.merge.MergeServiceStub.CredentialsHeader;

public class MergeImpl implements Merge {
	
	public MergeResponseModel MergeData(MergeModel request) throws ComputingServiceException {
		//return doMergeData(request);
		return dummyMergeData(request);
	}
	
	private MergeResponseModel doMergeData(MergeModel request) throws ComputingServiceException {
		String wsURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/MergeService";
		MergeServiceStub stub = null;
		
		try {
            stub = new MergeServiceStub(wsURL);
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(5 * 60 * 1000);
        } catch (AxisFault e1) {
            throw new ComputingServiceException(e1);
        }
		
		String marketDataEventSetID = request.getMarketDataEventSetID();
		String indexEventSetID = request.getIndexEventSetID();
		String riskFreeAssetEventSetID = request.getRiskFreeAssetEventSetID();
		CredentialsHeader Credentials = request.getCredentials();
		ArrayOfString merge1Measure1 = request.getMerge1MeasuresEv1();
		ArrayOfString merge1Measure2 = request.getMerge1MeasuresEv2();
		ArrayOfString merge2Measure1 = request.getMerge2MeasuresEv1();
		ArrayOfString merge2Measure2 = request.getMerge2MeasuresEv2();
		String merge1Option = request.getMerge1Option();
		String merge2Option = request.getMerge2Option();
		String merge1PrefixEv2 = request.getMerge1PrefixEv2();
		String merge2PrefixEv2 = request.getMerge2PrefixEv2();
		
		//first merge
		MergeServiceStub.Merge msg1 = new MergeServiceStub.Merge();
		msg1.setCredentials(Credentials);
		msg1.setEventSet1Id(marketDataEventSetID);
		msg1.setEventSet2Id(indexEventSetID);
		msg1.setMeasuresEv1(merge1Measure1);
		msg1.setMeasuresEv2(merge1Measure2);
		msg1.setMergeOption(merge1Option);
		msg1.setPrefixEv2(merge1PrefixEv2);
		msg1.setUserdefinedoption("");
		
		MergeServiceStub.MergeResponse response1 = new MergeServiceStub.MergeResponse();
		try{
			response1 = stub.merge(msg1);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		
		String ResultEventSetID1 = response1.getMessage();
		System.out.println(response1.getStatus() + ": " + response1.getMessage());
		
		//second merge
		MergeServiceStub.Merge msg2 = new MergeServiceStub.Merge();
		msg2.setCredentials(Credentials);
		msg2.setEventSet1Id(ResultEventSetID1);
		msg2.setEventSet2Id(riskFreeAssetEventSetID);
		msg2.setMeasuresEv1(merge2Measure1);
		msg2.setMeasuresEv2(merge2Measure2);
		msg2.setMergeOption(merge2Option);
		msg2.setPrefixEv2(merge2PrefixEv2);
		msg2.setUserdefinedoption("");
		
		MergeServiceStub.MergeResponse response2 = new MergeServiceStub.MergeResponse();
		try{
			response2 = stub.merge(msg2);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		
		MergeResponseModel resModel = new MergeResponseModel();
		resModel.setResultEventSetID(response2.getMessage());
		System.out.println(response2.getStatus() + ": " + response2.getMessage());
		
		return resModel;
	}
	
	private MergeResponseModel dummyMergeData(MergeModel request) throws ComputingServiceException {
		System.out.println(request.getMarketDataEventSetID());
		System.out.println(request.getIndexEventSetID());
		System.out.println(request.getRiskFreeAssetEventSetID());
		
		MergeResponseModel response = new MergeResponseModel();
		
		UUID uuid = UUID.randomUUID();
		String resultEventSetID = "merge-" + uuid + ".csv";
		response.setResultEventSetID(resultEventSetID);
		
		return response;
	}
	
}
