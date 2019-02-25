package com.project.linkto.fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.R;
import com.project.linkto.bean.Userbd;
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
    private TextView tv_join;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ed_login = (EditText) view.findViewById(R.id.ed_login);
        ed_password = (EditText) view.findViewById(R.id.ed_password);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        tv_join = (TextView) view.findViewById(R.id.tv_join);
        tv_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.gotoJoinNow();
            }
        });
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = ed_login.getText().toString().trim();
                String password = ed_password.getText().toString().trim();
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
                                mActivity.goToMain();

                                if (user != null) {
                                    Userbd userbd = new Userbd(user);
                                    DataHelper.getInstance().getuRepo().clearAll();
                                    DataHelper.getInstance().getuRepo().create(userbd);
                                    DataHelper.getInstance().setmUserbd(userbd);
                                }
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
                } else {
                    new MaterialDialog.Builder(mActivity)
                            .title(R.string.logout)
                            .content(R.string.empty_fields_text)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });

        return view;

    }


}
