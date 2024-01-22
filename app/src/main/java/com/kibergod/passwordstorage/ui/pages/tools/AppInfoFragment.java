package com.kibergod.passwordstorage.ui.pages.tools;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kibergod.passwordstorage.BuildConfig;
import com.kibergod.passwordstorage.R;
public class AppInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_info, container, false);

        setVersion(view);
        return view;
    }

    private void setVersion(View view) {
        TextView versionTextView = view.findViewById(R.id.programVersionView2);
        versionTextView.setText("v."+ BuildConfig.VERSION_NAME);
        versionTextView.setPaintFlags(versionTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}