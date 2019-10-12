package com.example.chandrabhagacollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.chandrabhagacollection.Constant.GLOBAL_DATE_FORMATE;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Products> uploads;

//    public ProductAdapter(Context context, ArrayList<Products> uploads) {
//        this.uploads = uploads;
//        this.context = context;
//    }

    public ProductAdapter(Context context,ArrayList<Products> mUsers) {

        this.uploads = mUsers;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productadapter_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Products user = uploads.get(position);

        holder.textViewName.setText(user.getBrandName());
        holder.textViewMobile.setText(user.getCatalogName());
        holder.textViewAddress.setText(user.getType());
        holder.textViewPinCode.setText(user.getQuantity());
        holder.textViewId.setText(Utility.convertMilliSecondsToFormatedDate(user.getCreatedDateTimeLong(), GLOBAL_DATE_FORMATE));

        holder.userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewMobile;
        public TextView textViewAddress;
        public TextView textViewPinCode;
        public TextView textViewId;
        public CardView userCard;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.namevalue);
            textViewMobile = (TextView) itemView.findViewById(R.id.user_mobilevalue);
            textViewAddress = (TextView) itemView.findViewById(R.id.user_addressvalue);
            textViewPinCode = (TextView) itemView.findViewById(R.id.user_pincodevalue);
            textViewId = (TextView) itemView.findViewById(R.id.user_idvalue);
            userCard = (CardView) itemView.findViewById(R.id.card_userid);

        }
    }

    public void reload(ArrayList<Products> leedsModelArrayList) {
        uploads.clear();
        uploads.addAll(leedsModelArrayList);
        notifyDataSetChanged();
    }
}
