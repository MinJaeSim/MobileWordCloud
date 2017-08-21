package com.example.simminje.mobilewordcloud.View;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simminje.mobilewordcloud.R;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PastResultFragment extends Fragment {

    private List<File> files;
    private int pos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_result, container, false);
        loadAnalysisData();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(new RecyclerView.Adapter<resultHolder>() {
            @Override
            public resultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                final View view = layoutInflater.inflate(R.layout.recycle_item, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DisplayActivity.class);
                        intent.putExtra("num", recyclerView.getChildAdapterPosition(view));
                        startActivity(intent);
                    }
                });
                return new resultHolder(view);
            }

            @Override
            public void onBindViewHolder(resultHolder holder, int position) {
                String title = files.get(position).getName();
                holder.setText(title);
            }

            @Override
            public int getItemCount() {
                return files.size();
            }
        });

        return view;
    }

    private void loadAnalysisData() {
        String dirPath = getContext().getFilesDir().getAbsolutePath();

        File file = new File(dirPath);
        File[] f = file.listFiles();
        if (f.length > 0)
            files = Arrays.asList(f);
    }

    class resultHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        resultHolder(View itemView) {
            super(itemView);

            Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "BodyRegular.otf");

            textView = (TextView) itemView.findViewById(R.id.chat_text_view);
            textView.setTypeface(type);

        }

        public void setText(String text) {
            textView.setText(text);
        }
    }
}
