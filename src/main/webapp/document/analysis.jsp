<!--<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>-->
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>文档分析</title>
		<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath}/static/layui/css/layui.css" />
	</head>

	<body>
		<div class="layui-main">
			<fieldset class="layui-elem-field layui-field-title">
				<legend>文档分析</legend>
			</fieldset>
			<div style="padding: 10px; background-color: #F2F2F2;background-image: url('../static/首页.jpg');">
				<div class="layui-row layui-col-space15">
				<div class="layui-col-md3">
					<div class="layui-card" >
						<div class="layui-card-header " style="padding:10px 0 5px 5px;">
							<form class="layui-form">
								<div class="layui-form-item">
									<div class="layui-inline">
										<input class="layui-input" name="docName" autocomplete="off" />
									</div>
									<div class="layui-inline"><a href="javascript:;">
										<i class="layui-icon"  style="font-size: 26px; color: #1E9FFF;">&#xe615;</i>
									</a>
									</div>
								</div>
							</form>
						</div>
						<div class="layui-card-body" style="min-height: 480px;max-height: 700px; overflow: auto;">
							<ul id="doclist">
								<li>文档1</li>
								<li>文档二</li>
							</ul>

						</div>
					</div>
					
				</div>
				<div class="layui-col-md9">
					<div class="layui-card">
						<div class="layui-card-header layui-bg-cyan">
							已选文档
						</div>
						<div class="layui-card-body" style="max-height: 150px;min-height: 150px;">
							<div id="" style="height: 114px;">
								<table class="layui-table">
									<colgroup>
										<col width="100">
										<col width="250">
										<col>
									</colgroup>
									<thead>
										<tr>
											<th>文档编号</th>
											<th>文档名称</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td>1</td>
											<td>文档一</td>
											<td><a href="javascript:remove(this)">移除</a></td>
										</tr>
										<tr>
											<td>1</td>
											<td>文档一</td>
											<td><a href="javascript:remove(this)">移除</a></td>
										</tr>
										<tr>
											<td>1</td>
											<td>文档一</td>
											<td><a href="javascript:remove(this)">移除</a></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div style="clear:both"></div>
							<form class="layui-form"> 
								<div class="layui-form-item">
									<label class="layui-form-label">分析方法</label>
									<div class="layui-inline">
										<input type="radio" name="x" title="规则分析" checked="checked"/>
										<input type="radio" name="x" title="nlp分析"/>
										<input type="radio" name="x" title="模型分析"/>
									</div>
									<div class="layui-inline"><button class="layui-btn layui-btn-danger">分析</button></div>
								</div>
							</form>
							
						</div>
						<div class="layui-card-body"></div>
					</div>
					<div class="layui-card">
						<div class="layui-card-header layui-bg-cyan">
							<span class="">分析结果</span>
						</div>
						<div class="layui-card-body" style="min-height: 250px;">
							<table>
								<tr>
									<td></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				</div>
			</div>
		</div>
	</body>
	<script src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath}/static/layui/layui.all.js" ></script>
	<script type="text/javascript">
		var contextPath = "${pageContext.request.contextPath}"
		layui.use(['form', 'layedit', 'laydate'],function(){
			var form = layui.form
			var layer = layui.layer
  			var layedit = layui.layedit
  			var laydate = layui.laydate;
		})
	</script>
		<script type="text/javascript">
		layui.use(['form', 'layedit', 'laydate'],function(){
			var form = layui.form
			var layer = layui.layer
  			var layedit = layui.layedit
  			var laydate = layui.laydate;
		})
		$.ajax({
			url:contextPath+"/document/getJSONlistDocument.action"
			,data:{
				'queryInfoBean.currentPage':1
				,'queryInfoBean.pageSize':1000
				,'queryInfoBean.param':''
			}
			,dataType:'json'
			,complete:function(res){
				console.log("complete...")
			}
			,error:function(res){
				console.log("error...")
			}
			,success:function(res){
				var datalist = res.list
				var content = ""
				for (var i in datalist){
					var item = datalist[i];
					content += "<li><a href='javascript:add("+item.id+");'>"+item.docName+"</a></li>"
				}
				$("#doclist").html(content)
				console.log("success...")
			}
		})
		function add(id){
			
		}
	</script>
</html>