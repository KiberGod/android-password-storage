package com.example.passwordstorage.ui.storage;

import androidx.appcompat.widget.TooltipCompat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedDataViewModel;
import com.example.passwordstorage.ui.storage.sections.BookmarksFragment;
import com.example.passwordstorage.ui.storage.sections.CategoriesFragment;
import com.example.passwordstorage.ui.storage.sections.RecordsFragment;
import com.google.android.material.tabs.TabLayout;

public class StorageFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    private SharedDataViewModel mViewModel;

    public static StorageFragment newInstance() {
        return new StorageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        addFragment(view);
        hideHintForTabLayout();
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

}