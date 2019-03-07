package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.AvisoAdapter;
import br.com.agafarma.agamobile.Adapter.FotoAdapter;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.QuestionarioPerguntaRespostaDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaFotoDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.Image;
import br.com.agafarma.agamobile.Util.ToolBar;

public class ListaFotoActivity extends AppCompatActivity {

    Toolbar barra;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static List<String> data;
    private static Button btnEnviar;
    public static Integer idQuestionarioPergunta;
    public static Integer idQuestionario;
    public static ProgressDialog ringProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_foto);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);
        btnEnviar = (Button) findViewById(R.id.foto_enviar);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager g = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(g);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringProgressDialog = new Diverso().TempoTela(ListaFotoActivity.this);
                new Thread(new Runnable() {

                    public void run() {

                        int i = 0;
                        QuestionarioRespostaFotoModel foto = new QuestionarioRespostaFotoModel();
                        foto.IdQuestionarioPergunta = idQuestionarioPergunta;
                        new QuestionarioRespostaFotoDAO(ListaFotoActivity.this).Deletar(foto);

                        for (String item : data) {

                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(Uri.parse(item));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap image = BitmapFactory.decodeStream(imageStream);
                            foto = new QuestionarioRespostaFotoModel();
                            foto.IdQuestionario = idQuestionario;
                            foto.IdQuestionarioPergunta = idQuestionarioPergunta;
                            foto.Imagem = image;
                            foto.IdUsuario = SessionModel.Usuario.UsuarioId;
                            foto.UriImagem = item;

                            new QuestionarioRespostaFotoDAO(ListaFotoActivity.this).Inserir(foto);


                        }

                        ListaFotoActivity.ringProgressDialog.dismiss();
                        finish();
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (data != null && data.size() > 0) {
            adapter = new FotoAdapter(getApplicationContext(), data);
            recyclerView.setAdapter(adapter);
        } else {
            new Dialog(ListaFotoActivity.this).ShowAlertOK("Atenção", "Você não selecionou nenhuma foto...", true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
