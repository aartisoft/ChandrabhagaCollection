package com.example.chandrabhagacollection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_Stock extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    Context context;
    ProgressBar progressBar;
    ArrayList<Products> ProductList;
    RecyclerView ProductRecycler;
    ProgressBar progress;

    private DatabaseReference mDatabaseRefproducts;
    private DatabaseReference mDatabase;
    String key;

    public Fragment_Stock() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stock, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Products");
        }

        ProductList = new ArrayList<>();

        ProductRecycler = (RecyclerView) view.findViewById(R.id.catalog_recycle);
        ProductRecycler.setHasFixedSize(true);
        ProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        progress = (ProgressBar) view.findViewById(R.id.catalog_progress);

        progress.setVisibility(View.VISIBLE);
        mDatabaseRefproducts = FirebaseDatabase.getInstance().getReference("Stock");
        mDatabaseRefproducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProductList.clear();
                for (DataSnapshot mainproductSnapshot : dataSnapshot.getChildren()) {

                    Products mainProducts = mainproductSnapshot.getValue(Products.class);
                    ProductList.add(mainProducts);


                }
                ProductAdapter catalogAdapter = new ProductAdapter(getActivity(),ProductList);
                ProductRecycler.setAdapter(catalogAdapter);
                progress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getStock();


        return view;
    }

    private void getStock() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // NOTE: This is the part that usually gives you the error
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
