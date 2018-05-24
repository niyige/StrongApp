package com.oyy.strong.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageHandlerUtils {

    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param imageName
     */
//    public static String saveBitmapToSDCard(Bitmap bitmap, String imageName) {
//
//        String path = Environment.getExternalStorageDirectory()
//                + File.separator + "com.oyy.strong" + File.separator + "imageFile" + File.separator + imageName + ".gif";
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(path);
//            if (fos != null) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//                fos.close();
//            }
//
//            return path;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 下载保存图片
     *
     * @param url
     */
    public static String downLoadImgSaveFile(String url, String type) {

        File filename = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File fileSD = new File(Constant.BASE_PATH);

            if (!fileSD.exists()) {
                fileSD.mkdirs();
            }
            //文件的路径
            filename = new File(fileSD, type + "_douTu.gif");
            if (!filename.exists()) {

                try {
                    filename.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream content = conn.getInputStream();
            byte[] buffer = new byte[1024]; //创建一个中转站
            int len = 0; //用来装每次读到的字节数
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(filename);
            while ((len = content.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename.getAbsolutePath();

    }
}
