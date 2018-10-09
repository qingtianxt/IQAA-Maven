<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/assets/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/assets/css/font-awesome.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/assets/css/ace-fonts.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/assets/css/ace.min.css"
	id="main-ace-style" />
<title>500页面</title>
<script
	src="${pageContext.request.contextPath }/assets/js/ace-extra.min.js"></script>
</head>
<body class="no-skin">
	<div class="main-container" id="main-container">
		<div class="main-content">
			<!-- /section:basics/content.breadcrumbs -->
			<div class="page-content">

				<!-- /section:settings.box -->
				<div class="page-content-area">
					<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->

							<!-- #section:pages/error -->
							<div class="error-container">
								<div class="well">
									<h1 class="grey lighter smaller">
										<span class="blue bigger-125"> <i
											class="ace-icon fa fa-random"></i> 500错误
										</span> 页面上有错误
									</h1>

									<hr />
									<h3 class="lighter smaller">
										我们正在修复 <i
											class="ace-icon fa fa-wrench icon-animated-wrench bigger-125"></i>
										中，请稍后!
									</h3>

									<div class="space"></div>

									<div>
										<h4 class="lighter smaller">同时，你还可以尝试一下以下好玩的东西:</h4>

										<ul class="list-unstyled spaced inline bigger-110 margin-15">
											<li><i class="ace-icon fa fa-hand-o-right blue"></i>
												去帮助页面</li>

											<li><i class="ace-icon fa fa-hand-o-right blue"></i>
												联系我们</li>
										</ul>
									</div>

									<hr />
									<div class="space"></div>

									<div class="center">
										<a href="javascript:history.back()" class="btn btn-grey">
											<i class="ace-icon fa fa-arrow-left" onclick="history.go(-1)"></i> 返回上一页
										</a> <a href="${pageContext.request.contextPath }/index.jsp" class="btn btn-primary"> <i
											class="ace-icon fa fa-tachometer"></i> 去主页
										</a>
									</div>
								</div>
							</div>

							<!-- /section:pages/error -->

							<!-- PAGE CONTENT ENDS -->
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.page-content-area -->
			</div>
			<script type="text/javascript">
				window.jQuery
						|| document
								.write("<script src='${pageContext.request.contextPath }/assets/js/jquery.min.js'>"
										+ "<"+"/script>");
			</script>
			<script type="text/javascript">
				if ('ontouchstart' in document.documentElement)
					document
							.write("<script src='${pageContext.request.contextPath }/assets/js/jquery.mobile.custom.min.js'>"
									+ "<"+"/script>");
			</script>
			<script
				src="${pageContext.request.contextPath }/assets/js/bootstrap.min.js"></script>
			<script
				src="${pageContext.request.contextPath }/assets/js/ace-elements.min.js"></script>
			<script
				src="${pageContext.request.contextPath }/assets/js/ace.min.js"></script>
			<!-- /.page-content -->
		</div>
	</div>
</body>
</html>