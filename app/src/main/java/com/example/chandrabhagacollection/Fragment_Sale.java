package com.example.chandrabhagacollection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Sale extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    Context context;
    Button btAdd;
    ProgressBar progressBar;
    EditText edtCatalogName, edtBrandName, edtQuantity, edtPrice;
    Spinner spinnerType;

    private List<String> listoccupation;
    ArrayList<Products> ProductList;

    private DatabaseReference mDatabaseRefproducts;
    private DatabaseReference mDatabase;
    String key,cat,type;

    public Fragment_Sale() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sale, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Products");
        }
        mDatabaseRefproducts = FirebaseDatabase.getInstance().getReference("Sales");
        ProductList = new ArrayList<>();


        edtCatalogName = (EditText) view.findViewById(R.id.catalog_name);
        edtBrandName = (EditText) view.findViewById(R.id.brand_name);
        edtQuantity = (EditText) view.findViewById(R.id.quantity);
        edtPrice = (EditText) view.findViewById(R.id.rate_per_peice);
        spinnerType = (Spinner) view.findViewById(R.id.spinner_type);

        btAdd = (Button) view.findViewById(R.id.btn_add_purchase);

        listoccupation = new ArrayList<>();
        listoccupation.add("Saree");
        listoccupation.add("Dress Material");
        listoccupation.add("Kurti");
        listoccupation.add("Leggins");

        ArrayAdapter<String> occupation = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, listoccupation);

        occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(occupation);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData();

            }
        });


        return view;
    }

    private void AddData() {
        key = mDatabaseRefproducts.push().getKey();
        Products product = new Products();
        product.setType(spinnerType.getSelectedItem().toString());
        product.setBrandName(edtBrandName.getText().toString());
        product.setCatalogName(edtCatalogName.getText().toString());
        product.setQuantity(edtQuantity.getText().toString());
        product.setPrice(edtPrice.getText().toString());

        mDatabaseRefproducts.child(key).setValue(product);

         type= spinnerType.getSelectedItem().toString();
         cat = edtCatalogName.getText().toString();
        String brand = edtBrandName.getText().toString();

        Query query3 = FirebaseDatabase.getInstance().getReference("Stock")
                .orderByChild("brandName")
                .equalTo(brand);
        query3.addValueEventListener(valueEventListener);
        Toast.makeText(getContext(), "Product Addred", Toast.LENGTH_SHORT).show();
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ProductList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Products subproducts1 = snapshot.getValue(Products.class);
                    if (subproducts1.getCatalogName().equalsIgnoreCase(cat) && subproducts1.getType().equalsIgnoreCase(type)) {
                        ProductList.add(subproducts1);
                    }

                }
                UpdateStock(ProductList);
                // subCatalogAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void UpdateStock(ArrayList<Products> productList) {
        Products products = productList.get(0);

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
