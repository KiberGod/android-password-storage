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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;

import java.util.ArrayList;

public class EditRecordFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;

    private static final String RECORD_INDEX = "record_index";

    private int recordIndex;

    private String tempIconId;

    private int fieldCounter = 0;

    private ArrayList<EditText> fieldNames = new ArrayList<>();
    private ArrayList<EditText> fieldValues = new ArrayList<>();

    private TextView textViewStatus;

    public EditRecordFragment() {
        // Required empty public constructor
    }

    public static EditRecordFragment newInstance(int recordIndex) {
        EditRecordFragment fragment = new EditRecordFragment();
        Bundle args = new Bundle();
        args.putInt(RECORD_INDEX, recordIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recordIndex = getArguments().getInt(RECORD_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_record, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedGeneratorDataViewModel = new ViewModelProvider(requireActivity()).get(SharedGeneratorDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.setMaxLengthForInput(view, R.id.editEditRecordTitle, MAX_TITLE_LENGTH);
        homeViewModel.setMaxLengthForInput(view, R.id.editEditRecordText, MAX_TEXT_LENGTH);

        textViewStatus = view.findViewById(R.id.editRecordStatus);

        tempIconId = sharedRecordsDataViewModel.getRecordIconIdByIndex(recordIndex);

        ToolbarBuilder.addToolbarToView(view, requireContext(), true, true, true,true);

        printRecordData(view);
        setCategoriesToDropdownButton(view);
        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
        setOnClickToAddField(view);
        setOnClickToDeleteButton(view);
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editEditRecordText, requireContext());
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editEditRecordTitle, requireContext(),true);
        ToolbarBuilder.setOnClickToGenPassword(view, R.id.editEditRecordText, sharedGeneratorDataViewModel.getPassword(requireContext()));
        ((HomeActivity) requireActivity()).setImageViewSize(view, R.id.editRecordIcon, ((HomeActivity) requireActivity()).getScreenWidth()/3);
        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.editEditRecordTitle, sharedRecordsDataViewModel.getRecordTitleByIndex(recordIndex));
        setTextViewText(view, R.id.editEditRecordText, sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex));

        ImageView recordIcon = view.findViewById(R.id.editRecordIcon);
        recordIcon.setImageResource(getResources().getIdentifier(sharedRecordsDataViewModel.getRecordIconIdByIndex(recordIndex), "drawable", requireContext().getPackageName()));

        for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
            String fieldName = sharedRecordsDataViewModel.getRecordFieldNameByIndex(recordIndex, i);
            String fieldValue = sharedRecordsDataViewModel.getRecordFieldValueByIndex(recordIndex, i);
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

        selectedCategoryTextView.setText(sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex)));
        if (selectedCategoryTextView.getText().toString().equals("")) {
            selectedCategoryTextView.setText(homeViewModel.getEmptyCategoryText());
        } else {
            ImageView selectedCategoryIcon = view.findViewById(R.id.selectedCategoryIcon);
            selectedCategoryIcon.setImageResource(
                    getResources().getIdentifier(sharedCategoriesDataViewModel.getCategoryIconIdById(sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex)), "drawable", requireContext().getPackageName())
            );
        }

        ArrayList<Category> categories = new ArrayList<>(sharedCategoriesDataViewModel.getAllCategories());
        categories.add(0, new Category(null, homeViewModel.getEmptyCategoryText()));

        LinearLayout dropdownButton = view.findViewById(R.id.dropdownEditRecordCategoryButton);
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) requireActivity()).showDropdownMenu(selectedCategoryTextView, categories, view, requireContext());
            }
        });
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        ImageView saveButton = view.findViewById(R.id.imgTick);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditRecord(view);
            }
        });
    }

    // Функція встановлює подію натискання кнопки видалення запису
    private void setOnClickToDeleteButton(View view) {
        ImageView deleteButton = view.findViewById(R.id.imgTrash);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    // Обробка редагування запису
    private void getEditRecord(View view) {
        EditText textInput = view.findViewById(R.id.editEditRecordText);
        EditText titleInput = view.findViewById(R.id.editEditRecordTitle);
        String recordTitle = titleInput.getText().toString();

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textViewStatus.getLayoutParams();
        if (recordTitle.length() != 0) {
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle, recordIndex)) {
                textViewStatus.setText("");
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(((HomeActivity) requireActivity()).getSelectedCategoryName(view));
                sharedRecordsDataViewModel.editRecord(
                        recordIndex,
                        recordTitle,
                        textInput.getText().toString(),
                        category_id, tempIconId,
                        homeViewModel.getStringsArray(fieldNames),
                        homeViewModel.getStringsArray(fieldValues));
                Toast.makeText(getActivity(), "Запис змінено " + recordTitle, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Запис з такою назвою вже існує");
                params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
                ((HomeActivity) requireActivity()).setScrollToTop(view, R.id.scrollView);
            }
        } else {
            textViewStatus.setText("Назва не може бути порожньою");
            params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
            ((HomeActivity) requireActivity()).setScrollToTop(view, R.id.scrollView);
        }
        textViewStatus.setLayoutParams(params);
    }

    // Вікно з підтвердженням видалення запису
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Ви впевнені, що хочете видалити запис? Цю дію буде неможливо відмінити.");
        builder.setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRecord();
            }
        });
        builder.setNegativeButton("Відмінити", null);
        builder.show();
    }

    // Оброка видалення запису
    private void deleteRecord() {
        sharedRecordsDataViewModel.deleteRecord(recordIndex);
        ((HomeActivity) requireActivity()).setStorageFragment();
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.editRecordIcon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) requireActivity()).showIconSelectionDialog(requireContext(), iconResourceId -> {
                    tempIconId = iconResourceId;
                    imageView.setImageResource(getResources().getIdentifier(iconResourceId, "drawable", requireContext().getPackageName()));
                });
            }
        });
    }

    // Встановлення обробника події натиснення на додавання полей
    private void setOnClickToAddField(View rootView) {
        LinearLayout addFieldButton = rootView.findViewById(R.id.addFieldButton);
        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewField(rootView, "", "", true);
            }
        });
    }

    // Додає у ScrollArea рядок з двома полями вводу та кнопкою видалення
    private void createNewField(View view, String fieldName, String fieldValue, boolean needScroll) {
        if (fieldCounter < MAX_FIELDS_LENGTH) {
            LinearLayout parentContainer = view.findViewById(R.id.mainContainer);
            View fieldView = getLayoutInflater().inflate(R.layout.fragment_edit_field, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(20), 0, 0);

            EditText editTextName = fieldView.findViewById(R.id.titleField);
            EditText editTextValue = fieldView.findViewById(R.id.valueField);
            homeViewModel.setMaxLengthForInput(editTextName, getMaxFieldNameLength());
            homeViewModel.setMaxLengthForInput(editTextValue, getMaxFieldValueLength());
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
                ScrollView scrollView = view.findViewById(R.id.scrollView);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }
    }

    // Функція обробки натиснення на кнопку видалення поля
    private void setOnClickToDeleteFieldButton(LinearLayout parentContainer, View fieldView, EditText editTextName, EditText editTextValue, View rootView) {
        LinearLayout deleteFieldButton = fieldView.findViewById(R.id.deleteFieldButton);
        deleteFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentContainer.removeView(fieldView);
                fieldNames.remove(editTextName);
                editTextValue.setOnFocusChangeListener(null);
                fieldValues.remove(editTextValue);
                fieldCounter--;
                ((HomeActivity) requireActivity()).updateTextInAddFieldButton(rootView, fieldCounter);
            }
        });
    }
}