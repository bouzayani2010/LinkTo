package com.project.linkto.Fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.linkto.Fragment.BaseFragment;
import com.project.linkto.R;
import com.project.linkto.bean.Post;
import com.project.linkto.bean.Userbd;
import com.project.linkto.singleton.DataHelper;
import com.project.linkto.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import static com.project.linkto.BaseActivity.mDatabase;
import static com.project.linkto.MainActivity.mAuth;

/**
 * Created by bbouzaiene on 10/05/2018.
 */

public class JoinNowFragment extends BaseFragment {

    private Button bt_join;
    private EditText ed_login;
    private EditText ed_password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joinnow, container, false);
        ed_login = (EditText) view.findViewById(R.id.ed_login);
        ed_password = (EditText) view.findViewById(R.id.ed_password);
        bt_join = (Button) view.findViewById(R.id.bt_join);


        bt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = ed_login.getText().toString();
                String password = ed_password.getText().toString();
                if (!Utils.isEmptyString(login) && !Utils.isEmptyString(password)) {
                    mAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseUser user = mAuth.getCurrentUser();
                           // DataHelper.getInstance().setmUser(user);
                            String key=user.getUid();

                            Userbd userbd = new Userbd(user);
                            Map<String, Object> postValues = userbd.toMap();

                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/users/" + key, postValues);
                            //childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

                            mDatabase.updateChildren(childUpdates);
                            if (task.isSuccessful()) {
                                new MaterialDialog.Builder(mActivity).title(R.string.logout)
                                        .content(R.string.registration_success_text)
                                        .positiveText(R.string.ok)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                                mActivity.goToSignIn();
                                            }
                                        }).show();
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
