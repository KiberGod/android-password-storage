package com.kibergod.passwordstorage.ui.pages.storage.sections;

import static com.kibergod.passwordstorage.model.Record.MAX_FIELDS_LENGTH;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;

public class ShowRecordFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private static final String RECORD_INDEX = "record_index";

    private int recordIndex;

    public ShowRecordFragment() {
        // Required empty public constructor
    }

    public static ShowRecordFragment newInstance(int recordIndex) {
        ShowRecordFragment fragment = new ShowRecordFragment();
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
        View view = inflater.inflate(R.layout.fragment_show_record, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);

        ToolbarBuilder.addToolbarToView(view, requireContext(), true, true, false,false,false,false);
        ToolbarBuilder.setOnClickToEditButton(view, () -> ((HomeActivity) requireActivity()).setEditRecordFragment(recordIndex));
        ToolbarBuilder.setOnClickToBookmark(view, requireContext(), () -> sharedRecordsDataViewModel.editBookmarkInRecordByIndex(recordIndex));

        printRecordData(view);
        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.recordTitle, sharedRecordsDataViewModel.getRecordTitleByIndex(recordIndex));
        setTextViewText(view, R.id.recordCategory,
                sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex))
        );
        setTextViewText(view, R.id.mainRecordText, sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex));

        ToolbarBuilder.setBookmarkStatus(view, requireContext(), sharedRecordsDataViewModel.getBookmarkByIndex(recordIndex));

        ImageView recordIcon = view.findViewById(R.id.recordIcon);
        recordIcon.setImageResource(getResources().getIdentifier(sharedRecordsDataViewModel.getRecordIconIdByIndex(recordIndex), "drawable", requireContext().getPackageName()));

        LinearLayout linearLayout = view.findViewById(R.id.showFieldsScrollArea);
        for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
            String name = sharedRecordsDataViewModel.getRecordFieldNameByIndex(recordIndex, i);
            String value = sharedRecordsDataViewModel.getRecordFieldProtectedValueByIndex(recordIndex, i);
            if (!name.equals("")) {
                createField(view, linearLayout, name, value, i);
            }
        }
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        TextView textView = view.findViewById(textViewId);
        textView.setText(text);
    }

    // Створює поле запису
    private void createField(View rootView, LinearLayout linearLayout, String name, String value, int fieldIndex) {
        LinearLayout newLayout = new LinearLayout(requireContext());
        newLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textViewName = new TextView(requireContext());
        textViewName.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_2));

        textViewName.setText(name);

        LinearLayout.LayoutParams paramsName = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsName.rightMargin = 16;
        textViewName.setLayoutParams(paramsName);

        TextView textViewValue = new TextView(requireContext());
        textViewValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        textViewValue.setText(value);

        newLayout.addView(textViewName);
        newLayout.addView(textViewValue);
        if (!value.equals("")) {
            newLayout.addView(getButton(value));
        }

        Switch switchView = new Switch(requireContext());
        switchView.setChecked(sharedRecordsDataViewModel.getRecordFieldValueVisibilityByIndex(recordIndex, fieldIndex));
        newLayout.addView(switchView);

        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedRecordsDataViewModel.setRecordFieldValueVisibilityByIndex(recordIndex, fieldIndex);
                textViewValue.setText(sharedRecordsDataViewModel.getRecordFieldProtectedValueByIndex(recordIndex, fieldIndex));
            }
        });

        linearLayout.addView(newLayout);
    }

    // Створює кнопку швидкого копіювання
    private Button getButton(String value) {
        Button button = new Button(requireContext());
        button.setText("Скопіювати");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);

                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("label", value);
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(getActivity(), "Скопійовано у буфер обміну", Toast.LENGTH_SHORT).show();
            }
        });
        return button;
    }
}