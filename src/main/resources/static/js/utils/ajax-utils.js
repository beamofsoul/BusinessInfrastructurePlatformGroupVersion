$(function() {
	
	$.del = function(url, data, successCallback, errorCallback) {
		$.ajax({
			type : 'DELETE',
			url : url,
			data : data,
			dataType : 'json',
			success : function(result) {
		    	if (successCallback) successCallback(result);
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		    	console.log(XMLHttpRequest.status+' readyState:'+XMLHttpRequest.readyState +' '+XMLHttpRequest.responseText);
		        if(errorCallback)  errorCallback('请求异常（代码:'+XMLHttpRequest.status+')');
		    }
		});
	}
		
	$.call = function(url) {
		$.ajax({
	        url: url,
	        success: function(data) {
	        	return data;
	        }
	    });
	}

	$.posty = function(url, data, successCallback, errorCallback) {
		$.ajax({
			headers: {'Accept': 'application/json','Content-Type': 'application/json'},
		    cache: false,
	        async: false,
		    type: 'POST',
		    url: url,
		    data: JSON.stringify(data),
		    dataType: 'json',
		    success: function(result) {
		    	if (successCallback) successCallback(result);
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		    	console.log(XMLHttpRequest.status+' readyState:'+XMLHttpRequest.readyState +' '+XMLHttpRequest.responseText);
		        if(errorCallback)  errorCallback('请求异常（代码:'+XMLHttpRequest.status+')');
		    }
		});
	}
	
	$.postify = function(url, data, fn) {
		$.ajax({
			headers: {'Accept': 'application/json','Content-Type': 'application/json'},
		    cache: false,
	        async: false,
		    type: 'POST',
		    url: url,
		    data: data,
		    dataType: 'json',
		    success: function(result) {
		    	if (fn) fn(result);
		    },
		    error: function(XMLHttpRequest, textStatus, errorThrown) {
		        console.log(XMLHttpRequest.responseText);
		    }
		});
	}
	
	$.gety = function(url, fn) {
		$.ajax({
			headers: {'Accept': 'application/json','Content-Type': 'application/json'},
		    cache: false,
	        async: false,
		    type: 'GET',
		    url: url,
		    dataType: 'json',
		    success: function(result) {
		    	if (fn) fn(result);
		    }
		});
	}
	
	$.igety = function(url, successCallback) {
		if(currentRequestMappingRootPath) url = currentRequestMappingRootPath+"/"+url;
		$.gety(url, successCallback);
	}
	
	$.iposty = function(url, data, successCallback, errorCallback) {
		if(currentRequestMappingRootPath)
			url = currentRequestMappingRootPath+"/"+url;
		if(!errorCallback) errorCallback = defaultErrorCallback;
		$.posty(url, data, successCallback, errorCallback);
	}
	
	$.idel = function(url, data, successCallback, errorCallback) {
		if(currentRequestMappingRootPath)
			url = currentRequestMappingRootPath+"/"+url;
		if(!errorCallback) errorCallback = defaultErrorCallback;
		$.del(url, data, successCallback, errorCallback);
	}
	
	function defaultErrorCallback(errorMessage){
		toastError(errorMessage);
	}
});