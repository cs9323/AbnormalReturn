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
import component.abnormalreturns.AbnormalReturns;
import component.download.Download;
import component.merge.Merge;
import component.merge.MergeServiceStub.ArrayOfString;
import component.merge.MergeServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeSeriesBuilding;
import component.timeseriesbuilding.TimeseriesServiceStub.TimeRange;
import component.trthimport.TRTHImport;

@SuppressWarnings({"unused"})
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
    
    @Override
    public String invoke(String messageType, String RIC, 
                          String startTime, String endTime,
                          String startDate, String endDate,
                          String useGMT, String useCorporateActions) 
                                  throws ComputingServiceException{
        try {
            
            // TODO Here insert code for TRTHImport
            
            TRTHImportModel trthImportRequest = 
                constructTRTHImportRequest(messageType, RIC, startTime, endTime, 
                                 startDate, endDate, useGMT, useCorporateActions);
            
            System.out.println("Invoking TRTHImport component");
            TRTHImportResponseModel trthImportResponse = invokeTRTHImport(trthImportRequest);
            System.out.println("Back from TRTHImport component");
            
            // TODO Here insert code for TimeSeriesBuilding
            
            TimeSeriesModel timeSeriesModel = constructTimeSeriesModel(trthImportResponse);
            System.out.println("Invoking TimeSeriesBuilding Component");
            TimeSeriesResponseModel timeSeriesResponse = invokeTimeSeriesBuilding(timeSeriesModel);     
            System.out.println("Back from TimeSeriesBuilding Component");
            
            // TODO Here insert code for Merge
            
            // TODO Here insert code for AbnormalReturn
            AbnormalreturnModel abnormalreturnModel=constructAbnormalreturnModel(null);
            System.out.println("Invoking AbnormalReturn Component");
            AbnormalreturnResponseModel abnormalreturnResponse=invokeAbnormalReturn(abnormalreturnModel);
            System.out.println("Back from AbnormalReturn Component");
            
            // TODO Here insert code for Download 
            DownloadModel downloadRequest = constructDownloadRequest(null);
                       
            System.out.println("Invoking Download component...");
            DownloadResponseModel downloadResponse = invokeDownload(downloadRequest);
            System.out.println("Back from Download component.");
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new ComputingServiceException(e);
        }
        return "";
    }
    
    private TRTHImportModel 
        constructTRTHImportRequest(String messageType, String RIC, 
                         String startTime, String endTime,
                         String startDate, String endDate,
                         String useGMT, String useCorporateActions) 
                                 {
        
        return new TRTHImportModel(messageType, 
                                    RIC, 
                                    startTime, 
                                    endTime, 
                                    startDate, 
                                    endDate, 
                                    useGMT, 
                                    useCorporateActions);
    }
    
    private TimeSeriesModel constructTimeSeriesModel(
    		TRTHImportResponseModel request 
    		) {
    	
    	component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader header = new component.timeseriesbuilding.TimeseriesServiceStub.CredentialsHeader();
        header.setPassword("");
        header.setUsername("");
        
        component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString measures = new component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString();
        measures.addString("spot");
        
        component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString rics = new component.timeseriesbuilding.TimeseriesServiceStub.ArrayOfString();
        rics.addString("ALL");
        
        String intervalDuration = "0";
        String intervalUnit = "spot";
        
        String useGMT = "1";
        
		return new TimeSeriesModel(header, request, measures, rics, intervalDuration, intervalUnit, useGMT);	
    }
    
    private MergeModel constructMergeModel(CredentialsHeader cre, String eId1, String eId2, 
			ArrayOfString mEv1, ArrayOfString mEv2, String option, String preEv2){
    	return new MergeModel(cre, eId1, eId2, mEv1, mEv2, option, preEv2);
    }
    
    private AbnormalreturnModel constructAbnormalreturnModel(MergeResponseModel request){
    	component.abnormalreturns.AbnormalreturnServiceStub.CredentialsHeader ch = new component.abnormalreturns.AbnormalreturnServiceStub.CredentialsHeader();
    	ch.setPassword("");
    	ch.setUsername("");
    	String modelType="marketmodel";
    	int dayWindow=3;
    	return new AbnormalreturnModel(ch,request.getMessage(),modelType,dayWindow);
    }
    
    private DownloadModel constructDownloadRequest(DownloadModel request) {
		String eventSetId = "eventSetId";
		return new DownloadModel(eventSetId);
	}

    
    private TRTHImportResponseModel invokeTRTHImport(TRTHImportModel request) throws Exception {
        return trth.ImportMarketData(request);
    }
   
    private TimeSeriesResponseModel invokeTimeSeriesBuilding(TimeSeriesModel request) throws Exception{
    	return timeSeriesBuilding.returnStatusMsg(request);
    }
    
    private MergeResponseModel invokeMerge(MergeModel request) throws Exception {
    	return merge.MergeData(request);
    }
    
    private AbnormalreturnResponseModel invokeAbnormalReturn(AbnormalreturnModel request) throws Exception{
    	return abnormalReturns.calculate(request);
    }
    
    private DownloadResponseModel invokeDownload(DownloadModel request) throws Exception{
    	return download.returnResult(request);
    }

}
