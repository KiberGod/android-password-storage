package com.kibergod.passwordstorage.ui.pages.storage;

import androidx.appcompat.widget.TooltipCompat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.storage.sections.BookmarksFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.CategoriesFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.RecordsFragment;
import com.google.android.material.tabs.TabLayout;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

public class StorageFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private boolean isShowFilters = false;

    public static StorageFragment newInstance() {
        return new StorageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage, container, false);

        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);

        addFragment(view);
        hideHintForTabLayout();
        setOnClickToFiltersButton(view);
        setFiltersSortMode(view);
        setFiltersParam(view);
        ((HomeActivity) requireActivity()).setOnCheckedToRadioGroup(view, R.id.filtersSorting);
        ((HomeActivity) requireActivity()).setOnCheckedToRadioGroup(view, R.id.filtersParam);
        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.searchButton, RabbitSupport.SupportDialogIDs.STORAGE_SEARCH, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.filtersButton, RabbitSupport.SupportDialogIDs.STORAGE_FILTERS, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        isShowFilters = false;
        super.onPause();
    }

    private void addFragment(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new RecordsFragment(), "Записи");
        adapter.addFragment(new CategoriesFragment(), "Категорії");
        adapter.addFragment(new BookmarksFragment(), "Закладки");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Функція приховує підказки, що з`являються при довгому натисканні на елементи TabLayout
    private void hideHintForTabLayout() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (tabLayout.getTabAt(i) != null) {
                TooltipCompat.setTooltipText(tabLayout.getTabAt(i).view, null);
            }
        }
    }

    // Згорнути / розгорнути картку-випадаюче меню фільтрів
    public void setOnClickToFiltersButton(View view) {
        ImageView filtersButton = view.findViewById(R.id.filtersButton);
        ViewUtils.setOnClickToDropdownView(view, R.id.filtersButton, R.id.searchBarBody,
                () -> filtersButton.setImageResource(R.drawable.vector__filters),
                () -> filtersButton.setImageResource(R.drawable.vector__mirror_filters)
        );
    }

    // Встановлення значення фільтру порядку сортування
    private void setFiltersSortMode(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.filtersSorting);
        if (sharedSettingsDataViewModel.getFiltersSortMode()) {
            radioGroup.check(R.id.filtersSortingMode2);
        } else {
            radioGroup.check(R.id.filtersSortingMode1);
        }
    }

    // Встановлення значення фільтру критерію сортування
    private void setFiltersParam(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.filtersParam);
        switch (sharedSettingsDataViewModel.getFiltersSortParam()) {
            case 1:
                radioGroup.check(R.id.filtersParamMode1);
                break;
            case 2:
                radioGroup.check(R.id.filtersParamMode2);
                break;
            case 3:
                radioGroup.check(R.id.filtersParamMode3);
                break;
        }
    }
}