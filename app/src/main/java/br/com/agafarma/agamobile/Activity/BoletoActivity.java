package br.com.agafarma.agamobile.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.BoletoAdapter;
import br.com.agafarma.agamobile.Adapter.PerfilAdapter;
import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.TituloModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.FinanceiroApi;

public class BoletoActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "BoletoActivity";
    private static ProgressDialog ringProgressDialog;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static List<MenuModel> data;
    private static String TAG = "BoletoActivity";
    private static RecyclerView recyclerView;

    private static final int COOPROFAR = 2;
    private static final int AGAFARMA = 1;


    private static final int COOPROFARAGAFARMA = 1;
    private static final int AGACARD = 2;
    private static final int AGACREDI = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boleto);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager g = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(g);
        Carregar();

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

    private void Carregar() {
        if (ringProgressDialog == null || !ringProgressDialog.isShowing()) {
            ringProgressDialog = new Diverso().TempoTela(BoletoActivity.this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CarregaMenu(BoletoActivity.this);
                            BoletoActivity.ringProgressDialog.dismiss();
                        }
                    });

                }
            }).start();
        }

    }

    private void CarregaMenu(Context mContext) {

        List<TituloModel> listaTitulo = new ArrayList<>();
        List<TituloModel> listaTituloCooprofar = new ArrayList<>();
        List<TituloModel> listaTituloAgafarma = new ArrayList<>();

        List<TituloModel> listaTituloAgaCard = new ArrayList<>();
        List<TituloModel> listaTituloAgaCredi = new ArrayList<>();

        try {
            listaTitulo = new FinanceiroApi().ListarBoleto(mContext, SessionModel.Loja.LojaId);
        } catch (IOException e) {
            e.printStackTrace();
            ringProgressDialog.dismiss();
            Diverso.Error(getApplicationContext());
        }

        for (TituloModel item : listaTitulo) {
            switch (item.TipoEmpresa) {
                case COOPROFARAGAFARMA:
                    if (item.Empresa == COOPROFAR)
                        listaTituloCooprofar.add(item);
                    if (item.Empresa == AGAFARMA)
                        listaTituloAgafarma.add(item);
                    break;
                case AGACARD:
                    listaTituloAgaCard.add(item);
                    break;
                case AGACREDI:
                    listaTituloAgaCredi.add(item);
                    break;
                default:
                    Log.i(DEBUG_TAG, "Tipo do Boleto não encontrado: " + item.TipoEmpresa);
                    break;

            }
        }

        data = new ArrayList();


        MenuModel menu = new MenuModel();
        menu.Titulo = "Cooprofar";
        menu.IdImage = R.drawable.ic_menu_boleto;
        menu.IdMenu = 005;
        menu.Cls = ListaBoletoActivity.class;
        menu.HintQt = listaTituloCooprofar.size();
        menu.ListaTitulo = listaTituloCooprofar;
        data.add(menu);

        menu = new MenuModel();

        menu.Titulo = "Agafarma";
        menu.IdImage = R.drawable.ic_menu_boleto;
        menu.IdMenu = 010;
        menu.Cls = ListaBoletoActivity.class;
        menu.HintQt = listaTituloAgafarma.size();
        menu.ListaTitulo = listaTituloAgafarma;
        data.add(menu);

        menu = new MenuModel();
        menu.Titulo = "AgaCredi";
        menu.IdImage = R.drawable.ic_menu_boleto;
        menu.IdMenu = 015;
        menu.Cls = ListaBoletoActivity.class;
        menu.HintQt = listaTituloAgaCredi.size();
        menu.ListaTitulo = listaTituloAgaCredi;
        data.add(menu);

        menu = new MenuModel();
        menu.Titulo = "AgaCard";
        menu.IdImage = R.drawable.ic_menu_boleto;
        menu.IdMenu = 020;
        menu.Cls = ListaBoletoActivity.class;
        menu.HintQt = listaTituloAgaCard.size();
        menu.ListaTitulo = listaTituloAgaCard;
        data.add(menu);

        adapter = new BoletoAdapter(getApplicationContext(), data);
        recyclerView.setAdapter(adapter);
    }


}
