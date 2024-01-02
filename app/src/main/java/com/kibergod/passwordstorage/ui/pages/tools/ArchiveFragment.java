package com.kibergod.passwordstorage.ui.pages.tools;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.model.Record;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment {

    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;

    private boolean rabbitFounderMode = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel.runAutoRemoveRecordsFromArchive();
        drawButtonList(view);
        setOnClickToClearArchiveButton(view);
        return view;
    }

    // Функція виводить весь список записів
    private void drawButtonList(View view) {
        ArrayList<Record> records = sharedRecordsDataViewModel.getRecords();
        int counter = 0;
        for (int i=0; i<records.size(); i++) {
            String icon_id;
            final int id = records.get(i).getId();

            if (sharedRecordsDataViewModel.isDeletedRecordById(id)) {
                if (sharedRecordsDataViewModel.needSetCategoryIconById(id)) {
                    icon_id = sharedCategoriesDataViewModel.getCategoryIconIdById(
                            sharedRecordsDataViewModel.getRecordCategory_idById(id)
                    );
                } else {
                    icon_id = sharedRecordsDataViewModel.getRecordIconIdById(id);
                }

                counter++;

                ((HomeActivity) requireActivity()).drawButton(
                        view,
                        requireContext(),
                        sharedRecordsDataViewModel.getRecordTitleById(id),
                        R.id.recordsScrollArea,
                        icon_id,
                        sharedRecordsDataViewModel.getRecordDeleted_atById(id),
                        R.drawable.vector__trash,
                        sharedRecordsDataViewModel.getRecordTimeToRemoveById(id),
                        () -> ((HomeActivity) requireActivity()).setShowRecordFragment(id)
                );
            }
        }

        if (counter == 0) {
            setRabbitImg(view);
        }
        setArchiveRecordCounter(view, counter);
    }

    // Натиск на кнопку очищення всього архіва
    private void setOnClickToClearArchiveButton(View view) {
        LinearLayout button = view.findViewById(R.id.clearAllArchiveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Ви впевнені, що хочете очистити архів? Всі архівовані записи будуть видалені. Цю дію буде неможливо скасувати.");
                builder.setPositiveButton("Очистити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedRecordsDataViewModel.removeAllRecordsFromArchive();
                        Toast.makeText(getActivity(), "Архів очищено", Toast.LENGTH_SHORT).show();
                        ((HomeActivity) requireActivity()).setArchiveFragment();
                    }
                });
                builder.setNegativeButton("Відмінити", null);
                builder.show();
            }
        });
    }

    // Виведення зображення кролика
    private void setRabbitImg(View view) {
        LinearLayout scrollArea = view.findViewById(R.id.recordsScrollArea);
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_not_found_page, null);
        scrollArea.addView(fragmentView);
        TextView notFoundMessageView = view.findViewById(R.id.notFoundMessage);
        notFoundMessageView.setText("Архівованих записів не знайдено.");

        ((HomeActivity) requireActivity()).setOnLongClickToRabbitImg(view, action -> {
            rabbitFounderMode = !rabbitFounderMode;
            return rabbitFounderMode; }
        );
    }

    // Встановлення кількості знайдених архівованих западів
    private void setArchiveRecordCounter(View view, int counter) {
        TextView textView = view.findViewById(R.id.archiveRecordCounter);
        textView.setText("Знайдено архівованих записів: " + counter);
    }
}