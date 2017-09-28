# Titanium Android - Multi-Image picker module
Meet the powerful Android multi-image picker module built by using RecyclerView & GridLayoutManager API, & the beautiful image loading [Glide Library](https://github.com/bumptech/glide).

* Efficient & blazing fast loading your entire gallery no matter there are 10k images or more.
* Select multiple images with some useful image manipulation methods.
* Image compression & resizing methods to further retrieve images blob data with efficient & mimimum memory usage. 
* Provides tons of customization options to mixes well with your app UI & does not feel like 3rd party module/library.


| Default UI             |  Custom UI |
|:-------------------------:|:-------------------------:|
|  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/default.png)   |  ![](https://github.com/prashantsaini1/titanium-android-imagepicker/blob/master/custom.png)  |


## Requirements & Installation
* Ti SDK >= 6.0.0.GA
* [Download latest module version from here](https://github.com/prashantsaini1/titanium-android-imagepicker/raw/master/android/dist/)
* Unzip it, put it in your Titanium project modules folder & add this line to your tiapp.xml <modules> node.

```
<module platform="android">in.prashant.imagepicker</module>
```


# Methods
1. **openGallery()**
* Opens the inbuilt gallery with a 3x3 default grid-view. 
* All arguments are optional. Color values are currently supported as #aarrggbb or #rrggbb. Will add more support in next release.
* Takes following arguments in a single dictionary object.

| Argument              | Description           | Default Value              |
| --------------------- |:--------------------- | :------------------------- |
|  String **colorPrimaryDark**     | Status bar background color | dark tint of `colorPrimary` |
|  String **colorPrimary**      |  Actionbar background color   | blue-tint |
|  String **backgroundColor**   | Background color behind grid-images    | white-tint |
|  String **coverViewColor**    |  Background color of cover-view behind checkmar icon   | semi-transparent black |
|     String **checkMarkColor** | Checkmark-icon color    | orange-tint |
| String **title**              |  Title of the gallery window   | Select Pictures |
| String **doneButtonTitle**    | Title of the OK button which calls the callback method    | DONE |
| int **columnCount**      |  Number of grid-view columns to show in gallery   | 3 (2 to 5) |
| int **imageHeight**    | ImageView height in dp    | square/width of image-view |
| boolean **dividerEnabled**      |  Enable / disable dividers between grid-columns   | true |
| int **dividerWidth**     | If `dividerEnabled` is true, use it to specify the width of dividers.    | 4 dp |
| int **maxImageSelection**     | Maximum number of images to select. Value <=0 will be considered as No Limit.    | No limit |
| function **callback**    | Callback method to get results into. See below example for its usage    | none |

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
    } else if (e.cancel) {
      // gallery result cancelled
    } else {
      alert(e.message);
    }
  }
});
```


2. **getImage(filePath)**: Returns the image as blob by passing String filepath.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |

</br>

3. **resizeAsAspect(filePath, width, height)**: Returns the image as blob by resizing to specified width/height & keeping aspect ratio correct. Resizes using higher dimension from width/height. 

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |
| int width    |  Resized width   | no |
| int height    |  Resized height   | no |

</br>

4. **resizeAsSame(filePath, width, height)**: Returns the image as blob by resizing to specified width/height exactly. Doesn't maintain aspect ratio.

| Argument        | Description           | Optional  |
| -------------   |:--------------------- | :------------------------- |
| String filePath | Image file path to get its blob data | no |
| int width    |  Resized width   | no |
| int height    |  Resized height   | no |



<hr/>

### Changelog
**v1.1.0**
* Maximum image selection count added. Can be also passed as 1 to select only 1 image instantly.
* Color formats supported now: RGB, ARGB, RRGGBB, AARRGGBB, Color Name

</br>

<hr/>
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
