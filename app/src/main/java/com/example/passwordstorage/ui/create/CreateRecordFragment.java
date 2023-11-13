package com.example.passwordstorage.ui.create;

import static com.example.passwordstorage.model.Record.MAX_FIELDS_LENGTH;
import static com.example.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.example.passwordstorage.model.Record.MAX_TITLE_LENGTH;
import static com.example.passwordstorage.model.Record.getMaxFieldNameLength;
import static com.example.passwordstorage.model.Record.getMaxFieldValueLength;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedCategoriesDataViewModel;
import com.example.passwordstorage.data.SharedGeneratorDataViewModel;
import com.example.passwordstorage.data.SharedRecordsDataViewModel;
import com.example.passwordstorage.model.Category;
import com.example.passwordstorage.ui.HomeActivity;
import com.example.passwordstorage.ui.HomeViewModel;

import java.util.ArrayList;


public class CreateRecordFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;
    private HomeViewModel homeViewModel;

    private TextView textViewStatus;

    private int tempIconId = -1;

    private int fieldCounter = 0;

    private ArrayList<EditText> fieldNames = new ArrayList<>();
    private ArrayList<EditText> fieldValues = new ArrayList<>();

    private EditText currentEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_record, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedGeneratorDataViewModel = new ViewModelProvider(requireActivity()).get(SharedGeneratorDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.setMaxLengthForInput(view, R.id.editCreateRecordTitle, MAX_TITLE_LENGTH);
        homeViewModel.setMaxLengthForInput(view, R.id.editCreateRecordText, MAX_TEXT_LENGTH);

        textViewStatus = view.findViewById(R.id.createRecordStatus);

        setCategoriesToDropdownButton(view);

        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
        setOnClickToAddField(view);
        setOnClickToGenPassword(view);
        setEditTextFocusChangeListener(view, R.id.editCreateRecordText);
        return view;
    }

    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        Button dropdownButton = view.findViewById(R.id.dropdownCreateRecordCategoryButton);

        dropdownButton.setText(homeViewModel.setEmptyCategoryText());

        ArrayList<Category> categories = new ArrayList<>(sharedCategoriesDataViewModel.getAllCategories());
        categories.add(0, new Category(null, homeViewModel.setEmptyCategoryText()));

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
        Button button = view.findViewById(R.id.saveNewRecordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewRecord(view);
            }
        });
    }

    // Обробка створення нового запису
    private void getNewRecord(View view) {
        EditText textInput = view.findViewById(R.id.editCreateRecordText);
        EditText titleInput = view.findViewById(R.id.editCreateRecordTitle);
        String recordTitle = titleInput.getText().toString();
        if (recordTitle.length() != 0) {
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle)) {
                textViewStatus.setText("");
                Button categoryButton = view.findViewById(R.id.dropdownCreateRecordCategoryButton);
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(categoryButton.getText().toString());
                sharedRecordsDataViewModel.addRecord(
                        recordTitle,
                        textInput.getText().toString(),
                        category_id,
                        tempIconId,
                        homeViewModel.getStringsArray(fieldNames),
                        homeViewModel.getStringsArray(fieldValues));
                Toast.makeText(getActivity(), "Створено запис " + recordTitle, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                textViewStatus.setText("Запис з таким заголовком вже існує");
            }
        } else {
            textViewStatus.setText("Заголовок запису не може бути порожнім");
        }
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.createRecordIcon);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) requireActivity()).showIconSelectionDialog(requireContext(), iconResourceId -> {
                    tempIconId = iconResourceId;
                    imageView.setImageResource(iconResourceId);
                });
            }
        });
    }

    // Встановлення обробника події натиснення на додавання полей
    private void setOnClickToAddField(View rootView) {
        Button addFieldButton = rootView.findViewById(R.id.addFieldButton);
        addFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewField(rootView);
            }
        });
    }

    // Додає у ScrollArea рядок з двома полями вводу та кнопкою видалення
    private void createNewField(View view) {
        if (fieldCounter < MAX_FIELDS_LENGTH) {
            LinearLayout linearLayout = view.findViewById(R.id.createRecordsScrollArea);

            LinearLayout newLayout = new LinearLayout(requireContext());
            newLayout.setOrientation(LinearLayout.HORIZONTAL);

            EditText editTextName = homeViewModel.getEditText(requireContext(), "Назва", getMaxFieldNameLength());
            EditText editTextValue = homeViewModel.getEditText(requireContext(), "Значення", getMaxFieldValueLength());

            fieldNames.add(editTextName);
            fieldValues.add(editTextValue);
            editTextValue.setId(View.generateViewId());

            newLayout.addView(editTextName);
            newLayout.addView(editTextValue);
            newLayout.addView(getButton(newLayout, editTextName, editTextValue));

            linearLayout.addView(newLayout, linearLayout.getChildCount() - 3);
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
        Button button = view.findViewById(R.id.generatePasswordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEditText != null) {
                    String password = sharedGeneratorDataViewModel.getPassword(requireContext());
                    if (currentEditText.getId() == R.id.editCreateRecordText) {
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