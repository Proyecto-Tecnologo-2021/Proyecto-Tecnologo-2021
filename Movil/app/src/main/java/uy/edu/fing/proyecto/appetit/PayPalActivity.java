package uy.edu.fing.proyecto.appetit;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtPedido;
import uy.edu.fing.proyecto.appetit.obj.DtResponse;

public class PayPalActivity extends AppCompatActivity {
    private static final String TAG = "PayPalActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    private static final int PAYPAL_REQUEST_CODE = 20213;
    final static Integer RC_SIGN_IN = 20213;
    DtPedido dtPedido = DtPedido.getInstance();
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private String tokenPayPal;


    //PayPalButton payPalButton;
    WebView navegador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

        buscarTokenPayPal();

        navegador = findViewById(R.id.webView);





/*
        payPalButton = findViewById(R.id.payPalButton);

        CheckoutConfig config = new CheckoutConfig(
                this.getApplication(),
                ConnConstants.PAYPAL_CLIENT_ID,
                Environment.SANDBOX,
                String.format("%s://paypalpay", BuildConfig.APPLICATION_ID),
                CurrencyCode.USD,
                UserAction.CONTINUE,
                new SettingsConfig( true, false)
        );
        PayPalCheckout.setConfig(config);




        PayPalCheckout.start(
                createOrderActions -> {
                    ArrayList purchaseUnits = new ArrayList<>();
                    purchaseUnits.add(new PurchaseUnit.Builder()
                            .amount(new Amount.Builder()
                                    .currencyCode(CurrencyCode.USD)
                                    .value("10.00")
                                    .build()
                            ).build());
                }
        );
*/
        /*
        PayPalCheckout.start(
                createOrderActions -> {
                    ArrayList purchaseUnits = new ArrayList<>();
                    purchaseUnits.add(
                            new PurchaseUnit.Builder()
                                    .amount(
                                            new Amount.Builder()
                                                    .currencyCode(CurrencyCode.USD)
                                                    .value("10.00")
                                                    .build()
                                    )
                                    .build()
                    );
                    Order order = new Order(
                            OrderIntent.CAPTURE,
                            new AppContext.Builder()
                                    .userAction(UserAction.PAY_NOW)
                                    .build(),
                            purchaseUnits
                    );
                    createOrderActions.create(order, orderId -> {

                    });
                },
                approval -> {
                    approval.getOrderActions().capture(result -> {
                        // Order successfully captured
                    });
                },
                null,
                () -> {
                    // Optional callback for when a buyer cancels the paysheet
                },
                errorInfo -> {
                    // Optional error callback
                }
        );


        payPalButton.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value("10.00")
                                                        .build()
                                        )
                                        .build()
                        );
                        Order order = new Order(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        //createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                        //createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                        //createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NonNull CaptureOrderResult captureOrderResult) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", captureOrderResult));
                            }
                        });
                        //approval.getOrderActions().capture(result -> Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result)));
                    }
                }
        );


         */
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, url);
            Log.i(TAG, "startsWith: " + url.startsWith("http://127.0.0.1:3000/"));
            if (!url.startsWith("http://127.0.0.1:3000/")) {
                Log.i(TAG, "startsWith false");
                return false;
            } else if( Uri.parse(url).getQuery().contains("paymentId")
                    && Uri.parse(url).getQuery().contains("token")
                    && (Uri.parse(url).getQuery().contains("PayerID"))){
                String access = Uri.parse(url).getQuery();
                Log.i("payer_id", access);
                //new LoginUserTask().execute(ConnConstant.API_USRLOGIN_URL + access);
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            navegador.setVisibility(View.INVISIBLE);
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //progressBar.setVisibility(View.INVISIBLE);
            navegador.setVisibility(View.VISIBLE);
        }
    }

    private void buscarTokenPayPal() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.PAYPAL_URLTOKEN_ID;
        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new PayPalActivity.GetTokenPAYPALTask().execute(stringUrl);
        }
    }
    private void buscarPaymentPayPal() {
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        String stringUrl = ConnConstants.PAYPAL_URLPAYMENT_ID;
        Log.i(TAG, stringUrl);

        if (networkInfo != null && networkInfo.isConnected()) {
            new PayPalActivity.GetPaymentPAYPALTask().execute(stringUrl);
        }
    }

    private class GetTokenPAYPALTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return TokenPayPalInfoGralUrl(urls[0]);
            } catch (IOException e) {
                return getString(R.string.err_recuperarpag);
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Object result) {
            //progressBar.setVisibility(View.INVISIBLE);

            if (result instanceof DtResponse) {
                DtResponse response = (DtResponse) result;
                if (response.getOk()) {
                    String token = (String) response.getCuerpo();
                    tokenPayPal = token;
                    Log.i(TAG, token);
                    buscarPaymentPayPal();
                } else {
                    if (ContextCompat.checkSelfPermission(PayPalActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(PayPalActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(PayPalActivity.this).create();
                        dialog.setTitle(R.string.access_title_err);
                        dialog.setMessage(response.getMensaje());
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_positive), (dialog1, which) -> onBackPressed());
                        dialog.show();

                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(PayPalActivity.this).create();
                dialog.setTitle(R.string.info_title);

                if (result instanceof String) {
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                    //Log.i(TAG, getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        }
    }

    private DtResponse TokenPayPalInfoGralUrl(String myurl) throws IOException {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {

            String auth = ConnConstants.PAYPAL_CLIENT_ID + ":" + ConnConstants.PAYPAL_SECRET_ID;
            byte[] encodedAuth = Base64.encode(auth.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
            String authorization = "Basic " + new String(encodedAuth);

            Log.i(TAG, authorization);

            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", ConnConstants.USER_AGENT);
            conn.setRequestProperty("Authorization", authorization);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Language", "en_US");

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = "grant_type=client_credentials";
            Log.i(TAG, data);

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            Log.i(TAG, "conn.getResponseCode: " + response + " - " + conn.getResponseMessage());
            if (response == 200) {
                is = conn.getInputStream();
                return readInfoGralJsonStream(is);
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is);
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readInfoGralJsonStream(is);
            } else {
                return new DtResponse(false, response + " - " + conn.getResponseMessage(), null);
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    public DtResponse readInfoPaymentJsonStream(InputStream in) throws IOException {
        //creating an InputStreamReader object
        InputStreamReader isReader = new InputStreamReader(in);
        //Creating a BufferedReader object
        BufferedReader breader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = breader.readLine()) != null) {
            sb.append(str);
        }
        JsonReader reader = new JsonReader(new StringReader(sb.toString()));
        try {
            return readPOSTPaymentMessage(reader);
        } finally {
            reader.close();
        }
    }

    public DtResponse readPOSTPaymentMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;
        Map<String, String> links = null;

        try {
            reader.beginObject();
            ok = true;
            mensaje = "POST Payment ok";
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("links")) {
                    links = readPaymentArray(reader);
                } else {
                    reader.skipValue();
                }
            }
        } catch (Exception e) {
            ok = false;
            mensaje = "POST Payment error";
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, links);

    }

    private class GetPaymentPAYPALTask extends AsyncTask<String, Void, Object> {
        @Override
        protected Object doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                return PaymentPayPalInfoGralUrl(urls[0]);
            } catch (IOException e) {
                return getString(R.string.err_recuperarpag);
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Object result) {
            //progressBar.setVisibility(View.INVISIBLE);

            if (result instanceof DtResponse) {
                DtResponse response = (DtResponse) result;
                if (response.getOk()) {
                    Map<String, String> links = (Map<String, String>) response.getCuerpo();
                    Log.i(TAG, links.get("REDIRECT"));
                    addRedirectWeb(links.get("REDIRECT"));
                } else {
                    if (ContextCompat.checkSelfPermission(PayPalActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(PayPalActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
                        requestPermissions(permisos, PERMISOS_REQUERIDOS);
                    }else{
                        AlertDialog dialog = new AlertDialog.Builder(PayPalActivity.this).create();
                        dialog.setTitle(R.string.access_title_err);
                        dialog.setMessage(response.getMensaje());
                        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_positive), (dialog1, which) -> onBackPressed());
                        dialog.show();

                    }

                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(PayPalActivity.this).create();
                dialog.setTitle(R.string.info_title);

                if (result instanceof String) {
                    dialog.setMessage((String) result);
                } else {
                    dialog.setMessage(getString(R.string.err_recuperarpag));
                    //Log.i(TAG, getString(R.string.err_recuperarpag));
                }
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.alert_btn_neutral), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                });
                dialog.show();
            }
        }
    }

    private DtResponse PaymentPayPalInfoGralUrl(String myurl) throws IOException {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {

            String authorization ="Bearer  " + tokenPayPal;

            Log.i(TAG, authorization);

            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", ConnConstants.USER_AGENT);
            conn.setRequestProperty("Authorization", authorization);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String data = PaymentToJSON();
            Log.i(TAG, data);

            byte[] out = data.getBytes(StandardCharsets.UTF_8);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();

            Log.i(TAG, "conn.getResponseCode: " + response + " - " + conn.getResponseMessage());
            if (response == 200) {
                is = conn.getInputStream();
                return readInfoPaymentJsonStream(is);
            } else if (response == 201) {
                is = conn.getInputStream();
                return readInfoPaymentJsonStream(is);
            } else if (response == 400) {
                is = conn.getErrorStream();
                return readInfoPaymentJsonStream(is);
            } else if (response == 500) {
                is = conn.getErrorStream();
                return readInfoPaymentJsonStream(is);
            } else {
                return new DtResponse(false, response + " - " + conn.getResponseMessage(), null);
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    public DtResponse readInfoGralJsonStream(InputStream in) throws IOException {
        //creating an InputStreamReader object
        InputStreamReader isReader = new InputStreamReader(in);
        //Creating a BufferedReader object
        BufferedReader breader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = breader.readLine()) != null) {
            sb.append(str);
        }
        JsonReader reader = new JsonReader(new StringReader(sb.toString()));
        try {
            return readPOSTMessage(reader);
        } finally {
            reader.close();
        }
    }

    public DtResponse readPOSTMessage(JsonReader reader) throws IOException {
        Boolean ok = false;
        String mensaje = null;
        String access_token = null;

        try {
            reader.beginObject();
            ok = true;
            mensaje = "GET Token ok";
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("access_token")) {
                    access_token = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
        } catch (Exception e) {
            ok = false;
            mensaje = "GET Token error";
        }
        reader.endObject();
        return new DtResponse(ok, mensaje, access_token);

    }

    private String PaymentToJSON() {
        String res = "";

        JSONObject jsonObject = new JSONObject();
        JSONObject payer = new JSONObject();
        JSONArray transactions = new JSONArray();
        JSONObject oTransactions = new JSONObject();
        JSONObject amount = new JSONObject();
        JSONObject payment_options = new JSONObject();
        JSONObject redirect_urls = new JSONObject();

        try {
            jsonObject.put("intent", "sale");
            payer.put("payment_method", "paypal");
            jsonObject.put("payer", payer);

            double monto = dtPedido.getTotal()/dtPedido.getCotizacion().getBuy();
            BigDecimal pmonto = new BigDecimal(Double.toString(monto));
            pmonto = pmonto.setScale(2, RoundingMode.HALF_UP);

            amount.put("total", pmonto);
            amount.put("currency", "USD");
            oTransactions.put("amount", amount);

            oTransactions.put("description", dtPedido.getRes_nombre());
            oTransactions.put("custom", getString(R.string.app_name));
            oTransactions.put("invoice_number", "0");

            payment_options.put("allowed_payment_method", "INSTANT_FUNDING_SOURCE");
            oTransactions.put("payment_options", payment_options);
            oTransactions.put("soft_descriptor", getString(R.string.app_name));

            transactions.put(oTransactions);
            jsonObject.put("transactions", transactions);

            redirect_urls.put("return_url", "http://127.0.0.1:3000/success");
            redirect_urls.put("cancel_url", "http://127.0.0.1:3000/cancel");
            jsonObject.put("redirect_urls", redirect_urls);

            res = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            res = "";
        }

        return res;
    }

    public Map<String, String> readPaymentArray(JsonReader reader) throws IOException {
        Map<String, String> links = new HashMap<String, String>();
        reader.beginArray();
        while (reader.hasNext()) {
            links.putAll(readPayment(reader));
        }
        reader.endArray();
        return links;
    }


    public Map<String, String> readPayment(JsonReader reader) throws IOException {
        Map<String, String> link = null;
        String href = null;
        String method = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("href") && reader.peek() != JsonToken.NULL) {
                href = reader.nextString();
            } else if (name.equals("method") && reader.peek() != JsonToken.NULL) {
                method = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if(href != null && method != null){
            link = new HashMap<String, String>();
            link.put(method, href);
        }
        return link;

    }
    public void addRedirectWeb(String link){
        navegador.loadUrl(link);

        WebSettings wstts = navegador.getSettings();
        wstts.setJavaScriptEnabled(true);
        wstts.setDomStorageEnabled(true);
        wstts.setSupportZoom(true);
        wstts.setUserAgentString(ConnConstants.USER_AGENT);

        navegador.setWebViewClient(new MyWebViewClient());


    }

}