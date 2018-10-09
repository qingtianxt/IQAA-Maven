<!--<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>-->
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>修改密码</title>
        <link rel="stylesheet" href="../static/layui/css/layui.css" />
    </head>
    <body>
    	<div class="" id="modifyDiv" style="padding: 20px;width:500px;margin:100px auto">
    		<form class="layui-form" id="modifyForm" name="modifyForm" target="_top"
    			method="post" action="${pageContext.request.contextPath}/managemodifyPwd.action">
    			<input type="hidden" name="userId" value="${ session_user_bean.userId }"/>
	    		<div class="layui-form-item">
	    			<label class="layui-form-label">
	    				原密码：
	    			</label>
	    			<div class="layui-input-block">
	    				<input type="password" id="oldpwd" name='oldpwd' placeholder="请输入原密码" class="layui-input"/>
	    			</div>
	    		</div>
	    		<div class="layui-form-item">
	    			<label class="layui-form-label">
	    				新密码：
	    			</label>
	    			<div class="layui-input-block">
	    				<input type="password" id='newpwd' name='password' placeholder="请输入新密码" class="layui-input"/>
	    			</div>
	    		</div>
	    		<div class="layui-form-item">
	    			<label class="layui-form-label">
	    				确认密码：
	    			</label>
	    			<div class="layui-input-block">
	    				<input type="password" name='repwd' placeholder="请输入确认密码" class="layui-input"/>
	    			</div>
	    		</div>
	    		<div class="layui-form-item">
	    			<div class="layui-input-block">
	    				<button class="layui-btn" type="submit">确定</button>
	    				<button class="layui-btn layui-btn-primary" type="reset">重置</button>
	    			</div>
	    		</div>
	    	</form>
    	</div>
    	
 	</body>
 	 	<script src="../static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
 	<script type="text/javascript" src="../static/layui/layui.all.js" ></script> 
 	<script src="../static/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
 	<script src="../static/js/jquery.form.js" type="text/javascript" charset="utf-8"></script> 
 	<script type="text/javascript">
 		/* $.validator.addMethod("notEqualTo",function(value,element,param){
 			var val = $(params).val();
 			return value == val;
 		},"this field should not repaitd!"); */
		$("form").validate({
			rules:{
				oldpwd:{
					required:true
				},
				password:{
					required:true,
					rangelength:[6,18]
				},
				repwd:{
					required:true,
					equalTo:"#newpwd"
				}
			},
			errorPlacement:function(error,element){
				error.css("color","red").insertAfter(element);
			},
			submitHandler:function(){
				layer.open({
					content:"您确定要提交吗？",
					btn:['确定','取消'],
					btn1:function(index,layero){
						layer.close(index,layero);
						document.modifyForm.submit();
					},
					btn2:function(index,layero){
						layer.close(index,layero);
					}
				}) 
			}
		})		
 	</script>
</html>