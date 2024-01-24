package com.elmahousingfinanceug_test.launch.rao.ocr;

import com.example.icebergocr.utils.OCRData;
import com.google.gson.Gson;

public class OCRTypeConverter {

    public String covert(OCRData ocrData) {
        if (ocrData == null) {
            return (null);
        }
        return new Gson().toJson(ocrData, OCRData.class);
    }

    public OCRData covert(String data) {
        if (data == null) {
            return (null);
        }
        return new Gson().fromJson(data, OCRData.class);
    }
}
