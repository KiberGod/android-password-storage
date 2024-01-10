package com.kibergod.passwordstorage.ui.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.KeyboardUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.function.Consumer;

public class IconSelectionDialog {

    // Функція відмальовує вспливаюче вікно вибору іконки
    public static void showIconSelectionDialog(Context context, Consumer<String> func) {
        KeyboardUtils.hideKeyboards(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_icon_selection, null);
        builder.setView(dialogView);

        GridLayout rootLayout = dialogView.findViewById(R.id.iconsScrollArea);

        String[] iconArray = context.getResources().getStringArray(R.array.vector_icons_array);

        int numColumns = 4;
        int numRows = (int) Math.ceil((float) iconArray.length / numColumns);

        rootLayout.setRowCount(numRows);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        for (int i = 0; i < iconArray.length; i++) {

            int iconResourceId = context.getResources().getIdentifier(iconArray[i], "drawable", context.getPackageName());

            ImageView imageView = ImageUtils.getResizeIcon(context, iconResourceId);

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.setMargins(0, 15, 0, 15);
            layoutParams.columnSpec = GridLayout.spec(i % numColumns, 1f);
            imageView.setLayoutParams(layoutParams);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (func != null) {
                        func.accept(context.getResources().getResourceEntryName(iconResourceId));
                    }
                    alertDialog.dismiss();
                }
            });

            rootLayout.addView(imageView);
        }
        alertDialog.show();
        ViewUtils.setOnClickToView(dialogView, R.id.cancelEditIconButton, () -> alertDialog.dismiss());
    }
}
