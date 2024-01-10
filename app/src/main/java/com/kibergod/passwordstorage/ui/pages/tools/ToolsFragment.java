package com.kibergod.passwordstorage.ui.pages.tools;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;


public class ToolsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tools, container, false);

        ViewUtils.setOnClickToView(view, R.id.passwordGeneratorButton, () -> ((HomeActivity) requireActivity()).setPasswordGeneratorFragment());
        ViewUtils.setOnClickToView(view, R.id.archiveButton, () -> ((HomeActivity) requireActivity()).setArchiveFragment());
        ViewUtils.setOnClickToView(view, R.id.rabbitSupportButton, () -> ((HomeActivity) requireActivity()).setRabbitSupportFragment());
        ViewUtils.setOnClickToView(view, R.id.appInfoButton, () -> ((HomeActivity) requireActivity()).setAppInfoFragment());

        ImageUtils.setColorToImg(requireContext(), view, R.id.generatorIcon, R.color.purple);
        ImageUtils.setColorToImg(requireContext(), view, R.id.archiveIcon, R.color.purple);
        ImageUtils.setColorToImg(requireContext(), view, R.id.rssIcon, R.color.purple);

        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.passwordGeneratorButton, RabbitSupport.SupportDialogIDs.TOOLS_GENERATOR, requireContext());
        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.archiveButton, RabbitSupport.SupportDialogIDs.TOOLS_ARCHIVE, requireContext());
        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.rabbitSupportButton, RabbitSupport.SupportDialogIDs.TOOLS_RSS, requireContext());
        return view;
    }
}