package br.com.agafarma.agamobile.Interface;

import java.util.List;

import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.BoletoModel;
import br.com.agafarma.agamobile.Model.CampanhaModel;
import br.com.agafarma.agamobile.Model.CircularModel;
import br.com.agafarma.agamobile.Model.ContatoChatModel;
import br.com.agafarma.agamobile.Model.LojaModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
import br.com.agafarma.agamobile.Model.TituloModel;
import br.com.agafarma.agamobile.Model.UsuarioModel;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestFull {

    @GET("/usuario/{versao}")
    public abstract Call<UsuarioModel> GetLogin(@Path("versao") String versao);

    @GET("/questionario/buscar")
    public abstract Call<List<QuestionarioModel>> GetQuestionario();

    @POST("/questionario/InserirResposta/{listaresposta}")
    public abstract Call<Boolean> PostResposta(@Body List<QuestionarioRespostaModel> listaQuestionario);

    @POST("/questionario/InserirFoto/{listaresposta}")
    public abstract Call<Boolean> PostFoto(@Body List<QuestionarioRespostaFotoModel> listaQuestionarioRespostaFoto);

    @GET("/aviso/buscar")
    public abstract Call<List<AvisoModel>> GetAviso();

    @POST("/aviso/avisoleitura/{listaresposta}")
    public abstract Call<Boolean> PostAvisoResposta(@Body List<AvisoModel> listaAviso);

    @GET("/circular/buscarnaolida/{idloja}")
    public abstract Call<List<CircularModel>> GetCircularNaoLida(@Path("idloja") int idLoja);

    @POST("/circular/lidasalvar/{idcircular}")
    public abstract Call<Boolean> PostCircularLida(@Path("idcircular") int idCircular);

    @GET("/questionario/cancelado")
    public abstract Call<List<QuestionarioModel>> GetQuestionarioCancelado();

    @Multipart
    @POST("questionario/uploadimagepergunta/{idquestionario}/{idquestionariopergunta}")
    Call<ResponseBody> UpLoadImagePregunta(@Path("idquestionario") int idQuestionario, @Path("idquestionariopergunta") int idQuestionarioPergunta, @Part MultipartBody.Part file);

    @GET("/loja/buscar/{idloja}")
    public abstract Call<LojaModel> GetLoja(@Path("idloja") int idLoja);

    @POST("/loja/atualizar")
    public abstract Call<Boolean> PostLoja(@Body LojaModel loja);

    @GET("/usuario/chat/listacontato")
    public abstract Call<List<ContatoChatModel>> GetContato();

    @GET("/financeiro/listarboleto/{idloja}")
    public abstract Call<List<TituloModel>> GetListaTitulo(@Path("idloja") int idLoja);

    @GET("/financeiro/buscarboleto/{idsequencia}/{idloja}")
    public abstract Call<BoletoModel> GetBoleto(@Path("idsequencia") int idSequencia, @Path("idloja") int idLoja);

    @GET("/financeiro/buscarboletoagacredi/{idloja}")
    public abstract Call<BoletoModel> GetBoletoAgaCredi(@Path("idloja") int idLoja);

    @GET("/campanha/listarhome/{idloja}")
    public abstract Call<List<CampanhaModel>> GetCampanha(@Path("idloja") int idLoja);


}
