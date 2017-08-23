package com.example.simminje.mobilewordcloud.View;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.simminje.mobilewordcloud.Model.Analysis;
import com.example.simminje.mobilewordcloud.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.stepstone.stepper.StepperLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.app.Activity.RESULT_OK;

public class LoadKakaoFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_OPENER = 0;
    private static final int REQUEST_CODE_RESOLUTION = 3;

    private GoogleApiClient mGoogleApiClient;
    private DriveId mSelectedFileDriveId;
    private String data;

    private Button displayButton;
    private ProgressBar progressBar;

    private boolean isCancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_load_kakao, container, false);

        StepperLayout stepperLayout = (StepperLayout) view.findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(new StepAdapter(getChildFragmentManager(), this.getContext()));

        isCancel = false;

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "TitleBold.otf");

        displayButton = (Button) view.findViewById(R.id.display_result);
        displayButton.setTypeface(type);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        FloatingActionButton googleButton = (FloatingActionButton) view.findViewById(R.id.googleButton);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(LoadKakaoFragment.this)
                    .addOnConnectionFailedListener(LoadKakaoFragment.this)
                    .build();
        }

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                isCancel = true;
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
            }
        });

        Button displayButton = (Button) view.findViewById(R.id.display_result);
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                if (analysisData()) {
                    Intent intent = new Intent(getContext(), DisplayActivity.class);
                    intent.putExtra("num", -1);
                    startActivity(intent);
                    hideProgressBar();
                    getActivity().finish();
                } else {
                    Snackbar.make(view, "다시 골라 주세요", Snackbar.LENGTH_SHORT).show();
                    hideProgressBar();
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_RESOLUTION && resultCode == RESULT_OK) {
            mGoogleApiClient.connect();
        } else if (requestCode == REQUEST_CODE_OPENER) {
            if (data == null) {
                isCancel = true;
                mSelectedFileDriveId = null;
            } else {
                isCancel = false;
                mSelectedFileDriveId = data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
            }
        }
    }

    private void open() {
        showProgressBar();
        DriveFile file = mSelectedFileDriveId.asDriveFile();

        file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(contentsOpenedCallback);
        mSelectedFileDriveId = null;
    }

    @Override
    public void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("GoogleDrive", "GoogleApiClient connected");

        if (mSelectedFileDriveId != null) {
            open();
            return;
        }

        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[]{"text/plain", "text/html"})
                .build(mGoogleApiClient);

        try {
            hideProgressBar();
            startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            Log.w("GoogleDrive", "Unable to send intent", e);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("GoogleDrive", "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i("GoogleDrive", "GoogleApiClient connection failed: " + result.toString());

        hideProgressBar();
        if (!result.hasResolution()) {
            GoogleApiAvailability.getInstance().getErrorDialog(this.getActivity(), result.getErrorCode(), 0).show();
            return;
        }
        try {
            result.startResolutionForResult(this.getActivity(), REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e("GoogleDrive", "Exception while starting resolution activity", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(LoadKakaoFragment.this)
                    .addOnConnectionFailedListener(LoadKakaoFragment.this)
                    .build();
        }
        if (!isCancel)
            mGoogleApiClient.connect();
    }

    private boolean analysisData() {
        if (data != null && data.length() > 0) {
            Context ctx = getContext();
            AssetManager am = ctx.getAssets();
            String dirPath = ctx.getFilesDir().getAbsolutePath();
            Analysis analysis = new Analysis(data);

            if (analysis.isFilterNull()) {
                try (InputStream stream = am.open("filter.txt")) {
                    analysis.loadFilter(stream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            analysis.saveData(dirPath);
            return true;
        } else {
            Snackbar.make(getView(), "분석할 데이터를 골라주세요", Snackbar.LENGTH_SHORT).show();
        }
        return false;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        displayButton.setEnabled(false);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        displayButton.setEnabled(true);
    }

    private final ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                return;
            }
            DriveContents contents = result.getDriveContents();

            BufferedReader reader = new BufferedReader(new InputStreamReader(contents.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append(" ");
                }
                data = builder.toString();
                hideProgressBar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}