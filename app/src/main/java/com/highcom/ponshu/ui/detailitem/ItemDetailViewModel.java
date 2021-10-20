package com.highcom.ponshu.ui.detailitem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.highcom.ponshu.datamodel.Brand;
import com.highcom.ponshu.datamodel.PonshuRepository;

public class ItemDetailViewModel extends AndroidViewModel {
    private PonshuRepository mRepository;

    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = PonshuRepository.getInstance();
    }

    public LiveData<Brand> getBrand(String id) {
        return mRepository.getBrand(id);
    }

    public void updateBrand(String id, Brand brand) {
        mRepository.updateBrand(id, brand);
    }
}
