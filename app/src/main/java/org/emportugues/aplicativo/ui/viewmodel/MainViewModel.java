package org.emportugues.aplicativo.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.emportugues.aplicativo.data.UserRepository;
import org.emportugues.aplicativo.data.database.entity.User;
import org.emportugues.aplicativo.model.ServiceRequest;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final UserRepository mRepository;
    private final LiveData<List<User>> mUserData;

    public MainViewModel(UserRepository mRepository) {
        this.mRepository = mRepository;
        this.mUserData = mRepository.getUserList();
    }

    public LiveData<List<User>> getUserList() {
        return mUserData;
    }

    public void postRequest(ServiceRequest serviceRequest) {
        mRepository.postServiceRequest(serviceRequest);
    }

}
