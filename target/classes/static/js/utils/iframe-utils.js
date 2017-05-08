/**
 * 调入到iFrame内时,自适应高度
 * 父页面iframe的id="iframeBody"
 */
function  adaptiveIframeHeight(selector) {
  var offset = 0;
  if (!selector) {
    selector = "#tableContainer";
    offset = 180;
  }
  var obj = $(document.body);
  if (obj.find(selector)[0] !== undefined) {
    var curTableheght = obj.find(selector).height();
    var curTreeHeght = obj.find('#treeview')[0] === undefined ? 0 : obj.find('#treeview').height();
    $(parent.document).find("#iframeBody").height(curTableheght + curTreeHeght + offset);
  }
}
///**
// * 当屏幕大小有变更时, 相应iframe框高度调整
// */
//window.onresize = function () {
//  alert('onresize')
//  adaptiveIframeHeight();
//};
/**
 * 焦点在iframe框下,按F5时刷新本页
 */
$("body").bind("keydown", function () {
  if (event.keyCode === 116) {
    initPageableData(getCurrentPageNumber());
    parent.myProgress();
    return false;
  }
});
