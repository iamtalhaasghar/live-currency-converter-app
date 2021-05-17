package pk.asghar.talha.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;
   // private CountryCodePicker fromCountry;
    //private CountryCodePicker toCountry;
    private EditText inputAmount;
    private EditText outputValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get gui widgets
        //fromCountry = findViewById(R.id.fromCountry);
        //toCountry = findViewById(R.id.toCountry);
        inputAmount = findViewById(R.id.inputAmount);
        outputValue = findViewById(R.id.outputValue);

        // initialize progress bar
        progressDialog =  new ProgressDialog(this);


    }

    public void convertButtonPressed(View view){
        double amount = Double.parseDouble(inputAmount.getText().toString());
        String fromCode = "USD"; //= fromCountry.getDefaultCountryNameCode();
        String toCode = "PKR"; //= toCountry.getDefaultCountryNameCode();

        String url = String.format("https://www.x-rates.com/calculator/?from=%s&to=%s&amount=%.2f",
                fromCode, toCode, amount);

        Log.i("URL_RATE:", url);
        Fetch fetcher = new Fetch();
        fetcher.execute(url);
    }

    class Fetch extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Fetching Exchange Rate ...");
            progressDialog.show();

        }


        @Override
        protected String doInBackground(String... url) {
            String searchClass = "ccOutputRslt";
            Document doc = null;
            try {
                doc = Jsoup.connect(url[0]).get();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            Element e = doc.getElementsByClass(searchClass).first();

            return e.text();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            outputValue.setText(result);
            progressDialog.dismiss();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }
}
