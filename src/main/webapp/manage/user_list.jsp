<!--
	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   -->
   <%@ taglib prefix="s" uri="/struts-tags" %><%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户列表</title>
		<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/static/css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/static/css/bootstrap-theme.min.css" />
		<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/static/layui/css/layui.css" />
		<style>
			label.layui-form-label{
				box-sizing: content-box;
			}
			div.panel{
				margin-top: 20px;
			}
			/*table#UserListTable{
				table-layout: fixed;
			}
			table#UserListTable td,table#UserListTable th{
				text-overflow: ellipsis;
				overflow: hidden;
				white-space: nowrap;
			}*/
		</style>
	</head>

	<body>
		<div class="container">
			<div class="panel panel-info" >
				<div class=" panel-heading">
					<h2>用户列表</h2>
				</div>
				<div class=" panel-body">
					<button class="btn btn-info" data-toggle="modal" data-target="#addUserModalDiv">添加用户</button>
					
					<table class="layui-table" id="UserListTable">
						<thead>
							<tr>
								<th>编号</th>
								<th>用户名</th>
								<th>用户状态</th>
								<th>角色</th>
								<td>操作</td>
							</tr>
						</thead>
						<tbody>
						<s:iterator value="#request.page.list" var="curUser" status="st">
							<tr>
								<td><s:property value="#st.count+(#request.page.currentPage-1)*#request.page.pageSize"/></td>
								<td><s:property value="#curUser.username"/></td>
								<td><s:property value="#curUser.isLock ? '锁定':'未锁定'"/></td>
								<td><s:property value="#curUser.role.rolename"/></td>
								<td>
									<div class="btn-group">
										<button class="btn btn-info dropdown-toggle" data-toggle="dropdown">
										操作
										<span class="caret"></span>
									</button>
										<ul class="dropdown-menu">
											<li>
												<a href="${ pageContext.request.contextPath }/managelock.action?userId=<s:property value='#curUser.userId'/>" >
													<s:property value="#curUser.isLock ? '解锁':'锁定'"/>
												</a>
											</li>
											<li>
												<a href="javascript:resetpwd('<s:property value='#curUser.userId' />')">重置密码</a>
											</li>
											<li>
												<a href="javascript:del('<s:property value='#curUser.userId' />')">删除</a>
											</li>
										</ul>
									</div>
								</td>
							</tr>
						</s:iterator>
						</tbody>
					</table>
					<div id="UserPage" style="text-align: center;"></div>
				</div>
				<div class="panel-footer">
					&nbsp;
				</div>
			</div>
		</div>
		<div class="modal fade" id="addUserModalDiv" tabindex="-1" role="dialog">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button class="close" type="button" data-dismiss="modal" aria-label="Close">
							<span class="" aria-hidden="true">&times;</span>
						</button>
						<h3>添加用户</h3>
					</div>
					<div class="modal-body">
						<form name="addUserForm" id="addUserForm" class="layui-form" method="post" 
							action="${ pageContext.request.contextPath }/manageaddUser.action">
							<div class="layui-form-item">
								<label class="layui-form-label" for="UserNameInput">用户名</label>
								<div class="layui-input-block">
									<input class="layui-input" type="text" name="username" id="UserNameInput" />
								</div>
							</div>
							<div class="layui-form-item">
								<label class="layui-form-label" for="password">密码</label>
								<div class="layui-input-block">
									<input class="layui-input" id="password" name="password" type="password"/>
								</div>
							</div>
							<div class="layui-form-item">
								<label class="layui-form-label" for="repassword">确认密码</label>
								<div class="layui-input-block">
									<input class="layui-input" id="repassword" name="repwd" type="password"/>
								</div>
							</div>
							<div class="layui-form-item">
								<label class="layui-form-label" for="roleSelect">角色</label>
								<div class="layui-input-inline">
									<select class="layui-input" id="roleSelect" name="role.roleid">
										<!-- <option value="">请选择</option>  -->
									</select>
								</div>
							</div> 
						</form>
					</div>
					<div class="modal-footer">
						<button class="btn btn-info" type="button" data-dismiss="modal">取消</button>
						<button class="btn btn-info" type="button" onclick="document.addUserForm.reset()">重置</button>
						<button class="btn btn-primary" type="button" onclick="save()">保存</button>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script src="${ pageContext.request.contextPath }/static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ pageContext.request.contextPath }/static/js/bootstrap.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ pageContext.request.contextPath }/static/layui/layui.all.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/static/js/jquery.validate.js" ></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/static/js/jquery.form.js" ></script>
	<script type="text/javascript">
		var contextPath = "${pageContext.request.contextPath}";
		var currentPage = parseInt("${requestScope.page.currentPage}");
		var pageSize = parseInt("${requestScope.page.pageSize}");
		var totalCount = parseInt("${requestScope.page.totalCount}");
		var username = "${sessionScope.sessionUserQueryInfo.username}";
		var startTime= "${sessionScope.sessionUserQueryInfo.startTime}";
		var endTime = "${sessionScope.sessionUserQueryInfo.endTime}";
		layui.use("laypage",function(){
			 var laypage = layui.laypage;
			 laypage.render({
			 	elem:"UserPage"
			 	,count:totalCount
			 	,curr:currentPage
			 	,limit:pageSize
			 	,limits:[5,10,15,20]
			 	,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
			    ,jump: function(obj){
			      console.log(obj)
			      if(this.first)
			      	location.href = contextPath+"/manageshowUser.action?query.currentPage="
			    		  +obj.curr+"&query.pageSize="+obj.limit
			    		  +"&query.startTime="+startTime
			    		  +"&query.endTime="+endTime
			    		  +"&query.username="+username;
			      this.first = true;
			    }
			 });
		})
		
		
		
		$("#addUserForm").validate({
			rules:{
				username:{
					required:true
					,rangelength:[6,18]
				},
				password:{
					required:true
					,rangelength:[6,18]
				},
				repwd:{
					required:true
					,equalTo:"#password"
				}
			},
			messages:{
				username:{
					required:"请输入用户名"
					,rangelength:"用户名长度在6-18"
				},
				password:{
					required:"请输入密码",
					rangelength:"密码长度在6-18位"
				},
				repwd:{
					required:"请输入确认密码"
					,equalTo:"两次密码输入不相同"
				}
			},
			errorPlacement:function(error,element){
				error.css("color","red").insertAfter(element);
			},
			submitHander:function(){
				
				layer.confirm(
					"您的确定要提交吗？"
					,function(index,layero){
						document.addUserForm.submit();
						layer.close(index);
					}
				);
			}
		})
		layui.use(['layer', 'form'],function(){
			var layer = layui.layer
			  ,form = layui.form;
			$.ajax({
				url:contextPath+"/toAddUserRole.action"
				,success:function(res){
					res = eval(res);
					console.log(res);
					var $select = $("#roleSelect");
					var $dl = $select.next().children("dl");
					for(var i in res){
						var $opt = $("<option></option>");
						$opt.html(res[i].rolename);
						$opt.attr("value",res[i].roleid);
						$select.append($opt);
						/* var $dd = $("<dd class='layui-this'>浙江省</dd>");
						$dd.attr("lay-value",res[i].roleid);
						$dd.html(res[i].rolename);
						$dl.append($dd); */
						form.render("select")
					}
				}
			})
		})
		
		function save(){
			$("#addUserForm").submit();
		}
		
		function del(userid){
			layui.use('layer',function(){
				layer = layui.layer;
				layer.confirm(
					"确定要删除该用户吗？"
					,function(index,layero){
						location.href = contextPath+"/managedelete.action?userId="+userid;
						layer.close(index);
					}
					)
			})
		}
		function resetpwd(userid){
			layer.open({
				content:"您确定要重置该用户密码吗？",
				btn:['确定','取消'],
				btn1:function(index,layero){
					layer.close(index);
					location.href = contextPath+"/manageresetpwd.action?userId="+userid;
				}
			})
		}
	</script>
	<u:alert msg="${ requestScope.msg }"></u:alert>
</html>