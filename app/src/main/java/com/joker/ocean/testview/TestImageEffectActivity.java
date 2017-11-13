package com.joker.ocean.testview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.joker.ocean.R;
import com.joker.ocean.pic.PicColorMatrixOperImpl;
import com.joker.pacific.component.BaseFragmentActivity;

/**
 * Created by joker on 17-11-13.
 */

public class TestImageEffectActivity extends BaseFragmentActivity implements View.OnClickListener {
    private ImageView imgPic;
    private SeekBar sbTune;
    private SeekBar sbSaturation;
    private SeekBar sbBright;
    private TextView tvReversal;
    private TextView tvNostalgia;
    private TextView tvColor;
    private TextView tvGray;
    private TextView tvHighSaturation;

    private TextView tvNegative;
    private TextView tvOldPic;
    private TextView tvNostalgiaEffect;
    private TextView tvRelief;

    private TextView tvTranslate;
    private TextView tvScaleBig;
    private TextView tvScaleSmall;
    private TextView tvHSkew;
    private TextView tvVSkew;
    private float mHUe;
    private float mSaturation;
    private float mLum;
    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private Bitmap mBitmap;
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.sb_tone:
                    mHUe = (progress - MID_VALUE) / 100 * 180;
                    break;
                case R.id.sb_saturation:
                    mSaturation = progress * 1.0f / MID_VALUE;
                    break;
                case R.id.sb_bright:
                    mLum = progress *  1.0F/ MID_VALUE;
                    break;
            }
            imgPic.setImageBitmap(PicColorMatrixOperImpl.handleImageEffect(mBitmap, mHUe, mSaturation, mLum));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_oper_layout);
        initView();
        setUp();
    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.luobin);
        imgPic = (ImageView) findViewById(R.id.iv_show);
        imgPic.setImageBitmap(mBitmap);
        sbTune = (SeekBar) findViewById(R.id.sb_tone);
        sbTune.setMax(MAX_VALUE);
        sbTune.setProgress(MID_VALUE);
        sbSaturation = (SeekBar) findViewById(R.id.sb_saturation);
        sbSaturation.setMax(MAX_VALUE);
        sbSaturation.setProgress(MID_VALUE);

        sbBright = (SeekBar) findViewById(R.id.sb_bright);
        sbBright.setMax(MAX_VALUE);
        sbBright.setProgress(MID_VALUE);

        tvReversal = (TextView) findViewById(R.id.tv_reversal);
        tvNostalgia = (TextView) findViewById(R.id.tv_Nostalgia);
        tvColor = (TextView) findViewById(R.id.tv_color);
        tvGray = (TextView) findViewById(R.id.tv_gray);
        tvHighSaturation = (TextView) findViewById(R.id.tv_high_saturation);

        tvNegative = (TextView) findViewById(R.id.tv_negative);
        tvOldPic = (TextView) findViewById(R.id.tv_old_pic);
        tvNostalgiaEffect = (TextView) findViewById(R.id.tv_Nostalgia_effect);
        tvRelief = (TextView) findViewById(R.id.tv_relief);

        tvTranslate = (TextView) findViewById(R.id.tv_translate);
        tvScaleBig = (TextView) findViewById(R.id.tv_scale_big);
        tvScaleSmall = (TextView) findViewById(R.id.tv_scale_small);
        tvHSkew = (TextView) findViewById(R.id.tv_h_skew);
        tvVSkew = (TextView) findViewById(R.id.tv_v_skew);
    }


    private void setUp() {
        sbTune.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        sbSaturation.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        sbBright.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        tvReversal.setOnClickListener(this);
        tvNostalgia.setOnClickListener(this);
        tvColor.setOnClickListener(this);
        tvGray.setOnClickListener(this);
        tvHighSaturation.setOnClickListener(this);

        tvNegative.setOnClickListener(this);
        tvOldPic.setOnClickListener(this);
        tvNostalgiaEffect.setOnClickListener(this);
        tvRelief.setOnClickListener(this);

        tvTranslate.setOnClickListener(this);
        tvScaleBig.setOnClickListener(this);
        tvScaleSmall.setOnClickListener(this);
        tvHSkew.setOnClickListener(this);
        tvVSkew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reversal:
                break;
            case R.id.tv_Nostalgia:
                break;
            case R.id.tv_color:
                break;
            case R.id.tv_gray:
                break;
            case R.id.tv_high_saturation:
                break;

            case R.id.tv_negative:
                break;
            case R.id.tv_old_pic:
                break;
            case R.id.tv_Nostalgia_effect:
                break;
            case R.id.tv_relief:
                break;

            case R.id.tv_translate:
                break;
            case R.id.tv_scale_big:
                break;
            case R.id.tv_scale_small:
                break;
            case R.id.tv_h_skew:
                break;
            case R.id.tv_v_skew:
                break;
        }
    }
}
