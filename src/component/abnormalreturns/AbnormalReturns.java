package component.abnormalreturns;

import org.osoa.sca.annotations.Remotable;

import util.exceptions.ComputingServiceException;
import util.exceptions.ServiceDownException;
import util.models.AbnormalreturnModel;
import util.models.AbnormalreturnResponseModel;

@Remotable
public interface AbnormalReturns {
	public AbnormalreturnResponseModel calculate(AbnormalreturnModel inputs) throws ComputingServiceException, ServiceDownException;
}
