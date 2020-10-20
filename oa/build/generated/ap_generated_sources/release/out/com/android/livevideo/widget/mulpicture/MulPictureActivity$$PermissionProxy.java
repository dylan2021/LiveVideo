// Generated code. Do not modify!
package com.android.livevideo.widget.mulpicture;

import com.zhy.m.permission.*;

public class MulPictureActivity$$PermissionProxy implements PermissionProxy<MulPictureActivity> {
@Override
 public void grant(MulPictureActivity source , int requestCode) {
switch(requestCode) {case 21:source.requestCameraSuccess();break;case 22:source.requestSdcardWriteSuccess();break;}  }
@Override
 public void denied(MulPictureActivity source , int requestCode) {
switch(requestCode) {case 21:source.requestCameraFailed();break;case 22:source.requestSdcardWriteFailed();break;}  }

}
