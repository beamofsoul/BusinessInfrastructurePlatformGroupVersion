/**
 * 将特定格式数组(非标准的类JSON数组)格式化为标准的JSON格式
 * @param data - {"user_name" : "tom","user_age" : 12,"user_parent_name" : "jim"}
 * @returns -  {"user" : {"name" : "tom", "age" : 12, "parent" : {"name" : "jim"}}}
 */
function formatNonstandardData2JSON(data){
    var newData = new Object();
    var m = 1;
    for(i in data){
//        console.log("第"+m+"次");
//        console.log("解析数据为"+i+":"+data[i]);
        var obj = formatOneElement(i, data[i]);
//        console.log("返回数据为"+obj);
//        console.log("合并前数据为"+JSON.stringify(newdata));
        newData = $.extend(true, newData, obj);
//        console.log("合并数据为"+JSON.stringify(newdata));
        m++;
    }
    return JSON.stringify(newData);
}

/**
 * 具体去格式化一个非标准JSON格式元素
 * @param str - userInfo_birthday
 * @param value - 1974-11-11
 * @returns Object {userInfo: Object}
 */
function formatOneElement(str,value){
    var robj = new Object();
    var result = str.split("_");
    var i = result.length-1;
    for(var j = i; j >= 0; j--){
        var obj = new Object();
        if(j == i){
            obj[result[j]] = value;
        }else{
            obj[result[j]] = robj;
        }
        robj = obj; 
    }
    return robj;
}

/**
 * 将传入的字符串或毫秒日期格式化为yyyy-MM-dd或yyyy-MM-dd HH:mm:ss格式
 * @param strTime 字符串或毫秒日期格式
 * @param showHour 是否显示HH:mm:ss
 * @returns 格式化后的字符串日期格式
 */
function formatDate (strTime, showHour) {
	var datetime = new Date(strTime);  
    var year = datetime.getFullYear();  
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;  
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();  
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();  
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();  
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return showHour ? 
    		year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second 
    		: year + "-" + month + "-" + date;
}

/**
 * 将传入的字符串或毫秒日期格式化为yyyy-MM-dd或yyyy-MM-dd 下午 HH:mm:ss格式
 * @param strTime 字符串或毫秒日期格式
 * @param showHour 是否显示HH:mm:ss
 * @returns 格式化后的字符串日期格式
 */
function formatDateAndEstimateTimePeriod (strTime, showHour) {
	var formatedDateStr = formatDate(strTime,showHour);
	if (showHour) {
		var content = formatedDateStr.split(' ');
		var time = content[1];
		var hour = parseInt(time.substring(0,2));
		var period = null;
		if (hour >= 13 && hour <= 18) period = '下午';
		else if (hour >= 19) period = '晚上';
		else if (hour >= 0 && hour <= 4) period = '凌晨';
		else if (hour > 4 && hour <= 8) period = '早晨';
		else if (hour > 8 && hour < 12) period = '上午';
		else period = '中午';
		content[1] = period + ' ' + time;
		return content.join(' ');
	}
	return formatedDateStr;
}

/**
 * 返回第二个输入时间与第一个输入时间的差，输出间隔多少天和多少个小时
 * @param pastTime - 靠前的时间(第一个时间)
 * @param currentTime - 靠后的时间(第二个时间)，默认当前时间
 * @returns 时间差，以天和小时计算
 */
function getTimeDifference(pastTime, currentTime) {
	if (!pastTime) return
	else pastTime = new Date(pastTime);
	if (!currentTime) currentTime = new Date();
	
	var diffHour = Math.round((currentTime - pastTime) / (60 * 60 * 1000));
	if (diffHour > 24) {
		var diffDay = Math.floor(diffHour / 24);
		diffHour = diffHour % 24;
		return diffDay + '天零' + diffHour + '小时';
	} else {
		return diffHour + '小时';
	}
}

/**
 * 转换data中 number boolean 数据类型为string ，返回一个新的结果对象
 * @param data 要转换的json对象
 * @returns 新的转换后的json对象
 */
function formatCopyObject2String(data){
	var copyObj = Object.assign({}, data);
	formatObject2String(copyObj);
	return copyObj;
}

/**
 * 在原有data中 转换data中 number boolean 数据类型为string
 * @param data 要转换的json对象
 */
function formatObject2String(data){
	var copyObj = data;
	var value;
	var itemType;
	for(var key in copyObj){
		value = copyObj[key];
		itemType = typeof value;
		if(itemType == 'object'){
			formatObject2String(value);
		}else if(itemType=='number'||itemType=='boolean'){
			copyObj[key] = String(value);
		}
	}
}

/**
 * 合并两个json ，以target属性为主 将sources对应的属性值覆盖掉target中同名属性值
 * @param target 包含属性的对象
 * @param sources 包含值的对象
 * @returns
 */
function mergeObjectSameAttribute(target,sources){
	var value;
	var itemType;
	for(var key in target){
		value = target[key];
		itemType = typeof value;
		if(itemType == 'object'){
			if(sources[key])
				mergeObjectSameAttribute(target[key],sources[key]);
		}else{
			target[key] = sources[key];
		}
	}
}



	 