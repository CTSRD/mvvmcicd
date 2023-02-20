package com.test.mvvmcicd.customDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.test.mvvmcicd.R;
import com.test.mvvmcicd.databinding.LayoutCustomBottomSheetDialog169Binding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * 選擇 的 BottomSheetDialog
 */
public class ImageBottomSheetDialog169 extends MyBottomSheetDialog {

    private LayoutCustomBottomSheetDialog169Binding binding;

    @NonNull
    private final int image;

    @Nullable
    String title, message, pos;

    @Nullable
    private Listener listener;

    public ImageBottomSheetDialog169(@NonNull Context context
            ,@NonNull int image, @Nullable String title, @Nullable String message,@Nullable String pos) {
        super(context);
        this.image = image;
        this.title = title;
        this.message = message;
        this.pos = pos;
        init();
    }

    public ImageBottomSheetDialog169(@NonNull Context context, int theme,@NonNull int image, @Nullable String title, @Nullable String message,@Nullable String pos) {
        super(context, theme);
        this.image = image;
        this.title = title;
        this.message = message;
        this.pos = pos;
        init();
    }

    public interface Listener {
        void onAction();

        void onCancel();
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    private void init() {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext())
                , R.layout.layout_custom_bottom_sheet_dialog_16_9
                , null
                , false
        );

        binding.dialogImage.setImageResource(image);

        if(title != null){
            binding.textTitle.setVisibility(View.VISIBLE);
            binding.textTitle.setText(title);
        }else{
            binding.textTitle.setVisibility(View.GONE);
        }

        if(message != null){
            binding.textMsg.setVisibility(View.VISIBLE);
            binding.textMsg.setText(message);
        }else{
            binding.textMsg.setVisibility(View.GONE);
        }

        if(pos != null){
            binding.btnAction.setVisibility(View.VISIBLE);
            binding.btnAction.setText(pos);
        }else{
            binding.btnAction.setVisibility(View.GONE);
        }

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });

        binding.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onAction();
                }
            }
        });

        setViewDataBinding(binding);

        final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setSkipCollapsed(true);
        binding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                bottomSheetBehavior.setPeekHeight(binding.getRoot().getMeasuredHeight());
            }
        });
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
}
