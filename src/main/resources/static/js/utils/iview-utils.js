// 加载分页业务数据用的URL
var loadPageableDataUrl;
// 当前用户操作的行为 - add、update、delete 等
var currentAction = null;
// 创建 iview table 
function createTable(columnNames,attributeNames,buttonsOnEachRow){
	if(columnNames.length != attributeNames.length) return;
	var tableColumnData = [];
	for (var i=0;i<attributeNames.length;i++) { 
//		//测试 日期
//		if(i==7){tableColumnData[i]={title:columnNames[i],key:attributeNames[i],render (row) {return formatDate(row.createDate,true);}};continue;}
		if('operation'==attributeNames[i]){
			tableColumnData[i]={title:columnNames[i],key:attributeNames[i],
				render (row, column, index) {
					return createTableRowButtons(buttonsOnEachRow,row, column, index);
				}
			};
		}else if('selection'==attributeNames[i]){
			tableColumnData[i] = {type: 'selection',width: 50,align: 'center'};
		}else{
			tableColumnData[i]={title:columnNames[i],key:attributeNames[i]};
		}
	}
	return tableColumnData;
}
//创建 iview table 行 button
function createTableRowButtons(buttonsOnEachRow,row, column, index){
	var btnStr = '';
	for (var btnIndex in buttonsOnEachRow){
		var btn = buttonsOnEachRow[btnIndex];
		var btnAttributes = btn.split('#');
		btn = btnAttributes[0];
		var btnName = btnAttributes[1] ? btnAttributes[1] : btnAttributes[0];
		btnStr +='<i-button type="text" size="small" @click="'+btn+'('+index+')">'+btnName+'</i-button>';
	}
	return btnStr;
}
/**
 * 生成综合查询form
 * @param iformName - form 绑定的data名称
 * @param queryFormItemName - form item label 数组
 * @param queryFormItems - form item 在data中的属性名
 * @param queryFormItemType - form item 的类型，如果是select 需要写成特定格式 select:option项的data
 * 
 */
function createTableQueryFrom(iformName,queryFormItemName,queryFormItems,queryFormItemType){
	var rowItemNum = 4;//每行放的个数
	var icolSpan = 24/rowItemNum-1;//24栅格
	var totalRow = queryFormItems.length/rowItemNum;
	if(queryFormItems.length%rowItemNum!=0){
		totalRow ++;
	}
	
	var itemIndex = 0;
	var queryForm = '<i-form ref="'+iformName+'" :model="'+iformName+'"  :show-message="false" label-position="left" :label-width="80" >';
	//行
	for(var rowIndex = 0;rowIndex<totalRow;rowIndex++){
		queryForm+='<Row type="flex" justify="space-between" >';
		//项
		for(var rowItemIndex = 0;rowItemIndex<rowItemNum;rowItemIndex++){
			queryForm+='<i-col span="'+icolSpan+'">';
			if(queryFormItemType[itemIndex]=='input'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<i-input v-model="'+iformName+'.'+queryFormItems[itemIndex]+'" ></i-input>';
				queryForm+='</Form-item>';
			}else if(queryFormItemType[itemIndex].indexOf(':')!=-1){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<i-select v-model="'+iformName+'.'+queryFormItems[itemIndex]+'" clearable>';
				queryForm+='<i-option v-for="item in '+queryFormItemType[itemIndex].slice(queryFormItemType[itemIndex].indexOf(':')+1)+'" :value="item.value" :key="item">{{ item.label }}</i-option>';
				queryForm+='</i-select>';
				queryForm+='</Form-item>';
			}else if(queryFormItemType[itemIndex]=='date'){
				queryForm+='<Form-item label="'+queryFormItemName[itemIndex]+'：" prop="'+queryFormItems[itemIndex]+'">';
				queryForm+='<Date-picker placement="bottom" v-model="'+iformName+'.'+queryFormItems[itemIndex]+'" type="date"  ></Date-picker>';
				queryForm+='</Form-item>';
			}
			queryForm+='</i-col>';
			itemIndex++;
		}
		queryForm+='</Row>';
	}
	
	queryForm+='<Row type="flex" justify="center" >';
	queryForm+='<i-col span="'+icolSpan+'">';
	queryForm+='<i-button icon="ios-search"  @click="querySubmit()" >查询</i-button>';
	queryForm+='<i-button icon="ios-search"  @click="formReset(\''+iformName+'\')"  style="margin-left: 8px">重置</i-button>';
	queryForm+='</i-col>';
	queryForm+='</Row>';
	queryForm+='</i-form>';
	return queryForm;
}

