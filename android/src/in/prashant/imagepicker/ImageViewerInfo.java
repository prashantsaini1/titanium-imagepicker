package in.prashant.imagepicker;

import android.os.Parcel;
import android.os.Parcelable;



public class ImageViewerInfo implements Parcelable {
	private String imagePath;
	private String imageTitle;
	private String imageTitleColor;
	private String imageTitleBackgroundColor;
	
	
	public ImageViewerInfo(String path, String title, String titleColor, String titleBg) {
		this.imagePath = path;
		this.imageTitle = title;
		this.imageTitleColor = titleColor;
		this.imageTitleBackgroundColor = titleBg;
	}
	
	public String getImagePath() { return this.imagePath; }
	public String getImageTitle() { return this.imageTitle; }
	public String getImageTitleColor() { return this.imageTitleColor; }
	public String getImageTitleBackgroundColor() { return this.imageTitleBackgroundColor; }
	
	
	public ImageViewerInfo(Parcel p) {
		imagePath = p.readString();
		imageTitle = p.readString();
		imageTitleColor = p.readString();
		imageTitleBackgroundColor = p.readString();
	}
	
	
	@Override
	public int describeContents() {
		return hashCode();
	}

	
	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(imagePath);
		p.writeString(imageTitle);
		p.writeString(imageTitleColor);
		p.writeString(imageTitleBackgroundColor);
	}

	
	public static final Parcelable.Creator<ImageViewerInfo> CREATOR = new Parcelable.Creator<ImageViewerInfo>() {
		@Override
		public ImageViewerInfo createFromParcel(Parcel p) {
			return new ImageViewerInfo(p);
		}

		@Override
		public ImageViewerInfo[] newArray(int size) {
			return new ImageViewerInfo[size];
		}
	};
}
