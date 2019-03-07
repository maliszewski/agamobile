package br.com.agafarma.agamobile.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Image {

    public static File CreateFileTemp(Context context, Bitmap bitmapImage) {
        String localAbsoluteFilePath = saveImageLocally(context, bitmapImage);
        File file = new File(localAbsoluteFilePath);
        return file;
    }

    private static String saveImageLocally(Context context, Bitmap _bitmap) {
        File outputDir = context.getCacheDir();
        File outputFile = null;
        try {
            outputFile = File.createTempFile("tmp", ".jpg", outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(outputFile);
            _bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return outputFile.getAbsolutePath().toString();
    }


    public static byte[] GetBitmapAsByteArray(Bitmap bitmap) {
        byte[] result = null;

        result = PGetBitmapAsByteArray(bitmap);

        return result;
    }


    private static byte[] PGetBitmapAsByteArray(Bitmap bitmap) {
        byte[] result = null;
        if (bitmap != null) {

            final int maxSize = 1000;
            int outWidth;
            int outHeight;
            int inWidth = bitmap.getWidth();
            int inHeight = bitmap.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            result = outputStream.toByteArray();
            resizedBitmap.recycle();

        }

        return result;
    }



}
