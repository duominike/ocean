# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/joker/dev/android-sdk-linux/tools/proguard/proguard-android.txt
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
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-ignorewarnings

##################################################################################################

# -keepattributes *Annotation*,Signature
# 保护给定的可选属性
-keepattributes *Annotation*,LineNumberTable,SourceFile,Signature
# *Annotation* 注释
# EnclosingMethod 不明
# Signature 签名
# Deprecated 过期的类或方法
# InnerClasses 内部类是否混淆
# SourceFile 文件名

##################################################################################################

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
##################################################################################################
# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
##################################################################################################
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
##################################################################################################
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
##################################################################################################
-keepclassmembers class **.R$* {
    public static <fields>;
}
##################################################################################################
# 依赖的module
-keep class com.joker.pacific.** {*;}
-keep class com.joker.sponge.customview.** {*;}
-keep class com.handmark.pulltorefresh.library.** {*;}
##################################################################################################
-keep class com.google.code.gson.** { *; }