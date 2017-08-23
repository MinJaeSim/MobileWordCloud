package com.example.simminje.mobilewordcloud.View;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simminje.mobilewordcloud.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class StepFragment extends Fragment implements Step {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        TextView stepTitle = null;
        TextView stepBody = null;

        int num = getArguments().getInt("page");
        switch (num) {
            case 0:
                v = inflater.inflate(R.layout.step1, container, false);
                stepTitle = (TextView) v.findViewById(R.id.step1_1);
                stepBody = (TextView) v.findViewById(R.id.step1_2);
                break;
            case 1:
                v = inflater.inflate(R.layout.step2, container, false);
                stepTitle = (TextView) v.findViewById(R.id.step2_1);
                stepBody = (TextView) v.findViewById(R.id.step2_2);
                break;
            case 2:
                v = inflater.inflate(R.layout.step3, container, false);
                stepTitle = (TextView) v.findViewById(R.id.step3_1);
                stepBody = (TextView) v.findViewById(R.id.step3_2);
                break;
        }

        stepTitle.setTextColor(Color.rgb(68, 68, 68));
        stepBody.setTextColor(Color.rgb(68, 68, 68));

        return v;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
