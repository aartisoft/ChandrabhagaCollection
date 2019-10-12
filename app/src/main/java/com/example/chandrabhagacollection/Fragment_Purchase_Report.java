package com.example.chandrabhagacollection;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.chandrabhagacollection.Constant.CALANDER_DATE_FORMATE;


public class Fragment_Purchase_Report extends Fragment {

    // NOTE: Removed Some unwanted Boiler Plate Codes
    private OnFragmentInteractionListener mListener;

    Context context;
    ProgressBar progressBar;
    ArrayList<Products> ProductList;
    RecyclerView ProductRecycler;
    ProgressBar progress;
    EditText FromDate, ToDate;
    int fromYear, fromMonth, fromDay;
    int toYear, toMonth, toDay;
    long fromDate, toDate;

    private DatabaseReference mDatabaseRefproducts;
    private DatabaseReference mDatabase;
    String key;
    ProductAdapter catalogAdapter;

    public Fragment_Purchase_Report() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_purchase_report, container, false);

        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // ie we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Products");
        }

        ProductList = new ArrayList<>();

        FromDate = (EditText) view.findViewById(R.id.edittextfromdate);
        ToDate = (EditText) view.findViewById(R.id.edittexttodate);
        ProductRecycler = (RecyclerView) view.findViewById(R.id.catalog_recycle);
        ProductRecycler.setHasFixedSize(true);
        ProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        progress = (ProgressBar) view.findViewById(R.id.catalog_progress);

        progress.setVisibility(View.VISIBLE);
        mDatabaseRefproducts = FirebaseDatabase.getInstance().getReference("Products");
        mDatabaseRefproducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProductList.clear();
                for (DataSnapshot mainproductSnapshot : dataSnapshot.getChildren()) {

                    Products mainProducts = mainproductSnapshot.getValue(Products.class);
                    ProductList.add(mainProducts);


                }
                catalogAdapter = new ProductAdapter(getActivity(),ProductList);
                ProductRecycler.setAdapter(catalogAdapter);
                progress.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getStock();

        setFromDateClickListner();
        setToDateClickListner();

        return view;
    }

    private void getStock() {

    }

    private void setToDateClickListner() {
        setToCurrentDate();
        ToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
                        ToDate.setText(sdf.format(myCalendar.getTime()));
                        toDay = selectedday;
                        toMonth = selectedmonth;
                        toYear = selectedyear;
                        toDate = myCalendar.getTimeInMillis();
                        filterByDate(ProductList);
                    }
                }, toYear, toMonth, toDay);
                mDatePicker.show();
            }
        });
    }
    private void setFromCurrentDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        fromYear = mcurrentDate.get(Calendar.YEAR);
        fromMonth = mcurrentDate.get(Calendar.MONTH);
        fromDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    private void setFromDateClickListner() {
        setFromCurrentDate();
        FromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        SimpleDateFormat sdf = new SimpleDateFormat(CALANDER_DATE_FORMATE, Locale.FRANCE);
                        String formatedDate = sdf.format(myCalendar.getTime());
                        FromDate.setText(formatedDate);
                        fromDay = selectedday;
                        fromMonth = selectedmonth;
                        fromYear = selectedyear;
                        fromDate = Utility.convertFormatedDateToMilliSeconds(formatedDate, CALANDER_DATE_FORMATE);
                        filterByDate(ProductList);
                    }
                }, fromYear, fromMonth, fromDay);
                mDatePicker.show();
            }
        });

    }

    private void setToCurrentDate() {
        toDate = System.currentTimeMillis();
        Calendar mcurrentDate = Calendar.getInstance();
        toYear = mcurrentDate.get(Calendar.YEAR);
        toMonth = mcurrentDate.get(Calendar.MONTH);
        toDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
    }


    private void filterByDate(ArrayList<Products> leedsModelArrayList) {
        try {
            ArrayList<Products> filterArrayList = new ArrayList<>();
            if (leedsModelArrayList != null) {
                if (fromDate > 0) {
                    for (Products leedsModel : leedsModelArrayList) {
                        if (leedsModel.getCreatedDateTimeLong() >= fromDate && leedsModel.getCreatedDateTimeLong() <= toDate) {
                            filterArrayList.add(leedsModel);
                        }
                    }
                } else {
                    for (Products leedsModel : leedsModelArrayList) {
                        if (leedsModel.getCreatedDateTimeLong() <= toDate) {
                            filterArrayList.add(leedsModel);
                        }
                    }
                }
            }
            serAdapter(filterArrayList);
        } catch (Exception e) {
            ExceptionUtil.logException(e);
        }
    }

    private void serAdapter(ArrayList<Products> leedsModels) {
        if (leedsModels != null) {
            if (catalogAdapter == null) {
                catalogAdapter = new ProductAdapter(getActivity(), leedsModels);
                ProductRecycler.setAdapter(catalogAdapter);

            } else {
                ArrayList<Products> leedsModelArrayList = new ArrayList<>();
                leedsModelArrayList.addAll(leedsModels);
                catalogAdapter.reload(leedsModelArrayList);
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
