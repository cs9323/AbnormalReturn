package component.computingservice;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.osoa.sca.annotations.Reference;
import org.python.modules.newmodule;

import util.exceptions.ComputingServiceException;
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

    @Override
    public String invoke(String messageType, String RIC, String startTime,
            String endTime, String startDate, String endDate, String useGMT,
            String useCorporateActions) throws ComputingServiceException {
    	
    	String invokeResponse = "";

        // TODO Here insert code for TRTHImport
    	TRTHImportModel trthImportRequest = null;
    	TRTHImportResponseModel trthImportResponse = null;
    	try{
	        trthImportRequest = constructTRTHImportRequest(
	                messageType, RIC, startTime, endTime, startDate, endDate,
	                useGMT, useCorporateActions);
	        System.out.println("Invoking TRTHImport component");
	        trthImportResponse = invokeTRTHImport(trthImportRequest);
	        System.out.println("Back from TRTHImport component");
    	}catch(ComputingServiceException e){
    		invokeResponse += "\"ComputingServiceException\":\"" + e.getFaultMessage() + "\"";
        	System.out.println(invokeResponse);
        	return invokeResponse;
    	}
        
        //download & visualization of import data
        try{
	        String indexEventSetId = trthImportResponse.getIndexEventSetID();
	        invokeResponse += doDownloadVisualization("TRTHIndex", indexEventSetId);
	        String marketDataEventSetId = trthImportResponse.getMarketDataEventSetID();
	        invokeResponse += doDownloadVisualization("TRTHMarket", marketDataEventSetId);
	        String riskFreeAssetEventSetID = trthImportResponse.getRiskFreeAssetEventSetID();
	        invokeResponse += doDownloadVisualization("TRTHRisk", riskFreeAssetEventSetID);
        }catch(ComputingServiceException e){
        	invokeResponse += "\"ComputingServiceException\":\"" + e.getFaultMessage() + "\"";
        	System.out.println(invokeResponse);
        	return invokeResponse;
        }
        
        // TODO Here insert code for TimeSeriesBuilding
        TimeSeriesModel timeSeriesModel = null;
        TimeSeriesResponseModel timeSeriesResponse = null;
        try{
	        timeSeriesModel = constructTimeSeriesModel(trthImportResponse);
	        System.out.println("Invoking TimeSeriesBuilding Component");
	        timeSeriesResponse = invokeTimeSeriesBuilding(timeSeriesModel);
	        System.out.println("Back from TimeSeriesBuilding Component");
        }catch(ComputingServiceException e){
        	invokeResponse += "\"ComputingServiceException\":\"" + e.getFaultMessage() + "\"";
        	System.out.println(invokeResponse);
        	return invokeResponse;
        }
        
        //download & visualization of timeSeriesBuilding data
        try{
        	invokeResponse += doDownloadVisualization("TSBIndex", timeSeriesResponse.getIndexEventSetID());
	        invokeResponse += doDownloadVisualization("TSBMarket", timeSeriesResponse.getMarketDataEventSetID());
	        invokeResponse += doDownloadVisualization("TSBRisk", timeSeriesResponse.getRiskFreeAssetEventSetID());
        }catch(ComputingServiceException e){
        	invokeResponse += "\"ComputingServiceException\":\"" + e.getFaultMessage() + "\"";
        	System.out.println(invokeResponse);
        	return invokeResponse;
        }
        
        // TODO Here insert code for Merge
        MergeModel mergeModel = null;
        MergeResponseModel mergeResponse = null;
        try{
	        mergeModel = constructMergeModel(timeSeriesResponse);
	        System.out.println("Invoking Merge Component");
	        mergeResponse = invokeMerge(mergeModel);
	        System.out.println("Back from Merge Component");
        }catch(ComputingServiceException e){
        	invokeResponse += "\"ComputingServiceException\":\"" + e.getFaultMessage() + "\"";
        	System.out.println(invokeResponse);
        	return invokeResponse;
        }
        
        //download & visualization of merge data
        try{
	        if(mergeModel.getRiskFreeAssetEventSetID() == null)
	        	invokeResponse += doDownloadVisualization("MergeOnce", mergeResponse.getResultEventSetID());
	        else
	        	invokeResponse += doDownloadVisualization("MergeTwice", mergeResponse.getResultEventSetID());
        }catch(ComputingServiceException e){
        	invokeResponse += "\"ComputingServiceException\":\"" + e.getFaultMessage() + "\"";
        	System.out.println(invokeResponse);
        	return invokeResponse;
        }
        // TODO Here insert code for AbnormalReturn
//            AbnormalreturnModel abnormalreturnModel = constructAbnormalreturnModel(mergeResponse);
//            System.out.println("Invoking AbnormalReturn Component");
//            AbnormalreturnResponseModel abnormalreturnResponse = invokeAbnormalReturn(abnormalreturnModel);
//            System.out.println("Back from AbnormalReturn Component");
//
//            // TODO Here insert code for Download
//            DownloadModel downloadRequest = constructDownloadRequest(abnormalreturnResponse.getEventSetID());
//            System.out.println("Invoking Download component...");
//            DownloadResponseModel downloadResponse = invokeDownload(downloadRequest);
//            System.out.println("Back from Download component.");
        
        invokeResponse = invokeResponse.substring(0, invokeResponse.length() - 1);
        System.out.println("Response: " + invokeResponse);
        return invokeResponse;

    }

    private TRTHImportModel constructTRTHImportRequest(String messageType,
            String RIC, String startTime, String endTime, String startDate,
            String endDate, String useGMT, String useCorporateActions) {

        return new TRTHImportModel(messageType, RIC, startTime, endTime,
                startDate, endDate, useGMT, useCorporateActions);
    }

    private TimeSeriesModel constructTimeSeriesModel(
            TRTHImportResponseModel request) {

        component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader header = new component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader();
        header.setPassword("");
        header.setUsername("");

        component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString measures = new component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString();
        measures.addString("ClsPrice");

        component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString rics = new component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString();
        rics.addString("ALL");

        String intervalDuration = "0";
        String intervalUnit = "spot";

        String useGMT = "true";

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
        Merge1MeasuresEv1.addString("ClsPrice");
        ArrayOfString Merge1MeasuresEv2 = new ArrayOfString();
        Merge1MeasuresEv2.addString("ClsPrice");
        ArrayOfString Merge2MeasuresEv1 = new ArrayOfString();
        Merge2MeasuresEv1.addString("ClsPrice");
        Merge2MeasuresEv1.addString("Index_ClsPrice");
        ArrayOfString Merge2MeasuresEv2 = new ArrayOfString();
        Merge2MeasuresEv2.addString("ClsPrice");
        String Merge1Option = "ByFile1ClosestBeforeOrEqualToEndInterval";
        String Merge2Option = "ByFile1ClosestBeforeOrEqualToEndInterval";
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
        String modelType = "marketmodel";
        int dayWindow = 2;
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
    	columns.addString("ClsPrice");
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
            throws ComputingServiceException {
        return trth.ImportMarketData(request);
    }

    private TimeSeriesResponseModel invokeTimeSeriesBuilding(
            TimeSeriesModel request) throws ComputingServiceException{
        return timeSeriesBuilding.returnStatusMsg(request);
    }

    private MergeResponseModel invokeMerge(MergeModel request) throws ComputingServiceException {
        return merge.MergeData(request);
    }

    private AbnormalreturnResponseModel invokeAbnormalReturn(
            AbnormalreturnModel request) throws ComputingServiceException {
        return abnormalReturns.calculate(request);
    }

    private DownloadResponseModel invokeDownload(DownloadModel request)
            throws ComputingServiceException {
        return download.returnResult(request);
    }
    
    private VisualizationResponseModel invokeVisualization(VisualizationModel request)
            throws ComputingServiceException{
    	return visualization.VisualizeData(request);
    }
    
    private String doDownloadVisualization(String namePrefix, String eventSetId) throws ComputingServiceException {
    	String results = "";
    	if(eventSetId != null) {
            DownloadModel DownloadReq = constructDownloadRequest(eventSetId);
            DownloadResponseModel DownloadRsp = invokeDownload(DownloadReq);
            results += "\"" + namePrefix + "DownloadURL\":\"" + DownloadRsp.getReturnFile().getAbsolutePath() + "\",";
            
       		if(!namePrefix.contains("TRTH")){
	            VisualizationModel VisualizeReq = constructVisualizationModel(eventSetId);
	            VisualizationResponseModel VisualizeRsp = invokeVisualization(VisualizeReq);
	            results += "\"" + namePrefix + "VisualizationURL\":\"" + VisualizeRsp.getVisualizationURL() + "\",";
       		}
    	}
    	return results;
    }
  
}
