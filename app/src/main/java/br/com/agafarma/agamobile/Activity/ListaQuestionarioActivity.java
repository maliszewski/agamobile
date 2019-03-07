package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;

import java.text.ParseException;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.AgendaAdapter;
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.ToolBar;

public class ListaQuestionarioActivity extends AppCompatActivity {

    Toolbar barra;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<QuestionarioModel> data;
    private static ProgressDialog ringProgressDialog;
    private static int IdFormularioTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_questionario);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        IdFormularioTipo = 0;
        if (getIntent().getExtras().getString("idFormularioTipo") != null) {
            IdFormularioTipo = Integer.valueOf(getIntent().getExtras().getString("idFormularioTipo")).intValue();
        }


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /*
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (ringProgressDialog == null || !ringProgressDialog.isShowing()) {
                    ringProgressDialog = new Diverso().TempoTela(ListaQuestionarioActivity.this);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CarregaGrid(ListaQuestionarioActivity.this);
                            ListaQuestionarioActivity.ringProgressDialog.dismiss();
                        }
                    }).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        CarregaGrid(ListaQuestionarioActivity.this);
    }

    private void CarregaGrid(Context mContex) {
        try {
            QuestionarioModel questionarioModel = new QuestionarioModel();
            questionarioModel.Status = 2;
            questionarioModel.IdFormularioTipo = IdFormularioTipo;
            data = new QuestionarioDAO(this).Buscar(questionarioModel);
            if (data != null && data.size() > 0) {
                adapter = new AgendaAdapter(mContex, data);
                recyclerView.setAdapter(adapter);
            }
            else
            {
                new Dialog(ListaQuestionarioActivity.this).ShowAlertOK("Atenção", "Você não possui questionario...", true);
            }
            return;
        } catch (ParseException localParseException) {
            localParseException.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = false;
        if (ringProgressDialog == null)
            super.dispatchTouchEvent(ev);
        else
            result = !ringProgressDialog.isShowing() ? super.dispatchTouchEvent(ev) : true;

        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
