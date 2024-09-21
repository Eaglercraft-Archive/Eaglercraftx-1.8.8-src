# EaglercraftXBungee

### [`gateway/EaglercraftXBungee/EaglerXBungee-Latest.jar`](gateway/EaglercraftXBungee/EaglerXBungee-Latest.jar)

### "EaglerXBungee" is a plugin that allows the EaglercraftX 1.8 client to join BungeeCord servers, with an optional authentication system if online-mode is enabled. This is not a setup guide, this document is intended to be used as reference for EaglerXBungee's configuration files and provide some surface-level information meant for plugin developers.

## Compiling EaglerXBungee

Minimum JDK version is 8, as of 1.3.0 we are finally using Gradle to compile EaglerXBungee instead of compiling it all manually, however you still need to manually download the latest version of BungeeCord and name it "BungeeCord.jar" and place it in the `deps` folder first before you continue. We just don't care enough to actually use Gradle correctly to download all the dependencies automatically when they are all subject to change at any time as BungeeCord recieves updates upstream. Use the "jar" task to automatically compile the EaglerXBungee JAR file.

## Configuration Files

### NOTE: Currently, the plugin does NOT automatically update config files, if you can't find an option in one of the configuration files documented here, you most likely need to add it to the file yourself!

**The default contents of the config files for EaglerXBungee are stored in [`gateway/EaglercraftXBungee/src/main/resources/net/lax1dude/eaglercraft/v1_8/plugin/gateway_bungeecord/config`](gateway/EaglercraftXBungee/src/main/resources/net/lax1dude/eaglercraft/v1_8/plugin/gateway_bungeecord/config)**

### `settings.yml`

The settings.yml file is primarily used for configuring the built-in skin and cape service and certain connection options.

- **`server_name:`** String, default value is `'EaglercraftXBungee Server'`, sets the name of this EaglercraftX server that is sent with query responses and used for the default "404 websocket upgrade failure" page.
- **`server_uuid:`** String, default value is randomized, sets the UUID of this EaglercraftX server to send with query responses, has no official uses outside of server lists.
- **`websocket_connection_timeout:`** Number, default value is `15000` milliseconds, sets how long a WebSocket connection can last without a ping before being disconnected.
- **`websocket_handshake_timeout:`** Number, default value is `5000` milliseconds, sets how long a connection can sit in the handshake phase before being disconnected.
- **`builtin_http_server_timeout:`** Number, default value is `10000` milliseconds, sets how long an HTTP request to the built-in HTTP server can remain open before being forcefully disconnected.
- **`http_websocket_compression_level:`** Number, default value is `6`, sets the ZLIB compression level (0-9) to use for compressing websocket frames, set to 0 to disable if HTTP compression is already handled through a reverse proxy. You almost definitely need some level of compression for the game to be playable on WiFi networks.
- **`download_vanilla_skins_to_clients:`** Boolean, default value is `true`, sets if the server should download the textures of custom skulls and skins of vanilla online-mode players from Mojang's servers to cache locally and send to all EaglercraftX clients on the server that attempt to render them.
- **`valid_skin_download_urls:`** List of strings, default includes only `'textures.minecraft.net'`, sets the allowed domains to download custom skulls and skins from that are requested by EaglercraftX clients, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`uuid_lookup_ratelimit_player:`** Integer, default value is `50`, limit of how many Mojang API UUID-to-profile lookups a single player is allowed to trigger per minute, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`uuid_lookup_ratelimit_global:`** Integer, default value is `175`, limit of how many Mojang API UUID-to-profile lookups the entire server is allowed to perform per minute, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_download_ratelimit_player:`** Integer, default value is `1000`, limit of how many texture downloads a single player is allowed to trigger per minute, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_download_ratelimit_global:`** Integer, default value is `30000`, limit of how many texture downloads the entire server is allowed to perform per minute, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_cache_db_uri:`** String, default value is `'jdbc:sqlite:eaglercraft_skins_cache.db'`, can be used to change the location of the SQLite database used as a cache for skins and profiles, or to make the server use an entirely different SQL database like MySQL to store the data instead, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_cache_keep_objects_days:`** Integer, default value is `45`, sets the max age for textures (skin files) stored in the skin cache database, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_cache_keep_profiles_days:`** Integer, default value is `7`, sets the max age for player profiles stored in the skin cache database, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_cache_max_objects:`** Integer, default value is `32768`, sets the max number of textures (skin files) stored in the skin cache database before the oldest textures begin to be deleted, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_cache_max_profiles:`** Integer, default value is `32768`, sets the max number of player profiles stored in the skin cache database before the oldest profiles begin to be deleted, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`skin_cache_antagonists_ratelimit:`** Integer, default value is `15`, sets the lockout limit for failing skin lookup requests, intended to reduce the effectiveness of some of the more simplistic types denial of service attacks that skids may attempt to perform on the skin download system, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`sql_driver_class:`** String, default value is `'internal'`, which is currently evaluated to `'org.sqlite.JDBC'`, can be used to set the name of the JDBC driver class to use for connecting to the `skin_cache_db_uri` database
- **`sql_driver_path:`** String, default value is `'internal'`, can be used to set the name of the external JAR file where the JDBC driver class to use for connecting to the `skin_cache_db_uri` database can be found, the default `'internal'` value downloads the sqlite-jdbc JAR from maven and loads it automatically, only relevant if `download_vanilla_skins_to_clients` is enabled.
- **`eagler_players_vanilla_skin:`** String, default value is `''` but was originally `'lax1dude'`, can be used to set the skin to apply to EaglercraftX players when a player on Minecraft Java Edition sees them in game. The value is the username of a premium Minecraft account to use the skin from. You cannot use a local PNG file due to the profile signature requirements in vanilla Minecraft clients.
- **`enable_is_eagler_player_property:`** Boolean, default value is `true`, can be used to control if the `isEaglerPlayer` GameProfile property should be added to EaglercraftX players, this property is used to ensure that EaglercraftX players always only display their custom skins when viewed by another EaglercraftX players on the server instead of showing the skin attached to their Java Edition username, but this property also cause plugins like ViaVersion to crash.
- **`disable_voice_chat_on_servers:`** List of strings, default value is nothing (`[]`), contains a list of names of registered servers on your BungeeCord proxy that voice chat should show up as "disabled" on. Note that to disable voice globally you should modify `listeners.yml` instead.
- **`disable_fnaw_skins_everywhere:`** Boolean, default value is `false`, can be used to globally disable FNAW skins if your players bitch about them a lot and are too lazy to just disable the FNAW skins locally on their clients.
- **`disable_fnaw_skins_on_servers:`** List of strings, default value is nothing (`[]`), contains a list of names of registered servers on your BungeeCord proxy that the FNAW skins should be disabled on. Good for explicitly disabling them for PVP but allowing them everywhere else.
- **`enable_backend_rpc_api:`** Boolean, default value is `false`, if support for servers running the EaglerXBukkitAPI plugin should be enabled or not.

### `listeners.yml`

Defines one or more "listeners" (open ports) for EaglercraftX players to use to join the server. Each listener supports the following configuration options, a lot of which you will already be familiar with if you've ever set up a BungeeCord for a Java Edition server before:

- **`address:`** String, default value is `0.0.0.0:8081`, sets the primary IPv4/port for EaglerXBungee to listen on.
- **`address_v6:`** String, default value is `'null'`, sets the primary IPv6/port for EaglerXBungee to listen on.
- **`max_players:`** Integer, default value is `60`, sets the maximum number of players that can join the server through this listener, set to `-1` to disable the limit.
- **`tab_list:`** String, default value is `GLOBAL_PING`, sets the option with the same name on the underlying BungeeCord listener, currently not used by EaglercraftX in any way.
- **`default_server:`** String, default value is `lobby`, sets the name of the default server for players to be sent to when they first connect to this listener.
- **`force_default_server:`** Boolean, default value is `false`, sets if players should always be connected to `default_server` when they connect to this listener.
- **`forward_ip:`** Boolean, default value is `false`, sets if connections to this listener will use an HTTP header to forward the player's real IP address from a reverse proxy (or CloudFlare) to the BungeeCord server. This is required for EaglerXBungee's rate limiting and a lot of plugins to work correctly if they are used behind a reverse HTTP proxy or CloudFlare.
- **`forward_ip_header:`** String, default value is `X-Real-IP`, sets the name of the request header that contains the player's real IP address if the `forward_ip` option is enabled. This option is commonly set to `X-Forwarded-For` or `CF-Connecting-IP` for a lot of server setups.
- **`redirect_legacy_clients_to:`** String, default value is `'null'`, sets the WebSocket address to redirect legacy Eaglercraft 1.5.2 clients to if they mistakenly try to join the server through this listener.
- **`server_icon:`** String, default value is `server-icon.png`, sets the name of the 64x64 PNG file to display as this listener's server icon, relative to the working directory of the BungeeCord proxy server.
- **`server_motd:`** List of up to 2 strings, default value is `'&6An EaglercraftX server'`, sets the contents of the listener's MOTD, which is the text displayed along with the `server_icon` when players add this server's listener address to their client's Multiplayer menu server list.
- **`allow_motd:`** Boolean, default value is `true`, if this listener should respond to MOTD queries or not.
- **`allow_query:`** Boolean, default value is `true`, if this listener should respond to all other types of queries or not.
- **`allow_protocol_v3:`** Boolean, default value is `true`, if this listener should allow clients using the v1/v2/v3 protocols to join (pre-u37 clients).
- **`allow_protocol_v4:`** Boolean, default value is `true`, if this listener should allow clients using the v4 protocol to join (post-u37 clients).
- **`protocol_v4_defrag_send_delay:`** Integer, default value is `10`, the number of milliseconds to wait before flushing all pending EaglercraftX plugin message packets, saves bandwidth by combining multiple messages into a single plugin message packet. Setting this to `0` has the same effect on clientbound packets as setting `eaglerNoDelay` to `true` does on a post-u37 client for all serverbound packets.
- **`allow_cookie_revoke_query:`** Boolean, default value is `true`, If this listener should accept queries from post-u37 clients to revoke session tokens, you need to create your own BungeeCord plugin to go with EaglerXBungee that handles the `EaglercraftRevokeSessionQueryEvent` event it fires in order for this feature to work correctly.
- **`request_motd_cache:`** Section that defines caching hints for server lists that cache the MOTD via the `MOTD.cache` query. As far as we know, not even the official Eaglercraft Server List on eaglercraft.com currently pays attention to these hints or attempts to cache MOTDs, so they can be ignored for now.
    - **`cache_ttl:`** Integer, default value is `7200`, sets how many seconds for the server list to store the MOTD in cache.
    - **`online_server_list_animation:`** Boolean, default is `false`, if the MOTD should be cached in an "animated format" that is yet to be standardized.
    - **`online_server_list_results:`** Boolean, default is `true`, if the MOTD should be cached when shown in search results.
    - **`online_server_list_trending:`** Boolean, default is `true`, if the MOTD should be cached if the server makes it to the top of the homepage.
    - **`online_server_list_portfolios:`** Boolean, default is `false`, if the MOTD should be cached when viewing more details about the specific server.
- **`http_server:`** Section that defines settings for the integrated HTTP server, used to make the listener behave as a normal HTTP server when a non-WebSocket request is recieved (like when the listener address is entered into a browser's address bar). These options can be used to replace the "404 WebSocket Upgrade Failure" message with a custom HTML file instead.
    - **`enabled:`** Boolean, default value is `false`, if this is set to true then the default "404 WebSocket Upgrade Failure" page will be disabled and replaced with the integrated file-based HTTP server, perfect for hosting a copy of the EaglercraftX client.
    - **`root:`** String, default value is `web`, sets the folder that contains the HTTP server's document root, this is relative to the `plugins/EaglercraftXBungee` folder where the config files are stored.
    - **`page_404_not_found:`** String, default value is `'default'`, can be used to replace the HTTP server's 404 page.
    - **`page_index_name:`** List of strings, default values are `'index.html'` and `'index.htm'`, can be used to specify the name of index.html.
- **`allow_voice:`** Boolean, default is `false`, sets if voice should show up as "disabled" for players using this listener. Voice is not recommended for public servers since little to no consideration was given to actually validating the contents of signaling packets sent between clients.
- **`ratelimit:`** Section containing rate limiting configurations for several different connection types.
    - **`ip:`** Global ratelimit imposed on all connection types.
    - **`login:`** Sets ratelimit on login (server join) attempts.
    - **`motd:`** Sets ratelimit on MOTD query types.
    - **`query:`** Sets ratelimit on all other query types.
        - **`enable:`** If the rate limit (ip/login/motd/query) should be enabled.
        - **`period:`** Sets the period in the number of seconds.
        - **`limit:`** Sets the number of requests a single IP address can send in `period` seconds before being limited.
        - **`limit_lockout:`** Sets the number of requests a single IP address can send in `period` seconds before being locked out.
        - **`lockout_duration:`** Sets the total number of seconds a "lock out" should last on this limiter.

### `authservice.yml`

The authservice.yml file is used for configuring the built-in online mode authentication service included with the plugin or to integrate with a 3rd party authentication system provided by another plugin.

- **`enable_authentication_system:`** Boolean, default is `true`, if the events for the authentication protocol should be enabled.
- **`use_onboard_eaglerx_system:`** Boolean, default is `true`, if the built-in online mode authentication system should be enabled.
- **`auth_db_uri:`** String, default value is `'jdbc:sqlite:eaglercraft_auths.db'`, can be used to change the location of the SQLite database used for the built-in online mode authentication system, or to make the server use an entirely different SQL database like MySQL to store the data instead.
- **`sql_driver_class:`** String, default value is `'internal'`, see the description of the `settings.yml` option with the same name.
- **`sql_driver_path:`** String, default value is `'internal'`, see the description of the `settings.yml` option with the same name.
- **`password_prompt_screen_text:`** String, default value is `'Enter your password to join:'`, text displayed on the EaglercraftX client's password screen when joining the server with the built-in online mode authentication system.
- **`wrong_password_screen_text:`** String, default value is `'Password Incorrect!'`, text displayed if the wrong password is entered on the EaglercraftX client's password screen when joining the server with the built-in online mode authentication system.
- **`not_registered_screen_text:`** String, default value is `'You are not registered on this server!'`, text displayed when joining the server with the built-in online mode authentication system when using an account that has not been registered.
- **`eagler_command_name:`** String, default value is `'eagler'`, the name of the command to use for registering and/or logging in when joining the server with the built-in online mode authentication system.
- **`use_register_command_text:`** String, default value is `'&aUse /eagler to set an Eaglercraft password on this account'`, localization for when players use the `/eagler` command on the server.
- **`use_change_command_text:`** String, default value is `'&bUse /eagler to change your Eaglercraft password'`, localization for when players use the `/eagler` command on the server.
- **`command_success_text:`** String, default value is `'&bYour eagler password was changed successfully.'`, localization for when players use the `/eagler` command on the server.
- **`last_eagler_login_message:`** String, default value is `'Your last Eaglercraft login was on $date from $ip'`, localization for when players join the server with the built-in online mode authentication system.
- **`too_many_registrations_message:`** String, default value is `'&cThe maximum number of registrations has been reached for your IP address'`, localization for when players use the `/eagler` command on the server.
- **`need_vanilla_to_register_message:`** String, default value is `'&cYou need to log in with a vanilla account to use this command'`, localization for when players use the `/eagler` command on the server.
- **`override_eagler_to_vanilla_skins:`** Boolean, default value is `false`, if players who join the server after registering with an online mode account should show the same skin as the online-mode account they registered with.
- **`max_registration_per_ip:`** Integer, default value is `-1`, if greater than 0 it specifies the max number of accounts that can be created per IP address on the server with the built-in online mode authentication system.

### `ice_servers.yml`

The ice_servers.yml file is used for configuring the set of STUN/TURN servers that clients on this server should use for voice chat. Beware the default "openrelayproject" TURN servers are no longer active as of 2024, most likely as a result of being the default ond only TURN servers shipped with every copy of Eaglercraft to ever use WebRTC in some way.

- **`voice_servers_no_passwd:`** List of strings, defines a set of STUN/TURN server URIs to use that don't require a username and password.
- **`voice_servers_passwd:`** Section of sections, defines a set of STUN/TURN server URIs to use that do require a username and password, along with the username and password to use with each one.

### `updates.yml`

The updates.yml file is used for configuring the decentralized and totally legal update system used by EaglercraftX clients.

- **`block_all_client_updates:`** Boolean, default value is `false`, can be used to completely disable the update system.
- **`discard_login_packet_certs:`** Boolean, default value is `false`, can be used to prevent the server from relaying random crowdsourced update certificates that were recieved from players who joined the server using signed clients.
- **`cert_packet_data_rate_limit:`** Integer, default value is `524288`, can be used to set the global rate limit for how many bytes per second of certificates the server should send to all players.
- **`enable_eagcert_folder:`** Boolean, default value is `true`, can be used to enable or disable the "eagcert" folder used for distributing specific certificates as locally provided .cert files
- **`download_latest_certs:`** Boolean, default value is `true`, can be used to automaticlly download the latest certificates to the "eagcert" folder
- **`download_certs_from:`** List of strings, defines the URLs to download the certificates from if `download_latest_certs` is enabled
- **`check_for_update_every:`** Integer, default value is `28800` seconds, defines how often to check the URL list for updated certificates

### `pause_menu/pause_menu.yml`

For EaglercraftX u37 and up, can be used for changing the appearance of the pause menu and a few other screens with custom icons and strings, also used for enabling the "Server Info" webview and configuring its contents.

- **`enable_custom_pause_menu:`** Boolean, default value is `false`, if pause menu customization should be enabled on supported clients or not
- **`server_info_button:`** Section, defines properties of the "Server Info" button, which is always hidden unless pause menu customization is enabled
    - **`enable_button:`** Boolean, default value is `true`, if the button should be shown or not
    - **`button_text:`** String, default value is `'Server Info'`, the text to display on the button, useful if you want to use this feature for something other than a "Server Info" button
    - **`button_mode_open_new_tab:`** Boolean, default value is `false`, can be used to make the "Server Info" button act as a hyperlink that opens a URL in a new tab instead of displaying content in an embedded webview iframe in the client.
    - **`server_info_embed_url:`** String, default value is `''`, sets the URL for the "Server Info" button to use if it should open a URL in a new tab or the webview instead of directly downloading the markup to display from the EaglerXBungee server itself over the WebSocket.
    - **`button_mode_embed_file:`** Boolean, default value is `true`, determines if the "Server Info" button should download the webview markup directly from the EaglerXBungee server over WebSocket instead of loading an external URL. Cannot be used with `button_mode_open_new_tab`!
    - **`server_info_embed_file:`** String, default value is `'server_info.html'`, sets the name of the local file/template containing the markup to display in the "Server Info" webview if it is not in URL mode.
    - **`server_info_embed_screen_title:`** String, default value is `'Server Info'`, sets the title string of the screen that displays the webview.
    - **`server_info_embed_send_chunk_rate:`** Integer, default value is `1`, defines how many chunks of server info data to send per 250ms when downloading the server info markup to a client.
    - **`server_info_embed_send_chunk_size:`** Integer, default value is `24576`, defines the size of each chunk of server info data when it is being downloaded to a client.
    - **`enable_template_macros:`** Boolean, default value is `true`, if the server info markup should be processed for any eagler template macros (defined like `` {% arg1 `arg2` ... %} ``)
    - **`server_info_embed_template_globals:`** Section, defines a list of additional global variables to use with the template processor
    - **`allow_embed_template_eval_macro:`** Boolean, default value is `false`, if the template processor should allow the "eval" macro to be used in the server info markup file (not to be confused with the JavaScript function, although there is never a good reason to use JavaScript's eval function in your code either)
    - **`enable_webview_javascript:`** Boolean, default value is `false`, if the server info webview should allow JavaScript to be executed or not. This will display an "allow JavaScript" screen to your players the first time they attempt to view it.
    - **`enable_webview_message_api:`** Boolean, default value is `false`, if the server info webview has JavaScript enabled and should be permitted to open a message channel back to your EaglerXBungee server to exchange arbitrary message packets. This can be used, for example, to implement a dynamic menu on your server using JavaScript and HTML that players can access through the server info webview that integrates directly with your gamemodes.
    - **`enable_webview_strict_csp:`** Boolean, default value is `true`, if the `csp` attribute on the webview iframe should be set or not for added security, beware this is not supported on all browsers and will be silently disabled when the client detects it as unsupported.
- **`discord_button:`** Section, can be used to turn the "Invite" (formerly "Open to LAN") button on the pause menu into a "Discord" button that players can click to join your discord server
    - **`enable_button:`** Boolean, default value is `true`, sets if the discord button should be enabled or not
    - **`button_text:`** String, default value is `'Discord'`, sets the text that should be displayed on the button
    - **`button_url:`** String, default value is `'https://invite url here'`, defines the URL to open when the button is pressed
- **`custom_images:`** Section, can be used to add icons to certain buttons, change the backgrounds of some screens, and add watermarks of your server to the inventory and pause menu and such if you're into that. For the best GPU compatibility, use only textures with powers of 2 as dimensions (such as 32x32 pixels), the width and height do not need to match, they just both need to be a power of 2. There is also a limit on the maximum size, icons can be no larger than 255x255 pixels (Effective max power-of-2 is 128x128). Color values will be downsampled to 16bpp and use a magic value to represent transparent pixels when the pause menu customization packet is sent to a client.
    - **`icon_title_L:`** String, default value is `''`, sets the icon to show on the left side of the pause menu screen's title
    - **`icon_title_R:`** String, default value is `''`, sets the icon to show on the right side of the pause menu screen's title
    - **`icon_backToGame_L:`** String, default value is `''`, sets the icon to show on the left side of the "Back to Game" button
    - **`icon_backToGame_R:`** String, default value is `''`, sets the icon to show on the right side of the "Back to Game" button
    - **`icon_achievements_L:`** String, default value is `''`, sets the icon to show on the left side of the "Achievements" button
    - **`icon_achievements_R:`** String, default value is `''`, sets the icon to show on the right side of the "Achievements" button
    - **`icon_statistics_L:`** String, default value is `''`, sets the icon to show on the left side of the "Statistics" button
    - **`icon_statistics_R:`** String, default value is `''`, sets the icon to show on the right side of the "Statistics" button
    - **`icon_serverInfo_L:`** String, default value is `''`, sets the icon to show on the left side of the server info button
    - **`icon_serverInfo_R:`** String, default value is `''`, sets the icon to show on the right side of the server info button
    - **`icon_options_L:`** String, default value is `''`, sets the icon to show on the left side of the "Options" button
    - **`icon_options_R:`** String, default value is `''`, sets the icon to show on the right side of the "Options" button
    - **`icon_discord_L:`** String, default value is `''`, sets the icon to show on the left side of the discord button
    - **`icon_discord_R:`** String, default value is `''`, sets the icon to show on the right side of the discord button
    - **`icon_disconnect_L:`** String, default value is `''`, sets the icon to show on the left side of the "Disconnect" button
    - **`icon_disconnect_R:`** String, default value is `''`, sets the icon to show on the right side of the "Disconnect" button
    - **`icon_background_pause:`** String, default value is `'test_image.png'`, sets the icon to show as a repeating pattern in the background of the pause menu and related screens. It is especially important for GPU compatibility for this one to be a power-of-2 sized texture.
    - **`icon_background_all:`** String, default value is `'test_image.png'`, sets the icon to show as a repeating pattern in the background of all other screens in the game. It is especially important for GPU compatibility for this one to be a power-of-2 sized texture.
    - **`icon_watermark_pause:`** String, default value is `''`, sets a watermark to show in the bottom-left corner of the pause menu
    - **`icon_watermark_all:`** String, default value is `''`, sets a watermark to show in the bottom-left corner of all other screens in the game

## Event Types

The events added by EaglerXBungee are located in the [`net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event`](gateway/EaglercraftXBungee/src/main/java/net/lax1dude/eaglercraft/v1_8/plugin/gateway_bungeecord/api/event) package and can be listened for the same way as you would for a regular BungeeCord event. When an EaglercraftX 1.8 player joins your server, all the regular BungeeCord login events are fired by EaglerXBungee to maintain compatibility with other existing BungeeCord plugins, however EaglerXBungee also adds several of its own event types to allow additional Eaglercraft specific features to be accessible through the main BungeeCord event bus as well.

- **`EaglercraftWebSocketOpenEvent`**  Event that is fired when a new WebSocket connection is first opened to the server (regardless if its a query or actual player login attempt) useful for quickly filtering out new connections based on a specific origin or user agent header.
- **`EaglercraftClientBrandEvent`** Event that is fired when an EaglercraftX player joins, it contains the Eaglercraft client's "brand" and "version" strings along with the origin and user agent headers that can be used to detect 90% of the currently existing skid clients thanks to lax1dude making the base client self-snitch these commonly modified strings.
- **`EaglercraftIsAuthRequiredEvent`** Event that is fired when an EaglercraftX player attempts to join the server while the authentication system is enabled and `use_onboard_eaglerx_system` is `false`, used for implementing custom authentication systems.
- **`EaglercraftHandleAuthPasswordEvent`** Event that is fired when an EaglercraftX player enters a password into the "Authentication Required" screen while the authentication system is enabled and `use_onboard_eaglerx_system` is `false`, used for implementing custom authentication systems.
- **`EaglercraftHandleAuthCookieEvent`** Event that is fired when an EaglercraftX player joins the server with cookies set and while authentication system is enabled and `use_onboard_eaglerx_system` is `false`, you must set cookie auth as allowed while handling "EaglercraftIsAuthRequiredEvent" first for this event to actually be fired, used for implementing custom authentication systems that use cookies to store a session token for auto login.
- **`EaglercraftRevokeSessionQueryEvent`** Event that is fired when a player uses the "Revoke Session Token" feature in a u37 client to invalidate a cookie that was set on their client with the "revoke query supported" bit. Make sure to enable session revoke queries in listeners.yml!
- **`EaglercraftRegisterSkinEvent`** Event that is fired when an EaglercraftX player's skin is recieved, can be used to analyze or modify or replace the skin with a different texture or preset ID if needed. Note that players using pre-u37 clients may not see the modified/replaced skin.
- **`EaglercraftRegisterCapeEvent`** Event that is fired when an EaglercraftX player's cape is recieved, can be used to analyze or modify or replace the cape with a different texture or preset ID if needed. Use preset ID 0 to disable their cape entirely. Note that players using pre-u37 clients may not see the modified/replaced cape.
- **`EaglercraftMOTDEvent`** Event that is fired when a MOTD query request is recieved, used for implement a custom server MOTD handler, or implementing an animated MOTD like the EaglerMOTD plugin.
- **`EaglercraftVoiceStatusChangeEvent`** Event that is fired when `allow_voice` is enabled and a player transitions between voice states (SERVER_DISABLE, DISABLED, ENABLED) cannot be cancelled so it is mostly just useful for logging or displaying some "Rules" in chat.
- **`EaglercraftWebViewChannelEvent`** Event that is fired when the server info webview is open and JavaScript is enabled and the webview opens/closes a new message channel to EaglerXBungee.
- **`EaglercraftWebViewMessageEvent`** Event that is fired when the server info webview is open and JavaScript is enabled and the webview has already opened a channel to EaglerXBungee and a new message is recieved on that open channel.

## Registering Queries

If you would like to add your own custom `Accept:` query handlers to the proxy (along with MOTD, version, and session revoke) you can register them at startup using the register functions provided by the [`EaglerQueryHandler`](gateway/EaglercraftXBungee/src/main/java/net/lax1dude/eaglercraft/v1_8/plugin/gateway_bungeecord/api/query/EaglerQueryHandler.java) class.

## EaglerXBungeeAPIHelper

To help make plugin development easier, a class called [`EaglerXBungeeAPIHelper`](gateway/EaglercraftXBungee/src/main/java/net/lax1dude/eaglercraft/v1_8/plugin/gateway_bungeecord/api/EaglerXBungeeAPIHelper.java) is included that defines dozens of helper functions for easily and safely interacting with EaglercraftX clients. This is to enable developers to program plugins for EaglerXBungee servers with minimal knowledge of the actual underlying protocol used by the client for skins and capes and voice and other exclusive features. Its recommended to convert all your existing code to use the `EaglerXBungeeAPIHelper` instead of whatever packet hacks you were doing before it was added when you migrate your network to EaglerXBungee 1.3.0+. All your existing packet hacks will be broken anyway unless you configure the server to force all u37+ clients to use protocol V3 instead.
