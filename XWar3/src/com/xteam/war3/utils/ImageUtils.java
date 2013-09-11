package com.xteam.war3.utils;

import java.lang.ref.WeakReference;

import com.xteam.war3.R;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtils {

	private int resultWidth;
	private int resultHeight;
	private Bitmap loadingBitmap;
	private Resources resources;
	private LruCache<String, Bitmap> lruCache;


	public ImageUtils(Activity context){
		resources = context.getResources();
		loadingBitmap = BitmapFactory.decodeResource(resources, R.drawable.empty_photo);
		resultWidth = 90;
		resultHeight = 90;
		
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		lruCache = new LruCache<String, Bitmap>(cacheSize){

			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return getByteCount(bitmap) / 1024;
			}
			
		};
	}

	public void loadBitmap(int resId, ImageView imageView) {
		final String imageKey = String.valueOf(resId);
	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	    	imageView.setImageBitmap(bitmap);
	    } else {
	    	if (cancelPotentialWork(resId, imageView)) {
	    		final LoadBitmapTask task = new LoadBitmapTask(imageView);
	    		final AsyncDrawable asyncDrawable = new AsyncDrawable(resources, loadingBitmap, task);
	    		imageView.setImageDrawable(asyncDrawable);
	    		task.execute(resId);
	    	}
	    }
	}
	
	private boolean cancelPotentialWork(int resId, ImageView imageView) {
	    final LoadBitmapTask bitmapWorkerTask = getLoadBitmapTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final int previousResId = bitmapWorkerTask.resId;
	        if (previousResId != resId) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled, or be null
	    return true;
	}
	
	private LoadBitmapTask getLoadBitmapTask(ImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncDrawable) {
		           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		           return asyncDrawable.getBitmapWorkerTask();
		       }
		    }
		    return null;
		}
	
	class AsyncDrawable extends BitmapDrawable {
	    private final WeakReference<LoadBitmapTask> LoadBitmapTaskReference;

	    public AsyncDrawable(Resources res, Bitmap bitmap, LoadBitmapTask bitmapWorkerTask) {
	        super(res, bitmap);
	        LoadBitmapTaskReference = new WeakReference<LoadBitmapTask>(bitmapWorkerTask);
	    }

	    public LoadBitmapTask getBitmapWorkerTask() {
	        return LoadBitmapTaskReference.get();
	    }
	}
	
	class LoadBitmapTask extends AsyncTask<Integer, Void, Bitmap> {

		private final WeakReference<ImageView> weakReference;
		private int resId;
		
		public LoadBitmapTask(ImageView imageView) {
			weakReference = new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(Integer... params) {
//			int resId = resources.getIdentifier("a" + (params[0] + 1), "drawable", mContext.getPackageName());
			this.resId = params[0];
			Bitmap bitmap = decodeBitmapFromResource(resId);
			addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}
			
			if (weakReference != null && bitmap != null) {
				final ImageView imageView = weakReference.get();
	            final LoadBitmapTask bitmapWorkerTask = getLoadBitmapTask(imageView);
				if (this == bitmapWorkerTask && imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
		
	}
	
	private Bitmap decodeBitmapFromResource(int resId) {
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(resources, resId, options);
	    // Calculate inSampleSize 
	    options.inSampleSize = calculateInSampleSize(options, resultWidth, resultHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    options.inPreferredConfig = Config.RGB_565;
	    Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);
//	    Log.e("owen", "compress ByteCount :" + bitmap.getByteCount());
	    return bitmap;
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        lruCache.put(key, bitmap);
	    }
	    
	}

	private Bitmap getBitmapFromMemCache(String key) {
	    return lruCache.get(key);
	}
	
    private final int getByteCount(Bitmap bitmap) {
        // int result permits bitmaps up to 46,340 x 46,340
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
//**********************************************************************************
	public Bitmap compressImage(int resId) {
		Options options = new Options();
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);
		Log.e("owen", "original ByteCount :" + bitmap.getByteCount());
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeResource(resources, resId, options);
		options.inSampleSize = computeSampleSize(options, -1, resultWidth * resultHeight);
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		bitmap = BitmapFactory.decodeResource(resources, resId, options);
		Log.e("owen", "compress ByteCount :" + bitmap.getByteCount());
		return bitmap;
	}
	
	private int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
				Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

}
