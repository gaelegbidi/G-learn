package ma.emsi.gael.g_learn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionActivity extends AppCompatActivity {

    private LoginButton fbLginButton;
    private AccessTokenTracker accessTokenTracker ;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connection);


        fbLginButton = (LoginButton)findViewById(R.id.login_button);
        fbLginButton.setReadPermissions("email");
        // If using in a fragment
        // loginButton.setFragment(this);
        // Other app specific specialization


        // Callback registration


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
            }
        };



        callbackManager =  CallbackManager.Factory.create();
        fbLginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Context context = getApplicationContext();
                CharSequence text = "Cnx sucess!!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                String email ="";
                                try {
                                    email= response.getJSONObject().getString("email");
                                } catch (JSONException jse){
                                    Log.e("ConnectionActivity","jse: "+jse);
                                }
                                Log.v("ConnectionActivity", "response.toString: "+response.toString());
                                Log.v("ConnectionActivity", "object.toString: "+object.toString());
                                Log.v("ConnectionActivity", "email: "+email);

                                // Application code
//                                String birthday = object.getString("birthday"); // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Context context = getApplicationContext();
                CharSequence text = "Cnx cancelled!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }






            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("ConnectionActivity",exception.toString());
                Context context = getApplicationContext();
                CharSequence text = "Cnx error!!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        Log.d("ConnectionActivity", data.getData().);
        Log.d("ConnectionActivity", requestCode+"requestCode");
        Log.d("ConnectionActivity", resultCode + "resultCode");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }



}
