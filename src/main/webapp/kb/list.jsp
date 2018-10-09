<%@page contentType="text/html"%><%@ taglib prefix="u" tagdir="/WEB-INF/tags"%><%@ taglib prefix="s" uri="/struts-tags" %>
<%@page pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>知识库</title>
        <link rel="stylesheet" type="text/css" href="../static/layui/css/layui.css"/>
        <link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/static/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/static/css/bootstrap-theme.min.css">
        <style type="text/css">
        	.layui-icon{
        		font-size: 25px; color: #1E9FFF;
        	}
        	label.layui-form-label{
				box-sizing: content-box;
			}
			#addKbDiv{
        		width:600px;
        		margin: 10px auto;
        	}
        	#list-table{
        		table-layout: fixed;
        	}
        	#list-table tbody tr td{
        		text-overflow: ellipsis;
        		overflow: hidden;
        		white-space: nowrap;
        	}
        </style>
    <body>
    	<div class="layui-main">
    	
    		<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief" >
    			<ul class="layui-tab-title">
    				<li class="layui-this" onclick="reloadList()">知识库列表</li>
    				<li id='add-tab-title'>添加</li>
    				<li style="display:none" id="detail-tab-title"></li>
    			</ul>
    			<div class="layui-tab-content">
    				<div class="layui-tab-item layui-show">
    					<div id="kb-div">
    						<jsp:include page="page_fregement.jsp" />
    					</div>
    					
    				</div>
    				<div class="layui-tab-item">
    					<div id="addKbDiv">
    						<jsp:include page="add_fregement.jsp"/>
    					</div>
    				</div>
    				<div class="layui-tab-item" id="detail-tab-item">
    					<table class="layui-table">
    						<colgroup>
    							<col width="300"/>
    							<col />
    						</colgroup>
    						<tr>
    							<th>标题</th>
    							<th>内容</th>
    						</tr>
    						<tr>
    							<td>问题描述</td>
    							<td id="question-content"></td>
    						</tr>
    						<tr>
    							<td>主题</td>
    							<td id="question-theme"></td>
    						</tr>
    						<tr>
    							<td>标准答案</td>
    							<td id="answer-content"></td>
    						</tr>
    					</table>
    				</div>
    			</div>
    		</div>
    	</div>
    	<%-- <s:debug></s:debug> --%>
 	</body>
 	<script src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
 	<script src="${pageContext.request.contextPath}/static/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript" type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath }/static/js/bootstrap.min.js"></script>
 	<script src="${pageContext.request.contextPath}/static/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
 	<script src="${pageContext.request.contextPath}/static/js/jquery.form.js" type="text/javascript" charset="utf-8"></script>
 	
 	<script>
 		var contextPath = "${pageContext.request.contextPath}";
 		var currentPage = "${requestScope.requestAnswer.currentPage}";
 		var pageSize = "${requestScope.requestAnswer.pageSize}";
 		var totalCount = "${requestScope.requestAnswer.totalCount}";
 		var totalPage = "${requestScope.requestAnswer.totalPage}";
 		var answer = "${sessionScope.sessionAnswerQueryInfo.answer}";
 		var question = "${sessionScope.sessionAnswerQueryInfo.question}";
 		var start = "${sessionScope.sessionAnswerQueryInfo.start}";
 		var end = "${sessionScope.sessionAnswerQueryInfo.end}";
 		layui.use(["laypage","laydate","element"],function(){
 			var laypage = layui.laypage;
 			laypage.render({
 				elem:"page-nav",
 				curr:currentPage,
 				count: totalCount,
				limit: pageSize,
				limits: [5, 10, 15, 20],
				layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
				jump: function(obj) {
					console.log(obj);
					if(!this.notFirst){
						this.notFirst = true;
					}else{
						var cur = obj.curr
						location.href = contextPath+"/kb/listKb?query.currentPage="+cur
								+"&query.pageSize="+obj.limit
								+"&query.answer="+answer
								+"&query.question="+question
								+"&query.start="+start
								+"&query.end="+end;
					}
				}
 			})
 			var laydate = layui.laydate;

			//常规用法
			laydate.render({
				elem : '#start'
			});
			laydate.render({
				elem : '#end'
			});
			
 		})
 		 

 		var needReload = false;
 		$("#addKbForm").validate({
 			rules:{
 				"standardQuestion.content":{
 					required:true
 				},
 				"subject":{
 					required:true
 				},
 				content:{
 					required:true
 				}
 			}
 			,messages:{
 				"standardQuestion.content":{
 					required:"请输入问题"
 				},
 				"subject":{
 					required:"请输入主题"
 				},
 				content:{
 					required:"请输入答案"
 				}
 			},
 			errorPlacement:function(error,elemnt){
 				error.css("color","red").insertAfter(elemnt)
 			}
 			,submitHandler:function(){
 				$("#addKbForm").ajaxForm({
	 					beforeSubmit:function(){
	 						console.log("提交")
	 						this.index = layer.load();
	 					},
	 					success:function(){
	 						console.log(arguments[1]);
	 						needReload = true;
	 						layer.open({
	 							content:arguments[0]
	 						});
	 						layer.close(this.index);
	 					},
	 					resetForm:true
	 				});
 			}
 		})
 		function reloadList(){
 			if(needReload){
 				location.reload();
 			}
 		}
 		
 		function del(id){
 			layer.open({
 				content:"您确定要删除此条信息吗？"
 				,title:"删除确认"
 				,btn:['确定','取消']
 				,btn1:function(index,layero){
 					layer.close(index);
 					layer.load();
 					location.href = contextPath+"/kb/deleteKb?id="+id;
 				},
 				btn2:function(index,layero){
 					layer.close(index);
 				}
 			})
 		}
 		function detail(index,id){
 			location.href = contextPath+"/kb/getKb?id="+id;
 			return;
 		}
 		function edit(index,id){
 			location.href =contextPath + "/kb/toUpdateKb?id="+id;
 		}
 		
 		function clearform(){
 			document.form1['query.start'].value=""
 			document.form1['query.end'].value=""
 			document.form1['query.question'].value=""
 		}
 	</script>
	<u:alert msg="${ requestScope.msg }"></u:alert>
</html>