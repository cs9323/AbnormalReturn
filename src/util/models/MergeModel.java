package util.models;

import java.io.Serializable;

import component.merge.MergeServiceStub.ArrayOfString;
import component.merge.MergeServiceStub.CredentialsHeader;

public class MergeModel implements Serializable{
	
    private static final long serialVersionUID = 1L;
    private String MarketDataEventSetID;
	private String IndexEventSetID;
	private String RiskFreeAssetEventSetID;
	private CredentialsHeader Credentials;
	private ArrayOfString Merge1MeasuresEv1;
	private ArrayOfString Merge1MeasuresEv2;
	private ArrayOfString Merge2MeasuresEv1;
	private ArrayOfString Merge2MeasuresEv2;
	private String Merge1Option;
	private String Merge2Option;
	private String Merge1PrefixEv2;
	private String Merge2PrefixEv2;
	
	public MergeModel() {}
	
	public MergeModel(String marketDataEventSetID, String indexEventSetID, String riskFreeAssetEventSetID, 
			CredentialsHeader credentials, ArrayOfString merge1MeasuresEv1, ArrayOfString merge1MeasuresEv2, 
			ArrayOfString merge2MeasuresEv1, ArrayOfString merge2MeasuresEv2, String merge1Option, 
			String merge2Option, String merge1PrefixEv2, String merge2PrefixEv2) {
		this.setMarketDataEventSetID(marketDataEventSetID);
		this.setIndexEventSetID(indexEventSetID);
		this.setRiskFreeAssetEventSetID(riskFreeAssetEventSetID);
		this.setCredentials(credentials);
		this.setMerge1MeasuresEv1(merge1MeasuresEv1);
		this.setMerge1MeasuresEv2(merge1MeasuresEv2);
		this.setMerge2MeasuresEv1(merge2MeasuresEv1);
		this.setMerge2MeasuresEv2(merge2MeasuresEv2);
		this.setMerge1Option(merge1Option);
		this.setMerge2Option(merge2Option);
		this.setMerge1PrefixEv2(merge1PrefixEv2);
		this.setMerge2PrefixEv2(merge2PrefixEv2);
	}
	
	public void setMarketDataEventSetID(String marketDataEventSetID) {
		MarketDataEventSetID = marketDataEventSetID;
	}
	
	public String getMarketDataEventSetID() {
		return MarketDataEventSetID;
	}
	
	public void setIndexEventSetID(String indexEventSetID) {
		IndexEventSetID = indexEventSetID;
	}
	
	public String getIndexEventSetID() {
		return IndexEventSetID;
	}
	
	public void setRiskFreeAssetEventSetID(String riskFreeAssetEventSetID) {
		RiskFreeAssetEventSetID = riskFreeAssetEventSetID;
	}
	
	public String getRiskFreeAssetEventSetID() {
		return RiskFreeAssetEventSetID;
	}
	
	public void setCredentials(CredentialsHeader credentials) {
		Credentials = credentials;
	}
	
	public CredentialsHeader getCredentials() {
		return Credentials;
	}

	public void setMerge1MeasuresEv1(ArrayOfString merge1MeasuresEv1) {
		Merge1MeasuresEv1 = merge1MeasuresEv1;
	}

	public ArrayOfString getMerge1MeasuresEv1() {
		return Merge1MeasuresEv1;
	}

	public void setMerge1MeasuresEv2(ArrayOfString merge1MeasuresEv2) {
		Merge1MeasuresEv2 = merge1MeasuresEv2;
	}

	public ArrayOfString getMerge1MeasuresEv2() {
		return Merge1MeasuresEv2;
	}

	public void setMerge2MeasuresEv1(ArrayOfString merge2MeasuresEv1) {
		Merge2MeasuresEv1 = merge2MeasuresEv1;
	}

	public ArrayOfString getMerge2MeasuresEv1() {
		return Merge2MeasuresEv1;
	}

	public void setMerge2MeasuresEv2(ArrayOfString merge2MeasuresEv2) {
		Merge2MeasuresEv2 = merge2MeasuresEv2;
	}

	public ArrayOfString getMerge2MeasuresEv2() {
		return Merge2MeasuresEv2;
	}

	public void setMerge1Option(String merge1Option) {
		Merge1Option = merge1Option;
	}

	public String getMerge1Option() {
		return Merge1Option;
	}

	public void setMerge2Option(String merge2Option) {
		Merge2Option = merge2Option;
	}

	public String getMerge2Option() {
		return Merge2Option;
	}

	public void setMerge1PrefixEv2(String merge1PrefixEv2) {
		Merge1PrefixEv2 = merge1PrefixEv2;
	}

	public String getMerge1PrefixEv2() {
		return Merge1PrefixEv2;
	}

	public void setMerge2PrefixEv2(String merge2PrefixEv2) {
		Merge2PrefixEv2 = merge2PrefixEv2;
	}

	public String getMerge2PrefixEv2() {
		return Merge2PrefixEv2;
	}
}
