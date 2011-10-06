package component.timeseriesbuilding;

import org.osoa.sca.annotations.Remotable;

import util.exceptions.ComputingServiceException;
import util.exceptions.ServiceDownException;
import util.models.TimeSeriesModel;
import util.models.TimeSeriesResponseModel;

@Remotable
public interface TimeSeriesBuilding {
	public TimeSeriesResponseModel returnStatusMsg(TimeSeriesModel request) throws ComputingServiceException, ServiceDownException;
}
