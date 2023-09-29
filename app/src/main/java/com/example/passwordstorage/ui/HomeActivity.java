package com.example.passwordstorage.ui;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.passwordstorage.MainActivity;
import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedDigitalOwnerViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.data.SharedSettingsDataViewModel;
import com.example.passwordstorage.databinding.ActivityHomeBinding;
import com.example.passwordstorage.ui.create.CreateCategoryFragment;
import com.example.passwordstorage.ui.create.CreateFragment;
import com.example.passwordstorage.ui.create.CreateRecordFragment;
import com.example.passwordstorage.ui.generator.GeneratorFragment;
import com.example.passwordstorage.ui.generator.PasswordGeneratorFragment;
import com.example.passwordstorage.ui.settings.SettingsFragment;
import com.example.passwordstorage.ui.storage.StorageFragment;
import com.example.passwordstorage.ui.storage.sections.EditCategoryFragment;
import com.example.passwordstorage.ui.storage.sections.EditRecordFragment;
import com.example.passwordstorage.ui.storage.sections.ShowCategoryFragment;
import com.example.passwordstorage.ui.storage.sections.ShowRecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.function.Consumer;


public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedDigitalOwnerViewModel sharedDigitalOwnerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedSettingsDataViewModel = new ViewModelProvider(this).get(SharedSettingsDataViewModel.class);
        sharedCategoriesDataViewModel = new ViewModelProvider(this).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(this).get(SharedRecordsDataViewModel.class);
        sharedDigitalOwnerViewModel = new ViewModelProvider(this).get(SharedDigitalOwnerViewModel.class);

        setFragment(new StorageFragment());
        setSelectedFromBottomNavBar();

        hideHintForBottomNavMenu();

        sharedSettingsDataViewModel.setSettings();
        sharedCategoriesDataViewModel.setCategories();
        sharedRecordsDataViewModel.setRecords();
        sharedDigitalOwnerViewModel.setDigitalOwner();
    }

    // Повертає користувача до калькулятора у разі втрати фокусу програмою
    @Override
    protected void onStop() {
        super.onStop();
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
            } else if (item.getItemId() == R.id.generator) {
                setFragment(new GeneratorFragment());
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
    public void setShowRecordFragment(int recordIndex) {
        ShowRecordFragment showRecordFragment = new ShowRecordFragment();
        Bundle args = new Bundle();
        args.putInt("record_index", recordIndex);
        navigateToFragment(showRecordFragment, args);
    }

    // Перехід на сторінку редагування запису
    public void setEditRecordFragment(int recordIndex) {
        EditRecordFragment editRecordFragment = new EditRecordFragment();
        Bundle args = new Bundle();
        args.putInt("record_index", recordIndex);
        navigateToFragment(editRecordFragment, args);
    }

    // Перехід на сторінку перегляду категорії
    public void setShowCategoryFragment(int categoryIndex) {
        ShowCategoryFragment showCategoryFragment = new ShowCategoryFragment();
        Bundle args = new Bundle();
        args.putInt("category_index", categoryIndex);
        navigateToFragment(showCategoryFragment, args);
    }

    // Перехід на сторінку редагування категорії
    public void setEditCategoryFragment(int categoryIndex) {
        EditCategoryFragment editCategoryFragment = new EditCategoryFragment();
        Bundle args = new Bundle();
        args.putInt("category_index", categoryIndex);
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

    // Функція додає кнопку запису або категорії
    public Button drawButton(View view, Context context, String title, int id_scrollArea, int icon_id) {
        Button button = new Button(context);
        button.setText(title);
        button.setTextColor(ContextCompat.getColor(context, R.color.gray_text));

        GradientDrawable roundedRectangle = new GradientDrawable();
        roundedRectangle.setColor(ContextCompat.getColor(context, R.color.gray_2));
        roundedRectangle.setCornerRadius(36);
        ViewCompat.setBackground(button, roundedRectangle);


        //ImageView vectorImageView = new ImageView(context);
        if (icon_id == -1 ) {
            icon_id = R.drawable.vector_template_image;
        }
        //Drawable vectorDrawable = ContextCompat.getDrawable(context, icon_id);
        //vectorImageView.setImageDrawable(vectorDrawable);

        ImageView vectorImageView = getResizeIcon(context, icon_id);

        vectorImageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //vectorImageView.setColorFilter(ContextCompat.getColor(context, R.color.gray_text));


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.CENTER;
        buttonParams.bottomMargin = 20;
        buttonParams.topMargin = 20;
        buttonParams.rightMargin = 40;
        buttonParams.leftMargin = 40;
        button.setLayoutParams(buttonParams);

        button.setCompoundDrawablesWithIntrinsicBounds(vectorImageView.getDrawable(), null, null, null);

        LinearLayout rootLayout = view.findViewById(id_scrollArea);
        rootLayout.addView(button);

        return button;
    }

    // Функція відмальовує вспливаюче вікно вибору іконки
    public void showIconSelectionDialog(Context context, Consumer<Integer> func) {
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

        for (int i = 0; i < iconArray.length; i++) {

            int iconResourceId = getResources().getIdentifier(iconArray[i], "drawable", context.getPackageName());
            System.out.println(iconResourceId);

            ImageView imageView = getResizeIcon(context, iconResourceId);

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.setMargins(0, 15, 0, 15);
            layoutParams.columnSpec = GridLayout.spec(i % numColumns, 1f);
            imageView.setLayoutParams(layoutParams);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (func != null) {
                        func.accept(iconResourceId);
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
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}