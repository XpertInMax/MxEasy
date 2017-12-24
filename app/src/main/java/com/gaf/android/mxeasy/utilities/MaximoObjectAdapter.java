package com.gaf.android.mxeasy.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaf.android.mxeasy.R;
import com.gaf.android.mxeasy.maximoobjects.PO;

import java.util.ArrayList;

/**
 * Created by Amani on 23-12-2017.
 */

public class MaximoObjectAdapter extends RecyclerView.Adapter<MaximoObjectAdapter.MaximoObjectAdapterViewHolder>{

    private ArrayList<PO> mMaximoPOList;


    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MaximoObjectAdapterOnClickHandler mforecastOnClickHandler;


    /**
     * The interface that receives onClick messages.
     */
    public interface MaximoObjectAdapterOnClickHandler {
        void onClick(PO po);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param maximoObjectAdapterOnClickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MaximoObjectAdapter(MaximoObjectAdapterOnClickHandler maximoObjectAdapterOnClickHandler){
        this.mforecastOnClickHandler = maximoObjectAdapterOnClickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MaximoObjectAdapter.MaximoObjectAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = layoutInflater.inflate(R.layout.list_mxobject, viewGroup, shouldAttachToParentImmediately);
        MaximoObjectAdapterViewHolder maximoObjectAdapterViewHolder = new MaximoObjectAdapterViewHolder(view);
        return maximoObjectAdapterViewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param maximoObjectAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MaximoObjectAdapterViewHolder maximoObjectAdapterViewHolder, int position) {
        PO fetPODataAtPosition = mMaximoPOList.get(position);
        maximoObjectAdapterViewHolder.textView1.setText(fetPODataAtPosition.getmPONum());
        maximoObjectAdapterViewHolder.textView2.setText(fetPODataAtPosition.getmDescription());
        maximoObjectAdapterViewHolder.textView3.setText(fetPODataAtPosition.getmStatus());
        maximoObjectAdapterViewHolder.textView4.setText(fetPODataAtPosition.getmPurchaseAgent());
        maximoObjectAdapterViewHolder.textView5.setText(fetPODataAtPosition.getmTotalCost().toString());
        maximoObjectAdapterViewHolder.textView6.setText(fetPODataAtPosition.getmVendor());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if(mMaximoPOList == null){
            return 0;
        }
        return mMaximoPOList.size();
    }

    public class MaximoObjectAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView textView1;
        public final TextView textView2;
        public final TextView textView3;
        public final TextView textView4;
        public final TextView textView5;
        public final TextView textView6;

        public MaximoObjectAdapterViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textAttr1);
            textView2 = itemView.findViewById(R.id.textAttr2);
            textView3 = itemView.findViewById(R.id.textAttr3);
            textView4 = itemView.findViewById(R.id.textAttr4);
            textView5 = itemView.findViewById(R.id.textAttr5);
            textView6 = itemView.findViewById(R.id.textAttr6);
        }


        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            PO poWhichIsClicked = mMaximoPOList.get(adapterPosition);
            mforecastOnClickHandler.onClick(poWhichIsClicked);
        }
    }
}
