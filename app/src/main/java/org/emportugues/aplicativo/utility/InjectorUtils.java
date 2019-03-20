package org.emportugues.aplicativo.utility;

import android.content.Context;

import org.emportugues.aplicativo.AppExecutors;
import org.emportugues.aplicativo.data.UserRepository;
import org.emportugues.aplicativo.data.database.UserDao;
import org.emportugues.aplicativo.data.database.UserDatabase;
import org.emportugues.aplicativo.data.network.UserNetworkDataSource;
import org.emportugues.aplicativo.ui.viewmodel.MainViewModelFactory;

public class InjectorUtils {

    public static UserRepository getRepository(Context context) {
        // Get all we need
        UserDao userDao = UserDatabase.getInstance(context).userDao();
        AppExecutors executors = AppExecutors.getInstance();
        UserNetworkDataSource networkDataSource = UserNetworkDataSource.getInstance(executors);

        return UserRepository.getInstance(userDao, networkDataSource, executors);
    }

    public static MainViewModelFactory getMainViewModelFactory(Context context) {
        UserRepository repository = getRepository(context);
        return new MainViewModelFactory(repository);
    }

}
