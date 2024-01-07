package com.kibergod.passwordstorage.ui.pages.tools;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;


public class ToolsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tools, container, false);

        setOnClickToButton(view, R.id.passwordGeneratorButton, () -> ((HomeActivity) requireActivity()).setPasswordGeneratorFragment());
        setOnClickToButton(view, R.id.archiveButton, () -> ((HomeActivity) requireActivity()).setArchiveFragment());
        setOnClickToButton(view, R.id.rabbitSupportButton, () -> ((HomeActivity) requireActivity()).setRabbitSupportFragment());
        setOnClickToButton(view, R.id.appInfoButton, () -> ((HomeActivity) requireActivity()).setAppInfoFragment());

        ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.generatorIcon, R.color.purple);
        ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.archiveIcon, R.color.purple);
        ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.rssIcon, R.color.purple);

        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIconByLongClick(view, R.id.passwordGeneratorButton, RabbitSupport.SupportDialogIDs.TOOLS_GENERATOR, requireContext());
        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIconByLongClick(view, R.id.archiveButton, RabbitSupport.SupportDialogIDs.TOOLS_ARCHIVE, requireContext());
        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIconByLongClick(view, R.id.rabbitSupportButton, RabbitSupport.SupportDialogIDs.TOOLS_RSS, requireContext());
        return view;
    }

    // Загальна функція кліку по кнопці
    private void setOnClickToButton(View view, int button_id, Runnable action) {
        LinearLayout button = view.findViewById(button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.run();
            }
        });
    }
}