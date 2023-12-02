package com.kibergod.passwordstorage.ui.create;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.ui.CategorySelectionDialog;
import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.model.Category;
import com.kibergod.passwordstorage.ui.HomeActivity;
import com.kibergod.passwordstorage.ui.HomeViewModel;

import java.util.ArrayList;


public class CreateRecordFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;
    private HomeViewModel homeViewModel;

    private TextView textViewStatus;

    private int tempIconId;

    private int fieldCounter = 0;

    private ArrayList<EditText> fieldNames = new ArrayList<>();
    private ArrayList<EditText> fieldValues = new ArrayList<>();

    private EditText currentEditTextForGenerator;

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

        tempIconId = getResources().getIdentifier("vector_template_image", "drawable", requireContext().getPackageName());
        textViewStatus = view.findViewById(R.id.createRecordStatus);

        setCategoriesToDropdownButton(view);

        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
        setOnClickToAddField(view);
        ((HomeActivity) requireActivity()).setOnClickToBackButton(view);
        setOnClickToGenPassword(view);
        ((HomeActivity) requireActivity()).setOnClickToEraseInput(view);
        setEditTextFocusChangeListener(view, R.id.editCreateRecordText);
        setEditTextFocusChangeListener(view, R.id.editCreateRecordTitle, true);
        ((HomeActivity) requireActivity()).setIconColorsToToolbar(view, requireContext());
        return view;
    }

    // Функція закріпляє за кнопкою діалогове меню зі списком категорій
    private void setCategoriesToDropdownButton(View view) {
        TextView selectedCategoryTextView = view.findViewById(R.id.selectedCategoryText);

        selectedCategoryTextView.setText(homeViewModel.getEmptyCategoryText());

        ArrayList<Category> categories = new ArrayList<>(sharedCategoriesDataViewModel.getAllCategories());
        categories.add(0, new Category(null, homeViewModel.getEmptyCategoryText()));

        LinearLayout dropdownButton = view.findViewById(R.id.dropdownCreateRecordCategoryButton);
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropdownMenu(selectedCategoryTextView, categories, view);
            }
        });
    }

    // Функція, що спрацьовуватиме при обранні категорій з списку
    private void showDropdownMenu(TextView selectedCategoryTextView, ArrayList<Category> categories, View view) {
        ((HomeActivity) requireActivity()).hideKeyboard();
        ImageView selectedCategoryIcon = view.findViewById(R.id.selectedCategoryIcon);
        CategorySelectionDialog.showCategorySelectionDialog(
                requireContext(),
                categories,
                getSelectedCategoryName(view),
                categoryId -> {
            String categoryName = sharedCategoriesDataViewModel.getCategoryNameById(categoryId);
            if (categoryName.equals("")) {
                selectedCategoryTextView.setText(homeViewModel.getEmptyCategoryText());
                selectedCategoryIcon.setImageResource(R.drawable.vector_template_image);
            } else {
                selectedCategoryTextView.setText(categoryName);
                selectedCategoryIcon.setImageResource(sharedCategoriesDataViewModel.getCategoryIconIdById(categoryId));
            }
        });
    }

    // Функція встановлює подію натискання кнопки збереження введених змін
    private void setOnClickToSaveButton(View view) {
        ImageView saveButton = view.findViewById(R.id.imgTick);
        saveButton.setOnClickListener(new View.OnClickListener() {
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
        ScrollView scrollView = view.findViewById(R.id.scrollView2);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textViewStatus.getLayoutParams();
        if (recordTitle.length() != 0) {
            if (sharedRecordsDataViewModel.checkRecordTitleUnique(recordTitle)) {
                textViewStatus.setText("");
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(getSelectedCategoryName(view));
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
                textViewStatus.setText("Запис з такою назвою вже існує");
                params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
                scrollView.smoothScrollTo(0, 0);
            }
        } else {
            textViewStatus.setText("Назва не може бути порожньою");
            params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
            scrollView.smoothScrollTo(0, 0);
        }
        textViewStatus.setLayoutParams(params);
    }

    // Повертає обрану категорію
    private String getSelectedCategoryName(View view) {
        TextView selectedCategoryTextView = view.findViewById(R.id.selectedCategoryText);
        return selectedCategoryTextView.getText().toString();
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
        LinearLayout addFieldButton = rootView.findViewById(R.id.addFieldButton);
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
            editTextName.setId(View.generateViewId());
            editTextValue.setId(View.generateViewId());
            fieldCounter++;
            updateTextInAddFieldButton(view);

            setOnClickToDeleteFieldButton(parentContainer, fieldView, editTextName, editTextValue, view);

            LinearLayout addFieldButton = view.findViewById(R.id.addFieldButton);
            parentContainer.addView(fieldView, parentContainer.indexOfChild(addFieldButton), layoutParams);
            setEditTextFocusChangeListener(view, editTextValue.getId());
            setEditTextFocusChangeListener(view, editTextName.getId(), true);


            ScrollView scrollView = view.findViewById(R.id.scrollView2);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
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
                updateTextInAddFieldButton(rootView);
            }
        });
    }

    // Оновлення тексту кнопки додавання полей
    private void updateTextInAddFieldButton(View view) {
        TextView addFieldButtonText = view.findViewById(R.id.addFieldButtonText);
        addFieldButtonText.setText("Додати поле (" + Integer.toString(fieldCounter) + "/10)");
    }

    private void setEditTextFocusChangeListener(View view, int editTextId) {
        setEditTextFocusChangeListener(view, editTextId, false);
    }

    // Автооновлення фокус-поля для подальшого використання функції генераціїї пароля
    private void setEditTextFocusChangeListener(View view, int editTextId, boolean isTotal) {
        final EditText editText = view.findViewById(editTextId);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!isTotal) {
                        currentEditTextForGenerator = editText;
                        ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.imgGears, R.color.purple);
                    }
                    ((HomeActivity) requireActivity()).setCurrentEditTextTotal(editText);
                    ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.imgEraser, R.color.white);
                } else if (editText == currentEditTextForGenerator){
                    if (!isTotal) {
                        currentEditTextForGenerator = null;
                        ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.imgGears, R.color.gray_text);
                    }
                    ((HomeActivity) requireActivity()).setCurrentEditTextTotal(null);
                    ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.imgEraser, R.color.gray_text);
                }
            }
        });
    }

    // Функція встановлює подію натискання кнопки генерації пароля
    private void setOnClickToGenPassword(View view) {
        ImageView generateButton = view.findViewById(R.id.imgGears);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEditTextForGenerator != null) {
                    String password = sharedGeneratorDataViewModel.getPassword(requireContext());
                    if (currentEditTextForGenerator.getId() == R.id.editCreateRecordText) {
                        int cursorPosition = currentEditTextForGenerator.getSelectionStart();
                        String currentText = currentEditTextForGenerator.getText().toString();
                        String newText = currentText.substring(0, cursorPosition) + password +
                                currentText.substring(cursorPosition);
                        currentEditTextForGenerator.setText(newText);
                    } else {
                        currentEditTextForGenerator.setText(password);
                    }
                }
            }
        });
    }
}