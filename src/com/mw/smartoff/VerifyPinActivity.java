package com.mw.smartoff;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.mw.smartoffice.R;
import com.parse.ParseUser;
import com.parse.PushService;

import java.util.Set;

public class VerifyPinActivity extends Activity implements View.OnClickListener {

    private static int MAIN_ACTIVITY = 1;
    EditText pinET;
    TextView headerTV;
    TextView pinInfoTV;
    TextView logoutTV;
    Button btn[] = new Button[10];
    ImageButton clearoneB;
    //	Button clearallB;
    Button loginB;
    RelativeLayout logoutLayout;

    Intent nextIntent;

    SharedPreferences sharedPreferences;
    Editor editor;

    TextView errorMsgTV;

    private Vibrator myVib;

    private void findThings() {
        pinET = (EditText) findViewById(R.id.pin_ET);
        headerTV = (TextView) findViewById(R.id.header_TV);
        errorMsgTV = (TextView) findViewById(R.id.error_msg_TV);
        btn[0] = (Button) findViewById(R.id.button1);
        btn[1] = (Button) findViewById(R.id.button2);
        btn[2] = (Button) findViewById(R.id.button3);
        btn[3] = (Button) findViewById(R.id.button4);
        btn[4] = (Button) findViewById(R.id.button5);
        btn[5] = (Button) findViewById(R.id.button6);
        btn[6] = (Button) findViewById(R.id.button7);
        btn[7] = (Button) findViewById(R.id.button8);
        btn[8] = (Button) findViewById(R.id.button9);
        btn[9] = (Button) findViewById(R.id.button0);
        clearoneB = (ImageButton) findViewById(R.id.clearone_B);
//		clearallB = (Button) findViewById(R.id.clearall_B);
        loginB = (Button) findViewById(R.id.login_B);
        pinInfoTV = (TextView) findViewById(R.id.PINInfo_TV);
        logoutTV = (TextView) findViewById(R.id.logout_TV);

        for (int i = 0; i < 10; i++) {
            btn[i].setOnClickListener(this);
        }
    }

    private void myOwnListeners() {
        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                errorMsgTV.setVisibility(View.GONE);
                if (s.length() > 4)
                    pinET.setText(s.toString().substring(0, 4));
            }
        });

    }

    private void initThings() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this
                .getApplicationContext());
        editor = sharedPreferences.edit();
        nextIntent = new Intent(VerifyPinActivity.this, MainActivity.class);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
    }

    boolean isPinThereInPrefs;

    private void initialVisibilityofViews() {
        isPinThereInPrefs = sharedPreferences.contains("pin");
        if (!isPinThereInPrefs) {
            headerTV.setText("Create your 4 digit PIN");
            pinInfoTV.setText("Secure your SMART OFFICE data with a 4-digit PIN. PIN ensures no one can access your data" +
                    " even when your phone is lost. PIN also provides convenience of access to SMART OFFICE without providing " +
                    " user name and password every time.");
            logoutTV.setVisibility(View.INVISIBLE);
//			loginB.setText("Save");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.verify_pin);
        findThings();
        myOwnListeners();
        initThings();
        initialVisibilityofViews();
    }

    public void onLogin(View view) {
        if (!validate())
            return;
        if (!isPinThereInPrefs) {
            editor.putString("pin", pinET.getText().toString());
            editor.commit();
            startActivityForResult(nextIntent, MAIN_ACTIVITY);
        } else if (!sharedPreferences.getString("pin", null).equals(
                pinET.getText().toString())) {
            errorMsgTV.setVisibility(View.VISIBLE);
            errorMsgTV.setText("PIN is incorrect");
        } else
            startActivityForResult(nextIntent, MAIN_ACTIVITY);
    }

    private boolean validate() {
        boolean bool = true;
        if (pinET.getText().toString().length() < 4) {
            errorMsgTV.setVisibility(View.VISIBLE);
            errorMsgTV.setText("Access code must be exactly 4 digits");
            bool = false;
        }

        return bool;
    }

    @Override
    public void onClick(View v) {
        myVib.vibrate(50);
        switch (v.getId()) {
            case R.id.button1:
                addtoarray("1");
                break;
            case R.id.button2:
                addtoarray("2");
                break;
            case R.id.button3:
                addtoarray("3");
                break;
            case R.id.button4:
                addtoarray("4");
                break;
            case R.id.button5:
                addtoarray("5");
                break;
            case R.id.button6:
                addtoarray("6");
                break;
            case R.id.button7:
                addtoarray("7");
                break;
            case R.id.button8:
                addtoarray("8");
                break;
            case R.id.button9:
                addtoarray("9");
                break;
            case R.id.button0:
                addtoarray("0");
                break;

            default:

        }

    }

    public void addtoarray(String numbers) {
        pinET.append(numbers);
    }

    public void onClearAll(View view) {
        pinET.setText("");
    }

    public void onClearOne(View view) {
        int slength = pinET.length();

        if (slength > 0) {

            // get the last character of the input
            String selection = pinET.getText().toString()
                    .substring(slength - 1, slength);
            System.out.println("Selection  :  " + selection);

            String result = pinET.getText().toString().replace(selection, "");
            System.out.println("Result  : " + result);

            pinET.setText(result);
            pinET.setSelection(pinET.getText().length());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAIN_ACTIVITY)
            if (resultCode == RESULT_OK) {

            }

    }

    // Satyen: what is this for?

    public void onCompanyLogin(View view) {
        Toast.makeText(this, "main login", Toast.LENGTH_SHORT).show();
    }

    boolean doubleBackToExitPressedOnce = false;
    
    public void onLogOut(View view) {

//    	if (doubleBackToExitPressedOnce)
//    	{
            // Satyen: we are removing PIN here
            editor.remove("pin");
            editor.commit();

            // Satyen: unsubscribing to channels
            Set<String> setOfAllSubscriptions = PushService.getSubscriptions(this);
            System.out.println(">>>>>>> Channels before clearing - " + setOfAllSubscriptions.toString());
            for (String setOfAllSubscription : setOfAllSubscriptions) {
                System.out.println(">>>>>>> MainActivity::onLogOut() - " + setOfAllSubscription);
                PushService.unsubscribe(this, setOfAllSubscription);
            }
            setOfAllSubscriptions = PushService.getSubscriptions(this);
            System.out.println(">>>>>>> Channels after cleared - " + setOfAllSubscriptions.toString());
            ParseUser.logOut();
            finish();

//    	}
//
//    	doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Click again to logout", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//

//        nextIntent = new Intent(this, LoginActivity.class);
//        startActivity(nextIntent);
    }


}
