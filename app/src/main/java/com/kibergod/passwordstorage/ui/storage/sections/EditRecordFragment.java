package com.kibergod.passwordstorage.ui.storage.sections;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.HomeActivity;
import com.kibergod.passwordstorage.ui.HomeViewModel;

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

    private EditText currentEditText;

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

        printRecordData(view);
        setOnClickToCancelEditRecordButton(view);
        setCategoriesToDropdownButton(view);
        setOnClickToSaveButton(view);
        setOnClickToDeleteButton(view);
        setOnClickToIconSelectWindow(view);
        setOnClickToAddField(view);
        setOnClickToGenPassword(view);
        setEditTextFocusChangeListener(view, R.id.editEditRecordText);

        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.editEditRecordTitle, sharedRecordsDataViewModel.getRecordTitleByIndex(recordIndex));
        setTextViewText(view, R.id.editEditRecordText, sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex));

        ImageView recordIcon = view.findViewById(R.id.editRecordIcon);
        recordIcon.setImageResource(getResources().getIdentifier(sharedRecordsDataViewModel.getRecordIconIdByIndex(recordIndex), "drawable", requireContext().getPackageName()));

        for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
            createNewField(
                    view,
                    sharedRecordsDataViewModel.getRecordFieldNameByIndex(recordIndex, i),
                    sharedRecordsDataViewModel.getRecordFieldValueByIndex(recordIndex, i)
            );
        }
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        EditText editText = view.findViewById(textViewId);
        editText.setText(text);
    }

    // Функція встановлює подію переходу на попередню сторінку (з переглядом запису)
    private void setOnClickToCancelEditRecordButton(View view) {
        Button button = view.findViewById(R.id.cancelEditRecordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        String buttonText = sharedCategoriesDataViewModel.getCategoryNameById(
                sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex)
        );
        if (buttonText.equals("")) {
            buttonText = homeViewModel.getEmptyCategoryText();
        }
        Button dropdownButton = view.findViewById(R.id.dropdownEditRecordCategoryButton);
        dropdownButton.setText(buttonText);

        ArrayList<Category> categories = new ArrayList<>(sharedCategoriesDataViewModel.getAllCategories());
        categories.add(0, new Category(null, homeViewModel.getEmptyCategoryText()));

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropdownMenu(dropdownButton, adapter, categories);
            }
        });
    }

    // Функція, що спрацьовуватиме при обранні категорій з списку
    private void showDropdownMenu(Button dropdownButton, ArrayAdapter<Category> adapter, ArrayList<Category> categories) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Category selectedCategory = categories.get(which);
                dropdownButton.setText(selectedCategory.getName());
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        Button button = view.findViewById(R.id.saveEditRecordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditRecord(view);
            }
        });
    }

    // Функція встановлює подію натискання кнопки видалення запису
    private void setOnClickToDeleteButton(View view) {
        Button button = view.findViewById(R.id.deleteRecordButton);
        button.setOnClickListener(new View.OnClickListener() {
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
        if (recordTitle.length() != 0) {
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle, recordIndex)) {
                textViewStatus.setText("");
                Button categoryButton = view.findViewById(R.id.dropdownEditRecordCategoryButton);
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(categoryButton.getText().toString());
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
                textViewStatus.setText("Запис з таким заголовком вже існує");
            }
        } else {
            textViewStatus.setText("Заголовок запису не може бути порожнім");
        }
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
        Button addFieldButton = rootView.findViewById(R.id.addNewFieldButton);
        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewField(rootView, "", "");
            }
        });
    }

    // Додає у ScrollArea рядок з двома полями вводу та кнопкою видалення
    private void createNewField(View view, String fieldName, String fieldValue) {
        if (fieldCounter < MAX_FIELDS_LENGTH) {
            LinearLayout linearLayout = view.findViewById(R.id.editRecordsScrollArea);

            LinearLayout newLayout = new LinearLayout(requireContext());
            newLayout.setOrientation(LinearLayout.HORIZONTAL);

            EditText editTextName = homeViewModel.getEditText(requireContext(), "Назва", getMaxFieldNameLength());
            EditText editTextValue = homeViewModel.getEditText(requireContext(), "Значення", getMaxFieldValueLength());

            editTextName.setText(fieldName);
            editTextValue.setText(fieldValue);

            fieldNames.add(editTextName);
            fieldValues.add(editTextValue);
            editTextValue.setId(View.generateViewId());

            newLayout.addView(editTextName);
            newLayout.addView(editTextValue);
            newLayout.addView(getButton(newLayout, editTextName, editTextValue));

            linearLayout.addView(newLayout, linearLayout.getChildCount() - 4);
            fieldCounter++;

            setEditTextFocusChangeListener(view, editTextValue.getId());
        }
    }

    // Створює кнопку видалення поля
    private Button getButton(LinearLayout linearLayout, EditText editTextName, EditText editTextValue) {
        Button button = new Button(requireContext());
        button.setText("-");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ViewGroup)linearLayout.getParent()).removeView(linearLayout);
                fieldNames.remove(editTextName);
                editTextValue.setOnFocusChangeListener(null);
                fieldValues.remove(editTextValue);
                fieldCounter--;
            }
        });
        return button;
    }

    // Автооновлення фокус-поля для подальшого використання функції генераціїї пароля
    private void setEditTextFocusChangeListener(View view, int editTextId) {
        final EditText editText = view.findViewById(editTextId);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    currentEditText = editText;
                } else if (editText == currentEditText){
                    currentEditText = null;
                }
            }
        });
    }

    // Функція встановлює подію натискання кнопки генерації пароля
    private void setOnClickToGenPassword(View view) {
        Button button = view.findViewById(R.id.generatePasswordButton2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEditText != null) {
                    String password = sharedGeneratorDataViewModel.getPassword(requireContext());
                    if (currentEditText.getId() == R.id.editEditRecordText) {
                        int cursorPosition = currentEditText.getSelectionStart();
                        String currentText = currentEditText.getText().toString();
                        String newText = currentText.substring(0, cursorPosition) + password +
                                currentText.substring(cursorPosition);
                        currentEditText.setText(newText);
                    } else {
                        currentEditText.setText(password);
                    }
                }
            }
        });
    }
}