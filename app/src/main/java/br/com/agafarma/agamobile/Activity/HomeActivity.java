package br.com.agafarma.agamobile.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.CampanhaAdapter;
import br.com.agafarma.agamobile.Adapter.HomeAdapter;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.ContatoDAO;
import br.com.agafarma.agamobile.Data.LoginDAO;
import br.com.agafarma.agamobile.Data.MqttMensagemDAO;
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaFotoDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.BoletoModel;
import br.com.agafarma.agamobile.Model.CampanhaModel;
import br.com.agafarma.agamobile.Model.CircularModel;
import br.com.agafarma.agamobile.Model.ContatoChatModel;
import br.com.agafarma.agamobile.Model.LojaModel;
import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.Model.MqttMensagemModel;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.Model.TituloModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Service.MqttService;
import br.com.agafarma.agamobile.Service.SincronizaService;
import br.com.agafarma.agamobile.Util.Autorizacao;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.WebApi.ChatApi;
import br.com.agafarma.agamobile.WebApi.CircularApi;
import br.com.agafarma.agamobile.WebApi.FinanceiroApi;
import br.com.agafarma.agamobile.WebApi.LojaApi;
import pl.droidsonroids.gif.GifTextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar barra;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    private static List<MenuModel> data;
    private static ProgressDialog ringProgressDialog;
    public Intent intentService;
    public static GifTextView carregando = null;
    public static ImageView iconTrocaLoja = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        barra = (Toolbar) findViewById(R.id.homeBarra);
        setSupportActionBar(barra);
        intentService = new Intent(this, SincronizaService.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Diverso.isOnline(HomeActivity.this)) {
                    carregando.setVisibility(View.VISIBLE);
                    Snackbar.make(view, "Estamos atuaizado, consulte mais tarde...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    HomeActivity.this.startService(intentService);

                } else {
                    Snackbar.make(view, "POR FAVOR CONECTAR A INTERNET", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
        });

        if (SessionModel.Usuario.LojaId > 0)
            fab.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        carregando = (GifTextView) findViewById(R.id.carregando);
        iconTrocaLoja = (ImageView) findViewById(R.id.trocaLoja);

        iconTrocaLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrocaLoja(HomeActivity.this);
            }
        });

        carregando.setVisibility(View.INVISIBLE);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        GridLayoutManager g = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(g);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (ringProgressDialog == null || !ringProgressDialog.isShowing()) {
                    ringProgressDialog = new Diverso().TempoTela(HomeActivity.this);
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
                                    CarregaMenu(HomeActivity.this);
                                    HomeActivity.ringProgressDialog.dismiss();
                                }
                            });

                        }
                    }).start();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        MqttService.acaoIniciarServico(HomeActivity.this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        CarregaMenu(this);
        //CarregaCampanha(this);
    }

    private void CarregaMenu(Context mContext) {

        if (SessionModel.Usuario.Administrativo.equals("S"))
            iconTrocaLoja.setVisibility(View.VISIBLE);
        else
            iconTrocaLoja.setVisibility(View.INVISIBLE);


        AvisoModel avisoFiltro = new AvisoModel();
        avisoFiltro.Tipo = 2;
        MqttMensagemModel mqttMFiltro = new MqttMensagemModel();
        mqttMFiltro.Status = 1;


        List<MenuModel> listaM = new ArrayList();
        List<AvisoModel> listaA = new ArrayList();
        List<CircularModel> listaC = new ArrayList();
        List<MqttMensagemModel> listaMqtt = new ArrayList();


        try {
            listaM = new QuestionarioDAO(mContext).BuscaTipoFormularioEnviado();
            listaA = new AvisoDAO(mContext).Buscar(avisoFiltro);
            listaMqtt = new MqttMensagemDAO(mContext).Buscar(mqttMFiltro, true);
            if (SessionModel.Usuario.LojaId > 0)
                listaC = new CircularApi().BuscaNaoLida(HomeActivity.this);

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Diverso.Error(getApplicationContext());
        }
        data = new ArrayList();


        MenuModel menu = new MenuModel();
        if (SessionModel.Usuario.LojaId > 0) {
            menu.Titulo = "Perfil";
            menu.IdImage = R.drawable.ic_menu_perfil;
            menu.IdMenu = 997;
            menu.Cls = PerfilActivity.class;
            data.add(menu);

        }
        if (listaC.size() > 0 && SessionModel.Usuario.LojaId > 0) {
            menu = new MenuModel();
            ListaCircularActivity.data = listaC;
            menu.Titulo = "Circular";
            menu.IdImage = R.drawable.ic_menu_circular;
            menu.IdMenu = 998;
            menu.Cls = ListaCircularActivity.class;
            menu.HintQt = listaC.size();
            data.add(menu);
        }

        if (listaA.size() > 0) {
            menu = new MenuModel();
            menu.Titulo = "Aviso";
            menu.IdImage = R.drawable.ic_menu_sino;
            menu.IdMenu = 999;
            menu.Cls = ListaAvisoActivity.class;
            menu.HintQt = listaA.size();
            data.add(menu);
        }

        menu = new MenuModel();
        menu.Titulo = "Mensagem";
        menu.IdImage = R.drawable.ic_menu_aviso;
        menu.IdMenu = 996;
        menu.Cls = ListaMqttActivity.class;
        menu.HintQt = listaMqtt.size();
        data.add(menu);


        if (SessionModel.Usuario.LojaId > 0) {
            menu = new MenuModel();
            menu.Titulo = "Boleto";
            menu.IdImage = R.drawable.ic_menu_boleto;
            menu.Cls = BoletoActivity.class;
            menu.IdMenu = 995;
            data.add(menu);
        }
        menu = new MenuModel();
        menu.Titulo = "Campanhas";
        menu.IdImage = R.drawable.ic_menu_megafone;
        menu.Cls = ListaCampanhaActivity.class;
        menu.IdMenu = 994;
        data.add(menu);


        menu = new MenuModel();
        menu.Titulo = "Sair";
        menu.IdImage = R.drawable.ic_menu_sair;
        menu.IdMenu = 1000;
        //data.add(menu);


        data.addAll(listaM);
        adapter = new HomeAdapter(mContext, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAviso) {
            Intent intent = new Intent(HomeActivity.this,
                    ListaAvisoActivity.class);
            startActivity(intent);
        } else if (id == R.id.menuLogoff) {
            new LoginDAO(this).Deletar(SessionModel.Usuario);
            startActivity(new Intent(this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void TrocaLoja(Context mContext) {

        final EditText inputLoja = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputLoja.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputLoja.setHint("Número da Loja");
        inputLoja.setLayoutParams(lp);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Troca de Loja");
        alertDialog.setView(inputLoja);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ProgressDialog progressDialog = new Diverso().TempoTela(((Dialog) dialog).getContext());
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int idLoja = 0;
                                if (String.valueOf(inputLoja.getText()).length() > 0)
                                    idLoja = Integer.parseInt(String.valueOf(inputLoja.getText()));

                                try {
                                    if (idLoja > 0) {
                                        SessionModel.Loja = new LojaApi().BuscarLoja(mContext, idLoja);
                                        SessionModel.Usuario.LojaId = SessionModel.Loja.LojaId;
                                    } else {
                                        SessionModel.Loja = new LojaModel();
                                        SessionModel.Usuario.LojaId = 0;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Diverso.Error(getApplicationContext());
                                }
                                CarregaMenu(mContext);
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).start();
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();
    }

}
