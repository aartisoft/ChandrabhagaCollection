package com.example.chandrabhagacollection;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


public class Fragment_Sell_Product extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    Context context;
    ProgressBar progressBar;
    ArrayList<Products> ProductList;
    ArrayList<Products> leedsArraylist;
    ArrayList<Products> leedsArraylist1;
    RecyclerView ProductRecycler;
    ProgressBar progress;

    private DatabaseReference mDatabaseRefproducts;
    private DatabaseReference mDatabase;
    String key;
    EditText Search;
    LeedRepository leedRepository;
    private Sell_Product_Adapter adapter;

    public Fragment_Sell_Product() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sell_product_layout, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Products");
        }

        ProductList = new ArrayList<>();
        leedsArraylist = new ArrayList<>();
        leedsArraylist1 = new ArrayList<>();

        leedRepository = new LeedRepositoryImpl();


        ProductRecycler = (RecyclerView) view.findViewById(R.id.catalog_recycle);
        ProductRecycler.setHasFixedSize(true);
        ProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        progress = (ProgressBar) view.findViewById(R.id.catalog_progress);
        Search = (EditText) view.findViewById(R.id.search);

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
                Sell_Product_Adapter catalogAdapter = new Sell_Product_Adapter(getActivity(),ProductList);
                ProductRecycler.setAdapter(catalogAdapter);
                progress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
//                    setAdapter(s.toString());
                    setAdapter(s.toString());
                } else {
                    /*
                     * Clear the list when editText is empty
                     * */
                    leedsArraylist1.clear();
                    ProductRecycler.removeAllViews();
                }

            }
        });


        return view;
    }


    private void setAdapter(final String toString) {
        leedsArraylist1.clear();
        leedsArraylist.clear();
        leedRepository.readAllStock(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    leedsArraylist = (ArrayList<Products>) object;
                    for (int i = 0; i < leedsArraylist.size(); i++) {
                        Products leed = leedsArraylist.get(i);
                        try {
                            if (leed.getType().toLowerCase().contains(toString)) {
                                leedsArraylist1.add(leed);
                            } else if (leed.getBrandName().toLowerCase().contains(toString)) {
                                leedsArraylist1.add(leed);
                            }
                        } catch (Exception e) {
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                serAdapter(leedsArraylist1);
            }

            @Override
            public void onError(Object object) {
                Utility.showLongMessage(getActivity(), getString(R.string.server_error));
            }
        });
    }

    private void serAdapter(ArrayList<Products> reportmodels) {
        if (reportmodels != null) {
            if (adapter == null) {
                adapter = new Sell_Product_Adapter(getContext(), reportmodels);
                ProductRecycler.setAdapter(adapter);
                ProductRecycler.setHasFixedSize(true);
                ProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                //onClickListner();
            } else {
                ArrayList<Products> leedsModelArrayList = new ArrayList<>();
                leedsModelArrayList.addAll(reportmodels);
//                adapter.reload(leedsModelArrayList);
                adapter = new Sell_Product_Adapter(getContext(), reportmodels);
                ProductRecycler.setAdapter(adapter);
            }
        }
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
