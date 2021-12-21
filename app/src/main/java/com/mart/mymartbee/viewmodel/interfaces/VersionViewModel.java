package com.mart.mymartbee.viewmodel.interfaces;

import androidx.lifecycle.LiveData;
import com.mart.mymartbee.model.CommonResponseModel;
import java.util.Map;

public interface VersionViewModel {

    LiveData<CommonResponseModel> getVersionLD();
    LiveData<String> getVersionError();
    LiveData<Boolean> progressVersionUpdate();

    void getVersions(Map<String, String> params);
}
