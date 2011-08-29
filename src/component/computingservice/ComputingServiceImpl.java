package component.computingservice;

import java.text.SimpleDateFormat;

import org.osoa.sca.annotations.Reference;

import util.models.MergeModel;
import util.models.TRTHImportModel;
import component.merge.Merge;
import component.merge.MergeServiceStub.ArrayOfString;
import component.merge.MergeServiceStub.CredentialsHeader;
import component.trthimport.TRTHImport;

public class ComputingServiceImpl implements ComputingService {

    @Reference
    public TRTHImport trth;
    public Merge merge;
    
    @Override
    public String invoke(String messageType, String RIC, 
                          String startTime, String endTime,
                          String startDate, String endDate,
                          String useGMT, String useCorporateActions) 
                                  throws Exception {
        
        
        TRTHImportModel request = 
                constructTRTHImportRequest(messageType, RIC, startTime, endTime, 
                                 startDate, endDate, useGMT, useCorporateActions);
        invokeTRTHImport(request);
        return null;
    }
    
    private TRTHImportModel 
        constructTRTHImportRequest(String messageType, String RIC, 
                         String startTime, String endTime,
                         String startDate, String endDate,
                         String useGMT, String useCorporateActions) 
                                 throws Exception{
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss:sss");
        
        return new TRTHImportModel(Integer.parseInt(messageType), 
                                    RIC, 
                                    timeFormatter.parse(startTime), 
                                    timeFormatter.parse(endTime), 
                                    dateFormatter.parse(startDate), 
                                    dateFormatter.parse(endDate), 
                                    Boolean.parseBoolean(useGMT), 
                                    Boolean.parseBoolean(useCorporateActions));
    }
    
    private MergeModel constructMergeModel(CredentialsHeader cre, String eId1, String eId2, 
			ArrayOfString mEv1, ArrayOfString mEv2, String option, String preEv2){
    	return new MergeModel(cre, eId1, eId2, mEv1, mEv2, option, preEv2);
    }
    
    private String invokeTRTHImport(TRTHImportModel request) throws Exception {
        return trth.ImportMarketData(request);
    }
    
    private String invokeMerge(MergeModel request) throws Exception {
    	return merge.MergeData(request);
    }

}
