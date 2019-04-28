package com.example.laudr.horamarcada;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AtividadeService {

    @FormUrlEncoded
    @POST("lista_atividades.php")
    Call<List<Atividade>> getAtividade(@Field("id") String id);
}
