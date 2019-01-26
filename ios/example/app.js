var TiImagePicker = require('ti.imagepicker');

var win = Ti.UI.createWindow({
    backgroundColor: '#fff'
});
var btn = Ti.UI.createButton({
    title: 'Select photos!'
});

btn.addEventListener('click', function() {
    TiImagePicker.openGallery({
        maxImageSelection: 5,
        tintColor: 'red',
        doneButtonTitle: 'Fertig',
        nextButtonTitle: 'Weiter',
        cancelButtonTitle: 'Abbrechen',
        cameraTitle: 'Kamera',
        libraryTitle: 'Galerie',
        albumsTitle: 'Alben',
        callback: function(event) {
            if (!event.success) {
                Ti.API.error('Cannot get images!');
                return;
            }

            console.log('Successfully selected ' + event.images.length + ' images!');

            event.images.forEach(function(image, index) {
                var file = Ti.Filesystem.getFile(Ti.Filesystem.applicationCacheDirectory, 'test-' + index + '.jpg');
                file.write(image);
                console.log(file.nativePath);
            });
        }
    })
});

win.add(btn);
win.open();