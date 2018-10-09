<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>文件浏览列表</title>
       
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/layui/css/layui.css"/>
        <style type="text/css">
        	th,td{
        		white-space: nowrap;
        		overflow: hidden;
        		text-overflow: ellipsis;
        	}
        </style>
    </head>
    <body>
    	<div class="container">
    		<div style="height:30px"></div>
	    	<form class="layui-form" action="${pageContext.request.contextPath}/document/getPageDocument.action">
    			 <div class="layui-form-item">
				    <div class="layui-inline">
				      <label class="layui-form-label" style="width:100px">文档标题</label>
				      <div class="layui-input-inline">
				        <input name="queryInfo.param" autocomplete="off" 
				        	class="layui-input" type="text" 
				        	value='${sessionScope.docPageInfo.param}'>
				      </div>
				    </div>
				    
				    <div class="layui-inline">
				    	
				    	<label class="layui-form-label">日期:</label>
				    	  <div class="layui-input-inline" style="width: 100px;">
				    	    <input name="queryInfo.start" id="start" 
				    	    	placeholder="开始日期" autocomplete="off" 
				    	    	class="layui-input" type="text" readonly="readonly" 
				    	    	value="<s:date format='yyyy-MM-dd' name='#session.docPageInfo.start'/>" >
				    	  </div>
				    	  <div class="layui-form-mid">-</div>
				    	  <div class="layui-input-inline" style="width: 100px;">
				    	    <input name="queryInfo.end" id="end" 
				    	    placeholder="截止日期" autocomplete="off" 
				    	    class="layui-input" type="text" 
				    	    readonly="readonly" value="<s:date format='yyyy-MM-dd' name='#session.docPageInfo.end'/>">
				    	  </div>
				    </div>
				    <div class="layui-inline">
				    	<button class="layui-btn">查询</button>
				    </div>
				    <div class="layui-inline">
				    	<button type="button" class="layui-btn" id="uploadbtn">
  							<i class="layui-icon">&#xe67c;</i>上传文档
						</button>
				    </div>
				    <div class="layui-inline">
				    	<input class="layui-btn layui-btn-primary" 
				    		onclick="document.secondForm.submit()"
				    		id="batchfecthbtn" type="hidden" value="批量提取">
				    </div>
				   <!--  <div class="layui-inline">
				    	<input class="layui-btn layui-btn-primary" id="batchfecthbtn2" onclick="document.secondForm.submit()" type="hidden" value="NLP提取" >
				    </div> -->
				 </div>
	    	</form>
    		<form name="secondForm" method="get" 
    			action="${pageContext.request.contextPath}/kb/batchselectquestion.action">
	    	<table class="layui-table" style="table-layout: fixed;">
			  <colgroup>
			  	<col width="50">
			    <col width="100">
			    <col width="350">
			    <col width="150">
			    <col width="200">
			    <col>
			  </colgroup>
			  <thead>
			    <tr>
			    	<th><input type="checkbox" title="全选" onclick="selectAll(this)"></th>
			    	<th>序号</th><th>文件名称</th>
			      <th>上传人</th><th>上传时间</th><th>操作</th>
			    </tr> 
			  </thead>
			  <tbody>
			    <s:iterator value="#request.pageBean.list" var="doc" status="st">
			    <tr>
			    	<td>
			    		<input type="checkbox" name="docBeans[<s:property value='#st.count-1'/>].id" 
			    			value="<s:property value='#doc.id'/>" onclick="showbatchbtn(this)" >
			    	</td>
			      <td>
			      	<s:property value="#st.count+(#request.pageBean.currentPage-1)*#request.pageBean.pageSize"/>
			      </td>
			      <td title="<s:property value='#doc.docName'/>">
			      	<a href="${pageContext.request.contextPath}/document/showDetail.jsp?id=<s:property value='#doc.id'/>" ><s:property value="#doc.docName"/></a>
			      </td>
			      <td><s:property value="#doc.userBean.username"/></td>
			      <td><s:date name="#doc.addtime"  format="yyyy-MM-dd hh:mm:ss" /></td>
			      <td>
			      	<a href="javascript:del('<s:property value='#doc.id'/>')"
							title="删除"> <i class="layui-icon"
								style="font-size: 30px; color: #1E9FFF;">&#xe640; </i></a> <a
							href="${ pageContext.request.contextPath }/document/downloadDocument.action?id=<s:property value='#doc.id'/>"
							title="下载"><i class="layui-icon"
								style="font-size: 30px; color: #1E9FFF;">&#xe601;</i></a> 
						</td>
			    </tr>
			    
			    </s:iterator>
			  </tbody>
			</table>
    		</form>
	<div id="test1" style="text-align:center"></div>
    	</div>
 	</body>
 	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/jquery-1.12.1.js" ></script>
 	<script type="text/javascript" src="${pageContext.request.contextPath}/static/js/bootstrap.min.js" ></script>
 	<script type="text/javascript" src="${pageContext.request.contextPath}/static/layui/layui.all.js" ></script>
 	<script type="text/javascript">
 		var contextPath = "${pageContext.request.contextPath}";
		var totalCount = parseInt("${requestScope.pageBean.totalCount}");
		var currentPage = parseInt("${requestScope.pageBean.currentPage}");
		var pageSize = parseInt("${requestScope.pageBean.pageSize}");
		var param = '${sessionScope.docPageInfo.param}';
		var start = "<s:date name='#session.docPageInfo.start'  format='yyyy-MM-dd' />";
		var end = "<s:date name='#session.docPageInfo.end' format='yyyy-MM-dd' />";
 		layui.use('laypage', function(){
		  var laypage = layui.laypage;
		  //执行一个laypage实例
		  laypage.render({
		    elem: 'test1' //注意，这里的 test1 是 ID，不用加 # 号
		    ,layout: ['count', 'prev', 'page', 'next', 'limit', 'skip']
		    ,count: totalCount //数据总数，从服务端得到
		    ,curr:currentPage
		    ,limit:pageSize
		    ,limits:[5,10,15,20]
		    ,jump:function(obj){
				    	if(this.first){
				    		layer.load();
							location.href = contextPath
									+ "/document/getPageDocument.action?queryInfo.param="
									+ param 
									+ "&queryInfo.currentPage="
									+ obj.curr
									+ "&queryInfo.pageSize="
									+ obj.limit
									+ "&queryInfo.start="
									+ start 
									+ "&queryInfo.end=" 
									+ end;
				    	}
						this.first = true;
					}
				});
			});
 		
 			// 日期选择器
			layui.use('laydate', function() {
				var laydate = layui.laydate;

				//常规用法
				laydate.render({
					elem : '#start'
				});
				laydate.render({
					elem : '#end'
				});
			})
			
			var uploadNumber = 0;
 			var successNumber = 0;
 			var failNumber = 0; 
			// 文件上传
			layui.use('upload', function(){
			  var upload = layui.upload;
			   
			  //执行实例
			  var uploadInst = upload.render({
			    elem: '#uploadbtn' //绑定元素
			    ,accept:'file'
			    ,field:'upload'
			    ,url: contextPath+'/document/receiveFile' //上传接口
			    ,acceptMime:'text/plain,text/html,text/htm,application/msword,application/vnd.ms-powerpoint,application/vnd.ms-excel'
		    	,multiple:true
			    ,number:10
			    ,before:function(obj){
			    	if(uploadNumber==0){
			    		this.layerIndex = layer.load();
				    	successNumber=0;
				    	failNumber =0;
			    	}
			    	uploadNumber ++;
			    }
			    ,done: function(res,index,upload){
			      //上传完毕回调
			      uploadNumber--;
			      if(res.code==1){
			    	  successNumber++;
			      }else{
			    	  failNumber ++;
			      }
			      if(uploadNumber==0){
				      layer.close(this.layerIndex); //关闭loading
				      layer.open({
				    	  content:/* successNumber+"个"+ */res.msg,
				    	  btn:['确定'],
				    	  btn1:function(index,layero){
				    		  layer.close(index,layero);
						      location.reload();
				    	  }
				      })
			      }
			      console.log(res);
			    }
			    ,error: function(x,y){
			      //请求异常回调
			      uploadNumber--;
			      if(uploadNumber==0){
				    layer.close(this.layerIndex); //关闭loading
			      	location.reload();
			      }
			    }
			  });
			});	
			
			function errorPrompt(content){
				layer.open({
					title:"错误",
					content:content,
					btn:['确定']
				})
			}
			
			function del(id) {
				layer.open({
					content : '确定要删除吗？',
					btn : [ '取消', '确定' ],
					btn1 : function(index, layero) {
						layer.close(index, layero);
					},
					btn2 : function(index, layero) {
						location.href = contextPath
								+ "/document/deleteDocument.action?id=" + id;
						layer.close(index, layero);
					}
				})
			}
			function selectAll(obj){
				$checkbox = $("input[type='checkbox']");
				for(var i = 0; i < $checkbox.length; i++){
					checkbox = $checkbox[i];
					checkbox.checked = obj.checked;
				}
				if(obj.checked){
					$("#batchfecthbtn").attr("type","button");
				}else{
					$("#batchfecthbtn").attr("type","hidden");
				}
			}
			function showbatchbtn(checkbox){
				$("#batchfecthbtn").attr("type",'hidden');
				$checkbox = $("input[type='checkbox']");
				for(var i = 0; i < $checkbox.length; i++){
					checkbox = $checkbox[i];
					if(checkbox.checked){
						$("#batchfecthbtn").attr("type","button");
						break;
					}
				}
			}
		</script>
		<u:alert msg="${requestScope.deleteMsg}"/>
</html>