package ti.imagepicker;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import org.appcelerator.titanium.TiApplication;
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

	private RequestOptions options;
	private RecyclerView mRecyclerView;
	private ArrayList<ImageViewerInfo> imagesAdapter = new ArrayList<ImageViewerInfo>();
    private PhotoAdapter adapterSet;


    private int frame_layout = 0;
    private int frame_layout_id = 0;
    private int image_view_layout = 0;
    private int image_view_container = 0;
    private int image_id = 0;
    private int title_id = 0;
    private int placeholder_image = 0;
    private boolean isShapeCircle = false;



	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        imagesAdapter = intent.getExtras().getParcelableArrayList(Defaults.Params.IMAGES);

        Defaults.setupInitialValues(getApplicationContext(), intent);

        if (!Defaults.ACTIVITY_THEME.isEmpty()) {
	    		setTheme(Utils.getR("style." + Defaults.ACTIVITY_THEME));
	    }

        setupIds();
        setContentView(frame_layout);

        isShapeCircle = Defaults.SHAPE_CIRCLE == Defaults.SHAPE;

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if (!Defaults.STATUS_BAR_COLOR.isEmpty()) {
            	window.setStatusBarColor(TiConvert.toColor(Defaults.STATUS_BAR_COLOR, TiApplication.getAppCurrentActivity()));
            }

            window.setBackgroundDrawable(TiConvert.toColorDrawable(Defaults.BACKGROUND_COLOR, TiApplication.getAppCurrentActivity()));
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
        		if (!Defaults.BAR_COLOR.isEmpty()) {
        			actionBar.setBackgroundDrawable(TiConvert.toColorDrawable(Defaults.BAR_COLOR, TiApplication.getAppCurrentActivity()));
            }

            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(Defaults.TITLE);
        } else {
	    		Log.e(TAG, Defaults.ACTION_BAR_ERROR_MSG);
	    }

        mRecyclerView = new RecyclerView(TiApplication.getInstance());
        mRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecyclerView.setLayoutManager(new GridLayoutManager(ImageViewerActivity.this, Defaults.GRID_SIZE));

        FrameLayout frame_container = (FrameLayout) findViewById(frame_layout_id);
        frame_container.addView(mRecyclerView);
        frame_container.setBackgroundColor(TiConvert.toColor(Defaults.BACKGROUND_COLOR, TiApplication.getAppCurrentActivity()));

        adapterSet = new PhotoAdapter(imagesAdapter);
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

    		image_view_layout = TiRHelper.getResource("layout.image_viewer");
    		image_view_container = TiRHelper.getResource("id.image_container");
    		image_id = TiRHelper.getResource("id.photo_gallery_image_view");
    		title_id = TiRHelper.getResource("id.imageTitle");

    		placeholder_image = TiRHelper.getResource("drawable.loading_placeholder");

    	} catch (ResourceNotFoundException e) {
    		Log.i(TAG, "XML resources could not be found!!!");
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
       	}

       	options.override(Defaults.IMAGE_HEIGHT, Defaults.IMAGE_HEIGHT);
       	options.placeholder(placeholder_image);
       	options.priority(Priority.HIGH);
    }


    private class PhotoHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        ImageView imView;
        TextView title;

        PhotoHolder(View v) {
            super(v);

            layout = (RelativeLayout) v.findViewById(image_view_container);
            imView = (ImageView) v.findViewById(image_id);
            title = (TextView) v.findViewById(title_id);

            layout.getLayoutParams().height = Defaults.IMAGE_HEIGHT;

            if (isShapeCircle) {
            	int pad = Defaults.CIRCLE_PADDING;
            	imView.setPadding(pad, pad, pad, pad);
            }
        }

        private void setImage(String imagePath) {
        	imagePath = imagePath.trim();

        	if (!imagePath.isEmpty()) {
        		if (imagePath.startsWith("http") || imagePath.startsWith("www")) {
        			try {
        				Glide
                        .with(getApplicationContext())
                        .load(imagePath)
                        .apply(options)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imView);
        			} catch(Exception exc) {}

        		} else {
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
        }

        private void setTitle(String title, String titleColor, String titleBg) {
        	if (title.trim().isEmpty()) {
        		this.layout.removeView(this.title);

        	} else {
        		this.title.setTextColor(TiConvert.toColor(titleColor, TiApplication.getAppCurrentActivity()));
        		this.title.setBackgroundColor(TiConvert.toColor(titleBg, TiApplication.getAppCurrentActivity()));
        		this.title.setText(title);
        	}
        }
    }


    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private ArrayList<ImageViewerInfo> allImagesArray;

        public PhotoAdapter(ArrayList<ImageViewerInfo> imagesArray) {
            allImagesArray = imagesArray;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup v, int type) {
            LayoutInflater inflater = LayoutInflater.from(TiApplication.getAppRootOrCurrentActivity());
            View view = inflater.inflate(image_view_layout, v, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder ph, int position) {
        	ImageViewerInfo info = allImagesArray.get(position);
            ph.setImage(info.getImagePath());
            ph.setTitle(info.getImageTitle(), info.getImageTitleColor(), info.getImageTitleBackgroundColor());
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


    @Override
    public void onBackPressed() {
    	mRecyclerView.stopScroll();
    	super.onBackPressed();
    }

}
