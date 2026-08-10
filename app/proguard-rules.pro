# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# ---- Basic ----
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Debugging information
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes Exceptions

# BuildConfig
-keep class com.byeboo.app.BuildConfig { *; }

# kotlinx.serialization (recommended/optional)
-keep @kotlinx.serialization.Serializable class * { *; }
-keepnames @kotlinx.serialization.Serializable class * { *; }
# The following 3 lines are optional
# -keep class kotlinx.serialization.** { *; }
# -keep class **$$serializer { *; }
# -keepclassmembers class * implements kotlinx.serialization.KSerializer { *; }

# Retrofit interfaces
-keep interface * {
    @retrofit2.http.* <methods>;
}

# Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ApplicationComponentManager { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { public <init>(...); }

# Crashlytics
-keep public class * extends java.lang.Exception
-keep class com.google.firebase.crashlytics.** { *; }

# Kakao SDK
-keep class com.kakao.sdk.** { *; }
-dontwarn com.kakao.sdk.**

# OkHttp & Retrofit
-keep class okhttp3.** { *; }
-keep class retrofit2.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**

# Timber
-keep class timber.log.Timber$Tree { *; }
-keep class timber.log.Timber$DebugTree { *; }
-assumenosideeffects class timber.log.Timber* {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Compose (if needed)
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# View constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Remove Android Log calls
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Kotlin Serialization
-keep class kotlinx.serialization.internal.** { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * {
    static ** Companion;
    static ** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# Navigation type-safe args
-keep class * extends androidx.navigation.NavArgs { *; }

# Class Protect
-keep class com.byeboo.app.core.model.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }

# Coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Mixpanel
-keep class com.mixpanel.** { *; }
-dontwarn com.mixpanel.**

# Google Play Core
-keep class com.google.android.play.** { *; }
-dontwarn com.google.android.play.**

# protectNavigation
-keep class com.byeboo.app.**.navigation.** { *; }

# Kotlin Serialization
-keep class kotlinx.serialization.internal.** { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * {
    static ** Companion;
    static ** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# Navigation type-safe args
-keep class * extends androidx.navigation.NavArgs { *; }

# Class Protect
-keep class com.byeboo.app.core.model.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }

# Coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Mixpanel
-keep class com.mixpanel.** { *; }
-dontwarn com.mixpanel.**

# Google Play Core
-keep class com.google.android.play.** { *; }
-dontwarn com.google.android.play.**

# protectNavigation
-keep class com.byeboo.app.**.navigation.** { *; }
