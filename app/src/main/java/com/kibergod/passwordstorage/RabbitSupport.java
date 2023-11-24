package com.kibergod.passwordstorage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
        MAIN_PASSWORD(3),
        SESSION_PROTECTED(4),
        INP_CALC_CLEARING(5),
        DIGITAL_OWNER(6);

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

        Button cancelButton = rabbitSupportDialog.findViewById(R.id.cancelButton);

        if (!activeBlockFlag) {
            LinearLayout infoActiveBlock = rabbitSupportDialog.findViewById(R.id.infoActiveBlock);
            ((ViewGroup) infoActiveBlock.getParent()).removeView(infoActiveBlock);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    blurView.setVisibility(View.GONE);
                    rabbitSupportDialog.cancel();
                }
            });
        } else {
            ((ViewGroup) cancelButton.getParent()).removeView(cancelButton);
        }

        if (getMainText(dialogID.getValue()).length() > 400) {
            ScrollView scrollView = rabbitSupportDialog.findViewById(R.id.scrollArea);

            ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
            layoutParams.height = (int) (300 * context.getResources().getDisplayMetrics().density + 0.5f);
            scrollView.setLayoutParams(layoutParams);
        }

        return rabbitSupportDialog;
    }

    // Набір назв секцій
    private static String getSection(int id) {
        return new String[]{
                "Налаштування > Цифровий власник",
                "Налаштування > Цифровий власник",
                "Налаштування > Цифровий власник",
                "Налаштування",
                "Налаштування",
                "Налаштування",
                "Налаштування > Цифровий власник"
        }[id];
    }

    // Набір тайтлів
    private static String getTitle(int id) {
        return new String[]{
                "Режим приховання даних",
                "Режим захисту входу",
                "Режим видалення даних",
                "Пароль для входу",
                "Сесійний захист",
                "Очищення калькулятора",
                "Цифровий власник"
        }[id];
    }

    // Набір описів
    private static String getMainText(int id) {
        return new String[]{
                "В разі активації цього режиму, Цифровий власник миттєво сховає всі існуючі категорії та записи. Для їх відновлення необхідно ввести ваш пароль у калькуляторі задом наперед. Ви можете спокійно продовжувати користуватись сховищем, створюючи нові категорії та записи навіть до моменту відновлення старих. Під час відновлення Цифровий власник об'єднає сховані дані з новоствореними.\n\nПам'ятайте, повторне приховання даних знищить попередні невідновлені дані. \n\n* для цього режиму не треба вказувати дату спрацювання",
                "У цьому режимі Цифровий власник заблокує можливість входити по стандартному паролю починаючи з певної, зазначеної вами, дати спрацювання (включно).  Для входу буде необхідно ввести ваш пароль задом наперед. \n\n* для цього режиму обов'язково вкажіть дату спрацювання",
                "У цьому режимі Цифровий власник чекатиме настання дати спрацювання, після чого чекатиме будь-який успішний вхід у сховище (неважливо, у вказану дату спрацювання або пізніше). Як тільки ці умови будуть виконані, він миттєво видалить всі дані записів та категорій. Видалення відбудеться до того, як хтось при вході зможе побачити хоч щось.\n\nПам'ятайте, дані будуть видалені не одразу (навіть є час, щоб вимкнути цей режим), але після видалення відновити їх буде неможливо.\n\n* для цього режиму обов'язково вкажіть дату спрацювання",
                "Це ваш особистий ключ для входу у сховище. Він вводиться у калькуляторі, тому має містити лише:\n\n- цифри (0-9)\n- символи ('.', '-', '+')\n\nТакож пароль не може бути порожнім або перевищувати довжину в 9 знаків.",
                "Будучи ввімкненою, дана опція повертатиме вас до калькулятора щоразу, коли:\n\n- програму було згорнуто\n- екран пристрою погас\n- пристрій вимкнуто\n\nПідвищує загальний рівень безпеки ваших даних.",
                "Дане налаштування дозволяє спростити вхід до сховища, шляхом видалення попередніх даних вводу у калькуляторі (якщо увімкнено).\n\nПідвищує безпеку використання програми при сторонніх людях.",
                "Цифровий власник - це підсистема Rabbit Hole, що містить алгоритми автоматизованого керування програмою. На даний момент має 3 режими роботи:\n\n- приховання даних\n- захист входу\n - видалення даних\n\nЦільовою задачею даної підсистеми є підвищення безпеки шляхом використання нестандартних методів.\n\nБудьте обережні при використанні та взаємодії з Цифровим власником. Уважно ознайомтесь з деталями обраного режиму, перш ніж його вмикати. Ця підсистема є вкрай небезпечною, допустивши помилку, ви ризикуєте втратити свої дані або навіть доступ до сховища.\n\nВикористовуйте тільки у тому разі, якщо точно знаєте, що робите."
        }[id];
    }

    // Набір ідентифікаторів картинок
    private static int getImageId(int id) {
        return new int[]{
                R.drawable.vector__modern_rabbit,
                R.drawable.vector__modern_rabbit,
                R.drawable.vector__modern_rabbit,
                R.drawable.vector__vertical_key,
                R.drawable.vector__phone_lock,
                R.drawable.vector__eraser,
                R.drawable.vector__running_rabbit
        }[id];
    }

}
