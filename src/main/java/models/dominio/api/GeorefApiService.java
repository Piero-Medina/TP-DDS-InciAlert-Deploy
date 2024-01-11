package models.dominio.api;
import models.dominio.api.mapeo.ListaDepartamentos;
import models.dominio.api.mapeo.ListaMunicipios;
import models.dominio.api.mapeo.ListaProvincias;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeorefApiService {


    @GET("provincias")
    Call<ListaProvincias> provincias();

    @GET("provincias")
    Call<ListaProvincias> provincias(@Query("campos") String campos);

    @GET("municipios")
    Call<ListaMunicipios> municipios(@Query("provincia") int idProvincia);

    @GET("municipios")
    Call<ListaMunicipios> municipios(@Query("provincia") int idProvincia, @Query("campos") String campos);

    @GET("municipios")
    Call<ListaMunicipios> municipios(@Query("provincia") int idProvincia, @Query("campos") String campos, @Query("max") int max);

    @GET("ubicacion")
    Call<UbicacionResponse> obtenerUbicacion(@Query("lat") double latitud, @Query("lon") double longitud);

    @GET("departamentos")
    Call<ListaDepartamentos> departamentos();

    @GET("departamentos")
    Call<ListaDepartamentos> departamentosPor(@Query("provincia") String nombreProvincia);

    @GET("departamentos")
    Call<ListaDepartamentos> departamentosPor(@Query("provincia") int idProvincia);

    @GET("municipios")
    Call<ListaMunicipios> municipiosPor(@Query("provincia") String nombreProvincia);

    @GET("municipios")
    Call<ListaMunicipios> municipiosPor(@Query("provincia") int idProvincia);
}
