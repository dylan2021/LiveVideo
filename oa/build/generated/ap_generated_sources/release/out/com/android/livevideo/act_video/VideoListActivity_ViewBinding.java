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

  private View view2131296390;

  private View view2131296395;

  private View view2131296392;

  private View view2131296394;

  @UiThread
  public VideoListActivity_ViewBinding(VideoListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VideoListActivity_ViewBinding(final VideoListActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_h, "method 'onClick'");
    view2131296390 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_v, "method 'onClick'");
    view2131296395 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_live, "method 'onClick'");
    view2131296392 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_origin, "method 'onClick'");
    view2131296394 = view;
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


    view2131296390.setOnClickListener(null);
    view2131296390 = null;
    view2131296395.setOnClickListener(null);
    view2131296395 = null;
    view2131296392.setOnClickListener(null);
    view2131296392 = null;
    view2131296394.setOnClickListener(null);
    view2131296394 = null;
  }
}
