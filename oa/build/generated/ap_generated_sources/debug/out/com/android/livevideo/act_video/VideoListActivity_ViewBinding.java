// Generated code from Butter Knife. Do not modify!
package com.android.livevideo.act_video;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.android.livevideo.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VideoListActivity_ViewBinding implements Unbinder {
  private VideoListActivity target;

  private View view2131296379;

  private View view2131296383;

  private View view2131296381;

  private View view2131296382;

  @UiThread
  public VideoListActivity_ViewBinding(VideoListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VideoListActivity_ViewBinding(final VideoListActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_h, "method 'onClick'");
    view2131296379 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_v, "method 'onClick'");
    view2131296383 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_live, "method 'onClick'");
    view2131296381 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_origin, "method 'onClick'");
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view2131296379.setOnClickListener(null);
    view2131296379 = null;
    view2131296383.setOnClickListener(null);
    view2131296383 = null;
    view2131296381.setOnClickListener(null);
    view2131296381 = null;
    view2131296382.setOnClickListener(null);
    view2131296382 = null;
  }
}
