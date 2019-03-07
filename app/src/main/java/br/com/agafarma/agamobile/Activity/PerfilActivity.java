package br.com.agafarma.agamobile.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.PerfilAdapter;
import br.com.agafarma.agamobile.Model.MenuModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.ToolBar;

public class PerfilActivity extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static List<MenuModel> data;
    private static String TAG = "PerfilActivity";
    private static RecyclerView recyclerView;
    private TextView txtNomeFantasia;
    private TextView txtRazaoSocial;
    private ImageView imgEstrela1;
    private ImageView imgEstrela2;
    private ImageView imgEstrela3;
    private ImageView imgEstrela4;
    private ImageView imgEstrela5;
    private ImageView imgEstrela6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager g = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(g);

        txtNomeFantasia = (TextView) findViewById(R.id.perfil_nome_fantasia);
        txtRazaoSocial = (TextView) findViewById(R.id.perfil_razao_social);

        imgEstrela1 = (ImageView) findViewById(R.id.perfil_estrela1);
        imgEstrela2 = (ImageView) findViewById(R.id.perfil_estrela2);
        imgEstrela3 = (ImageView) findViewById(R.id.perfil_estrela3);
        imgEstrela4 = (ImageView) findViewById(R.id.perfil_estrela4);
        imgEstrela5 = (ImageView) findViewById(R.id.perfil_estrela5);
        imgEstrela6 = (ImageView) findViewById(R.id.perfil_estrela6);
    }


    @Override
    protected void onResume() {
        super.onResume();
        CarregaMenu(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    private void CarregaMenu(Context mContext) {
        CarregaEstrela();
        String nomeF = SessionModel.Loja.NomeFantasia.length() > 0 ? SessionModel.Loja.NomeFantasia : "";
        nomeF = nomeF.length() > 18 ? nomeF.substring(0, 18) : nomeF;
        nomeF = nomeF + " - AGA" + SessionModel.Loja.LojaId;
        String razaoS = SessionModel.Loja.RazaoSocial.length() > 0 ? SessionModel.Loja.RazaoSocial : "";
        razaoS = razaoS.length() > 30 ? razaoS.substring(0, 30) : razaoS;

        txtNomeFantasia.setText(nomeF);
        txtRazaoSocial.setText(razaoS);
        data = new ArrayList();


        MenuModel menu = new MenuModel();
        menu.Titulo = "Cadastrais";
        menu.IdImage = R.drawable.ic_casdastro;
        menu.IdMenu = 005;
        menu.Cls = PerfilGeralActivity.class;
        data.add(menu);

        menu = new MenuModel();

        menu.Titulo = "Localização";
        menu.IdImage = R.drawable.ic_localizacao;
        menu.IdMenu = 010;
        menu.Cls = PerfilLocalizacaoActivity.class;
        menu.HintQt = 0;
        data.add(menu);

        menu = new MenuModel();
        menu.Titulo = "Contato";
        menu.IdImage = R.drawable.ic_contato;
        menu.IdMenu = 015;
        menu.Cls = PerfilContatoActivity.class;
        menu.HintQt = 0;
        data.add(menu);

        menu = new MenuModel();
        menu.Titulo = "Encarte";
        menu.IdImage = R.drawable.ic_encarte;
        menu.IdMenu = 020;
        menu.Cls = PerfilEncarteActivity.class;
        menu.HintQt = 0;
        data.add(menu);

        menu = new MenuModel();
        menu.Titulo = "Financeiro";
        menu.IdImage = R.drawable.ic_financeiro;
        menu.IdMenu = 025;
        menu.Cls = PerfilFinanceiroActivity.class;
        menu.HintQt = 0;
        data.add(menu);

        menu = new MenuModel();
        menu.Titulo = "Diversos";
        menu.IdImage = R.drawable.ic_diverso;
        menu.IdMenu = 030;
        menu.Cls = PerfilDiversoActivity.class;
        menu.HintQt = 0;
        data.add(menu);

        adapter = new PerfilAdapter(getApplicationContext(), data);
        recyclerView.setAdapter(adapter);
    }

    private void CarregaEstrela() {
        if (SessionModel.Loja.ClassificacaoLojaId == 6) {
            imgEstrela1.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        if (SessionModel.Loja.ClassificacaoLojaId == 5) {
            imgEstrela1.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela2.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);

        }
        if (SessionModel.Loja.ClassificacaoLojaId == 4) {
            imgEstrela1.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela2.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela3.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);

        }
        if (SessionModel.Loja.ClassificacaoLojaId == 3) {
            imgEstrela1.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela2.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela3.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela4.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        if (SessionModel.Loja.ClassificacaoLojaId == 1) {
            imgEstrela1.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela2.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela3.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela4.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela5.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);

        }

        if (SessionModel.Loja.ClassificacaoLojaId == 2) {
            imgEstrela1.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela1.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela2.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela2.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela3.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela3.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela4.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela4.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela5.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela5.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);
            imgEstrela6.setImageResource(R.drawable.ic_estrela_cheia);
            imgEstrela6.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.perfilEstrelaCheia), android.graphics.PorterDuff.Mode.SRC_IN);

        }
    }

}
