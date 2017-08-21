package com.example.simminje.mobilewordcloud.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simminje.mobilewordcloud.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class StepFragment extends Fragment implements Step{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        int num = getArguments().getInt("page");
        switch (num) {
            case 0 :
                v = inflater.inflate(R.layout.step1, container, false);
                break;
            case 1 :
                v = inflater.inflate(R.layout.step2, container, false);
                break;
            case 2 :
                v = inflater.inflate(R.layout.step3, container, false);
                break;
        }
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
