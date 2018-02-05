package com.lqh.tapeview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lqh.library.TapeView;
import com.lqh.library.Utils;
import com.lqh.library.listener.OnScaleChangeListener;

public class MainActivity extends AppCompatActivity {

    private TapeView tapeView1;
    private TapeView tapeView2;
    private TextView textView1;
    private TextView textView2;
    private EditText editText;

    SpannableString msp1;
    SpannableString msp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tapeView1 = findViewById(R.id.tapeView1);
        tapeView2 = findViewById(R.id.tapeView2);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);

        tapeView1.setOnScaleChangeListener(new OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scale) {
                msp1 = new SpannableString(getString(R.string.height_label, scale + ""));
                msp1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                msp1.setSpan(new ForegroundColorSpan(Color.BLACK), msp1.length() - 2, msp1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                msp1.setSpan(new RelativeSizeSpan(1.5f), 3, msp1.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView1.setText(msp1);
            }
        });


        tapeView2.setMin(0);
        tapeView2.setMax(100);
        tapeView2.setInterval(10);
        tapeView2.setSpace(0.1f);
        tapeView2.setScaleLineColor(Color.WHITE);
        tapeView2.setBackgroundColor(Color.BLUE);
        tapeView2.setScaleTextColor(Color.WHITE);
        tapeView2.setIndicatorColor(Color.WHITE);
        tapeView2.setTextSize(Utils.dpToPixel(16));
        tapeView2.setCurrent(0);
        tapeView2.setOnScaleChangeListener(new OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scale) {
                msp2 = new SpannableString(getString(R.string.weight_label, scale + ""));
                msp2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                msp2.setSpan(new ForegroundColorSpan(Color.BLACK), msp2.length() - 2, msp2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                msp2.setSpan(new RelativeSizeSpan(1.5f), 3, msp2.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView2.setText(msp2);
            }
        });
    }

    public void scroll(View view) {
        if (!TextUtils.isEmpty(editText.getText())) {
            tapeView2.setCurrent(Float.valueOf(editText.getText().toString()));
        }
    }
}
