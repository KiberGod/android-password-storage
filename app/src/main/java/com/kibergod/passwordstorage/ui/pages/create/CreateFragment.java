package com.kibergod.passwordstorage.ui.pages.create;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;

public class CreateFragment extends Fragment {

    private CreateViewModel mViewModel;

    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create, container, false);

        setOnClickToCreateButton(view, R.id.createRecordButton);
        setOnClickToCreateButton(view, R.id.createCategoryButton);

        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIcon(view, R.id.createPageImg, RabbitSupport.SupportDialogIDs.CREATE_RECORD, requireContext(), R.id.blurViewInCreatePage);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // Функція встановлює подію переходу на сторінку створення запису/категорії по натисненню кнопки
    private void setOnClickToCreateButton(View view, int button_id) {
        LinearLayout createButton = view.findViewById(button_id);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_id == R.id.createRecordButton) {
                    ((HomeActivity) requireActivity()).setCreateRecordFragment();
                } else if (button_id == R.id.createCategoryButton) {
                    ((HomeActivity) requireActivity()).setCreateCategoryFragment();
                }
            }
        });
    }

}