<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
						<form class="layui-form" method="POST" name="form1" action="${ pageContext.request.contextPath }/kb/listKb">
			    			 <div class="layui-form-item">
							    <div class="layui-inline">
							      <label class="layui-form-label" style="width:100px">文档标题</label>
							      <div class="layui-input-inline">
							        <input name="query.question" autocomplete="off" 
							        	class="layui-input" type="text" 
							        	value='${sessionScope.sessionAnswerQueryInfo.question}'>
							      </div>
							    </div>
							    
							    <div class="layui-inline">
							    	
							    	<label class="layui-form-label">日期:</label>
							    	  <div class="layui-input-inline" style="width: 100px;">
							    	    <input name="query.start" id="start" 
							    	    	placeholder="开始日期" autocomplete="off" 
							    	    	class="layui-input" type="text" readonly="readonly" 
							    	    	value="<s:date format='yyyy-MM-dd' name='#session.sessionAnswerQueryInfo.start'/>" >
							    	  </div>
							    	  <div class="layui-form-mid">-</div>
							    	  <div class="layui-input-inline" style="width: 100px;">
							    	    <input name="query.end" id="end" 
							    	    placeholder="截止日期" autocomplete="off" 
							    	    class="layui-input" type="text" 
							    	    readonly="readonly" value="<s:date format='yyyy-MM-dd' name='#session.sessionAnswerQueryInfo.end'/>">
							    	  </div>
							    </div>
							    <div class="layui-inline">
							    	<button class="layui-btn">查询</button>
							    </div>
							    <div class="layui-inline">
							    	<button class="layui-btn" type="button" onclick="clearform()">清空</button>
							    </div>
							   <!--  <div class="layui-inline">
							    	<button type="button" class="layui-btn" id="uploadbtn">
			  							<i class="layui-icon">&#xe67c;</i>上传文档
									</button>
							    </div> -->
							 </div>
				    	</form>
<c:if test="${! empty requestScope.requestAnswer.list }">
						<table class="layui-table" id="list-table">
    						<colgroup>
    							<col width="100px" /><col width="200px" /><col width="100px" />
    							<col width="350px" /><col width="100px" /><col  />
    						</colgroup>
    						<thead>
    							<tr>
    								<th>序号</th><th>标准问题</th><th>主题</th><th>原文链接</th><th>相关问题</th><th>操作
    							</tr>
    						</thead>
    						<tbody>
    						<c:forEach items="${ requestScope.requestAnswer.list }" var="curr" varStatus="status">
    							<tr>
    								<td>${ status.index + 1 + (requestScope.requestAnswer.currentPage-1) * requestScope.requestAnswer.pageSize }</td>
    								<td>${ curr.standardQuestion.content }</td>
    								<td>${ curr.subject }</td><td>${ curr.content }</td>
    								<td>
    									<%-- <ul>
    									<c:forEach items="${ curr.extendQuestion }" var="c">
											<li>${ c.content }</li>    										
    									</c:forEach>
    									</ul> --%>
    									<a href="${pageContext.request.contextPath}/">${curr.doc.docName}</a>
									</td>
    								<td>
   										<a title="查看" href="javascript:detail('${ status.index+1 }','${ curr.id }')">
   											<i class='layui-icon'>&#xe655;</i></a>
    									<a href="javascript:edit('${ status.index+1 }','${ curr.id }')" title="编辑">
    										<i class="layui-icon">&#xe642;</i></a>
    									<a href="javascript:del('${ curr.id }')" title="删除">
    										<i class="layui-icon">&#xe640;</i></a>
    								</td>
    							</tr>
    						</c:forEach>
    						</tbody>
    					</table>
    					<div class="" id="page-nav" style="text-align:center">
    						
    					</div>
</c:if>
<c:if test="${ empty requestScope.requestAnswer.list }">
<div style="text-aglin:center;position:relative;">
	<i class="layui-icon" style="position:absolute; left:50%; top:50%;margin-left:-75px; font-size: 150px;color:gray">&#xe702;</i>
	<p>
		没有任何查询结果...
	</p>
</div>
</c:if>