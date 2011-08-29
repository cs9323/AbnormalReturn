package component.abnormalreturns;

import org.osoa.sca.annotations.Remotable;

import util.models.AbnormalreturnModel;
import util.models.AbnormalreturnResponseModel;

@Remotable
public interface AbnormalReturns {
	public AbnormalreturnResponseModel calculate(AbnormalreturnModel inputs) throws Exception;
}
