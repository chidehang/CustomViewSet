package com.demo.cdh.customviewset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.demo.cdh.customviewset.view.SearchView;

public class PathActivity extends Activity implements View.OnClickListener {

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchView:
                searchView.startSearch();
                break;
        }
    }
}
