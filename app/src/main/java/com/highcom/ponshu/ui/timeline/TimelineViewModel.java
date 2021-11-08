package com.highcom.ponshu.ui.timeline;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.highcom.ponshu.datamodel.BrandIdentifier;
import com.highcom.ponshu.datamodel.PonshuRepository;

import java.util.List;

public class TimelineViewModel extends AndroidViewModel {
    private PonshuRepository mRepository;

    public TimelineViewModel(@NonNull Application application) {
        super(application);
        mRepository = PonshuRepository.getInstance();
    }

    public LiveData<List<BrandIdentifier>> getBrandIdentifierList() {
        return mRepository.getBrandIdentifierList();
    }
}