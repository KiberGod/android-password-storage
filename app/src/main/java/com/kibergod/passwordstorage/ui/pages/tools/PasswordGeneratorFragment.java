package com.kibergod.passwordstorage.ui.pages.tools;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.kibergod.passwordstorage.R;
import com.kibergod.passwordstorage.data.SharedGeneratorDataViewModel;
import com.kibergod.passwordstorage.data.SharedSettingsDataViewModel;
import com.kibergod.passwordstorage.ui.pages.HomeActivity;
import com.kibergod.passwordstorage.ui.pages.HomeViewModel;
import com.kibergod.passwordstorage.ui.tools.RabbitSupport;
import com.kibergod.passwordstorage.ui.utils.FontUtils;
import com.kibergod.passwordstorage.ui.utils.ImageUtils;
import com.kibergod.passwordstorage.ui.utils.KeyboardUtils;
import com.kibergod.passwordstorage.ui.utils.ViewUtils;

public class PasswordGeneratorFragment extends Fragment {
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;
    private SharedSettingsDataViewModel sharedSettingsDataViewModel;

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
            ViewUtils.setMaxLengthForInput(this.lengthEdit, 4);
            setOnChangedToEditText(index);
            setOnFocusToEditText();
            ImageUtils.setColorToImg(requireContext(), view, R.id.generatorButtonIcon, R.color.white);
        }

        private int getEditTextId() {
            return lengthEdit.getId();
        }

        private void setOnCheckedToUsageSwitch(View view, int index, int idSignBarItem) {
            ConstraintLayout signItemLayout = view.findViewById(idSignBarItem);
            usageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    KeyboardUtils.hideKeyboards(requireContext());
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
                    KeyboardUtils.hideKeyboards(requireContext());
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

            TextView textView = settingsItem.findViewById(R.id.randomLenText);
            textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (textView.getLayout().getLineCount() > 1) {
                        LinearLayout sign = settingsItem.findViewById(R.id.generatorItemSign);
                        sign.setVisibility(View.GONE);
                    }
                    textView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });

            FontUtils.setFontSizeToView(requireContext(), settingsItem, R.id.symbolSetSign, sharedSettingsDataViewModel.getFontSizeMain());
            FontUtils.setFontSizeToView(requireContext(), settingsItem, R.id.typeSwitch, sharedSettingsDataViewModel.getFontSizeMain());
            FontUtils.setFontSizeToView(requireContext(), settingsItem, R.id.randomLenSwitch, sharedSettingsDataViewModel.getFontSizeMain());
            FontUtils.setFontSizeToView(requireContext(), settingsItem, R.id.editLength, sharedSettingsDataViewModel.getFontSizeInput());
            FontUtils.setFontSizeToView(requireContext(), settingsItem, R.id.randomLenText, sharedSettingsDataViewModel.getFontSizeFieldCaptions());
            FontUtils.setFontSizeToView(requireContext(), settingsItem, R.id.lenText, sharedSettingsDataViewModel.getFontSizeFieldCaptions());

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_generator, container, false);
        sharedSettingsDataViewModel = new ViewModelProvider(requireActivity()).get(SharedSettingsDataViewModel.class);
        sharedGeneratorDataViewModel = new ViewModelProvider(requireActivity()).get(SharedGeneratorDataViewModel.class);

        RabbitSupport.setRabbitSupportDialogToIconByLongClick(view, R.id.generatorTitle, RabbitSupport.SupportDialogIDs.TOOLS_GENERATOR, requireContext(), sharedSettingsDataViewModel.getFontSizeRssMain(), sharedSettingsDataViewModel.getFontSizeRssSecondary());
        setSymbolsSetSettings(view);
        setOnClickToGeneratePassButton(view);
        setSettingsToSeekBar(view);
        setSettingsToEditText(view);
        printData(view);
        resizeFonts(view);
        return view;
    }

    private void resizeFonts(View view) {
        int fontSizeMain = sharedSettingsDataViewModel.getFontSizeMain();
        int fontSizeInput = sharedSettingsDataViewModel.getFontSizeInput();
        int fontSizeButtons = sharedSettingsDataViewModel.getFontSizeButtons();
        int fontSizeFieldCaptions = sharedSettingsDataViewModel.getFontSizeFieldCaptions();

        FontUtils.setFontSizeToView(requireContext(), view, R.id.generatorTitle, fontSizeMain);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.generatorSettingsText, fontSizeMain);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.passwordEdit, fontSizeInput);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.notUseSymbolsEdit, fontSizeInput);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.generatePasButtonText, fontSizeButtons);

        FontUtils.setFontSizeToView(requireContext(), view, R.id.genPassText, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.textPasswordLength, fontSizeFieldCaptions);
        FontUtils.setFontSizeToView(requireContext(), view, R.id.notUseSymbolsText, fontSizeFieldCaptions);
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
                KeyboardUtils.hideKeyboards(requireContext());
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

        ViewUtils.setMaxLengthForInput(editText, sharedGeneratorDataViewModel.getMaxNotUseSymbols());

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
        ViewUtils.setOnClickToView(view, R.id.createPassword, () -> {
            EditText passwordEdit = view.findViewById(R.id.passwordEdit);
            passwordEdit.setText(sharedGeneratorDataViewModel.getPassword(requireContext()));
            printSetsLengths();
        });
    }
}