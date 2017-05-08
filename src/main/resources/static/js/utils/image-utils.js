/**
 * 将已经剪切的画布按照原型切边，被切除的部分由黑色代替
 * @param sourceCanvas - 按照方形进行截取的画布
 */
function getRoundedCanvas(sourceCanvas) {
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
    var width = sourceCanvas.width;
    var height = sourceCanvas.height;

    canvas.width = width;
    canvas.height = height;
    context.beginPath();
    context.arc(width / 2, height / 2, Math.min(width, height) / 2, 0, 2 * Math.PI);
    context.strokeStyle = 'rgba(0,0,0,0)';
    context.stroke();
    context.clip();
    context.drawImage(sourceCanvas, 0, 0, width, height);

    return canvas;
  }

/**
 * 将输入参数画布转化为图片DOM对象
 * @param canvas - 画布对象
 * @param id - 图片id属性值
 * @returns image - 图片DOM对象
 */
function canvasToImage(canvas, id) {
	var image = new Image();
	image.src = canvas.toDataURL("image/jpeg");
	if (id) image.id = id;
	return image;
}

/**
 * 根据输入的base64字符串计算其转换成图片后有多大
 * @param base64Code - 图片字符串
 * @result float - 以MB为单位的图片大小
 */
function getImageSize(base64Code) {
	if (base64Code) {
		//去掉base64文件头(标注图片格式)
		base64Code = base64Code.substring(base64Code.indexOf(",") + 1, base64Code.length);
		//去掉base64尾部等号
		var equalIndex = base64Code.indexOf('=');
		if (equalIndex > 0) base64Code = base64Code.substring(0, equalIndex);
		//获取base64长度
		var length = base64Code.length;
		//计算文件流大小，单位为字节
		var size = parseInt(length - (length / 8) * 2);
		//计算成MB，并返回
		return ((size / 1024) / 1024);
	}
	return 0;
}