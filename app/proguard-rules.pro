# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#1 使用了自定义View要保证不能被混淆
#2 使用了枚举要保证枚举不被混淆
#3 对第三方库中的类不进行混淆
#4 运用了反射的类也不进行混淆
#5 使用了 Gson 之类的工具要使 JavaBean 类即实体类不被混淆
#6 在引用第三方库的时候，标明库的混淆规则
#7 有用到 WebView 的 JS 调用也需要保证写的接口方法不混淆
#8 Parcelable 的子类和 Creator 静态成员变量不混淆，否则会产生 Android.os.BadParcelableException 异常
#9 使用的四大组件，自定义的Application* 实体类
#10 JNI中调用的类
#11 Layout布局使用的View构造函数（自定义控件）、android:onClick等。


#------------------------------------------主项目混淆规则----------------------------------------------
#实体类不参与混淆
-keep class com.xtree.mvvmhabit.entity.** { *; }


#tkrefreshlayout
-keep class com.lcodecore.tkrefreshlayout.** { *; }
-dontwarn com.lcodecore.tkrefreshlayout.**

#-------------------------------------------MVVMHabit混淆规则----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class me.xtree.mvvmhabit.http.BaseResponse { *; }
-keep class me.xtree.mvvmhabit.http.BaseResponse2 { *; }
-keep class me.xtree.mvvmhabit.http.PMBaseResponse { *; }

-keep class com.xtree.base.vo.**{*;}
-keep class com.xtree.activity.vo.**{*;}
-keep class com.xtree.bet.bean.**{*;}
-keep class com.xtree.home.vo.**{*;}
-keep class com.xtree.mine.vo.**{*;}
-keep class com.xtree.recharge.vo.**{*;}

#-------------------------------------------------------------------------

#--------------------------------2.第三方包-------------------------------
#support
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**

#databinding
-keep class android.databinding.** { *; }
-dontwarn android.databinding.**

#annotation
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.sunloto.shandong.bean.** { *; }

#glide
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep class com.bumptech.glide.** { *; }
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#glide-transformations
-keep class jp.wasabeef.glide.transformations.** {*;}
-dontwarn jp.wasabeef.glide.transformations.**

#okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn okio.**

#RxJava RxAndroid
-dontwarn rx.*
-dontwarn sun.misc.**

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

#RxLifecycle
-keep class com.trello.rxlifecycle2.** { *; }
-keep interface com.trello.rxlifecycle2.** { *; }
-dontwarn com.trello.rxlifecycle2.**

#RxPermissions
-keep class com.tbruyelle.rxpermissions2.** { *; }
-keep interface com.tbruyelle.rxpermissions2.** { *; }

#material-dialogs
-keep class com.afollestad.materialdialogs.** { *; }
-dontwarn om.afollestad.materialdialogs.**

#=====================bindingcollectionadapter=====================
-keep class me.tatarka.bindingcollectionadapter.** { *; }
-dontwarn me.tatarka.bindingcollectionadapter.**


#---------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

-keep public class com.xtree.base.widget.WebAppInterface { *; }

#----------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------
-keep public class * extends me.xtree.mvvmhabit.base.BaseActivity{ *; }
-keep public class * extends me.xtree.mvvmhabit.base.BaseFragment{ *; }
-keep public class * extends me.xtree.mvvmhabit.binding.command.BindingCommand{ *; }
-keep public class * extends me.xtree.mvvmhabit.binding.command.ResponseCommand{ *; }


#----------------------------------------------------------------------------

#---------------------------------5.自定义控件------------------------------

#----------------------------------------------------------------------------
#---------------------------------6.其他定制区-------------------------------
#native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#保持类中的所有方法名
-keepclassmembers class * {
    public <methods>;
    private <methods>;
}

#----------------------------------------------------------------------------

#---------------------------------基本指令区---------------------------------
# 抑制警告
-ignorewarnings
#指定代码的压缩级别
-optimizationpasses 5
#包名不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#混淆包路径
-repackageclasses ''
-flattenpackagehierarchy ''

#保护注解
-keepattributes *Annotation*

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#保留SourceFile和LineNumber属性
-keepattributes SourceFile,LineNumberTable

#忽略警告
#-ignorewarning
#----------记录生成的日志数据,gradle build时在本项目根目录输出---------
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class com.xtree.base.widget.BrowserActivity {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
#----------------------------------------------------------------------------

#---------------------------------ARouter------------------------------------
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
#----------------------------------------------------------------------------

#-keep class com.xtree.** {*;}

#---------------------------------Module类不能混淆------------------------------------
-keep class com.xtree.base.base.**{*;}
-keep class com.xtree.base.config.**{*;}
-keep public class com.xtree.bet.BetModuleInit { *; }
-keep public class com.xtree.home.HomeModuleInit { *; }
-keep public class com.xtree.main.MainModuleInit { *; }
-keep public class com.xtree.main.MainModuleInit { *; }