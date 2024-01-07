package com.kibergod.passwordstorage.ui.pages.settings;

import static com.kibergod.passwordstorage.model.DigitalOwner.DATA_DELETION_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.HIDE_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.PROTECTED_MODE;
import static com.kibergod.passwordstorage.model.Settings.MAX_PASSWORD_LENGTH;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedDigitalOwnerViewModel sharedDigitalOwnerViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private TextView textViewStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        sharedDigitalOwnerViewModel = new ViewModelProvider(requireActivity()).get(SharedDigitalOwnerViewModel.class);
        sharedCategoriesDataViewModel = new ViewModelProvider(requireActivity()).get(SharedCategoriesDataViewModel.class);
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
        setOnChangeToCalendar(view);
        ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.imgVerticalKey, R.color.purple);
        ((HomeActivity) requireActivity()).setOnClickToDropdownLayout(view, R.id.editPasswordLayoutHead, R.id.editPasswordLayoutBody, false);
        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIconByClick(view, R.id.imgVerticalKey, RabbitSupport.SupportDialogIDs.SETTINGS_LOGIN_PASSWORD, requireContext());
        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIconByClick(view, R.id.imgPhoneLock, RabbitSupport.SupportDialogIDs.SETTINGS_SESSION_PROTECT, requireContext());
        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIconByClick(view, R.id.imgEraser, RabbitSupport.SupportDialogIDs.SETTINGS_CALC_CLEARING, requireContext());
        ((HomeActivity) requireActivity()).setRabbitSupportDialogToIconByClick(view, R.id.imgRunningRabbit, RabbitSupport.SupportDialogIDs.DIGITAL_OWNER_GENERAL_INFO, requireContext());
        return view;
    }

    // Встановлення кольору іконки налаштування
    private void setColorToImg(View view, int imageId, boolean mode) {
        int colorId = R.color.gray_text;
        if (mode) {
            colorId = R.color.purple;
        }
        ImageView imageView = view.findViewById(imageId);
        imageView.setColorFilter(ContextCompat.getColor(requireContext(), colorId), PorterDuff.Mode.SRC_IN);
    }

    // Функція виводить дані налаштуваннь на екран
    private void printSettingsData(View view) {
        Switch activityProtectionSwitch = view.findViewById(R.id.activityProtectionFlag);
        activityProtectionSwitch.setChecked(sharedSettingsDataViewModel.getActivityProtection());
        setColorToImg(view, R.id.imgPhoneLock, activityProtectionSwitch.isChecked());

        Switch inputPassClearingSwitch = view.findViewById(R.id.inputCalcClearingFlag);
        inputPassClearingSwitch.setChecked(sharedSettingsDataViewModel.getInputCalcClearing());
        setColorToImg(view, R.id.imgEraser, inputPassClearingSwitch.isChecked());

        Switch digitalOwnerSwitch = view.findViewById(R.id.digitalOwnerFlag);
        digitalOwnerSwitch.setChecked(sharedSettingsDataViewModel.getDigitalOwner());
        showOrHideDigitalOwnerSettings(view);
        setColorToImg(view, R.id.imgRunningRabbit, digitalOwnerSwitch.isChecked());

        EditText inputPassword = view.findViewById(R.id.inputPassword);
        inputPassword.setText(sharedSettingsDataViewModel.getPassword());

        printCalendarData(view);

        printDigitalOwnerMod(view, R.id.digitalOwnerMode1Flag, HIDE_MODE);
        printDigitalOwnerMod(view, R.id.digitalOwnerMode2Flag, PROTECTED_MODE);
        printDigitalOwnerMod(view, R.id.digitalOwnerMode3Flag, DATA_DELETION_MODE);
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
                hideAllKeyBoards(view);

                if (switch_id == R.id.activityProtectionFlag) {
                    setColorToImg(view, R.id.imgPhoneLock, settingSwitch.isChecked());
                } else if (switch_id == R.id.inputCalcClearingFlag) {
                    setColorToImg(view, R.id.imgEraser, settingSwitch.isChecked());
                } else if (switch_id == R.id.digitalOwnerFlag) {
                    setColorToImg(view, R.id.imgRunningRabbit, settingSwitch.isChecked());
                }
            }
        });
    }

    private void setOnClickToRadioButton(View view, int radioButtonId, Runnable onClickRunnable) {
        RadioButton settingsRadioButton = view.findViewById(radioButtonId);

        settingsRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRunnable.run();
                hideAllKeyBoards(view);
            }
        });
    }

    // Встановлює обробник натискання на скид налаштуваннь до стандартних
    private void setOnClickDefaultSettingsButton(View view) {
        Button button = view.findViewById(R.id.setDefaultSettingsButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllKeyBoards(view);
                sharedSettingsDataViewModel.setDefaultSettings();
                printSettingsData(view);
            }
        });
    }

    // Функція встановлює подію натискання кнопки збереження введених змін (для пароля)
    private void setOnClickToSavePasswordButton(View view) {
        Button button = view.findViewById(R.id.savePasswordButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllKeyBoards(view);
                getEditPassword(view);
            }
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
            Dialog infoDialog = RabbitSupport.getRabbitSupportDialog(requireContext(), ID, rootView, true);


            Button positiveButton = infoDialog.findViewById(R.id.positiveButton);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedDigitalOwnerViewModel.setMode(mode);
                    if (sharedDigitalOwnerViewModel.isHideMode(mode)) {
                        sharedRecordsDataViewModel.hideAllRecords();
                    }
                    printCalendarData(rootView);
                    infoDialog.setOnDismissListener(null);
                    infoDialog.cancel();
                    blurView.setVisibility(View.GONE);
                    infoDialog.setOnDismissListener(dismissListener);
                }
            });

            Button negativeButton = infoDialog.findViewById(R.id.negativeButton);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedDigitalOwnerViewModel.setPassiveMode();
                    offDigitalOwnerMods(rootView);
                    infoDialog.setOnDismissListener(null);
                    infoDialog.cancel();
                    blurView.setVisibility(View.GONE);
                    infoDialog.setOnDismissListener(dismissListener);
                }
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
                hideAllKeyBoards(rootView);
                sharedDigitalOwnerViewModel.editDate(dayOfMonth, month, year);
                Toast.makeText(getActivity(), "Точка спрацювання встановлена", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Функція приховання клавіатур
    private void hideAllKeyBoards(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        hideKeyBoard(inputMethodManager, view, R.id.inputPassword);
    }

    // Приховання клавіатури для конкретного поля
    private void hideKeyBoard(InputMethodManager inputMethodManager, View view, int id) {
        EditText editText = view.findViewById(id);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}