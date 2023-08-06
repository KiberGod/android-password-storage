package com.example.passwordstorage.ui.storage.sections;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.passwordstorage.R;

public class RecordsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_records, container, false);

        for (int i=0; i<30; i++) {
            drawRecordButton(view);
        }
        return view;
    }

    // Функція додає кнопку запису
    private void drawRecordButton(View view) {
        Button button = new Button(requireContext());
        button.setText("Назва запису");
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text));

        GradientDrawable roundedRectangle = new GradientDrawable();
        roundedRectangle.setColor(ContextCompat.getColor(requireContext(), R.color.gray_2));
        roundedRectangle.setCornerRadius(36);
        ViewCompat.setBackground(button, roundedRectangle);


        ImageView vectorImageView = new ImageView(requireContext());
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.vector_template_image);
        vectorImageView.setImageDrawable(vectorDrawable);

        vectorImageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        vectorImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_text));


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.CENTER;
        buttonParams.bottomMargin = 20;
        buttonParams.topMargin = 20;
        buttonParams.rightMargin = 40;
        buttonParams.leftMargin = 40;
        button.setLayoutParams(buttonParams);

        button.setCompoundDrawablesWithIntrinsicBounds(vectorImageView.getDrawable(), null, null, null);

        LinearLayout rootLayout = view.findViewById(R.id.scrollArea);
        rootLayout.addView(button);
    }
}