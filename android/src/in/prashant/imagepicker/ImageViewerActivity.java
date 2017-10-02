package in.prashant.imagepicker;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.util.TiRHelper;
import org.appcelerator.titanium.util.TiRHelper.ResourceNotFoundException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Prashant Saini on 16/09/17.
 */

public class ImageViewerActivity extends AppCompatActivity {
	private static final String TAG = "ImageViewerActivity";
	private static final int DONE_MENU = 111;
    
	private RequestOptions options;
	private RecyclerView mRecyclerView;
    private ArrayList<ImageAdapaterArray> adapter = new ArrayList<ImageAdapaterArray>();
    private PhotoAdapter adapterSet;
    
    
    private int frame_layout = 0;
    private int frame_layout_id = 0;
    private int image_viewer_layout = 0;
    private int image_viewer_container = 0;
    private int image_id = 0;
    private int title_id = 0;
    private int error_image = 0;
    private boolean isShapeCircle = false;
   

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Defaults.setupInitialValues(getApplicationContext(), getIntent());
        setupIds();
        setContentView(frame_layout);
        
        isShapeCircle = Defaults.SHAPE_CIRCLE == Defaults.SHAPE;

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            
            if (!Defaults.STATUS_BAR_COLOR.isEmpty()) {
            	window.setStatusBarColor(TiConvert.toColor(Defaults.STATUS_BAR_COLOR));
            }
            
            window.setBackgroundDrawable(TiConvert.toColorDrawable(Defaults.BACKGROUND_COLOR));
        }
        
        ActionBar actionBar = getSupportActionBar();
        
        if (!Defaults.BAR_COLOR.isEmpty()) {
        	actionBar.setBackgroundDrawable(TiConvert.toColorDrawable(Defaults.BAR_COLOR));
        }
        	
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(Defaults.TITLE);
        
        mRecyclerView = new RecyclerView(TiApplication.getInstance());
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(new GridLayoutManager(ImageViewerActivity.this, Defaults.GRID_SIZE));

        FrameLayout frame_container = (FrameLayout) findViewById(frame_layout_id);
        frame_container.addView(mRecyclerView);
        frame_container.setBackgroundColor(TiConvert.toColor(Defaults.BACKGROUND_COLOR));
        
        adapterSet = new PhotoAdapter(adapter);
        mRecyclerView.setAdapter(adapterSet);
        
        if ( (1 == Defaults.SHOW_DIVIDER) && (!isShapeCircle) ) {
        	mRecyclerView.addItemDecoration(new DividerDecoration());
        }
        
        setupGlideOptions(); // set glide-options to apply on image
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            
            default:
            	break;
        }

        return super.onOptionsItemSelected(item);
    }
    

    @Override
    protected void onPause() {
        mRecyclerView.stopScroll();
        super.onPause();
    }
    
    
    private void setupIds() {
    	try {
    		frame_layout = TiRHelper.getResource("layout.container");
    		frame_layout_id = TiRHelper.getResource("id.container");
    		
    		image_viewer_layout = TiRHelper.getResource("layout.image_viewer");
    		image_viewer_container = TiRHelper.getResource("id.layout_container");
    		image_id = TiRHelper.getResource("id.photo_gallery_image_view");
    		title_id = TiRHelper.getResource("id.imageTitle");
    		
    		error_image = TiRHelper.getResource("drawable.no_image");
    		
    	} catch (ResourceNotFoundException e) {
    		Log.i(TAG, "XML resources could not be found!!!");
    	}
    }
    
    
    @SuppressWarnings("unchecked")
	private void setupGlideOptions() {
    	options = new RequestOptions();
    	int imageDim = (int) 0.6 * Defaults.IMAGE_HEIGHT; 
    	options.override(imageDim, imageDim);
    	
    	if (isShapeCircle) {
    		if (Defaults.CIRCLE_RADIUS > 0) {
    			options.transforms(new CenterCrop(), new RoundedCorners(Defaults.CIRCLE_RADIUS));
    			
    		} else {
    			options.circleCrop();
    		}
    		
    	} else {
    		options.centerCrop();
    	}
    	
    	options.error(error_image);
    	options.priority(Priority.HIGH);
    }
    
    
    private class PhotoHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        ImageView imView;
        TextView title;

        PhotoHolder(View v) {
            super(v);
            
            layout = (RelativeLayout) v.findViewById(image_viewer_container);
            imView = (ImageView) v.findViewById(image_id);
            title = (TextView) v.findViewById(title_id);
            
            layout.getLayoutParams().height = Defaults.IMAGE_HEIGHT;
            
            if (isShapeCircle) {
            	int pad = Defaults.CIRCLE_PADDING;
            	imView.setPadding(pad, pad, pad, pad);
//            	layout.removeView(title);
            }
        }

        private void setImageAndTitle(String imageTitle, String imagePath) {
        	this.title.setText(imageTitle);
        	
        	try {
        		Glide  
                .with(getApplicationContext())
                .load(new File(imagePath))
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imView);
        		
        	} catch(Exception exc) {}
        }
    }


    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private ArrayList<ImageAdapaterArray> allImagesArray;

        public PhotoAdapter(ArrayList<ImageAdapaterArray> imagePathArray) {
            allImagesArray = imagePathArray;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup v, int type) {
            LayoutInflater inflater = LayoutInflater.from(TiApplication.getAppRootOrCurrentActivity());
            View view = inflater.inflate(image_viewer_layout, v, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder ph, int position) {
//            ph.setImageAndTitle(allImagesArray.get(position).imagePath);

        }

        @Override
        public int getItemCount() {
            return allImagesArray.size();
        }
    }
    

    private class DividerDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildLayoutPosition(view);
            int halfPadding = Math.abs(Defaults.DIVIDER_WIDTH / 2);
            int fullPadding = 2 * halfPadding;

            outRect.top = (pos < Defaults.GRID_SIZE) ? 0 : fullPadding;

            if (pos % Defaults.GRID_SIZE == 0) {      // first column items
                outRect.left = 0;
                outRect.right = halfPadding;

            } else if ( ((pos + 1) % Defaults.GRID_SIZE) == 0 ) {      // last column items
                outRect.right = 0;
                outRect.left = halfPadding;

            } else {    // middle columns items
                outRect.left = halfPadding;
                outRect.right = halfPadding;
            }

            outRect.bottom = 0;
        }
    }
    

    private ArrayList<ImageAdapaterArray> setPhotosList() {
        ArrayList<ImageAdapaterArray> galleryList = new ArrayList<ImageAdapaterArray>();

        
        return galleryList;
    }
    
    
    private void drawBackground(View v, int color, boolean isCircle) {
    	if (isCircle) {
            GradientDrawable gd = new GradientDrawable();
            gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            gd.setColors(new int[]{color, Color.TRANSPARENT });
            gd.setGradientRadius((Defaults.IMAGE_HEIGHT - Defaults.CIRCLE_PADDING)/2);
            v.setBackground(gd);
            
        } else {
            ShapeDrawable oval = new ShapeDrawable (new OvalShape());
            oval.getPaint().setColor(color);
            v.setBackground(oval);
        }
    }
    	
    
    private void processImages(String path) {
    	ArrayList<CharSequence> imagePaths = new ArrayList<CharSequence>();
    	
//    	if (isMultipleSelection) {
//    		if (0 == totalSelectedImages) {
//                Toast.makeText(getApplicationContext(), "No pictures selected.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            final int totalCount = adapter.size();
//            int totalImages = totalSelectedImages;
//            int i = 0;
//
//            while((totalImages > 0) && (i < totalCount)) {
//                if (adapter.get(i).selectionState) {
//                    imagePaths.add(adapter.get(i).imagePath);
//                    --totalImages;
//                }
//
//                ++i;
//            }
//            
//    	} else {
//    		imagePaths.add(path);
//    	}
    	
    	Intent intent = new Intent();
        intent.putExtra(Defaults.Params.IMAGES, imagePaths);
        intent.putExtra(TiC.PROPERTY_SUCCESS, true);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    
    @Override
    public void onBackPressed() {
    	mRecyclerView.stopScroll();
    	
//        if (totalSelectedImages > 0) {      // if images are selected, then unselect them on back-press
//            int i = 0;
//            final int totalCount = adapter.size();
//
//            while((totalSelectedImages > 0) && (i < totalCount)) {
//                if (adapter.get(i).selectionState) {
//                    adapter.get(i).selectionState = false;  // set selected state to false
//                    adapterSet.notifyItemChanged(i);        // notfiy that this position data has been changed & then reflect its UI
//                    --totalSelectedImages;                  // decrease the selected image count by 1
//                }
//
//                ++i;
//            }
//
//            setTotalCount();
//
//        } else {
//            setResult(RESULT_CANCELED);
//            super.onBackPressed();
//        }
    }
   
}







