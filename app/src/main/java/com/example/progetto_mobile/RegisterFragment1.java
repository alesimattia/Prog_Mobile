package com.example.progetto_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment1 extends Fragment {

    private String nome=null, cognome=null, tel=null;

    private EditText editText_nome;
    private EditText editText_cognome;
    private EditText editText_tel;
    private ImageButton btnNext;
    private ImageButton btnBack;

    public RegisterFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

    TODO: Rename and change types and number of parameters
    public static RegisterFragment2 newInstance() {
        RegisterFragment2 fragment = new RegisterFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register1_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        editText_nome = getView().findViewById(R.id.editText_nome);
        editText_cognome = getView().findViewById(R.id.editText_cognome);
        editText_tel = getView().findViewById(R.id.editText_tel);

        btnNext = getView().findViewById(R.id.button_next);
        btnBack=getView().findViewById(R.id.button_back1);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome=editText_nome.getText().toString();
                cognome=editText_cognome.getText().toString();
                tel=editText_tel.getText().toString();

                if (nome.isEmpty()) {
                    editText_nome.setError(getActivity().getResources().getString(R.string.err_richiesto));
                    editText_nome.requestFocus();
                    return;
                }

                if (cognome.isEmpty()) {
                    editText_cognome.setError(getResources().getString(R.string.err_richiesto));
                    editText_cognome.requestFocus();
                    return;
                }
                if (tel.isEmpty()) {
                    editText_tel.setError(getResources().getString(R.string.err_richiesto));
                    editText_tel.requestFocus();
                    return;
                }


                Bundle bundle = new Bundle();
                bundle.putString("nome", nome);
                bundle.putString("cognome", cognome);
                bundle.putString("tel", tel);

                RegisterFragment2 fragment2 = new RegisterFragment2();
                fragment2.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.placeholder, fragment2).commit();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getActivity(), MainActivity.class);
                startActivity(main);
            }
        });
    }

}
