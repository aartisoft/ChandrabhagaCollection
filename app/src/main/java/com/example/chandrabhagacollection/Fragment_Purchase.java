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

import androidx.fragment.app.Fragment;

import com.example.chandrabhagacollection.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Purchase extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    Context context;
    Button btAdd;
    ProgressBar progressBar;
    EditText edtCatalogName, edtBrandName, edtQuantity, edtPrice;
    Spinner spinnerType;

    private List<String> listoccupation;

    private DatabaseReference mDatabaseRefproducts, mDatabaseRefstock;
    private DatabaseReference mDatabase;
    String key, key1;

    public Fragment_Purchase() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Products");
        }
        mDatabaseRefproducts = FirebaseDatabase.getInstance().getReference("Products");
        mDatabaseRefstock = FirebaseDatabase.getInstance().getReference("Stock");

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
        key1 = mDatabaseRefstock.push().getKey();
        Products product = new Products();
        product.setType(spinnerType.getSelectedItem().toString());
        product.setBrandName(edtBrandName.getText().toString());
        product.setCatalogName(edtCatalogName.getText().toString());
        product.setQuantity(edtQuantity.getText().toString());
        product.setPrice(edtPrice.getText().toString());
        product.setProductId(key1);

        mDatabaseRefproducts.child(key).setValue(product);
        mDatabaseRefstock.child(key1).setValue(product);
        Toast.makeText(getContext(), "Product Addred", Toast.LENGTH_SHORT).show();
        edtCatalogName.setText("");
        edtBrandName.setText("");
        edtQuantity.setText("");
        edtPrice.setText("");
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
