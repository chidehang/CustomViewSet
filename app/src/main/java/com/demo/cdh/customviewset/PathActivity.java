package com.demo.cdh.customviewset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.demo.cdh.customviewset.view.LoadView;
import com.demo.cdh.customviewset.view.SearchView;

public class PathActivity extends Activity implements View.OnClickListener {

    private SearchView searchView;
    private LoadView loadSuccess;
    private LoadView loadFailure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        searchView = (SearchView) findViewById(R.id.searchView);
        loadSuccess = (LoadView) findViewById(R.id.loadSuccess);
        loadFailure = (LoadView) findViewById(R.id.loadFailure);

        searchView.setOnClickListener(this);
        loadSuccess.setOnClickListener(this);
        loadFailure.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchView:
                searchView.startSearch();
                break;

            case R.id.loadSuccess:
                loadSuccess.setResult = 1;
                loadSuccess.startLoading();
                break;

            case R.id.loadFailure:
                loadFailure.setResult = 2;
                loadFailure.startLoading();
                break;
        }
    }
}
