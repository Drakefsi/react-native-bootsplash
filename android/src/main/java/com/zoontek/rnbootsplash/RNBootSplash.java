package com.zoontek.rnbootsplash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.UiThreadUtil;

public class RNBootSplash {

  private static boolean mInitialized = false;
  private static int mLayoutResId = -1;
  private static boolean mIsVisible = false;

  public static void init(final int layoutResId, @NonNull final Activity activity) {
    if (!mInitialized) {
      mLayoutResId = layoutResId;
      mInitialized = true;
      RNBootSplash.show(activity, 0.0f);
    }
  }

  static void show(@NonNull final Activity activity, final Float duration) {
    UiThreadUtil.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (!mInitialized || mIsVisible) {
          return;
        }

        mIsVisible = true;

        Context context = activity.getApplicationContext();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int roundedDuration = duration.intValue();
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context)
                .inflate(mLayoutResId, null);

        layout.setId(R.id.bootsplash_layout_id);

        if (roundedDuration <= 0) {
          activity.addContentView(layout, params);
        } else {
          layout.setAlpha(0.0f);
          activity.addContentView(layout, params);

          layout
              .animate()
              .setDuration(roundedDuration)
              .alpha(1.0f)
              .setInterpolator(new DecelerateInterpolator())
              .start();
        }
      }
    });
  }

  static void hide(@NonNull final Activity activity, final Float duration) {
    UiThreadUtil.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (!mInitialized || !mIsVisible) {
          return;
        }

        mIsVisible = false;

        final RelativeLayout layout = activity.findViewById(R.id.bootsplash_layout_id);

        if (layout == null) {
          return;
        }

        int roundedDuration = duration.intValue();
        final ViewGroup parent = (ViewGroup) layout.getParent();

        if (roundedDuration <= 0) {
          parent.removeView(layout);
        } else {
          layout
              .animate()
              .setDuration(roundedDuration)
              .alpha(0.0f)
              .setInterpolator(new AccelerateInterpolator())
              .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                  super.onAnimationEnd(animation);
                  if (parent != null) {
                    parent.removeView(layout);
                  }
                }
              }).start();
        }
      }
    });
  }
}
