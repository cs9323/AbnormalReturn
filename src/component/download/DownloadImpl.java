package component.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.io.FileUtils;

import component.download.DowloadEventSetStub.DownloadBinaryFile;
import component.download.DowloadEventSetStub.DownloadBinaryFileResponse;

import util.exceptions.ComputingServiceException;
import util.exceptions.ServiceDownException;
import util.models.DownloadModel;
import util.models.DownloadResponseModel;

public class DownloadImpl implements Download {

	@Override
	public DownloadResponseModel returnResult(DownloadModel request)
			throws ComputingServiceException, ServiceDownException {
		return generateReturnResult(request);
		//return generateDummyresult(request);
	}

	private DownloadResponseModel generateReturnResult(DownloadModel request) throws ComputingServiceException, ServiceDownException {
		// TODO Auto-generated method stub
		String data = request.geteventSetId();
		
		String wURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/DowloadEventSet?wsdl";
		
		String path = System.getProperty("java.io.tmpdir");
		
		DowloadEventSetStub stub = null;
		try {
            stub = new DowloadEventSetStub(wURL);
        } catch (AxisFault e) {
            // TODO Auto-generated catch block
            //throw new ComputingServiceException(e.getMessage());
        	throw new ServiceDownException("Download Service is Down.");
        }
		
		DownloadBinaryFile download_request = new DownloadBinaryFile();
		
		download_request.setData(data);
		
		DownloadBinaryFileResponse resp = null;
		
		try {
            resp = stub.downloadBinaryFile(download_request);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            //throw new ComputingServiceException(e.getMessage());
        	throw new ServiceDownException("Download Service is Down.");
        } catch (IOExceptionException e) {
            // TODO Auto-generated catch block
            throw new ComputingServiceException(e.getMessage());
        }
        
        DownloadResponseModel response_model = new DownloadResponseModel();
        if(path.contains("/temp")) {
			path = path.replace("/temp", "/webapps/ROOT/");
			File tempResult = (File) resp.get_return();
			String fileName = tempResult.getName();
			File result = new File(path + fileName);
			try {
				FileUtils.copyFile(tempResult, result);
			} catch (IOException e) {
				throw new ComputingServiceException(e.getMessage());
			}
			
			MessageContext mc = MessageContext.getCurrentMessageContext();
			HttpServletRequest req = (HttpServletRequest)mc.getProperty("transport.http.servletRequest");
			String url = req.getRequestURL().toString();
			url = url.substring(0, url.lastIndexOf('/'));
			url = url.substring(0, url.lastIndexOf('/'));
			url = url + "/" + result.getName();
			response_model.setEventSetId(url);
        }else {
        	File result = (File) resp.get_return();
        	try {
				FileUtils.copyFile(result, new File("/home/shifengming/tomcat6.0/webapps/ROOT/" + result.getName()));
			} catch (IOException e) {
				throw new ComputingServiceException(e.getMessage());
			}
			String url = "http://127.0.0.1:8080/";
			url = url + result.getName();
			response_model.setEventSetId(url);
        }
		
		
		
		System.out.println(response_model.getEventSetId());
		return response_model;
		
	}
	
	private DownloadResponseModel generateDummyresult(DownloadModel request) throws ComputingServiceException{
		String eventSetId = request.geteventSetId();
		
		System.out.println(eventSetId);
		
		//String filepath = System.getProperty("java.io.tmpdir");
	
		//String downloadFile = filepath+"/"+eventSetId;
		String downloadFile = "/home/shifengming/tomcat6.0/webapps/ROOT/outputCSV/" + eventSetId;
		File resFile = null;
		try {
			resFile = new File(downloadFile);
			PrintWriter out = new PrintWriter(resFile);
			out.println("This is the dummy output data of AbnormalReturn Service.");
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DownloadResponseModel resModel = new DownloadResponseModel();
		resModel.setEventSetId(resFile.getAbsolutePath());
		
		return resModel;
	}

}
