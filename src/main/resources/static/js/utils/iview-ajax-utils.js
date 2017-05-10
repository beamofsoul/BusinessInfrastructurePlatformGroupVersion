$.iposty = function(url, data, successCallback, errorCallback) {
	if(!errorCallback) errorCallback = defaultErrorCallback;
	$.posty(url, data, successCallback, errorCallback);
}

$.idel = function(url, data, successCallback, errorCallback) {
	if(!errorCallback) errorCallback = defaultErrorCallback;
	$.del(url, data, successCallback, errorCallback);
}

function defaultErrorCallback(errorMessage){
	toastError(errorMessage);
}