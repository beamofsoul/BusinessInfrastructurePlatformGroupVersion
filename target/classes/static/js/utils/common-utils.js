var projectPath = window.document.location.pathname.substring(0,window.document.location.pathname.substr(1).indexOf('/')+1);

$(function() {

	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name]) {
				if (!o[this.name].push) {
					o[this.name] = [ o[this.name] ];
				}
				o[this.name].push(this.value || '');
			} else {
	      		o[this.name] = this.value || '';
	    	}
	  	});
		return o;
	};
	
	String.prototype.trims = function() {
		var reg=/&nbsp;/ig;
		var result = this.replace(reg,'');
		return result.replace(/(^\s+)|(\s+$)/g,"");
	}
	
	String.prototype.cut = function(size,omit) {
		if (size == null) size = 20;
		if (this == null || this.length <= size) return this;
		var omitValue = omit ? '' : '...';
		return this.substring(0,size) + omitValue;
	}
	
	abort = function() {
		window.onerror = function(msg,url,line) {
			if (msg.indexOf("stoprun") != -1) {
				return true;
			}
		}
		throw new Error('stoprun');
	}
	
	isArray = function(o) {
		return Object.prototype.toString.call(o) == '[object Array]';
	}

});