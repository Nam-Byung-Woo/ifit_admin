$(function(){
	$(".adminGnbArea .logo").click(function(){
		$(location).attr("href","/index.ifit");
	});
	
	$('.number').trigger('keyup');
	
	$.fn.wSelect.defaults.size = 6;
	$("select").wSelect();
	if($("select.colorPreview").length>0){
		var selectObj = $("select.colorPreview");
		var wSelectObj = selectObj.next();
		wSelectObj.find(".wSelect-option-value").each(function(index){
			$(this).html("<p></p>" + $(this).html());
			$(this).find("p").css("background-color", selectObj.find("option:eq("+index+")").attr("title"));
		});
	}
	
	$("#loginBtn").click(function(){
		$("#loginForm").submit();
	});
	$("#logoutBtn").click(function(){
		$(location).attr("href","/logoutAction.ifit");
	});
	$(".yet").click(function(){
		alert("준비중 입니다.");
		return false;
	});
	
	$(".listSearchBtn").click(function(){
		$(this).parents("form").submit();
	});
	
	$(".listSort").click(function(){
		$(this).parents("form").find("#sortCol").val($(this).attr("data-sort-col"));
		var sortValObj = $(this).parents("form").find("#sortVal");
		sortValObj.val(sortValObj.val()=="DESC" ? "ASC" : "DESC");
		$(this).parents("form").find("#pageNum").val(1);
		$(this).parents("form").submit();
	});
	
	$("#countPerPage").change(function(){
		$(this).parents("form").submit();
	});
	
	$(".writeBtn").click(function(){
		var queryIncode = $(this).parents("form").find("#queryIncode").val();
		$(location).attr("href",$(this).parents("form").attr("id")+"Editor.ifit?queryIncode=" + queryIncode);
	});
	
	$(".editBtn").click(function(){
		var queryIncode = $(this).parents("form").find("#queryIncode").val();
		
		$(location).attr("href",$(this).parents("form").attr("id")+"Editor.ifit?queryIncode=" + queryIncode + "&isUpdateMode=" + true + "&seq=" + $(this).attr("data-seq"));
	});
	
	$(".listBtn").click(function(){
		var queryDecode = $(this).parents("form").find("#queryDecode").val();
		$(location).attr("href",$(this).parents("form").attr("id")+"List.ifit?" + queryDecode);
	});
	
	$(".listAllCheck").change(function(){
		if($(this).is(":checked")){
			$(".listItemCheck").prop("checked",true);
			$(".listItemCheck").attr("checked",true);
        }else{
        	$(".listItemCheck").prop("checked",false);
        	$(".listItemCheck").attr("checked",false);
        }
	});
	
	$(".deleteBtn").click(function(){
		var itemCount = 0;
		$(".listItemCheck").each(function(){
			if($(this).is(":checked")){
				itemCount++;
			}
		});
		if(itemCount <= 0){
			alert("선택한 항목이 없습니다.");
			return false;
		}
		if(confirm("선택한 " + itemCount + "개 항목을 삭제하시겠습니까?")){
			$(this).parents("form").attr("action", $(this).parents("form").attr("id")+"DeleteAction.ifit");
			$(this).parents("form").submit();
		}
	});
	
	$(".tabArea li").click(function(){
		$(location).attr("href",$(location).attr("pathname")+"?tabID="+$(this).attr("data-tabID"));
	});

});

//숫자 입력시
$(document).on("keyup",".number",function(e){
	$(this).val($(this).val().replace(/[^0-9]/gi,""));		// 숫자만 입력
	if($(this).val() > 1000000000){
		$(this).val("1000000000");
	}
	$(this).val($(this).val().replace(/(^0+)/,""));		// 앞의 0제거
	$(this).val(addMoneyComma($(this).val()));
});

$(document).on("click",".paging ul li a.btnOff",function(e){
	e.preventDefault();
	var pageNum = $(this).attr("id");
	pageNum = pageNum.replace("page_","");
	$(this).parents("form").find("#pageNum").val(pageNum);
	$(this).parents("form").submit();
});

$(document).on("click",".writeActionBtn",function(e){
	if(validateCheck($(this).parents("form"))){
		if(confirm($(this).parents("form").attr("data-confirm-msg"))){
			return true;
		}
	}
	return false;
});

$(document).on("click",".updateActionBtn",function(e){
	if(validateCheck($(this).parents("form"))){
		if(confirm($(this).parents("form").attr("data-confirm-msg"))){
			return true;
		}
	}
	return false;
});

function getAjaxData(data){
	console.log(data);
	var rtnData = "";
//	$("body").waitMe({
//		effect : 'bounce',
//		text : '데이터 처리중 입니다. 잠시만 기다려 주세요.',
//		bg : 'rgba(255,255,255,0.5)',
//		color : '#000'
//	});
	$.ajax({
		url: data.url,
		type: "post",
		data: {"data":JSON.stringify(data)},
		dataType:"text",
		async: false,
		success:function(result){
			console.log(result);
			rtnData = result;
		},
		error: function(xhr,status, error){
			alert("에러발생");
		}
	});
//	$("body").waitMe('hide');
	return rtnData;
}

function validateCheck(obj){
	var rtn = false;
	var data = {"formID":obj.attr("id")+obj.attr("data-mode")};
	obj.find("input[name!='']").each(function(){
		if($(this).attr("type")=="file"){
			data[$(this).attr("name")] = $(this)[0].jFiler.files_list.length;
		}else if($(this).attr("type")=="radio"){
			data[$(this).attr("name")] = $(':radio[name="'+$(this).attr("name")+'"]:checked').val();
		}else{
			if($(this).attr("name") in data && $.isArray(data[$(this).attr("name")])){
				data[$(this).attr("name")].push($(this).val());
			}else{
				data[$(this).attr("name")] = data[$(this).attr("name")] = $(this).hasClass("arrayData") ?  [$(this).val()] : $(this).val();
			}
		}
	});
	obj.find("select[name!='']").each(function(){
		data[$(this).attr("name")] = $(this).val();
	});
	obj.find("textarea").each(function(){
		data[$(this).attr("name")] = $(this).val();
	});
	
	data.url = "/ajaxFormValidate.ifit";
	var jsonObj = JSON.parse(getAjaxData(data));
	if(!jsonObj.res){
		alert(jsonObj.msg);
		$("#"+jsonObj.elementID).focus();
	}else{
		rtn = true;
	}
	
	return rtn;
}

function addMoneyComma(num){
	var temp = num+"";
	return temp.replace(/\B(?=(\d{3})+(?!\d))/g, ",");	// 3자리마다 콤마 
}