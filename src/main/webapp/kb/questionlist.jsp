<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>处理结果</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/layui/css/layui.css"/>
<style type="text/css">
	th,td{
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	.layui-icon {
		font-size: 25px; color: #1E9FFF;
	}
	.layui-card-header{
		font-size:30px;
		color:999234;
	}
</style>
</head>
<body>
	<div class="layui-main">
		<div class="layui-card" style="margin-top:20px">
			<div class="layui-card-header">分析结果</div>
			<div class="layui-card-body">
				<table class="layui-table" id="questionlisttable" style="table-layout: fixed">
		   			<colgroup>
		   				<col width="100"><col width="250" /><col width="200" /><col width="350" /><col />
		   			</colgroup>
		   			<thead>
		    			<tr>
		    				<th>序号</th><th>标准问题</th><th>主题</th><th>标准答案</th><th>
		    					<a href="javascript:;" ></a>操作
		    				</th>
		    			</tr>
		   			</thead>
		   			<tbody id="regular-div">
		   				<c:forEach items="${ requestScope.questions }" var="question" varStatus="status">
		   					<tr class='qa-row'>
		   						<td>${status.index+1}</td>
		   						<td>${question.content}</td>
		   						<td>${question.subject}</td>
		   						<td>${question.standardAnswer.content}</td>
		   						<td>
		   							<a title="删除" href="javascript:;"
		   								onclick="shanchu(this,'${question.standardAnswer.id}')"><i class='layui-icon' 
		   								>&#xe640;</i></a>
									<a title="查看" href="${pageContext.request.contextPath}/kb
									/showquestion.action?question.standardAnswer.id=${question.standardAnswer.id}">
										<i class="layui-icon">&#xe621;</i>
									</a>
		   						</td>
		   					</tr>
		   				</c:forEach>
		   				<tr>
		   					<td colspan="5" >
		   						<s:actionerror/>
		   					</td>
		   				</tr>
		   			</tbody>
		   			<tfoot><tr><td colspan="5" id="pagenation1"></td></tr></tfoot>
		   		</table>
			</div>
		</div>
		
	</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/layui/layui.all.js" ></script>
<script type="text/javascript">
	var contextPath = "${pageContext.request.contextPath}";
	var option = null;
	var laypage;
	layui.use(['laypage'],function(){
		laypage = layui.laypage; 
		option = {
				elem:"pagenation1",
 				curr:1,
 				count:$(".qa-row").length,
				limit: 5,
				limits: [5, 10, 15, 20],
				layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
				jump:function(obj){
					var $trows= $(".qa-row");
					$trows.hide();
					var start = (obj.curr-1) * obj.limit;
					var end = obj.curr * obj.limit;
					for(var i = 0 ; i < obj.count; i++){
						if(i >= start && i < end && i < obj.count ){
							$($trows[i]).show();
						}
					}
				}
		};
		laypage.render(option);
	});
	
	
	function shanchu(obj,index,showResult){
		var table = document.getElementById("questionlisttable");
		console.log("删除");
		
		$.ajax({
			url:contextPath+"/kb/delKb.action",
			dataType:"html",
			method:"post",
			data:{'id':index}, 
			beforeSend:function(){
				console.log("beforeSend")
				this.index = layer.load(1);
			},
			success:function(res){
				layer.open({
					content:res
					,btn:["确定"]
					,btn1:function(index,layero){
						layer.close(index);
						if(res=="删除成功")
						{
							$(obj).parent().parent().remove();
							option.count = $(".qa-row").length;
							laypage.render(option);
						}
					}
				});
			},
			error:function(error,textStatus){
				layer.open({
					content : textStatus
				})
				console.log(error);
				console.log(textStatus)
			},
			complete:function(){
				layer.close(this.index)
			}
		})
	}
	<%--
	function saveAll(obj){
		var $btns = $("a:contains('保存')");
		for(var i = 0; i < $btns.length; i++){ 
			$btns[i].click();
			//alert($btns[i].onclick)
		}
	}
	function saveAll(){
		alert('该功能尚未实现')
	} 
	function storageAtLocal(){
		var text = $("#regular-div").html();
		var time = new Date();
		time.setDate(time.getDate()+24*3600);
		document.cookie = "localquestions="+escape(text)+";expires="+time.toGMTString()+";path=content.jsp";
	}
	function toShow(obj,index){
		var row = obj.parentNode.parentNode;
		var question = row.cells[1].innerHTML;
		var subject = row.cells[2].innerHTML;
		var answer = row.cells[3].innerHTML;
		var url = "content.jsp?question="+question+"&subject="+subject+"&answer="+answer;
		location.assign(url)
		$.post(url,{
			question:question,
			subject:subject,
			answer:answer
		},function(res){
			$(html).html(res);
		})
	}--%>
</script>
</html>