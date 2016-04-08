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

    @BindingAdapter({"android:addTextChangedListener"})
    public static void addTextChangedListener(EditText view, TextWatcher watcher) {
        if(watcher != null) {
            view.addTextChangedListener(watcher);
        }
    }
}
