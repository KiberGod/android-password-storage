package com.kibergod.passwordstorage.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.model.Category;

import java.util.ArrayList;
import java.util.function.Consumer;

public class CategorySelectionDialog {

    // Відображення вікна вибору категорії
    public static void showCategorySelectionDialog(Context context, ArrayList<Category> categories, Consumer<Integer> func) {
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
            View categoryBlock = createCategoryBlock(category, context);
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
    private static View createCategoryBlock(Category category, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View categoryBlock = inflater.inflate(R.layout.dialog_category_selection_item, null);

        ImageView imageView = categoryBlock.findViewById(R.id.imgCategoryItem);
        TextView textView = categoryBlock.findViewById(R.id.textCategoryItem);

        if (category.hasIcon()) {
            imageView.setImageResource(category.getIconId());
        } else {
            imageView.setImageResource(R.drawable.vector_template_image);
        }
        textView.setText(category.getName());

        return categoryBlock;
    }
}
