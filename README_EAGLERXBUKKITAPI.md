# EaglercraftXBukkitAPI

### [`gateway/EaglercraftXBukkitAPI/EaglercraftXBukkitAPI-Latest.jar`](gateway/EaglercraftXBukkitAPI/EaglercraftXBukkitAPI-Latest.jar)

### "EaglerXBukkitAPI" is a Bukkit plugin and protocol to allow Bukkit plugins to easily communicate with an EaglerXBungee or EaglerXVelocity instance, its purpose is to provide a standardized interface for creating complex Bukkit plugins that tightly integrate with EaglercraftX features, that would otherwise require developers to also create a BungeeCord/Velocity plugin to act as a "middle man" for sending certain packets or listening for certain events.

## Compiling EaglerXBukkitAPI

Minimum JDK version is 8, since what we are doing here is actually fully within the limitations of the official Bukkit API, we have actually set up Gradle correctly for once. No dependencies have to be downloaded manually, just run `gradlew jar` and you are done.

## Configuration Files

EaglerXBukkitAPI has no configuration files, however you must to edit `settings.yml` on EaglerXBungee/EaglerXVelocity to add or change `enable_backend_rpc_api` to `true` first in order to enable it.

## Initializing Connections

EaglerXBukkitAPI uses Google Commons (Guava) futures in order to implement a system that is similar to JavaScript promises to asynchronously complete tasks without blocking any threads. You should never block and wait for a task to complete synchronously, use the `addCallback` function with an anonymous inner class instead. Here is an example of how to correctly initialize an EaglerXBukkitAPI connection:

```java
// import net.lax1dude.eaglercraft.v1_8.plugin.bukkit_rpc_helper.api.IEaglerXBukkitAPI;
@EventHander
public void onPlayerJoinEvent(PlayerJoinEvent evt) {
    IEaglerXBukkitAPI.createAPI(evt.getPlayer())
            .addCallback(new FutureCallback<IEaglerXBukkitAPI>() {
        @Override
        public void onSuccess(IEaglerXBukkitAPI result) {
            // Do stuff with result
        }
        @Override
        public void onFailure(Throwable t) {
            // Something went wrong
        }
    });
}
```

You can call `createAPI` as many times as you want, subsequent calls will return the existing `IEaglerXBukkitAPI` handle instead of creating a new one.

## Making a Request

Here is a very simple example of using EaglerXBukkitAPI to determine the name of the website (origin) a player is using, assuming you've already used `createAPI` to initialize a new connection:

```java
// ...
public void onSuccess(IEaglerXBukkitAPI apiObj) {
    apiObj.requestPlayerOrigin().addCallback(new FutureCallback<ResponseString>() {
        @Override
        public void onSuccess(ResponseString result) {
            System.out.println("Player's origin is: " + result.string);
        }
        @Override
        public void onFailure(Throwable t) {
            // Something went wrong
        }
    });
}
```

## Event Listeners

Here is an example of how to use EaglerXBukkitAPI to remotely listen for server info webview events on an EaglerXBungee/EaglerXVelocity instance from a Bukkit plugin:

```java
// ...
public void onSuccess(IEaglerXBukkitAPI apiObj) {
    apiObj.addEventListener(EnumSubscribeEvents.EVENT_WEBVIEW_OPEN_CLOSE,
        new IEaglerRPCEventListener<EventWebViewOpenClose>() {
            public void handleEvent(IEaglerXBukkitAPI api, EnumSubscribeEvents eventType,
                    EventWebViewOpenClose eventData) {
                // Handle open/close events
            }
        });
    apiObj.addEventListener(EnumSubscribeEvents.EVENT_WEBVIEW_MESSAGE,
        new IEaglerRPCEventListener<EventWebViewMessage>() {
            public void handleEvent(IEaglerXBukkitAPI api, EnumSubscribeEvents eventType,
                    EventWebViewMessage eventData) {
                // Handle messages
            }
        });
    apiObj.subscribeEvents(
            EnumSubscribeEvents.EVENT_WEBVIEW_OPEN_CLOSE,
            EnumSubscribeEvents.EVENT_WEBVIEW_MESSAGE
        );
}
```

The rest of EaglerXBukkitAPI should be self explanatory, its just another EaglerXBungeeAPIHelper/EaglerXVelocityAPIHelper.
