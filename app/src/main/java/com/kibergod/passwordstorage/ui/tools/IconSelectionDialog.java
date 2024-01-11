package com.kibergod.passwordstorage.ui.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.KeyboardUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.function.Consumer;

public class IconSelectionDialog {

    private static int[] iconSetIds = {
            R.array.general,
            R.array.internet,
            R.array.social_networks,
            R.array.connection,
            R.array.services,
            R.array.shopping
    };

    private static String[] iconSetNames = {
            "Загальне",
            "Інтернет",
            "Соціальні мережі",
            "Зв'язок",
            "Сервіси",
            "Шопінг"
    };

    // Функція відмальовує вспливаюче вікно вибору іконки
    public static void showIconSelectionDialog(Context context, Consumer<String> func) {
        KeyboardUtils.hideKeyboards(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_icon_selection, null);
        builder.setView(dialogView);

        GridLayout rootLayout = dialogView.findViewById(R.id.iconsScrollArea);


        int numColumns = 4;
        rootLayout.setRowCount(1);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        for (int set = 0; set < iconSetIds.length; set++) {

            String[] iconArray = context.getResources().getStringArray(iconSetIds[set]);

            View iconsTitleView = LayoutInflater.from(context).inflate(R.layout.dialog_icon_selection_item, null);
            GridLayout.LayoutParams titleParams = new GridLayout.LayoutParams();
            titleParams.columnSpec = GridLayout.spec(0, numColumns);
            iconsTitleView.setLayoutParams(titleParams);
            TextView title = iconsTitleView.findViewById(R.id.iconsGroupName);
            title.setText(iconSetNames[set]);
            rootLayout.addView(iconsTitleView);

            for (int icon = 0; icon < iconArray.length; icon++) {

                int iconResourceId = context.getResources().getIdentifier(iconArray[icon], "drawable", context.getPackageName());

                ImageView imageView = ImageUtils.getResizeIcon(context, iconResourceId);

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                if (set != iconSetIds.length-1 && icon == iconArray.length - 1) {
                    layoutParams.setMargins(0, 15, 0, 50);
                } else {
                    layoutParams.setMargins(0, 15, 0, 15);
                }
                layoutParams.columnSpec = GridLayout.spec(icon % numColumns, 1f);
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
        }

        alertDialog.show();
        ViewUtils.setOnClickToView(dialogView, R.id.cancelEditIconButton, () -> alertDialog.dismiss());
    }
}
