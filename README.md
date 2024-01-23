# citadel-fix-conn

This is the unofficial modified version of [Citadel](https://github.com/AlexModGuy/citadel).

## Modification Notes

```diff
// WebHelper.java

try {
         URL url = new URL(urlString);
         URLConnection connection = url.openConnection();
+        connection.setConnectTimeout(3000);
+        connection.setReadTimeout(3000);
         InputStream stream = connection.getInputStream();
         InputStreamReader reader = new InputStreamReader(stream);
         return new BufferedReader(reader);
```

The modification mainly focuses on the timeout of its method `com.github.alexthe666.citadel.web.WebHelper.getURLContents`. In this repository, the timeout `ConnectTimeout` and `ReadTimeout` are both set to 3000 milliseconds. Just feel free to set them to other preferred values before you build your own version.

## About the Project

This project's structure is based on the Forge MDK of the corresponding Minecraft version. The complete code is attained from decompiling the `deobf`ed file provided in [Curseforge download page](https://www.curseforge.com/minecraft/mc-mods/citadel/files), using [Vineflower](https://github.com/Vineflower/vineflower) decompiler.

## Build

>[!IMPORTANT]
> To ensure you're using the right version of Gradle, please use the wrapped gradle (`./gradlew`) at repository root rather than your locally installed one.
> 
> Also, please make sure you have compatible version of Java installed for Gradle used to run the build task.

Run

```shell
./gradlew build
```

and grab the file in `build/libs`.

## License

LGPL v3