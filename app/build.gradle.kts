plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.menuplannerapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.menuplannerapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Versions compatibles avec compileSdk 34
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Volley pour les requêtes HTTP (API)
    implementation("com.android.volley:volley:1.2.1")

    // RecyclerView pour les listes
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Glide pour charger les images depuis Internet
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Room pour la base de données locale
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    // Lottie pour les animations
    implementation("com.airbnb.android:lottie:6.1.0")
}