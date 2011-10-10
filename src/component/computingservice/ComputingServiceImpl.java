package component.computingservice;

import org.apache.axis2.context.MessageContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.AxisFault;
import org.apache.commons.io.FileUtils;
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
        
        //analyze the abnormal return data
        try{
        	String analysisEventSetId = doAbnormalAnalysis(abnormalreturnResponse.getEventSetID());
        	invokeResponse.put("AnalysisResult", analysisEventSetId);
        }catch(ComputingServiceException e) {
        	WriteExceptionLog(e.getFaultMessage());
    		invokeResponse.put("ComputingServiceException", e.getFaultMessage());
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
    
	public static String doAbnormalAnalysis(String file) throws ComputingServiceException {
		String eventSetId = null;
		try {
			String path = System.getProperty("java.io.tmpdir");
			ArrayList<ABData> abnList = new ArrayList<ABData>();
			FileReader fr = new FileReader(path + "/" + file);
			BufferedReader bfr = new BufferedReader(fr);
			bfr.readLine();
			String temp = bfr.readLine();
			while(temp != null) {
				ABData data = new ABData();
				temp = temp.replace(", ", ",");
				String[] fields = temp.split(",");
				if(fields != null)
					data.setSecurity(fields[0]);
				if(fields.length > 0) {
					Calendar date = Calendar.getInstance();
					DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
					Date d = formatter.parse(fields[1]);
					date.set(d.getYear(), d.getMonth(), d.getDate());
					data.setDateOfTrade(date);
				}
				if(fields.length > 3)
					data.setClsPrice(Double.parseDouble(fields[3]));
				if(fields.length > 8) {
					if(fields[8].equals("Infinity"))
						data.setVariance(Double.MAX_VALUE);
					else
						data.setVariance(Double.parseDouble(fields[8]));
				}
				if(fields.length > 9) {
					double num = Double.parseDouble(fields[9]);
					if(num == 1)
						data.setIs_abnormal(true);
					else
						data.setIs_abnormal(false);
				}
				if(fields.length > 10) 
					data.setNews_abnormal(fields[10]);
				abnList.add(data);
				temp = bfr.readLine();
			}
			
			ABData firstData = abnList.get(0);
			int curMonth = firstData.getDateOfTrade().get(firstData.getDateOfTrade().MONTH);
			int curYear = firstData.getDateOfTrade().get(firstData.getDateOfTrade().YEAR);
			ArrayList<ABData> itemsList = new ArrayList<ABData>();
			String id = file.substring(4, file.length() - 4);
			File output = File.createTempFile("analysis-", ".csv");
			FileWriter fw = new FileWriter(output);
			fw.append("Security, Month, #Trades, #Abnormal, Max_ClsPrice, Min_ClsPrice, Max_variance, Is Abnormal, Max_variance_news_abnormal\n");
			for(int i = 0; i < abnList.size(); i++) {
				ABData data = abnList.get(i);
				if(curMonth == data.getDateOfTrade().get(data.getDateOfTrade().MONTH)
						&& curYear == data.getDateOfTrade().get(data.getDateOfTrade().YEAR)) {
					itemsList.add(data);
				}
				else {
					if(itemsList.size() > 0)
						AnalyzeList(itemsList, fw);
					curMonth = data.getDateOfTrade().get(
							data.getDateOfTrade().MONTH);
					curYear = data.getDateOfTrade().get(
							data.getDateOfTrade().YEAR);
					i--;
				}
			}
			if (itemsList.size() != 0)
				AnalyzeList(itemsList, fw);
			fw.close();
			
			if(path.contains("/temp")) {
				path = path.replace("/temp", "/webapps/ROOT/");
				String fileName = output.getName();
				File result = new File(path + fileName);
				try {
					FileUtils.copyFile(output, result);
				} catch (IOException e) {
					throw new ComputingServiceException(e.getMessage());
				}
				
				MessageContext mc = MessageContext.getCurrentMessageContext();
				HttpServletRequest req = (HttpServletRequest)mc.getProperty("transport.http.servletRequest");
				String url = req.getRequestURL().toString();
				url = url.substring(0, url.lastIndexOf('/'));
				url = url.substring(0, url.lastIndexOf('/'));
				url = url + "/" + result.getName();
				eventSetId = url;
	        }
			else {
	        	try {
					FileUtils.copyFile(output, new File("/home/shifengming/tomcat6.0/webapps/ROOT/" + output.getName()));
				} catch (IOException e) {
					throw new ComputingServiceException(e.getMessage());
				}
				String url = "http://127.0.0.1:8080/";
				url = url + output.getName();
				eventSetId = url;
	        }
			
		} catch (Exception e) {
			throw new ComputingServiceException(e.getMessage());
		}
		
		System.out.println("AnalysisResult: " + eventSetId);
		return eventSetId;
	}
    
	public static void AnalyzeList(ArrayList<ABData> itemsList, Writer write) throws IOException {
		String Security = itemsList.get(0).getSecurity();
		Calendar month = itemsList.get(0).getDateOfTrade();
		int numOfTrades = itemsList.size();
		int numOfAbnormal = 0;
		double max_ClsPrice = itemsList.get(0).getClsPrice();
		double min_ClsPrice = itemsList.get(0).getClsPrice();
		double max_variance = 0;
		boolean isAbnormal = false;
		String newsAbnormal = null;
		int index = 0;
		
		for (int i = 0; i < itemsList.size(); ++i) {
			ABData item = itemsList.get(i);
			if(max_ClsPrice < item.getClsPrice())
				max_ClsPrice = item.getClsPrice();
			if(min_ClsPrice > item.getClsPrice())
				min_ClsPrice = item.getClsPrice();
			if(max_variance < item.getVariance()) {
				max_variance = item.getVariance();
				index = i;
			}
			if(item.isIs_abnormal())
				numOfAbnormal++;
		}
		isAbnormal = itemsList.get(index).isIs_abnormal();
		newsAbnormal = itemsList.get(index).getNews_abnormal();
		
		if(max_variance == Double.MAX_VALUE);
			
		String output = Security + ", " +
					String.valueOf(month.get(month.YEAR) + 1900) + "-" +
					String.valueOf(month.get(month.MONTH) + 1) + ", " +
					numOfTrades + ", " +
					numOfAbnormal + ", " +
					max_ClsPrice + ", " +
					min_ClsPrice + ", ";
		if(max_variance == Double.MAX_VALUE)
			output = output + "Infinity, ";
		else if(max_variance != 0)
			output = output + max_variance + ", ";
		else
			output = output + ", ";
		if(max_variance != 0)
			if(isAbnormal)
				output = output + 1 + ", ";
			else 
				output = output + 0 + ", ";
		else 
			output = output + ", ";
		if(newsAbnormal == null)
			output = output + "\n";
		else
			output = output + newsAbnormal + "\n";
		
		write.append(output);
		itemsList.clear();
	}
	
	public static class ABData {
		private String Security;
		private Calendar DateOfTrade;
		private double ClsPrice;
		private double Variance;
		private boolean Is_abnormal;
		private String news_abnormal;
		
		public ABData() {
			this.Security = null;
			this.DateOfTrade = null;
			this.ClsPrice = 0;
			this.Variance = 0;
			this.Is_abnormal = false;
			this.news_abnormal = null;
		}
		
		public void setSecurity(String security) {
			Security = security;
		}
		
		public String getSecurity() {
			return Security;
		}
		
		public void setDateOfTrade(Calendar dateOfTrade) {
			DateOfTrade = dateOfTrade;
		}
		
		public Calendar getDateOfTrade() {
			return DateOfTrade;
		}
		
		public void setClsPrice(double clsPrice) {
			ClsPrice = clsPrice;
		}
		
		public double getClsPrice() {
			return ClsPrice;
		}
		
		public void setVariance(double variance) {
			Variance = variance;
		}
		
		public double getVariance() {
			return Variance;
		}
		
		public void setIs_abnormal(boolean is_abnormal) {
			Is_abnormal = is_abnormal;
		}
		
		public boolean isIs_abnormal() {
			return Is_abnormal;
		}
		
		public void setNews_abnormal(String news_abnormal) {
			this.news_abnormal = news_abnormal;
		}
		
		public String getNews_abnormal() {
			return news_abnormal;
		}
	}
}
