package util.config;

public class Paging{
    
    public Paging(){
    }
    
    public String getPaging(int totalCount, int currentPage, int countPerPage){
    	String pagingHTML = "";
    	int endTotalPage = ( totalCount % countPerPage > 0 ) ? (totalCount / countPerPage) +1 : totalCount / countPerPage;
    	int startNum = (currentPage - Code.pagingGroupNum);
    	int endNum = (currentPage + Code.pagingGroupNum);
//    	endTotalPage = 7;
    	
    	if(startNum < 1){
    		int addNum = 1 - startNum;
    		startNum = 1;
    		endNum = endNum + addNum;
    	}
    	if(endNum > endTotalPage){
    		int dropNum = endNum - endTotalPage;
    		endNum = endTotalPage;
    		startNum = startNum - dropNum;
    		startNum = startNum < 0 ? 1 : startNum;
    	}
    	
    	if(totalCount != 0){
    		pagingHTML += "<ul>";
    		if(startNum != endNum && currentPage != 1){
    			pagingHTML += "<li><a href='#' class='btnOff' id='page_1'>&lt;&lt;</a></li>";
    		}
    	//	pagingStr += '<li><a href="#" class="btnOff">&lt;</a></li>';
    		for(int i=startNum; i<=endNum; i++){
    			if(currentPage == i){
    				pagingHTML += "<li><span class='btnOn'>" + i + "</span></li>";
    			}else{
    				pagingHTML += "<li><a href='#' class='btnOff' id='page_" + i + "'>" + i + "</a></li>";
    			}
    			
    		}
    	//	pagingStr += '<li><a href="#" class="btnOff">&gt;</a></li>';
    		if(startNum != endNum && currentPage != endTotalPage){
    			pagingHTML += "<li><a href='#' class='btnOff' id='page_" + endTotalPage + "'>&gt;&gt;</a></li>";
    		}
    		pagingHTML += "</ul>";
    	}
//    	$(".paging").html(pagingStr);
    	
    	return pagingHTML;
    }
    
}