package com.interdigital.android.samplemapdataapp.cluster.oxon;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.GravityCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.interdigital.android.samplemapdataapp.R;
import com.interdigital.android.samplemapdataapp.cluster.BaseClusterRenderer;

public class VmsClusterRenderer extends BaseClusterRenderer<VmsClusterItem> {

    private Context context;

    public VmsClusterRenderer(Context context, GoogleMap googleMap,
                              ClusterManager<VmsClusterItem> clusterManager) {
        super(context, googleMap, clusterManager);
        this.context = context;
    }

    @Override
    public int getIconResource(VmsClusterItem vmsClusterItem) {
        return R.drawable.vm_sign_icon;
    }

    @Override
    public int getIconClusterResource() {
        return R.drawable.vms_cluster_icon;
    }

    @Override
    public View getInfoContents(Marker marker) {
        VmsClusterItem vmsClusterItem = getClusterItem(marker);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView signTextView = new TextView(context);
        signTextView.setTextColor(0xff000000);
        signTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        signTextView.setTypeface(Typeface.DEFAULT_BOLD);
        StringBuilder buf = new StringBuilder();
        for (String line : vmsClusterItem.getVariableMessageSign().getLegend()) {
            buf.append(line.trim());
            if (line.trim().length() > 0) {
                buf.append("\n");
            }
        }
        signTextView.setText(buf.toString().trim());
        linearLayout.addView(signTextView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView nameTextView = new TextView(context);
        nameTextView.setText(vmsClusterItem.getVariableMessageSign().getDescription());
        nameTextView.setTextColor(0xff808080);
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        nameTextView.setGravity(GravityCompat.END);
        linearLayout.addView(nameTextView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return linearLayout;
    }

    @Override
    public float getAnchorY() {
        return 0.8f;
    }
}
