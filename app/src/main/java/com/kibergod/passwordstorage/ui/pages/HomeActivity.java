package com.kibergod.passwordstorage.ui.pages;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kibergod.passwordstorage.MainActivity;
import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedDigitalOwnerViewModel;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.databinding.ActivityHomeBinding;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.pages.tools.ArchiveFragment;
import com.kibergod.passwordstorage.ui.pages.tools.RabbitSupportFragment;
import com.kibergod.passwordstorage.ui.tools.CategorySelectionDialog;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.pages.create.CreateCategoryFragment;
import com.kibergod.passwordstorage.ui.pages.create.CreateFragment;
import com.kibergod.passwordstorage.ui.pages.create.CreateRecordFragment;
import com.kibergod.passwordstorage.ui.pages.tools.ToolsFragment;
import com.kibergod.passwordstorage.ui.pages.tools.PasswordGeneratorFragment;
import com.kibergod.passwordstorage.ui.pages.settings.SettingsFragment;
import com.kibergod.passwordstorage.ui.pages.storage.StorageFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.EditCategoryFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.EditRecordFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.ShowCategoryFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.ShowRecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;


public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private HomeViewModel homeViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedDigitalOwnerViewModel sharedDigitalOwnerViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;

    private int screenWidth;

    private boolean rabbitFounderMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        sharedSettingsDataViewModel = new ViewModelProvider(this).get(SharedSettingsDataViewModel.class);
        sharedCategoriesDataViewModel = new ViewModelProvider(this).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(this).get(SharedRecordsDataViewModel.class);
        sharedDigitalOwnerViewModel = new ViewModelProvider(this).get(SharedDigitalOwnerViewModel.class);
        sharedGeneratorDataViewModel = new ViewModelProvider(this).get(SharedGeneratorDataViewModel.class);

        setFragment(new StorageFragment());
        setSelectedFromBottomNavBar();

        hideHintForBottomNavMenu();

        sharedSettingsDataViewModel.setSettings();
        sharedCategoriesDataViewModel.setCategories();
        sharedDigitalOwnerViewModel.setDigitalOwner();
        sharedRecordsDataViewModel.setRecords(sharedDigitalOwnerViewModel.getRetrieveRecords());
        sharedGeneratorDataViewModel.setPasswordGenerator(this);

        sharedDigitalOwnerViewModel.setRetrieveRecords(false);
        setScreenWidth();
        new RabbitSupport();
    }

    // Повертає користувача до калькулятора у разі втрати фокусу програмою
    @Override
    protected void onRestart() {
        super.onRestart();
        if (sharedSettingsDataViewModel.getActivityProtection()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Функція встановлює прослуховування натискань на нижнє меню навігації
    private void setSelectedFromBottomNavBar() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.storage) {
                setFragment(new StorageFragment());
            } else if (item.getItemId() == R.id.create) {
                setFragment(new CreateFragment());
            } else if (item.getItemId() == R.id.tools) {
                setFragment(new ToolsFragment());
            } else if (item.getItemId() == R.id.settings) {
                setFragment(new SettingsFragment());
            }
            return true;
        });
    }

    // Перехід на інший фрагмент UI по меню навігації
    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    // Приховання підказки для BottomNavMenu шляхом перевантаження опції довгого натискання
    private void hideHintForBottomNavMenu() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
            View view = bottomNavigationView.findViewById(menuItem.getItemId());
            view.setOnLongClickListener(v -> true);
        }
    }

    // Загальна функція переходу на інший фрагмент (сторінку)
    private void navigateToFragment(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Перехід на сторінку перегляду запису
    public void setShowRecordFragment(int recordId) {
        ShowRecordFragment showRecordFragment = new ShowRecordFragment();
        Bundle args = new Bundle();
        args.putInt("record_id", recordId);
        navigateToFragment(showRecordFragment, args);
    }

    // Перехід на сторінку редагування запису
    public void setEditRecordFragment(int recordId) {
        EditRecordFragment editRecordFragment = new EditRecordFragment();
        Bundle args = new Bundle();
        args.putInt("record_id", recordId);
        navigateToFragment(editRecordFragment, args);
    }

    // Перехід на сторінку перегляду категорії
    public void setShowCategoryFragment(int categoryId) {
        ShowCategoryFragment showCategoryFragment = new ShowCategoryFragment();
        Bundle args = new Bundle();
        args.putInt("category_id", categoryId);
        navigateToFragment(showCategoryFragment, args);
    }

    // Перехід на сторінку редагування категорії
    public void setEditCategoryFragment(int categoryId) {
        EditCategoryFragment editCategoryFragment = new EditCategoryFragment();
        Bundle args = new Bundle();
        args.putInt("category_id", categoryId);
        navigateToFragment(editCategoryFragment, args);
    }

    // Перехід на сторінку створення запису
    public void setCreateRecordFragment() {
        CreateRecordFragment createRecordFragment = new CreateRecordFragment();
        navigateToFragment(createRecordFragment, null);
    }

    // Перехід на сторінку створення категорії
    public void setCreateCategoryFragment() {
        CreateCategoryFragment createCategoryFragment = new CreateCategoryFragment();
        navigateToFragment(createCategoryFragment, null);
    }

    // Перехід до стартового вікна сховища
    public void setStorageFragment() {
        StorageFragment storageFragment = new StorageFragment();
        navigateToFragment(storageFragment, null);
    }

    // Перехід на сторінку генератора паролів
    public void setPasswordGeneratorFragment() {
        PasswordGeneratorFragment passwordGeneratorFragment = new PasswordGeneratorFragment();
        navigateToFragment(passwordGeneratorFragment, null);
    }

    // Перехід на сторінку архіву
    public void setArchiveFragment() {
        ArchiveFragment archiveFragment = new ArchiveFragment();
        navigateToFragment(archiveFragment, null);
    }

    // Перехід на сторінку RSS-довідника
    public void setRabbitSupportFragment() {
        setRabbitSupportFragment(-1, -1);
    }

    // Перехід на сторінку RSS-довідника
    public void setRabbitSupportFragment(int sectionIndex) {
        setRabbitSupportFragment(sectionIndex, -1);
    }

    // Перехід на сторінку RSS-довідника
    public void setRabbitSupportFragment(int sectionIndex, int subsectionIndex) {
        RabbitSupportFragment rabbitSupportFragment = new RabbitSupportFragment();
        Bundle args = new Bundle();
        args.putInt("sectionIndex", sectionIndex);
        args.putInt("subsectionIndex", subsectionIndex);
        navigateToFragment(rabbitSupportFragment, args);
    }

    // Функція додає картку запису або категорії
    public void drawButton(View view, Context context, String title, int id_scrollArea, String icon_id, String action_at, int action_atIconId, String timeToRemove, Runnable action) {
        LinearLayout parentContainer = view.findViewById(id_scrollArea);
        View itemView = getLayoutInflater().inflate(R.layout.fragment_show_item, null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, convertDPtoPX(15), 0, 0);

        TextView itemName = itemView.findViewById(R.id.itemName);
        itemName.setText(title);

        TextView itemAction_at = itemView.findViewById(R.id.itemAction_at);
        itemAction_at.setText(action_at);

        ImageView itemAction_atIcon = itemView.findViewById(R.id.itemAction_atIcon);
        itemAction_atIcon.setImageResource(action_atIconId);

        ImageView itemIcon = itemView.findViewById(R.id.itemIcon);
        itemIcon.setImageResource(getResources().getIdentifier(icon_id, "drawable", context.getPackageName()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.run();
            }
        });

        if (timeToRemove != null) {
            TextView timeToRemoveView = itemView.findViewById(R.id.timeToRecordRemove);
            timeToRemoveView.setText("Автовидалення через: " + timeToRemove);
            timeToRemoveView.setVisibility(View.VISIBLE);
        }

        parentContainer.addView(itemView, layoutParams);
    }

    // Функція відмальовує вспливаюче вікно вибору іконки
    public void showIconSelectionDialog(Context context, Consumer<String> func) {
        hideKeyboard();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_icon_selection, null);
        builder.setView(dialogView);

        GridLayout rootLayout = dialogView.findViewById(R.id.iconsScrollArea);

        Resources res = context.getResources();
        String[] iconArray = getResources().getStringArray(R.array.vector_icons_array);

        int numColumns = 4;
        int numRows = (int) Math.ceil((float) iconArray.length / numColumns);

        rootLayout.setRowCount(numRows);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        for (int i = 0; i < iconArray.length; i++) {

            int iconResourceId = getResources().getIdentifier(iconArray[i], "drawable", context.getPackageName());

            ImageView imageView = getResizeIcon(context, iconResourceId);

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.setMargins(0, 15, 0, 15);
            layoutParams.columnSpec = GridLayout.spec(i % numColumns, 1f);
            imageView.setLayoutParams(layoutParams);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (func != null) {
                        func.accept(getResources().getResourceEntryName(iconResourceId));
                    }
                    alertDialog.dismiss();
                }
            });

            rootLayout.addView(imageView);
        }

        alertDialog.show();

        Button button = dialogView.findViewById(R.id.cancelEditIconButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { alertDialog.dismiss(); }
        });
    }

    // Функція зменшує векторне зображення, задане ідентифікатором
    public ImageView getResizeIcon(Context context, int iconResourceId) {
        ImageView imageView = new ImageView(context);
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), iconResourceId, null);

        int newWidth = vectorDrawable.getIntrinsicWidth() / 2;
        int newHeight = vectorDrawable.getIntrinsicHeight() / 2;

        Bitmap resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resizedBitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);

        imageView.setImageBitmap(resizedBitmap);
        return imageView;
    }

    // Функція приховує клавіатуру вводу, якщо та відкрита
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Функція встановлює вспливаючі вікна з довідками від RabbitSupport
    public void setRabbitSupportDialogToIcon(View view, int iconId, RabbitSupport.SupportDialogIDs ID, Context context, int blurViewId) {
        ImageView imageView = view.findViewById(iconId);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog infoDialog = RabbitSupport.getRabbitSupportDialog(context, ID, view, blurViewId);
                infoDialog.show();
            }
        });
    }

    // Встановлення кольору іконки
    public void setColorToImg(Context context, View view, int imageId, int colorId) {
        ImageView imageView = view.findViewById(imageId);
        if (imageView != null) {
            imageView.setColorFilter(ContextCompat.getColor(context, colorId), PorterDuff.Mode.SRC_IN);
        }
    }

    // Конвертація dp у px
    public int convertDPtoPX (int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // Встановлює (значення змінної, а не сам екран) ширину екрана у px
    private void setScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
    }

    // Повертає ширину екрана у px
    public int getScreenWidth() {
        return screenWidth;
    }

    public void setImageViewSize (View view, int imgId, int sizePX) {
        setImageViewSize(view, imgId, sizePX, sizePX);
    }

    // Автозменшення картинки запису
    public void setImageViewSize (View view, int imgId, int widthPX, int heightPX) {
        ImageView imageView = view.findViewById(imgId);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = widthPX;
        layoutParams.height = heightPX;
        imageView.setLayoutParams(layoutParams);
    }

    // Оновлення тексту кнопки додавання полей
    public void updateTextInAddFieldButton(View view, int fieldCounter) {
        TextView addFieldButtonText = view.findViewById(R.id.addFieldButtonText);
        addFieldButtonText.setText("Додати поле (" + Integer.toString(fieldCounter) + "/10)");
    }

    // Повертає обрану категорію
    public String getSelectedCategoryName(View view) {
        TextView selectedCategoryTextView = view.findViewById(R.id.selectedCategoryText);
        return selectedCategoryTextView.getText().toString();
    }

    // Функція, що спрацьовуватиме при обранні категорій з списку
    public void showDropdownMenu(TextView selectedCategoryTextView, ArrayList<Category> categories, View view, Context context) {
        hideKeyboard();
        ImageView selectedCategoryIcon = view.findViewById(R.id.selectedCategoryIcon);
        CategorySelectionDialog.showCategorySelectionDialog(
                context,
                categories,
                getSelectedCategoryName(view),
                categoryId -> {
                    String categoryName = sharedCategoriesDataViewModel.getCategoryNameById(categoryId);
                    if (categoryName.equals("")) {
                        selectedCategoryTextView.setText(homeViewModel.getEmptyCategoryText());
                        selectedCategoryIcon.setImageResource(R.drawable.vector_template_image);
                    } else {
                        selectedCategoryTextView.setText(categoryName);
                        selectedCategoryIcon.setImageResource(
                                getResources().getIdentifier(sharedCategoriesDataViewModel.getCategoryIconIdById(categoryId), "drawable", context.getPackageName())
                        );
                    }
                });
    }

    // Автоскролл на початок (верх) сторінки
    public void setScrollToTop(View view, int scrollId) {
        ScrollView scrollView = view.findViewById(scrollId);
        scrollView.smoothScrollTo(0, 0);
    }

    // Автоскролл у самий низ сторінки
    public void setScrollToBottom(View view, int scrollId) {
        ScrollView scrollView = view.findViewById(scrollId);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    // Повертає об`єкт вібратора
    public Vibrator getVibrator() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            return (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        }
        return null;
    }

    // Згорнути / розгорнути картку-випадаюче меню
    public void setOnClickToDropdownLayout(View view, int idLayoutHead, int idLayoutBody, boolean needScroll) {
        LinearLayout editLayoutHead = view.findViewById(idLayoutHead);
        LinearLayout editLayoutBody = view.findViewById(idLayoutBody);
        editLayoutBody.setVisibility(View.GONE);
        editLayoutHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editLayoutBody.getVisibility() == View.VISIBLE) {
                    editLayoutBody.setVisibility(View.GONE);
                } else {
                    editLayoutBody.setVisibility(View.VISIBLE);
                    if (needScroll) {
                        setScrollToBottom(view, R.id.scrollView);
                    }
                }
            }
        });
    }

    // Встановлення слухача змін для поля пошуку
    public void setTextChangedListenerToSearchBar(View view, int idClearLinear, Runnable action) {
        EditText searchEditText = findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                clearObjects(view, idClearLinear);
                action.run();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    // Очищення поля з записами / картегоріями / закладками
    private void clearObjects(View view, int idClearLinear) {
        LinearLayout linearLayout = view.findViewById(idClearLinear);
        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }
    }

    // Друк повідомлення про відсутність записів / категорій / закладок
    public void printNotFoundPage(View view, int idScrollArea, String searchParam, String notFoundMessage) {
        LinearLayout scrollArea = view.findViewById(idScrollArea);
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_not_found_page, null);
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
        setOnLongClickToRabbitImg(view, action -> {
            rabbitFounderMode = !rabbitFounderMode;
            return rabbitFounderMode; }
        );
        resetRabbitImg(view, rabbitFounderMode);
    }

    // Встановлення довгого натиску на зображення кролика
    public void setOnLongClickToRabbitImg(View view, Function<Void, Boolean> action) {
        ImageView rabbitImg = view.findViewById(R.id.rabbitFounder);
        rabbitImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vibrator = getVibrator();
                if (vibrator != null) {
                    vibrator.vibrate(100);
                }
                resetRabbitImg(view, action.apply(null));
                return true;
            }
        });
    }

    // Превстановлення зображення кролика (при пошуку)
    public void resetRabbitImg(View view, boolean flag) {
        ImageView rabbitImg = view.findViewById(R.id.rabbitFounder);

        if (flag) {
            rabbitImg.setImageResource(R.drawable.vector__rabbit_with_carrots);
        } else {
            rabbitImg.setImageResource(R.drawable.vector__rabbit_without_carrots);
        }
    }

    // Повертає фільтр упорядкування (від настаріших або від найновіших)
    public boolean getFiltersSortMode() {
        RadioGroup radioGroup = findViewById(R.id.filtersSorting);
        RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        if (selectedRadioButton.getId() == R.id.filtersSortingMode1) {
            return false;
        } else {
            return true;
        }
    }

    // Повертає фільтр критерію сортування (за якою саме датою, створення/редагування/перегляду)
    public int getFiltersParam() {
        RadioGroup radioGroup = findViewById(R.id.filtersParam);
        RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        if (selectedRadioButton.getId() == R.id.filtersParamMode1) {
            return 1;
        } else if (selectedRadioButton.getId() == R.id.filtersParamMode2) {
            return 2;
        } else {
            return 3;
        }
    }

    // Встановлення слухача змін фільтрів
    public void setOnCheckedToRadioGroup(View view, int idGroup) {
        RadioGroup radioGroup = view.findViewById(idGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (idGroup == R.id.filtersSorting) {
                    sharedSettingsDataViewModel.editFiltersSortMode(getFiltersSortMode());
                } else {
                    sharedSettingsDataViewModel.editFiltersSortParam(getFiltersParam());
                }
                sharedRecordsDataViewModel.sortRecords(sharedSettingsDataViewModel.getFiltersSortParam(), sharedSettingsDataViewModel.getFiltersSortMode());
                sharedCategoriesDataViewModel.sortCategories(sharedSettingsDataViewModel.getFiltersSortParam(), sharedSettingsDataViewModel.getFiltersSortMode());
                EditText searchEditText = findViewById(R.id.searchEditText);
                String searchText = searchEditText.getText().toString();
                searchEditText.setText( searchText + " ");
                searchEditText.setText(searchText);
                searchEditText.setSelection(searchText.length());
            }
        });
    }

    // Повертає міні-іконку параметру фільтра
    public int getAction_atIconId() {
        switch (sharedSettingsDataViewModel.getFiltersSortParam()) {
            case 1:
                return R.drawable.vector__modern_pencil;
            case 2:
                return R.drawable.vector__open_eye;
            case 3:
                return R.drawable.vector__add_circle_24;
            default:
                return R.drawable.vector__add_circle_24;
        }
    }
}