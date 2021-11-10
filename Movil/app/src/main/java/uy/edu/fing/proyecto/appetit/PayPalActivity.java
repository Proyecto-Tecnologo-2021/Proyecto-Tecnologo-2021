package uy.edu.fing.proyecto.appetit;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.Order;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PayPalButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import uy.edu.fing.proyecto.appetit.constant.ConnConstants;

public class PayPalActivity extends AppCompatActivity {
    private static final String TAG = "PayPalActivity";
    private static final int PERMISOS_REQUERIDOS = 1;
    private static final int PAYPAL_REQUEST_CODE = 20213;
    final static Integer RC_SIGN_IN = 20213;

    PayPalButton payPalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);

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

}