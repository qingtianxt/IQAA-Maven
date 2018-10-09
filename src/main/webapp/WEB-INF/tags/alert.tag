<%@ tag language="java" pageEncoding="UTF-8"%><%@ attribute name="msg" required="false" rtexprvalue="true" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><c:if test="${ ! empty msg }"><script type="text/javascript">
	layer.open({
		content:"${msg}",
		btn:["确定"],
		btn1:function(index,layero){
			layer.close(index,layero); 
		}
	})
</script></c:if>