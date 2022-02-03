package com.rajan.movienepal.utility;


import com.rajan.movienepal.model.ApiError;
import com.rajan.movienepal.retrofit.ApiClient;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

   public  static final ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = ApiClient.retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError error = null;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return error;
    }
}
