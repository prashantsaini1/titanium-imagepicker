package in.prashant.imagepicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import org.appcelerator.titanium.TiBlob;

/**
 * Created by prashant on 20/06/17.
 */



public class Blobby {
    protected static TiBlob rescale(String filePath, int destWidth, int destHeight, boolean keepAspect) {
        if (null != filePath) {
        	Bitmap resultBitmap = Blobby.getSmallBitmap(filePath, destWidth, destHeight);
        	
        	if (!keepAspect) {
                return TiBlob.blobFromImage(Bitmap.createScaledBitmap(resultBitmap, destWidth, destHeight, false));

            } else {
                int finalWidth = 0, finalHeight = 0;
                int photoW = resultBitmap.getWidth();
                int photoH = resultBitmap.getHeight();
                float ratioH = (float) photoH / photoW;

                if (photoH > photoW) {
                    finalHeight = (photoH > destHeight) ? destHeight : photoH;
                    finalWidth = (int) (finalHeight / ratioH);

                } else {
                    finalWidth = (photoW > destWidth) ? destWidth : photoW;
                    finalHeight = (int) (finalWidth * ratioH);
                }
                
                return TiBlob.blobFromImage(Bitmap.createScaledBitmap(resultBitmap, finalWidth, finalHeight, false));
            }
        	
        } else {
        	return null;
        }
    }


    private static int getInSampleSize(BitmapFactory.Options options, int w, int h) {
        int inSampleSize = 1;

        if (options.outHeight > h || options.outWidth > w) {
            int halfHeight = options.outHeight / 2;
            int halfWidth = options.outWidth / 2;

            while ((halfHeight / inSampleSize) > h && (halfWidth / inSampleSize) > w) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    protected static Bitmap getSmallBitmap(String filePath, int w, int h) {
        File tempFile = new File(filePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = getInSampleSize(options, w, h);
        
        return BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
    }
}







