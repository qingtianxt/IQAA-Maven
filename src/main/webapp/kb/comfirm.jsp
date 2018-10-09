<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>确认界面</title>
        <link rel="stylesheet" type="text/css" href="../static/layui/css/layui.css"/>
         <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap-theme.min.css">
    </head>
    <body>
    	<div class="layui-main">
    	<s:if test="#session.answeres != null">
    		<table class="layui-table" id="confirmTab">
    			<colgroup>
    				<col width="100"><col width="150" /><col width="100" /><col width="250" /><col width="100" /><col />
    			</colgroup>
    			<thead>
	    			<tr>
	    				<th>序号</th><th>标准问题</th><th>主题</th><th>标准答案</th><th>操作</th>
	    			</tr>
    			</thead>
    			<tbody>
    				<s:iterator value="#session.answeres" var="answer" status="st">
    				<tr style="display: none">
    					<td><s:property value="#st.count"/></td><!-- 序号 -->
    					<td>
    						<s:property value="#answer.standardQuestion.content"/>
						</td>
    					<td>
    					</td>
    					<td>
    						<s:property value="#answer.content"/>
    					</td>
    					<td>
    						<button class="layui-btn" type="button" 
    							onclick="saveCurrItem(<s:property value="#st.count"/>)" >
    							保存</button>
    					</td>
    				</tr>
    				</s:iterator>
    			</tbody>
    		</table>
    		<div class="layui-main" style="text-align: center;" id="pages">
    			
    		</div>
    	</s:if>
    	<s:else>
    		<u:alert msg="暂无任何信息"></u:alert>
    	</s:else>
    	</div>
<%--     	<s:debug></s:debug> --%>
 	</body>
 	<script src="../static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
 	<script src="../static/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
 	<script src="../static/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript">
 		var contextPath = "${pageContext.request.contextPath}"
 		var totalCount = "${size}";
 		var currentPage = 1;
 		var pageSize = 5;
 		var totalPage = parseInt(totalCount) / pageSize;
 		layui.use("laypage",function(){
 			var laypage = layui.laypage;
		  
		  //执行一个laypage实例
		  laypage.render({
		    elem: 'pages' //注意，这里的 test1 是 ID，不用加 # 号
		    ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
		    ,count: totalCount //数据总数，从服务端得到
		    ,curr:currentPage
		    ,limit:pageSize
		    ,limits:[5,10,15,20]
		    ,jump:function(obj){
		    	//var $table = $("#confirmTab");
		    	var table = document.getElementById("confirmTab");
		    	for( var row in table.rows){
		    		$(table.rows[row]).hide();
		    	} 
		    	$(table.rows[0]).show();
		    	var start = (obj.curr - 1) * obj.limit;
		    	for(var i = 1; i <= obj.limit;i++ ){
		    		if(start + i > obj.count){
		    			break;
		    		}
		    		$(table.rows[start + i]).show();
		    	}
		    }
		  });
 		})
 		
 		function saveCurrItem(index){
 			var table = document.getElementById("confirmTab");
 			var rows = table.rows[index];
 			$.ajax({
 				url:contextPath+"/kb/addKb"
 				,method:"POST"
 				,data:{
 					content:rows.cells[3].innerHTML
 					,subject:rows.cells[2].innerHTML
 					,'standardQuestion.content':rows.cells[1].innerHTML
 					,'standardQuestion.subject':rows.cells[2].innerHTML
 				},
 				success:function(){
 					console.log(arguments[1]);
					var btn = rows.cells[4].getElementsByTagName("button")[0];
					btn.onclick= null;
					btn.innerHTML = "已保存";
					layer.open({
						content:arguments[0]
					});
 				},error:function(){
 					layer.open({
 						content:'上传错误！'
 					})
 				},beforeSend:function(){
 					console.log("提交")
					//this.index = layer.load();
 				},complete:function(){
 					//layer.close(this.index);
 				}
 			})
 		}
 	</script>
</html>