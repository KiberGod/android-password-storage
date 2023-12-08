package com.kibergod.passwordstorage.ui.tools;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.model.Category;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CategorySelectionDialog {

    // Відображення вікна вибору категорії
    public static void showCategorySelectionDialog(Context context, ArrayList<Category> categories, String activeCategoryName, Consumer<Integer> func) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_category_selection, null);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button button = dialogView.findViewById(R.id.cancelEditCategoryButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { alertDialog.dismiss(); }
        });

        LinearLayout linearLayout = dialogView.findViewById(R.id.categoriesSelectScrollArea);

        for (Category category : categories) {
            View categoryBlock = createCategoryBlock(category, context, activeCategoryName);

            if (category != categories.get(categories.size()-1)) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 0, 0, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        20,
                        context.getResources().getDisplayMetrics()
                ));

                categoryBlock.setLayoutParams(layoutParams);
            }

            categoryBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (func != null) {
                        func.accept(category.getId());
                    }
                    alertDialog.dismiss();
                }
            });
            linearLayout.addView(categoryBlock);
        }

        alertDialog.show();
    }

    // Створення блоків "зображення + назва" категорій
    private static View createCategoryBlock(Category category, Context context, String activeCategoryName) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryBlock = inflater.inflate(R.layout.dialog_category_selection_item, null);

        ImageView imageView = categoryBlock.findViewById(R.id.imgCategoryItem);
        TextView textView = categoryBlock.findViewById(R.id.textCategoryItem);

        imageView.setImageResource(context.getResources().getIdentifier(category.getIconId(), "drawable", context.getPackageName()));
        textView.setText(category.getName());

        if (category.getName().equals(activeCategoryName)) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.purple));
        }

        return categoryBlock;
    }
}
