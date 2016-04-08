package com.laole918.umpaytest.binding;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.laole918.lib.text.SimpleTextWatcher;

/**
 * Created by laole918 on 2016/3/31 0031.
 */
public class BindingAdapters {

    @BindingAdapter({"app:edit_text"})
    public static void addTextChangedListener(EditText editText, CharSequence edit_text) {
        ViewDataBinding binding = DataBindingUtil.findBinding(editText);
        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
//                binding.setVariable()
            }
        });
    }
}
