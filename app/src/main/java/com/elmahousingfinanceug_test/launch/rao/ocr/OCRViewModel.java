package com.elmahousingfinanceug_test.launch.rao.ocr;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OCRViewModel extends ViewModel {


    private MutableLiveData<String> isImageFront = new MutableLiveData<>();
    private MutableLiveData<String> isImageBack = new MutableLiveData<>();



    public MutableLiveData<String> getIsImageFront() {
        return isImageFront;
    }

    public void setIsImageFront(String imageFront) {
        isImageFront.setValue(imageFront);
    }

    public MutableLiveData<String> getIsImageBack() {
        return isImageBack;
    }

    public void setIsImageBack(String imageBack) {
        isImageBack.setValue(imageBack);
    }

}
