package br.com.agafarma.agamobile.Model;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuModel {
    public int IdMenu;
    public String Titulo;
    public int IdImage;
    public Class<?> Cls;
    public int HintQt = 0;
    public List<TituloModel> ListaTitulo;
    public String Transporte;
    public MenuModel(){
        ListaTitulo = new ArrayList<TituloModel>();
    }

}
