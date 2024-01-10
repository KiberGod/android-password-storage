package com.kibergod.passwordstorage.ui.pages.create;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

public class CreateFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create, container, false);

        setOnClickToCreateButton(view, R.id.createRecordButton);
        setOnClickToCreateButton(view, R.id.createCategoryButton);

        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.createRecordButton, RabbitSupport.SupportDialogIDs.CREATE_RECORD, requireContext());
        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.createCategoryButton, RabbitSupport.SupportDialogIDs.CREATE_CATEGORY, requireContext());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Функція встановлює подію переходу на сторінку створення запису/категорії по натисненню кнопки
    private void setOnClickToCreateButton(View view, int button_id) {
        ViewUtils.setOnClickToView(view, button_id, () -> {
            if (button_id == R.id.createRecordButton) {
                ((HomeActivity) requireActivity()).setCreateRecordFragment();
            } else if (button_id == R.id.createCategoryButton) {
                ((HomeActivity) requireActivity()).setCreateCategoryFragment();
            }
        });
    }

}