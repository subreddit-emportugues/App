package org.emportugues.aplicativo.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.emportugues.aplicativo.data.UserRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link org.emportugues.aplicativo.data.UserRepository}
 */
public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final UserRepository userRepository;

    public MainViewModelFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(userRepository);
    }

}
