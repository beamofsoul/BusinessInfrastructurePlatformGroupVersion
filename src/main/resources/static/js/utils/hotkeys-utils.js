function hotkey(keys,fn) {
	hotkeys(keys, function(event,handler){if(fn) fn();event.preventDefault()});
}

(function() {
	var k = function(action){
		var eventObj = document.createEvent("Events");
		eventObj.initEvent("keydown", true, true);
		eventObj.keyCode = 75;
		eventObj.which = 75;
		document.body.dispatchEvent(eventObj);
	};

	var killSpaceBar = function(evt) {

	var target = evt.target || {},
		isInput = ("INPUT" == target.tagName || "TEXTAREA" == target.tagName || "SELECT" == target.tagName || "EMBED" == target.tagName);
	
		// if we're an input or not a real target exit
		if(isInput || !target.tagName) return;
	
		// if we're a fake input like the comments exit
		if(target && target.getAttribute && target.getAttribute('role') === 'textbox') return;
	
		// ignore the space and send a 'k' to pause
		if (evt.keyCode === 32) {
			evt.preventDefault();
			k();
		}
	};

	document.addEventListener("keydown", killSpaceBar, false);
})();