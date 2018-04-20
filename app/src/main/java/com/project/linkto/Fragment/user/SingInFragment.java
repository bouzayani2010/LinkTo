package com.project.linkto.Fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.project.linkto.Fragment.BaseFragment;
import com.project.linkto.R;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;

import static android.content.ContentValues.TAG;
import static com.project.linkto.MainActivity.mAuth;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class SingInFragment extends BaseFragment {
    private EditText ed_login;
    private EditText ed_password;
    private Button bt_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ed_login = (EditText) view.findViewById(R.id.ed_login);
        ed_password = (EditText) view.findViewById(R.id.ed_password);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = ed_login.getText().toString();
                String password = ed_password.getText().toString();
                if (!Utils.isEmptyString(login) && !Utils.isEmptyString(password)) {
                    mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                DataHelper.getInstance().setmUser(user);
                                DataHelper.getInstance().setConnected(true);
                                mActivity.goToHome();
                               // updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(mActivity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                             //   updateUI(null);
                            }
                        }
                    });
                }
            }
        });

        return view;

    }
}