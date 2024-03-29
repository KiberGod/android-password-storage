package com.kibergod.passwordstorage.ui.tools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.function.Function;

public class ToolbarBuilder {

    /*
     *  Даний клас відповідає за подубову панелі інструментів для сторінок взаємодій з категоріями та записами
     */

    private static final int VISIBILITY_SWITCH_ID = R.id.imgGodEye;
    private static final int BOOKMARK_ID = R.id.imgBookmark;
    private static final int EDIT_BUTTON_ID = R.id.imgModerPencil;
    private static final int BACK_BUTTON_ID = R.id.imgLeftArrow;
    private static final int ERASER_ID = R.id.imgEraser;
    private static final int GENERATOR_ID = R.id.imgGears;
    private static final int TRASH_ID = R.id.imgTrash;
    private static final int SAVE_BUTTON_ID = R.id.imgTick;
    private static final int RESTORE_ID =R.id.imgRestore;

    private static final int PLACE_FOR_TOOLBAR_ID = R.id.placeForToolbar;
    private static final int TOOLBAR_FRAGMENT_ID = R.layout.fragment_toolbar;

    private static ConstraintLayout toolbarLayout;

    private static EditText currentEditTextTotal;
    private static EditText currentEditTextForGenerator;
    private static boolean bookmarkStatus;

    /*
    * Вбудова панелі інструментів у UI.
    *
    * Для використання надана view обов`язково має містити <LinearLayout> з android:id="@+id/placeForToolbar"
    */
    public static void addToolbarToView(View view, Context context,
                                        boolean useVisibilitySwitch,
                                        boolean useBookmark,
                                        boolean useEditButton,
                                        boolean useEraser,
                                        boolean useGenerator,
                                        boolean useTrash,
                                        boolean useSaveButton,
                                        boolean useRestore) {
        LinearLayout placeForToolbar = view.findViewById(PLACE_FOR_TOOLBAR_ID);

        if (placeForToolbar != null) {
            toolbarLayout = (ConstraintLayout) LayoutInflater.from(context).inflate(TOOLBAR_FRAGMENT_ID, null);

            placeForToolbar.addView(toolbarLayout);

            deleteUnusedTools(view, useVisibilitySwitch, useBookmark, useEditButton, useEraser, useGenerator, useTrash, useSaveButton, useRestore);

            ViewUtils.setOnClickToView(view, BACK_BUTTON_ID, () -> ((Activity) context).onBackPressed());
            setIconColorsToToolbar(view, context);
        }
    }

    // Загальне видалення непотрібних інструментів
    private static void deleteUnusedTools(View view, boolean useVisibilitySwitch, boolean useBookmark, boolean useEditButton,
                                          boolean useEraser, boolean useGenerator, boolean useTrash, boolean useSaveButton, boolean useRestore) {
        if (!useBookmark) {
            deleteTool(view, BOOKMARK_ID);
        } else {
            bookmarkStatus = false;
        }

        if (!useEditButton) {
            deleteTool(view, EDIT_BUTTON_ID);
        }

        if (!useEraser) {
            deleteTool(view, ERASER_ID);
        } else {
            setOnClickToEraseButton(view);
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

        if (!useVisibilitySwitch) {
            deleteTool(view, VISIBILITY_SWITCH_ID);
        }

        if (!useRestore) {
            deleteTool(view, RESTORE_ID);
        }
    }

    // Функція видалення інструмента
    private static void deleteTool(View view, int toolId) {
        ImageView tool = view.findViewById(toolId);
        ViewGroup parentGroup = (ViewGroup) tool.getParent();
        parentGroup.removeView(tool);
    }

    // Функція встановлює початкові кольори іконок
    private static void setIconColorsToToolbar(View view, Context context) {
        ImageUtils.setColorToImg(context, view, BOOKMARK_ID, R.color.white);
        ImageUtils.setColorToImg(context, view, ERASER_ID, R.color.gray_text);
        ImageUtils.setColorToImg(context, view, GENERATOR_ID, R.color.gray_text);
        ImageUtils.setColorToImg(context, view, TRASH_ID, R.color.white);
        ImageUtils.setColorToImg(context, view, SAVE_BUTTON_ID, R.color.white);
    }

    // Автооновлення фокус-поля
    public static void setEditTextFocusChangeListener(View view, int editTextId, Context context) {
        setEditTextFocusChangeListener(view, editTextId, context, false);
    }

    // Автооновлення фокус-поля
    public static void setEditTextFocusChangeListener(View view, int editTextId, Context context, boolean isTotal) {
        final EditText editText = view.findViewById(editTextId);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!isTotal) {
                        currentEditTextForGenerator = editText;
                        ImageUtils.setColorToImg(context, view, GENERATOR_ID, R.color.purple);
                    }
                    currentEditTextTotal = editText;
                    ImageUtils.setColorToImg(context, view, ERASER_ID, R.color.white);
                } else {
                    if (!isTotal) {
                        currentEditTextForGenerator = null;
                        ImageUtils.setColorToImg(context, view, GENERATOR_ID, R.color.gray_text);
                    }
                    currentEditTextTotal = null;
                    ImageUtils.setColorToImg(context, view, ERASER_ID, R.color.gray_text);
                }
            }
        });
    }

    // Натиснення на кнопку стертя вводу у полі
    private static void setOnClickToEraseButton(View view) {
        ViewUtils.setOnClickToView(view, ERASER_ID, () -> {
            if (currentEditTextTotal != null) {
                currentEditTextTotal.setText("");
            }
        });
    }

    // Функція встановлює подію натискання кнопки генерації пароля
    public static void setOnClickToGenPassword(View view, int textViewId, Function<Void, String> generatePassFunc, Runnable action) {
        ViewUtils.setOnClickToView(view, GENERATOR_ID, () -> {
            if (currentEditTextForGenerator != null) {
                if (currentEditTextForGenerator.getId() == textViewId) {
                    int cursorPosition = currentEditTextForGenerator.getSelectionStart();
                    String currentText = currentEditTextForGenerator.getText().toString();
                    String newText = currentText.substring(0, cursorPosition) + generatePassFunc.apply(null) +
                            currentText.substring(cursorPosition);
                    currentEditTextForGenerator.setText(newText);
                } else {
                    currentEditTextForGenerator.setText(generatePassFunc.apply(null));
                }
                action.run();
            }
        });
    }

    // Функція встановлює подію натиснення кнопки редагування
    public static void setOnClickToEditButton(View view, Runnable action) {
        ViewUtils.setOnClickToView(view, EDIT_BUTTON_ID, action);
    }

    // Функція встановлює подію натиснення закладки
    public static void setOnClickToBookmark(View view, Context context, Runnable action) {
        ViewUtils.setOnClickToView(view, BOOKMARK_ID, () -> {
            action.run();
            bookmarkStatus = !bookmarkStatus;
            resetBookmarkButtonColor(view, context);
        });
    }

    // Функція автозміни кольору інструменту "Закладка"
    public static void resetBookmarkButtonColor(View view, Context context) {
        if (bookmarkStatus) {
            ImageUtils.setColorToImg(context, view, BOOKMARK_ID, R.color.purple);
        } else {
            ImageUtils.setColorToImg(context, view, BOOKMARK_ID, R.color.white);
        }
    }

    // Встановлення значення закладки для автозміни кольору
    public static void setBookmarkStatus(View view, Context context, boolean bookmarkStatus) {
        ToolbarBuilder.bookmarkStatus = bookmarkStatus;
        resetBookmarkButtonColor(view, context);
    }

    // Функція оновлення асету перемикача глобального захисту перегляду
    private static void resetVisibilitySwitchIcon(ImageView switchIcon, boolean switchStatus) {
        if (switchStatus) {
            switchIcon.setImageResource(R.drawable.vector__close_eye);
        } else {
            switchIcon.setImageResource(R.drawable.vector__open_eye);
        }
    }

    // Встановлення події натиснення на кнопку перемикача глобального захисту перегляду
    public static void setOnClickToVisibilitySwitchButton(View view, Function<Void, Boolean> getSwitchStatus, Runnable action) {
        ImageView switchIcon = view.findViewById(VISIBILITY_SWITCH_ID);
        resetVisibilitySwitchIcon(switchIcon, getSwitchStatus.apply(null));
        ViewUtils.setOnClickToView(view, VISIBILITY_SWITCH_ID, () -> {
            action.run();
            resetVisibilitySwitchIcon(switchIcon, getSwitchStatus.apply(null));
        });
    }

    // Функція встановлює подію довгого натиснення кнопки генератора
    public static void setOnLongClickToGenerator(View view, Runnable action) {
        ViewUtils.setOnLongClickToView(view, GENERATOR_ID, () -> {
            action.run();
        });
    }
}