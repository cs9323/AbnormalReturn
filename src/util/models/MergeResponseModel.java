package util.models;

public class MergeResponseModel {
	
	private String ResultEventSetID;
	private String Status;
	
	public MergeResponseModel() {}
	
	public MergeResponseModel(String result){
		this.ResultEventSetID = result;
	}

	public void setResultEventSetID(String resultEventSetID) {
		ResultEventSetID = resultEventSetID;
	}

	public String getResultEventSetID() {
		return ResultEventSetID;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getStatus() {
		return Status;
	}
	
	
	/*private String Message;
	private String Status;
	
	public MergeResponseModel(){}
	
	public MergeResponseModel(String m, String s){
		this.Message = m;
		this.Status = s;
	}
	
	public void setMessage(String message) {
		Message = message;
	}
	
	public String getMessage() {
		return Message;
	}
	
	public void setStatus(String status) {
		Status = status;
	}
	
	public String getStatus() {
		return Status;
	}*/
	
}
