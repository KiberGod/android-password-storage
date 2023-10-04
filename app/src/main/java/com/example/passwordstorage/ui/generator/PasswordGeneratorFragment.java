package com.example.passwordstorage.ui.generator;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.passwordstorage.R;
import com.example.passwordstorage.data.SharedGeneratorDataViewModel;
import com.example.passwordstorage.ui.HomeViewModel;

public class PasswordGeneratorFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private SharedGeneratorDataViewModel sharedGeneratorDataViewModel;

    public class SymbolSetSettings {
        public Switch usageSwitch;
        public Switch randomLengthSwitch;
        public EditText lengthEdit;

        public SymbolSetSettings(View view, int idUsageSwitch, int idRandomLengthSwitch, int idLengthEdit, int index) {
            this.usageSwitch = view.findViewById(idUsageSwitch);
            this.randomLengthSwitch = view.findViewById(idRandomLengthSwitch);
            this.lengthEdit = view.findViewById(idLengthEdit);

            setOnCheckedToUsageSwitch(view, index);
            setOnCheckedToRandomLengthSwitch(view, index);
            homeViewModel.setMaxLengthForInput(this.lengthEdit, 4);
        }

        private void setOnCheckedToUsageSwitch(View view, int index) {
            usageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    hideAllKeyBoards(view);
                    sharedGeneratorDataViewModel.editSymbolSetUsageByIndex(index);
                    if (isChecked) {
                        randomLengthSwitch.setVisibility(View.VISIBLE);
                        if (randomLengthSwitch.isChecked()) {
                            lengthEdit.setVisibility(View.VISIBLE);
                        }
                    } else {
                        randomLengthSwitch.setVisibility(View.GONE);
                        lengthEdit.setVisibility(View.GONE);
                    }
                }
            });
        }

        private void setOnCheckedToRandomLengthSwitch(View view, int index) {
            randomLengthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    hideAllKeyBoards(view);
                    sharedGeneratorDataViewModel.editSymbolSetRandomLengthByIndex(index);
                    if (isChecked) {
                        lengthEdit.setVisibility(View.VISIBLE);
                        randomLengthSwitch.setText("Вручну");
                    } else {
                        lengthEdit.setVisibility(View.GONE);
                        randomLengthSwitch.setText("Випадково");
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
        sharedGeneratorDataViewModel.testInit();

        setSymbolsSetSettings(view);
        setSettingsToSeekBar(view);
        setSettingsToEditText(view);
        setOnClickToSaveLengthsButton(view);
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
                sharedGeneratorDataViewModel.editPassLength(progress + sharedGeneratorDataViewModel.getMinPassLength());
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
                sharedGeneratorDataViewModel.editNotUseSymbols(editable.toString());
            }
        });
    }

    private void setSymbolsSetSettings(View view) {
        symbolSetSettings = new SymbolSetSettings[sharedGeneratorDataViewModel.getNumberTypes()];

        symbolSetSettings[0] = new SymbolSetSettings(view, R.id.typeSwitch1, R.id.randomLenSwitch1, R.id.editLength1, 0);
        symbolSetSettings[1] = new SymbolSetSettings(view, R.id.typeSwitch2, R.id.randomLenSwitch2, R.id.editLength2, 1);
        symbolSetSettings[2] = new SymbolSetSettings(view, R.id.typeSwitch3, R.id.randomLenSwitch3, R.id.editLength3, 2);
        symbolSetSettings[3] = new SymbolSetSettings(view, R.id.typeSwitch4, R.id.randomLenSwitch4, R.id.editLength4, 3);
    }

    // Функція приховання клавіатур
    private void hideAllKeyBoards(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        hideKeyBoard(inputMethodManager, view, R.id.passwordEdit);

        hideKeyBoard(inputMethodManager, view, R.id.editLength1);
        hideKeyBoard(inputMethodManager, view, R.id.editLength2);
        hideKeyBoard(inputMethodManager, view, R.id.editLength3);
        hideKeyBoard(inputMethodManager, view, R.id.editLength4);

        hideKeyBoard(inputMethodManager, view, R.id.notUseSymbolsEdit);
    }

    // Приховання клавіатури для конкретного поля
    private void hideKeyBoard(InputMethodManager inputMethodManager, View view, int id) {
        EditText editText = view.findViewById(id);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void setOnClickToSaveLengthsButton(View view) {
        Button button = view.findViewById(R.id.setNewLengthsButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAllKeyBoards(view);
                for (int i=0; i<symbolSetSettings.length; i++) {
                    String text = symbolSetSettings[i].lengthEdit.getText().toString();
                    if (text.equals("")) {
                        text = "0";
                    }
                    sharedGeneratorDataViewModel.editSymbolSetLengthByIndex(i, Integer.parseInt(text));
                }

                printSetsLengths();
            }
        });
    }

    private void printSetsLengths() {
        for (int i=0; i<symbolSetSettings.length; i++) {
            symbolSetSettings[i].printLength(i);
        }
    }
}