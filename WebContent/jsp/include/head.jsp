<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<title>iFit 관리자</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/jquery/jquery-ui-1.11.2.custom/jquery-ui.min.css" />
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/font-awesome-4.6.1/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/wSelect/wSelect.css" />
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/farbtastic-1.2/farbtastic.css" />
<link rel="stylesheet" type="text/css" href="/jsp/common/css/general.css" />
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/jQuery.filer-1.0.5/css/jquery.filer.css" />
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/jQuery.filer-1.0.5/css/themes/jquery.filer-dragdropbox-theme.css" />
<link rel="stylesheet" type="text/css" href="/jsp/common/plugin/waitMe/waitMe.min.css" />

<script type="text/javascript" src='/jsp/common/plugin/jquery/jquery-1.11.1.min.js'></script>
<script type="text/javascript" src="/jsp/common/plugin/jquery/jquery-ui-1.11.2.custom/jquery-ui.min.js"></script>
<script type="text/javascript" src='/jsp/common/plugin/wSelect/wSelect.min.js'></script>
<script type="text/javascript" src='/jsp/common/plugin/farbtastic-1.2/farbtastic.js'></script>
<script type="text/javascript" src="/jsp/common/plugin/jQuery.filer-1.0.5/js/jquery.filer.js"></script>
<script type="text/javascript" src="/jsp/common/plugin/waitMe/waitMe.min.js"></script>
<script type="text/javascript" src='/jsp/common/js/admin.js'></script>
<script type="text/javascript" src='/jsp/common/js/layerPopup.js'></script>
<script type="text/javascript">
<!--
	var alertMsg = "<s:property value="#session.alertMsg"/>";
	if(alertMsg != ""){
		alert(alertMsg);
		<s:set name="alertMsg" value="" scope="session"/>
	}

	var validateMsgMap = {
		res : "${validateMsgMap.res}",
		elementID : "${validateMsgMap.elementID}",
		msg : "${validateMsgMap.msg}"
	};
	if(validateMsgMap.res == "false"){
		alert(validateMsgMap.msg);
		$("#"+validateMsgMap.elementID).focus();
	}
//-->
</script>