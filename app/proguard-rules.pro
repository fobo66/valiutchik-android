# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Andrew\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

  -dontwarn afu.org.checkerframework.checker.**
  -dontwarn org.checkerframework.checker.**
  -dontwarn org.joda.convert.**
  -dontwarn com.google.maps.**
  -dontwarn com.google.appengine.api.urlfetch.**

   # okhttp
   -dontwarn okhttp3.**
   -dontwarn okio.**
   -dontwarn javax.annotation.**
   # A resource is loaded with a relative path so the package of this class must be preserved.
   -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

   # Google
   -keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
   -keep class com.google.android.gms.ads.identifier.** { *; }
   -dontwarn com.google.android.gms.**

   # Legacy
   -keep class org.apache.http.** { *; }
   -dontwarn org.apache.http.**
   -dontwarn android.net.http.**

   # Google Play Services library
   -keep class * extends java.util.ListResourceBundle {
       protected Object[][] getContents();
   }
   -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
     public static final *** NULL;
   }
   -keepnames class * implements android.os.Parcelable
   -keepclassmembers class * implements android.os.Parcelable {
     public static final *** CREATOR;
   }
   -keep @interface android.support.annotation.Keep
   -keep @android.support.annotation.Keep class *
   -keepclasseswithmembers class * {
     @android.support.annotation.Keep <fields>;
   }
   -keepclasseswithmembers class * {
     @android.support.annotation.Keep <methods>;
   }
   -keep @interface com.google.android.gms.common.annotation.KeepName
   -keepnames @com.google.android.gms.common.annotation.KeepName class *
   -keepclassmembernames class * {
     @com.google.android.gms.common.annotation.KeepName *;
   }
   -keep @interface com.google.android.gms.common.util.DynamiteApi
   -keep public @com.google.android.gms.common.util.DynamiteApi class * {
     public <fields>;
     public <methods>;
   }
   -keep class com.google.android.gms.common.GooglePlayServicesNotAvailableException {*;}
   -keep class com.google.android.gms.common.GooglePlayServicesRepairableException {*;}

   # Google Play Services library 9.0.0 only
   -dontwarn android.security.NetworkSecurityPolicy
   -keep public @com.google.android.gms.common.util.DynamiteApi class * { *; }

   # support-v4
   -keep class android.support.v4.app.Fragment { *; }
   -keep class android.support.v4.app.FragmentActivity { *; }
   -keep class android.support.v4.app.FragmentManager { *; }
   -keep class android.support.v4.app.FragmentTransaction { *; }
   -keep class android.support.v4.content.LocalBroadcastManager { *; }
   -keep class android.support.v4.util.LruCache { *; }
   -keep class android.support.v4.view.PagerAdapter { *; }
   -keep class android.support.v4.view.ViewPager { *; }
   -keep class android.support.v4.content.ContextCompat { *; }

   # support-v7-recyclerview
   -keep class android.support.v7.widget.RecyclerView { *; }
   -keep class android.support.v7.widget.LinearLayoutManager { *; }

  -keepclassmembers class fobo66.exchangecourcesbelarus.models.** {
    *;
  }

  # Guava
  -dontwarn javax.annotation.**
  -dontwarn javax.inject.**
  -dontwarn sun.misc.Unsafe
  -dontwarn java.lang.ClassValue
  -dontwarn com.google.j2objc.annotations.**
  -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
  -dontwarn com.google.errorprone.annotations.**

  -keep class com.google.common.base.CaseFormat
  -keep class com.google.common.base.Preconditions