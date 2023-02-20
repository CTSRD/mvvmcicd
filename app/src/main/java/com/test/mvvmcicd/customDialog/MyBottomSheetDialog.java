package com.test.mvvmcicd.customDialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MyBottomSheetDialog extends BottomSheetDialog {

    @Nullable
    private ViewDataBinding viewDataBinding = null;

    public MyBottomSheetDialog(@NonNull Context context) {
        this(context, 0);
    }

    public MyBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected MyBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Nullable
    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    public void setViewDataBinding(@Nullable ViewDataBinding viewDataBinding) {
        this.viewDataBinding = viewDataBinding;
        setContentView(this.viewDataBinding.getRoot());
    }

}
