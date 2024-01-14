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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.kibergod.passwordstorage.BuildConfig;
import com.kibergod.passwordstorage.MainActivity;
import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedDigitalOwnerViewModel;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.databinding.ActivityHomeBinding;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.pages.tools.AppInfoFragment;
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
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.KeyboardUtils;
import com.kibergod.passwordstorage.ui.utils.Vibrator;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

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
        ImageUtils.setScreenWidth(this);
        new RabbitSupport();

        setVersionToToolbar();
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
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commitNow();
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

    // Перехід на сторінку довідки про програму
    public void setAppInfoFragment() {
        AppInfoFragment appInfoFragment = new AppInfoFragment();
        navigateToFragment(appInfoFragment, null);
    }

    // Функція додає картку запису або категорії
    public void drawButton(View view, Context context, String title, int id_scrollArea, String icon_id, String action_at, int action_atIconId, String timeToRemove, int fontSizeMain, int fontSizeCaptions, Runnable action) {
        LinearLayout parentContainer = view.findViewById(id_scrollArea);
        View itemView = getLayoutInflater().inflate(R.layout.fragment_show_item, null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, convertDPtoPX(15), 0, 0);

        TextView itemName = itemView.findViewById(R.id.itemName);
        itemName.setText(title);
        FontUtils.setFontSizeToView(context, itemView, R.id.itemName, fontSizeMain);

        TextView itemAction_at = itemView.findViewById(R.id.itemAction_at);
        itemAction_at.setText(action_at);
        FontUtils.setFontSizeToView(context, itemView, R.id.itemAction_at, fontSizeCaptions);

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
            FontUtils.setFontSizeToView(context, itemView, R.id.timeToRecordRemove, fontSizeCaptions);
        }

        parentContainer.addView(itemView, layoutParams);
    }

    // Конвертація dp у px
    public int convertDPtoPX (int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // Оновлення тексту кнопки додавання полей
    public void updateTextInAddFieldButton(View view, int fieldCounter) {
        TextView addFieldButtonText = view.findViewById(R.id.addFieldButtonText);
        addFieldButtonText.setText("Додати поле (" + Integer.toString(fieldCounter) + "/10)");
    }

    // Викликає появу вікна вибору категорії
    public void callCategorySelectionDialog(TextView selectedCategoryTextView, ArrayList<Category> categories, View view, Context context, int fontSize) {
        ImageView selectedCategoryIcon = view.findViewById(R.id.selectedCategoryIcon);
        CategorySelectionDialog.showCategorySelectionDialog(
                context,
                categories,
                CategorySelectionDialog.getSelectedCategoryName(view),
                fontSize,
                categoryId -> {
                    String categoryName = sharedCategoriesDataViewModel.getCategoryNameById(categoryId);
                    if (categoryName.equals("")) {
                        selectedCategoryTextView.setText(CategorySelectionDialog.getEmptyCategoryText());
                        selectedCategoryIcon.setImageResource(R.drawable.vector_template_image);
                    } else {
                        selectedCategoryTextView.setText(categoryName);
                        selectedCategoryIcon.setImageResource(
                                getResources().getIdentifier(sharedCategoriesDataViewModel.getCategoryIconIdById(categoryId), "drawable", context.getPackageName())
                        );
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

    // Встановлює поточну версію у тулбар
    private void setVersionToToolbar() {
        TextView versionTextView = findViewById(R.id.programVersionView);
        versionTextView.setText("v."+BuildConfig.VERSION_NAME);
        versionTextView.setPaintFlags(versionTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}