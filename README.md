# Titanium Android - Multi-Image picker module
Meet the powerful Android multi-image picker module built by using RecyclerView & GridLayoutManager API, & the beautiful image loading [Glide Library](https://github.com/bumptech/glide).

This module helps to select multiple images from an inbuilt gallery which renders gallery images blazingly fast.

It also provides tons of customization options to mixes well with your app UI & does not feel like 3rd party module/library.

| Default UI             |  Custom UI |
|:-------------------------:|:-------------------------:|
|  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/default.png)   |  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/custom.png)  |


## Requirements & Installation
* Ti SDK >= 6.0.0.GA
* [Download module from here](android/dist/in.prashant.imagepicker-android-1.0.0.zip)
* Unzip it, put it in your Titanium project modules folder & add this line to your tiapp.xml <modules> node.

```
<module platform="android">in.prashant.imagepicker</module>
```


# Methods
1. **openGallery()**: Opens the inbuilt gallery with a 3x3 default grid-view. Takes following arguments (all are optional though) in a single dictionary object.

| Argument        | Description           | Default Value  |
| -------------   |:--------------------- | :------------------------- |
| colorPrimaryDark| Status bar background color as mentioned in Android theme attribute | dark tint of `colorPrimary` |
| colorPrimary    |  Actionbar background color   | **blue-tint** |
| backgroundColor | Background color behind grid-images    | **white-tint** |
| coverViewColor    |  Background color of cover-view behind checkmar icon   | **semi-transparent black** |
| checkMarkColor | Checkmark-icon color    | **orange-tint** |
| title    |  Title of the gallery window   | **Select Pictures** |
| doneButtonTitle | Title of the OK button which calls the callback method    | **DONE** |
| columnCount    |  Number of grid-view columns to show in gallery   | **3** |
| imageHeight | ImageView height in dp    | **square/width of image-view** |
| dividerEnabled    |  Enable / disable dividers between grid-columns   | **true** |
| dividerWidth | If `dividerEnabled` is true, use it to specify the width of dividers.    | **4 dp** |
| colorPrimary    |  Actionbar background color   | **blue-tint** |
| backgroundColor | Background color behind grid-images    | **white-tint** |
| colorPrimary    |  Actionbar background color   | **blue-tint** |
| backgroundColor | Background color behind grid-images    | **white-tint** |
| callback | Callback method to get results into. Use below parameters to know the result. See below example for its usage    | **none** |

```javascript
var module = require('in.prashant.imagepicker');
module.openGallery({
  title : "Custom Title",
  colorPrimaryDark : '#de3b30',
  colorPrimary : '#de3b30',
  columnCount : 4,
  coverViewColor : '#aaffffff',
  checkMarkColor : '#000000',
  doneButtonTitle : "Proceed",
  callback : function (e) {
    if (e.success) {
      var allImages = e.images;
    }
  }
});
```


2. **getImage()**: Returns the image as blob by passing String filepath.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |

</br>

3. **resizeAsAspect()**: Returns the image as blob by resizing to specified width/height & keeping aspect ratio correct. Resizes using higher dimension from width/height. 

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |
| int width    |  Resized width   | no |
| int height    |  Resized height   | no |

</br>

4. **resizeAsSame()**: Returns the image as blob by resizing to specified width/height exactly. Doesn't maintain aspect ratio.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |
| int width    |  Resized width   | no |
| int height    |  Resized height   | no |

#### Feel free to use this module & make it better with regular updates & bug reporting.

## Thanks & Credits
* [Michael Gangolf](https://github.com/m1ga) for helping me out on Slack.
