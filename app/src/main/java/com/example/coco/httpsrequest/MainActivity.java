package com.example.coco.httpsrequest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coco.httpsrequest.http.HttpUtilsSafe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 12306证书
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.mEd)
    EditText mEd;
    @BindView(R.id.mBtn)
    Button mBtn;
    @BindView(R.id.mTv)
    TextView mTv;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        handler = new Handler(getMainLooper());
    }

    @OnClick(R.id.mBtn)
    public void onViewClicked() {
        String ss = mEd.getText().toString().trim();
        HttpUtilsSafe.getInstance().get(this, ss, new HttpUtilsSafe.OnRequestCallBack() {
            @Override
            public void Successed(final String s) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTv.setText(s);
                    }
                });
            }

            @Override
            public void Failed(final Exception e) {

                Log.e(TAG, "Failed: "+e.getMessage() );
            }
        });
    }
}
