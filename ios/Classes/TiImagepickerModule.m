/**
 * titanium-ios-imagepicker
 *
 * Created by Your Name
 * Copyright (c) 2018 Your Company. All rights reserved.
 */

#import "TiImagepickerModule.h"

#import "TiApp.h"
#import "TiBase.h"
#import "TiHost.h"
#import "TiUtils.h"

@implementation TiImagepickerModule

#pragma mark Internal

- (id)moduleGUID
{
  return @"680ebae9-5f3a-4a1d-9afc-31183b24157a";
}

- (NSString *)moduleId
{
  return @"ti.imagepicker";
}

#pragma mark Lifecycle

- (void)startup
{
  [super startup];
  DebugLog(@"[DEBUG] %@ loaded", self);
}

#pragma Public APIs

- (void)openGallery:(id)args
{
  ENSURE_SINGLE_ARG(args, NSDictionary);
  
  NSUInteger maxImageSelection = [TiUtils intValue:args[@"maxImageSelection"] def:1000];
  NSString *doneButtonTitle = [TiUtils stringValue:args[@"doneButtonTitle"]];
  NSString *cancelButtonTitle = [TiUtils stringValue:args[@"cancelButtonTitle"]];
  NSString *noCameraTitle = [TiUtils stringValue:args[@"noCameraTitle"]];
  NSString *noImagesTitle = [TiUtils stringValue:args[@"noImagesTitle"]];
  NSString *settingsTitle = [TiUtils stringValue:args[@"settingsTitle"]];
  NSUInteger columnCount = [TiUtils intValue:args[@"columnCount"] def:3];
  BOOL recordLocation = [TiUtils boolValue:args[@"recordLocation"] def:NO];

  _currentCallback = (KrollCallback *)args[@"callback"];

  Configuration *imagePickerConfig = [[Configuration alloc] init];
  imagePickerConfig.doneButtonTitle = doneButtonTitle;
  imagePickerConfig.cancelButtonTitle = cancelButtonTitle;
  imagePickerConfig.noCameraTitle = noCameraTitle;
  imagePickerConfig.noImagesTitle = noImagesTitle;
  imagePickerConfig.settingsTitle = settingsTitle;
  imagePickerConfig.recordLocation = recordLocation;

  ImagePickerController *imagePicker = [[ImagePickerController alloc] initWithConfiguration:imagePickerConfig];
  
  imagePicker.delegate = self;
  
  TiThreadPerformOnMainThread(^{
    [TiApp.app showModalController:imagePicker animated:YES];
  }, NO);
}

- (void)cancelButtonDidPress:(ImagePickerController * _Nonnull)imagePicker {
  [_currentCallback call:@[@{ @"success": @NO, @"cancel": @YES, @"images": @[] }] thisObject:self];
  [TiApp.app hideModalController:imagePicker animated:YES];
}

- (void)doneButtonDidPress:(ImagePickerController * _Nonnull)imagePicker images:(NSArray<UIImage *> * _Nonnull)images {
  NSMutableArray<TiBlob *> *mappedPhotos = [NSMutableArray arrayWithCapacity:images.count];
  
  [images enumerateObjectsUsingBlock:^(UIImage * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
    [mappedPhotos addObject:[[TiBlob alloc] _initWithPageContext:self.pageContext andImage:obj]];
  }];
  
  [_currentCallback call:@[@{ @"success": @YES, @"cancel": @NO, @"images": mappedPhotos }] thisObject:self];
  [TiApp.app hideModalController:imagePicker animated:YES];
}

- (void)wrapperDidPress:(ImagePickerController * _Nonnull)imagePicker images:(NSArray<UIImage *> * _Nonnull)images {
  [TiApp.app hideModalController:imagePicker animated:YES];
  DebugLog(@"[WARN] Wrapper pressed");
}

@end
