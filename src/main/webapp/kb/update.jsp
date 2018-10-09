<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>编辑</title>
        <link rel="stylesheet" type="text/css" href="../static/layui/css/layui.css"/>
    </head>
    <body>
    	<div class="layui-main">
    		<div style="height:40px"></div>
    		<form class="layui-form"  id="addKbForm" name="addKbForm"
				action="${pageContext.request.contextPath}/kb/updateKb" 
				method="post">
				<input id="" name="id" class="layui-input" type="hidden" value="${answer.id }" />
				<div class="layui-form-item">
					<label class="layui-form-label">标准问题：</label>
					<div class="layui-input-block">
						<input id="" name="standardQuestion.content" 
							class="layui-input" type="text" autocomplete="off"
							value="${answer.standardQuestion.content}" />
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">主题：</label>
					<div class="layui-input-block">
						<input id="subject" name="subject" class="layui-input"
							 type="text" value="${answer.subject}" autocomplete="off"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">标准答案：</label>
					<div class="layui-input-block"> 
						<textarea rows="15" cols="" 
							name="content" placeholder="请输入问题答案" 
							class="layui-textarea">${answer.content}</textarea>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-input-block">
						<button type="submit" class="layui-btn ">提交</button>
						<a class="layui-btn layui-btn-primary" 
							href='${pageContext.request.contextPath }/kb/listKb'>取消</a>
					</div>
				</div>
			</form>
    	</div>
 	</body>
 	<script src="../static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript" src="../static/js/jquery.form.js" ></script>
 	<script src="../static/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
 	<script src="../static/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript">
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
 				layer.open({
 					content:"您确定要提交吗？",
 					btn:['取消','确定'],
 					btn1:function(index,layero){
 						layer.close(index);
 					},
 					btn2:function(index,layero){
 						layer.close(index);
 						document.addKbForm.submit();
 					}
 				})
 				/*$("#addKbForm").ajaxForm({
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
 						$("input[name='id']").remove();
 					},
 					resetForm:true
 				});*/
 			}
 		})
 	</script>
</html>