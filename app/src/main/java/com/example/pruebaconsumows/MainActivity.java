package com.example.pruebaconsumows;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText nombre;
    TextView response;
    Button consumir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        nombre = (EditText) findViewById(R.id.nombre);
        response = (TextView) findViewById(R.id.response);
        consumir = (Button) findViewById(R.id.consumir);

        consumir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             response.setText(GetResponse(nombre.getText().toString()));
            }
        });

    }
    public String GetResponse(String nombre){
        String METHOD="Saludar";
        String NAMESPACE="http://localhost/facturandote-wsdemo/";
        String SOAPACTION="http://localhost/facturandote-wsdemo/andamios/FacturandoteWSConnectAndamiosDEMO.php?method=Saludar";
        SoapObject request = new SoapObject(NAMESPACE,METHOD);
        request.addProperty("nombre",nombre);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(URL);
        try {

            transportSE.call(SOAPACTION,envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String result = (String) response.getProperty("SaludarReturn");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            return "Error: "+e.getMessage();

        } catch (XmlPullParserException e) {
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
            return "Error: "+e.getMessage();
        }


    }
}