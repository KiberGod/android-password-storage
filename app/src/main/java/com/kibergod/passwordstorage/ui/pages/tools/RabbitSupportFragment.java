package com.kibergod.passwordstorage.ui.pages.tools;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.ArrayList;

public class RabbitSupportFragment extends Fragment {
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private static final String SECTION_INDEX = "sectionIndex";
    private static final String SUBSECTION_INDEX = "subsectionIndex";
    private int sectionIndex;
    private int subsectionIndex;

    private LinearLayout parentContainer;
    private LinearLayout.LayoutParams layoutParams;

    public RabbitSupportFragment() {
        // Required empty public constructor
    }

    public static RabbitSupportFragment newInstance(int sectionIndex, int subsectionIndex) {
        RabbitSupportFragment fragment = new RabbitSupportFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_INDEX, sectionIndex);
        args.putInt(SUBSECTION_INDEX, subsectionIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionIndex = getArguments().getInt(SECTION_INDEX);
            subsectionIndex = getArguments().getInt(SUBSECTION_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rabbit_support, container, false);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);

        initUI_Objects(view);

        if (sectionIndex == -1) {
            printSections();
        } else if (subsectionIndex == -1) {
            printSubsections();
            printSubsectionTitle(view);
        } else {
            printSubsectionView();
            printSubsectionTitle(view);
        }

        setOnClickToBackButton(view);
        resizeFonts(view);
        return view;
    }

    private void resizeFonts(View view) {
        int fontSizeMain = sharedSettingsDataViewModel.getFontSizeMain();

        FontUtils.setFontSizeToView(requireContext(), view, R.id.rssTitle, fontSizeMain);
    }

    private void initUI_Objects(View view) {
        parentContainer = view.findViewById(R.id.rabbitSupportScrollArea);

        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(20), 0, 0);
    }

    private void printSections() {
        ArrayList<RabbitSupport.Section> sections = RabbitSupport.getSections();
        for (int i=0; i<sections.size(); i++) {
            final int index = i;
            printSection(
                    sections.get(i).getName(),
                    sections.get(i).getIcon(),
                    () -> ((HomeActivity) requireActivity()).setRabbitSupportFragment(index)
            );
        }
    }

    private void printSubsections() {
        ArrayList<RabbitSupport.Section.Subsection> subsections = RabbitSupport.getSubsectionsBySectionIndex(sectionIndex);
        for (int i=0; i<subsections.size(); i++) {
            final int index = i;
            printSection(
                    subsections.get(i).getName(),
                    subsections.get(i).getIcon(),
                    () -> ((HomeActivity) requireActivity()).setRabbitSupportFragment(sectionIndex, index)
            );
        }
    }

    private void printSection(String name, int iconId, Runnable action) {
        View itemView = getLayoutInflater().inflate(R.layout.fragment_rabbit_support_item, null);

        TextView itemName = itemView.findViewById(R.id.nameRSSitem);
        itemName.setText(name);
        FontUtils.setFontSizeToView(requireContext(), itemView, R.id.nameRSSitem, sharedSettingsDataViewModel.getFontSizeMain());

        ImageView itemIcon = itemView.findViewById(R.id.imgRSSitem);
        itemIcon.setImageResource(iconId);
        ImageUtils.setColorToImg(requireContext(), itemView, R.id.imgRSSitem, R.color.purple);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.run();
            }
        });

        parentContainer.addView(itemView, layoutParams);
    }

    private void printSubsectionView() {
        RabbitSupport.Section section = RabbitSupport.getSectionByIndex(sectionIndex);
        View itemView = getLayoutInflater().inflate(R.layout.fragment_rabbit_support_subsection, null);

        FontUtils.setFontSizeToView(requireContext(), itemView, R.id.nameWindow, sharedSettingsDataViewModel.getFontSizeMain());

        TextView itemName = itemView.findViewById(R.id.nameSection);
        itemName.setText("Розділ: " + section.getName());
        FontUtils.setFontSizeToView(requireContext(), itemView, R.id.nameSection, sharedSettingsDataViewModel.getFontSizeMain());

        TextView itemSubName = itemView.findViewById(R.id.nameSubsection);
        itemSubName.setText(section.getSubsections().get(subsectionIndex).getName());
        FontUtils.setFontSizeToView(requireContext(), itemView, R.id.nameSubsection, sharedSettingsDataViewModel.getFontSizeMain());

        TextView itemMainText = itemView.findViewById(R.id.mainTextSubsection);
        itemMainText.setText(section.getSubsections().get(subsectionIndex).getText());
        FontUtils.setFontSizeToView(requireContext(), itemView, R.id.mainTextSubsection, sharedSettingsDataViewModel.getFontSizeMain());

        ImageView itemIcon = itemView.findViewById(R.id.imgRSSsubsection);
        itemIcon.setImageResource(section.getSubsections().get(subsectionIndex).getIcon());
        ImageUtils.setColorToImg(requireContext(), itemView, R.id.imgRSSsubsection, R.color.purple);

        parentContainer.addView(itemView, layoutParams);
    }

    private void setOnClickToBackButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.backButton, () -> getActivity().onBackPressed());
    }

    private void printSubsectionTitle(View view) {
        TextView textView = view.findViewById(R.id.rssTitle);
        textView.append(" > " + RabbitSupport.getSectionByIndex(sectionIndex).getName());
    }
}