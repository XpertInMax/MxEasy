package com.gaf.android.mxeasy.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gaf.android.mxeasy.R;
import com.gaf.android.mxeasy.maximoobjects.PO;

import java.util.ArrayList;

/**
 * Created by Amani on 22-12-2017.
 */

public class MxObjectAdapter extends ArrayAdapter<PO> {


    public MxObjectAdapter(@NonNull Context context, @NonNull ArrayList<PO> pos) {
        super(context, 0, pos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View mxObjectListView = convertView;

        if(mxObjectListView == null){
            mxObjectListView = LayoutInflater.from(getContext()).inflate(R.layout.list_mxobject, parent, false);
        }

        PO po = getItem(position);

        TextView textView1 = mxObjectListView.findViewById(R.id.textAttr1);
        textView1.setText(po.getmPONum());

        TextView textView2 = mxObjectListView.findViewById(R.id.textAttr2);
        textView2.setText(po.getmDescription());

        TextView textView6 = mxObjectListView.findViewById(R.id.textAttr6);
        textView6.setText("Rev " + po.getmVendor());

        TextView textView3 = mxObjectListView.findViewById(R.id.textAttr3);
        textView3.setText(po.getmStatus());

        TextView textView4 = mxObjectListView.findViewById(R.id.textAttr4);
        textView4.setText(po.getmPurchaseAgent());

        TextView textView5 = mxObjectListView.findViewById(R.id.textAttr5);
        textView5.setText(po.getmTotalCost().toString());

        return mxObjectListView;
    }
}
