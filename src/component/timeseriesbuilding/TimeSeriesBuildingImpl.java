package component.timeseriesbuilding;

import java.rmi.RemoteException;

import component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString;
import component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeRange;
import component.timeseriesbuilding.TimeseriesServiceStub.Timeseries;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeseriesResponse;

import util.models.TRTHImportModel;
import util.models.TimeSeriesModel;
import util.models.TimeSeriesResponseModel;

public class TimeSeriesBuildingImpl implements TimeSeriesBuilding {
	
	@Override
	public TimeSeriesResponseModel returnStatusMsg(TimeSeriesModel request) throws Exception{
        return generateStatusMsg(request);
    }
	
	private TimeSeriesResponseModel generateStatusMsg(TimeSeriesModel request) throws Exception{
		CredentialsHeader header = request.getCredentialsHeader();
		String EventSetID = request.getEventSetID();
		ArrayOfString measures = request.getMeasures();
		ArrayOfString rics = request.getRics();
		TimeRange timeRange = request.getTimeRange();
		String intervalDuration = request.getIntervalDuration();
		String intervalUnit = request.getIntervalUnit();
		String useGMT = request.getUseGMT();
		
		String wURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/TimeseriesService?wsdl";
		TimeseriesServiceStub stub = new TimeseriesServiceStub(wURL);
		
		Timeseries Timeseries_request = new Timeseries();
		
		Timeseries_request.setCredentials(header);
		Timeseries_request.setEventSetId(EventSetID);
		Timeseries_request.setMeasures(measures);
		Timeseries_request.setRics(rics);
		Timeseries_request.setTime(timeRange);
		Timeseries_request.setTimeIntervalDuration(intervalDuration);
		Timeseries_request.setTimeIntervalUnit(intervalUnit);
		Timeseries_request.setUseGMT(useGMT);
		
		TimeseriesResponse response = null;
		
		try {
			response = stub.timeseries(Timeseries_request);
		} catch (RemoteException e) {
			throw e;
		}
		
		String status = response.getMessage();
		StringBuffer sb = new StringBuffer();
		String[] result = null;
		if (status.equals("ok")){
			sb.append(status).append(",").append(EventSetID);
			String temp = sb.toString();
			result = temp.split(",");
		}
		if (status.equals("er")){
			sb.append(status).append(",").append("error happens!");
			String temp = sb.toString();
			result = temp.split(",");
		}
		TimeSeriesResponseModel response_model = new TimeSeriesResponseModel();
		response_model.setResponseModel(result);
		return response_model;
	}
}
