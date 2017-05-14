var currentRequestMappingRootPath;

//function initCurrentRequestMappingRootPath(currentRequestPath,baseRequestPath){
//	if(!currentRequestPath) return currentRequestMappingRootPath=null;
//	if(!baseRequestPath) baseRequestPath = 'admin' ;
//	
//	var pathArray = currentRequestPath.split('/');
//	for(var i=0;i< pathArray.length;i++){
//		if(baseRequestPath == pathArray[i]){
//			return currentRequestMappingRootPath = pathArray[i+1];
//		}
//	}
	
function initCurrentRequestMappingRootPath(currentRequestPath,baseRequestPath){
	if(!currentRequestPath) return currentRequestMappingRootPath=null;
	var pathArray = currentRequestPath.split('/');
	return currentRequestMappingRootPath = pathArray[0];
}