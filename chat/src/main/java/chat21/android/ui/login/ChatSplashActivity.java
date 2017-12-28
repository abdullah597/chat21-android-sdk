package chat21.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import chat21.android.core.ChatManager;
import static chat21.android.utils.DebugConstants.DEBUG_LOGIN;

/**
 * Created by stefanodp91 on 21/12/17.
 */


// sources:
// https://www.bignerdranch.com/blog/splash-screens-the-right-way/
// https://github.com/parse-community/ParseUI-Android/blob/master/ParseUI-Login/src/main/java/com/parse/ui/ParseLoginDispatchActivity.java
// https://bitbucket.org/frontiere21/smart21-android-aste/src/dce43efe4ae649a1516d4b5e397eff4ba77676f9/app/src/main/java/it/smart21/android/aste/activities/DispatchActivity.java?at=default&fileviewer=file-view-default
public abstract class ChatSplashActivity extends AppCompatActivity {

    /**
     * The class to start when the login process has been finished
     *
     * @return the target class
     */
    protected abstract Class<?> getTargetClass();

//    /**
//     * The intent to launch to perform the login activity
//     *
//     * @return the chat login intent
//     */
//    protected abstract Intent getLoginIntent();

    private static final int LOGIN_REQUEST = 0;
    private static final int TARGET_REQUEST = 1;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(DEBUG_LOGIN, "ChatSplashActivity.onCreate");

        runDispatch();
    }

    @Override
    final protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(DEBUG_LOGIN, "ChatSplashActivity.onActivityResult: requestCode == " + requestCode + " resultCode == " + requestCode);
        setResult(resultCode);
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            runDispatch();
        } else {
            finish();
        }
    }

    private void runDispatch() {
        Log.d(DEBUG_LOGIN, "ChatSplashActivity.runDispatch");
        // If current user has already logged in launch the target activity,
        // else launch the login activity
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            Log.d(DEBUG_LOGIN, "ChatSplashActivity.runDispatch: user is logged in. Goto : " + getTargetClass().getName());
            Intent targetIntent = new Intent(this, getTargetClass());
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                targetIntent.putExtras(extras);
            }
            startActivityForResult(targetIntent, TARGET_REQUEST);
        } else {
            Log.d(DEBUG_LOGIN, "ChatSplashActivity.runDispatch: user is not logged in. Goto  ChatLoginActivity");
            // Send user to login activity
//            startActivityForResult(getLoginIntent(), LOGIN_REQUEST);
            startActivityForResult(new Intent(this, ChatLoginActivity.class), LOGIN_REQUEST);
        }
    }
}