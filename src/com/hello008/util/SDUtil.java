package com.hello008.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import com.hello008.base.C;

import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class SDUtil {

	private static String TAG = SDUtil.class.getSimpleName();
	
	private static double MB = 1024;
	private static double FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private static double IMAGE_EXPIRE_TIME = 10;
	public static String dir;

	public static Bitmap getImage(String fileName) {
		// check image file exists
		String realFileName = getSDPath() + C.dir.faces + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {//如果SD卡不存在
			Log.w("~``````````````1111", "1111");
			return null;
		}
		// get original image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		Log.w("~``````````````2222", "2222");
		return BitmapFactory.decodeFile(realFileName, options);
	}
	
	public static Bitmap getImageByDir(String fileName,String dir2) {
		// check image file exists
		String realFileName = getSDPath() + dir2 + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {
			return null;
		}
		// get original image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(realFileName, options);
	}
	
	/**
	 * 获取本地存储图片
	 * @param fileUrl
	 * @return
	 */
	public static Bitmap getImageUri(String fileUri) {
		// check image file exists
		String realFileUri= fileUri;
		File file = new File(realFileUri);
		if (!file.exists()) {
			return null;
		}
		// get original image
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(realFileUri, options);
	}
	
	public static Bitmap getSample(String fileName) {
		// check image file exists
		String realFileName =getSDPath() + C.dir.faces + "/" + fileName;
		File file = new File(realFileName);
		if (!file.exists()) {
			realFileName = getSDPath() + C.dir.faces + "/" + fileName;
			return null;
		}
		// get original size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(realFileName, options);
		int zoom = (int) (options.outHeight / (float) 50);
		if (zoom < 0) zoom = 1;
		// get resized image
		options.inSampleSize = zoom;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(realFileName, options);
		return bitmap;
	}
	
	//使用Bitmap加Matrix来缩放  w，h单位为像素
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h)   
    {    
        Bitmap BitmapOrg = bitmap;    
        int width = BitmapOrg.getWidth();    
        int height = BitmapOrg.getHeight();    
        int newWidth = w;    
        int newHeight = h;    
  
        float scaleWidth = ((float) newWidth) / width;    
        float scaleHeight = ((float) newHeight) / height;    
  
        Matrix matrix = new Matrix();    
        matrix.postScale(scaleWidth, scaleHeight);    
        // if you want to rotate the Bitmap     
        // matrix.postRotate(45);     
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,    
                        height, matrix, true);    
        return resizedBitmap;    
    }
    
    //dip转换成px
    public static int dip2px(Context context, float dipValue) {
    	final float scale = context.getResources().getDisplayMetrics().density;
    	return (int) (dipValue * scale + 0.5f);
    }
	
	public static void saveImage(Bitmap bitmap, String fileName,Context context) {
		dir = Environment.getExternalStorageDirectory().getPath();//获取sd卡根目录
		
		if (bitmap == null) {
			Log.w(TAG, " trying to save null bitmap");
			return;
		}
		// 判断sdcard上的空间
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSpace()) {
			Log.w(TAG, "Low free space onsd, do not cache");
			return;
		}
		// 不存在则创建目录
		File dir = new File(getSDPath() + C.dir.faces);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		// 不存在则创建目录
		File dir2 = new File(getSDPath() + C.dir.facesoriginal);
		if (!dir2.exists()) {
			dir2.mkdirs();
		}
		
		// 保存图片
		try {
			
			//保存头像原图
			String realFileName2 = getSDPath() + C.dir.facesoriginal + "/" + fileName;
			File file2 = new File(realFileName2);
			file2.createNewFile();
			OutputStream outStream2 = new FileOutputStream(file2);
			bitmap.compress(Bitmap.CompressFormat.PNG,100,outStream2);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 30, outStream);
			outStream2.flush();
			outStream2.close();
			
			String realFileName = getSDPath() + C.dir.faces + "/" + fileName;
			File file = new File(realFileName);
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			float dipF = C.constants.facedip; 
			int pxI = SDUtil.dip2px(context, dipF);//像素
			bitmap = SDUtil.resizeImage(bitmap, pxI, pxI);//缩小成固定像素
			bitmap.compress(CompressFormat.JPEG,50,outStream);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 30, outStream);
			outStream.flush();
			outStream.close();
			
			Log.i(TAG, "Image saved tosd");
		} catch (FileNotFoundException e) {
			Log.w(TAG, "FileNotFoundException");
		} catch (IOException e) {
			Log.w(TAG, "IOException");
		}
	}

	protected static void updateTime(String fileName) {
		File file = new File(fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/**
	 * 计算sdcard上的剩余空间
	 * 
	 * @return
	 */
	public static int getFreeSpace() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	public static void removeExpiredCache(String dirPath, String filename) {
		File file = new File(dirPath, filename);
		if (System.currentTimeMillis() - file.lastModified() > IMAGE_EXPIRE_TIME) {
			Log.i(TAG, "Clear some expiredcache files ");
			file.delete();
		}
	}

	public static void removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > getFreeSpace()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			Log.i(TAG, "Clear some expiredcache files ");
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}

		}

	}
	
	/**
	 * 获取sd卡路径 若sd卡不存在则获取手机根路径
	 * @author 王凯
	 */
	public static String getSDPath(){
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
		.equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		}else{
			sdDir = Environment.getRootDirectory();
		}
		
		return sdDir.toString();
	}

	private static class FileLastModifSort implements Comparator<File> {
		@Override
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
}