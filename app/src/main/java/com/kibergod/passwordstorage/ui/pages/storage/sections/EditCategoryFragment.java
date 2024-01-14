package com.kibergod.passwordstorage.ui.pages.storage.sections;

import static com.kibergod.passwordstorage.model.Category.MAX_NAME_LENGTH;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;
import com.kibergod.passwordstorage.ui.tools.IconSelectionDialog;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

public class EditCategoryFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;

    private static final String CATEGORY_ID = "category_id";

    private int categoryId;

    private String tempIconId;

    private TextView textViewStatus;

    public EditCategoryFragment() {
        // Required empty public constructor
    }

    public static EditCategoryFragment newInstance(int categoryId) {
        EditCategoryFragment fragment = new EditCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_category, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        textViewStatus = view.findViewById(R.id.editCategoryStatus);

        homeViewModel.setMaxLengthForInput(view, R.id.editEditCategoryName, MAX_NAME_LENGTH);

        tempIconId = sharedCategoriesDataViewModel.getCategoryIconIdById(categoryId);

        ToolbarBuilder.addToolbarToView(view, requireContext(), false, false,false,true, false, true,true,false);

        printCategoryData(view);
        setOnClickToSaveButton(view);
        setOnClickToDeleteButton(view);
        setOnClickToIconSelectWindow(view);
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editEditCategoryName, requireContext());
        resizeFonts(view);
        return view;
    }

    private void resizeFonts(View view) {
        int fontSizeMain = sharedSettingsDataViewModel.getFontSizeMain();
        int fontSizeInput = sharedSettingsDataViewModel.getFontSizeInput();
        int fontSizeOther = sharedSettingsDataViewModel.getFontSizeOther();

        FontUtils.setFontSizeToView(requireContext(), view, R.id.pageTitle, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editEditCategoryName, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editCategoryStatus, fontSizeOther);
    }

    // Функція встановлення тексту до UI-компонентів
    private void printCategoryData(View view) {
        TextView categoryName = view.findViewById(R.id.editEditCategoryName);
        categoryName.setText(sharedCategoriesDataViewModel.getCategoryNameById(categoryId));

        ImageView categoryIcon = view.findViewById(R.id.editCategoryIcon);
        categoryIcon.setImageResource(
                getResources().getIdentifier(sharedCategoriesDataViewModel.getCategoryIconIdById(categoryId), "drawable", requireContext().getPackageName())
        );
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.imgTick, () -> getEditCategory(view));
    }

    // Обробка редагування категорії
    private void getEditCategory(View view) {
        EditText nameInput = view.findViewById(R.id.editEditCategoryName);
        String categoryName = nameInput.getText().toString();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textViewStatus.getLayoutParams();
        if (categoryName.length() != 0) {
            if (sharedCategoriesDataViewModel.checkCategoryNameUnique(categoryName, categoryId)) {
                textViewStatus.setText("");
                sharedCategoriesDataViewModel.editCategory(categoryId, categoryName, tempIconId, sharedSettingsDataViewModel.getFiltersSortParam(), sharedSettingsDataViewModel.getFiltersSortMode());
                Toast.makeText(getActivity(), "Змінено категорію " + categoryName, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Категорія з такою назвою вже існує");
                params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
            }
        } else {
            textViewStatus.setText("Назва не може бути порожньою");
            params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
        }
        textViewStatus.setLayoutParams(params);
    }

    // Функція встановлює подію натискання кнопки видалення категорії
    private void setOnClickToDeleteButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.imgTrash, () -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Ви впевнені, що хочете видалити категорію? Цю дію буде неможливо відмінити.");
            builder.setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCategory();
                }
            });
            builder.setNegativeButton("Відмінити", null);
            builder.show();
        });
    }

    // Оброка видалення запису
    private void deleteCategory() {
        sharedRecordsDataViewModel.detachCategory(
                categoryId
        );
        sharedCategoriesDataViewModel.deleteCategory(categoryId);
        ((HomeActivity) requireActivity()).setStorageFragment();
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.editCategoryIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IconSelectionDialog.showIconSelectionDialog(requireContext(), sharedSettingsDataViewModel.getFontSizeMain(), iconResourceId -> {
                    tempIconId = iconResourceId;
                    imageView.setImageResource(getResources().getIdentifier(iconResourceId, "drawable", requireContext().getPackageName()));
                });
            }
        });
    }
}