package component.download;

import org.osoa.sca.annotations.Remotable;

import util.exceptions.ComputingServiceException;
import util.models.DownloadModel;
import util.models.DownloadResponseModel;

@Remotable
public interface Download {
	public DownloadResponseModel returnResult(DownloadModel request) throws ComputingServiceException;
}
