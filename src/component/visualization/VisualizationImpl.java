package component.visualization;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import component.visualization.VisualizationServiceStub.VisualizationResponse;

import util.exceptions.ComputingServiceException;
import util.models.VisualizationModel;
import util.models.VisualizationResponseModel;

public class VisualizationImpl implements Visualization {

	@Override
	public VisualizationResponseModel VisualizeData(VisualizationModel request)
			throws ComputingServiceException {
		
		String wsURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/VisualizationService?wsdl";
		VisualizationServiceStub stub = null;
		
		try {
			stub = new VisualizationServiceStub(wsURL);
			stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(5 * 60 * 1000);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			throw new ComputingServiceException(e);
		}
		
		VisualizationServiceStub.Visualization req = new VisualizationServiceStub.Visualization();
		req.setColumns(request.getColumns());
		req.setCredentials(request.getCredentials());
		req.setEventSetId(request.getEventSetId());
		req.setUri(request.getURI());
		
		VisualizationResponse rsp = null;
		try {
			rsp = stub.visualization(req);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			throw new ComputingServiceException(e);
		}
		
		VisualizationResponseModel result = new VisualizationResponseModel();
		result.setVisualizationURL(rsp.getMessage());
		
		System.out.println(rsp.getStatus() + ": " + rsp.getMessage());
		return result;
	}

}
