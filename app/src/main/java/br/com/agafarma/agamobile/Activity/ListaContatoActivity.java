package br.com.agafarma.agamobile.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.AvisoAdapter;
import br.com.agafarma.agamobile.Adapter.ContatoAdapter;
import br.com.agafarma.agamobile.Data.AvisoDAO;
import br.com.agafarma.agamobile.Data.ContatoDAO;
import br.com.agafarma.agamobile.Model.AvisoModel;
import br.com.agafarma.agamobile.Model.ContatoChatModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.ToolBar;

public class ListaContatoActivity extends AppCompatActivity {

    Toolbar barra;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<ContatoChatModel> data;

    private EditText txtPesquisar;
    private Button btnPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contato);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        getSupportActionBar().setTitle("Contatos");

        new ToolBar().Action(true, getSupportActionBar(), this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ContatoDAO contatoDAO = new ContatoDAO(ListaContatoActivity.this);
        ContatoChatModel contatoFiltro = new ContatoChatModel();
        try {
            data = contatoDAO.Buscar(contatoFiltro);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (data != null && data.size() > 0) {
            adapter = new ContatoAdapter(getApplicationContext(), data);
            recyclerView.setAdapter(adapter);
        } else {
            new Dialog(ListaContatoActivity.this).ShowAlertOK("Atenção", "Você não possui contatos...", true);
        }

        Pesquisar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    private void Pesquisar() {
        btnPesquisar = (Button) findViewById(R.id.btn_pesquisar_contato);
        txtPesquisar = (EditText) findViewById(R.id.txt_pesquisar);

        txtPesquisar.setVisibility(View.INVISIBLE);

        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPesquisar.setVisibility(View.INVISIBLE);
                txtPesquisar.setVisibility(View.VISIBLE);
            }
        });

        txtPesquisar.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    try {
                        ContatoChatModel contatoFiltro = new ContatoChatModel();
                        contatoFiltro.Nome = s.toString();
                        data = new ContatoDAO(ListaContatoActivity.this).Buscar(contatoFiltro);
                        if (data != null && data.size() > 0) {
                            adapter = new ContatoAdapter(getApplicationContext(), data);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
    }
}
