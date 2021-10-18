package com.highcom.ponshu.datamodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PonshuRepository {
    private static PonshuRepository mInstance;

    private FirebaseFirestore mDb;
    private CollectionReference mCollectionRef;

    private MutableLiveData<List<String>> mBrandNameList;
    private MutableLiveData<Brand> mBrand;

    public static PonshuRepository getInstance() {
        if (mInstance == null) {
            mInstance = new PonshuRepository();
        }
        return mInstance;
    }

    private PonshuRepository() {
        mDb = FirebaseFirestore.getInstance();
        mCollectionRef = mDb.collection("BrandList");
    }

    public LiveData<List<String>> getBrandNameList() {
        if (mBrandNameList == null) {
            mBrandNameList = new MutableLiveData<>();
        }
        mCollectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> brandNameList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> brandData = (Map<String, Object>) document.getData();
                                brandNameList.add((String)brandData.get("title"));
                            }
                            mBrandNameList.setValue(brandNameList);
                        } else {
                            Log.d("FIREBASE", task.getException().toString());
                        }
                    }
                });
        return mBrandNameList;
    }

    public LiveData<Brand> getBrand(String name) {
        if (mBrand == null) {
            mBrand = new MutableLiveData<>();
        }
        mCollectionRef.whereEqualTo("title", name).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()) return;
                            Map<String, Object> brandData = (Map<String, Object>) task.getResult().getDocuments().get(0).getData();
                            String title = (String) brandData.get("title");
                            String subTitle = (String) brandData.get("subTitle");
                            Long polisingRate = (Long) brandData.get("polishingRate");
                            List<Map<String, Object>> aromaRawList = (List<Map<String, Object>>) brandData.get("aromaList");
                            List<Aroma> aromaList = new ArrayList<>();
                            for (Map<String, Object> aromaRawData : aromaRawList) {
                                Long elapsedCount = (Long) aromaRawData.get("elapsedCount");
                                Long aromaLevel = (Long) aromaRawData.get("aromaLevel");
                                Date elapsedDate = ((Timestamp)aromaRawData.get("elapsedDate")).toDate();
                                Aroma aroma = new Aroma(elapsedCount, aromaLevel, elapsedDate);
                                aromaList.add(aroma);
                            }
                            Brand brand = new Brand(title, subTitle, polisingRate, aromaList);
                            mBrand.setValue(brand);
                        } else {
                            Log.d("FIREBASE", task.getException().toString());
                        }
                    }
                });

        return mBrand;
    }

    public void updateBrand(Brand brand) {
        mCollectionRef.whereEqualTo("title", brand.getTitle()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()) {
                                mCollectionRef.add(brand);
                            } else {
                                String id = task.getResult().getDocuments().get(0).getId();
                                mCollectionRef.document(id).set(brand);
                            }
                        } else {
                            Log.d("FIREBASE", task.getException().toString());
                        }
                    }
        });
    }
}
