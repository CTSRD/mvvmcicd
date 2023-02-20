package com.test.mvvmcicd.customDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.test.mvvmcicd.R;
import com.test.mvvmcicd.databinding.BottomSheetNodeInfoBinding;
import com.test.mvvmcicd.model.Node;
import com.test.mvvmcicd.model.NodeModel;
import com.test.mvvmcicd.model.NodeType;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.test.mvvmcicd.databinding.BottomSheetNodeInfoBinding;
import com.test.mvvmcicd.model.NodeModel;

/**
 * 選擇 的 BottomSheetDialog
 */
public class NodeInfoBottomSheetDialog extends MyBottomSheetDialog {

    @NonNull Context context;

    /**
     * need input uplink if need to show
     */
    @Nullable
    private final String uplink;

    private ConstraintLayout clDevices;
    private ConstraintLayout clLocation;
    private ConstraintLayout clAddNode;
    private ConstraintLayout clMoreInfo;
    private TextView tvTroubleShooting;
    private TextView tvTitle, tvSubTitle;
    private ExtendedFloatingActionButton tvDeviceIcon, ivLocationIcon, ivAddNodeIcon, ivMoreIcon;
//    private ImageView ivAddNodeIcon;

    private BottomSheetNodeInfoBinding binding;

    @Nullable
    private Listener listener;

    public NodeInfoBottomSheetDialog(@NonNull Context context
            , int theme
            , @NonNull NodeModel node
            , @Nullable String fromNode
    ) {
        super(context, theme);
        this.context = context;
        this.uplink = fromNode;
        init(node);
    }

    public interface Listener {
        void onCancel();

        void onSelect(int action, NodeModel node);
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    private void init(NodeModel node) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(getContext())
                , R.layout.bottom_sheet_node_info
                , null
                , false
        );

        setViewDataBinding(binding);

        tvTitle = binding.headerView.tvTitle;
        tvSubTitle = binding.headerView.tvSubtitle;
        clDevices = binding.clDeviceInfo;
        clLocation = binding.clLocation;
        clAddNode = binding.clAddNodes;
        clMoreInfo = binding.clMoreInfo;
        tvTroubleShooting = binding.tvTroubleShooting;
        tvDeviceIcon = binding.tvDeviceIcon;
        ivLocationIcon = binding.ivLocationIcon;
        ivAddNodeIcon = binding.ivAddNodeIcon;
        ivMoreIcon = binding.ivMoreIcon;

        setNodeToBottomSheet(node);

        binding.headerView.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });

        clAddNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(v.getId(), node);
                }
            }
        });
        ivAddNodeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(R.id.cl_add_nodes, node);
                }
            }
        });
        clDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(v.getId(), node);
                }
            }
        });
        tvDeviceIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(R.id.cl_device_info, node);
                }
            }
        });
        clLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(v.getId(), node);
                }
            }
        });
        ivLocationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(R.id.cl_location, node);
                }
            }
        });
        clMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(v.getId(), node);
                }
            }
        });
        ivMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(R.id.cl_more_info, node);
                }
            }
        });
        tvTroubleShooting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSelect(R.id.tvTroubleShooting, node);
                }
            }
        });

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

    private void setNodeToBottomSheet(NodeModel node) {
        binding.bottomSheetInfo.setTag(node);
        tvTitle.setText(node.getNode().getLocation());
        setSubTitle(node.getNode());
        tvDeviceIcon.setText(String.valueOf(node.getNode().getDevices()));

        if(node.getNode().getRole().equals(NodeType.EARTH.name())){
            clDevices.setVisibility(View.VISIBLE);
            clLocation.setVisibility(View.GONE);
            clAddNode.setVisibility(View.GONE);
            clMoreInfo.setVisibility(View.VISIBLE);
            tvTroubleShooting.setVisibility(View.GONE);
            clDevices.setEnabled(true);
            clMoreInfo.setEnabled(true);
        }else if(node.getNode().getRole().equals(NodeType.CAP.name())){
            clDevices.setVisibility(View.VISIBLE);
            clLocation.setVisibility(View.VISIBLE);
            clAddNode.setVisibility(View.VISIBLE);
            clMoreInfo.setVisibility(View.GONE);
            tvTroubleShooting.setVisibility(View.GONE);
            if(node.getNode().getConnected()){
                tvDeviceIcon.setEnabled(true);
                ivLocationIcon.setEnabled(true);
                ivAddNodeIcon.setEnabled(true);
                clDevices.setEnabled(true);
                clLocation.setEnabled(true);
                clAddNode.setEnabled(true);
            }else{
//                    tvDeviceIcon.setEnabled(false
//                    ivLocationIcon.setEnabled(false
//                    ivAddNodeIcon.setEnabled(false
                clDevices.setVisibility(View.GONE);
                clLocation.setVisibility(View.GONE);
                clAddNode.setVisibility(View.GONE);
                tvTroubleShooting.setVisibility(View.VISIBLE);
            }
        }else{
            clDevices.setVisibility(View.VISIBLE);
            clLocation.setVisibility(View.VISIBLE);
            clAddNode.setVisibility(View.GONE);
            clMoreInfo.setVisibility(View.VISIBLE);
            tvTroubleShooting.setVisibility(View.GONE);
            if(node.getNode().getConnected()){
                tvDeviceIcon.setEnabled(true);
                ivLocationIcon.setEnabled(true);
                clDevices.setEnabled(true);
                clLocation.setEnabled(true);
            }else{
                tvDeviceIcon.setEnabled(false);
                ivLocationIcon.setEnabled(false);
                clDevices.setEnabled(false);
                clLocation.setEnabled(false);
            }
        }
    }

    private void setSubTitle(Node node) {
        if(!node.getConnected()){
            tvSubTitle.setVisibility(View.VISIBLE);
            tvSubTitle.setText("no_response");
            tvSubTitle.setTextColor(context.getColor(R.color.warning));
        }else if(node.getDistance() == 1){
            tvSubTitle.setVisibility(View.VISIBLE);
            tvSubTitle.setTextColor(context.getColor(R.color.warning));
            tvSubTitle.setText("too_close");
        }else if(node.getDistance() == 2){
            tvSubTitle.setTextColor(context.getColor(R.color.warning));
            tvSubTitle.setVisibility(View.VISIBLE);
            tvSubTitle.setText("too_far");
        }else if(node.getDistance() == 4){
            tvSubTitle.setTextColor(context.getColor(R.color.warning));
            tvSubTitle.setVisibility(View.GONE);
        }else{
            tvSubTitle.setVisibility(View.GONE);
        }
    }
}
