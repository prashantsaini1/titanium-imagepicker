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

public class Recycler_Activity extends AppCompatActivity {
	private static final String TAG = "ImagePicker";
	private static final int DONE_MENU = 111;
    
	private RequestOptions options;
	private RecyclerView mRecyclerView;
    private ArrayList<ImageAdapaterArray> adapter = new ArrayList<ImageAdapaterArray>();
    private PhotoAdapter adapterSet;
    
    private int totalSelectedImages = 0;
    private int container = 0;
    private int image_container_id = 0;
    private int main_layout_id = 0;
    private int main_image_view = 0;
    private int image_id = 0;
    private int image_checkbox = 0;
    private int image_cover = 0;
    private int error_image = 0;
    private boolean isMultipleSelection = true;
    private boolean isShapeCircle = false;
   

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Defaults.setupInitialValues(getApplicationContext(), getIntent());
        setupIds();
        setContentView(main_layout_id);
        
        isMultipleSelection = (1 != Defaults.MAX_IMAGE_SELECTION); 
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(Recycler_Activity.this, Defaults.GRID_SIZE));

        FrameLayout frame_container = (FrameLayout) findViewById(container);
        frame_container.addView(mRecyclerView);
        frame_container.setBackgroundColor(TiConvert.toColor(Defaults.BACKGROUND_COLOR));
        
        adapterSet = new PhotoAdapter(adapter);
        mRecyclerView.setAdapter(adapterSet);
        
        if ( (1 == Defaults.SHOW_DIVIDER) && (!isShapeCircle) ) {
        	mRecyclerView.addItemDecoration(new DividerDecoration());
        }
        
        setupGlideOptions(); // set glide-options to apply on image
        
        // Get gallery photos in a new UI thread like AsyncTask to update UI changes properly
        new FetchImages().execute();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if (isMultipleSelection) {
    		MenuItem menuitem = menu.add(Menu.NONE, DONE_MENU, Menu.NONE, Defaults.DONE_BTN_TITLE);
            menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	}
        
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            
            case DONE_MENU:
            	processImages("");
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
    		container = TiRHelper.getResource("id.container");
    		image_container_id = TiRHelper.getResource("id.image_container");
    		main_layout_id = TiRHelper.getResource("layout.container");
    		main_image_view = TiRHelper.getResource("layout.image_view");
    		image_id = TiRHelper.getResource("id.photo_gallery_image_view");
    		image_cover = TiRHelper.getResource("id.coverView");
    		image_checkbox = TiRHelper.getResource("id.checkbox");
    		error_image = TiRHelper.getResource("drawable.no_image");
    		
    	} catch (ResourceNotFoundException e) {
    		Log.i(TAG, "XML resources could not be found!!!");
    	}
    }
    
    
    private void setupMaxCountSize() {
    	int maxCount = Defaults.MAX_IMAGE_SELECTION;
    	
    	// set max-image-select count to total no. of pics if passed count is <= 0 or
    	// >= total no. of pics
    	if ( (maxCount <= 0) || (maxCount >= adapter.size()) ) {		 
    		Defaults.MAX_IMAGE_SELECTION = adapter.size();
    	}
    }
    

    private void setTotalCount() {
    	// show subtitle when there is a multiple selection of images
    	if (isMultipleSelection) {
    		ActionBar actionBar = getSupportActionBar();
            int maxSelectionLimit = Defaults.MAX_IMAGE_SELECTION > 0 ? Defaults.MAX_IMAGE_SELECTION : adapter.size();
            actionBar.setSubtitle(totalSelectedImages + " / " + maxSelectionLimit);
    	}
    }
    
    
    @SuppressWarnings("unchecked")
	private void setupGlideOptions() {
    	options = new RequestOptions();
    	
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
    
    
    private class FetchImages extends AsyncTask<Void, Void, ArrayList<ImageAdapaterArray>> {
        @Override
        protected ArrayList<ImageAdapaterArray> doInBackground(Void... params) {
        	return getGalleryPhotos();
        }

        @Override
        protected void onPostExecute(ArrayList<ImageAdapaterArray> items) {
        	if (items.size() > 0) {
        		totalSelectedImages = 0;
            	
        		adapter.clear();
            	adapter.addAll(items);
            	
            	setupMaxCountSize();
            	setTotalCount();
            	
                adapterSet.notifyDataSetChanged();
                
        	} else {
        		Toast.makeText(TiApplication.getAppCurrentActivity().getApplicationContext(), "No pictures available in your gallery.", Toast.LENGTH_SHORT).show();
        		onBackPressed();
        	}
        }
    }
   

    private class PhotoHolder extends RecyclerView.ViewHolder {
        int position = -1;
        RelativeLayout layout;
        ImageView imView;
        View cover_view;
        ImageView checkMark;

        PhotoHolder(View v) {
            super(v);
            
            layout = (RelativeLayout) v.findViewById(image_container_id);
            cover_view = (View) v.findViewById(image_cover);
            imView = (ImageView) v.findViewById(image_id);
            
            layout.getLayoutParams().height = Defaults.IMAGE_HEIGHT;
            
            checkMark = (ImageView) v.findViewById(image_checkbox);
            drawBackground(checkMark, TiConvert.toColor(Defaults.CHECKMARK_COLOR), false);
            
            if (isShapeCircle) {
            	int pad = Defaults.CIRCLE_PADDING;
            	imView.setPadding(pad, pad, pad, pad);
            	cover_view.setPadding(pad, pad, pad, pad);
            	
            	cover_view.getLayoutParams().height = Defaults.IMAGE_HEIGHT - 2 * pad;
            	cover_view.getLayoutParams().width = Defaults.IMAGE_HEIGHT - 2 * pad;
                
            	drawBackground(cover_view, TiConvert.toColor(Defaults.COVER_VIEW_COLOR), true);
            	
            } else {
            	cover_view.setBackgroundColor(TiConvert.toColor(Defaults.COVER_VIEW_COLOR));
            }

            v.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    if (isMultipleSelection) {
                    	boolean isChecked = adapter.get(position).selectionState;

                        if (isChecked) {
                        	--totalSelectedImages;
                        	setSelectionState(false, position);
                        	adapter.get(position).selectionState = false;
                        	
                        } else {
                        	if (totalSelectedImages < Defaults.MAX_IMAGE_SELECTION) {
    	                    	++totalSelectedImages;
    	                    	setSelectionState(true, position);
    	                    	adapter.get(position).selectionState = true;
    	                    	
                        	} else {
                        		Toast.makeText(getApplicationContext(), Defaults.MAX_IMAGE_MSG, Toast.LENGTH_SHORT).show();
                        	}
                        }

                        setTotalCount();
                        
                    } else {
                    	processImages(adapter.get(position).imagePath);
                    }
                }
            });
        }

        private void setSelectionState(boolean state, int pos) {
            position = pos;

            if (state) {
            	cover_view.setVisibility(View.VISIBLE);
            	checkMark.setVisibility(View.VISIBLE);

            } else {
            	cover_view.setVisibility(View.GONE);
            	checkMark.setVisibility(View.GONE);
            }
        }

        private void setImagePath(String path) {
        	try {
        		Glide  
                .with(getApplicationContext())
                .load(new File(path))
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
            View view = inflater.inflate(main_image_view, v, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder ph, int position) {
            ph.setSelectionState(allImagesArray.get(position).selectionState, position);
            ph.setImagePath(allImagesArray.get(position).imagePath);

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
    

    private ArrayList<ImageAdapaterArray> getGalleryPhotos() {
        ArrayList<ImageAdapaterArray> galleryList = new ArrayList<ImageAdapaterArray>();

        try {
            final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Thumbnails.DATA };
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor imagecursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    int thumbColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                    String thumbPath = imagecursor.getString(thumbColumnIndex);
                    galleryList.add(new ImageAdapaterArray(thumbPath));
                }
            }

            imagecursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // show newest photo at beginning of the list
        Collections.reverse(galleryList);
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
    	
    	if (isMultipleSelection) {
    		if (0 == totalSelectedImages) {
                Toast.makeText(getApplicationContext(), "No pictures selected.", Toast.LENGTH_SHORT).show();
                return;
            }

            final int totalCount = adapter.size();
            int totalImages = totalSelectedImages;
            int i = 0;

            while((totalImages > 0) && (i < totalCount)) {
                if (adapter.get(i).selectionState) {
                    imagePaths.add(adapter.get(i).imagePath);
                    --totalImages;
                }

                ++i;
            }
            
    	} else {
    		imagePaths.add(path);
    	}
    	
    	Intent intent = new Intent();
        intent.putExtra(Defaults.Params.IMAGES, imagePaths);
        intent.putExtra(TiC.PROPERTY_SUCCESS, true);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    
    @Override
    public void onBackPressed() {
    	mRecyclerView.stopScroll();
    	
        if (totalSelectedImages > 0) {      // if images are selected, then unselect them on back-press
            int i = 0;
            final int totalCount = adapter.size();

            while((totalSelectedImages > 0) && (i < totalCount)) {
                if (adapter.get(i).selectionState) {
                    adapter.get(i).selectionState = false;  // set selected state to false
                    adapterSet.notifyItemChanged(i);        // notfiy that this position data has been changed & then reflect its UI
                    --totalSelectedImages;                  // decrease the selected image count by 1
                }

                ++i;
            }

            setTotalCount();

        } else {
            setResult(RESULT_CANCELED);
            super.onBackPressed();
        }
    }
   
}







