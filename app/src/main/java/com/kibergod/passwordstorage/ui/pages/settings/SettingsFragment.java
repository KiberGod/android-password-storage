package com.kibergod.passwordstorage.ui.pages.settings;

import static com.kibergod.passwordstorage.model.DigitalOwner.DATA_DELETION_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.HIDE_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.PROTECTED_MODE;
import static com.kibergod.passwordstorage.model.Settings.MAX_PASSWORD_LENGTH;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedDigitalOwnerViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.KeyboardUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedDigitalOwnerViewModel sharedDigitalOwnerViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private TextView textViewStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        sharedDigitalOwnerViewModel = new ViewModelProvider(requireActivity()).get(SharedDigitalOwnerViewModel.class);
        sharedRecordsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedRecordsDataViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        textViewStatus = view.findViewById(R.id.editPasswordStatus);

        homeViewModel.setMaxLengthForInput(view, R.id.inputPassword, MAX_PASSWORD_LENGTH);

        printSettingsData(view);
        setOnClickToSwitch(view, R.id.activityProtectionFlag, () -> sharedSettingsDataViewModel.editActivityProtection());
        setOnClickToSwitch(view, R.id.inputCalcClearingFlag, () -> sharedSettingsDataViewModel.editInputCalcClearing());
        setOnClickToSwitch(view, R.id.digitalOwnerFlag, () -> editDigitalOwnerSetting(view));
        setOnClickToRadioButton(view, R.id.digitalOwnerMode1Flag, () -> showResetModeConfirmDialog(view, R.id.digitalOwnerMode1Flag, HIDE_MODE));
        setOnClickToRadioButton(view, R.id.digitalOwnerMode2Flag, () -> showResetModeConfirmDialog(view, R.id.digitalOwnerMode2Flag, PROTECTED_MODE));
        setOnClickToRadioButton(view, R.id.digitalOwnerMode3Flag, () -> showResetModeConfirmDialog(view, R.id.digitalOwnerMode3Flag, DATA_DELETION_MODE));
        setOnClickDefaultSettingsButton(view);
        setOnClickToSavePasswordButton(view);
        setOnClickToEditFontsButton(view);
        setOnChangeToCalendar(view);
        ImageUtils.setColorToImg(requireContext(), view, R.id.imgVerticalKey, R.color.purple);
        ViewUtils.setOnClickToDropdownView(view, R.id.editPasswordLayoutHead, R.id.editPasswordLayoutBody);
        ViewUtils.setOnClickToDropdownView(view, R.id.editFontsLayoutHead, R.id.editFontsLayoutBody);
        setRssDialogs(view);
        resizeFonts(view);
        return view;
    }

    private void resizeFonts(View view) {
        int fontSizeMain = sharedSettingsDataViewModel.getFontSizeMain();
        int fontSizeInput = sharedSettingsDataViewModel.getFontSizeInput();
        int fontSizeLargeButtons = sharedSettingsDataViewModel.getFontSizeLargeButtons();
        int fontSizeFieldCaptions = sharedSettingsDataViewModel.getFontSizeFieldCaptions();

        FontUtils.setFontSizeToView(requireContext(), view, R.id.pageTitle, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editPassText, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontsText, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.activityProtectionFlag, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.inputCalcClearingFlag, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.digitalOwnerFlag, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.textView5, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.digitalOwnerMode1Flag, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.digitalOwnerMode2Flag, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.digitalOwnerMode3Flag, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.textView6, fontSizeMain);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.inputPassword, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeMain, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeInput, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeButtons, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeLargeButtons, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeFieldCaptions, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeRssMain, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeRssSecondary, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.editFontSizeOther, fontSizeInput);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.editPasswordStatus, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeMainCaption, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeInputCaption, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeButtonsCaption, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeLargeButtonsCaption, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeCaptionsCaption, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeRssMainCaption, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeRssSecondaryCaption, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.fontSizeOtherCaption, fontSizeFieldCaptions);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.savePasswordButton, fontSizeLargeButtons);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.saveFontsButton, fontSizeLargeButtons);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.setDefaultSettingsButton, fontSizeLargeButtons);
    }

    private void setRssDialogs(View view) {
        RabbitSupport.setRabbitSupportDialogToIconByClick(view, R.id.imgVerticalKey, RabbitSupport.SupportDialogIDs.SETTINGS_LOGIN_PASSWORD, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        RabbitSupport.setRabbitSupportDialogToIconByClick(view, R.id.imgPhoneLock, RabbitSupport.SupportDialogIDs.SETTINGS_SESSION_PROTECT, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        RabbitSupport.setRabbitSupportDialogToIconByClick(view, R.id.imgEraser, RabbitSupport.SupportDialogIDs.SETTINGS_CALC_CLEARING, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        RabbitSupport.setRabbitSupportDialogToIconByClick(view, R.id.imgRunningRabbit, RabbitSupport.SupportDialogIDs.DIGITAL_OWNER_GENERAL_INFO, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
    }

    // Функція виводить дані налаштуваннь на екран
    private void printSettingsData(View view) {
        Switch activityProtectionSwitch = view.findViewById(R.id.activityProtectionFlag);
        activityProtectionSwitch.setChecked(sharedSettingsDataViewModel.getActivityProtection());
        ImageUtils.setColorToImg(requireContext(), view, R.id.imgPhoneLock, getColor(activityProtectionSwitch.isChecked()));

        Switch inputPassClearingSwitch = view.findViewById(R.id.inputCalcClearingFlag);
        inputPassClearingSwitch.setChecked(sharedSettingsDataViewModel.getInputCalcClearing());
        ImageUtils.setColorToImg(requireContext(), view, R.id.imgEraser, getColor(inputPassClearingSwitch.isChecked()));

        Switch digitalOwnerSwitch = view.findViewById(R.id.digitalOwnerFlag);
        digitalOwnerSwitch.setChecked(sharedSettingsDataViewModel.getDigitalOwner());
        showOrHideDigitalOwnerSettings(view);
        ImageUtils.setColorToImg(requireContext(), view, R.id.imgRunningRabbit, getColor(digitalOwnerSwitch.isChecked()));

        EditText inputPassword = view.findViewById(R.id.inputPassword);
        inputPassword.setText(sharedSettingsDataViewModel.getPassword());

        printCalendarData(view);

        printDigitalOwnerMod(view, R.id.digitalOwnerMode1Flag, HIDE_MODE);
        printDigitalOwnerMod(view, R.id.digitalOwnerMode2Flag, PROTECTED_MODE);
        printDigitalOwnerMod(view, R.id.digitalOwnerMode3Flag, DATA_DELETION_MODE);

        printFontSizes(view);
    }

    private void printFontSizes(View view) {
        setTextToEditText(view, R.id.editFontSizeMain, sharedSettingsDataViewModel.getFontSizeMain());
        setTextToEditText(view, R.id.editFontSizeInput, sharedSettingsDataViewModel.getFontSizeInput());
        setTextToEditText(view, R.id.editFontSizeButtons, sharedSettingsDataViewModel.getFontSizeButtons());
        setTextToEditText(view, R.id.editFontSizeLargeButtons, sharedSettingsDataViewModel.getFontSizeLargeButtons());
        setTextToEditText(view, R.id.editFontSizeFieldCaptions, sharedSettingsDataViewModel.getFontSizeFieldCaptions());
        setTextToEditText(view, R.id.editFontSizeRssMain, sharedSettingsDataViewModel.getFontSizeRssMain());
        setTextToEditText(view, R.id.editFontSizeRssSecondary, sharedSettingsDataViewModel.getFontSizeRssSecondary());
        setTextToEditText(view, R.id.editFontSizeOther, sharedSettingsDataViewModel.getFontSizeOther());
    }

    private void printCalendarData(View view) {
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.clearFocus();
        calendarView.setDate(sharedDigitalOwnerViewModel.getDateMilliseconds());
    }

    private void printDigitalOwnerMod(View view, int id, int mode) {
        RadioButton digitalOwnerMode = view.findViewById(id);
        digitalOwnerMode.setChecked(sharedDigitalOwnerViewModel.getModeFlag(mode));
    }

    // Встановлює обробник натискань на перемикач налаштування
    private void setOnClickToSwitch(View view, int switch_id, Runnable onClickRunnable) {
        Switch settingSwitch = view.findViewById(switch_id);

        settingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRunnable.run();
                KeyboardUtils.hideKeyboards(requireContext());

                if (switch_id == R.id.activityProtectionFlag) {
                    ImageUtils.setColorToImg(requireContext(), view, R.id.imgPhoneLock, getColor(settingSwitch.isChecked()));
                } else if (switch_id == R.id.inputCalcClearingFlag) {
                    ImageUtils.setColorToImg(requireContext(), view, R.id.imgEraser, getColor(settingSwitch.isChecked()));
                } else if (switch_id == R.id.digitalOwnerFlag) {
                    ImageUtils.setColorToImg(requireContext(), view, R.id.imgRunningRabbit, getColor(settingSwitch.isChecked()));
                }
            }
        });
    }

    private void setOnClickToRadioButton(View view, int radioButtonId, Runnable onClickRunnable) {
        ViewUtils.setOnClickToView(view, radioButtonId, () -> {
            onClickRunnable.run();
            KeyboardUtils.hideKeyboards(requireContext());
        });
    }

    // Встановлює обробник натискання на скид налаштуваннь до стандартних
    private void setOnClickDefaultSettingsButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.setDefaultSettingsButton, () -> {
            KeyboardUtils.hideKeyboards(requireContext());
            sharedSettingsDataViewModel.setDefaultSettings();
            printSettingsData(view);
            resizeFonts(view);
            setRssDialogs(view);
        });
    }

    // Функція встановлює подію натискання кнопки збереження введених змін (для пароля)
    private void setOnClickToSavePasswordButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.savePasswordButton, () -> {
            KeyboardUtils.hideKeyboards(requireContext());
            getEditPassword(view);
        });
    }

    // Обробка редагування пароля
    private void getEditPassword(View view) {
        EditText inputPassword = view.findViewById(R.id.inputPassword);
        String newPassword = inputPassword.getText().toString();

        TextView editPasswordStatus = view.findViewById(R.id.editPasswordStatus);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) editPasswordStatus.getLayoutParams();
        if (newPassword.length() != 0) {
            if (sharedSettingsDataViewModel.passwordValidation(newPassword)) {
                textViewStatus.setText("");
                sharedSettingsDataViewModel.editPassword(newPassword);
                Toast.makeText(getActivity(), "Пароль успішно змінено", Toast.LENGTH_SHORT).show();
                params = homeViewModel.getParamsForValidLine(requireContext(), params, 0);
            } else {
                textViewStatus.setText("* пароль може складатися лише з символів [0-9], '.', '-', '+' ");
                params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
            }
        } else {
            textViewStatus.setText("Пароль не може бути порожнім");
            params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
        }
        editPasswordStatus.setLayoutParams(params);
    }

    // Логіка переключення налаштування DigitalOwner
    private void editDigitalOwnerSetting(View view) {
        sharedSettingsDataViewModel.editDigitalOwner();
        showOrHideDigitalOwnerSettings(view);
    }

    // Автоприховання налаштувань Цифрового власника
    private void showOrHideDigitalOwnerSettings(View view) {
        LinearLayout digitalOwnerSettingsLayout = view.findViewById(R.id.digitalOwnerSettings);
        if (sharedSettingsDataViewModel.getDigitalOwner()) {
            digitalOwnerSettingsLayout.setVisibility(View.VISIBLE);
        } else {
            digitalOwnerSettingsLayout.setVisibility(View.GONE);
        }
    }

    // Вікно з підтвердженням зміни режиму
    private void showResetModeConfirmDialog(View rootView, int radioButtonId, int mode) {
        RadioButton modeRadioButton = rootView.findViewById(radioButtonId);

        View blurView = rootView.findViewById(R.id.blurView);
        DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                blurView.setVisibility(View.GONE);
                sharedDigitalOwnerViewModel.setPassiveMode();
                offDigitalOwnerMods(rootView);
            }
        };

        if (modeRadioButton.isChecked()) {
            RabbitSupport.SupportDialogIDs ID = RabbitSupport.SupportDialogIDs.DIGITAL_OWNER_HIDE_MODE;
            switch (mode) {
                case PROTECTED_MODE:
                    ID = RabbitSupport.SupportDialogIDs.DIGITAL_OWNER_PROTECTED_MODE;
                    break;
                case DATA_DELETION_MODE:
                    ID = RabbitSupport.SupportDialogIDs.DIGITAL_OWNER_DELETION_MODE;
                    break;
            }
            Dialog infoDialog = RabbitSupport.getRabbitSupportDialog(requireContext(), ID, rootView, true, sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());

            ViewUtils.setOnClickToDialog(infoDialog, R.id.positiveButton, () -> {
                sharedDigitalOwnerViewModel.setMode(mode);
                if (sharedDigitalOwnerViewModel.isHideMode(mode)) {
                    sharedRecordsDataViewModel.hideAllRecords();
                }
                printCalendarData(rootView);
                infoDialog.setOnDismissListener(null);
                infoDialog.cancel();
                blurView.setVisibility(View.GONE);
                infoDialog.setOnDismissListener(dismissListener);
            });

            ViewUtils.setOnClickToDialog(infoDialog, R.id.negativeButton, () -> {
                sharedDigitalOwnerViewModel.setPassiveMode();
                offDigitalOwnerMods(rootView);
                infoDialog.setOnDismissListener(null);
                infoDialog.cancel();
                blurView.setVisibility(View.GONE);
                infoDialog.setOnDismissListener(dismissListener);
            });

            infoDialog.setOnDismissListener(dismissListener);
            infoDialog.show();
        } else {
            sharedDigitalOwnerViewModel.setPassiveMode();
        }
    }

    // Вимикає всі режими "Цифрового власника"
    private void offDigitalOwnerMods(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.digitalOwnerModsRGroup);
        radioGroup.clearCheck();
    }

    // Функція встановлює подію натискання кнопки збереження кількості днів для спрацювання Цифрового власника
    private void setOnChangeToCalendar(View rootView) {
        CalendarView calendarView = rootView.findViewById(R.id.calendarView);

        Calendar calendar = Calendar.getInstance();

        calendarView.setMinDate(calendar.getTimeInMillis());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                KeyboardUtils.hideKeyboards(requireContext());
                sharedDigitalOwnerViewModel.editDate(dayOfMonth, month, year);
                Toast.makeText(getActivity(), "Точка спрацювання встановлена", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Ресетер кольору іконок
    private int getColor(boolean mode) {
        return mode ? R.color.purple : R.color.gray_text;
    }

    private void setTextToEditText(View view, int editTextId, int fontSize) {
        EditText textView = view.findViewById(editTextId);
        textView.setText(Integer.toString(fontSize));
    }

    private int getFontSize(View view, int editTextId) {
        EditText editText = view.findViewById(editTextId);
        String inputText = editText.getText().toString().trim();

        if (!inputText.isEmpty()) {
            try {
                return Integer.parseInt(inputText);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    private void setOnClickToEditFontsButton(View view) {
        ViewUtils.setOnClickToView(view, R.id.saveFontsButton, () -> {
            KeyboardUtils.hideKeyboards(requireContext());

            TextView validStatusView = view.findViewById(R.id.editFontSizeStatus);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) validStatusView.getLayoutParams();

            boolean[] validStatus = {
                    sharedSettingsDataViewModel.setFontSizeMain(getFontSize(view, R.id.editFontSizeMain)),
                    sharedSettingsDataViewModel.setFontSizeInput(getFontSize(view, R.id.editFontSizeInput)),
                    sharedSettingsDataViewModel.setFontSizeButtons(getFontSize(view, R.id.editFontSizeButtons)),
                    sharedSettingsDataViewModel.setFontSizeLargeButtons(getFontSize(view, R.id.editFontSizeLargeButtons)),
                    sharedSettingsDataViewModel.setFontSizeFieldCaptions(getFontSize(view, R.id.editFontSizeFieldCaptions)),
                    sharedSettingsDataViewModel.setFontSizeRssMain(getFontSize(view, R.id.editFontSizeRssMain)),
                    sharedSettingsDataViewModel.setFontSizeRssSecondary(getFontSize(view, R.id.editFontSizeRssSecondary)),
                    sharedSettingsDataViewModel.setFontSizeOther(getFontSize(view, R.id.editFontSizeOther)),
            };

            validStatusView.setText("");
            params = homeViewModel.getParamsForValidLine(requireContext(), params, 0);
            for (boolean status : validStatus) {
                if (!status) {
                    validStatusView.setText("Один або декілька шрифтів не були змінені, бо мають значення менше " +
                            sharedSettingsDataViewModel.getMinFontSize() + " або більше " +
                            sharedSettingsDataViewModel.getMaxFontSize() + "." +
                            "" +
                            "");
                    params = homeViewModel.getParamsForValidLine(requireContext(), params, 5);
                }
            }
            validStatusView.setLayoutParams(params);
            printFontSizes(view);
            resizeFonts(view);
            setRssDialogs(view);
            Toast.makeText(getActivity(), "Розміри шрифтів було оновлено", Toast.LENGTH_SHORT).show();
        });
    }
}