package com.laole918.umpaytest.binding;

import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by laole918 on 2016/3/31 0031.
 */
public class BindingAdapters {

    @BindingAdapter({"addTextChangedListener"})
    public static void addTextChangedListener(EditText editText, TextWatcher addTextChangedListener) {
        if(addTextChangedListener != null) {
            editText.addTextChangedListener(addTextChangedListener);
        }
    }
}
