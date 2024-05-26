package com.aadevelopers.cashkingapp;

import static com.aadevelopers.cashkingapp.Activity_otp.chkref;
import static com.aadevelopers.cashkingapp.Activity_otp.randomAlphaNumeric;
import static com.aadevelopers.cashkingapp.csm.MainActivity.toast;
import static com.aadevelopers.cashkingapp.helper.AppController.hidepDialog;
import static com.aadevelopers.cashkingapp.helper.AppController.initpDialog;
import static com.aadevelopers.cashkingapp.helper.AppController.isConnected;
import static com.aadevelopers.cashkingapp.helper.AppController.showpDialog;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_KEY;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCESS_Value;
import static com.aadevelopers.cashkingapp.helper.Constatnt.ACCOUNT_STATE_ENABLED;
import static com.aadevelopers.cashkingapp.helper.Constatnt.API;
import static com.aadevelopers.cashkingapp.helper.Constatnt.Base_Url;
import static com.aadevelopers.cashkingapp.helper.Constatnt.USER_LOGIN;
import static com.aadevelopers.cashkingapp.helper.Helper.FRAGMENT_FORGOT_PASSWORD;
import static com.aadevelopers.cashkingapp.helper.Helper.FRAGMENT_SIGNUP;
import static com.aadevelopers.cashkingapp.helper.Helper.FRAGMENT_TYPE;
import static com.aadevelopers.cashkingapp.helper.Helper.getTextFromTextView;
import static com.aadevelopers.cashkingapp.helper.Helper.isValidEmail;
import static com.aadevelopers.cashkingapp.helper.Helper.isValidPassword;
import static com.aadevelopers.cashkingapp.helper.PrefManager.setWindowFlag;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.text.HtmlCompat;

import com.aadevelopers.cashkingapp.helper.AppController;
import com.aadevelopers.cashkingapp.helper.ContextExtensionKt;
import com.aadevelopers.cashkingapp.helper.JsonRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Activity_Login extends AppCompatActivity {

    EditText phone;
    Button submit;
    int account_status = 0;
    public static AlertDialog dialogwindow;
    private static final String TAG = "LoginActivity";
    String email;
    String username;
    String name;
    Uri profile;
    int simple_otp = 1227;
    String spinner_code, new_phone;
    CheckBox check1, check2, check3;
    int c1 = 0, c2, c3;
    String code = "";
    ImageView login, fb_btn;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 1001;
    public static String user = "league", password = "", msisdn = "", sid = "LEAGUE", msg = " for Verification. Khelo family Welcomes You!", fl = "0", gwid = "2";


    CallbackManager callbackManager;
    String androidId = " ";
    TextView text;
    private TextInputEditText email_edit_text, password_edit_text;
    private AppCompatButton login_button;
    private TextView forgotPassword,singup_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        initpDialog(Activity_Login.this);
        phone = findViewById(R.id.phone);
        submit = findViewById(R.id.submit);
        text = findViewById(R.id.text);


        String url1 = "<font color='" + getColor(R.color.gray) + "'><a href='" + getString(R.string.terms_of_service) + "'>Terms of service</a></font>";
        String url2 = "<font color='" + getColor(R.color.gray) + "'><a href='" + getString(R.string.privacy_policy) + "'>Privacy policy</a></font>";

        String formattedText = "By continuing, you agree to our <br>  " + url1 + " and " + url2;
        text.setLinksClickable(true);
        text.setMovementMethod(LinkMovementMethod.getInstance());
        text.setText(HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_LEGACY));
        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        forgotPassword = findViewById(R.id.forgotPassword);
        login_button = findViewById(R.id.login_btn);
        singup_btn = findViewById(R.id.sign_up_btn);

        ContextExtensionKt.showShortToast(this, "Welcome to the " + getResources().getString(R.string.app_name));

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp((Application) getApplicationContext());


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code

                                        try {
                                            email = object.getString("id");
                                            name = object.getString("name");
                                            profile = Uri.parse(object.getJSONObject("picture").getJSONObject("data").getString("url"));

                                            String newString = name.replace(" ", "_");
                                            username = newString + email;
                                            check_user(email, password,false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        ContextExtensionKt.showShortToast(Activity_Login.this, "Login failed!");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        ContextExtensionKt.showShortToast(Activity_Login.this, exception.getMessage());
                    }
                });

        fb_btn = findViewById(R.id.l1);

        fb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Activity_Login.this, Arrays.asList("email", "public_profile", "user_friends"));
            }
        });

        AppController.transparentStatusAndNavigation(Activity_Login.this);
        initpDialog(Activity_Login.this);

        login = findViewById(R.id.l2);
        configureGoogleClient();
        code = randomAlphaNumeric(8);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInToGoogle();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refer_dialog();
            }
        });
        login_button.setOnClickListener(view -> {
            String email = getTextFromTextView(email_edit_text);
            String password = getTextFromTextView(password_edit_text);
            String emailErrorMessage = isValidEmail(email);
            String passwordErrorMessage = isValidPassword(password);
            if (!emailErrorMessage.equals("ok")) {
                email_edit_text.setError(emailErrorMessage);
                email_edit_text.requestFocus();
            } else if (!passwordErrorMessage.equals("ok")) {
                password_edit_text.setError(passwordErrorMessage);
                password_edit_text.requestFocus();
            } else {
                this.email = email;
                check_user(email,password,true);
            }
        });
        singup_btn.setOnClickListener(view -> {
            Intent intent=new Intent(this, FragmentLoadingActivity.class);
            intent.putExtra(FRAGMENT_TYPE,FRAGMENT_SIGNUP);
            startActivity(intent);
        });
        forgotPassword.setOnClickListener(view -> {
            Intent intent=new Intent(this, FragmentLoadingActivity.class);
            intent.putExtra(FRAGMENT_TYPE,FRAGMENT_FORGOT_PASSWORD);
            startActivity(intent);
        });
    }

    ActivityResultLauncher<Intent> startforresultt = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    int result_code = result.getResultCode();
                    Intent data = result.getData();
                    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account);
                            Log.e("data", account.getDisplayName());
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e);

                        }
                    } else {
                        ContextExtensionKt.showShortToast(Activity_Login.this, "Google sign in failed");
                    }
                }
            }
    );

    public void rand() {
        Random rand = new Random();
        int resRandom = rand.nextInt((9999 - 100) + 1) + 10;
        simple_otp = resRandom;
    }

    public void submit() {
        if (account_status == 0) {
            rand();
            if (phone.getText().length() < 10) {
                phone.setError("Please Input Valid Phone Number");
            } else {
                new_phone = phone.getText().toString();
                check_user(new_phone, password,false);
            }
        }
    }

    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken("1:1021849794722:android:860046418b47e287e3bffb")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signInToGoogle() {
        startforresultt.launch(new Intent(googleSignInClient.getSignInIntent()));
    }


    //03. Check User
    public void check_user(String e, String password1,boolean isCustomLogin) {


        showpDialog();
        if (isConnected(Activity_Login.this)) {
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("error").equalsIgnoreCase("false")) {
                                    account_status = 1;
                                    signin(email,password1);

                                } else if (response.getString("error").equalsIgnoreCase("true")) {
                                    account_status = 2;
                                    if (isCustomLogin) {
                                        hidepDialog();
                                        ContextExtensionKt.showLongToast(Activity_Login.this, "This email is not registered");
                                    } else {
                                        register("0000000000", username, name, email, profile);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                ContextExtensionKt.showLongToast(Activity_Login.this, "Check User " + e.toString());
                                hidepDialog();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ContextExtensionKt.showLongToast(getApplicationContext(), "Check User " + e.toString());
                    hidepDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(ACCESS_KEY, ACCESS_Value);
                    params.put("user_check", API);
                    params.put("phone", e);
                    Log.e("Error", "phone :" + password1);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }


    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            spinner_code = parent.getItemAtPosition(pos).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void refer_dialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(Activity_Login.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.accept_term, null);
        alert.setView(dialogView);
        alert.setCancelable(false);
        dialogwindow = alert.create();
        dialogwindow.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        check3 = dialogView.findViewById(R.id.check3);
        Button ok = (Button) dialogView.findViewById(R.id.done);
        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    c3 = 3;
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check3.isChecked()) {
                    submit();
                } else {
                    ContextExtensionKt.showShortToast(Activity_Login.this, "You are not Eligible");
                }
                dialogwindow.dismiss();
            }
        });

        alert.setCancelable(true);
        dialogwindow.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());
                            email = user.getEmail();
                            name = user.getDisplayName();
                            profile = user.getPhotoUrl();

                            String newString = email.replace("@gmail.com", "");
                            username = newString;
                            check_user(email, password,false);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            ContextExtensionKt.showLongToast(Activity_Login.this, "signInWithCredential:failure" + task.getException());
                        }
                    }
                });
    }


    public void register(final String enter_phone, final String username, final String name, final String email, final Uri pro) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            androidId = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            if (androidId == null) {
                androidId = " ";
            }
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                androidId = mTelephony.getDeviceId();
            } else {
                androidId = Settings.Secure.getString(
                        getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
            if (androidId == null) {
                androidId = " ";
            }
        }
        if (isConnected(Activity_Login.this)) {
            showpDialog();
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                if (response.getString("error").equalsIgnoreCase("true")) {
                                    ContextExtensionKt.showLongToast(getApplicationContext(), response.getString("message"));
                                } else if (response.getString("error").equalsIgnoreCase("false")) {
                                    signin(email, "");
                                }
                            } catch (JSONException e) {
                            }
                            hidepDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ContextExtensionKt.showLongToast(getApplicationContext(),"Please check your internet connection!");
                    hidepDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(ACCESS_KEY, ACCESS_Value);
                    params.put("user_signup", API);
                    params.put("phone", enter_phone);
                    params.put("password", "");
                    params.put("username", username);
                    params.put("name", name);
                    params.put("email", email);
                    params.put("refer", code);
                    params.put("device", androidId);
                    params.put("image", String.valueOf(pro));
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }


    public void signin(final String enter_phone, String password) {

        if (isConnected(Activity_Login.this)) {
            showpDialog();
            JsonRequest jsonReq = new JsonRequest(Request.Method.POST, Base_Url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (AppController.getInstance().authorize(response)) {
                                if (AppController.getInstance().getState().equals(ACCOUNT_STATE_ENABLED)) {

                                    try {

                                        if (response.getString("error").equalsIgnoreCase("true")) {
                                            ContextExtensionKt.showShortToast(getApplicationContext(),response.getString("message"));
                                        } else if (response.getString("error").equalsIgnoreCase("false")) {
                                            chkref(Activity_Login.this);
                                        }
                                    } catch (JSONException e) {

                                        e.printStackTrace();

                                    }

                                } else {
                                    AppController.getInstance().logout(Activity_Login.this);
                                    ContextExtensionKt.showShortToast(getApplicationContext(), getText(R.string.msg_account_blocked).toString());
                                }

                            } else {
                                ContextExtensionKt.showShortToast(getApplicationContext(), "Invalid login details try again");
                            }

                            hidepDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ContextExtensionKt.showShortToast(getApplicationContext(), getText(R.string.error_data_loading).toString());
                    hidepDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(ACCESS_KEY, ACCESS_Value);
                    params.put(USER_LOGIN, API);
                    params.put("phone", enter_phone);
                    AppController.getInstance().setPassword(password);
                    AppController.getInstance().getBadge();
                    params.put("password", password);
                    Log.e(TAG, "getParams: "+password);
                    return params;
                }
            };
            AppController.getInstance().getRequestQueue().getCache().clear();
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }
}