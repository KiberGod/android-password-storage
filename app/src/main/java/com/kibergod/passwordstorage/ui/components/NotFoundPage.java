package com.kibergod.passwordstorage.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.utils.Vibrator;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Function;

public class NotFoundPage {

    private static boolean rabbitFounderMode = true;

    public static void printNotFoundPage(@NotNull Context context, @NotNull View view, int idScrollArea) {
        LinearLayout scrollArea = view.findViewById(R.id.recordsScrollArea);
        View fragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_not_found_page, null);
        scrollArea.addView(fragmentView);
        TextView notFoundMessageView = view.findViewById(R.id.notFoundMessage);
        notFoundMessageView.setText("Архівованих записів не знайдено.");

        setOnLongClickToRabbitImg(context, view, action -> {
            rabbitFounderMode = !rabbitFounderMode;
            return rabbitFounderMode; }
        );
        resetRabbitImg(view, rabbitFounderMode);
    }

    // Друк повідомлення про відсутність записів / категорій / закладок
    public static void printNotFoundPage(@NotNull Context context, @NotNull View view, int idScrollArea, String searchParam, String notFoundMessage) {
        LinearLayout scrollArea = view.findViewById(idScrollArea);
        View fragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_not_found_page, null);
        scrollArea.addView(fragmentView);
        TextView notFoundMessageView = view.findViewById(R.id.notFoundMessage);
        if (!searchParam.equals("")) {
            Random random = new Random();

            int randomNumber = random.nextInt(12) + 1;
            switch (randomNumber) {
                case 1:
                    notFoundMessage = "За запитом нічого не знайдено";
                    break;
                case 2:
                    notFoundMessage = "Я не буду це шукати";
                    break;
                case 3:
                    notFoundMessage = "Можливо щось таке є, але шукай сам";
                    break;
                case 4:
                    notFoundMessage = "Як я повинен це знайти?";
                    break;
                case 5:
                    notFoundMessage = "Я нічого не знайшов, чесно";
                    break;
                case 6:
                    notFoundMessage = "І як це шукати?";
                    break;
                case 7:
                    notFoundMessage = "О ні, це я шукати точно не буду";
                    break;
                case 8:
                    notFoundMessage = "Порожньо, спробуй згадати хоч частину назви";
                    break;
                case 9:
                    notFoundMessage = "Введи будь-яку частинку назви. Якщо щось таке є - я обов'язково це знайду";
                    break;
                case 10:
                    notFoundMessage = "Можеш не використовувати CAPSLOCK, я і так знайду";
                    break;
                case 11:
                    notFoundMessage = "Спробуй ще :)";
                    break;
                case 12:
                    notFoundMessage = "Вибач, але 404, not found :)";
                    break;
            }
        }
        notFoundMessageView.setText(notFoundMessage);
        setOnLongClickToRabbitImg(context, view, action -> {
            rabbitFounderMode = !rabbitFounderMode;
            return rabbitFounderMode; }
        );
        resetRabbitImg(view, rabbitFounderMode);
    }

    // Встановлення довгого натиску на зображення кролика
    public static void setOnLongClickToRabbitImg(Context context, View view, Function<Void, Boolean> action) {
        ImageView rabbitImg = view.findViewById(R.id.rabbitFounder);
        rabbitImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator.vibrate(context);
                resetRabbitImg(view, action.apply(null));
                return true;
            }
        });
    }

    // Превстановлення зображення кролика (при пошуку)
    public static void resetRabbitImg(View view, boolean flag) {
        ImageView rabbitImg = view.findViewById(R.id.rabbitFounder);

        if (flag) {
            rabbitImg.setImageResource(R.drawable.vector__rabbit_with_carrots);
        } else {
            rabbitImg.setImageResource(R.drawable.vector__rabbit_without_carrots);
        }
    }
}
