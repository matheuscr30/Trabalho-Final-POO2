package com.matheus.magazinehenrique.tools;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by matheus on 17/10/17.
 */

public interface ViaCepServices {
    @GET("ws/{cep}/json/")
    Call<RepoCEP> listRepos(@Path("cep") String cep);
}
