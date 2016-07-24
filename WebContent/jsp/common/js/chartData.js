function chartInit(obj){
    google.charts.load('current', {'packages':['corechart']});
    google.charts.setOnLoadCallback(function(){
    	drawChart(obj);
    });
}

function drawChart(obj){
	var chart;
	var data = getChartData(obj);
    var options = {'title':'',
                   'width':800,
                   'height':500,
                   sliceVisibilityThreshold:0
                   };
    
	chart = new google.visualization.PieChart(document.getElementById(obj.type));
    chart.draw(data, options);
}

function getChartData(obj){
	var data = {
		"url":"/ajaxGetData.ifit",
		"dataKind":"chartData",
		"chartType":obj.type,
		"queryData":obj.queryData
	};
	var jsonObj = JSON.parse(getAjaxData(data));
	var chartData = new google.visualization.DataTable();
	chartData.addColumn('string', 'key');
	chartData.addColumn('number', 'val');
	for(var i=0; i<jsonObj.length; i++){
		chartData.addRows([[jsonObj[i].key, parseInt(jsonObj[i].val)]]);
	}
	
	console.log(chartData);
	
	return chartData;
}