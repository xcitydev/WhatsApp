package com.xcityprime.whatsapp.view.activities.display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.xcityprime.whatsapp.R;
import com.xcityprime.whatsapp.databinding.ActivityImageBinding;
import com.xcityprime.whatsapp.view.activities.common.Common;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);

        binding.zoomable.setImageBitmap(Common.IMAGE_BITMAP);
    }
}