<!--<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>-->
							<form class="layui-form"  id="addKbForm" name="addKbForm"
								action="${pageContext.request.contextPath}/kb/addKb" 
								method="post">
    							<div class="layui-form-item">
    								<label class="layui-form-label">标准问题：</label>
    								<div class="layui-input-block">
    									<input id="" name="standardQuestion.content" class="layui-input" type="text" />
    								</div>
    							</div>
    							<div class="layui-form-item">
    								<label class="layui-form-label">主题：</label>
    								<div class="layui-input-block">
    									<input id="subject" name="subject" class="layui-input" type="text" />
    								</div>
    							</div>
    							<div class="layui-form-item">
    								<label class="layui-form-label">标准答案：</label>
    								<div class="layui-input-block">
<!--     									<input id="" name="content" class="layui-input" type="text" /> -->
    									<textarea rows="10" cols="" 
    										name="content" placeholder="请输入问题答案" 
    										class="layui-textarea"></textarea>
    								</div>
    							</div>
    							<!-- <div class="layui-form-item">
    								<label class="layui-form-label">相关问题：</label>
    								<div class="layui-input-inline">
    									<input id="" name="extendQuestion.content" class="layui-input" type="text" />
    								</div>
    								<div class="layui-input-inline">
    									<label class="layui-form-label">
    										<a><i class="layui-icon">&#xe654;</i></a>
    										<a><i class="layui-icon">&#x1006;</i></a>
    									</label>
    								</div>
    							</div> -->
    							<div class="layui-form-item">
    								<div class="layui-input-block">
    									<button type="submit" class="layui-btn ">提交</button>
    								</div>
    							</div>
    						</form>