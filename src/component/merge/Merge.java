package component.merge;

import org.osoa.sca.annotations.Remotable;

import util.models.MergeModel;

@Remotable
public interface Merge {
	public String MergeData(MergeModel request) throws Exception;
}
