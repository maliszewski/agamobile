package br.com.agafarma.agamobile.Activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.AvisoAdapter;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.ToolBar;

public class ListaAvisoActivity extends AppCompatActivity {

    Toolbar barra;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<AvisoModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aviso);
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
        AvisoDAO avisoDAO = new AvisoDAO(ListaAvisoActivity.this);
        AvisoModel avisoFiltro = new AvisoModel();
        avisoFiltro.Tipo = 2;
        try {
            data = avisoDAO.Buscar(avisoFiltro);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (data != null && data.size() > 0) {
            adapter = new AvisoAdapter(getApplicationContext(), data);
            recyclerView.setAdapter(adapter);
        } else {
            new Dialog(ListaAvisoActivity.this).ShowAlertOK("Atenção", "Você não possui aviso...", true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
