package component.computingservice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.apache.axis2.AxisFault;
import org.json.JSONObject;
import org.osoa.sca.annotations.Reference;

import util.exceptions.ComputingServiceException;
import util.exceptions.ServiceDownException;
import util.models.AbnormalreturnModel;
import util.models.AbnormalreturnResponseModel;
import util.models.DownloadModel;
import util.models.DownloadResponseModel;
import util.models.MergeModel;
import util.models.MergeResponseModel;
import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;
import util.models.TimeSeriesModel;
import util.models.TimeSeriesResponseModel;
import util.models.VisualizationModel;
import util.models.VisualizationResponseModel;
import component.abnormalreturns.AbnormalReturns;
import component.download.Download;
import component.merge.Merge;
import component.merge.MergeServiceStub.ArrayOfString;
import component.merge.MergeServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeSeriesBuilding;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeRange;
import component.trthimport.TRTHImport;
import component.trthimport.TRTHImportCacheServiceStub.DateRange;
import component.visualization.Visualization;
import component.visualization.VisualizationServiceStub;

@SuppressWarnings({ "unused" })
public class ComputingServiceImpl implements ComputingService {

    @Reference
    public TRTHImport trth;

    @Reference
    public TimeSeriesBuilding timeSeriesBuilding;

    @Reference
    public Merge merge;

    @Reference
    public AbnormalReturns abnormalReturns;

    @Reference
    public Download download;
    
    @Reference
    public Visualization visualization;

    private String _messageType = null;
    private String _marketDataRIC = null;
    private String _indexRIC = null;
    private String _riskRIC = null;
    private String _startDate = null;
    private String _endDate = null;
    private String _startTime = null;
    private String _endTime = null;
    private String _useGMT = null;
    private String _useCorporationAction = null;
    private String _Measurement = null;
    private String _intervalUnit = null;
    private String _intervalDuration = null;
    private String _mergeOption = null;
    private String _daysWindow = null;
    private String _modelType = null;
    
    private JSONObject invokeResponse;
    
    @Override
    public String invoke(String messageType, String marketDataRIC, String indexRIC, String riskRIC,
    		String startDate, String endDate, String startTime, String endTime,
            String useGMT, String useCorporationAction, 
            String Measurement, String intervalUnit, String intervalDuration,
            String mergeOption,
            String daysWindow, String modelType) 
    		throws ComputingServiceException {
    	
    	// Set all the global variables
    	_messageType = messageType;
    	_marketDataRIC = marketDataRIC;
    	_indexRIC = indexRIC;
    	_riskRIC = riskRIC;
    	_startDate = startDate;
    	_endDate = endDate;
    	_startTime = startTime;
    	_endTime = endTime;
    	_useGMT = useGMT;
    	_useCorporationAction = useCorporationAction;
    	_Measurement = Measurement;
    	_intervalUnit = intervalUnit;
    	_intervalDuration = intervalDuration;
    	_mergeOption = mergeOption;
    	_daysWindow = daysWindow;
    	_modelType = modelType;
    	
    	invokeResponse = new JSONObject();

        // TODO Here insert code for TRTHImport
    	TRTHImportModel trthImportRequest = null;
    	TRTHImportResponseModel trthImportResponse = null;
    	try{
	        trthImportRequest = constructTRTHImportRequest(
	                messageType, marketDataRIC, indexRIC, riskRIC, startTime, endTime, 
	                startDate, endDate, useGMT, useCorporationAction);
	        System.out.println("Invoking TRTHImport component");
	        trthImportResponse = invokeTRTHImport(trthImportRequest);
	        System.out.println("Back from TRTHImport component");
    	}catch(ComputingServiceException e){
    		invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
    	}catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
    	
    	if(!trthImportResponse.getMarketDataStatus()) {
    		invokeResponse.put("TRTHImportMarketException", trthImportResponse.getMarketDataEventSetID());
        	System.out.println(invokeResponse.toString());
        	trthImportResponse.setMarketDataEventSetID(null);
    	}
    	
    	if(!trthImportResponse.getIndexStatus()) {
    		invokeResponse.put("TRTHImportIndexException", trthImportResponse.getIndexEventSetID());
        	System.out.println(invokeResponse.toString());
        	trthImportResponse.setIndexEventSetID(null);
    	}
    	
    	if(!trthImportResponse.getRiskFreeAssetStatus()) {
    		invokeResponse.put("TRTHImportRiskException", trthImportResponse.getRiskFreeAssetEventSetID());
        	System.out.println(invokeResponse.toString());
        	trthImportResponse.setRiskFreeAssetEventSetID(null);
    	}
    	
        //download & visualization of import data
        try{
	        String indexEventSetId = trthImportResponse.getIndexEventSetID();
	        doDownloadVisualization("TRTHIndex", indexEventSetId);
	        String marketDataEventSetId = trthImportResponse.getMarketDataEventSetID();
	        doDownloadVisualization("TRTHMarket", marketDataEventSetId);
	        String riskFreeAssetEventSetID = trthImportResponse.getRiskFreeAssetEventSetID();
	        doDownloadVisualization("TRTHRisk", riskFreeAssetEventSetID);
        }catch(ComputingServiceException e){
        	invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
        }catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
        
        // TODO Here insert code for TimeSeriesBuilding
        if(_Measurement == null || _Measurement.length() == 0 || _intervalDuration == null 
        		|| _intervalDuration.length() == 0|| _intervalUnit == null || _intervalUnit.length() == 0)
        	return invokeResponse.toString();
        TimeSeriesModel timeSeriesModel = null;
        TimeSeriesResponseModel timeSeriesResponse = null;
        try{
	        timeSeriesModel = constructTimeSeriesModel(trthImportResponse);
	        System.out.println("Invoking TimeSeriesBuilding Component");
	        timeSeriesResponse = invokeTimeSeriesBuilding(timeSeriesModel);
	        System.out.println("Back from TimeSeriesBuilding Component");
        }catch(ComputingServiceException e){
        	invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
        }catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
        
        //download & visualization of timeSeriesBuilding data
        try{
        	doDownloadVisualization("TSBIndex", timeSeriesResponse.getIndexEventSetID());
	        doDownloadVisualization("TSBMarket", timeSeriesResponse.getMarketDataEventSetID());
	        doDownloadVisualization("TSBRisk", timeSeriesResponse.getRiskFreeAssetEventSetID());
        }catch(ComputingServiceException e){
        	invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
        }catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
        
        // TODO Here insert code for Merge
        if(_mergeOption == null || _mergeOption.length() == 0)
        	return invokeResponse.toString();
        MergeModel mergeModel = null;
        MergeResponseModel mergeResponse = null;
        try{
	        mergeModel = constructMergeModel(timeSeriesResponse);
	        System.out.println("Invoking Merge Component");
	        mergeResponse = invokeMerge(mergeModel);
	        System.out.println("Back from Merge Component");
        }catch(ComputingServiceException e){
        	invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
        }catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
        //download & visualization of merge data
        try{
	        if(mergeModel.getRiskFreeAssetEventSetID() == null)
	        	doDownloadVisualization("MergeOnce", mergeResponse.getResultEventSetID());
	        else
	        	doDownloadVisualization("MergeTwice", mergeResponse.getResultEventSetID());
        }catch(ComputingServiceException e){
        	invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
        }catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
        
        // TODO Here insert code for AbnormalReturn
        if(_daysWindow == null || _daysWindow.length() == 0 || _modelType == null || _modelType.length() == 0)
        	return invokeResponse.toString();
        AbnormalreturnModel abnormalreturnModel = null;
        AbnormalreturnResponseModel abnormalreturnResponse = null;
        try{
	        abnormalreturnModel = constructAbnormalreturnModel(mergeResponse);
	        System.out.println("Invoking AbnormalReturn Component");
	        abnormalreturnResponse = invokeAbnormalReturn(abnormalreturnModel);
	        System.out.println("Back from AbnormalReturn Component");
        }catch(ComputingServiceException e){
        	invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
        }catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
        //download & visualization of abnormalReturn
        try{
	       	doDownloadVisualization("ABN", abnormalreturnResponse.getEventSetID());
        }catch(ComputingServiceException e){
        	invokeResponse.put("ComputingServiceException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
        }catch (ServiceDownException e) {
    		WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ServiceDownException", e.getFaultMessage());
        	System.out.println(invokeResponse.toString());
        	return invokeResponse.toString();
		}
        
        
        //final return result
        System.out.println("Response: " + invokeResponse.toString());
        return invokeResponse.toString();

    }

    private TRTHImportModel constructTRTHImportRequest(String messageType,
            String marketDataRIC, String indexRIC, String riskRIC, String startTime, String endTime, 
            String startDate, String endDate, String useGMT, String useCorporationAction) {
    	DateRange dateRange = new DateRange();
    	dateRange.setStart(startDate);
    	dateRange.setEnd(endDate);
    	component.trthimport.TRTHImportCacheServiceStub.TimeRange timeRange 
    					= new component.trthimport.TRTHImportCacheServiceStub.TimeRange();
    	timeRange.setStart(startTime);
    	timeRange.setEnd(endTime);

        return new TRTHImportModel(messageType, marketDataRIC, indexRIC, riskRIC, dateRange,
                timeRange, useGMT, useCorporationAction);
    }

    private TimeSeriesModel constructTimeSeriesModel(
            TRTHImportResponseModel request) {

        component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader header = new component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader();
        header.setPassword("");
        header.setUsername("");

        component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString measures = new component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString();
        measures.addString(_Measurement);

        component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString rics = new component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString();
        rics.addString("ALL");

        String intervalDuration = _intervalDuration;
        String intervalUnit = _intervalUnit;

        String useGMT = _useGMT;

        return new TimeSeriesModel(header, request, measures, rics,
                intervalDuration, intervalUnit, useGMT);
    }

    private MergeModel constructMergeModel(TimeSeriesResponseModel request) {
        String MarketDataEventSetID = request.getMarketDataEventSetID();
        String IndexEventSetID = request.getIndexEventSetID();
        String RiskFreeAssetEventSetID = request.getRiskFreeAssetEventSetID();
        CredentialsHeader Credentials = new CredentialsHeader();
        Credentials.setPassword("");
        Credentials.setUsername("");
        ArrayOfString Merge1MeasuresEv1 = new ArrayOfString();
        Merge1MeasuresEv1.addString(_Measurement);
        ArrayOfString Merge1MeasuresEv2 = new ArrayOfString();
        Merge1MeasuresEv2.addString(_Measurement);
        ArrayOfString Merge2MeasuresEv1 = new ArrayOfString();
        Merge2MeasuresEv1.addString(_Measurement);
        Merge2MeasuresEv1.addString("Index_" + _Measurement);
        ArrayOfString Merge2MeasuresEv2 = new ArrayOfString();
        Merge2MeasuresEv2.addString(_Measurement);
        String Merge1Option = _mergeOption;
        String Merge2Option = _mergeOption;
        String Merge1PrefixEv2 = "Index";
        String Merge2PrefixEv2 = "RiskFreeAssetReturn";

        return new MergeModel(MarketDataEventSetID, IndexEventSetID,
                RiskFreeAssetEventSetID, Credentials, Merge1MeasuresEv1,
                Merge1MeasuresEv2, Merge2MeasuresEv1, Merge2MeasuresEv2,
                Merge1Option, Merge2Option, Merge1PrefixEv2, Merge2PrefixEv2);
    }

    private AbnormalreturnModel constructAbnormalreturnModel(
            MergeResponseModel request) {
        component.abnormalreturns.AbnormalreturnServiceStub.CredentialsHeader ch = new component.abnormalreturns.AbnormalreturnServiceStub.CredentialsHeader();
        ch.setPassword("");
        ch.setUsername("");
        String modelType = _modelType;
        String dayWindow = _daysWindow;
        return new AbnormalreturnModel(ch, request.getResultEventSetID(),
                modelType, dayWindow);
    }
    

    private DownloadModel constructDownloadRequest(
            String eventSetId) {
        return new DownloadModel(eventSetId);
    }
    
    private VisualizationModel constructVisualizationModel(String eventSetId){
    	VisualizationModel request = new VisualizationModel();
    	String uri = "http://soc-server2.cse.unsw.edu.au:14080/";
    	VisualizationServiceStub.ArrayOfString columns = new VisualizationServiceStub.ArrayOfString();
    	columns.addString(_Measurement);
    	columns.addString("news_abnormal");
    	VisualizationServiceStub.CredentialsHeader header = new VisualizationServiceStub.CredentialsHeader();
    	header.setPassword("");
    	header.setUsername("");
    	request.setEventSetId(eventSetId);
    	request.setURI(uri);
    	request.setColumns(columns);
    	request.setCredentials(header);
    	return request;
    }

    private TRTHImportResponseModel invokeTRTHImport(TRTHImportModel request)
            throws ComputingServiceException, ServiceDownException {
        return trth.ImportMarketData(request);
    }

    private TimeSeriesResponseModel invokeTimeSeriesBuilding(
            TimeSeriesModel request) throws ComputingServiceException, ServiceDownException{
        return timeSeriesBuilding.returnStatusMsg(request);
    }

    private MergeResponseModel invokeMerge(MergeModel request) throws ComputingServiceException, ServiceDownException {
        return merge.MergeData(request);
    }

    private AbnormalreturnResponseModel invokeAbnormalReturn(
            AbnormalreturnModel request) throws ComputingServiceException, ServiceDownException {
        return abnormalReturns.calculate(request);
    }

    private DownloadResponseModel invokeDownload(DownloadModel request)
            throws ComputingServiceException, ServiceDownException {
        return download.returnResult(request);
    }
    
    private VisualizationResponseModel invokeVisualization(VisualizationModel request)
            throws ComputingServiceException, ServiceDownException{
    	return visualization.VisualizeData(request);
    }
    
    private void doDownloadVisualization(String namePrefix, String eventSetId) throws ComputingServiceException, ServiceDownException {
    	String results = "";
    	if(eventSetId != null) {
            DownloadModel DownloadReq = constructDownloadRequest(eventSetId);
            DownloadResponseModel DownloadRsp = invokeDownload(DownloadReq);
            invokeResponse.put(namePrefix + "DownloadURL", DownloadRsp.getEventSetId());
            
       		if(!namePrefix.contains("TRTH")){
	            VisualizationModel VisualizeReq = constructVisualizationModel(eventSetId);
	            VisualizationResponseModel VisualizeRsp = invokeVisualization(VisualizeReq);
	            invokeResponse.put(namePrefix + "VisualizationURL", VisualizeRsp.getVisualizationURL());
       		}
    	}
    }
    
    private void WriteExceptionLog(String ExpMessage) {
    	int ServiceNO = 0;
    	if (ExpMessage.contains("TRTHImport"))
    		ServiceNO = 1;
    	if (ExpMessage.contains("TimeSeries"))
    		ServiceNO = 2;
    	if (ExpMessage.contains("Merge"))
    		ServiceNO = 3;
    	if (ExpMessage.contains("AbnormalReturn"))
    		ServiceNO = 4;
    	if (ExpMessage.contains("Download"))
    		ServiceNO = 5;
    	if (ExpMessage.contains("Visualization"))
    		ServiceNO = 6;
    	String fileName = "/home/neil/9323/AbnormalReturnRecovery/ServicesStatus";
    	ArrayList<String> items = new ArrayList<String>();
    	try {
    	    FileReader fr = new FileReader(fileName);
    	    BufferedReader br = new BufferedReader(fr);
    	    String line = null;
    	    int count =0;
    	    while ((line = br.readLine()) != null) {
    	    	if (line.trim().length() == 0)
    	    		continue;
    	    	 count++;
    	    	 if (count == ServiceNO) {
    	    		 int spilt = line.indexOf(" ");
    	    		 line = line.substring(0, spilt) + " BAD";
    	    	 }
    	         items.add(line);
    	    }
    	    br.close();
    	    fr.close();
    	    FileWriter fw = new FileWriter(fileName, false);
    	    for (int i = 0; i < items.size(); ++i) {
    	    	fw.append(items.get(i));
    	    	if (i != items.size() - 1)
    	    		fw.append("\n");
    	    }
    	    fw.close();
    	}
    	catch (Exception e) {
			System.out.println("Writing ServicesStatus File Error:" + e.toString());
		}
    }
}
