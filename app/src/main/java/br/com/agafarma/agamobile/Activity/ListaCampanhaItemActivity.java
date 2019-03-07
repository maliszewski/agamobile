package br.com.agafarma.agamobile.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.CampanhaAdapter;
import br.com.agafarma.agamobile.Model.CampanhaModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.ToolBar;
import br.com.agafarma.agamobile.WebApi.CampanhaApi;

public class ListaCampanhaItemActivity extends AppCompatActivity {

    Toolbar barra;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static List<CampanhaModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_campanha_item);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            data = new CampanhaApi().BuscaCampanhaHome(getApplicationContext(), SessionModel.Usuario.LojaId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data != null && data.size() > 0) {
            adapter = new CampanhaAdapter(getApplicationContext(), data);
            recyclerView.setAdapter(adapter);
        } else {
            new Dialog(ListaCampanhaItemActivity.this).ShowAlertOK("Atenção", "Você não possui Campanha...", true);
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
