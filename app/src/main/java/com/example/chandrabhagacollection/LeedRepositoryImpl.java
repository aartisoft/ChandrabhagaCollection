package com.example.chandrabhagacollection;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Map;

public class LeedRepositoryImpl extends FirebaseTemplateRepository implements LeedRepository {


    @Override
    public void updateLeed(String leedId, Map leedsMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constant.STOCK_TABLE_REF.child(leedId);
        fireBaseUpdateChildren(databaseReference, leedsMap, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }
}
