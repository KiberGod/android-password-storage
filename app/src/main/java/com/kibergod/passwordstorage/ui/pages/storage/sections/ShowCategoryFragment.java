package com.kibergod.passwordstorage.ui.pages.storage.sections;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.model.Record;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;

import java.util.ArrayList;

public class ShowCategoryFragment extends Fragment {

    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private static final String CATEGORY_INDEX = "category_index";

    private int categoryIndex;

    public ShowCategoryFragment() {
        // Required empty public constructor
    }

    public static ShowCategoryFragment newInstance(int categoryIndex) {
        ShowCategoryFragment fragment = new ShowCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(CATEGORY_INDEX, categoryIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryIndex = getArguments().getInt(CATEGORY_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_category, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);

        ToolbarBuilder.addToolbarToView(view, requireContext(), false,false, true, false,false,false,false);
        ToolbarBuilder.setOnClickToEditButton(view, () -> ((HomeActivity) requireActivity()).setEditCategoryFragment(categoryIndex));

        printCategoryData(view);

        ((HomeActivity) requireActivity()).setOnClickToDropdownLayout(view, R.id.categoryStatisticHead, R.id.categoryStatisticBody, false);
        ((HomeActivity) requireActivity()).setOnClickToDropdownLayout(view, R.id.metadataHead, R.id.metadataBody, true);
        sharedCategoriesDataViewModel.updateCategoryViewed_atByIndex(categoryIndex);
        return view;
    }

    // Функція встановлення тексту до UI-компонентів
    private void printCategoryData(View view) {
        TextView categoryName = view.findViewById(R.id.categoryName);
        categoryName.setText(sharedCategoriesDataViewModel.getCategoryNameByIndex(categoryIndex));
        TextView categoryRecordCounter = view.findViewById(R.id.categoryRecordCounter);
        categoryRecordCounter.setText(
                "Знайдено записів з даною категорією: " +
                        sharedRecordsDataViewModel.getRecordCountByCategoryId(
                                sharedCategoriesDataViewModel.getCategoryIdByIndex(categoryIndex)
                        )
        );

        ImageView categoryIcon = view.findViewById(R.id.categoryIcon);
        categoryIcon.setImageResource(
                getResources().getIdentifier(sharedCategoriesDataViewModel.getCategoryIconIdByIndex(categoryIndex), "drawable", requireContext().getPackageName())
        );

        TextView categoryCreated_at = view.findViewById(R.id.created_at);
        categoryCreated_at.setText(sharedCategoriesDataViewModel.getCategoryCreated_atByIndex(categoryIndex));

        TextView categoryUpdated_at = view.findViewById(R.id.updated_at);
        categoryUpdated_at.setText(sharedCategoriesDataViewModel.getCategoryUpdated_atByIndex(categoryIndex));

        TextView categoryViewed_at = view.findViewById(R.id.viewed_at);
        categoryViewed_at.setText(sharedCategoriesDataViewModel.getCategoryViewed_atByIndex(categoryIndex));


        ArrayList<Record> records = sharedRecordsDataViewModel.getRecordsByCategoryId(
                sharedCategoriesDataViewModel.getCategoryIdByIndex(categoryIndex)
        );

        for (int i =0; i<records.size(); i++) {
            createRecordItem(view,i+1+ ". " + records.get(i).getTitle(), sharedRecordsDataViewModel.getRecordIndexByRecordObj(records.get(i)));
        }
    }

    // Створення поля запису категорії
    private void createRecordItem(View view, String recordName, int recordIndex) {
        LinearLayout parentContainer = view.findViewById(R.id.categoryStatisticBody);
        View recordItemView = getLayoutInflater().inflate(R.layout.fragment_show_category_record, null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(10), 0, 0);

        TextView textRecordName = recordItemView.findViewById(R.id.recordName);
        textRecordName.setText(recordName);
        textRecordName.setPaintFlags(textRecordName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        LinearLayout placeForRecordList = view.findViewById(R.id.placeForRecordList);
        recordItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) requireActivity()).setShowRecordFragment(recordIndex);
            }
        });
        parentContainer.addView(recordItemView, parentContainer.indexOfChild(placeForRecordList), layoutParams);
    }
}