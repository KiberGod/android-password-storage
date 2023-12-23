package com.kibergod.passwordstorage.ui.pages.storage.sections;

import static com.kibergod.passwordstorage.model.Record.MAX_FIELDS_LENGTH;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;

import java.util.ArrayList;

public class ShowRecordFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private static final String RECORD_INDEX = "record_index";

    private int recordIndex;

    private ArrayList<TextView> protectedTextViews;
    private ArrayList<Integer> protectedTextViewsIds;

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
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        protectedTextViews = new ArrayList<>();
        protectedTextViewsIds = new ArrayList<>();

        ToolbarBuilder.addToolbarToView(view, requireContext(), true,true, true, false,false,false,false);
        ToolbarBuilder.setOnClickToEditButton(view, () -> ((HomeActivity) requireActivity()).setEditRecordFragment(recordIndex));

        ToolbarBuilder.setOnClickToBookmark(view, requireContext(), () -> {
            sharedRecordsDataViewModel.editBookmarkInRecordByIndex(recordIndex);
            Vibrator vibrator = ((HomeActivity) requireActivity()).getVibrator();
            if (vibrator != null) {
                vibrator.vibrate(100);
            }
        });

        ToolbarBuilder.setOnClickToVisibilitySwitchButton(view,
                res -> sharedRecordsDataViewModel.getRecordToTalValueVisibilityByIndex(recordIndex),
                () -> {
                    sharedRecordsDataViewModel.editToTalValueVisibilityInRecordByIndex(recordIndex);
                    updateProtectedTextViews(view, sharedRecordsDataViewModel.getRecordToTalValueVisibilityByIndex(recordIndex));
                });

        printRecordData(view);
        ((HomeActivity) requireActivity()).setOnClickToDropdownLayout(view, R.id.metadataHead, R.id.metadataBody);
        sharedRecordsDataViewModel.updateRecordViewed_atByIndex(recordIndex);
        return view;
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.recordTitle, sharedRecordsDataViewModel.getRecordTitleByIndex(recordIndex));
        String category = sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex));
        if (category.equals("")) {
            category = homeViewModel.getEmptyCategoryText();
        }
        setTextViewText(view, R.id.recordCategory, category);

        if (sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex).equals("")) {
            LinearLayout linearLayout = view.findViewById(R.id.recordMainTextLayout);
            ViewGroup parentLayout = (ViewGroup) linearLayout.getParent();
            parentLayout.removeView(linearLayout);
        } else {
            setTextViewText(view, R.id.mainRecordText, sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex));
        }

        if (!sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex)).equals("")) {
            ImageView categoryIcon = view.findViewById(R.id.recordCategoryIcon);
            categoryIcon.setImageResource(getResources().getIdentifier(
                    sharedCategoriesDataViewModel.getCategoryIconIdById(sharedRecordsDataViewModel.getRecordCategory_idByIndex(recordIndex)),
                    "drawable", requireContext().getPackageName())
            );
        }

        ToolbarBuilder.setBookmarkStatus(view, requireContext(), sharedRecordsDataViewModel.getBookmarkByIndex(recordIndex));

        ImageView recordIcon = view.findViewById(R.id.recordIcon);
        recordIcon.setImageResource(getResources().getIdentifier(sharedRecordsDataViewModel.getRecordIconIdByIndex(recordIndex), "drawable", requireContext().getPackageName()));

        for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
            String fieldName = sharedRecordsDataViewModel.getRecordFieldNameByIndex(recordIndex, i);
            String fieldValue = sharedRecordsDataViewModel.getRecordFieldProtectedValueByIndex(recordIndex, i);
            if (!fieldName.equals("") || !fieldValue.equals("")) {
                createField(view, fieldName, fieldValue, i);
            }
        }

        TextView recordCreated_at = view.findViewById(R.id.created_at);
        recordCreated_at.setText(sharedRecordsDataViewModel.getRecordCreated_atByIndex(recordIndex));

        TextView recordUpdated_at = view.findViewById(R.id.updated_at);
        recordUpdated_at.setText(sharedRecordsDataViewModel.getRecordUpdated_atByIndex(recordIndex));

        TextView recordViewed_at = view.findViewById(R.id.viewed_at);
        recordViewed_at.setText(sharedRecordsDataViewModel.getRecordViewed_atByIndex(recordIndex));

        updateProtectedTextViews(view, sharedRecordsDataViewModel.getRecordToTalValueVisibilityByIndex(recordIndex));
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        TextView textView = view.findViewById(textViewId);
        textView.setText(text);
    }

    // Створює поле запису
    private void createField(View view, String fieldName, String fieldValue, int fieldIndex) {
        LinearLayout parentContainer = view.findViewById(R.id.mainContainer);
        View fieldView = getLayoutInflater().inflate(R.layout.fragment_show_field, null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(20), 0, 0);

        TextView textViewName = fieldView.findViewById(R.id.titleField);
        TextView textViewValue = fieldView.findViewById(R.id.valueField);

        textViewName.setText(fieldName);
        textViewValue.setText(fieldValue);

        ImageView imageView = fieldView.findViewById(R.id.hideButtonIcon);
        imageView.setId(View.generateViewId());
        textViewValue.setId(View.generateViewId());
        protectedTextViews.add(textViewValue);
        protectedTextViewsIds.add(fieldIndex);

        updateFieldValueBlock(textViewValue, imageView, fieldIndex);
        setOnClickToHideValueButton(fieldView, imageView.getId(), fieldIndex, textViewValue.getId());
        setOnClickToCopyValueButton(fieldView, sharedRecordsDataViewModel.getRecordFieldValueByIndex(recordIndex, fieldIndex));

        LinearLayout placeForFields = view.findViewById(R.id.placeForFields);
        parentContainer.addView(fieldView, parentContainer.indexOfChild(placeForFields), layoutParams);
    }



    // Встановлення події натиснення на кнопку приховання значення поля
    private void setOnClickToHideValueButton(View fieldView, int imageId, int fieldIndex, int textViewValueId) {
        LinearLayout hideValueButton = fieldView.findViewById(R.id.hideValueButton);
        ImageView imageView = fieldView.findViewById(imageId);
        TextView textViewValue = fieldView.findViewById(textViewValueId);

        hideValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedRecordsDataViewModel.setRecordFieldValueVisibilityByIndex(recordIndex, fieldIndex);
                updateFieldValueBlock(textViewValue, imageView, fieldIndex);
            }
        });
    }

    // Оновлення блоку значення поля
    private void updateFieldValueBlock(TextView textViewValue, ImageView imageView, int fieldIndex) {
        if (sharedRecordsDataViewModel.getRecordFieldValueVisibilityByIndex(recordIndex, fieldIndex)) {
            imageView.setImageResource(R.drawable.vector__open_eye);
            if (!sharedRecordsDataViewModel.getRecordToTalValueVisibilityByIndex(recordIndex)) {
                textViewValue.setText(sharedRecordsDataViewModel.getRecordFieldValueByIndex(recordIndex, fieldIndex));
            }
        } else {
            imageView.setImageResource(R.drawable.vector__close_eye);
            textViewValue.setText(sharedRecordsDataViewModel.getRecordFieldProtectedValueByIndex(recordIndex, fieldIndex));
        }
    }

    // Встановлення події натиснення на кнопку копіювання значення поля
    private void setOnClickToCopyValueButton(View fieldView, String value) {
        ImageView copyButton = fieldView.findViewById(R.id.copyButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrator = ((HomeActivity) requireActivity()).getVibrator();
                if (vibrator != null) {
                    vibrator.vibrate(100);
                }

                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText("label", value);
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(getActivity(), "Скопійовано у буфер обміну", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Оновлення сторінки після переключення глобальної кнопки захисту перегляду
    private void updateProtectedTextViews(View view, boolean totalVisibilityValue) {
        if (totalVisibilityValue) {
            setParamsToProtectedTextView(view,
                    sharedRecordsDataViewModel.getRecordProtectedValueByIndex(
                            sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex)
                    ), R.color.purple);
        } else {
            setParamsToProtectedTextView(view, sharedRecordsDataViewModel.getRecordTextByIndex(recordIndex), R.color.white);
        }
    }

    // Встановлення параметрів до полів, на яких розповсюджується захист перегляду
    private void setParamsToProtectedTextView(View view, String mainTextValue, int textColorId) {
        TextView mainText = view.findViewById(R.id.mainRecordText);
        if (mainText != null) {
            mainText.setText(mainTextValue);
            mainText.setTextColor(requireContext().getColor(textColorId));
        }

        for (int i=0; i<protectedTextViews.size(); i++) {
            protectedTextViews.get(i).setTextColor(requireContext().getColor(textColorId));

            protectedTextViews.get(i).setText(
                    sharedRecordsDataViewModel.getRecordFieldProtectedValueByIndex(recordIndex, protectedTextViewsIds.get(i))
            );
        }
    }

}