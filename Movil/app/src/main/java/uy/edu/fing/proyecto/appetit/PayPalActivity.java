package uy.edu.fing.proyecto.appetit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PayPalActivity extends AppCompatActivity {
    private static final String TAG = "PayPalActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    private static final int PAYPAL_REQUEST_CODE = 20213;
    final static Integer RC_SIGN_IN = 20213;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);


/*

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

        payPalButton = findViewById(R.id.payPalButton);

 */
/*
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
*/
/*
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
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
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



}