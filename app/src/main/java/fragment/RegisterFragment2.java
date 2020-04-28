package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.progetto_mobile.R;
import com.example.progetto_mobile.RegisterActivity;

public class RegisterFragment2 extends Fragment {

    private String email=null, password=null, patente=null;

    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_patente;
    private ImageButton btnConfirm;
    private ImageButton btnBack;

    private Bundle bundle;

    public RegisterFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        bundle = getArguments();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register2_frag, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        editText_email = getView().findViewById(R.id.editText_email);
        editText_password = getView().findViewById(R.id.editText_password);
        editText_patente = getView().findViewById(R.id.editText_patente);

        btnConfirm = getView().findViewById(R.id.button_confirm);
        btnBack=getView().findViewById(R.id.button_back2);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=editText_email.getText().toString();
                password=editText_password.getText().toString();
                patente=editText_patente.getText().toString();

                if (email.isEmpty()) {
                    editText_email.setError(getResources().getString(R.string.err_richiesto));
                    editText_email.requestFocus();
                    return;
                }

                if (password.isEmpty() || password.length()<6) {
                    editText_password.setError(getResources().getString(R.string.passAlert));
                    editText_password.requestFocus();
                    return;
                }
                if (!patente.isEmpty() && patente.length()<6) {
                    editText_patente.setError(getResources().getString(R.string.licenceAlert));
                    editText_patente.requestFocus();
                    return;
                }

                ((RegisterActivity)getActivity()).registration(bundle.getString("nome"),
                                                                bundle.getString("cognome"),
                                                                bundle.getString("tel"), email, password, patente);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.placeholder, new RegisterFragment1()).commit();
            }
        });
    }

}