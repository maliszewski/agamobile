package br.com.agafarma.agamobile.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.RespostaAdapter;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaRespostaModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.ToolBar;

public class AvisoActivity extends AppCompatActivity {

    private static int IdAviso;
    private TextView pTitulo;
    private TextView pDescricao;
    private static String TAG = "AvisoActivity";
    private AvisoModel aviso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);
        pTitulo = (TextView) findViewById(R.id.aviso_titulo);
        pDescricao = (TextView) findViewById(R.id.aviso_descricao);
        IdAviso = 0;
        if (getIntent().getExtras().getString("idAviso") != null) {
            IdAviso = Integer.valueOf(getIntent().getExtras().getString("idAviso")).intValue();
        }
        aviso = new AvisoModel();
        aviso.IdAviso = IdAviso;
        try {
            aviso = new AvisoDAO(this).Buscar(aviso).get(0);
            aviso.Tipo = 3;
            aviso.DataCadastro = new Date();
            aviso.IdUsuario = SessionModel.Usuario.UsuarioId;

            pTitulo.setText(aviso.Titulo);
            pDescricao.setText(aviso.Descricao);
            new AvisoDAO(this).Atualizar(aviso);

            barra.setTitle("Aviso");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


}
