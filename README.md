# Titanium Android - Multi-Image picker module
Meet the powerful Android multi-image picker module built by using RecyclerView & GridLayoutManager API, & the beautiful image loading [Glide Library](https://github.com/bumptech/glide).

* Efficient & blazing fast loading your entire gallery no matter there are 10k images or more.
* Select multiple images with some useful image manipulation methods.
* Image compression & resizing methods to further retrieve images blob data with efficient & mimimum memory usage.
* Provides tons of customization options to mixes well with your app UI & does not feel like 3rd party module/library.


| Default UI             |  Custom square UI |  Custom circle UI   |
|:-------------------------:|:-------------------------:|:------------------------:|
|  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/default.png)   |  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/custom.png)  |  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/custom_circle.png)  |


## Requirements & Installation
* Ti SDK >= 7.0.0.GA
* [Download latest module version from here](https://github.com/prashantsaini1/titanium-android-imagepicker/raw/master/android/dist/)
* Unzip it, put it in your Titanium project modules folder & add this line to your tiapp.xml <modules> node.

```
<module platform="android">in.prashant.imagepicker</module>
```


# Methods
1. **openGallery()**
* Opens the inbuilt gallery with a 3x3 default grid-view.
* Takes following arguments in a single dictionary object. (All arguments are optional though)

| Argument              | Description           | Default Value              |
| --------------------- |:--------------------- | :------------------------- |
| String **colorPrimaryDark**     | Status bar background color | Same as app theme |
| String **colorPrimary**       |  Actionbar background color   | Same as app theme |
| String **theme**              |  Custom theme       | Same as app theme |
| String **backgroundColor**    | Background color behind grid-images    | white-tint |
| String **coverViewColor**     |  Background color of cover-view behind checkmar icon   | semi-transparent black |
| String **checkMarkColor**     | Checkmark-icon color    | orange-tint |
| String **title**              |  Title of the gallery window   | Select Pictures |
| String **doneButtonTitle**    | Title of the OK button which calls the callback method    | DONE |
| int **columnCount**      |  Number of grid-view columns to show in gallery   | 3 (2 to 5) |
| int **imageHeight**    | ImageView height in dp    | same as width |
| boolean **dividerEnabled**      |  Enable / disable dividers between grid-columns   | true |
| int **dividerWidth**     | If `dividerEnabled` is true, use it to specify the width of dividers.    | 4 dp |
| int **maxImageSelection**     | Maximum number of images to select. Can be used for single image selection by passing as 1     | No limit |
| String **maxImageMessage**     | Message to show when max limit is reached.     | - |
| int **shape**     | Constant as Module.SHAPE_CIRCLE or  Module.SHAPE_SQUARE     | Module.SHAPE_SQUARE |
| int **circleRadius**     | Radius of the circle if shape is Module.SHAPE_CIRCLE     | Complete circle |
| int **circlePadding**     | Padding between circular images similar to divider-width     | 5 |
| function **callback**    | Callback method to get results into. See below example for its usage    | none |

```javascript
var module = require('in.prashant.imagepicker');
module.openGallery({
  title : "Custom Title",
  colorPrimaryDark : '#de3b30',
  colorPrimary : '#de3b30',
  theme : 'CustomeTheme',       // mandatory to avoid crash in case if any non ActionBar theme is applied on overall app
  columnCount : 4,
  coverViewColor : '#aaffffff',
  checkMarkColor : '#000000',
  doneButtonTitle : "Proceed",
  callback : function (e) {
    if (e.success) {
      var allImages = e.images;
    } else if (e.cancel) {
      // gallery result cancelled
    } else {
      alert(e.message);
    }
  }
});
```

2. **createCustomGallery(args)**:
* Open a custom gallery with defined images. See following code on its usage.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| Dict args | Pass same options as above method `openGallery` with additional `images` array | no |

```javascript
var module = require('in.prashant.imagepicker');
module.createCustomGallery({
	title : "Local + Remote Pictures",
	columnCount : 2,
	images : [{
		image : 'http://s.wsj.net/public/resources/images/BN-AF879_idiwal_G_20131101093312.jpg',
		imageTitle : "Diwali 1",
		titleColor : "#cc0",
		titleBackgroundColor : "blue"
	}, {
		image : 'https://c.tadst.com/gfx/750w/india-diwali.jpg?1',
		imageTitle : 'Diwali Happiness'
	}, {
		image : 'http://kids.nationalgeographic.com/content/dam/kids/photos/Countries/H-P/india-diwali.adapt.945.1.jpg',
		imageTitle : 'NAT Geo Diwali',
		titleColor : "#000",
		titleBackgroundColor : "#ddd"
	}, {
		image : '/storage/emulated/0/CMWallpaperHd/HD Wallpaper/city-673335.jpg',  // your local image path
		imageTitle : 'Local Image',
		titleColor : "yellow",
		titleBackgroundColor : "black"
	}]
});
```
| 2 Column UI   |   3 Column UI   |
|:-------------------------:|:-------------------------:|
|  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/custom_gallery2.png)   |  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/custom_gallery3.png)  |

</br>


3. **getImage(filePath)**: Returns the image as blob by passing String filepath.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |

</br>


4. **resizeAsAspect(filePath, width, height)**: Returns the image as blob by resizing to specified width/height & keeping aspect ratio correct. Resizes using higher dimension from width/height.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |
| int width    |  Resized width   | no |
| int height    |  Resized height   | no |

</br>


5. **resizeAsSame(filePath, width, height)**: Returns the image as blob by resizing to specified width/height exactly. Doesn't maintain aspect ratio.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |
| int width    |  Resized width   | no |
| int height    |  Resized height   | no |



<hr/>

### Changelog

**v2.1.0**
* Added `theme` property support to define any custom theme.
* Fixed crash if any non ActionBar theme is applied on overall app.
* Added new Glide library v4.7.0.

**v1.3.0**
* Added method to create a custom gallery which can load local images or remote images or both at same time.
* Images can also have with a title with custom font color & backgroundColor.


**v1.2.0**
* Upgraded to Glide 4.1.1
* Added circular shape option for image-views with circular radius & padding.
* Added option to define message on max limit reach.
* Added error image drawable when no image is found, can be replaced by your custom drawable file with same name as 'no_image.png'.


**v1.1.0**
* Maximum image selection count added. Can be also passed as 1 to select only 1 image instantly.
* Color formats supported now: RGB, ARGB, RRGGBB, AARRGGBB, & color names

</br>


<hr/>

## Thanks & Credits
* [Michael Gangolf](https://github.com/m1ga) for helping me out on Slack.


## LICENSE
    Copyright 2017 Prashant Saini

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
