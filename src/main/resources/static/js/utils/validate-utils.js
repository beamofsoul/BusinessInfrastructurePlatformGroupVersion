/**
 * 通过输入的权限动作到后台判断当前用户是否拥有此权限
 * @param action - 权限针对的动作
 * @returns isPermissive - 是否被授权
 */
function checkPermission(action) {
	var isPermissive;
	$.ajax({
		headers: {
	        'Accept': 'application/json',
	        'Content-Type': 'application/json'
	    },
	    cache: false,
        async: false,
	    type: 'POST',
	    url: projectPath + '/admin/permission/hasPermission',
	    data: JSON.stringify(action),
	    dataType: 'json',
	    success: function(data) {
	    	isPermissive = data.isPermissive;
	    }
	});
	return isPermissive;
}

/**
 * 判断是否选中的业务记录已经被使用，如果被使用则取消执行所有后续JS 
 * @param obj - 一个或多个业务对象id字符串，以逗号分隔
 */
function checkUsed(obj) {
	//判断是否当前业务对象已经被使用	
	var isUsed;
	$.ajax({
	    headers: {'Accept': 'application/json','Content-Type': 'application/json'},
	    cache: false,
        async: false,
	    type: 'POST',
	    url: 'isUsed',
	    data: obj,
	    dataType: 'json',
	    success: function(data) {
	    	isUsed = data.isUsed;
	    }
	});
	if (isUsed) {
		warn('所选记录已经被使用!');
		abort();
	}
}

/**
 * 判断是否选中的业务记录可以被删除，如果不能够被删除则取消执行所有后续JS 
 * @param obj - 一个或多个业务对象id字符串，以逗号分隔
 */
function checkCanBeDeleted(obj) {
	//判断是否选中的业务记录可以被删除	
	var canBeDeleted;
	$.ajax({
	    headers: {'Accept': 'application/json','Content-Type': 'application/json'},
	    cache: false,
        async: false,
	    type: 'POST',
	    url: 'canBeDeleted',
	    data: obj,
	    dataType: 'json',
	    success: function(data) {
	    	canBeDeleted = data.message;
	    }
	});
	if (canBeDeleted) {
		warn(canBeDeleted);
		abort();
	}
}

/**
 * 通过输入的url到后台验证是否data唯一
 * @param url - 指向后台特定验证方法的url
 * @param data - 需要传递给后台验证方法的数据
 * @param formKey - 如果formKey为update，则说明有可能数据库中应有一条记录(原始数据)与当前提交数据的唯一字段相吻合，需要进一步判断
 */
function checkUnique(url,data,id) {
	var isUnique;
	$.ajax({
	    headers: {
	        'Accept': 'application/json',
	        'Content-Type': 'application/json'
	    },
	    cache: false,
        async: false,
	    type: 'POST',
	    url: url,
	    data: JSON.stringify({'data':data,'id':id}),
	    dataType: 'json',
	    success: function(data) {
	    	isUnique = data.isUnique;
	    }
	});
	return isUnique;
}

/**
 * 根据表单key与表单验证规则判断
 * 如果判断规则不存在，则通过页面验证属性生成表单验证规则
 * 如果判断规则存在，则直接返回该规则
 * @param formKey - 表单key，如：add
 * @param rules - 表单验证规则，如：[{name: 'address', required: true}]
 * @returns var - 针对表单key指向表单的表单验证规则
 */
function generateValidateRules(formKey,rules) {
	var rs = [];
	if (rules == null || rules == [] || rules.length == 0) {
		$('#'+formKey+'Form').find('input[type=text],input[type=password],textarea').each(function() {
			if($(this).hasClass('required') || hasAttr($(this).attr('id'),'required')) {
				rs.push({name: $(this).attr('id').replace(formKey+'_',''), required: true});
			}
		});
		return rs;
	} else {
		return rules;
	}
}

/**
 * 根据表单key与表单验证规则判断，对表单中输入参数进行输入性验证
 * 如有不符合规则的输入，将通过warn/alert对用户进行警告，并返回是否通过表单验证
 * @param formKey - 表单key，如：add
 * @param rules - 表单验证规则，如：[{name: 'address', required: true}]
 * @returns boolean - true：通过表单验证，false：未通过表单验证
 */
function validateForm(formKey,rules) {
	//当业务.js未定义表单验证框架时，通过页面元素属性重新构建表单验证规则
	rules = generateValidateRules(formKey,rules);
	//遍历所有规则条目，并进行验证
	for(var index in rules) {
		obj = rules[index];
		//如当前条目不存在，自动进行下一条验证
		if (obj == null || !obj.name) continue;
		//获取当前条目中，验证对象的基本信息
		var elementId = generateElementId(formKey, obj.name);           //eg. add_username
		var element = getElementById(elementId);                        //eg. $('#add_username')
		var value = getInputValue(elementId);                           //eg. tom
		var displayName = getDisplayName(elementId).trims();            //eg. 用户名
		var id = getElementById(generateElementId(formKey, 'id')).val(); //eg. $('#add_id').val()
		
		var required = obj.required;
		//用于验证不必填但是又有其他验证规则的元素
		//规则为：有值就验证其他设定的验证规则，无值则跳过所有验证规则
		var solid = !(value.trim() == '' && !required);
		
		//判断当前页面元素值是否必填
		if ((required && (value == null || value.length == 0)) || hasClass(elementId,'required')) {
			warn('请输入'+displayName+'!');return false;
		}
		//判断当前页面元素值是否唯一
		//验证元素值唯一的前提是该元素值必须为必填
		if ((obj.unique && !checkUnique(obj.unique,value,id)) 
				|| (hasAttr(elementId,'unique') && !checkUnique(element.attr('unique'),value,id))) {
			warn('该'+displayName+'已经被使用!');return false;
		}
		//判断当前页面元素值能够通过特定正则表达式
		if (obj.regex && solid) {
			var regs = obj.regex;
			for(var i in regs) {
				var reg = regs[i];
				if (reg.rule && !reg.rule.test(value)) {
					warn(reg.warn);return false;
				}
			}
		}
		//判断当前页面元素值是否小于某特定值或特定页面元素值
		if ((obj.lessThan || hasAttr(elementId,'lessThan')) && solid) {
			var lessThan = obj.lessThan ? obj.lessThan : getElementById(elementId).attr('lessThan');
			if ((typeof(lessThan) == 'number' || !isNaN(Number(lessThan))) && value >= Number(lessThan)) {
				warn(displayName+'不能大于等于'+lessThan+'!');return false;
			} else {
				var lessThanTargetElementId = generateElementId(formKey, lessThan);
				var target = getElementById(lessThanTargetElementId);
				var targetValue = $(target).val();
				if (typeof($(target).val()) != 'undefined' && targetValue.trim() != '' && value >= Number(targetValue)) {
					warn(displayName+'不能大于等于'+getDisplayName(lessThanTargetElementId));return false;
				}
			}
		}
		//判断当前页面元素值是否大于某特定值或特定页面元素值
		if ((obj.greatThan || hasAttr(elementId,'greatThan')) && solid) {
			var greatThan = obj.greatThan ? obj.greatThan : getElementById(elementId).attr('greatThan');
			if ((typeof(greatThan) == 'number' || !isNaN(Number(greatThan))) && value <= Number(greatThan)) {
				warn(displayName+'不能小于等于'+greatThan+'!');return false;
			} else {
				var greatThanTargetElementId = generateElementId(formKey, greatThan);
				var target = getElementById(greatThanTargetElementId);
				var targetValue = $(target).val();
				if (typeof($(target).val()) != 'undefined' && targetValue.trim() != '' && value <= Number(targetValue)) {
					warn(displayName+'不能小于等于'+getDisplayName(greatThanTargetElementId));return false;
				}
			}
		}
		//判断当前页面元素值是否大于等于某特定值或特定页面元素值
		if ((obj.greatEqual || hasAttr(elementId,'greatEqual')) && solid) {
			var greatEqual = obj.greatEqual ? obj.greatEqual : getElementById(elementId).attr('greatEqual');
			if ((typeof(greatEqual) == 'number' || !isNaN(Number(greatEqual))) && value < Number(greatEqual)) {
				warn(displayName+'不能小于'+greatEqual+'!');return false;
			} else {
				var greatEqualTargetElementId = generateElementId(formKey, greatEqual);
				var target = getElementById(greatEqualTargetElementId);
				var targetValue = $(target).val();
				if (typeof($(target).val()) != 'undefined' && targetValue.trim() != '' && value < Number(targetValue)) {
					warn(displayName+'不能小于'+getDisplayName(greatEqualTargetElementId));return false;
				}
			}
		}
		//判断当前页面元素值是否小于等于某特定值或特定页面元素值
		if ((obj.lessEqual || hasAttr(elementId,'lessEqual')) && solid) {
			var lessEqual = obj.lessEqual ? obj.lessEqual : getElementById(elementId).attr('lessEqual');
			if ((typeof(lessEqual) == 'number' || !isNaN(Number(lessEqual))) && value > Number(lessEqual)) {
				warn(displayName+'不能大于'+lessEqual+'!');return false;
			} else {
				var lessEqualTargetElementId = generateElementId(formKey, lessEqual);
				var target = getElementById(lessEqualTargetElementId);
				var targetValue = $(target).val();
				if (typeof($(target).val()) != 'undefined' && targetValue.trim() != '' && value > Number(targetValue)) {
					warn(displayName+'不能大于'+getDisplayName(lessEqualTargetElementId));return false;
				}
			}
		}
		//判断当前页面元素值是否等于特定页面元素值
		if ((obj.equal || hasAttr(elementId,'equal')) && solid) {
			var equalTargetElementId = generateElementId(formKey, obj.equal ? obj.equal : getElementById(elementId).attr('equal'));
			var target = getElementById(equalTargetElementId);
			var targetValue = $(target).val();
			if (targetValue != null && targetValue.trim() != '' && value != Number(targetValue)) {
				warn(displayName+'需与'+getDisplayName(equalTargetElementId)+'一致!');return false;
			}
		}
		//判断当前页面元素值是否超过设定的最大长度
		if ((obj.maxLength || hasAttr(elementId,'maxLength')) && solid) {
			var maxLength = obj.maxLength ? obj.maxLength : getElementById(elementId).attr('maxLength');
			if ((typeof(maxLength) == 'number' || !isNaN(Number(maxLength))) && value.trim().length > Number(maxLength)) {
				warn(displayName+"的最大长度不能超过"+maxLength+'位!');return false;
			}
		}
		//判断当前页面元素值是否低于设定的最小长度
		if ((obj.minLength || hasAttr(elementId,'minLength')) && solid) {
			var minLength = obj.minLength ? obj.minLength : getElementById(elementId).attr('minLength');
			if ((typeof(minLength) == 'number' || !isNaN(Number(minLength))) && value.trim().length < Number(minLength)) {
				warn(displayName+"的最小长度不能低于"+minLength+'位!');return false;
			}
		}
	}
	//如都验证成功，返回true
	return true;
}

/**
 * 根据输入的页面元素id与元素属性名判断是否该页面元素中包含该属性
 * @param id - 页面元素id，如：add_username
 * @param attrName - 属性名，如：maxLength
 * @returns boolean - true：该页面元素保有该属性，false：该页面元素不存在该属性
 */
function hasAttr(id,attrName) {
	return typeof(getElementById(id).attr(attrName)) != 'undefined';
}

/**
 * 根据表单key与对象属性名，生成该页面元素在表单中的id
 * @param formKey - 表单key，如：add
 * @param id - 对象属性名，如：username
 * @returns var - 表单中页面元素id
 */
function generateElementId(formKey,id) {
	return formKey+'_'+id;
}

/**
 * 根据页面元素id返回其JQuery对象
 * @param id - 页面元素id，如：add_username
 * @returns $ - 该页面元素的JQuery对象
 */
function getElementById(id) {
	return $('#'+id);
}

/**
 * 根据页面元素id获取其显示名称
 * @param id - 页面元素id，如：add_username
 * @returns var - 页面元素显示名称，如：用户名称
 */
function getDisplayName(id) {
	return getElementById(id+'_label').html();
}

/**
 * 根据页面元素id获取其value值
 * @param id - 页面元素id，如：add_username
 * @returns var - trim后的value值
 */
function getInputValue(id) {
	var value = getElementById(id).val();
	if (typeof(value)=="undefined") {
		console.log(id);
		return "";
	}
	return value.trim();
}

/**
 * 判断是否页面元素有某种样式
 * @param id - 页面元素id，如：add_username
 * @param clazz - 页面元素保有的样式名，如：required
 * @returns boolean - true：有该样式，false：没有该样式
 */
function hasClass(id,clazz) {
	return $('#'+id).hasClass(clazz);
}