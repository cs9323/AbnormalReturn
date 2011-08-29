package component.computingservice;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.osoa.sca.annotations.Reference;

import util.models.MergeModel;
import util.models.TRTHImportModel;
import util.models.TRTHImportResponseModel;
import component.merge.Merge;
import component.merge.MergeServiceStub.ArrayOfString;
import component.merge.MergeServiceStub.CredentialsHeader;
import component.timeseriesbuilding.TimeSeriesBuilding;
import component.trthimport.TRTHImport;

public class ComputingServiceImpl implements ComputingService {

    @Reference
    public TRTHImport trth;
    
    @Reference
    public TimeSeriesBuilding timeSeriesBuilding;
    
    @Reference
    public Merge merge;
    
    @Override
    public String invoke(String messageType, String RIC, 
                          String startTime, String endTime,
                          String startDate, String endDate,
                          String useGMT, String useCorporateActions) 
                                  throws AxisFault, RemoteException {
        
        TRTHImportModel trthImportRequest = 
                constructTRTHImportRequest(messageType, RIC, startTime, endTime, 
                                 startDate, endDate, useGMT, useCorporateActions);
        
        TRTHImportResponseModel trthImportResponse = invokeTRTHImport(trthImportRequest);
        return trthImportResponse.getEventSetID();
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
    
    private MergeModel constructMergeModel(CredentialsHeader cre, String eId1, String eId2, 
			ArrayOfString mEv1, ArrayOfString mEv2, String option, String preEv2){
    	return new MergeModel(cre, eId1, eId2, mEv1, mEv2, option, preEv2);
    }
    
    private TRTHImportResponseModel invokeTRTHImport(TRTHImportModel request) throws AxisFault, RemoteException {
        return trth.ImportMarketData(request);
    }
    
    private String invokeMerge(MergeModel request) throws Exception {
    	return merge.MergeData(request);
    }

}
