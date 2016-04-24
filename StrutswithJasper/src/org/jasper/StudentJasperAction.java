package org.jasper;
import java.util.HashMap;
import java.util.Map;
import com.opensymphony.xwork2.ActionSupport;

public class StudentJasperAction extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Object> param=null;
		
	private String studId;
	private String rptFmt;
  
	public String getRptFmt() {
		return rptFmt;
	}

	public void setRptFmt(String rptFmt) {
		this.rptFmt = rptFmt;
	}

	

	

	public String getStudId() {
		return studId;
	}

	public void setStudId(String studId) {
		this.studId = studId;
	}

	public Map<String, Object> getParam() {
		return param;
	}


	public void setParam(Map<String, Object> param) {
		this.param = param;
	}


	public String execute() throws Exception {
    
        try {
    		param = new HashMap<String,Object>();
    		
    		param.put("id", new Integer(studId));
    		param.put("Title", "Student  Details");
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
 
        return SUCCESS;
    }
 
   
}