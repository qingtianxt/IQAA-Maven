<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>问题详情</title>
        <link rel="stylesheet" type="text/css" href="../static/layui/css/layui.css"/>
        <style>
        	.layui-card-header{
        		font-size:20px;
        		font-weight: bold;
        		color: #dde333;
        	}
        </style>
    </head>
    <body>
    	<div class="layui-main">
    		<div style="height:20px"></div>
    		<div class="layui-card">
    			<div class="layui-card-header">详情</div>
    			<div class="layui-card-body">
	    			<a href="${pageContext.request.contextPath}/kb/backselectquestion.action" title="返回" class="layui-btn layui-btn-primary">返回</a>
	    			<a href="javascript:;" onclick="save(this,<s:property value="id"/>)"  title="删除"  class="layui-btn layui-btn-primary">删除</a>
		    		<table class="layui-table" style="width:677px">
						<colgroup>
							<col width="150"/>
							<col width="300"/>
						</colgroup>
						<thead>
							<tr>
								<th>标题</th>
								<th>内容</th>
							</tr>									
						</thead>
						<tbody>
							<tr>
								<td>问题描述</td>
								<td id="question-content"><s:property value="standardQuestion.content"/></td>
							</tr>
							<tr>
								<td>主题</td>
								<td id="question-theme"><s:property value="subject"/></td>
							</tr>
							<tr>
								<td>标准答案</td>
								<td id="answer-content"><s:property value="content"/></td>
							</tr>
						</tbody>
						<tfoot>
							<!-- <tr style="border: none;">
								<td style="border: none;"></td>
								<td style="border: none;"> 
									<a href='' class="layui-btn layui-btn-primary">返回</a>
								</td>
							</tr> -->
						</tfoot>
					</table>
    			</div>
    		</div>
    	</div> 
 	</body>
 	<script src="../static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
 	<script src="../static/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
 	<script src="../static/js/jquery.form.js" type="text/javascript" charset="utf-8"></script>
 	<script src="../static/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript">
 		var contextPath = "${pageContext.request.contextPath}";
 		function save(obj,id){
 			var question = $("#question-content").html();
 			var theme = $("#question-theme").html();
 			var answer = $("#answer-content").html();
 			$.ajax({
 				url:contextPath+"/kb/delKb",
 				method:"post",
 				data:{
					id:id, 					
 					content:answer,
 					subject:theme,
 					'standardQuestion.content':question,
 					'standardQuestion.subject':theme
 				},
 				beforeSend:function(){
 					console.log("beforeSend");
 					this.index = layer.load(1);
 				},
 				success:function(res){
 					layer.open({
 						content:res,
 						btn:["确定"],
 						btn1:function(index,layero){
 							layer.close(index);
 							if(res=="删除成功")
		 						location.href = contextPath+"/kb/backselectquestion.action"
 						}
 					})
 				},
 				error:function(error,textStatus){
 					layer.open({
 						content:textStatus
 					})
 				},
 				complete:function(){
 					layer.close(this.index);
 				}
 			})
 		}
 		
 	</script>
</html>