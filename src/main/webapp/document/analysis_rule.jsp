<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>规则管理</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/layui/css/layui.css"/>
        <style type="text/css">
        	th,td{
        		white-space: nowrap;
        		overflow: hidden;
        		text-overflow: ellipsis;
        	}
        	.layui-table tbody td:HOVER { 
				cursor:pointer;
				font-size: 20px;
				background-color: rgba(123,21,22,0.4);
			}
			table.layui-table{
				font-size:16px
			}
        </style>
    </head>
    <body>
    	<div class="layui-main">
    		<div class="layui-collapse" style="margin-top: 20px;">
    			<div class="layui-colla-item">
    				<h1 class="layui-colla-title layui-bg-cyan" style="font-size:25px">规则管理</h1>
    				<div class="layui-colla-content layui-show" style="min-height:500px;position: relative;">
    					<div id="ruleblack" class="" style="position: absolute; top: 10px; bottom: 10px; left: 10px;right: 10px;overflow: auto">
	    				<button class="layui-btn layui-btn-default" onclick="showaddform()">新建规则</button>
   							<c:if test="${ not empty pageBean.list }"><table class="layui-table" style="table-layout: fixed;">
    							<colgroup>
    								<col width="80"><col width="150" /><col width="200" /><col width="200" />
    								<col width="200"><col width="150" />
    							</colgroup>
    							<thead>
    								<tr>
    									<th>序号</th>
    									<th>名称</th>
    									<th>问题选择器</th>
    									<th>答案选择器</th>
    									<th>主题选择器</th>
    									<th>操作 &nbsp;&nbsp;</th>
    								</tr>
    							</thead>
    							<tbody>
    								<c:forEach items="${pageBean.list}" var="selectorBean"
    								 varStatus="status" ><tr style="display:none" class="selector_row">
    									<td>${status.index+1}</td>
    									<td>${ selectorBean.name }</td>
    									<td onclick="show(this)" title="点击查看详情">${ selectorBean.questionSelector }</td>
    									<td onclick="show(this)" title="点击查看详情">${ selectorBean.answerSelector }</td>
    									<td onclick="show(this)" title="点击查看详情">${ selectorBean.themeSelector }</td>
    									<td><a href="javascript:del('${ selectorBean.id }')">删除</a>&nbsp;&nbsp;
    										<a href="javascript:edit('${ selectorBean.id }')">编辑</a>
    									</td>
    								</tr></c:forEach>
    							</tbody>
    							<tfoot>
	    							<tr>
	    								<td id="pagediv" colspan="6">
	    							</tr>
    							</tfoot>
    						</table></c:if><c:if test="${ empty pageBean.list }">
    						<div>没有选择器。。。</div>
    						</c:if>
    					</div>
    					<div class="" id="addnewform" style="position: absolute; bottom: 10px; left: 10px; right: 100px; display: none;">
    						<form class="layui-form" name="addnewform" method="POST" 
    							action="${pageContext.request.contextPath}/document/selectorsave.action">
    							<div class="layui-form-item">
    								<label class="layui-form-label">新建规则：</label>
    								<input type="hidden" name="id" value="">
    								<div class="layui-input-inline">
    									<input class="layui-input" name="name" placeholder="请输入规则名称"/>
    								</div>
    								<div class="layui-inline">
    									<input class="layui-input" name="questionSelector" placeholder="问题选择器" />
    								</div>
    								<div class="layui-inline">
    									<input class="layui-input" name="answerSelector" placeholder="答案选择器" />
    								</div>
    								<div class="layui-inline">
    									<input class="layui-input" name="themeSelector" placeholder="主题选择器" />
    								</div>
    								<div class="layui-inline">
    									<button class="layui-btn">保存</button>
    									<button class="layui-btn layui-btn-normal" type="button" onclick="canceladd(this.parentNode.parentNode.parentNode)">取消</button>
    								</div>
    							</div>
    						</form>
    					</div>
    				</div>
    			</div>
    			<div class="layui-colla-item layui-hide">
    				<h1 class="layui-colla-title">模型管理</h1>
    				<div class="layui-colla-content ">
    					xxxxxxxxx
    				</div>
    			</div>
    		</div>
    	</div>
 	</body>
 	<script src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.js"></script>
 	<script src="${pageContext.request.contextPath}/static/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
 	<script src="${pageContext.request.contextPath}/static/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript">
 		var contextPath = "${pageContext.request.contextPath}"
 		layui.use(['laypage'],function(){
 			var laypage = layui.laypage;
 			var $trws = $(".selector_row");
 			var obj = {
 					elem:'pagediv'
 					,curr:1
 					,limit:5
 					,limits:[5,10,15,20,25]
					,count:$trws.length
					,layout:['prev','next','count']
 					,jump:function(obj,first){
						$trws.hide() 						
 						for (var i = 0; i < obj.limit; i++){
 							var index = (obj.curr-1) * obj.limit + i
 							$($trws[index]).show()
 						}
 					}
 				}
 			laypage.render(obj)
 		})
 		function canceladd(node){
 			$("#addnewform").hide()
 			$("#ruleblack").css("bottom","10px")
 			clearform()
 		}
 		function clearform(){
 			document.addnewform.relueanme.value = ""
 			document.addnewform.questionSelector.value=""
 			document.addnewform.answerSelector.value = ""
 		}
 		function showaddform(){
 			$("#ruleblack").css("bottom","75px")
 			$("#addnewform").show()
 		}
 		$("form").validate({
 			rules:{
 				rulename:{required:true}
 				,questionSelector:{required:true}
 				,answerSelector:{required:true}
 			},
 			errorPlacement:function(error,element){
 				ayer.tips(error.html(),element,{tips:1})
 			}
 		})
 		function edit(id){
 			$.ajax({
 				type:"get",
 				url:contextPath+"/document/selectorgetRule.action",
 				data:{
 					id:id
 				},
 				dataType:"json"
 				,beforeSend:function(res){
 					this.index = layer.load(1)
 				}
 				,success:function(res){
 					try{
 						document.addnewform.id.value = res['id']
 						document.addnewform.name.value = res['name']
 						document.addnewform.questionSelector.value = res['questionSelector']
 						document.addnewform.answerSelector.value = res['answerSelector']
 						document.addnewform.themeSelector.value = res['themeSelector']
 						showaddform()
 						console.log(res)
 					}catch(e){
 						console.log(e)
 					}
 				}
 				,error:function(res){
 					
 				}
 				,complete:function(res){
 					layer.close(this.index)
 				},
 				async:true
 			});
 		}
 		
 		function del(id){
 			layer.open({
 				title:"警告"
 				,content:"您确定要删除这条规则吗？"
 				,btn:['确定','取消']
 				,btn1:function(index,layero){
 					layer.close(index)
 					deleterule(id)
 				}
 				,btn2:function(index,layero){
 					layer.close(index)
 				}
 			})
 		}
 		function deleterule(id){
 			location.href = contextPath + "/document/selectordel.action?id="+id
 		}
 		function show(node){
 			layer.msg(node.innerHTML, {
 		        time: 20000, //20s后自动关闭
 		        btn: ['关闭']
 		      });
 		}
 	</script>
 	<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
 	<u:alert msg="${msg}"/>
</html>