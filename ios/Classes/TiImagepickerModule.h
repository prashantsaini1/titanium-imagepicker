/**
 * titanium-ios-imagepicker
 *
 * Created by Your Name
 * Copyright (c) 2018 Your Company. All rights reserved.
 */

#import "TiModule.h"

#import <ImagePicker/ImagePicker-Swift.h>

@interface TiImagepickerModule : TiModule<ImagePickerDelegate> {
  KrollCallback *_currentCallback;
}

- (void)openGallery:(id)args;

@end
