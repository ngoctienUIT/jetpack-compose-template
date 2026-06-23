# Add project specific ProGuard rules here.
# https://developer.android.com/studio/build/shrink-code

# Keep line numbers for readable crash reports in Play Console
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# --- Kotlin ---
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# --- Kotlinx Serialization ---
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-keep @kotlinx.serialization.Serializable class * {
    <fields>;
    <init>(...);
}

# --- Retrofit ---
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation,allowshrinking interface * {
    @retrofit2.http.* <methods>;
}
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# --- Hilt / Dagger ---
-dontwarn com.google.errorprone.annotations.**
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keepclasseswithmembers class * {
    @javax.inject.* <fields>;
}
-keepclasseswithmembers class * {
    @javax.inject.* <methods>;
}

# --- Room (ready when @Database is added) ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# --- Coil ---
-dontwarn coil.**

# --- Firebase ---
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# --- Google Identity / Credential Manager ---
-keep class com.google.android.libraries.identity.** { *; }
-dontwarn com.google.android.libraries.identity.**
-keep class androidx.credentials.** { *; }
-dontwarn androidx.credentials.**

# --- Facebook SDK ---
-keep class com.facebook.** { *; }
-dontwarn com.facebook.**
-keepattributes Signature

# --- App models & navigation routes ---
-keep class com.ngoctientnt.template.app.navigation.** { *; }
-keep class com.ngoctientnt.template.core.appinfo.model.** { *; }
-keep class com.ngoctientnt.template.core.network.dto.** { *; }
-keep class com.ngoctientnt.template.core.network.paging.dto.** { *; }
-keep class com.ngoctientnt.template.feature.explore.data.remote.dto.** { *; }
-keep class * extends androidx.paging.PagingSource { *; }
-keep class com.ngoctientnt.template.core.auth.data.remote.dto.** { *; }
-keep class com.ngoctientnt.template.core.auth.domain.model.** { *; }

# --- Security Crypto ---
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# --- Window extensions (Compose / Activity) ---
-dontwarn androidx.window.**
