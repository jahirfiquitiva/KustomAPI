# Kustom API #
Kustom API is the core library to create Play Store Skin packages for Kustom the most advanced Android Wallpaper Engine!

# Basic Usage
The best way is to start from the sample application available at: https://bitbucket.org/frankmonza/kustomskinsample

# Manual Setup
If you want to integrate it manually into your project just add this to your `build.gradle` file:
```
dependencies {
    compile 'org.bitbucket.frankmonza:kustomapi:+'
}
```

Then edit your Android Manifest and add the provider specifying what kind of data your are going to offer
```
<provider
     android:name="org.kustom.api.Provider"
     android:authorities="pick.your.favourite.provider.name"
     android:exported="true"
     >
     <intent-filter>
         <action android:name="org.kustom.provider.WALLPAPERS" />
     </intent-filter>
</provider>
```
