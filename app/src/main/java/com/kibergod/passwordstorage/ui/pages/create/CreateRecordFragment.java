package com.kibergod.passwordstorage.ui.pages.create;

import static com.kibergod.passwordstorage.model.Record.MAX_FIELDS_LENGTH;
import static com.kibergod.passwordstorage.model.Record.MAX_TEXT_LENGTH;
import static com.kibergod.passwordstorage.model.Record.MAX_TITLE_LENGTH;
import static com.kibergod.passwordstorage.model.Record.getMaxFieldNameLength;
import static com.kibergod.passwordstorage.model.Record.getMaxFieldValueLength;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Vibrator;
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


public class CreateRecordFragment extends Fragment {

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

        tempIconId = "vector_template_image";
        textViewStatus = view.findViewById(R.id.createRecordStatus);

        ToolbarBuilder.addToolbarToView(view, requireContext(), false,false,true, true, false,true);

        setCategoriesToDropdownButton(view);
        setOnClickToSaveButton(view);
        setOnClickToIconSelectWindow(view);
        setOnClickToAddField(view);
        ToolbarBuilder.setOnClickToGenPassword(view, R.id.editCreateRecordText, sharedGeneratorDataViewModel.getPassword(requireContext()), () -> {
            Vibrator vibrator = ((HomeActivity) requireActivity()).getVibrator();
            if (vibrator != null) {
                vibrator.vibrate(100);
            }
        });
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editCreateRecordText, requireContext());
        ToolbarBuilder.setEditTextFocusChangeListener(view, R.id.editCreateRecordTitle, requireContext(),true);
        ((HomeActivity) requireActivity()).setImageViewSize(view, R.id.createRecordIcon, ((HomeActivity) requireActivity()).getScreenWidth()/3);
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
                getNewRecord(view);
            }
        });
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
                int category_id = sharedCategoriesDataViewModel.getCategoryIdByName(((HomeActivity) requireActivity()).getSelectedCategoryName(view));
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
                ((HomeActivity) requireActivity()).setScrollToTop(view, R.id.scrollView2);
            }
        } else {
            textViewStatus.setText("Назва не може бути порожньою");
            params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
            ((HomeActivity) requireActivity()).setScrollToTop(view, R.id.scrollView2);
        }
        textViewStatus.setLayoutParams(params);
    }

    // Встановлення обробника події натиснення на іконку
    private void setOnClickToIconSelectWindow(View view) {
        ImageView imageView = view.findViewById(R.id.createRecordIcon);

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
            ((HomeActivity) requireActivity()).updateTextInAddFieldButton(view, fieldCounter);

            setOnClickToDeleteFieldButton(parentContainer, fieldView, editTextName, editTextValue, view);

            LinearLayout addFieldButton = view.findViewById(R.id.addFieldButton);
            parentContainer.addView(fieldView, parentContainer.indexOfChild(addFieldButton), layoutParams);
            ToolbarBuilder.setEditTextFocusChangeListener(view, editTextValue.getId(), requireContext());
            ToolbarBuilder.setEditTextFocusChangeListener(view, editTextName.getId(), requireContext(), true);

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
                ((HomeActivity) requireActivity()).updateTextInAddFieldButton(rootView, fieldCounter);
            }
        });
    }
}