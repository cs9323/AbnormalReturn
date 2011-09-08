package component.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.apache.axis2.AxisFault;

import component.download.DowloadEventSetStub.DownloadBinaryFile;
import component.download.DowloadEventSetStub.DownloadBinaryFileResponse;

import util.models.DownloadModel;
import util.models.DownloadResponseModel;

public class DownloadImpl implements Download {

	@Override
	public DownloadResponseModel returnResult(DownloadModel request)
			throws Exception {
		//return generateReturnResult(request);
		return generateDummyresult(request);
	}

	private DownloadResponseModel generateReturnResult(DownloadModel request) throws Exception {
		// TODO Auto-generated method stub
		String data = request.geteventSetId();
		
		String wURL = "http://soc-server2.cse.unsw.edu.au:14080/axis2/services/DowloadEventSet?wsdl";
		
		DowloadEventSetStub stub = new DowloadEventSetStub(wURL);
		
		DownloadBinaryFile download_request = new DownloadBinaryFile();
		
		download_request.setData(data);
		
		DownloadBinaryFileResponse resp = stub.downloadBinaryFile(download_request);
		File result = (File) resp.get_return();
		DownloadResponseModel response_model = new DownloadResponseModel();
		response_model.set_return(result);
		//resp.set_return(download_request);
		return response_model;
		
	}
	
	private DownloadResponseModel generateDummyresult(DownloadModel request) throws Exception{
		String eventSetId = request.geteventSetId();
		
		System.out.println(eventSetId);
		
	
		String downloadFile = System.getProperty("catalina.home")+"/webapps/ROOT/"+eventSetId+".csv";
		try {
			PrintWriter out = new PrintWriter(new File(downloadFile));
			out.println("The eventSetId for this file is: "+eventSetId);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new DownloadResponseModel();
	}

}
