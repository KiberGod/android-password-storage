package com.kibergod.passwordstorage.ui.pages.tools;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;

public class PasswordGeneratorFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;

    public class SymbolSetSettings {
        private Switch usageSwitch;
        private Switch randomLengthSwitch;
        private EditText lengthEdit;
        private TextView signItemLen;

        private TextWatcher watcher;

        public SymbolSetSettings(View view, int index, String symbolSetName, String sign, int idSignBarItem, int idSignItemLen) {
            signItemLen = view.findViewById(idSignItemLen);

            printSymbolSetBlock(view, symbolSetName, sign);

            setOnCheckedToUsageSwitch(view, index, idSignBarItem);
            setOnCheckedToRandomLengthSwitch(view, index);
            homeViewModel.setMaxLengthForInput(this.lengthEdit, 4);
            setOnChangedToEditText(index);
            setOnFocusToEditText();
            ((HomeActivity) requireActivity()).setColorToImg(requireContext(), view, R.id.generatorButtonIcon, R.color.white);
        }

        private int getEditTextId() {
            return lengthEdit.getId();
        }

        private void setOnCheckedToUsageSwitch(View view, int index, int idSignBarItem) {
            ConstraintLayout signItemLayout = view.findViewById(idSignBarItem);
            usageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    hideAllKeyBoards(view);
                    sharedGeneratorDataViewModel.editSymbolSetUsageByIndex(index, usageSwitch.isChecked(), requireContext());
                    updateSignBar(signItemLayout, isChecked);
                }
            });
        }

        private void updateSignBar(ConstraintLayout signItemLayout, boolean usageFlag) {
            if (usageFlag) {
                signItemLayout.setVisibility(View.VISIBLE);
            } else {
                signItemLayout.setVisibility(View.GONE);
            }
        }

        private void setOnCheckedToRandomLengthSwitch(View view, int index) {
            randomLengthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    hideAllKeyBoards(view);
                    sharedGeneratorDataViewModel.editSymbolSetRandomLengthByIndex(index, randomLengthSwitch.isChecked(), requireContext());
                    if (isChecked) {
                        randomLengthSwitch.setText("Вручну");
                    } else {
                        randomLengthSwitch.setText("Випадково");
                    }
                }
            });
        }

        private void setOnChangedToEditText(int index) {
            watcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable editable) {
                    removeTextChangedListeners();
                    String text = editable.toString();
                    if (text.equals("")) {
                        text = "0";
                    }
                    sharedGeneratorDataViewModel.editSymbolSetLengthByIndex(index, Integer.parseInt(text), requireContext());
                    printSetsLengths();
                    lengthEdit.setSelection(lengthEdit.getText().length());
                    addTextChangedListeners();
                }
            };
            lengthEdit.addTextChangedListener(watcher);
        }

        private void setOnFocusToEditText() {
            lengthEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        removeTextChangedListeners();
                        String currentText = lengthEdit.getText().toString();
                        if (currentText.equals("0")) {
                            lengthEdit.setText("");
                        } else if (currentText.length() > 0 && currentText.charAt(0) == '0') {
                            lengthEdit.setText(currentText.substring(1));
                        }
                        addTextChangedListeners();
                    }
                }
            });
        }

        public void setData(int index) {
            usageSwitch.setChecked(sharedGeneratorDataViewModel.getSymbolsSetUsageByIndex(index));
            randomLengthSwitch.setChecked(sharedGeneratorDataViewModel.getSymbolsSetRandomLenByIndex(index));
            printLength(index);
        }

        public void printLength(int index) {
            lengthEdit.setText(Integer.toString(sharedGeneratorDataViewModel.getSymbolsSetLengthByIndex(index)));
            signItemLen.setText(Integer.toString(sharedGeneratorDataViewModel.getSymbolsSetLengthByIndex(index)));
        }

        public void printSymbolSetBlock(View view, String symbolSetName, String sign) {
            LinearLayout parentContainer = view.findViewById(R.id.mainContainer);
            View settingsItem = getLayoutInflater().inflate(R.layout.fragment_password_generator_item, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, ((HomeActivity) requireActivity()).convertDPtoPX(20), 0, 0);

            usageSwitch = settingsItem.findViewById(R.id.typeSwitch);
            randomLengthSwitch = settingsItem.findViewById(R.id.randomLenSwitch);
            lengthEdit = settingsItem.findViewById(R.id.editLength);

            usageSwitch.setId(View.generateViewId());
            randomLengthSwitch.setId(View.generateViewId());
            lengthEdit.setId(View.generateViewId());

            usageSwitch.setText(symbolSetName);

            TextView symbolSetSign = settingsItem.findViewById(R.id.symbolSetSign);
            symbolSetSign.setText(sign);

            LinearLayout placeForItems = view.findViewById(R.id.placeForGeneratorSettings);
            parentContainer.addView(settingsItem, parentContainer.indexOfChild(placeForItems), layoutParams);
        }
    }

    private SymbolSetSettings[] symbolSetSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password_generator, container, false);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        sharedGeneratorDataViewModel = new ViewModelProvider(requireActivity()).get(SharedGeneratorDataViewModel.class);

        setSymbolsSetSettings(view);
        setOnClickToGeneratePassButton(view);
        setSettingsToSeekBar(view);
        setSettingsToEditText(view);
        printData(view);

        return view;
    }

    private void printData(View view) {
        setPassLengthToTextView(view);

        EditText editText = view.findViewById(R.id.notUseSymbolsEdit);
        editText.setText(sharedGeneratorDataViewModel.getNotUseSymbols());

        for (int i=0; i<symbolSetSettings.length; i++) {
            symbolSetSettings[i].setData(i);
        }
    }

    private void setPassLengthToTextView(View view) {
        TextView textView = view.findViewById(R.id.textPasswordLength);
        textView.setText("Довжина пароля: " + sharedGeneratorDataViewModel.getPassLength());
    }

    // Встановлення налаштуань для повзунка довжини пароля
    private void setSettingsToSeekBar(View view) {
        SeekBar seekBar = view.findViewById(R.id.passwordLengthSeekBar);
        seekBar.setMax(sharedGeneratorDataViewModel.getMaxPassLength() - sharedGeneratorDataViewModel.getMinPassLength());
        seekBar.setProgress(sharedGeneratorDataViewModel.getPassLength() - sharedGeneratorDataViewModel.getMinPassLength());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hideAllKeyBoards(view);
                sharedGeneratorDataViewModel.editPassLength(progress + sharedGeneratorDataViewModel.getMinPassLength(), requireContext());
                setPassLengthToTextView(view);
                printSetsLengths();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setSettingsToEditText(View view) {
        EditText editText = view.findViewById(R.id.notUseSymbolsEdit);

        homeViewModel.setMaxLengthForInput(editText, sharedGeneratorDataViewModel.getMaxNotUseSymbols());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                sharedGeneratorDataViewModel.editNotUseSymbols(editable.toString(), requireContext());
            }
        });
    }

    private void setSymbolsSetSettings(View view) {
        symbolSetSettings = new SymbolSetSettings[sharedGeneratorDataViewModel.getNumberTypes()];

        symbolSetSettings[0] = new SymbolSetSettings(view, 0, "Цифри", "0-9", R.id.signItem1, R.id.signItemLen1);
        symbolSetSettings[1] = new SymbolSetSettings(view, 1, "Маленькі літери", "a-z", R.id.signItem2, R.id.signItemLen2);
        symbolSetSettings[2] = new SymbolSetSettings(view, 2, "Великі літери", "A-Z", R.id.signItem3, R.id.signItemLen3);
        symbolSetSettings[3] = new SymbolSetSettings(view, 3, "Спец. символи", "#.&", R.id.signItem4, R.id.signItemLen4);
    }

    // Функція приховання клавіатур
    private void hideAllKeyBoards(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        hideKeyBoard(inputMethodManager, view, R.id.passwordEdit);

        hideKeyBoard(inputMethodManager, view, symbolSetSettings[0].getEditTextId());
        hideKeyBoard(inputMethodManager, view, symbolSetSettings[1].getEditTextId());
        hideKeyBoard(inputMethodManager, view, symbolSetSettings[2].getEditTextId());
        hideKeyBoard(inputMethodManager, view, symbolSetSettings[3].getEditTextId());

        hideKeyBoard(inputMethodManager, view, R.id.notUseSymbolsEdit);
    }

    // Приховання клавіатури для конкретного поля
    private void hideKeyBoard(InputMethodManager inputMethodManager, View view, int id) {
        EditText editText = view.findViewById(id);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void printSetsLengths() {
        for (int i=0; i<symbolSetSettings.length; i++) {
            symbolSetSettings[i].printLength(i);
        }
    }

    private void removeTextChangedListeners() {
        for (SymbolSetSettings symbolSetSetting: symbolSetSettings) {
            symbolSetSetting.lengthEdit.removeTextChangedListener(symbolSetSetting.watcher);;
        }
    }

    private void addTextChangedListeners() {
        for (SymbolSetSettings symbolSetSetting: symbolSetSettings) {
            symbolSetSetting.lengthEdit.addTextChangedListener(symbolSetSetting.watcher);;
        }
    }

    private void setOnClickToGeneratePassButton(View view) {
        LinearLayout button = view.findViewById(R.id.createPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText passwordEdit = view.findViewById(R.id.passwordEdit);
                passwordEdit.setText(
                        sharedGeneratorDataViewModel.getPassword(requireContext())
                );
                printSetsLengths();
            }
        });
    }
}