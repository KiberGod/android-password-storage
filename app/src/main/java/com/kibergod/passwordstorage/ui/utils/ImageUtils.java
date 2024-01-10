package com.kibergod.passwordstorage.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import org.jetbrains.annotations.NotNull;

public class ImageUtils {
    private static int screenWidth;

    // Встановлює (значення змінної, а не сам екран) ширину екрана у px
    public static void setScreenWidth(@NotNull Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
        }
    }

    // Повертає ширину екрана у px
    public static int getScreenWidth() {
        return screenWidth;
    }

    // Функція зменшує векторне зображення, задане ідентифікатором
    public static ImageView getResizeIcon(@NotNull Context context, int iconResourceId) {
        ImageView imageView = new ImageView(context);
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), iconResourceId, null);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth() / 2,
                vectorDrawable.getIntrinsicHeight() / 2,
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(resizedBitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        imageView.setImageBitmap(resizedBitmap);
        return imageView;
    }

    // Встановлення кольору іконки
    public static void setColorToImg(@NotNull Context context, @NotNull View view, int imageId, int colorId) {
        ImageView imageView = view.findViewById(imageId);
        if (imageView != null) {
            imageView.setColorFilter(ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_IN);
        }
    }

    // Автозменшення картинки запису
    public static void setImageViewSize (@NotNull View view, int imgId, int sizePX) {
        setImageViewSize(view, imgId, sizePX, sizePX);
    }

    // Автозменшення картинки запису
    public static void setImageViewSize (@NotNull View view, int imgId, int widthPX, int heightPX) {
        ImageView imageView = view.findViewById(imgId);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = widthPX;
        layoutParams.height = heightPX;
        imageView.setLayoutParams(layoutParams);
    }
}
