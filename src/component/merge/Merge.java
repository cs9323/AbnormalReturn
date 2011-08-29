package component.merge;

import org.osoa.sca.annotations.Remotable;

import util.models.MergeModel;
import util.models.MergeResponseModel;

@Remotable
public interface Merge {
	public MergeResponseModel MergeData(MergeModel request) throws Exception;
}
