/**
 * 
 */

 		function changepage(obj){
 			var $trows = $("#"+obj.elem).children();
 			$trows.hide();
 			obj.count = $trows.length;
			var start = (obj.curr-1) * obj.limit;
			var end = obj.curr * obj.limit;
			for(var i = 0 ; i < obj.count; i++){
				if(i >= start && i < end && i < $trows.length ){
					$($trows[i]).show();
				}
			}
 		}
 		var pageoption1 = {
 				elem:"pagenation1",
 				layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
 				limits: [5,10,15,20,25],
 				limit : 5
 			}
 		var pageoption2 = {
 				elem:"pagenation2",
 				layout:['count','prev','page','next','limit','skip'],
 				limits: [5,10,15,20,25],
 				limit : 5
 		}
 		
 		var laypage = null;
 		layui.use("laypage",function(){
 			laypage = layui.laypage;
 			laypage.render(pageoption1);
 			laypage.render(pageoption2);
 		})
 		function extract(docId,url,showId,obj){
 			if(obj.first)
 				return;
 			obj.first = true;
 			$.ajax({
 	 			url:url
 	 			,data:{
 	 				id:docId
 	 			},
 	 			dataType:"json"
 	 			,beforeSend:function(){
 	 				this.layerIndex = layer.load(1);
 	 				$(showId).empty();
 	 			}
 	 			,success:function(response){
 	 				
 	 				res = response.data
 	 				if(!res)
 	 					res = []
 	 				if(response.msg!="success"){
 	 					var $tr = $("<tr><td colspan='5'>"+response.msg+"</td></tr>")
 	 					$tr.appendTo($(showId));
 	 				}
 	 				else if(res.length == 0){
 	 					var $tr = $("<tr><td colspan='5'>没有分析出任何内荣</td></tr>")
 	 					$tr.appendTo($(showId));
 	 				}
					for (var i in res){
						var ol = parseInt(i) + 1;
						if(!res[i].subject){
							res[i].subject = "";
						}
						var $tr = $("<tr></tr>");
						var $a = $("<a class='' title='添加' href='javascript:;'><i class='layui-icon' style='font-size: 25px; color: #1E9FFF;'>&#xe654;</i></a>");
						$tr.append("<td>"+ol+"</td>")
						.append("<td title='查看详细'>"+res[i].content+"</td>")
						.append("<td>"+res[i].subject+"</td>")
						.append("<td title='查看详细'>"+res[i].standardAnswer.content+"</td>")
						.append($("<td>").append($a));
						$tr.appendTo($(showId));
						$a.bind('click',function(){
								/* save(res[i]) */
								$(this).unbind('click');
								$(this).html("<i class='layui-icon' style='font-size: 25px; color: #1E9FFF;' title='删除'>&#xe640;</i>")
							});
					}
					switch(showId){
					
					case '#regular-div':	
						pageoption1.count = res.length;
						laypage.render(pageoption1);
						break;
					case '#nlp-div':
						pageoption2.count = res.length;
						laypage.render(pageoption2);
						break;
					case '#model-div':
						pageoption3.count = res.length;
						pageoption3.jump = changepage(pageoption3);
						laypage.render(pageoption3);
						break;
					}
 	 			}
 	 			,complete:function(){
 	 				layer.close(this.layerIndex);
 	 			}
 	 		});
 			//this.onclick = null;
 		}
 		
 		function save(obj){
 			console.log("保存！");
 			var data = {
				content:obj.standardAnswer.content,
				subject:obj.subject,
				'standardQuestion.content':obj.content,
				'standardQuestion.subject':obj.subject
			}
 			$.ajax({
 				url:contextPath+"/kb/addKb",
 				method:"post",
 				data:data,
 				beforeSend:function(){
 					this.index = layer.load(1);
 				},
 				success:function(res){
 					layer.open({
 						content:res
 					})
 				},
 				error:function(){
 					layer.open({
 						content : "服务器异常"
 					})
 				},
 				complete:function(){
 					layer.close(this.index)
 				}
 			})
 		}
 		
 		function modelselect(id,obj){
 			if(obj.first)
 				return;
 			obj.first = true;
 			$.ajax({
 				data:{
 					id:docId
 				},
 				url:contextPath+"/kb/selwimoquestion.action"
 				,beforeSend:function(){
 					this.layerIndex = layer.load(1);
 					$("#model-div").empty()
 				},
 				complete:function(){
 					layer.close(this.layerIndex)
 				}
 				,success:function(response){
 					res = response.data
 	 				if(!res)
 	 					res = []
 	 				if(response.msg!="success"){
 	 					var $tr = $("<tr><td colspan='5'>"+response.msg+"</td></tr>")
 	 					$tr.appendTo($("#model-div"));
 	 				}
 	 				else if(res.length == 0){
 	 					var $tr = $("<tr><td colspan='5'>没有找到有用的内容....</td></tr>")
 	 					$tr.appendTo($("#model-div"));
 	 				}
 					var rows = new Array();
 					for (var i in res){
						var ol = parseInt(i) + 1;
						var $tr = $("<tr></tr>");
						var $a = $("<a class='' title='添加' href='javascript:;'><i class='layui-icon' style='font-size: 25px; color: #1E9FFF;'>&#xe654;</i></a>");
						$tr.append("<td>"+ol+"</td>")
						.append("<td title='查看详细' onclick='show(this)'>"+res[i].content+"</td>")
						.append("<td>无</td>")
						.append("<td title='查看详细' onclick='show(this)'>"+res[i].standardAnswer.content+"</td>")
						.append($("<td>").append($a));
						$tr.appendTo($("#model-div"));
						$a.bind('click',function(){
								/* save(res[i]) */
								$(this).unbind('click');
								$(this).html("<i class='layui-icon' style='font-size: 25px; color: #1E9FFF;' title='删除'>&#xe640;</i>")
							});
						rows.push($tr)
					}
 					laypage.render({
 						elem:"pagenation3",
 		 				layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
 		 				limits: [5,10,15,20,25],
 		 				limit : 5,
 		 				curr:1,
 		 				count:rows.length,
 		 				jump:function(obj){
 		 					try{
 		 						for(var i=0; i < rows.length; i++){
 		 							rows[i].hide();
 		 						}
 		 						var start = (obj.curr-1)*obj.limit;
 		 						for(var i = start; i < start+obj.limit; i++){
 		 							rows[i].show()
 		 						}
 		 					}catch(e){
 		 						console.log(e);
 		 					}
 		 				}
 					});
 				}
 			})
 		}
 		function show(node){
 			layer.msg(node.innerHTML,{
 		        time: 20000, //20s后自动关闭
 		        btn: ['关闭']
 		      })
 		}