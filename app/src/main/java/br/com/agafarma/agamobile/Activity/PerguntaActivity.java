package br.com.agafarma.agamobile.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.agafarma.agamobile.Adapter.AgendaAdapter;
import br.com.agafarma.agamobile.Adapter.RespostaAdapter;
import br.com.agafarma.agamobile.Data.QuestionarioDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaDAO;
import br.com.agafarma.agamobile.Data.QuestionarioRespostaFotoDAO;
import br.com.agafarma.agamobile.Model.QuestionarioModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaModel;
import br.com.agafarma.agamobile.Model.QuestionarioPerguntaRespostaModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaFotoModel;
import br.com.agafarma.agamobile.Model.QuestionarioRespostaModel;
import br.com.agafarma.agamobile.Model.SessionModel;
import br.com.agafarma.agamobile.R;
import br.com.agafarma.agamobile.Util.Dialog;
import br.com.agafarma.agamobile.Util.Diverso;
import br.com.agafarma.agamobile.Util.ToolBar;

public class PerguntaActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<QuestionarioPerguntaModel> dataPergunta;
    private static List<QuestionarioPerguntaRespostaModel> dataResposta;
    private static List<QuestionarioPerguntaRespostaModel> data;
    private static int IdQuestionarioPergunta;
    private static String DEBUG_TAG = "PerguntaActivity";
    private TextView pTitulo;
    private TextView pDescricao;
    private EditText pTexto;

    private QuestionarioModel q;
    private QuestionarioPerguntaModel perguntaAtual;
    public int iP;
    private static String TAG = "PerguntaActivity";
    private FloatingActionButton fabAnexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pergunta);
        Toolbar barra = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(barra);
        new ToolBar().Action(true, getSupportActionBar(), this);
        pTitulo = (TextView) findViewById(R.id.pergunta_titulo);
        pDescricao = (TextView) findViewById(R.id.pergunta_descricao);
        pTexto = (EditText) findViewById(R.id.pergunta_texto);
        int idQuestionario = 0;
        if (getIntent().getExtras().getString("idQuestionario") != null) {
            idQuestionario = Integer.valueOf(getIntent().getExtras().getString("idQuestionario")).intValue();
        }
        q = new QuestionarioModel();
        q.IdQuestionario = idQuestionario;
        try {
            q = new QuestionarioDAO(this).Carregar(q);
            barra.setTitle(q.IdLoja + ":" + q.Titulo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        new QuestionarioRespostaDAO(this).Deletar(q.IdQuestionario);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalvarResposta();
            }
        });

        fabAnexo = (FloatingActionButton) findViewById(R.id.fabAnexo);

        activity = this;
        fabAnexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Camera", "Galeria", "Cancelar"};

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Anexar Fotos!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Camera")) {
                            captureImage();
                        } else if (items[item].equals("Galeria")) {
                            pickImage();
                        } else if (items[item].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_REQUEST_CODE);
                    }
                }
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_STORAGE_REQUEST_CODE);
                    }
                }
            }
        }
        if (requestCode == MY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_STORAGE_REQUEST_CODE);
                    }
                }
            }
        }
    }

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_STORAGE_REQUEST_CODE = 112;
    Uri mImageCaptureUri;

    Activity activity;

    public void captureImage() {

        if (activity != null) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
            mImageCaptureUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".my.package.name.provider", f);

            //mImageCaptureUri = Uri.fromFile(f);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            activity.startActivityForResult(intent, PICK_IMAGE_MULTIPLE);

        } else {

            //Log("Activity not assigned");
        }
    }

    public void pickImage() {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activity.startActivityForResult(i, PICK_IMAGE_MULTIPLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CarregarPergunta();
    }


    private List<QuestionarioPerguntaRespostaModel> filtraResposta(int idQuestionarioPergunta) {
        List<QuestionarioPerguntaRespostaModel> result = new ArrayList<>();
        for (QuestionarioPerguntaRespostaModel item : dataResposta) {
            if (item.IdQuestionarioPergunta == idQuestionarioPergunta)
                result.add(item);
        }
        return result;
    }

    private QuestionarioPerguntaModel buscaPergunta(int idQuestionarioPergunta) {
        for (QuestionarioPerguntaModel item : dataPergunta) {
            if (item.IdQuestionarioPergunta == idQuestionarioPergunta)
                return item;
        }
        return new QuestionarioPerguntaModel();
    }

    public void RadioOnClick(View view) {
        RadioButton radioButton = ((RadioButton) view);
        ViewGroup vG = (ViewGroup) findViewById(R.id.pergunta_grupo);
        ArrayList<View> listaView = Diverso.getViewsByTag(vG, "radio");
        for (View v : listaView) {
            ((RadioButton) v).setChecked(false);
        }
        radioButton.setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void CarregarPergunta() {
        if ((q.ListaQuestionarioPergunta.size() != 0) && (q.ListaQuestionarioPergunta.size() - 1 >= iP)) {

            perguntaAtual = q.ListaQuestionarioPergunta.get(iP);
            if (perguntaAtual != null) {
                pDescricao.setText(perguntaAtual.Descricao);
                pTitulo.setText(perguntaAtual.Titulo);
                if (perguntaAtual.TipoResposta != 3 && (perguntaAtual.ListaQuestionarioPerguntaResposta != null) && (perguntaAtual.ListaQuestionarioPerguntaResposta.size() > 0)) {
                    recyclerView.setVisibility(View.VISIBLE);
                    pTexto.setVisibility(View.INVISIBLE);
                    adapter = new RespostaAdapter(getApplicationContext(), perguntaAtual.ListaQuestionarioPerguntaResposta, perguntaAtual.TipoResposta);
                    recyclerView.setAdapter(adapter);
                } else if (perguntaAtual.TipoResposta == 3) {
                    recyclerView.setAdapter(null);
                    recyclerView.setVisibility(View.INVISIBLE);
                    pTexto.setText("");
                    pTexto.setVisibility(View.VISIBLE);
                }

                if (perguntaAtual.TipoPerguntaRespostaFoto == 1)
                    fabAnexo.setVisibility(View.INVISIBLE);
                else if (perguntaAtual.TipoPerguntaRespostaFoto == 2 || perguntaAtual.TipoPerguntaRespostaFoto == 3)
                    fabAnexo.setVisibility(View.VISIBLE);

            }
        } else {
            if (q.ListaQuestionarioPergunta.size() == 0) {
                new Dialog(this).ShowAlertOK("Atenção", "Questinario Não possui perguntas...", Boolean.valueOf(true));
                return;
            }
            FinalizaQuestionario();
        }
    }

    public void FinalizaQuestionario() {
        q.Status = 3;
        new QuestionarioDAO(this).Atualizar(this.q);
        Log.i(TAG, "Questionario Finalizado: ");
        new Dialog(this).ShowAlertOK("Atenção", "Questinario Respondido...", Boolean.valueOf(true));
    }

    public void SalvarResposta() {
        ViewGroup grupoPergunta = (ViewGroup) findViewById(R.id.pergunta_grupo);
        ArrayList<View> listaRadio = Diverso.getViewsByTag(grupoPergunta, "radio");
        ArrayList<View> listaCheck = Diverso.getViewsByTag(grupoPergunta, "check");
        List<QuestionarioRespostaModel> listaResposta = new ArrayList();
        int i;
        if (perguntaAtual.TipoResposta == 1) {
            for (View radio : listaRadio) {
                if (((RadioButton) radio).isChecked()) {
                    i = ((Integer) ((RadioButton) radio).getTag(R.id.IdQuestionarioPerguntaResposta)).intValue();
                    QuestionarioRespostaModel resposta = new QuestionarioRespostaModel();
                    resposta.IdQuestionario = perguntaAtual.IdQuestionario;
                    resposta.IdQuestionarioPergunta = perguntaAtual.IdQuestionarioPergunta;
                    resposta.IdQuestionarioPerguntaResposta = i;
                    resposta.DataCadastro = new Date();
                    resposta.IdUsuario = SessionModel.Usuario.UsuarioId;
                    listaResposta.add(resposta);
                    Log.i(TAG, "Inserida Resposta Radio: " + i);
                }
            }
        } else if (perguntaAtual.TipoResposta == 2) {
            for (View check : listaCheck) {

                if (((CheckBox) check).isChecked()) {
                    i = ((Integer) ((CheckBox) check).getTag(R.id.IdQuestionarioPerguntaResposta)).intValue();
                    QuestionarioRespostaModel resposta = new QuestionarioRespostaModel();
                    resposta.IdQuestionario = perguntaAtual.IdQuestionario;
                    resposta.IdQuestionarioPergunta = perguntaAtual.IdQuestionarioPergunta;
                    resposta.IdQuestionarioPerguntaResposta = i;
                    resposta.DataCadastro = new Date();
                    resposta.IdUsuario = SessionModel.Usuario.UsuarioId;
                    listaResposta.add(resposta);
                    Log.i(TAG, "Inserida Resposta Check: " + i);
                }
            }
        } else if (perguntaAtual.TipoResposta == 3) {

            QuestionarioRespostaModel resposta = new QuestionarioRespostaModel();
            resposta.IdQuestionario = perguntaAtual.IdQuestionario;
            resposta.IdQuestionarioPergunta = perguntaAtual.IdQuestionarioPergunta;
            resposta.IdQuestionarioPerguntaResposta = 0;
            resposta.DataCadastro = new Date();
            resposta.IdUsuario = SessionModel.Usuario.UsuarioId;
            resposta.Texto = String.valueOf(pTexto.getText());
            listaResposta.add(resposta);

        }

        List<QuestionarioRespostaFotoModel> listaFoto = null;
        try {
            listaFoto = new QuestionarioRespostaFotoDAO(this).Buscar(perguntaAtual);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (listaFoto.size() == 0 && perguntaAtual.TipoPerguntaRespostaFoto == 3) {
            new Dialog(this).ShowAlertOK("Atenção", "Por favor inserir fotos...");
            return;

        }

        if (listaResposta.size() == 0) {
            new Dialog(this).ShowAlertOK("Atenção", "Por favor selecione uma resposta...");
            return;
        }

        for (QuestionarioRespostaModel resposta : listaResposta) {
            new QuestionarioRespostaDAO(this).Inserir(resposta);
        }
        Log.i(TAG, "Respostas Gravadas Total: " + listaResposta.size());
        this.iP += 1;
        onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> dataUriFoto = new ArrayList();
        if (requestCode == PICK_IMAGE_MULTIPLE) {
            if (resultCode == this.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) {
                        dataUriFoto.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                } else if (data.getExtras() != null) {
                    Bitmap foto = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    foto.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String caminhoFoto = MediaStore.Images.Media.insertImage(activity.getContentResolver(), foto, "foto", null);
                    dataUriFoto.add(Uri.parse(caminhoFoto).toString());
                }
            } else if (data != null && data.getData() != null) {
                dataUriFoto.add(data.getData().getPath());
            }
            if (dataUriFoto.size() > 0) {
                ListaFotoActivity.data = dataUriFoto;
                ListaFotoActivity.idQuestionarioPergunta = perguntaAtual.IdQuestionarioPergunta;
                ListaFotoActivity.idQuestionario = q.IdQuestionario;
                Intent intent = new Intent(this, ListaFotoActivity.class);
                startActivity(intent);
            }
        }
    }


    int PICK_IMAGE_MULTIPLE = 1;


}
