package com.snick.zzj.t_reader.model;

import com.snick.zzj.t_reader.model.beans.WelcomeImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by zzj on 17-1-22.
 */

public interface RequestServices {
    @GET("{resolution}")
    Call<WelcomeImage> getWelcomeImage(@Path("resolution") String resolution);
// you can add some other meathod
}
