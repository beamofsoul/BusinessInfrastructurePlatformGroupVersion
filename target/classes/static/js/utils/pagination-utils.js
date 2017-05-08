var tableContainer = 'tableContainer';
var globalSize; //全局参数 - 每页显示多少条记录，当当前页刷新时此参数更新
var globalSort; //全局参数 - 当前页排序规则，包括数据列和升降序属性

var loadPageableDataUrl; //加载分页业务数据用的URL
var loadPageableDataCallback; //加载分页业务数据的回调方法
var loadPageableDataBefore; //加载分页业务数据方法执行前该执行的方法
var loadPageableDataAfter; //加载分页业务数据方法执行后该执行的方法
var documentReady; //页面加载好后的预留方法

$(document).ready(function () {
  //如果页面中没有tableContainer对象
  //说明该页面没有需要加载数据的table
  //则不需要加载数据和定义table对象高度
  if ($('div#' + tableContainer).length == 0)
    return;

  //初始化页面数据
  initPageableData();

  //执行页面加载好后的预留方法
  if (documentReady)
    documentReady();
});

/**
 * 根据当前页码和是否采用异步方式加载分页数据
 * page: 当前页码 - 第一页从0开始
 * async: 是否采用异步加载方式 - 首次加载页面不能使用异步加载,需要等数据加载完以后才执行其他JS方法
 * size: 每页显示多少行数据
 * sort: 按照那个字段(必须为数据表格对象的属性名称)排序
 * direction: 按照升序或降序排列，取值为0(asc)或1(desc)
 */
function initPageableData(page, async, size, sort, direction) {
  //执行加载分页数据执行前需要执行的方法
  if (loadPageableDataBefore)
    loadPageableDataBefore(page, async, size, sort, direction);
  //如果不传入当前页码,默认当前页码为0
  if (!page)
    page = 0;
  if (typeof (async) == "undefined")
    async = false;
  //是否选择了每页展示数量下拉菜单中的选项才进入此方法,默认为是
  var isSelectRecordNumber = true;
  //首次加载数据时或每次点击页码按钮时
  if (!size) {
    //如果用户再次点选了已经选中的页码按钮
    //比如,在第一页点选页码按钮1
    //防止重新获取重复的数据
    if (isAccessCurrentPage(page, async))
      return;
    //确定不是点选每页展示数量菜单中按钮进入此方法
    //有可能不需要重新初始化记录数量信息提示条
    isSelectRecordNumber = false;
    //获取默认每页记录数量
    //第一次加载页面时获取默认值(async为false时为第一次加载页码)
    //其他通过页码按钮加载页码数据时获取用户选择的每页加载数量
    size = async ? getHowManyRecords() : getFirstRecordNumber();
  }
  //如果不传入排序列与升降序参数,默认为""和0(升序)
  sort = getDefaultSortValue(sort, direction);
  if (!direction)
    direction = 0;
  //从后台获取数据并解析
  $.ajax({
    headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
    cache: false,
    async: async,
    type: 'POST',
    url: loadPageableDataUrl,
    data: "{\"page\":" + page + ",\"size\":" + size + ",\"sort\":\"" + sort + "\",\"direction\":" + direction + "}",
    dataType: 'json',
    success: function (data) {
      //更新全局参数-当前页排序规则，存储其值以便用户再次点击排序时，防止重复排序
      globalSort = sort + "#" + direction;
      //更新全局参数-每页显示多少条记录，存储记录数以便在其他控件事件中调用，如实现数据表格列排序时被调用
      //此参数需在生成表格前被赋值或更新，否则将影响调用此参数控件的功能
      globalSize = data.pageableData.size;
      //加载页面表单和数据
      $('div#' + tableContainer).html(loadPageableDataCallback(data));
      //加载完数据后,重新初始化页码按钮的超链接和激活状态(底色)
      initPageButtons(page, data.pageableData);
      //加载完数据后,重新初始化全选按钮状态,默认为未选中
      initCheckboxesAndToogles();

      //执行加载分页数据完成后需要执行的方法
      if (loadPageableDataAfter)
        loadPageableDataAfter(data);
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      console.log(XMLHttpRequest.responseText);
    }
  });
}

/**
 * 根据当前页码、是否采用异步方式和查询条件加载分页数据
 * page: 当前页码 - 第一页从0开始
 * async: 是否采用异步加载方式 - 首次加载页面不能使用异步加载
 *        ,需要等数据加载完以后才执行其他JS方法
 * size: 每页显示多少行数据
 * condition: JSON格式的查询条件 
 */
function initPageableDataByCondition(page, async, size, condition) {
  //以下代码挑拣复制于function initPageableData(page,async,size,sort,direction)
  //不需要进行重构，考虑到后续开发可能再次对此方法进行修改
  if (loadPageableDataBefore)
    loadPageableDataBefore(page, async, size, sort, direction);
  if (!page)
    page = 0;
  if (typeof (async) == "undefined")
    async = false;
  var isSelectRecordNumber = true;
  if (!size) {
    if (isAccessCurrentPage(page, async))
      return;
    isSelectRecordNumber = false;
    size = async ? getHowManyRecords() : getFirstRecordNumber();
  }

  var data = JSON.stringify({page: page, size: size, condition: condition});
  $.ajax({
    headers: {'Content-Type': 'application/json'},
    async: async,
    type: 'POST',
    url: loadPageableDataUrl,
    data: data,
    dataType: 'json',
    success: function (data) {
      globalSize = data.pageableData.size;
      $('div#' + tableContainer).html(loadPageableDataCallback(data));
      initPageButtons(page, data.pageableData);
      initCheckboxesAndToogles();
      if (loadPageableDataAfter)
        loadPageableDataAfter(data);
    }
  });
}

/**
 * 加载分页数据的回调方法的具体实现方法
 */
var loadPageableDataCallback0 = function(data, noCheckbox, securityKey) {
	data = data.pageableData;
	var value = [];
	for(var i=0;i<data.numberOfElements;i++) {
		value[i] = parseValuesOnEachRow(data.content[i]);
	}
	return generateDefaultDataTable(columnNames,attributeNames,value,noCheckbox,securityKey);
};

/**
 * 加载数据完成后,重新初始化全选按钮状态,默认为未选中
 */
function initCheckboxesAndToogles() {
  //全选checkbox单击事件
  $(':checkbox#all').click(function () {
    var isAllChecked = $(':checkbox#all').is(':checked');
    $(':checkbox.idc').each(function () {
      $(this).prop('checked', isAllChecked);
    });
  });
  //加载数据完成后,重新初始化全选按钮状态,默认为未选中
  $(':checkbox#all').prop('checked', false);
}

/**
 * 获取当前被选中的页码按钮所代表的页数
 * 返回值currentPage从0开始
 */
function getCurrentPageNumber() {
  var currentPage = 0;
  $('ul.pagination').find('li').each(function () {
    if ($(this).hasClass('am-active')) {
      currentPage = $(this).find('a').find('span').html() - 1;
    }
  });
  return currentPage;
}

/**
 * 获取页码按钮列表中最后一个按钮的index值
 * PS: 返回的页码为页码按钮的html内容,不是$('ul.pagination')下li数组的下标值
 */
function getLastPageNumber() {
  return parseInt($('ul.pagination').find('li:last-child').prev().prev().find('a').find('span').html());
}

/**
 * 根据输入的页码,跳转到特定页
 * @param page : 输入的页码为页码按钮的html内容,使用时应减一,变成$('ul.pagination')下li数组的下标值进行操作
 */
function gotoPageNumber(page) {
  $('ul.pagination').find('li[id=' + (page - 1) + 'li]').find('a').find('span').trigger('click');
}

/**
 * 判断是否用户在众多页码按钮中点选了已经成为当前页码的按钮
 * 比如,当页码按钮1被选中的时候,点选页码按钮1
 * async: 为true时,页面不是首次加载
 * page: 当前需要加载的页码,从0开始
 */
function isAccessCurrentPage(page, async) {
  return (async && page == getCurrentPageNumber());
}

/**
 * 重新初始化页码按钮的超链接和激活状态(底色)	
 */
function initPageButtons(page, data) {
  //根据当前用户选择的页码将该页码按钮颜色变蓝
  //,而其他页码按钮颜色恢复成默认颜色
  $('ul.pagination').find('li').each(function () {
    if ($(this).hasClass('am-active')) {
      $(this).removeClass('am-active');
    }
  });
  $('#' + page + 'li').addClass('am-active');

  //生成分页按钮
  generatePageButtons(data.totalPages, page);

  //给页码解释赋值
  var base = page * data.size;
  setFirstRecord(data.numberOfElements !== 0 ? base + 1 : 0);
  setLastRecord(base + data.numberOfElements);
  setTotalRecords(data.totalElements);
  setHowManyRecords(data.size);
}

/**
 * 根据总页码数量生成页码按钮
 * ,只有第一次加载页面时才被使用
 */
function generatePageButtons(totalPages, currentPage) {
  var html = '<li id="firstLi"><a id="firstA"><span>&laquo;</span></a></li>';
  //如果当前总页数为0,说明当前页没有数据
  //生成一个页码按钮
  if (totalPages === 0) {
    html += '<li id="0li" class="am-active"><a href="#"><span>1</span></a></li><li id="lastLi"><a id="lastA" disabled><span>&raquo;</span></a></li>';
    $('li#firstLi').addClass('am-disabled');
  } else {
    var curPage = currentPage + 1, halfInBetween = 0;
    //上一页
    if (curPage > 1) {
      html = '<li id="firstLi"><a id="firstA" href="javascript:initPageableData(0,true);generatePageButtons('
          + totalPages + ',0);"' + '><span>&laquo;</span></a></li>';
      html += '<li id="prevA"><a id="prevA" href="javascript:initPageableData(' + (currentPage - 1)
          + ',true);generatePageButtons(' + totalPages + ',' + (currentPage - 1) + ');"' + '><span>上一页</span></a></li>';
    } else {
      html = '<li id="firstLi"><a id="firstA" disabled><span>&laquo;</span></a></li>';
      html += '<li id="prevA"><a id="prevA" disabled><span>上一页</span></a></li>';
    }
    //中间页码
    if (curPage !== 1 && curPage >= 4 && totalPages !== 4) {
      html += '<li id="0li"><a href="javascript:initPageableData(0,true); generatePageButtons('
          + totalPages + ',0);">' + '<span>1</span></a></li>';
    }
    if (curPage - 2 > 2 && curPage <= totalPages && totalPages > 5) {
      halfInBetween = parseInt((curPage - 2) / 2);
      html += '<li><a href="javascript:initPageableData(' + halfInBetween + ',true);"><span>...</span></a></li>';

    }
    var start = curPage - 2, end = curPage + 2;
    if ((start > 1 && curPage < 4) || curPage === 1) {
      end++;
    }
    if (curPage > totalPages - 4 && curPage >= totalPages) {
      start--;
    }
    for (; start <= end; start++) {
      if (start <= totalPages && start >= 1) {
        if (start === curPage) {
          html += '<li id="' + (start - 1) + 'li" class="am-active"><a href="javascript:"><span>' + start + '</span></a></li>';
        } else {
          html += '<li id="' + (start - 1) + 'li"><a href="javascript:initPageableData('
              + (start - 1) + ',true);generatePageButtons(' + totalPages + ',' + (start - 1) + ');"><span>' + start + '</span></a></li>';
        }
      }
    }
    if (curPage + 2 < totalPages - 1 && curPage >= 1 && totalPages > 5) {
      halfInBetween = parseInt((totalPages - 1 - (curPage + 2)) / 2) + (curPage + 2);
      html += '<li><a href="javascript:initPageableData(' + halfInBetween + ',true);"><span>...</span></a></li>';
    }
    if (curPage !== totalPages && curPage < totalPages - 2
        && totalPages !== 4) {
      html += '<li id="' + (totalPages - 1) + 'li"><a href="javascript:initPageableData(' + (totalPages - 1)
          + ',true);generatePageButtons(' + totalPages + ',' + (totalPages - 1) + ');"' + '><span>' + totalPages + '</span></a></li>';
    }
    //下一页
    if (curPage < totalPages) {
      html += '<li id="nextA"><a id="nextA" href="javascript:initPageableData(' + curPage
          + ',true);generatePageButtons(' + totalPages + ',' + curPage + ');"' + '><span>下一页</span></a></li>';
      html += '<li id="lastLi"><a id="lastA" href="javascript:initPageableData(' + (totalPages - 1)
          + ',true);generatePageButtons(' + totalPages + ',' + (totalPages - 1) + ');"><span>&raquo;</span></a></li>';
    } else {
      html += '<li id="nextA"><a id="nextA" disabled><span>下一页</span></a></li><li id="lastLi"><a id="lastA" disabled><span>&raquo;</span></a></li>';
    }
  }
  $('ul.pagination').html(html);
}

/**
 * 当点选数据行头部的复选框时,复选框将被选中或取消选择
 * BugResolve: 如不加此方法,点选头部复选框将有几率无法响应选中事件
 */
function invertSelectRow(element) {
  $(element).prop('checked', !$(element).is(':checked'));
}

/**
 * 当点选数据行时,选中或取消选中当前行的复选框
 * 检查是否页面所有复选框已经被选中
 */
function selectRow(element) {
  var checkbox = $(element).children('td').eq(0).find('input');
  $(checkbox).prop('checked', !$(checkbox).is(':checked'));
  checkAll();
}

/**
 * 根据输入的行id,选中该行记录前复选框
 */
function selectRowById(elementId) {
  selectRow($('tr#' + elementId));
}

/**
 * 检查当所有页面记录复选框被选中时,将全选勾中
 */
function checkAll() {
  var state = true;
  $(':checkbox.idc').each(function () {
    if (!$(this).is(':checked'))
      state = false;
  });
  $(':checkbox#all').prop("checked", state);
}

/**
 * 选中或取消所有dataTable头部的复选框
 */
function doCheckAll(state) {
  $(":checkbox.idc, #all").attr("checked", state);
}

/**
 * 计算并返回当前页面被选中记录前的复选框数量，不包括全选复选框
 */
function countCheckedbox() {
  return $(':checkbox.idc:checked').length;
}

/**
 * 计算并返回当前页数据表格中一共有多少条数据
 */
function countExistRow() {
  return $(':checkbox.idc').length;
}

/**
 * 获取所有被选中记录的id，并以逗号作为间隔连成的字符串格式返回
 */
function getCheckedIds() {
  var ids = '';
  $(':checkbox.idc:checked').each(function () {
    ids += $(this).attr('id') + ',';
  });
  return ids == '' ? ids : ids.substring(0, ids.length - 1);
}

/**
 * 根据业务对象Id和属性名称获取其在数据表格中的JQuery对象
 * @param id - 业务对象Id
 * @param name - 业务对象属性
 * @returns object - 对应的JQuery对象
 */
function getColumnInRow(id, name) {
  return $('div#tableContainer tr[id=' + id + ']').find('td[name=' + name + ']');
}

/**
 * 根据业务对象Id和属性名称获取其在数据表格中的值
 * @param id - 业务对象Id
 * @param name - 业务对象属性
 * @returns object - 对应的显示值
 */
function getDataInRow(id, name) {
  return $(getColumnInRow(id, name)).html();
}

/**
 * 根据业务对象Id和属性名称赋给其在数据表格中的值
 * @param id - 业务对象Id
 * @param name - 业务对象属性
 */
function setDataInRow(id, name, html) {
  $(getColumnInRow(id, name)).html(html);
}

/**
 * 渲染所有复选框样式
 */
function enableAllCheckboxStyle(selector) {
  if (!selector)
    selector = ':checkbox';
  $(selector).each(function () {
    $(this).uCheck('enable')
  });
}

/**
 * 在当前页面执行过删除记录操作后，对分页数据表中的数据进行重新加载
 */
function reloadPageableDataAfterDelte() {
  //在页面删除选中的记录
  //如果此页为最后一页,且并非删除所有当前页记录,且并非当前页只有一条数据,则只删除选中的记录
  //否则,获取当前激活的页码数和每页显示数,重新刷新当前页数据,以便后面的数据能够展示到当前页来
  var allChecked = $(':checkbox#all').is(':checked');
  var last = getLastRecord();
  var total = getTotalRecords();
  var onlyRow = countExistRow() <= 1;

  if (last == total && !allChecked && !onlyRow) {
    $(':checkbox.idc:checked').each(function () {
      $(this).parent().parent().remove();
      last--;
      total--;
    });
    setLastRecord(last);
    setTotalRecords(total);
  } else {
    var currentPageNumber = getCurrentPageNumber();
    if (currentPageNumber > 0 && (allChecked || onlyRow))
      currentPageNumber--;
    initPageableData(currentPageNumber, true, getHowManyRecords());
  }
}

/**
 * 禁止事件冒泡并只选当前行
 */
function onlySelectCurrentRow(e, id) {
  cancelBubble(e);
  if (countCheckedbox() > 0)
    doCheckAll(false);
  selectRowById(id);
}

function getFirstRecordNumber() {
  return $('dfn#firstRecordNumber').html();
}

function setFirstRecordNumber(number) {
  return $('dfn#firstRecordNumber').html(number);
}

function getFirstRecord() {
  return $('dfn#firstRecord').html();
}

function setFirstRecord(number) {
  $('dfn#firstRecord').html(number);
}

function getLastRecord() {
  return $('dfn#lastRecord').html();
}

function setLastRecord(number) {
  $('dfn#lastRecord').html(number);
}

function getTotalRecords() {
  return $('dfn#totalRecords').html();
}

function setTotalRecords(number) {
  $('dfn#totalRecords').html(number);
}

function getHowManyRecords() {
  return $('dfn#howManyRecords').html();
}

function setHowManyRecords(number) {
  $('dfn#howManyRecords').html(number);
  //更新全局参数-每页记录显示数量
  globalSize = number;
}

function getDefaultSortValue(sort, direction) {
  if (typeof (sort) == "undefined") {
    sort = "";
  } else {
    //防止重复排序
    if (globalSort) {
      var sorts = globalSort.split('#');
      if (sorts[0] == sort && sorts[1] == direction) {
        abort();
      }
    }
  }
  return sort;
}

var cancelBubble = function (e) {
  e = window.event || e;
  if (e.stopPropagation) {
    e.stopPropagation();
  } else {
    e.cancelBubble = true;
  }
};
