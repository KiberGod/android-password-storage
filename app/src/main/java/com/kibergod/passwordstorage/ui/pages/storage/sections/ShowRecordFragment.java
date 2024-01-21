package com.kibergod.passwordstorage.ui.pages.storage.sections;

import static com.kibergod.passwordstorage.model.Record.MAX_FIELDS_LENGTH;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;
import com.kibergod.passwordstorage.ui.tools.CategorySelectionDialog;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.tools.ToolbarBuilder;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.Vibrator;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.ArrayList;

public class ShowRecordFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private static final String RECORD_ID = "record_id";

    private int recordId;

    private ArrayList<TextView> protectedTextViews;
    private ArrayList<Integer> protectedTextViewsIds;

    public ShowRecordFragment() {
        // Required empty public constructor
    }

    public static ShowRecordFragment newInstance(int recordId) {
        ShowRecordFragment fragment = new ShowRecordFragment();
        Bundle args = new Bundle();
        args.putInt(RECORD_ID, recordId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recordId = getArguments().getInt(RECORD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_record, container, false);

        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        protectedTextViews = new ArrayList<>();
        protectedTextViewsIds = new ArrayList<>();

        setToolbar(view);

        printRecordData(view);
        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.metadataHead, RabbitSupport.SupportDialogIDs.STORAGE_METADATA, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        ViewUtils.setOnClickToDropdownView(view, R.id.metadataHead, R.id.metadataBody, null, () -> ViewUtils.setScrollToBottom(view, R.id.scrollView));
        sharedRecordsDataViewModel.updateRecordViewed_atById(recordId, sharedSettingsDataViewModel.getFiltersSortParam(), sharedSettingsDataViewModel.getFiltersSortMode());
        ImageUtils.setImageViewSize(view, R.id.recordIcon, ImageUtils.getScreenWidth()/3);
        resizeFonts(view);
        return view;
    }

    private void resizeFonts(View view) {
        int fontSizeMain = sharedSettingsDataViewModel.getFontSizeMain();
        int fontSizeFieldCaptions = sharedSettingsDataViewModel.getFontSizeFieldCaptions();
        int fontSizeOther = sharedSettingsDataViewModel.getFontSizeOther();

        FontUtils.setFontSizeToView(requireContext(), view, R.id.recordTitle, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.mainRecordText, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.categoryText, fontSizeMain);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.recordCategory, fontSizeOther);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.timeToRecordRemove, fontSizeFieldCaptions);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.metadataTitle, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.created_at, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.updated_at, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.viewed_at, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.deleted_at, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.created_atText, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.updated_atText, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.viewed_atText, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.deleted_atText, fontSizeFieldCaptions);
    }

    // Функція почергово викликає функцію для встановлення даних запису до UI-компонентів
    private void printRecordData(View view) {
        setTextViewText(view, R.id.recordTitle, sharedRecordsDataViewModel.getRecordTitleById(recordId));
        String category = sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idById(recordId));
        if (category.equals("")) {
            category = CategorySelectionDialog.getEmptyCategoryText();
        }
        setTextViewText(view, R.id.recordCategory, category);

        if (sharedRecordsDataViewModel.getRecordTextById(recordId).equals("")) {
            LinearLayout linearLayout = view.findViewById(R.id.recordMainTextLayout);
            ViewGroup parentLayout = (ViewGroup) linearLayout.getParent();
            parentLayout.removeView(linearLayout);
        } else {
            setTextViewText(view, R.id.mainRecordText, sharedRecordsDataViewModel.getRecordTextById(recordId));
        }

        if (!sharedCategoriesDataViewModel.getCategoryNameById(sharedRecordsDataViewModel.getRecordCategory_idById(recordId)).equals("")) {
            ImageView categoryIcon = view.findViewById(R.id.recordCategoryIcon);
            categoryIcon.setImageResource(getResources().getIdentifier(
                    sharedCategoriesDataViewModel.getCategoryIconIdById(sharedRecordsDataViewModel.getRecordCategory_idById(recordId)),
                    "drawable", requireContext().getPackageName())
            );
        }

        ToolbarBuilder.setBookmarkStatus(view, requireContext(), sharedRecordsDataViewModel.getBookmarkById(recordId));

        ImageView recordIcon = view.findViewById(R.id.recordIcon);
        recordIcon.setImageResource(getResources().getIdentifier(sharedRecordsDataViewModel.getRecordIconIdById(recordId), "drawable", requireContext().getPackageName()));

        for (int i=0; i<MAX_FIELDS_LENGTH; i++) {
            String fieldName = sharedRecordsDataViewModel.getRecordFieldNameById(recordId, i);
            String fieldValue = sharedRecordsDataViewModel.getRecordFieldProtectedValueById(recordId, i);
            if (!fieldName.equals("") || !fieldValue.equals("")) {
                createField(view, fieldName, fieldValue, i);
            }
        }

        TextView recordCreated_at = view.findViewById(R.id.created_at);
        recordCreated_at.setText(sharedRecordsDataViewModel.getRecordCreated_atById(recordId));

        TextView recordUpdated_at = view.findViewById(R.id.updated_at);
        recordUpdated_at.setText(sharedRecordsDataViewModel.getRecordUpdated_atById(recordId));

        TextView recordViewed_at = view.findViewById(R.id.viewed_at);
        recordViewed_at.setText(sharedRecordsDataViewModel.getRecordViewed_atById(recordId));

        updateProtectedTextViews(view, sharedRecordsDataViewModel.getRecordToTalValueVisibilityById(recordId));

        if (sharedRecordsDataViewModel.isDeletedRecordById(recordId)) {
            LinearLayout deleted_atLayout = view.findViewById(R.id.deleted_atLayout);
            deleted_atLayout.setVisibility(View.VISIBLE);

            TextView recordDeleted_at = view.findViewById(R.id.deleted_at);
            recordDeleted_at.setText(sharedRecordsDataViewModel.getRecordDeleted_atById(recordId));

            LinearLayout archiveTitleLayout = view.findViewById(R.id.archiveTitleLayout);
            archiveTitleLayout.setVisibility(View.VISIBLE);
        }
    }

    // Функція встановлення тексту до UI-компонентів
    private void setTextViewText(@NonNull View view, int textViewId, String text) {
        TextView textView = view.findViewById(textViewId);
        textView.setText(text);
    }

    // Створює поле запису
    private void createField(View view, String fieldName, String fieldValue, int fieldIndex) {
        LinearLayout parentContainer = view.findViewById(R.id.mainContainer);
        View fieldView = getLayoutInflater().inflate(R.layout.fragment_show_field, null);

        FontUtils.setFontSizeToView(requireContext(), fieldView, R.id.titleField, sharedSettingsDataViewModel.getFontSizeInput());
        FontUtils.setFontSizeToView(requireContext(), fieldView, R.id.valueField, sharedSettingsDataViewModel.getFontSizeInput());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(20), 0, 0);

        TextView textViewName = fieldView.findViewById(R.id.titleField);
        TextView textViewValue = fieldView.findViewById(R.id.valueField);

        textViewName.setText(fieldName);
        textViewValue.setText(fieldValue);

        ImageView imageView = fieldView.findViewById(R.id.hideButtonIcon);
        imageView.setId(View.generateViewId());
        textViewValue.setId(View.generateViewId());
        protectedTextViews.add(textViewValue);
        protectedTextViewsIds.add(fieldIndex);

        updateFieldValueBlock(textViewValue, imageView, fieldIndex);
        setOnClickToHideValueButton(fieldView, imageView.getId(), fieldIndex, textViewValue.getId());
        setOnClickToCopyValueButton(fieldView, sharedRecordsDataViewModel.getRecordFieldValueById(recordId, fieldIndex));

        LinearLayout placeForFields = view.findViewById(R.id.placeForFields);
        parentContainer.addView(fieldView, parentContainer.indexOfChild(placeForFields), layoutParams);
    }



    // Встановлення події натиснення на кнопку приховання значення поля
    private void setOnClickToHideValueButton(View fieldView, int imageId, int fieldIndex, int textViewValueId) {
        ImageView imageView = fieldView.findViewById(imageId);
        TextView textViewValue = fieldView.findViewById(textViewValueId);
        ViewUtils.setOnClickToView(fieldView, R.id.hideValueButton, () -> {
            sharedRecordsDataViewModel.setRecordFieldValueVisibilityById(recordId, fieldIndex);
            updateFieldValueBlock(textViewValue, imageView, fieldIndex);
        });
    }

    // Оновлення блоку значення поля
    private void updateFieldValueBlock(TextView textViewValue, ImageView imageView, int fieldIndex) {
        if (sharedRecordsDataViewModel.getRecordFieldValueVisibilityById(recordId, fieldIndex)) {
            imageView.setImageResource(R.drawable.vector__open_eye);
            if (!sharedRecordsDataViewModel.getRecordToTalValueVisibilityById(recordId)) {
                textViewValue.setText(sharedRecordsDataViewModel.getRecordFieldValueById(recordId, fieldIndex));
            }
        } else {
            imageView.setImageResource(R.drawable.vector__close_eye);
            textViewValue.setText(sharedRecordsDataViewModel.getRecordFieldProtectedValueById(recordId, fieldIndex));
        }
    }

    // Встановлення події натиснення на кнопку копіювання значення поля
    private void setOnClickToCopyValueButton(View fieldView, String value) {
        ViewUtils.setOnClickToView(fieldView, R.id.copyButton, () -> {
            Vibrator.vibrate(requireContext());

            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("label", value);
                clipboard.setPrimaryClip(clip);
            }

            Toast.makeText(getActivity(), "Скопійовано у буфер обміну", Toast.LENGTH_SHORT).show();
        });
    }

    // Оновлення сторінки після переключення глобальної кнопки захисту перегляду
    private void updateProtectedTextViews(View view, boolean totalVisibilityValue) {
        if (totalVisibilityValue) {
            setParamsToProtectedTextView(view,
                    sharedRecordsDataViewModel.getRecordProtectedValue(
                            sharedRecordsDataViewModel.getRecordTextById(recordId)
                    ), R.color.purple);
        } else {
            setParamsToProtectedTextView(view, sharedRecordsDataViewModel.getRecordTextById(recordId), R.color.white);
        }
    }

    // Встановлення параметрів до полів, на яких розповсюджується захист перегляду
    private void setParamsToProtectedTextView(View view, String mainTextValue, int textColorId) {
        TextView mainText = view.findViewById(R.id.mainRecordText);
        if (mainText != null) {
            mainText.setText(mainTextValue);
            mainText.setTextColor(requireContext().getColor(textColorId));
        }

        for (int i=0; i<protectedTextViews.size(); i++) {
            protectedTextViews.get(i).setTextColor(requireContext().getColor(textColorId));

            protectedTextViews.get(i).setText(
                    sharedRecordsDataViewModel.getRecordFieldProtectedValueById(recordId, protectedTextViewsIds.get(i))
            );
        }
    }

    // Встановлення тулбару
    private void setToolbar(View view) {
        if (sharedRecordsDataViewModel.isDeletedRecordById(recordId)) {
            ToolbarBuilder.addToolbarToView(view, requireContext(), false,false, false, false,false,true,false,true);
            setOnClickToDeleteButton(view);
            setOnClickToRestoreButton(view);
            TextView timeToRemoveView = view.findViewById(R.id.timeToRecordRemove);
            timeToRemoveView.setText("Автовидалення через: " + sharedRecordsDataViewModel.getRecordTimeToRemoveById(recordId));
            timeToRemoveView.setVisibility(View.VISIBLE);
        } else {
            ToolbarBuilder.addToolbarToView(view, requireContext(), true,true, true, false,false,false,false,false);
            ToolbarBuilder.setOnClickToEditButton(view, () -> ((HomeActivity) requireActivity()).setEditRecordFragment(recordId));

            ToolbarBuilder.setOnClickToBookmark(view, requireContext(), () -> {
                sharedRecordsDataViewModel.editBookmarkInRecordById(recordId);
                Vibrator.vibrate(requireContext());
            });

            ToolbarBuilder.setOnClickToVisibilitySwitchButton(view,
                    res -> sharedRecordsDataViewModel.getRecordToTalValueVisibilityById(recordId),
                    () -> {
                        sharedRecordsDataViewModel.editToTalValueVisibilityInRecordById(recordId);
                        updateProtectedTextViews(view, sharedRecordsDataViewModel.getRecordToTalValueVisibilityById(recordId));
                    });
        }
    }

    // Функція встановлює подію натискання кнопки видалення запису
    private void setOnClickToDeleteButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.imgTrash, () -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Ви впевнені, що хочете видалити запис? Цю дію буде неможливо скасувати.");
            builder.setPositiveButton("Видалити", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Запис " + sharedRecordsDataViewModel.getRecordTitleById(recordId) + " видалено назавжди", Toast.LENGTH_SHORT).show();
                    sharedRecordsDataViewModel.deleteRecordFromArchive(recordId);
                    ((HomeActivity) requireActivity()).setArchiveFragment();
                }
            });
            builder.setNegativeButton("Відмінити", null);
            builder.show();
        });
    }

    // Функція встановлює подію натискання кнопки відновлення запису
    private void setOnClickToRestoreButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.imgRestore, () -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("Запис буде перенесено з архіву до сховища. Відновити?");
            builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Відновлено запис " + sharedRecordsDataViewModel.getRecordTitleById(recordId), Toast.LENGTH_SHORT).show();
                    sharedRecordsDataViewModel.restoreRecordFromArchive(recordId);
                    ((HomeActivity) requireActivity()).setArchiveFragment();
                }
            });
            builder.setNegativeButton("Ні", null);
            builder.show();
        });
    }
}