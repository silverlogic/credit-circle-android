# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Kevin/Desktop/Android_Development/sdk/tools/proguard/proguard-android.txt
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

#Icepick ----------------------
-dontwarn icepick.**
-keep class icepick.** { *; }
-keep class **$$Icepick { *; }
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}
#------------------------------

-keepattributes EnclosingMethod

# Realm sugar
 -keepclassmembers class io.tsl.realm_sugar.RealmSugarAnnotationProcessor {
     javax.annotation.processing.ProcessingEnvironment processingEnv;
 }
 -dontwarn io.tsl.realm_sugar.**