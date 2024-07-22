package com.example.pruebaconsumows;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    EditText nombre;
    TextView response,cargando;
    Button consumir;
    LinearLayout form;
    ProgressBar progres;
    View fondo;
    String respuest = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        form = (LinearLayout)findViewById(R.id.form);
        nombre = (EditText) findViewById(R.id.nombre);
        response = (TextView) findViewById(R.id.response);
        cargando = (TextView) findViewById(R.id.cargando);
        consumir = (Button) findViewById(R.id.consumir);
        progres = (ProgressBar)findViewById(R.id.progress);
        fondo = (View)findViewById(R.id.fondo);
        cargando.setVisibility(View.INVISIBLE);
        progres.setVisibility(View.INVISIBLE);
        fondo.setVisibility(View.INVISIBLE);


        // adding the color to be shown
        ObjectAnimator animator = ObjectAnimator.ofInt(cargando, "textColor", Color.WHITE, Color.RED);

        // duration of one color
        animator.setDuration(900);
        animator.setEvaluator(new ArgbEvaluator());

        // color will be show in reverse manner
        animator.setRepeatCount(Animation.REVERSE);

        // It will be repeated up to infinite time
        animator.setRepeatCount(Animation.INFINITE);
        animator.start();
        consumir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargando.setVisibility(View.VISIBLE);
                progres.setVisibility(View.VISIBLE);
                fondo.setVisibility(View.VISIBLE);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        respuest=GetResponse(nombre.getText().toString());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cargando.setVisibility(View.INVISIBLE);
                                progres.setVisibility(View.INVISIBLE);
                                fondo.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                });
                response.setText(respuest);
            }
        });

    }
    public String GetResponse(String nombre){
        String METHOD="Saludar";
        String NAMESPACE="http://192.168.1.78/facturandote-wsdemo/";
        String SOAPACTION="http://192.168.1.78/facturandote-wsdemo/andamios/FacturandoteWSConnectAndamiosDEMO.php?method=Saludar";
        final String URL="http://192.168.1.78/facturandote-wsdemo/andamios/FacturandoteWSConnectAndamiosDEMO.php?wsdl";
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


            return "Error: "+e.getMessage();

        } catch (XmlPullParserException e) {

            return "Error: "+e.getMessage();
        }


    }

}
