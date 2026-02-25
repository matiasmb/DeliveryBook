# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in SDK_ROOT/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.kts.
#
# When you enable minify (isMinifyEnabled = true) in release, uncomment
# or add rules below to avoid stripping required code.

# ---------- Hilt / Dagger ----------
-dontwarn com.google.errorprone.**
-dontwarn javax.inject.**
-dontwarn org.checkerframework.**
-keep class dagger.hilt.** { *; }
-keepclasseswithmembers class * {
    @dagger.hilt.* <methods>;
}

# ---------- Room ----------
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ---------- Kotlin / Coroutines ----------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ---------- Keep app models if serialized / reflected ----------
-keep class com.deliverybook.domain.model.** { *; }

# ---------- Paging ----------
-keep class androidx.paging.** { *; }
