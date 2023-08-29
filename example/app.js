// This is a test harness for your module
// You should do something interesting in this harness
// to test out the module and to provide instructions
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow();
win.open();

win.addEventListener("open", function() {
	console.log("open")
	var module = require('ti.imagepicker');
	var permissions = [
		"android.permission.READ_MEDIA_IMAGES"
	];

	Ti.Android.requestPermissions(permissions, function(e) {
		if (e.success) {
			module.openGallery({
				title: "Custom Title",
				colorPrimaryDark: '#de3b30',
				colorPrimary: '#de3b30',
				theme: 'CustomeTheme', // Pass any ActionBar enabled theme to avoid crash if any non ActionBar theme is applied on overall app, else auto uses the application theme
				columnCount: 4,
				coverViewColor: '#aaffffff',
				checkMarkColor: '#000000',
				doneButtonTitle: "Proceed",
				callback: function(e) {
					if (e.success) {
						var allImages = e.images;
					} else if (e.cancel) {
						// gallery result cancelled
					} else {
						alert(e.message);
					}
				}
			});
		}
	});

})
