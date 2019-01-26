//
//  TiImagepickerModule.swift
//  titanium-imagepicker
//
//  Created by Your Name
//  Copyright (c) 2019 Your Company. All rights reserved.
//

import UIKit
import TitaniumKit
import YPImagePicker

/**
 
 Titanium Swift Module Requirements
 ---
 
 1. Use the @objc annotation to expose your class to Objective-C (used by the Titanium core)
 2. Use the @objc annotation to expose your method to Objective-C as well.
 3. Method arguments always have the "[Any]" type, specifying a various number of arguments.
 Unwrap them like you would do in Swift, e.g. "guard let arguments = arguments, let message = arguments.first"
 4. You can use any public Titanium API like before, e.g. TiUtils. Remember the type safety of Swift, like Int vs Int32
 and NSString vs. String.
 
 */

@objc(TiImagepickerModule)
class TiImagepickerModule: TiModule {

  public let testProperty: String = "Hello World"
  
  func moduleGUID() -> String {
    return "8477e94d-ea5f-4fe6-8f6a-1e6bacbda2d7"
  }
  
  override func moduleId() -> String! {
    return "ti.imagepicker"
  }

  override func startup() {
    super.startup()
    debugPrint("[DEBUG] \(self) loaded")
  }
  
  @objc(openGallery:)
  func openGallery(arguments: Array<Any>?) {
    guard let arguments = arguments, let options = arguments[0] as? [String: Any] else { return }

    guard let callback: KrollCallback = options["callback"] as? KrollCallback else { return }
    var maxImageSelection = 99

    var config = YPImagePickerConfiguration()

    // Some hardcoded values that may become configurable in the future
    config.showsFilters = false
    config.startOnScreen = .library
    config.screens = [.library, .photo]
    config.shouldSaveNewPicturesToAlbum = false
    
    if options["tintColor"] != nil {
      config.colors.tintColor = TiUtils.colorValue(options["tintColor"])!.color
    }
    
    if options["maxImageSelection"] != nil {
      maxImageSelection = options["maxImageSelection"] as! Int
    }
    
    if options["doneButtonTitle"] != nil {
      config.wordings.done = options["doneButtonTitle"] as! String
    }
    
    if options["cancelButtonTitle"] != nil {
      config.wordings.cancel = options["cancelButtonTitle"] as! String
    }
    
    if options["nextButtonTitle"] != nil {
      config.wordings.next = options["nextButtonTitle"] as! String
    }
    
    if options["cameraTitle"] != nil {
      config.wordings.cameraTitle = options["cameraTitle"] as! String
    }
    
    if options["libraryTitle"] != nil {
      config.wordings.libraryTitle = options["libraryTitle"] as! String
    }

    if options["albumsTitle"] != nil {
      config.wordings.albumsTitle = options["albumsTitle"] as! String
    }
    
    config.library.maxNumberOfItems = maxImageSelection

    let picker = YPImagePicker(configuration: config)

    picker.didFinishPicking { [unowned picker] items, cancelled in
      if cancelled {
        callback.call([["success": false, "cancel": true, "images": []]], thisObject: self)
        picker.dismiss(animated: true, completion: nil)
        return
      }
  
      var images: [TiBlob] = []

      for item in items {
        switch item {
        case .photo(let photo):
          images.append(self.blob(from: photo.image))
        case .video(_):
          print("[WARN] Videos are not handled so far")
        }
      }
  
      callback.call([["images": images, "success": true]], thisObject: self)
      picker.dismiss(animated: true, completion: nil)
    }
 
    guard let controller = TiApp.controller(), let topPresentedController = controller.topPresentedController() else {
      print("[WARN] No window opened. Ignoring gallery call …")
      return
    }

    topPresentedController.present(picker, animated: true, completion: nil)
  }
  
  private func blob(from image: UIImage) -> TiBlob {
    return TiBlob()._init(withPageContext: self.pageContext, andImage: image)
  }
}
