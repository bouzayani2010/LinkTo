package com.project.linkto;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.project.linkto.Fragment.BaseFragment;
import com.project.linkto.Fragment.HomeFragment;
import com.project.linkto.Fragment.feeds.FeedPostFragment;
import com.project.linkto.Fragment.user.JoinNowFragment;
import com.project.linkto.Fragment.user.SingInFragment;
import com.project.linkto.utils.Utils;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    public void pushtoFragments(final String tag, final Fragment fragment,
                                final boolean shouldAnimate, final int contentContainerId,
                                final boolean withLoss) {


        final int WHAT = 1;
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();

                    ft.addToBackStack(tag);
                    if (shouldAnimate) {
                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    ft.replace(contentContainerId, fragment);
                    if (withLoss) {
                        // dangerous
                        ft.commitAllowingStateLoss();
                    } else {
                        ft.commit();
                    }
                }
            }
        };
        handler.sendEmptyMessage(WHAT);

    }

    public FirebaseDatabase database;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        database = FirebaseDatabase.getInstance();


    }

    @Override
    public void onBackPressed() {
        Log.d("onBackPressed", "Container00");

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onBackPressed();
        } else {
            finish();
        }
    }


    public void goToHome() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof HomeFragment)) {
                pushtoFragments("Home", new HomeFragment(), true, R.id.container, false);
            }
        } else {
            pushtoFragments("Home", new HomeFragment(), true, R.id.container, false);
        }

    }

    public void goToSignIn() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof SingInFragment)) {
                pushtoFragments("SignIn", new SingInFragment(), true, R.id.container, false);
            }
        } else {
            pushtoFragments("SignIn", new SingInFragment(), true, R.id.container, false);
        }
    }

    public void gotoJoinNow() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof JoinNowFragment)) {
                pushtoFragments("JoinNow", new JoinNowFragment(), true, R.id.container, false);
            }
        } else {
            pushtoFragments("JoinNow", new JoinNowFragment(), true, R.id.container, false);
        }
    }

    public void saveUser(FirebaseUser user) {
        Utils.saveParam(this, "useremail", user.getEmail());
    }

    public void gotoFeedPost() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof FeedPostFragment)) {
                pushtoFragments("SignIn", new FeedPostFragment(), true, R.id.container, false);
            }
        } else {
            pushtoFragments("SignIn", new FeedPostFragment(), true, R.id.container, false);
        }
    }
}
