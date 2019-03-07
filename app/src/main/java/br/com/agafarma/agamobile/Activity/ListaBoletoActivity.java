package br.com.agafarma.agamobile.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import br.com.agafarma.agamobile.Adapter.CircularAdapter;
import br.com.agafarma.agamobile.Adapter.TituloAdapter;
import br.com.agafarma.agamobile.Model.BoletoModel;
import br.com.agafarma.agamobile.Model.CircularModel;
import br.com.agafarma.agamobile.Model.TituloModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.ToolBar;

public class ListaBoletoActivity extends AppCompatActivity {

    Toolbar barra;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static List<TituloModel> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_boleto);
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
        if (data != null && data.size() > 0) {
            adapter = new TituloAdapter(getApplicationContext(), data);
            recyclerView.setAdapter(adapter);
        } else {
            new Dialog(ListaBoletoActivity.this).ShowAlertOK("Atenção", "Você não possui Boleto...", true);
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
