package com.kibergod.passwordstorage.ui.pages.create;

import static com.kibergod.passwordstorage.model.Record.MAX_FIELDS_LENGTH;
import static com.kibergod.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.kibergod.passwordstorage.model.Record.MAX_TITLE_LENGTH;
import static com.kibergod.passwordstorage.model.Record.getMaxFieldNameLength;
import static com.kibergod.passwordstorage.model.Record.getMaxFieldValueLength;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;
import com.kibergod.passwordstorage.ui.tools.CategorySelectionDialog;
import com.kibergod.passwordstorage.ui.tools.IconSelectionDialog;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.Vibrator;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.ArrayList;


public class CreateRecordFragment extends Fragment {
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;
    private HomeViewModel homeViewModel;

    private TextView textViewStatus;

    private String tempIconId;

    private int fieldCounter = 0;

    private ArrayList<EditText> fieldNames = new ArrayList<>();
    private ArrayList<EditText> fieldValues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_record, container, false);

        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedGeneratorDataViewModel = new ViewModelProvider(requireActivity()).get(SharedGeneratorDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        ViewUtils.setMaxLengthForInput(view, R.id.editCreateRecordTitle, MAX_TITLE_LENGTH);
        ViewUtils.setMaxLengthForInput(view, R.id.editCreateRecordText, MAX_TEXT_LENGTH);

        tempIconId = "vector_template_image";
        textViewStatus = view.findViewById(R.id.createRecordStatus);

        ToolbarBuilder.addToolbarToView(view, requireContext(), false, false,false,true, true, false,true,false);

        setCategoriesToDropdownButton(view);
        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
        setOnClickToAddField(view);
        ToolbarBuilder.setOnClickToGenPassword(view, R.id.editCreateRecordText, res -> sharedGeneratorDataViewModel.getPassword(requireContext()), () -> {
            Vibrator.vibrate(requireContext());
        });
        ToolbarBuilder.setOnLongClickToGenerator(view, () -> ((HomeActivity) requireActivity()).setPasswordGeneratorFragment());
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editCreateRecordText, requireContext());
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editCreateRecordTitle, requireContext(),true);
        ImageUtils.setImageViewSize(view, R.id.createRecordIcon, ImageUtils.getScreenWidth()/3);
        resizeFonts(view);
        return view;
    }

    private void resizeFonts(View view) {
        int fontSizeMain = sharedSettingsDataViewModel.getFontSizeMain();
        int fontSizeInput = sharedSettingsDataViewModel.getFontSizeInput();
        int fontSizeButtons = sharedSettingsDataViewModel.getFontSizeButtons();
        int fontSizeOther = sharedSettingsDataViewModel.getFontSizeOther();

        FontUtils.setFontSizeToView(requireContext(), view, R.id.pageTitle, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.categoryText, fontSizeMain);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.editCreateRecordTitle, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editCreateRecordText, fontSizeInput);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.addFieldButtonText, fontSizeButtons);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.createRecordStatus, fontSizeOther);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.selectedCategoryText, fontSizeOther);
    }


    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        TextView selectedCategoryTextView = view.findViewById(R.id.selectedCategoryText);
        selectedCategoryTextView.setText(CategorySelectionDialog.getEmptyCategoryText());

        ArrayList<Category> categories = new ArrayList<>(sharedCategoriesDataViewModel.getAllCategories());
        categories.add(0, new Category(null, CategorySelectionDialog.getEmptyCategoryText()));

        ViewUtils.setOnClickToView(view, R.id.dropdownCreateRecordCategoryButton, () -> {
            ((HomeActivity) requireActivity()).callCategorySelectionDialog(selectedCategoryTextView, categories, view, requireContext(), sharedSettingsDataViewModel.getFontSizeMain());
        });
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.imgTick, () -> getNewRecord(view));
    }

    // Обробка створення нового запису
    private void getNewRecord(View view) {
        EditText textInput = view.findViewById(R.id.editCreateRecordText);
        EditText titleInput = view.findViewById(R.id.editCreateRecordTitle);
        String recordTitle = titleInput.getText().toString();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textViewStatus.getLayoutParams();
        if (recordTitle.length() != 0) {
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle)) {
                textViewStatus.setText("");
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(CategorySelectionDialog.getSelectedCategoryName(view));
                sharedRecordsDataViewModel.addRecord(
                        recordTitle,
                        textInput.getText().toString(),
                        category_id,
                        tempIconId,
                        homeViewModel.getStringsArray(fieldNames),
                        homeViewModel.getStringsArray(fieldValues),
                        sharedSettingsDataViewModel.getFiltersSortParam(),
                        sharedSettingsDataViewModel.getFiltersSortMode());
                Toast.makeText(getActivity(), "Створено запис " + recordTitle, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Запис з такою назвою вже існує");
                params = ViewUtils.getParamsForValidLine(requireContext(), params, 5);
                ViewUtils.setScrollToTop(view, R.id.scrollView);
            }
        } else {
            textViewStatus.setText("Назва не може бути порожньою");
            params = ViewUtils.getParamsForValidLine(requireContext(), params, 5);
            ViewUtils.setScrollToTop(view, R.id.scrollView);
        }
        textViewStatus.setLayoutParams(params);
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.createRecordIcon);
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

    // Встановлення обробника події натиснення на додавання полей
    private void setOnClickToAddField(View rootView) {
        ViewUtils.setOnClickToView(rootView, R.id.addFieldButton, () -> createNewField(rootView));
    }

    // Додає у ScrollArea рядок з двома полями вводу та кнопкою видалення
    private void createNewField(View view) {
        if (fieldCounter < MAX_FIELDS_LENGTH) {
            LinearLayout parentContainer = view.findViewById(R.id.mainContainer);
            View fieldView = getLayoutInflater().inflate(R.layout.fragment_edit_field, null);

            FontUtils.setFontSizeToView(requireContext(), fieldView, R.id.titleField, sharedSettingsDataViewModel.getFontSizeInput());
            FontUtils.setFontSizeToView(requireContext(), fieldView, R.id.valueField, sharedSettingsDataViewModel.getFontSizeInput());

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(20), 0, 0);

            EditText editTextName = fieldView.findViewById(R.id.titleField);
            EditText editTextValue = fieldView.findViewById(R.id.valueField);
            ViewUtils.setMaxLengthForInput(editTextName, getMaxFieldNameLength());
            ViewUtils.setMaxLengthForInput(editTextValue, getMaxFieldValueLength());
            fieldNames.add(editTextName);
            fieldValues.add(editTextValue);
            editTextName.setId(View.generateViewId());
            editTextValue.setId(View.generateViewId());
            fieldCounter++;
            ((HomeActivity) requireActivity()).updateTextInAddFieldButton(view, fieldCounter);

            setOnClickToDeleteFieldButton(parentContainer, fieldView, editTextName, editTextValue, view);

            LinearLayout addFieldButton = view.findViewById(R.id.addFieldButton);
            parentContainer.addView(fieldView, parentContainer.indexOfChild(addFieldButton), layoutParams);
            ToolbarBuilder.setEditTextFocusChangeListener(view, editTextValue.getId(), requireContext());
            ToolbarBuilder.setEditTextFocusChangeListener(view, editTextName.getId(), requireContext(), true);

            ViewUtils.setScrollToBottom(view, R.id.scrollView);
        }
    }

    // Функція обробки натиснення на кнопку видалення поля
    private void setOnClickToDeleteFieldButton(LinearLayout parentContainer, View fieldView, EditText editTextName, EditText editTextValue, View rootView) {
        ViewUtils.setOnClickToView(fieldView, R.id.deleteFieldButton, () -> {
            parentContainer.removeView(fieldView);
            fieldNames.remove(editTextName);
            editTextValue.setOnFocusChangeListener(null);
            fieldValues.remove(editTextValue);
            fieldCounter--;
            ((HomeActivity) requireActivity()).updateTextInAddFieldButton(rootView, fieldCounter);
        });
    }
}