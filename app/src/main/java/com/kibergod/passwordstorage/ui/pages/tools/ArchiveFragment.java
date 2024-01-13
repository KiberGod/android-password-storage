package com.kibergod.passwordstorage.ui.pages.tools;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.model.Record;
import com.kibergod.passwordstorage.ui.components.NotFoundPage;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment {
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);

        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel.runAutoRemoveRecordsFromArchive();
        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.archiveTitle, RabbitSupport.SupportDialogIDs.TOOLS_ARCHIVE, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
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
            NotFoundPage.printNotFoundPage(requireContext(), view, R.id.recordsScrollArea);
        }
        setArchiveRecordCounter(view, counter);
    }

    // Натиск на кнопку очищення всього архіва
    private void setOnClickToClearArchiveButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.clearAllArchiveButton, () -> {
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
        });
    }

    // Встановлення кількості знайдених архівованих западів
    private void setArchiveRecordCounter(View view, int counter) {
        TextView textView = view.findViewById(R.id.archiveRecordCounter);
        textView.setText("Знайдено архівованих записів: " + counter);
    }
}