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

The modification mainly focuses on the timeout of its method `com.github.alexthe666.citadel.web.WebHelper.getURLContents`. Idealy, the method should be asynchronous and non-blocking, and this might be implemented in the future.

For now, in this repository, the timeout `ConnectTimeout` and `ReadTimeout` are both set to 3000 milliseconds. Just feel free to set them to other preferred values before you build your own version.

## Purpose

Since Citadel is a library mod that is used in a wide range of  mods that may be included in packs, it can be frequently installed on servers. Due to many reasons, the Internet connection can be unstable or controlled by some rules, which will cause the update-checks, patreon requests, etc to fail.

We all know most of the mods can totally work perfectly without any Internet connection, as it's just a part of local game modification. But they still need to do update checks to notice users and keep itself updated, and they still need to fetch patreon list to show. These Internet requests do no harm if they are [done *asynchronously*](https://stackoverflow.com/questions/3142915/how-do-you-create-an-asynchronous-http-request-in-java), therefore non-blocking.

However, sometimes we can also see web requests done synchronously, [as in Citadel](https://github.com/AlexModGuy/Citadel/blob/master/src/main/java/com/github/alexthe666/citadel/web/WebHelper.java). The difference it made can be subtle if the quality connection is quite high, but it can also block the whole startup process [for over 10 minutes](https://github.com/AlexModGuy/Citadel/issues/145) if there's some limit in the connection or the quality is poor.

It's OK for local players or online clients, because the former barely experience *that* poor Internet connection at home or other places suitable for gaming, or they can just think it as some sort of lag instead of thread blocking caused by Internet connection. And the latter will not see any of the problem at all, because the whole process is done on  server startup. *However, if it's not done on startup but in game running, the server might be unprecedentedly disappointing because the whole world will then lag randomly, depending on the network quality.*

Considering the Internet connection quality varies among servers  and actually the blocking of startup is unreasonable and time-wasting, this project helps server owners with similar issues get the version _with_ the timeout. And 3 seconds (by default) is quite enough for minor web requests.

## Where Does the Project Structure From?

For previous versions of Citadel (e.g. 1.16.5), the  structure is based on the Forge MDK of the corresponding Minecraft version. The complete code is attained from decompiling the `deobf`ed file provided in [Curseforge download page](https://www.curseforge.com/minecraft/mc-mods/citadel/files), using [Vineflower](https://github.com/Vineflower/vineflower) decompiler.

For versions available in [the repository](https://github.com/AlexModGuy/citadel) (e.g. 1.19.2 and 1.19.4), the structure directly comes from the branches of the repository.

## Build Your Own Version

Citadel code of different versions is placed under the respective branch. Firstly switch to the branch and download the repository. Then run `./gradlew build` in the directory and grab the files in `build/libs`.

>[!IMPORTANT]
> To ensure you're using the right version of Gradle, please use the wrapped gradle (`./gradlew`) at repository root rather than your locally installed one.
> 
> Also, please make sure you have compatible version of Java installed for Gradle used to run the build task.

## License

LGPL v3