package com.project.linkto;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.project.linkto.fragment.BaseFragment;
import com.project.linkto.fragment.MainFragment;
import com.project.linkto.fragment.chapter.MyHomeFragment;
import com.project.linkto.fragment.feeds.CommentFragment;
import com.project.linkto.fragment.feeds.FeedPostFragment;
import com.project.linkto.fragment.message.ChatMessageFragment;
import com.project.linkto.fragment.user.JoinNowFragment;
import com.project.linkto.fragment.user.ProfileFragment;
import com.project.linkto.fragment.user.SingInFragment;

/**
 * Created by bbouzaiene on 17/04/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    public static DatabaseReference mDatabase;
    public static FirebaseStorage storage;
    public static KProgressHUD kProgressHUD;

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
        storage = FirebaseStorage.getInstance();
        mDatabase = database.getReference();
        mDatabase.keepSynced(true);


        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                //     .setLabel("Please wait")
                //   .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // do nothing, just override
    }



    public void goToHome() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof MyHomeFragment)) {
                pushtoFragments("Home", new MyHomeFragment(), true, R.id.container, false);
            }
        } else {
            pushtoFragments("Home", new MyHomeFragment(), true, R.id.container, false);
        }

    }

    public void goToMain() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof MainFragment)) {
                pushtoFragments("Main", new MainFragment(), true, R.id.container, false);
            }
        } else {
            pushtoFragments("Main", new MainFragment(), true, R.id.container, false);
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

    public void gotoComment(CommentFragment commentFragment) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof CommentFragment)) {
                pushtoFragments("Comment", commentFragment, true, R.id.container, false);
            } else {
                commentFragment.setPost(commentFragment.getPost());
            }
        } else {
            pushtoFragments("Comment", commentFragment, true, R.id.container, false);
        }
    }

    public void gotoChatMessage(ChatMessageFragment chatMessageFragment) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof ChatMessageFragment)) {
                pushtoFragments("chatMessage", chatMessageFragment, true, R.id.container, false);
            }
        } else {
            pushtoFragments("chatMessage", chatMessageFragment, true, R.id.container, false);
        }
    }

    public void gotoProfile(ProfileFragment profileFragment ) {
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null) {
            if (!(fragment instanceof ChatMessageFragment)) {
                pushtoFragments("profile", profileFragment, true, R.id.container, false);
            }
        } else {
            pushtoFragments("profile", profileFragment, true, R.id.container, false);
        }
    }
}
