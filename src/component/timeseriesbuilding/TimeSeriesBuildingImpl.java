package component.timeseriesbuilding;

import java.rmi.RemoteException;
import java.util.UUID;

import org.apache.axis2.AxisFault;

import component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString;
import component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeRange;
import component.timeseriesbuilding.TimeseriesServiceStub.Timeseries;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeseriesResponse;

import util.exceptions.ComputingServiceException;
import util.exceptions.ServiceDownException;
import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;
import util.models.TimeSeriesModel;
import util.models.TimeSeriesResponseModel;

@SuppressWarnings("unused")
public class TimeSeriesBuildingImpl implements TimeSeriesBuilding {
	
	String wURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/TimeseriesService?wsdl";
	TimeseriesServiceStub stub;
	
	Timeseries Timeseries_request;
	
	@Override
	public TimeSeriesResponseModel returnStatusMsg(TimeSeriesModel request) throws ComputingServiceException, ServiceDownException{
		return generateStatusMsg(request);
		//return dummy(request);
	}
	
	
    private TimeSeriesResponseModel generateStatusMsg(TimeSeriesModel request) throws ComputingServiceException, ServiceDownException{
		CredentialsHeader header = request.getCredentialsHeader();
		
		String marketData = request.getRequest().getMarketDataEventSetID();
		String index = request.getRequest().getIndexEventSetID();
		String riskFreeAsset = request.getRequest().getRiskFreeAssetEventSetID();
		
		ArrayOfString measures = request.getMeasures();
		ArrayOfString rics = request.getRics();
		TimeRange timeRange = request.getTimeRange();
		String intervalDuration = request.getIntervalDuration();
		String intervalUnit = request.getIntervalUnit();
		String useGMT = request.getUseGMT();
		
		try {
            stub = new TimeseriesServiceStub(wURL);
        } catch (AxisFault e) {
            //throw new ComputingServiceException(e.getMessage());
        	throw new ServiceDownException("TimeSeries Service is Down.");
        }
		Timeseries_request = new Timeseries();
		
		Timeseries_request.setCredentials(header);
		Timeseries_request.setMeasures(measures);
		Timeseries_request.setRics(rics);
		Timeseries_request.setTime(timeRange);
		Timeseries_request.setTimeIntervalDuration(intervalDuration);
		Timeseries_request.setTimeIntervalUnit(intervalUnit);
		Timeseries_request.setUseGMT(useGMT);

		try {
			if(request.getRequest().getMarketDataStatus())
				marketData = invoke(marketData);
			else
				marketData = null;
			if(request.getRequest().getIndexStatus())
				index = invoke(index);
			else
				index = null;
            if(request.getRequest().getRiskFreeAssetStatus())
            	riskFreeAsset = invoke(riskFreeAsset);
            else
            	riskFreeAsset = null;
		} catch (ComputingServiceException e) {
		    throw new ComputingServiceException(e.getFaultMessage());
        }
		
		TimeSeriesResponseModel response = new TimeSeriesResponseModel(marketData, index, riskFreeAsset);
		return response;
		
	}
	
	public String invoke(String EventSetID) throws ComputingServiceException, ServiceDownException {
		
		Timeseries_request.setEventSetId(EventSetID);
		TimeseriesResponse response = null;
		
		try {
			response = stub.timeseries(Timeseries_request);
		} catch (RemoteException e) {
			//throw new ComputingServiceException(e.getMessage());
        	throw new ServiceDownException("TimeSeries Service is Down.");
		}
		
		String statusmsg = response.getMessage();
		String status = response.getStatus();
		
		StringBuffer sb = new StringBuffer();
		String[] result = null;
		
		System.out.println(response.getStatus() + ": " + response.getMessage());
		
		if (status.equals("ok")){
			return statusmsg;
		}
		if (status.equals("er")){
			throw new ComputingServiceException("In timeSeriesBuilding service, " + statusmsg);
		}
		return "";
	}
	
	private TimeSeriesResponseModel dummy(TimeSeriesModel request) {
	    
		System.out.println(request.getRequest().getMarketDataEventSetID());
		System.out.println(request.getRequest().getIndexEventSetID());
		System.out.println(request.getRequest().getRiskFreeAssetEventSetID());
		
		TimeSeriesResponseModel response = new TimeSeriesResponseModel();
		
		UUID uuid = UUID.randomUUID();
		String MarketDataEventSetID = "tsb-" + uuid + ".csv";
		response.setMarketDataEventSetID(MarketDataEventSetID);
		
		uuid = UUID.randomUUID();
		String IndexEventSetID = "tsb-" + uuid + ".csv";
		response.setIndexEventSetID(IndexEventSetID);
		
		uuid = UUID.randomUUID();
		String RiskFreeAssetEventSetID = "tsb-" + uuid + ".csv";
		response.setRiskFreeAssetEventSetID(RiskFreeAssetEventSetID);
		
		return response;
		
	}
}
