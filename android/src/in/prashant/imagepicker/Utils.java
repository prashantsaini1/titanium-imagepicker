
package in.prashant.imagepicker;

import java.lang.reflect.Field;
import org.appcelerator.titanium.util.TiRHelper;


public class Utils {
	public static int getR(String path) {
		try {
			return TiRHelper.getResource(path);
			
		} catch (Exception exc) {
			return -1;
		}
	}
	
	public static int[] getStyleableIntArray(String packageName, String name) {
        try {
            Field[] fields2 = Class.forName(packageName + ".R$styleable" ).getFields();

            for (Field f : fields2) {
                if ( f.getName().equals( name ) ) {
                    int[] ret = (int[])f.get( null );
                    return ret;
                }
            }
        }  catch ( Throwable t ) {}

        return null;
    }
}