package component.timeseriesbuilding;

import java.rmi.RemoteException;
import java.util.UUID;

import component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString;
import component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeRange;
import component.timeseriesbuilding.TimeseriesServiceStub.Timeseries;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeseriesResponse;

import util.models.TRTHImportModel;
import util.models.TimeSeriesModel;
import util.models.TimeSeriesResponseModel;

public class TimeSeriesBuildingImpl implements TimeSeriesBuilding {
	
	String wURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/TimeseriesService?wsdl";
	TimeseriesServiceStub stub;
	
	Timeseries Timeseries_request;
	
	@Override
	public TimeSeriesResponseModel returnStatusMsg(TimeSeriesModel request) throws Exception{
	    System.out.println("try");
		return dummy(request);
	}
	
	private TimeSeriesResponseModel generateStatusMsg(TimeSeriesModel request) throws Exception{
		CredentialsHeader header = request.getCredentialsHeader();
		
		String marketData = request.getRequest().getMarketDataEventSetID();
		String index = request.getRequest().getIndexEventSetID();
		String riskFreeAsset = request.getRequest().getIndexEventSetID();
		
		ArrayOfString measures = request.getMeasures();
		ArrayOfString rics = request.getRics();
		TimeRange timeRange = request.getTimeRange();
		String intervalDuration = request.getIntervalDuration();
		String intervalUnit = request.getIntervalUnit();
		String useGMT = request.getUseGMT();
		
		stub = new TimeseriesServiceStub(wURL);
		Timeseries_request = new Timeseries();
		
		Timeseries_request.setCredentials(header);
		Timeseries_request.setMeasures(measures);
		Timeseries_request.setRics(rics);
		Timeseries_request.setTime(timeRange);
		Timeseries_request.setTimeIntervalDuration(intervalDuration);
		Timeseries_request.setTimeIntervalUnit(intervalUnit);
		Timeseries_request.setUseGMT(useGMT);

		marketData = invoke(marketData);
		index = invoke(index);
		riskFreeAsset = invoke(riskFreeAsset);
		
		TimeSeriesResponseModel response = new TimeSeriesResponseModel(marketData, index, riskFreeAsset);
		return response;
		
	}
	
	public String invoke(String EventSetID) throws RemoteException{
		
		Timeseries_request.setEventSetId(EventSetID);
		TimeseriesResponse response = null;
		
		try {
			response = stub.timeseries(Timeseries_request);
		} catch (RemoteException e) {
			throw e;
		}
		
		String statusmsg = response.getMessage();
		String status = response.getStatus();
		
		StringBuffer sb = new StringBuffer();
		String[] result = null;
		if (status.equals("ok")){
			return statusmsg;
		}
		if (status.equals("er")){
			return "error";
		}
		return "";
	}
	
	private TimeSeriesResponseModel dummy(TimeSeriesModel request){
		System.out.println(request.getRequest().getMarketDataEventSetID());
		System.out.println(request.getRequest().getIndexEventSetID());
		System.out.println(request.getRequest().getRiskFreeAssetEventSetID());
		
		TimeSeriesResponseModel response = new TimeSeriesResponseModel();
		
		UUID uuid = UUID.randomUUID();
		String MarketDataEventSetID = "rdth-" + uuid + ".csv";
		response.setMarketDataEventSetID(MarketDataEventSetID);
		
		uuid = UUID.randomUUID();
		String IndexEventSetID = "rdth-" + uuid + ".csv";
		response.setIndexEventSetID(IndexEventSetID);
		
		uuid = UUID.randomUUID();
		String RiskFreeAssetEventSetID = "rdth-" + uuid + ".csv";
		response.setRiskFreeAssetEventSetID(RiskFreeAssetEventSetID);
		
		return response;
		
	}
}
