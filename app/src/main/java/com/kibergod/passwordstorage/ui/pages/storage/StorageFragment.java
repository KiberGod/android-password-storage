package com.kibergod.passwordstorage.ui.pages.storage;

import androidx.appcompat.widget.TooltipCompat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.storage.sections.BookmarksFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.CategoriesFragment;
import com.kibergod.passwordstorage.ui.pages.storage.sections.RecordsFragment;
import com.google.android.material.tabs.TabLayout;

public class StorageFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    private boolean isShowFilters = false;

    public static StorageFragment newInstance() {
        return new StorageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        addFragment(view);
        hideHintForTabLayout();
        setOnClickToFiltersButton(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        LinearLayout editLayoutBody = view.findViewById(R.id.searchBarBody);
        editLayoutBody.setVisibility(View.GONE);
        filtersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowFilters = !isShowFilters;
                if (isShowFilters) {
                    editLayoutBody.setVisibility(View.VISIBLE);
                    filtersButton.setImageResource(R.drawable.vector__mirror_filters);
                } else {
                    editLayoutBody.setVisibility(View.GONE);
                    filtersButton.setImageResource(R.drawable.vector__filters);
                }
            }
        });
    }
}