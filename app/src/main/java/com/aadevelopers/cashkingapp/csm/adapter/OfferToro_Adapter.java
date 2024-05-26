package com.aadevelopers.cashkingapp.csm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.aadevelopers.cashkingapp.R;
import com.aadevelopers.cashkingapp.csm.model.OfferToro_Model;
import com.aadevelopers.cashkingapp.csm.topsheet.OfferDialog;
import com.aadevelopers.cashkingapp.helper.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;


public class OfferToro_Adapter extends RecyclerView.Adapter<OfferToro_Adapter.ViewHolder> {
    public List<OfferToro_Model> offer;
    public Context context;
    Picasso picasso;
    int check;

    public OfferToro_Adapter(List<OfferToro_Model> historyList, Context context, int check) {
        this.offer = historyList;
        this.context = context;
        this.check = check;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offertoro, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OfferToro_Model offer_list = offer.get(position);

        Glide.with(context).load(offer_list.getImage_url())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(holder.img);

        holder.offer_name.setText(offer_list.getOffer_name());
        holder.offer_desc.setText(offer_list.getOffer_desc());
        holder.amount.setText(offer_list.getAmount() + "+");
        if (offer_list.getPayout_type().equals("cpi")) {
            holder.install_txt.setText("Install");
        } else {
            holder.install_txt.setText("GET");
        }

        holder.click.setOnClickListener(view -> {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            OfferDialog newFragment = new OfferDialog();
            Bundle args = new Bundle();
            args.putString("Offer_name", offer_list.getOffer_name());
            args.putString("offer_disc", offer_list.getOffer_desc());
            args.putString("disclaimer", offer_list.getDisclaimer());
            args.putString("amount", offer_list.getAmount());
            args.putString("Image", offer_list.getImage_url());
            String string = offer_list.getOffer_url_easy();
            String ur = string.replace("[USER_ID]", AppController.getInstance().getId());
            args.putString("url", ur);
            newFragment.setArguments(args);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        });


    }

    @Override
    public int getItemCount() {
        if (check == 0) {
            if (offer.size() <= 4) {
                return offer.size();
            } else {
                return 5;
            }
        } else {
            return offer.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView offer_name, offer_desc, amount, install_txt;
        ImageView img;
        LinearLayout click;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            offer_desc = itemView.findViewById(R.id.offer_desc);
            offer_name = itemView.findViewById(R.id.offer_name);
            amount = itemView.findViewById(R.id.amount);
            install_txt = itemView.findViewById(R.id.install_txt);
            click = itemView.findViewById(R.id.click);
        }
    }
}


