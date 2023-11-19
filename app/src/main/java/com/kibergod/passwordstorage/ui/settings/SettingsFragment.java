package com.kibergod.passwordstorage.ui.settings;

import static com.kibergod.passwordstorage.model.DigitalOwner.DATA_DELETION_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.HIDE_MODE;
import static com.kibergod.passwordstorage.model.DigitalOwner.PROTECTED_MODE;
import static com.kibergod.passwordstorage.model.Settings.MAX_PASSWORD_LENGTH;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.TypedValue;
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
import com.kibergod.passwordstorage.data.SharedCategoriesDataViewModel;
import com.kibergod.passwordstorage.data.SharedDigitalOwnerViewModel;
import com.kibergod.passwordstorage.data.SharedRecordsDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.HomeViewModel;

import java.util.Calendar;

public class SettingsFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;
    private SharedDigitalOwnerViewModel sharedDigitalOwnerViewModel;
    private SharedCategoriesDataViewModel sharedCategoriesDataViewModel;
    private SharedRecordsDataViewModel sharedRecordsDataViewModel;

    private TextView textViewStatus;

    private final String DESCRIPTION_MOD1 = "Копії ваших файлів даних (записи та категорії) будуть приховані до моменту їх відновлення. Оригінали будуть замінені порожніми, тимчасовими файлами. Для відновлення прихованих даних необідно ввести ваш пароль входу у калькуляторі задом наперед. Після відновлення тимчасові дані будуть об`єднані з відновленними.";
    private final String DESCRIPTION_MOD2 = "У цьому режимі Цифровий власник через задану кількість днів заблокує можливість входити по стандартному паролю. Для входу буде необхідно ввести ваш пароль задом наперед. \n\nВідлік часу почнеться з поточної дати.";
    private final String DESCRIPTION_MOD3 = "У цьому режимі Цифровий власник через задану кількість днів після першого успішного заходу одразу знищить усі дані записів та категорій. Дані буде неможливо відновити. Видалення відбудеться до того, як хтось при вході зможе побачити дані. \n\nВідлік часу почнеться з поточної дати.";

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
        setOnClickToRadioButton(view, R.id.digitalOwnerMode1Flag, () -> showResetModeConfirmDialog(view, R.id.digitalOwnerMode1Flag, DESCRIPTION_MOD1, HIDE_MODE));
        setOnClickToRadioButton(view, R.id.digitalOwnerMode2Flag, () -> showResetModeConfirmDialog(view, R.id.digitalOwnerMode2Flag, DESCRIPTION_MOD2, PROTECTED_MODE));
        setOnClickToRadioButton(view, R.id.digitalOwnerMode3Flag, () -> showResetModeConfirmDialog(view, R.id.digitalOwnerMode3Flag, DESCRIPTION_MOD3, DATA_DELETION_MODE));
        setOnClickDefaultSettingsButton(view);
        setOnClickToSavePasswordButton(view);
        setOnChangeToCalendar(view);
        setColorToImg(view, R.id.imgVerticalKey, true);
        setOnClickToEditPassLayout(view);

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
                params = getParamsForValidLine(params, 0);
            } else {
                textViewStatus.setText("* пароль може складатися лише з символів [0-9], '.', '-', '+' ");
                params = getParamsForValidLine(params, 5);
            }
        } else {
            textViewStatus.setText("Пароль не може бути порожнім");
            params = getParamsForValidLine(params, 5);
        }
        editPasswordStatus.setLayoutParams(params);
    }

    private ViewGroup.MarginLayoutParams getParamsForValidLine(ViewGroup.MarginLayoutParams params, int dp) {
        params.topMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
        if (dp == 0) {
            params.height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    getResources().getDisplayMetrics()
            );
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        return params;
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
    private void showResetModeConfirmDialog(View rootView, int radioButtonId, String text, int mode) {
        RadioButton modeRadioButton = rootView.findViewById(radioButtonId);

        if (modeRadioButton.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setMessage(text);
            builder.setPositiveButton("Увімкнути", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sharedDigitalOwnerViewModel.setMode(mode);
                    if (sharedDigitalOwnerViewModel.isHideMode(mode)) {
                        sharedDigitalOwnerViewModel.hideData();
                        sharedCategoriesDataViewModel.dataDestroy();
                        sharedRecordsDataViewModel.dataDestroy();
                    }
                    printCalendarData(rootView);
                }
            });
            builder.setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sharedDigitalOwnerViewModel.setPassiveMode();
                    offDigitalOwnerMods(rootView);
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    sharedDigitalOwnerViewModel.setPassiveMode();
                    offDigitalOwnerMods(rootView);
                }
            });
            builder.show();
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

    // Встановлення логіки поведінки поля редагування паролю
    private void setOnClickToEditPassLayout(View view) {
        LinearLayout editPasswordLayoutHead = view.findViewById(R.id.editPasswordLayoutHead);
        LinearLayout editPasswordLayoutBody = view.findViewById(R.id.editPasswordLayoutBody);
        editPasswordLayoutBody.setVisibility(View.GONE);
        editPasswordLayoutHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPasswordLayoutBody.getVisibility() == View.VISIBLE) {
                    editPasswordLayoutBody.setVisibility(View.GONE);
                } else {
                    editPasswordLayoutBody.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}