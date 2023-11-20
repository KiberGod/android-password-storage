package com.kibergod.passwordstorage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

/*
 *  Даний клас є реалізацією системи довідок RSS (Rabbit Support System)
 */
public class RabbitSupport {

    // Перелік ідентифікаторів доступних довідкових вікон
    public enum SupportDialogIDs {
        DIGITAL_OWNER_HIDE_MODE(0),
        DIGITAL_OWNER_PROTECTED_MODE(1),
        DIGITAL_OWNER_DATA_DELETION_MODE(2),
        MAIN_PASSWORD(3);

        private final int value;

        SupportDialogIDs(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /*
     *  Конструктор довідкового вікна, простий
     *
     * Context context              -   контекст, поверх якого буде відображено довідкове вікно
     * SupportDialogIDs dialogID    -   ідентифікатор довідкового вікна
     * View rootView                -   вікно, поверх якого буде відображено довідкове вікно
     * int blurViewId               -   ідентифікатор блюр-елемента основного вікна
     */
    public static Dialog getRabbitSupportDialog(Context context, SupportDialogIDs dialogID, View rootView, int blurViewId) {
        return getRabbitSupportDialog(context, dialogID, rootView, blurViewId, false);
    }

    /*
     *  Конструктор довідкового вікна, розширений
     *
     * Context context              -   контекст, поверх якого буде відображено довідкове вікно
     * SupportDialogIDs dialogID    -   ідентифікатор довідкового вікна
     * View rootView                -   вікно, поверх якого буде відображено довідкове вікно
     * int blurViewId               -   ідентифікатор блюр-елемента основного вікна
     * boolean activeBlockFlag      -   флаг, що відповідає за додаткову, активну частину (з кнопками) діалогового вікна
     *                                      true    ->  кнопки будуть відображені
     *                                      false   ->  кнопки будуть видалені
     */
    public static Dialog getRabbitSupportDialog(Context context, SupportDialogIDs dialogID, View rootView, int blurViewId, boolean activeBlockFlag) {
        Dialog rabbitSupportDialog = new Dialog(context, R.style.InfoDialog);
        rabbitSupportDialog.setContentView(R.layout.dialog_rabbit_support);

        ImageView imageView = rabbitSupportDialog.findViewById(R.id.infoImg);
        imageView.setImageResource(getImageId(dialogID.getValue()));
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.purple), PorterDuff.Mode.SRC_IN);

        TextView infoSection = rabbitSupportDialog.findViewById(R.id.infoSection);
        TextView infoTitle = rabbitSupportDialog.findViewById(R.id.infoTitle);
        TextView mainInfoText = rabbitSupportDialog.findViewById(R.id.mainInfoText);
        infoSection.setText("Розділ: " + getSection(dialogID.getValue()));
        infoTitle.setText(getTitle(dialogID.getValue()));
        mainInfoText.setText(getMainText(dialogID.getValue()));


        View blurView = rootView.findViewById(blurViewId);
        blurView.setVisibility(View.VISIBLE);
        rabbitSupportDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                blurView.setVisibility(View.GONE);
            }
        });

        if (!activeBlockFlag) {
            LinearLayout infoActiveBlock = rabbitSupportDialog.findViewById(R.id.infoActiveBlock);
            ((ViewGroup) infoActiveBlock.getParent()).removeView(infoActiveBlock);
        }

        return rabbitSupportDialog;
    }

    // Набір назв секцій
    private static String getSection(int id) {
        return new String[]{
                "Налаштування > Цифровий власник",
                "Налаштування > Цифровий власник",
                "Налаштування > Цифровий власник",
                "Налаштування"
        }[id];
    }

    // Набір тайтлів
    private static String getTitle(int id) {
        return new String[]{
                "Режим приховання даних",
                "Режим захисту входу",
                "Режим видалення даних",
                "Пароль для входу"
        }[id];
    }

    // Набір описів
    private static String getMainText(int id) {
        return new String[]{
                "Копії ваших файлів даних (записи та категорії) будуть приховані до моменту їх відновлення. Оригінали будуть замінені порожніми, тимчасовими файлами. Для відновлення прихованих даних необідно ввести ваш пароль входу у калькуляторі задом наперед. Після відновлення тимчасові дані будуть об`єднані з відновленними.",
                "У цьому режимі Цифровий власник через задану кількість днів заблокує можливість входити по стандартному паролю. Для входу буде необхідно ввести ваш пароль задом наперед. \n\nВідлік часу почнеться з поточної дати.",
                "У цьому режимі Цифровий власник через задану кількість днів після першого успішного заходу одразу знищить усі дані записів та категорій. Дані буде неможливо відновити. Видалення відбудеться до того, як хтось при вході зможе побачити дані. \n\nВідлік часу почнеться з поточної дати.",
                "Описовий текст головного пароля для входу. Описовий текст головного пароля для входу. Описовий текст головного пароля для входу. Описовий текст головного пароля для входу."
        }[id];
    }

    // Набір ідентифікаторів картинок
    private static int getImageId(int id) {
        return new int[]{
                R.drawable.vector__modern_rabbit,
                R.drawable.vector__modern_rabbit,
                R.drawable.vector__modern_rabbit,
                R.drawable.vector__vertical_key
        }[id];
    }

}
