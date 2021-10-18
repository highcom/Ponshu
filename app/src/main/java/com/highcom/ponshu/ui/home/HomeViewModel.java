package com.highcom.ponshu.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.highcom.ponshu.datamodel.PonshuRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private PonshuRepository mRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mRepository = PonshuRepository.getInstance();
    }

    public LiveData<List<String>> getBrandNameList() {
        return mRepository.getBrandNameList();
    }
}