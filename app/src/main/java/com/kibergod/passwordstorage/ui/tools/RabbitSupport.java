package com.kibergod.passwordstorage.ui.tools;

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

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/*
 *  Даний клас є реалізацією системи довідок RSS (Rabbit Support System)
 */
public class RabbitSupport {

    public class Section {

        public class Subsection {
            private int name_id;
            private int icon_id;
            private int text_id;

            public Subsection(int name_id, int icon_id, int text_id) {
                this.name_id = name_id;
                this.icon_id = icon_id;
                this.text_id = text_id;
            }

            public int getName_id() { return name_id; }
            public int getIcon_id() { return icon_id; }
            public int getText_id() { return text_id; }
            public String getName() { return getTitle(name_id); }
            public int getIcon() { return getImageId(icon_id); }
            public String getText() { return getMainText(text_id); }
        }

        private int name_id;
        private int icon_id;
        private ArrayList<Subsection> subsections;

        public Section(int name_id, int icon_id) {
            this.name_id = name_id;
            this.icon_id = icon_id;
            subsections = new ArrayList<>();
        }

        public void addSubsection(int subName_id, int subIcon_id, int subText_id) {
            subsections.add(new Subsection(subName_id, subIcon_id, subText_id));
        }

        public int getName_id() { return name_id; }
        public int getIcon_id() { return icon_id; }
        public String getName() { return getSection(name_id); }
        public int getIcon() { return getImageId(icon_id); }
        public ArrayList<Subsection> getSubsections() { return subsections; }
    }

    private static ArrayList<Section> sections;


    public RabbitSupport() {
        sections = new ArrayList<>();

        Section section1 = new Section(0,0);
        section1.addSubsection(0,0, 0);
        section1.addSubsection(14,13,16);
        section1.addSubsection(15,14,17);
        section1.addSubsection(16,15,18);
        section1.addSubsection(17,16,19);
        section1.addSubsection(1,6,1);
        section1.addSubsection(2,7,2);
        sections.add(section1);

        Section section2 = new Section(1,1);
        section2.addSubsection(3,1,3);
        section2.addSubsection(4,1,4);
        sections.add(section2);

        Section section3 = new Section(2,2);
        section3.addSubsection(0,2,5);
        section3.addSubsection(5,8,6);
        section3.addSubsection(6,17,7);
        section3.addSubsection(7,18,8);
        sections.add(section3);

        Section section4 = new Section(3,3);
        section4.addSubsection(8,9,9);
        section4.addSubsection(9,10,10);
        section4.addSubsection(10,11,11);
        sections.add(section4);

        Section section5 = new Section(4,4);
        section5.addSubsection(0,4,12);
        section5.addSubsection(11,12,13);
        section5.addSubsection(12,12,14);
        section5.addSubsection(13,12,15);
        sections.add(section5);

        Section section6 = new Section(5,18);
        sections.add(section6);
    }

    public static ArrayList<Section> getSections() {
        return sections;
    }

    public static ArrayList<Section.Subsection> getSubsectionsBySectionIndex(int index) {
        return sections.get(index).getSubsections();
    }

    public static Section getSectionByIndex(int index) {
        return sections.get(index);
    }


    // Перелік ідентифікаторів доступних довідкових вікон
    public enum SupportDialogIDs {
        STORAGE_GENERAL_INFO(0,0),
        STORAGE_RECORD(0,1),
        STORAGE_CATEGORY(0,2),
        STORAGE_BOOKMARK(0,3),
        STORAGE_METADATA(0,4),
        STORAGE_SEARCH(0,5),
        STORAGE_FILTERS(0,6),
        CREATE_RECORD(1,0),
        CREATE_CATEGORY(1,1),
        TOOLS_GENERAL_INFO(2,0),
        TOOLS_GENERATOR(2,1),
        TOOLS_ARCHIVE(2,2),
        TOOLS_RSS(2,3),
        SETTINGS_LOGIN_PASSWORD(3,0),
        SETTINGS_SESSION_PROTECT(3,1),
        SETTINGS_CALC_CLEARING(3,2),
        DIGITAL_OWNER_GENERAL_INFO(4,0),
        DIGITAL_OWNER_HIDE_MODE(4,1),
        DIGITAL_OWNER_PROTECTED_MODE(4,2),
        DIGITAL_OWNER_DELETION_MODE(4,3);

        private final int sectionId;
        private final int subsectionId;


        SupportDialogIDs(int sectionId, int subsectionId) {
            this.sectionId = sectionId;
            this.subsectionId = subsectionId;
        }

        public String getSectionName() { return sections.get(sectionId).getName(); }
        public String getSubsectionName() { return sections.get(sectionId).subsections.get(subsectionId).getName(); }
        public String getSubsectionText() { return sections.get(sectionId).subsections.get(subsectionId).getText(); }
        public int getSubsectionIcon() { return sections.get(sectionId).subsections.get(subsectionId).getIcon(); }
    }

    /*
     *  Конструктор довідкового вікна, простий
     *
     * Context context              -   контекст, поверх якого буде відображено довідкове вікно
     * SupportDialogIDs dialogID    -   ідентифікатор довідкового вікна
     * View rootView                -   вікно, поверх якого буде відображено довідкове вікно
     */
    public static Dialog getRabbitSupportDialog(Context context, SupportDialogIDs dialogID, View rootView, int fontSizeRssMain, int fontSizeRssSecondary) {
        return getRabbitSupportDialog(context, dialogID, rootView, false, fontSizeRssMain, fontSizeRssSecondary);
    }

    /*
     *  Конструктор довідкового вікна, розширений
     *
     * Context context              -   контекст, поверх якого буде відображено довідкове вікно
     * SupportDialogIDs dialogID    -   ідентифікатор довідкового вікна
     * View rootView                -   вікно, поверх якого буде відображено довідкове вікно
     * boolean activeBlockFlag      -   флаг, що відповідає за додаткову, активну частину (з кнопками) діалогового вікна
     *                                      true    ->  кнопки будуть відображені
     *                                      false   ->  кнопки будуть видалені
     */
    public static Dialog getRabbitSupportDialog(Context context, SupportDialogIDs dialogID, View rootView, boolean activeBlockFlag, int fontSizeRssMain, int fontSizeRssSecondary) {
        Dialog rabbitSupportDialog = new Dialog(context, R.style.InfoDialog);
        rabbitSupportDialog.setContentView(R.layout.dialog_rabbit_support);

        ImageView imageView = rabbitSupportDialog.findViewById(R.id.infoImg);
        imageView.setImageResource(dialogID.getSubsectionIcon());
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.purple), PorterDuff.Mode.SRC_IN);

        FontUtils.setFontSizeToView(context, rootView, rabbitSupportDialog, R.id.mainInfoText, fontSizeRssMain);
        FontUtils.setFontSizeToView(context, rootView, rabbitSupportDialog, R.id.nameWindow, fontSizeRssSecondary);
        FontUtils.setFontSizeToView(context, rootView, rabbitSupportDialog, R.id.infoSection, fontSizeRssSecondary);

        TextView infoSection = rabbitSupportDialog.findViewById(R.id.infoSection);
        TextView infoTitle = rabbitSupportDialog.findViewById(R.id.infoTitle);
        TextView mainInfoText = rabbitSupportDialog.findViewById(R.id.mainInfoText);
        infoSection.setText("Розділ: " + dialogID.getSectionName());
        infoTitle.setText(dialogID.getSubsectionName());
        mainInfoText.setText(dialogID.getSubsectionText());

        View blurView = rootView.findViewById(R.id.blurView);
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

        if (dialogID.getSubsectionText().length() > 400) {
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
                "Сховище",
                "Сторінка створення",
                "Інструменти",
                "Налаштування",
                "Цифровий власник",
                "FAQ",
        }[id];
    }

    // Набір тайтлів
    private static String getTitle(int id) {
        return new String[]{
                "Загальна інформація",
                "Пошук",
                "Фільтри",
                "Створення запису",
                "Створення категорії",
                "Генератор",
                "Архів",
                "RSS-довідник",
                "Пароль для входу",
                "Сесійний захист",
                "Очищення калькулятора",
                "Режим приховання даних",
                "Режим захисту входу",
                "Режим видалення даних",
                "Запис",
                "Категорія",
                "Закладка",
                "Метадані",
        }[id];
    }

    // Набір описів
    private static String getMainText(int id) {
        return new String[]{
                "Сховище - це місце, де зберігаються записи, категорії та закладки. Саме звідси ви можете керувати переліченими об'єктами, а саме:\n\n- переглядати\n- редагувати\n- видаляти\n\n Слід зазначити, що видалені або приховані Цифровим власником записи зберігаються поза межами сховища, тож знайти їх тут не вдасться (до моменту їх відновлення). \n\nЩоб перемикатися  між списками записів, категорій та закладок, використовуйте свайп ліворуч та праворуч.",
                "Сховище має вбудовану пошукову систему. Для її спрацювання достатньо просто почати щось вводити у відповідне поле. Пошук працює за наступними правилами:\n\n1) В якості поля для порівняння використовується назва.\n\n2) Імунітет до регістру літер. Неважливо, які літери ви використовуватимете при пошуку, великі чи малі. Так само неважливо, з яких літер складається назва. Наприклад, якщо є запис з іменем 'Запис 1', то для його пошуку можна використати будь-яку з наступних конструкцій: 'запис 1', 'ЗАПИС 1', 'Запис 1' або 'зАпИс 1'. Результат буде однаковий.\n\n3) Пошук у контексті. Нема ніякої необхідності вводити точну назву шуканого об'єкта, достатньо ввести її частину. Наприклад запис з назвою 'Запис 2' можна знайти, використовуючи наступні конструкції: 'АПИС', 'ап', 'заП', 'с 2' та інші.\n\nВсі об'єкти, що не відповідають пошуковому запиту будуть тимчасово приховані.",
                "Сховище має власну систему фільтрів. Вони використовуються для того, щоб змінити порядок та сортування об'єктів. Загалом є дві групи фільтрів: 'сортування' та 'критерій'. Фільтр критерію вказує, яке саме поле обрати в якості критерію сортування. На даний момент доступно 3 варіанти:\n\n - дата оновлення\n - дата перегляду\n - дата створення\n\nВ залежності від обраного критерію, буде змінюватись іконка та часова мітка на картках об'єктів.\n\nДругий фільтр надає можливість змінити порядок сортування, починаючи або від найстаріших об'єктів, або від найновіших.\n\nФільтри можуть працювати як самостійно, так і разом з пошуковим запитом, сортуючи результати пошуку.",
                "Щоб створити запис, достатньо перейти у розділ створення та натиснути відповідну кнопку. Процес створення є стандартним та не містить якихось особливостей. Все що необхідно - це заповнити поля даними та зберегти.\n\nТакож тут присутня валідація даних, яка розповсюджується на поле назви запису. Якщо запис з такою назвою вже існує, система видасть відповідне повідомлення з проханням змінити назву. Це зроблено для того, аби унеможливити створення однойменних записів та майбутньої плутанини користувача у них.\n\n * якщо ви впевнені, що ваша назва є унікальною та немає жодного запису з такою ж самою назвою, але валідація все одно видає попередження, це означає одне з наступного: \n\n- в архіві міститься запис з такою є самою назвою\n- серед прихованих Цифровим власником записів є запис з такою самою назвою",
                "Щоб створити категорію, достатньо перейти у розділ створення та натиснути відповідну кнопку. Процес створення є стандартним та не містить якихось особливостей. Все що необхідно - це заповнити поля даними та зберегти.\n\nТакож тут присутня валідація даних, яка розповсюджується на поле назви категорії. Якщо категорія з такою назвою вже існує, система видасть відповідне повідомлення з проханням змінити назву. Це зроблено для того, аби унеможливити створення однойменних категорій та майбутньої плутанини користувача у них.",
                "У даному розділі містяться деякі додаткові підсистеми, представлені у вигляді інструментів. Їх основною метою є спрощення та покращення роботи користувача з програмою. На даний момент цей розділ містить: \n\n - генератор\n - архів\n - RSS-довідник",
                "Генератор - це одна з підсистем Rabbit Hole, що надає можливість генерувати паролі, коди, секретні ключі та інші дані. Доступ до функції генерації надається у два способи:\n\n- напряму зі сторінки генератора\n- за допомогою інструменту швидкого доступу на сторінках редагування запису\n\nДля використання другого способу достатньо лише один раз налаштувати генератор. Надалі в нього можна навіть не заходити та використовувати виставлені налаштування генерації напряму у контексті редагування запису.\n\nГенератор має ряд налаштувань, які роблять його генерацію більш гнучкою та різноманітною. Присутні наступні налаштування: \n\n1) Довжина поля. Максимальна довжина сягає 100 символів, мінімальна - 1 символ.\n\n2) Заборонені символи. Ви можете вказати, які саме символи не слід використовувати під час генерації. За замовчуванням це '@%*^#&', але ви можете змінити цей перелік.\n\n3) Використання символьних сетів. Загалом є 4 сети:\n - цифри [0-9]\n - маленькі літери [a-z]\n - великі літери [A-Z]\n - спец.символи\nВи маєте можливість додавати та вилучати ці сети з процесу генерації. Також, окремо для кожного сету, ви можете налаштувати спосіб задання кількості символів, які будуть використані під час генерації. Випадкова кількість буде автоматично змінюватись перед кожною генерацією, а ручне задання кількості залишить цей параметр статичним. Кількість символів вказує, скільки саме генератору необхідно взяти символів з даного сету.\n\n* Сет спец.символів містить наступний набір символів:\n!@#$%^&*()-_=+[]{}|;:'\",.<>?\n\n* для швидкого відкриття сторінки генератора зі сторінок редагування записів, просто натисніть та утримуйте його іконку.",
                "Архів - це одна з підсистем Rabbit Hole, що деякий час зберігає в собі видалені записи. Якщо з моменту видалення запису пройде 1 місяць, то запис буде видалено назавжди. До цього моменту користувача є можливість переглядати видалені (зі сховища) записи та відновляти їх. Час до автовидалення вказується на картках записів. Архівовані записи не відображаються у сховищі. Відновлені записи повертаються до архіву.\n\nТакож архів надає можливість очистити одразу всі записи не дочікуючись автовидалення. Ця дія (як і автовидалення) назавжди видалить запис і відновити його буде неможливо. Якщо на момент приховання даних Цифровим власником запис знаходився у архіві, то очищення архіву не видалить його. Ця опція стосується лише явно відображених записів архіву.  \n\n* активація Цифрового власника у режимі приховання даних приховає також і архівовані записи, але це не збереже їх від автовидалення через місяць, тож будьте уважні!",
                "RSS (або Rabbit Support System) - це одна з підсистем Rabbit Hole, що представляє собою інтерактивний довідник. Зараз ви знаходитесь у ньому. У різних місцях інтерфейсу програми є елементи, натиснення на які викличе RSS-вікно з короткою довідковою інформацією прямо у контексті того розділу, де відбувся виклик.\n\nRSS містить інформацію стосовно:\n\n- розділів програми\n- підсистем та інструментів\n- об'єктів даних\n- налаштувань\n - деякі поради щодо використання",
                "Це ваш особистий ключ для входу у сховище. Він вводиться у калькуляторі, тому має містити лише:\n\n- цифри (0-9)\n- символи ('.', '-', '+')\n\nТакож пароль не може бути порожнім або перевищувати довжину в 9 знаків.",
                "Будучи ввімкненою, дана опція повертатиме вас до калькулятора щоразу, коли:\n\n- програму було згорнуто\n- екран пристрою погас\n- пристрій вимкнуто\n\nПідвищує загальний рівень безпеки ваших даних.",
                "Дане налаштування дозволяє спростити вхід до сховища, шляхом видалення попередніх даних вводу у калькуляторі (якщо увімкнено).\n\nПідвищує безпеку використання програми при сторонніх людях.",
                "Цифровий власник - це підсистема Rabbit Hole, що містить алгоритми автоматизованого керування програмою. На даний момент має 3 режими роботи:\n\n- приховання даних\n- захист входу\n- видалення даних\n\nЦільовою задачею даної підсистеми є підвищення безпеки шляхом використання нестандартних методів.\n\nБудьте обережні при використанні та взаємодії з Цифровим власником. Уважно ознайомтесь з деталями обраного режиму, перш ніж його вмикати. Ця підсистема є вкрай небезпечною, допустивши помилку, ви ризикуєте втратити свої дані або навіть доступ до сховища.\n\nВикористовуйте тільки у тому разі, якщо точно знаєте, що робите.",
                "В разі активації цього режиму, Цифровий власник миттєво сховає всі існуючі записи. Для їх відновлення необхідно ввести ваш пароль у калькуляторі задом наперед. Ви можете спокійно продовжувати користуватись сховищем, створюючи нові записи навіть до моменту відновлення старих. Під час відновлення Цифровий власник об'єднає сховані дані з новоствореними.\n\nВи можете повторно приховувати дані. Під час відновлення буде повернуто абсолютно усі записи\n\n* для цього режиму не треба вказувати дату спрацювання",
                "У цьому режимі Цифровий власник заблокує можливість входити по стандартному паролю починаючи з певної, зазначеної вами, дати спрацювання (включно).  Для входу буде необхідно ввести ваш пароль задом наперед. \n\n* для цього режиму обов'язково вкажіть дату спрацювання",
                "У цьому режимі Цифровий власник чекатиме настання дати спрацювання, після чого чекатиме будь-який успішний вхід у сховище (неважливо, у вказану дату спрацювання або пізніше). Як тільки ці умови будуть виконані, він миттєво видалить всі дані записів та категорій. Видалення відбудеться до того, як хтось при вході зможе побачити хоч щось.\n\nПам'ятайте, дані будуть видалені не одразу (навіть є час, щоб вимкнути цей режим), але після видалення відновити їх буде неможливо.\n\n* для цього режиму обов'язково вкажіть дату спрацювання",
                "Запис - це основний об'єкт зберігання інформації. Він складається з:\n\n- назви\n- основного тексту\n- категорії\n- іконки\n- 10 полів типу 'назва-значення'\n\nОбов'язковим для заповнення є лише поле назви, воно повинно бути унікальним. Всі інші поля не мають такої вимоги, але мають ряд інших обмежень, в основному, пов'язаних з максимальним значенням довжини. Обмеження працює не за кількістю символів, а за кількістю байт (різні символи займають різну кількість байт), через що в одне й те саме поле вміститься вдвічі більше англійських літер, ніж українських.\n\nВи можете виконувати з записом наступні дії:\n\n- переглядати\n- редагувати\n- створювати\n- видаляти (переміщувати в архів)\n- видаляти назавжди (з архіву)\n- додавати до закладок\n\nДля роботи з записом вам надається ряд інструментів:\n\n- ластик для миттєвого очищення поля\n- швидкий доступ до генератора\n- інструмент візуального захисту даних\n- швидке копіювання (для значень полів)\n\nБудь-який запис має блок метаданих (див. детальніше у підрозділі 'Метадані'.",
                "Категорія - це другорядний об'єкт даних. Вона складається з назви та іконки. Використовується для структуризації записів, шляхом їх об'єднання. Якщо запис не має власної іконки, категорія автоматично підставить у картку запису свою іконку (до моменту появи власної іконки запису).\n\nЗ набору інструментів для категорії доступне лише використання ластику. Як і у записі, в категорії є блок метаданих. Окрім нього є ще один блок - блок статистики. Там відображені усі записи, до яких приєднана дана категорія, та їх загальна кількість.\n\n* архівовані записи будуть мати червоний колір у блоці статистики, а приховані Цифровим власником - зовсім відсутні.",
                "Закладка - це той же самий запис, який майже не чім не відрізняється. Закладки мають окреме місце у сховищі. Можуть використовуватися як швидкий доступ до записів.",
                "Метадані - це особливий блок даних, що мають усі категорії та записи (і закладки відповідно). Цей блок складається з:\n\n1) Дати створення. Встановлюється один раз і більше ніколи не змінюється.\n\n2) Дати перегляду. Оновлюється кожен раз, коли користувач переходить на сторінку конкретного запису чи категорії.\n\n3) Дати редагування. Оновлюється щоразу після успішного редагування об'єкта.\n\n4) Дата видалення (тільки в архіві й тільки для записів). Встановлюється в момент перенесення запису в архів. Може бути очищений, якщо запис буде відновлено.\n\nНа відміну від інших полів, поля метаданих не можуть бути напряму змінені користувачем.",
        }[id];
    }

    // Набір ідентифікаторів картинок
    private static int getImageId(int id) {
        return new int[]{
                R.drawable.vector__lock_24,
                R.drawable.vector__add_circle_24,
                R.drawable.vector__tools,
                R.drawable.vector__settings_24,
                R.drawable.vector__running_rabbit,
                R.drawable.vector_template_image,
                R.drawable.vector__search,
                R.drawable.vector__filters,
                R.drawable.vector__gears,
                R.drawable.vector__vertical_key,
                R.drawable.vector__phone_lock,
                R.drawable.vector__eraser,
                R.drawable.vector__modern_rabbit,
                R.drawable.vector__file,
                R.drawable.vector__category,
                R.drawable.vector__bookmark,
                R.drawable.vector__binary,
                R.drawable.vector__archive,
                R.drawable.vector__faq,
        }[id];
    }

    // Функція встановлює вспливаючі вікна з довідками від RabbitSupport (короткий клік)
    public static void setRabbitSupportDialogToIconByClick(@NotNull View view, int viewId, SupportDialogIDs ID, @NotNull Context context, int fontSizeRssMain, int fontSizeRssSecondary) {
        ViewUtils.setOnClickToView(
                view,
                viewId,
                () -> {
                    Dialog infoDialog = getRabbitSupportDialog(context, ID, view, fontSizeRssMain, fontSizeRssSecondary);
                    infoDialog.show();
                }
        );
    }

    // Функція встановлює вспливаючі вікна з довідками від RabbitSupport (довгий клік)
    public static void setRabbitSupportDialogToIconByLongClick(@NotNull View view, int viewId, SupportDialogIDs ID, @NotNull Context context, int fontSizeRssMain, int fontSizeRssSecondary) {
        ViewUtils.setOnLongClickToView(
                view,
                viewId,
                () -> {
                    Dialog infoDialog = getRabbitSupportDialog(context, ID, view, fontSizeRssMain, fontSizeRssSecondary);
                    infoDialog.show();
                }
        );
    }
}
