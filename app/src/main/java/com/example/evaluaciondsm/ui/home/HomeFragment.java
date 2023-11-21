package com.example.evaluaciondsm.ui.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


//import com.example.evaluaciondsm.AdminSQLiteOpenHelper;
import com.example.evaluaciondsm.AdminSQLiteOpenHelper;
import com.example.evaluaciondsm.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private EditText etCode, etPrice, etDescription;
    private Button btnRegister, btnSearchP, btnDelete, btnEdit;
    private ArrayList <String> listProducts;
    private ArrayAdapter <String> adapter;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        etCode = binding.txtCode;
        etPrice = binding.txtPrice;
        etDescription = binding.txtDescription;



        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        binding.btnSearchP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Metodo para guardar los productos
    public void save() {

        if (binding.txtCode.getText().toString().isEmpty() || binding.txtPrice.getText().toString().isEmpty() || binding.txtDescription.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
            SQLiteDatabase database = admin.getWritableDatabase();

            ContentValues register = new ContentValues();
            register.put("code", binding.txtCode.getText().toString());
            register.put("name", binding.txtPrice.getText().toString());
            register.put("price", binding.txtDescription.getText().toString());

            database.insert("products", null, register);
            database.close();

            binding.txtCode.setText("");
            binding.txtPrice.setText("");
            binding.txtDescription.setText("");

            Toast.makeText(getContext(), "Producto registrado", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para buscar productos
    public void search() {
        if(binding.txtCode.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Debe ingresar el codigo del producto", Toast.LENGTH_SHORT).show();
        }else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
            SQLiteDatabase database = admin.getWritableDatabase();

            String code = binding.txtCode.getText().toString();

            Cursor row = database.rawQuery("select name, price from products where code = " + code, null);

            if(row.moveToFirst()){
                binding.txtPrice.setText(row.getString(0));
                binding.txtDescription.setText(row.getString(1));
                database.close();
            }else{
                Toast.makeText(getContext(), "No existe el producto", Toast.LENGTH_SHORT).show();
                database.close();
            }

        }
    }

    public void delete() {

        if(binding.txtCode.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Debe ingresar el codigo del producto", Toast.LENGTH_SHORT).show();
        }else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
            SQLiteDatabase database = admin.getWritableDatabase();

            String code = binding.txtCode.getText().toString();

            int cant = database.delete("products", "code = " + code, null);
            database.close();

            binding.txtCode.setText("");
            binding.txtPrice.setText("");
            binding.txtDescription.setText("");

            if(cant == 1){
                Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "No existe el producto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void edit() {

        if(binding.txtCode.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Debe ingresar el codigo del producto", Toast.LENGTH_SHORT).show();
        }else{
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getContext(), "products", null, 1);
            SQLiteDatabase database = admin.getWritableDatabase();

            String code = binding.txtCode.getText().toString();
            ContentValues register = new ContentValues();
            register.put("code", binding.txtCode.getText().toString());
            register.put("name", binding.txtPrice.getText().toString());
            register.put("price", binding.txtDescription.getText().toString());

            int cant = database.update("products", register, "code = " + code, null);
            database.close();

            if(cant == 1){
                Toast.makeText(getContext(), "Producto modificado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "No existe el producto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cleanForm() {
    }

}