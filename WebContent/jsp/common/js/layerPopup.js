$(function(){
	
	$(document).on("click",".openPop",function(e){
		var layerObj = $(".layer-"+$(this).attr("data-pop-id"));
		layer_init(layerObj);
	});
	
	// 글 보기 레이어팝업
	$(".viewBtn").click(function(){
		var dataKind = $(this).parents("form").attr("id");
		var layerObj = $(".layer-"+dataKind+"View");
		layer_init(layerObj);
		
		var data = {
				"dataKind":dataKind,
				"seq":$(this).attr("data-seq"),
				"url":"/ajaxGetData.ifit"
		};

		layerObj.find("#jsonObj").val(getAjaxData(data));
		layerObj.find("#jsonObj").trigger("change");
		
//		for(key in jsonObj) {
//			if((jsonObj[key]).constructor == Object){
//				console.log("Object::" + key);
//				for(key2 in jsonObj[key]) {
//					layerObj.find("#"+dataKind+"_"+key).html(jsonObj[key]);
//					$("#" + key + "-" + key2).html(jsonObj[key][key2]);
//				}
////				layerObj.find("#"+dataKind+"_"+key).html(jsonObj[key]);
//			}else if((jsonObj[key]).constructor == Array){
//				console.log("Array::" + key);
//				for(key2 in jsonObj[key]) {
////					layerObj.find("#"+dataKind+"_"+key).html(jsonObj[key]);
////					$("#" + key + "-" + key2).html(jsonObj[key][key2]);
//					console.log(key2);
//				}
////				layerObj.find("#"+dataKind+"_"+key).html(jsonObj[key]);
//			}
////			layerObj.find("#"+dataKind+"_"+key).html(jsonObj[key]);
//		}
		
	});
	
	function layer_init(layerObj){
		var layer = layerObj.find("#popLayer");
		var bg = layer.prev().hasClass('bg');    //dimmed 레이어를 감지하기 위한 boolean 변수
		if(bg){
			$(layerObj).fadeIn();   //'bg' 클래스가 존재하면 레이어가 나타나고 배경은 dimmed 된다.
		}else{
			layer.fadeIn();
		}
		
		layer_location_setting(layer);
		
		//반투명된 배경 스크롤 막기
		$("body").css("overflow-x","hidden");
		$("body").css("overflow-y","hidden");
//		$("body").on("mousewheel.disableScroll DOMMouseScroll.disableScroll touchmove.disableScroll", function(e) {
//	        e.preventDefault();
//	        return;
//	    });
	    $(window).on("keydown.disableScroll", function(e) {
	        var eventKeyArray = [33, 34, 35, 36, 37, 38, 39, 40];
	        for (var i = 0; i < eventKeyArray.length; i++) {
	            if (e.keyCode === eventKeyArray [i]) {
	                e.preventDefault();
	                return;
	            }
	        }
	    });
	    
	    layer.find('.layerClose').off("click").on("click",function(e){
	    	layer_close(layerObj);
		});
	    
	    $('.layer .bg').off("click").on("click",function(e){  //배경을 클릭하면 레이어를 사라지게 하는 이벤트 핸들러
			//layer_close(layerObj);
		});
	}
	
	function layer_location_setting(layer){
		// 화면의 중앙에 레이어를 띄운다.
		if( layer.outerHeight() < $(document).height() ){
			layer.css('margin-top', '-'+layer.outerHeight()/2+'px');
		}else{
			layer.css('top', '0px');
		}
		if( layer.outerWidth() < $(document).width() ){
			layer.css('margin-left', '-' + layer.outerWidth()/2+'px');
		}else{
			layer.css('left', '0px');
		}
	}
	
	function layer_close(layerObj){
		var closeAnswer = layerObj.attr("data-close-answer")=="true" ? true : false;
		if(closeAnswer){
			if(!confirm("작업내역이 저장되지 않았습니다.\n작성을 종료하시겠습니까?")){
				return false;
			}
		}
		$(".layer").fadeOut(); 	//레이어를 사라지게 한다.
		$("body").css("overflow-y","auto");
		$(window).off(".disableScroll");
		
//		$(".validate").each(function(){
//			if($(this).prop("tagName")=="INPUT"){
//				$(this).val("");
//			}else if($(this).prop("tagName")=="TEXTAREA"){
//				$(this).val("");
//			}else if($(this).prop("tagName")=="SELECT"){
//				$(this).find("option:eq(0)").attr("selected","selected");
//			}else if($(this).prop("tagName")=="P"){
//				$(this).html($(this).attr("data-default"));
//			}
//		});
//		layerObj.find(".wSelect-options").children().removeClass("wSelect-option-selected");
//		layerObj.find(".wSelect-option:first-child").addClass("wSelect-option-selected");
//		layerObj.find(".wSelect-selected").html("선택");
	}
});
