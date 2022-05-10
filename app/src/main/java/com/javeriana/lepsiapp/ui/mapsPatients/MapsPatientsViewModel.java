package com.javeriana.lepsiapp.ui.mapsPatients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapsPatientsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MapsPatientsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}