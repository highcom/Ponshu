package com.highcom.ponshu.datamodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private MutableLiveData<List<BrandIdentifier>> mBrandNameList;
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

    public LiveData<List<BrandIdentifier>> getBrandIdentifierList() {
        if (mBrandNameList == null) {
            mBrandNameList = new MutableLiveData<>();
        }
        mCollectionRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<BrandIdentifier> brandIdentifierList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> brandData = (Map<String, Object>) document.getData();
                                brandIdentifierList.add(new BrandIdentifier(document.getId(), (String) brandData.get("title")));
                            }
                            mBrandNameList.setValue(brandIdentifierList);
                        } else {
                            Log.d("FIREBASE", task.getException().toString());
                        }
                    }
                });
        return mBrandNameList;
    }

    public LiveData<Brand> getBrand(String id) {
        if (mBrand == null) {
            mBrand = new MutableLiveData<>();
        }
        if (id == null) {
            Brand brand = new Brand("", "", "", 0L, "", "", "", 0L, 0L, "", new ArrayList<>(), new ArrayList<>());
        } else {
            mCollectionRef.document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> brandData = (Map<String, Object>) task.getResult().getData();
                                String title = (String) brandData.get("title");
                                String subTitle = (String) brandData.get("subTitle");
                                String specific = (String) brandData.get("specific");
                                Long polisingRate = (Long) brandData.get("polishingRate");
                                String brewery = (String) brandData.get("brewery");
                                String area = (String) brandData.get("area");
                                String rawMaterial = (String) brandData.get("rawMaterial");
                                Long capacity = (Long) brandData.get("capacity");
                                Long storageTemperature = (Long) brandData.get("storageTemperature");
                                String howToDrink = (String) brandData.get("howToDrink");
                                List<Long> tasteList = (List<Long>) brandData.get("tasteList");
                                List<Map<String, Object>> aromaRawList = (List<Map<String, Object>>) brandData.get("aromaList");
                                List<Aroma> aromaList = new ArrayList<>();
                                for (Map<String, Object> aromaRawData : aromaRawList) {
                                    Long elapsedCount = (Long) aromaRawData.get("elapsedCount");
                                    Long aromaLevel = (Long) aromaRawData.get("aromaLevel");
                                    Date elapsedDate = ((Timestamp) aromaRawData.get("elapsedDate")).toDate();
                                    Aroma aroma = new Aroma(elapsedCount, aromaLevel, elapsedDate);
                                    aromaList.add(aroma);
                                }
                                Brand brand = new Brand(title, subTitle, specific, polisingRate, brewery, area, rawMaterial, capacity, storageTemperature, howToDrink, tasteList, aromaList);
                                mBrand.setValue(brand);
                            } else {
                                Log.d("FIREBASE", task.getException().toString());
                            }
                        }
                    });
        }

        return mBrand;
    }

    public void updateBrand(String id, Brand brand) {
        if (id == null) {
            mCollectionRef.add(brand);
        } else {
            mCollectionRef.document(id).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                mCollectionRef.document(id).set(brand);
                            } else {
                                Log.d("FIREBASE", task.getException().toString());
                            }
                        }
                    });
        }
    }
}
