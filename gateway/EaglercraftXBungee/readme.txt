Plugin for eaglercraft on bungeecord

Not using gradle to give more direct access to bungeecord's internals, as gradle only provides a dummy jar containing the api. 

EaglercraftXBungee requires netty's websocket client/server, which is already in the production bungeecord jar so it's ideal to compile directly against the real jar

Simply link "src/main/java" and "src/main/resources" as source folders, and then add the latest version of bungeecord jar for minecraft 1.8 to the build path.

To build, export the source folders as a JAR and export the JAR to contain all the classes found in the JARs in "deps" within it, but not including the classes from the actual bungeecord jar