# Kustom API #
Kustom API is the core library to create Play Store Skin packages for Kustom the most advanced Android Wallpaper Engine!

# Basic Usage
The best way is to start from the sample application available at: https://bitbucket.org/frankmonza/kustomskinsample

# Manual Setup
If you want to integrate it manually into your project just add this to your `build.gradle` file:
```gradle
dependencies {
    implementation 'org.bitbucket.frankmonza:kustomapi:+'
}
```

Then edit your Android Manifest and add the provider specifying what kind of data your are going to offer
```xml
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

# Preset Info
If you need to add Preset Informations in your dashboard you can load easily using the Info Loader class, first add the preset library:
```gradle
dependencies {
    implementation 'org.bitbucket.frankmonza:kustompreset:+'
}
```

Then, for example, if you need to get the info for a widget called "awezome.kwgt" stored in your asset folder it will be enought to do:
```java
PresetInfoLoader.create(new AssetPresetFile("widgets/awezome.kwgt"))
    .load(context, new PresetInfoLoader.Callback() {
        @Override
        public void onInfoLoaded(PresetInfo info) {
            // Do something when data is loaded, for example set it to a recycle view holder, check PresetInfo class for more fields
            holder.setTitle(info.getTitle());
            holder.setAuthor(info.getAuthor());
        } 
    });
```

# Image preview with Glide library
If you are using Glide library you can easily load Kustom previews by first including preset library along with Glide basic libs:
```gradle
dependencies {
    implementation 'org.bitbucket.frankmonza:kustompreset:+'
    implementation 'com.github.bumptech.glide:glide:+'
    annotationProcessor 'com.github.bumptech.glide:compiler:+'
}
```

Then implement an empty class that extends AppGlideModule in your app anywhere, something like:
```java
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
}
```

Finally use Glide to load the previews, for example:
```java
Glide.with(context)
    .asBitmap()
    .load(new PresetFile("widgets/awezome.kwgt")
    .into(R.id.myimageview);
```
