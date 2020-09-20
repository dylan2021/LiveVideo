
##---------------Begin: proguard configuration common for all Android apps ----------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''


-keep public class * extends android.app.Activity
-keep public class * extends android.support.v4.app.FragmentActivity
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

#保持 Serializable 不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


# 所有native的方法不能去混淆.
-keepclasseswithmembernames class * {
    native <methods>;
}


-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}


-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}
# 对于带有回调函数onXXEvent的，不能混淆
-keepclassmembers class * { void *(**On*Event); }

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
##---------------End: proguard configuration common for all Android apps ----------


##-------------------- mySelf ---------------------------

-keep class com.google.vr.** {*;}
-keep class com.soundcloud.android.crop.** {*;}

-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** { *;}

#picasso
-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** {*; }
-keepclasseswithmembernames class * {
    native <methods>;
}

#slidingmenu
-dontwarn android.support.**
-dontwarn com.google.android.maps.**
-dontwarn com.jeremyfeinstein.slidingmenu.**

-dontwarn com.tencent.**
-ignorewarnings

#-dontwarn okio.**
#-dontwarn com.google.code.gson.**
#-dontwarn org.joda.time.**
#-dontwarn retrofit2.**
#-dontwarn rx.internal.util.unsafe.**
#-dontwarn com.trello.**
#-dontwarn com.alibaba.**
#-dontwarn com.zhy.m.**

-keep class android.support.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keep class com.jeremyfeinstein.slidingmenu.** { *; }
-keep interface com.jeremyfeinstein.slidingmenu.** { *; }

#-keep class com.google.code.gson.** { *; }
#-keep class joda-time.** { *; }
#-keep class com.squareup.retrofit2.** { *; }
#-keep class com.trello.** { *; }

#-keep class com.alibaba.** { *; }

-keep public class com.android.livevideo.ota.model.OtaService

#baidu push
#-libraryjars libs/pushservice-5.3.0.99.jar
-dontwarn com.baidu.**
-keep class com.baidu.**{*; }

##--------------------End mySelf --------------------------

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature


# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; } ##这里需要改成解析到哪个  javabean
#-keep class ExtraDataBean
-keep class com.android.livevideo.push.model.** { *; }

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#-----------------------umeng------------------------------------------
-dontwarn com.umeng.**
-dontwarn org.apache.commons.**
-dontwarn com.tencent.weibo.sdk.**
-keep class com.umeng*.** {*; }
-keep public class [your_pkg].R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
##---------------End: proguard configuration for Gson  ----------
#RXjava
-dontwarn sun.misc.** -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* { long producerIndex; long consumerIndex; }
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef { rx.internal.util.atomic.LinkedQueueNode producerNode; }
# Okio
-dontwarn com.squareup.** -dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
#库
-keep class javax.ws.rs.** { *; }
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
#v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-dontwarn com.zhy.m.** -keep class com.zhy.m.** {*;}
 -keep interface com.zhy.m.** { *; }
 -keep class **$$PermissionProxy { *; }
 #新浪
 -keep class android.webkit.**{*;}
 -keep public class android.webkit.WebView {*;}
 -keep public class android.webkit.WebViewClient {*;}
 -keep class com.weibo.net.** {*;}
 -keep class com.sina.**{*;}
 -keep class m.framework.**{*;}
 #sina SDK
-dontwarn android.net.http.**
-keep class android.net.http.** { *;}
# Joda Time
-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
 -keep class org.joda.time.** { *; }
 -keep interface org.joda.time.** { *; }

 -dontwarn org.codehaus.**
 -dontwarn java.nio.**
 -dontwarn java.lang.invoke.**
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions

 #WebView的处理
 -keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
     public boolean *(android.webkit.WebView, java.lang.String);
 }
 -keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, java.lang.String);
 }
 -keepclassmembers class com.sina.weibo.sdk.web.client.BaseWebViewClient {
    <fields>;
        <methods>;
 }
 # ############### volley混淆 ############### # # -------------------------------------------
 -keep class com.android.volley.** {*;}
 -keep class com.android.volley.toolbox.** {*;}
 -keep class com.android.volley.Response$* { *; }
 -keep class com.android.volley.Request$* { *; }
 -keep class com.android.volley.RequestQueue$* { *; }
 -keep class com.android.volley.toolbox.HurlStack$* { *; }
 -keep class com.android.volley.toolbox.ImageLoader$* { *; }
 #------------------实体类 # -------------------------------------------
 -keep class com.android.livevideo.bean.** { *; }
 -keep class com.android.livevideo.game.bean.** { *; }
 -keep class com.android.livevideo.App { *; }
 -keep class com.jzt.hol.android.jkda.sdk.bean.** { *; }
 -keep class com.jzt.hol.android.jkda.sdk.services.** { *; }
 -keep class com.android.livevideo.push.model.ExtraDataBean
 -keep class com.android.livevideo.push.model.MessageDetail
 -keep class com.android.livevideo.activity.main.MainHomeActivity
 -keep class com.android.livevideo.activity.hub.HubItemActivity

 -keep class org.apache.http.** {*; }
 -keep class org.apache.**{*;}

 #-dontwarn com.facebook.**
-dontwarn com.facebook.**
-keep enum com.facebook.** {*;}
-keep public interface com.facebook.**
-keep class com.facebook.** {*;}
#retrofit2.x
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
#Rxjava RxAndroid
-keepattributes Signature -keepattributes Exceptions
-dontwarn rx.* -dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
   long consumerIndex;
}
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
  rx.internal.util.atomic.LinkedQueueNode producerNode;
}
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#广告
-keepattributes SourceFile,LineNumberTable
 -keep class com.inmobi.** { *; }
-keep public class com.google.android.gms.**
 -dontwarn com.google.android.gms.**
 -dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{ public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
public *;
}
# skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.** -dontwarn com.squareup.okhttp.**
# skip Moat classes
-keep class com.moat.** {*;}
 -dontwarn com.moat.**
# skip AVID classes
-keep class com.integralads.avid.library.* {*;}

#广告
-dontwarn com.hubcloud.adhubsdk.**

-dontwarn android.app.**
-dontwarn android.support.**


-keepattributes Signature
-keepattributes *Annotation*

-keep class com.hubcloud.adhubsdk.** {*; }
-keep class android.app.**{*;}
-keep class **.R$* {*;}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-dontshrink -dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
 -dontwarn com.facebook.**
 -keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep class com.facebook.**
-keep class com.facebook.** { *; }
 -keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** { *; }
-keep class com.tencent.mm.opensdk.** { *; }
-keep class com.tencent.wxop.** { *; }
 -keep class com.tencent.mm.sdk.** { *; }
 -dontwarn twitter4j.**
  -keep class twitter4j.** { *; }
  -keep class com.tencent.** {*;}
  -dontwarn com.tencent.**
  -keep class com.kakao.** {*;}
   -dontwarn com.kakao.**
   -keep public class com.umeng.com.umeng.soexample.R$*{ public static final int *; }
   -keep public class com.linkedin.android.mobilesdk.R$*{ public static final int *; }
 -keepclassmembers enum * { public static **[] values(); public static ** valueOf(java.lang.String); }
 -keep class com.tencent.open.TDialog$*
 -keep class com.tencent.open.TDialog$* {*;}
 -keep class com.tencent.open.PKDialog
 -keep class com.tencent.open.PKDialog {*;}
 -keep class com.tencent.open.PKDialog$*
 -keep class com.tencent.open.PKDialog$* {*;}
  -keep class com.umeng.socialize.impl.ImageImpl {*;}
 -keep class com.sina.** {*;}
 -dontwarn com.sina.**
  -keep class  com.alipay.share.sdk.** { *; }
 -keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }
 -keep class com.linkedin.** { *; }
 -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
  -keepattributes Signature

  #jwt依赖
  -keepattributes InnerClasses

  -keep class io.jsonwebtoken.** { *; }
  -keepnames class io.jsonwebtoken.* { *; }
  -keepnames interface io.jsonwebtoken.* { *; }

  -keep class org.bouncycastle.** { *; }
  -keepnames class org.bouncycastle.** { *; }
  -dontwarn org.bouncycastle.**


