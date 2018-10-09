<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>JSP Page</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/layui/css/layui.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap-theme.min.css" />
		<style>
			#regular-div tr td,#nlp-div tr td,#model-div tr td{
				text-overflow: ellipsis;
				overflow: hidden; 
				white-space: nowrap;
			}
			.layui-table{
				table-layout: fixed;
			}
		</style>
    </head>
    <body>
    	<div class="container" >
    	<div style="height:20px"></div>
    	<div class="layui-tab layui-tab-brief">
		  <ul class="layui-tab-title">
		    <li class="layui-this">文档内容</li>
		    <li onclick="extract('${param.id}','${pageContext.request.contextPath}/kb/selectquestion.action','#regular-div',this)">基于规则的分析</li>
		    <li onclick="extract('${param.id}','${pageContext.request.contextPath}/document/extractDocument.action','#nlp-div',this)">NLP分析</li>
		    <li onclick="modelselect('${param.id}',this)" class="">模型分析</li>
		  </ul>
		  <div class="layui-tab-content">
		    <div class="layui-tab-item layui-show">
		    	<div id="contentDiv">
   					<iframe id="children" name="Main" src="${ pageContext.request.contextPath }/document/displayDocument.action?id=${param.id}" 
						width="100%" height="500px"  frameborder="no" >
					</iframe>
   				</div>
		    </div>
		    <div id="" class="layui-tab-item">
			    <table class="layui-table" id="">
	    			<colgroup>
	    				<col width="100"><col width="250" /><col width="200" /><col width="350" /><col />
	    			</colgroup>
	    			<thead>
		    			<tr>
		    				<th>序号</th><th>标准问题</th><th>主题</th><th>标准答案</th><th>操作</th>
		    			</tr>
	    			</thead>
	    			<tbody id="regular-div">
	    				
	    			</tbody>
	    			<tfoot><tr><td colspan="5" id="pagenation1"></td></tr></tfoot>
	    		</table>
		    </div>
		    <div id="" class="layui-tab-item">
		    	<table class="layui-table" id="">
	    			<colgroup>
	    				<col width="100"><col width="250" /><col width="200" /><col width="350" /><col />
	    			</colgroup>
	    			<thead>
		    			<tr>
		    				<th>序号</th><th>标准问题</th><th>主题</th><th>标准答案</th><th>操作</th>
		    			</tr>
	    			</thead>
	    			<tbody id="nlp-div">
	    				
	    			</tbody>
	    			<tfoot><tr><td colspan="5" id="pagenation2"></td></tr></tfoot>
	    		</table>
			</div>
		    <div id="" class="layui-tab-item">
		    	<table class="layui-table" id="">
	    			<colgroup>
	    				<col width="100"><col width="250" /><col width="200" /><col width="350" /><col />
	    			</colgroup>
	    			<thead>
		    			<tr>
		    				<th>序号</th><th>标准问题</th><th>主题</th><th>标准答案</th><th>操作</th>
		    			</tr>
	    			</thead>
	    			<tbody id="model-div">
	    				
	    			</tbody>
	    			<tfoot><tr><td colspan="5" id="pagenation3"></td></tr></tfoot>
	    		</table>
		    </div>
		  </div>
		</div>
    	</div>
 	</body>
 	<script src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
 	<script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript" src="${pageContext.request.contextPath}/static/layui/layui.all.js" ></script>
 	<script type="text/javascript">
 		/* var content = "${ docContent }";
 		$("#contentDiv").text(content); */
 		var contextPath = "${pageContext.request.contextPath}";
 		var docId = "${param.id}";
 		var h = (window.innerHeight - 120) + "px";
 	</script>
 	<script type="text/javascript" src="${ pageContext.request.contextPath }/static/js/my/showDetail.js"></script>
</html>