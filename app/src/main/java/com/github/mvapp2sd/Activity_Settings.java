package com.github.mvapp2sd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

/**
 * Created by EternalPhane on 2015/7/4.
 * GitHub: https://github.com/EternalPhane
 */
public class Activity_Settings extends Activity {
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        app = (MyApplication) getApplication();
        int set_id[] = new int[app.Get_set_num()];
        set_id[0] = R.id.set_showsys;
        set_id[1] = R.id.set_linkdata;
        set_id[2] = R.id.set_linkcache;
        for (int i = 0; i < app.Get_set_num(); i++) {
            ((CheckedTextView) findViewById(set_id[i])).setTag(i);
            ((CheckedTextView) findViewById(set_id[i])).setChecked(app.Get_set_bool(i));
            ((CheckedTextView) findViewById(set_id[i])).setOnClickListener(
                    new CheckedTextView.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((CheckedTextView) v).toggle();
                            app.Toggle_set_bool((int) (v.getTag()));
                        }
                    }
            );
        }
    }
}
