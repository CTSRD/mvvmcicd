package com.test.mvvmcicd.customDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.test.mvvmcicd.R;
import com.test.mvvmcicd.databinding.LayoutShareWifiBinding;
import com.test.mvvmcicd.utils.TopSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * 選擇 的 BottomSheetDialog
 */
public class ShareWifiTopSheetDialog extends MyTopSheetDialog {

    @Nullable
    private String title,subtitle;

    private LayoutShareWifiBinding binding;

    @Nullable
    private Listener listener;

    public ShareWifiTopSheetDialog(@NonNull Context context
            , @NonNull final String title) {
        super(context);
        init();
    }

    public ShareWifiTopSheetDialog(@NonNull Context context
            , int theme
            , @NonNull final String title
            , @Nullable final String subtitle
            ) {
        super(context, theme);
        this.title = title;
        this.subtitle = subtitle;
        init();
    }

    public interface Listener {
        void onCancel();
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    private void init() {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext())
                , R.layout.layout_share_wifi
                , null
                , false
        );

        binding.tvTitle.setText(title);

        if(this.subtitle != null){
            binding.tvSubtitle.setText(subtitle);
            binding.tvSubtitle.setVisibility(View.VISIBLE);
        }else{
            binding.tvSubtitle.setVisibility(View.GONE);
        }

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });

        setViewDataBinding(binding);

        final TopSheetBehavior<View> topSheetBehavior = TopSheetBehavior.from((View) binding.getRoot().getParent());
        topSheetBehavior.setHideable(false);
        topSheetBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
        topSheetBehavior.setSkipCollapsed(true);
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                topSheetBehavior.setPeekHeight(binding.getRoot().getMeasuredHeight());
            }
        });
        topSheetBehavior.setTopSheetCallback(new TopSheetBehavior.TopSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    topSheetBehavior.setState(TopSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
}
