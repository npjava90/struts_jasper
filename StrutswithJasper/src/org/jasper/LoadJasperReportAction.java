package org.jasper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

public class LoadJasperReportAction extends StrutsResultSupport {

	protected String parameters;
	protected String rptFormat;

	protected String studId;

	public String getRptFormat() {
		return rptFormat;
	}

	public void setRptFormat(String rptFormat) {
		this.rptFormat = rptFormat;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String findParam(String key, Map<String, Object> params) {
		Object obj = params.get(key);
		if (obj != null) {
			String[] values = (String[]) obj;
			return values.length > 0 ? values[0] : null;
		}
		return (String) obj;
	}

	@Override
	protected void doExecute(String arg0, ActionInvocation arg1)
			throws Exception {
		ActionContext context = ActionContext.getContext();
		Map<String, Object> params = context.getParameters();
		String studentId = findParam("studId", params);

		try {
			String reportFileName = "StudentReport";
			Connection conn = null;

			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/studentdb",
							"root", "admin!@#");

			HttpServletRequest request = ServletActionContext.getRequest();		

			ValueStack stack = arg1.getStack();
		

			parameters = conditionalParse(parameters, arg1);
			String contextPath = request.getSession().getServletContext()
					.getRealPath("/report/" + reportFileName + ".jrxml");
			try {

				InputStream input = new FileInputStream(new File(contextPath));

				System.out.println("Context Path is  : =" + contextPath);
				JasperDesign jasperDesign = JRXmlLoader.load(input);

				System.out.println("Compiling Report Designs");

				JasperReport jasperReport = JasperCompileManager
						.compileReport(jasperDesign);

				jasperReport.setProperty(
						"net.sf.jasperreports.query.executer.factory.plsql",
						"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");

				JRProperties.setProperty(
						JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX
								+ "plsql",
						"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
				System.out.println("Creating JasperPrint Object");
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("studId", studentId);

				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, conn);

				int pages = jasperPrint.getPages().size();
				if (pages != 0) {
					File f = new File("d:\\struts1.pdf");
					f.createNewFile();

					// Exporting the report
					OutputStream output = new FileOutputStream(f);

					JasperExportManager.exportReportToPdfStream(jasperPrint,
							output);

					System.out.println("Report Generation Complete");
				} else {
					System.out.println(" NO DATA FOUND TO EXPORT .....");
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}