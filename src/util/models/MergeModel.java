package util.models;

import component.merge.MergeServiceStub.ArrayOfString;
import component.merge.MergeServiceStub.CredentialsHeader;

public class MergeModel {
	private CredentialsHeader Credentials;
	private String EventSet1Id;
	private String EventSet2Id;
	private ArrayOfString MeasuresEv1;
	private ArrayOfString MeasuresEv2;
	private String MergeOption;
	private String PrefixEv2;
	
	public MergeModel(){}
	
	public MergeModel(CredentialsHeader cre, String eId1, String eId2, 
			ArrayOfString mEv1, ArrayOfString mEv2, String option, String preEv2){
		this.Credentials = cre;
		this.EventSet1Id = eId1;
		this.EventSet2Id = eId2;
		this.MeasuresEv1 = mEv1;
		this.MeasuresEv2 = mEv2;
		this.MergeOption = option;
		this.PrefixEv2 = preEv2;
	}
	
	public void setCredentials(CredentialsHeader credentials) {
		Credentials = credentials;
	}
	
	public CredentialsHeader getCredentials() {
		return Credentials;
	}
	
	public void setEventSet1Id(String eventSet1Id) {
		EventSet1Id = eventSet1Id;
	}
	
	public String getEventSet1Id() {
		return EventSet1Id;
	}
	
	public void setEventSet2Id(String eventSet2Id) {
		EventSet2Id = eventSet2Id;
	}
	
	public String getEventSet2Id() {
		return EventSet2Id;
	}
	
	public void setMeasuresEv1(ArrayOfString measuresEv1) {
		MeasuresEv1 = measuresEv1;
	}
	
	public ArrayOfString getMeasuresEv1() {
		return MeasuresEv1;
	}
	
	public void setMeasuresEv2(ArrayOfString measuresEv2) {
		MeasuresEv2 = measuresEv2;
	}
	
	public ArrayOfString getMeasuresEv2() {
		return MeasuresEv2;
	}
	
	public void setMergeOption(String mergeOption) {
		MergeOption = mergeOption;
	}
	
	public String getMergeOption() {
		return MergeOption;
	}
	
	public void setPrefixEv2(String prefixEv2) {
		PrefixEv2 = prefixEv2;
	}
	
	public String getPrefixEv2() {
		return PrefixEv2;
	}
}
