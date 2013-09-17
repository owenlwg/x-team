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

	private Bitmap loadingBitmap;
	private Resources resources;
	private static LruCache<String, Bitmap> lruCache;


	public ImageUtils(Activity context){
		resources = context.getResources();
		loadingBitmap = BitmapFactory.decodeResource(resources, R.drawable.empty_photo);

		if (lruCache == null) {
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			final int cacheSize = maxMemory / 8;
			Log.e("owen", "cacheSize:" + cacheSize/1024 + "MB");
			lruCache = new LruCache<String, Bitmap>(cacheSize){
				
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return getByteCount(bitmap) / 1024;
				}
				
			};
		}
	}

	public void loadBitmap(ImageView imageView, int resId) {
		LoadBitmapTask task = new LoadBitmapTask(imageView);
		task.execute(resId);
	}
	
	public void loadBitmap(ImageView imageView, int resId, int reqWidth, int reqHeight) {
		final String imageKey = String.valueOf(resId);
		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			if (cancelPotentialWork(resId, imageView)) {
				final LoadBitmapConcurrentTask task = new LoadBitmapConcurrentTask(imageView, true);
				final AsyncDrawable asyncDrawable = new AsyncDrawable(resources, loadingBitmap, task);
				imageView.setImageDrawable(asyncDrawable);
				task.execute(resId, reqWidth, reqHeight);
			}
		}
	}

	private boolean cancelPotentialWork(int resId, ImageView imageView) {
		final LoadBitmapConcurrentTask bitmapWorkerTask = getLoadBitmapConcurrentTask(imageView);

		if (bitmapWorkerTask != null) {
			final int previousResId = bitmapWorkerTask.resId;
			if (previousResId != resId) {
				bitmapWorkerTask.cancel(true);
			} else {
				return false;
			}
		}
		return true;
	}

	private LoadBitmapConcurrentTask getLoadBitmapConcurrentTask(ImageView imageView) {
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
		private final WeakReference<LoadBitmapConcurrentTask> taskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, LoadBitmapConcurrentTask bitmapWorkerTask) {
			super(res, bitmap);
			taskReference = new WeakReference<LoadBitmapConcurrentTask>(bitmapWorkerTask);
		}

		public LoadBitmapConcurrentTask getBitmapWorkerTask() {
			return taskReference.get();
		}
	}
	
	class LoadBitmapTask extends AsyncTask<Integer, Void, Bitmap> {
		private final WeakReference<ImageView> weakReference;
		
		public LoadBitmapTask(ImageView imageView) {
			weakReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			int resId = params[0];
			Bitmap bitmap = decodeBitmapFromRes(resId);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (weakReference != null && bitmap != null) {
				final ImageView imageView = weakReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
		
	}
	
	class LoadBitmapConcurrentTask extends AsyncTask<Integer, Void, Bitmap> {

		private final WeakReference<ImageView> weakReference;
		private int resId;
		private boolean isCompress;

		public LoadBitmapConcurrentTask(ImageView imageView, boolean isCompress) {
			weakReference = new WeakReference<ImageView>(imageView);
			this.isCompress = isCompress;
		}

		@Override
		protected Bitmap doInBackground(Integer... params) {
			//			int resId = resources.getIdentifier("a" + (params[0] + 1), "drawable", mContext.getPackageName());
			resId = params[0];
			Bitmap bitmap = null;
			if (isCompress) {
				bitmap = decodeBitmapFromRes(resId, params[1], params[2]);
			} else {
				bitmap = decodeBitmapFromRes(resId);
			}
			addBitmapToMemoryCache(String.valueOf(resId), bitmap);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			if (weakReference != null && bitmap != null) {
				final ImageView imageView = weakReference.get();
				final LoadBitmapConcurrentTask bitmapWorkerTask = getLoadBitmapConcurrentTask(imageView);
				if (this == bitmapWorkerTask && imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}

	}

	private Bitmap decodeBitmapFromRes(int resId) {
		return BitmapFactory.decodeResource(resources, resId);
	}
	
	private Bitmap decodeBitmapFromRes(int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, options);
		
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.RGB_565;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);
		//	    Log.e("owen", "compress ByteCount :" + bitmap.getByteCount());
		return bitmap;
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
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
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
	
	
/**********************************************************************************************************************/
	public Bitmap compressImage(int resId) {
		Options options = new Options();
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);
		Log.e("owen", "original ByteCount :" + bitmap.getByteCount());
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeResource(resources, resId, options);
		options.inSampleSize = computeSampleSize(options, -1, 128 * 128);
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
