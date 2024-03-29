package com.kibergod.passwordstorage.ui.pages.storage.sections;

import static com.kibergod.passwordstorage.model.Record.MAX_FIELDS_LENGTH;
import static com.kibergod.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.kibergod.passwordstorage.model.Record.MAX_TITLE_LENGTH;
import static com.kibergod.passwordstorage.model.Record.getMaxFieldNameLength;
import static com.kibergod.passwordstorage.model.Record.getMaxFieldValueLength;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class EditRecordFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;

    private static final String RECORD_ID = "record_id";

    private int recordId;

    private String tempIconId;

    private int fieldCounter = 0;

    private ArrayList<EditText> fieldNames = new ArrayList<>();
    private ArrayList<EditText> fieldValues = new ArrayList<>();

    private TextView textViewStatus;

    public EditRecordFragment() {
        // Required empty public constructor
    }

    public static EditRecordFragment newInstance(int recordId) {
        EditRecordFragment fragment = new EditRecordFragment();
        Bundle args = new Bundle();
        args.putInt(RECORD_ID, recordId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recordId = getArguments().getInt(RECORD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_record, container, false);

        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedGeneratorDataViewModel = new ViewModelProvider(requireActivity()).get(SharedGeneratorDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        ViewUtils.setMaxLengthForInput(view, R.id.editEditRecordTitle, MAX_TITLE_LENGTH);
        ViewUtils.setMaxLengthForInput(view, R.id.editEditRecordText, MAX_TEXT_LENGTH);

        textViewStatus = view.findViewById(R.id.editRecordStatus);

        tempIconId = sharedRecordsDataViewModel.getRecordIconIdById(recordId);

        ToolbarBuilder.addToolbarToView(view, requireContext(), false,false,false,true, true, true,true,false);

        printRecordData(view);
        setCategoriesToDropdownButton(view);
        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
        setOnClickToAddField(view);
        setOnClickToDeleteButton(view);
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editEditRecordText, requireContext());
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editEditRecordTitle, requireContext(),true);
        ToolbarBuilder.setOnClickToGenPassword(view, R.id.editEditRecordText, res -> sharedGeneratorDataViewModel.getPassword(requireContext()), () -> {
            Vibrator.vibrate(requireContext());
        });
        ToolbarBuilder.setOnLongClickToGenerator(view, () -> ((HomeActivity) requireActivity()).setPasswordGeneratorFragment());
        ImageUtils.setImageViewSize(view, R.id.editRecordIcon, ImageUtils.getScreenWidth()/3);
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

        FontUtils.setFontSizeToView(requireContext(), view, R.id.editEditRecordTitle, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editEditRecordText, fontSizeInput);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.addFieldButtonText, fontSizeButtons);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.editRecordStatus, fontSizeOther);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.selectedCategoryText, fontSizeOther);
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.editEditRecordTitle, sharedRecordsDataViewModel.getRecordTitleById(recordId));
        setTextViewText(view, R.id.editEditRecordText, sharedRecordsDataViewModel.getRecordTextById(recordId));

        ImageView recordIcon = view.findViewById(R.id.editRecordIcon);
        recordIcon.setImageResource(getResources().getIdentifier(sharedRecordsDataViewModel.getRecordIconIdById(recordId), "drawable", requireContext().getPackageName()));

        for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
            String fieldName = sharedRecordsDataViewModel.getRecordFieldNameById(recordId, i);
            String fieldValue = sharedRecordsDataViewModel.getRecordFieldValueById(recordId, i);
            if (!fieldName.equals("") || !fieldValue.equals("")) {
                createNewField(
                        view,
                        fieldName,
                        fieldValue,
                        false
                );
            }
        }
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        EditText editText = view.findViewById(textViewId);
        editText.setText(text);
    }

    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        TextView selectedCategoryTextView = view.findViewById(R.id.selectedCategoryText);

        selectedCategoryTextView.setText(sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idById(recordId)));
        if (selectedCategoryTextView.getText().toString().equals("")) {
            selectedCategoryTextView.setText(CategorySelectionDialog.getEmptyCategoryText());
        } else {
            ImageView selectedCategoryIcon = view.findViewById(R.id.selectedCategoryIcon);
            selectedCategoryIcon.setImageResource(
                    getResources().getIdentifier(sharedCategoriesDataViewModel.getCategoryIconIdById(sharedRecordsDataViewModel.getRecordCategory_idById(recordId)), "drawable", requireContext().getPackageName())
            );
        }

        ArrayList<Category> categories = new ArrayList<>(sharedCategoriesDataViewModel.getAllCategories());
        categories.add(0, new Category(null, CategorySelectionDialog.getEmptyCategoryText()));

        ViewUtils.setOnClickToView(view, R.id.dropdownEditRecordCategoryButton, () -> {
            ((HomeActivity) requireActivity()).callCategorySelectionDialog(selectedCategoryTextView, categories, view, requireContext(), sharedSettingsDataViewModel.getFontSizeMain());
        });
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.imgTick, () -> getEditRecord(view));
    }

    // Функція встановлює подію натискання кнопки видалення запису
    private void setOnClickToDeleteButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.imgTrash, () -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Ви впевнені, що хочете видалити запис? В разі видалення ви зможете відновити цей запис з архіву протягом 1 місяця, починаючи з поточної дати.");
            builder.setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteRecord();
                }
            });
            builder.setNegativeButton("Відмінити", null);
            builder.show();
        });
    }

    // Обробка редагування запису
    private void getEditRecord(View view) {
        EditText textInput = view.findViewById(R.id.editEditRecordText);
        EditText titleInput = view.findViewById(R.id.editEditRecordTitle);
        String recordTitle = titleInput.getText().toString();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textViewStatus.getLayoutParams();
        if (recordTitle.length() != 0) {
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle, recordId)) {
                textViewStatus.setText("");
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(CategorySelectionDialog.getSelectedCategoryName(view));
                sharedRecordsDataViewModel.editRecord(
                        recordId,
                        recordTitle,
                        textInput.getText().toString(),
                        category_id, tempIconId,
                        homeViewModel.getStringsArray(fieldNames),
                        homeViewModel.getStringsArray(fieldValues),
                        sharedSettingsDataViewModel.getFiltersSortParam(),
                        sharedSettingsDataViewModel.getFiltersSortMode());
                Toast.makeText(getActivity(), "Запис змінено " + recordTitle, Toast.LENGTH_SHORT).show();
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

    // Оброка видалення запису
    private void deleteRecord() {
        sharedRecordsDataViewModel.deleteRecord(recordId);
        ((HomeActivity) requireActivity()).setStorageFragment();
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.editRecordIcon);
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
        ViewUtils.setOnClickToView(rootView, R.id.addFieldButton, () -> createNewField(rootView, "", "", true));
    }

    // Додає у ScrollArea рядок з двома полями вводу та кнопкою видалення
    private void createNewField(View view, String fieldName, String fieldValue, boolean needScroll) {
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
            editTextName.setText(fieldName);
            editTextValue.setText(fieldValue);
            editTextName.setId(View.generateViewId());
            editTextValue.setId(View.generateViewId());
            fieldCounter++;
            ((HomeActivity) requireActivity()).updateTextInAddFieldButton(view, fieldCounter);

            setOnClickToDeleteFieldButton(parentContainer, fieldView, editTextName, editTextValue, view);

            LinearLayout addFieldButton = view.findViewById(R.id.addFieldButton);
            parentContainer.addView(fieldView, parentContainer.indexOfChild(addFieldButton), layoutParams);
            ToolbarBuilder.setEditTextFocusChangeListener(view, editTextValue.getId(), requireContext());
            ToolbarBuilder.setEditTextFocusChangeListener(view, editTextName.getId(), requireContext(), true);

            if (needScroll) {
                ViewUtils.setScrollToBottom(view, R.id.scrollView);
            }
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