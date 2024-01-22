package com.kibergod.passwordstorage.ui.pages.tools;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kibergod.passwordstorage.BuildConfig;
import com.kibergod.passwordstorage.R;

import java.util.Arrays;

public class AppInfoFragment extends Fragment {
    public Boolean[] isActivated = {false, false, false};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_info, container, false);

        setVersion(view);
        setMoveToCardView(view, R.id.cardArea1, R.id.card1, R.id.descriptionText1, 0);
        setMoveToCardView(view, R.id.cardArea2, R.id.card2, R.id.descriptionText2, 1);
        setMoveToCardView(view, R.id.cardArea3, R.id.card3, R.id.descriptionText3, 2);
        return view;
    }

    private void setVersion(View view) {
        TextView versionTextView = view.findViewById(R.id.programVersionView2);
        versionTextView.setText("v."+ BuildConfig.VERSION_NAME);
        versionTextView.setPaintFlags(versionTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setMoveToCardView(View view, int idCardArea, int idCard, int idDescriptionText, int i) {
        ScrollView scrollView = view.findViewById(R.id.scrollArea);
        ConstraintLayout constraintLayout = view.findViewById(idCardArea);
        CardView cardView = view.findViewById(idCard);
        final float[] card_xPosition = new float[1];

        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        card_xPosition[0] = cardView.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        cardView.setX(event.getRawX() - card_xPosition[0]);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (cardView.getX() <= 0) {
                            cardView.setX(20);
                            TextView textView = view.findViewById(idDescriptionText);
                            textView.setTextColor(requireContext().getColor(R.color.purple));
                            isActivated[i] = true;
                            checkActivationPassphrase(scrollView, view);
                            cardView.setOnTouchListener(null);
                        } else {
                            cardView.animate().x(constraintLayout.getWidth() / 2 - cardView.getWidth() / 2).setDuration(300).start();
                        }
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return true;
            }
        });
    }

    private void checkActivationPassphrase(ScrollView scrollView, View view) {
        if (Arrays.stream(isActivated).allMatch(value -> value)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.setVisibility(View.GONE);
                    LinearLayout linearLayout = view.findViewById(R.id.passphrase);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }, 1000);
        }
    }
}