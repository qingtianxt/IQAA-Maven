<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
	<s:form action="uploadDocument" namespace="/document" method="post" enctype="multipart/form-data">
		<s:file name="upload" label="前选择文件" size="20"></s:file>
		<s:submit value="上传" align="center"></s:submit>
	</s:form>
	<s:actionmessage/>
	<s:fielderror fieldName="uploadMsg"></s:fielderror>
</body>
</html>