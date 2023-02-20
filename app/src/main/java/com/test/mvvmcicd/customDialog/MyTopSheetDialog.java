package com.test.mvvmcicd.customDialog;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public class MyTopSheetDialog extends TopSheetDialog {

    @Nullable
    private ViewDataBinding viewDataBinding = null;

    public MyTopSheetDialog(@NonNull Context context) {
        this(context, 0);
    }

    public MyTopSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected MyTopSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
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
