package entity;

import java.io.Serializable;

/**
* @author YiChao
* @version 创建时间：2019年3月1日 下午5:37:58
* 说明:自定义响应结构
*/
public class UResult implements Serializable{

	private Boolean success;	//是否操作成功
	private String msg;	//提示消息
	
	public UResult(Boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
