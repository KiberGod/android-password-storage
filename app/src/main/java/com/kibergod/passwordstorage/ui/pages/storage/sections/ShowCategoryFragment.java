package com.kibergod.passwordstorage.ui.pages.storage.sections;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.model.Record;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.ArrayList;

public class ShowCategoryFragment extends Fragment {
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private static final String CATEGORY_ID = "category_id";

    private int categoryId;

    public ShowCategoryFragment() {
        // Required empty public constructor
    }

    public static ShowCategoryFragment newInstance(int categoryId) {
        ShowCategoryFragment fragment = new ShowCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_show_category, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);

        ToolbarBuilder.addToolbarToView(view, requireContext(), false,false, true, false,false,false,false,false);
        ToolbarBuilder.setOnClickToEditButton(view, () -> ((HomeActivity) requireActivity()).setEditCategoryFragment(categoryId));

        printCategoryData(view);

        ViewUtils.setOnClickToDropdownView(view, R.id.categoryStatisticHead, R.id.categoryStatisticBody);
        ViewUtils.setOnClickToDropdownView(view, R.id.metadataHead, R.id.metadataBody, null, () -> ViewUtils.setScrollToBottom(view, R.id.scrollView));

        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.metadataHead, RabbitSupport.SupportDialogIDs.STORAGE_METADATA, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        sharedCategoriesDataViewModel.updateCategoryViewed_atById(categoryId, sharedSettingsDataViewModel.getFiltersSortParam(), sharedSettingsDataViewModel.getFiltersSortMode());
        resizeFonts(view);
        return view;
    }

    private void resizeFonts(View view) {
        int fontSizeMain = sharedSettingsDataViewModel.getFontSizeMain();
        int fontSizeFieldCaptions = sharedSettingsDataViewModel.getFontSizeFieldCaptions();
        int fontSizeOther = sharedSettingsDataViewModel.getFontSizeOther();

        FontUtils.setFontSizeToView(requireContext(), view, R.id.categoryName, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.statisticBlockTitle, fontSizeMain);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.categoryRecordCounter, fontSizeOther);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.metadataTitle, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.created_at, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.updated_at, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.viewed_at, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.created_atText, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.updated_atText, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.viewed_atText, fontSizeFieldCaptions);
    }

    // Функція встановлення тексту до UI-компонентів
    private void printCategoryData(View view) {
        TextView categoryName = view.findViewById(R.id.categoryName);
        categoryName.setText(sharedCategoriesDataViewModel.getCategoryNameById(categoryId));

        ImageView categoryIcon = view.findViewById(R.id.categoryIcon);
        categoryIcon.setImageResource(
                getResources().getIdentifier(sharedCategoriesDataViewModel.getCategoryIconIdById(categoryId), "drawable", requireContext().getPackageName())
        );

        TextView categoryCreated_at = view.findViewById(R.id.created_at);
        categoryCreated_at.setText(sharedCategoriesDataViewModel.getCategoryCreated_atById(categoryId));

        TextView categoryUpdated_at = view.findViewById(R.id.updated_at);
        categoryUpdated_at.setText(sharedCategoriesDataViewModel.getCategoryUpdated_atById(categoryId));

        TextView categoryViewed_at = view.findViewById(R.id.viewed_at);
        categoryViewed_at.setText(sharedCategoriesDataViewModel.getCategoryViewed_atById(categoryId));


        ArrayList<Record> records = sharedRecordsDataViewModel.getRecordsByCategoryId(categoryId);

        int recordsCounter = 0;
        for (int i =0; i<records.size(); i++) {
            if (!records.get(i).getHidden()) {
                createRecordItem(view,i+1+ ". " + records.get(i).getTitle(), sharedRecordsDataViewModel.getRecordIdByRecordObj(records.get(i)));
                recordsCounter++;
            }
        }

        TextView categoryRecordCounter = view.findViewById(R.id.categoryRecordCounter);
        categoryRecordCounter.setText("Знайдено записів з даною категорією: " + recordsCounter);
    }

    // Створення поля запису категорії
    private void createRecordItem(View view, String recordName, int recordId) {
        LinearLayout parentContainer = view.findViewById(R.id.categoryStatisticBody);
        View recordItemView = getLayoutInflater().inflate(R.layout.fragment_show_category_record, null);
        FontUtils.setFontSizeToView(requireContext(), recordItemView, R.id.recordName, sharedSettingsDataViewModel.getFontSizeOther());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(10), 0, 0);

        TextView textRecordName = recordItemView.findViewById(R.id.recordName);
        if (sharedRecordsDataViewModel.isDeletedRecordById(recordId)) {
            textRecordName.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
        }
        textRecordName.setText(recordName);
        textRecordName.setPaintFlags(textRecordName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        LinearLayout placeForRecordList = view.findViewById(R.id.placeForRecordList);
        recordItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) requireActivity()).setShowRecordFragment(recordId);
            }
        });
        parentContainer.addView(recordItemView, parentContainer.indexOfChild(placeForRecordList), layoutParams);
    }
}