<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Jasper Report Example</title>

</head>
<body>

<s:actionerror/>

<s:form name="reportForm" action="studentJasperCall.action" metod="post" >

<TABLE id="reptbl" width="400px" border="2">
  <TR>
  <td> 
	Enter Student id : <s:textfield name="studId" label="Stock" /> <s:submit  value="Generate Student Report" type="button"/>
 </td>
 <td>
 <s:radio label="format" name="rptFmt" list="#{'pdf':'PDF'}" value="pdf" />
 </td>
	
</TR>
 
</TABLE>	

</s:form>

</body>
</html>