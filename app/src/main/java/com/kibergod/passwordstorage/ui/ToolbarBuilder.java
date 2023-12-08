package com.kibergod.passwordstorage.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kibergod.passwordstorage.R;

public class ToolbarBuilder {

    /*
     *  Даний клас відповідає за подубову панелі інструментів для сторінок взаємодій з категоріями та записами
     */

    private static final int ERASER_ID = R.id.imgEraser;
    private static final int GENERATOR_ID = R.id.imgGears;
    private static final int TRASH_ID = R.id.imgTrash;
    private static final int SAVE_BUTTON_ID = R.id.imgTick;

    private static final int PLACE_FOR_TOOLBAR_ID = R.id.placeForToolbar;
    private static final int TOOLBAR_FRAGMENT_ID = R.layout.fragment_toolbar;

    private static ConstraintLayout toolbarLayout;

    /*
    * Вбудова панелі інструментів у UI.
    *
    * Для використання надана view обов`язково має містити <LinearLayout> з android:id="@+id/placeForToolbar"
    */
    public static void addToolbarToView(View view, Context context, boolean useEraser, boolean useGenerator, boolean useTrash, boolean useSaveButton) {
        LinearLayout placeForToolbar = view.findViewById(PLACE_FOR_TOOLBAR_ID);

        if (placeForToolbar != null) {
            toolbarLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(TOOLBAR_FRAGMENT_ID, null);

            placeForToolbar.addView(toolbarLayout);

            deleteUnusedTools(view, useEraser, useGenerator, useTrash, useSaveButton);
        }
    }

    // Загальне видалення непотрібних інструментів
    private static void deleteUnusedTools(View view, boolean useEraser, boolean useGenerator, boolean useTrash, boolean useSaveButton) {
        if (!useEraser) {
            deleteTool(view, ERASER_ID);
        }

        if (!useGenerator) {
            deleteTool(view, GENERATOR_ID);
        }

        if (!useTrash) {
            deleteTool(view, TRASH_ID);
        }

        if (!useSaveButton) {
            deleteTool(view, SAVE_BUTTON_ID);
        }
    }

    // Функція видалення інструмента
    private static void deleteTool(View view, int toolId) {
        ImageView tool = view.findViewById(toolId);
        ViewGroup parentGroup = (ViewGroup) tool.getParent();
        parentGroup.removeView(tool);
    }
}
