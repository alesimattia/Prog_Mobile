package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.progetto_mobile.ChooseActivity;
import com.example.progetto_mobile.R;

                                                                    //spinner
public class InsertFragment1 extends Fragment implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener{

    private RadioGroup radioGroup;
    private ImageButton btnNext;
    private ImageButton btnBack;

    private String tratta=null;
    private String verso = null;
    private String posti=null;


    public InsertFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.insert1_frag, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        Spinner spin_tratta = view.findViewById(R.id.spinner_tratta);
        Spinner spin_posti = view.findViewById(R.id.spinner_posti);
        //layout di default
        ArrayAdapter<CharSequence> adapter_tratta = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_tratte, android.R.layout.simple_spinner_item);
        adapter_tratta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  //layout lista di default
        spin_tratta.setAdapter(adapter_tratta); //applica
        spin_tratta.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter_posti = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_posti, android.R.layout.simple_spinner_item);
        adapter_posti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_posti.setAdapter(adapter_posti);
        spin_posti.setOnItemSelectedListener(this);

        btnNext = view.findViewById(R.id.button_next);
        btnBack = view.findViewById(R.id.button_back1);

        RadioButton checked = view.findViewById(R.id.radio_andata);     //inizializza la variabile "verso" su "andata"
        verso = checked.getText().toString();

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radio_andata);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checked=radioGroup.getCheckedRadioButtonId();
                RadioButton radio = view.findViewById(checked);
                verso = radio.getText().toString();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ChooseActivity.class);
                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tratta", tratta);
                bundle.putString("verso", verso);
                bundle.putString("posti", posti);

                InsertFragment2 fragment2 = new InsertFragment2();
                fragment2.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.placeholder, fragment2).commit();
            }
        });
    }


    @Override   //da interfaccia dello spinner  --> implementato come da documentazione Google
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.spinner_posti :
                posti=parent.getSelectedItem().toString();
                break;
            case R.id.spinner_tratta :
                tratta=parent.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {/*niente*/}

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {/*niente*/}
}
