package in.prashant.imagepicker;

import java.util.ArrayList;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiActivityResultHandler;

import android.app.Activity;
import android.content.Intent;



public class GalleryResultHandler implements TiActivityResultHandler{
	private KrollObject krollObject;
	private KrollFunction callback = null;
	
	
	public GalleryResultHandler(KrollFunction callback, KrollObject obj) {
		this.callback = callback;
		this.krollObject = obj;
	}
	
	
	@Override
	public void onError(Activity activity, int requestCode, Exception exc) {
		flushResponse(TiC.EVENT_PROPERTY_ERROR, true, TiC.EVENT_PROPERTY_MESSAGE, exc.toString());
	}

	
	@Override
	public void onResult(Activity activity, int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_CANCELED){
			flushResponse(TiC.PROPERTY_CANCEL, true, TiC.PROPERTY_SUCCESS, false, TiC.EVENT_PROPERTY_MESSAGE, "Result Cancelled");
			
		} else if (Defaults.REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK) {
			if (null != data && data.hasExtra(TiC.PROPERTY_SUCCESS) && data.hasExtra(Defaults.Params.IMAGES)) {
                ArrayList<CharSequence> imagePaths = data.getCharSequenceArrayListExtra(Defaults.Params.IMAGES);
                flushResponse(TiC.PROPERTY_CANCEL, false, TiC.PROPERTY_SUCCESS, true, Defaults.Params.IMAGES, imagePaths.toArray());
            }
		}
	}
	

	// check if callback is not null & then iterate over any number of arguments to make key-value pair
	protected void flushResponse(Object... params) {
		if (null != callback) {
			ArrayList<Object> args = new ArrayList<Object>();
			
			for (Object a : params) {
				args.add(a);	
			}
			
			int length = args.size();
			KrollDict response = new KrollDict();
			
			for (int i=0; i<length; i++) {		// double increase i to make key-value pair
				String key = (String) args.get(i);
				++i;
				response.put(key, args.get(i));
			}
			
			callback.callAsync(krollObject, response);
		}
	}
}
