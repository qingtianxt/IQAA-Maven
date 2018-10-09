<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>智能问答后台管理</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/layui/css/layui.css" />
		<style type="text/css">
			:root{
				height: 100%;	
			}
			body{
				display: flex;
				flex-direction: column;
				height: 100%;
			}
			
			#children-contanier{
				flex:2;
				-ms-flex: 2;
			}
			
		</style>
	</head>
	<body>
		<div class="layui-header" style="width: 100%;background-color:  #393D49;">
			<div class="layui-main">
				<ul class="layui-nav">
					<li class="layui-nav-item">
						<a target="Main" href="${pageContext.request.contextPath}/default.html">首页</a>
					</li>
					<li class="layui-nav-item">
						<a href="${pageContext.request.contextPath}/document/getPageDocument.action" target="Main">文档库</a>
						<dl class="layui-nav-child">
							<dd class="">
								<a href="${pageContext.request.contextPath}/document/getPageDocument.action" target="Main">
									文档管理
								</a>
							</dd>
							<dd class="">
								<a target="Main" href="${pageContext.request.contextPath}/document/selectorlist.action">规则管理</a>
							</dd>
						</dl>
					</li>
			     <%-- <li class="layui-nav-item ">
			        <a target="Main" 
			        	href="${pageContext.request.contextPath}/document/cssSelectorActionlist.action">
			        	文档管理</a> 
			      </li>  --%>
			      <li class="layui-nav-item ">
			        <a href="${ pageContext.request.contextPath }/kb/listKb" target="Main">知识库</a>
			      </li> 
			      <li class="layui-nav-item ">
			        <a href="${pageContext.request.contextPath}/manageshowUser.action" target="Main">用户管理<!-- <span class="layui-badge-dot"></span> --></a>
			      </li> 
			    </ul>
			    <ul class="layui-nav layui-layout-right">
			    	<li class="layui-nav-item">
			    	<c:if test="${ ! empty sessionScope.session_user_bean }">
						<a href="javascript:;">
							${ sessionScope.session_user_bean.username }
						</a>
			    		<dl class="layui-nav-child">
			    			<dd class="layui-hide-sm layui-show-xs"><a 
			    				href="${ pageContext.request.contextPath }/manage/modify_pwd.jsp" 
			    				target="Main">修改密码</a></dd>
			    			<dd class=""><a 
			    				href="${ pageContext.request.contextPath }/manage/modify_pwd.jsp" 
			    				target="Main">修改密码</a></dd>
			    			<dd class="layui-hide-sm layui-show-xs"><a 
			    				href="${ pageContext.request.contextPath }/manage/modify_pwd.jsp" 
			    				target="Main">修改密码</a></dd>
			    			<dd class="layui-hide-sm layui-show-xs"><a 
			    				href="${ pageContext.request.contextPath}/managelogout" 
			    				target="_top">退出登录</a></dd>
			    			<dd class=""><a 
			    				href="${ pageContext.request.contextPath}/managelogout" 
			    				target="_top">退出登录</a></dd>
			    			<dd class="layui-hide-sm layui-show-xs"><a 
			    				href="${ pageContext.request.contextPath}/managelogout" 
			    				target="_top">修改密码</a></dd>
			    		</dl>
			    	</c:if>
			    	<c:if test="${ empty sessionScope.session_user_bean }">
			    		<a href="${ pageContext.request.contextPath }/manage/login.jsp" 
			    			target="_top">登录</a>
			    	</c:if>
			    	</li>
			    </ul>
			</div>
		</div>
		<div id="children-contanier">
			<iframe id="children" name="Main" 
				src="${pageContext.request.contextPath}/default.html" 
				width="100%" height="100%" scrolling="auto" 
				frameborder="no" >
			</iframe>
		</div>
		
	</body>
	<script src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/static/layui/layui.all.js" ></script>
	<script src="${pageContext.request.contextPath}/static/js/jquery.validate.js" type="text/javascript" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath}/static/js/jquery.form.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript">
		var contextPath = "${pageContext.request.contextPath}"
		 
		function setFrameHeight(){  
			var height = $(window.top).innerHeight()-65;
// 			alert(height);
			$("#children").height(height);
	   }
		window.onresize = setFrameHeight; 
		window.Main.onload = setFrameHeight;
		function sub(){
			$("#modifyfrom").ajaxForm({
				beforeSubmit:function(){
					layer.close(index);
					this.index = layer.load(1);
				}, 
				success:function(data){
					layer.open({
						type:1,
						title:'提示',
						skin:'layui-layer-rim',
						content:data
					});
				}
			});
		}
		
		
	</script>
	<u:alert msg="${msg }"></u:alert>
</html>
