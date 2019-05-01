package com.fzu.facheck.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class PhotoUtil {
    public static void takePicture(Activity activity,Uri imageUri, int requestCode) {
        //调用系统相机
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    public static void openPic(Activity activity, int requestCode) {
        //打开系统相册
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, requestCode);
    }
    //g根据相册放回结果获取path
    public static String handleImageOnKitKat(final Context context,Intent data){
        String imagepath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(context,uri)){
            String docid=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docid.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagepath=getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri=ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docid));
                imagepath=getImagePath(context,contentUri,null);}
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagepath=getImagePath(context,uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                imagepath=uri.getPath();
            }
        return imagepath;
    }
    public static String getImagePath(final Context context,Uri uri,String selection){
        String path=null;
        Cursor cursor=context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
