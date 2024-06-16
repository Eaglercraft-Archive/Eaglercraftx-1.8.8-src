package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command.CommandConfirmCode;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command.CommandDomain;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command.CommandEaglerPurge;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command.CommandEaglerRegister;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.command.CommandRatelimit;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.handlers.EaglerPacketEventListener;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.handlers.EaglerPluginEventListener;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.web.HttpWebServer;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.shit.CompatWarning;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.BinaryHttpClient;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.CapeServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.ISkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice.VoiceService;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.BungeeCord;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class EaglerXBungee extends Plugin {

	public static final String NATIVE_BUNGEECORD_BUILD = "1.21-R0.1-SNAPSHOT:8a88ce4:1849";
	public static final String NATIVE_WATERFALL_BUILD = "1.21-R0.1-SNAPSHOT:de8345a:579";
	
	static {
		CompatWarning.displayCompatWarning();
	}
	
	private static EaglerXBungee instance = null;
	private EaglerBungeeConfig conf = null;
	private EventLoopGroup eventLoopGroup;
	private EventLoopGroup eventLoopGroupBoss;
	private Collection<Channel> openChannels;
	private Timer closeInactiveConnections = null;
	private Timer skinServiceTasks = null;
	private Timer authServiceTasks = null;
	private final ChannelFutureListener newChannelListener;
	private ISkinService skinService;
	private CapeServiceOffline capeService;
	private VoiceService voiceService;
	private DefaultAuthSystem defaultAuthSystem;
	
	public EaglerXBungee() {
		instance = this;
		openChannels = new LinkedList();
		newChannelListener = new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture ch) throws Exception {
				synchronized(openChannels) { // synchronize whole block to preserve logging order
					if(ch.isSuccess()) {
						EaglerXBungee.logger().info("Eaglercraft is listening on: " + ch.channel().attr(EaglerPipeline.LOCAL_ADDRESS).get().toString());
						openChannels.add(ch.channel());
					}else {
						EaglerXBungee.logger().severe("Eaglercraft could not bind port: " + ch.channel().attr(EaglerPipeline.LOCAL_ADDRESS).get().toString());
						EaglerXBungee.logger().severe("Reason: " + ch.cause().toString());
					}
				}
			}
		};
	}
	
	@Override
	public void onLoad() {
		try {
			eventLoopGroup = ((BungeeCord) getProxy()).eventLoops;
		} catch (NoSuchFieldError e) {
			try {
				eventLoopGroup = (EventLoopGroup) BungeeCord.class.getField("workerEventLoopGroup").get(getProxy());
				eventLoopGroupBoss = (EventLoopGroup) BungeeCord.class.getField("bossEventLoopGroup").get(getProxy());
			} catch (IllegalAccessException | NoSuchFieldException ex) {
				throw new RuntimeException(ex);
			}
		}
		reloadConfig();
	}

	@Override
	public void onEnable() {
		PluginManager mgr = getProxy().getPluginManager();
		mgr.registerListener(this, new EaglerPluginEventListener(this));
		mgr.registerListener(this, new EaglerPacketEventListener(this));
		mgr.registerCommand(this, new CommandRatelimit());
		mgr.registerCommand(this, new CommandConfirmCode());
		mgr.registerCommand(this, new CommandDomain());
		EaglerAuthConfig authConf = conf.getAuthConfig();
		conf.setCracked(!BungeeCord.getInstance().getConfig().isOnlineMode() || !authConf.isEnableAuthentication());
		if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
			if(!BungeeCord.getInstance().getConfig().isOnlineMode()) {
				getLogger().severe("Online mode is set to false! Authentication system has been disabled");
				authConf.triggerOnlineModeDisabled();
			}else {
				mgr.registerCommand(this, new CommandEaglerRegister(authConf.getEaglerCommandName()));
				mgr.registerCommand(this, new CommandEaglerPurge(authConf.getEaglerCommandName()));
			}
		}
		getProxy().registerChannel(SkinService.CHANNEL);
		getProxy().registerChannel(CapeServiceOffline.CHANNEL);
		getProxy().registerChannel(EaglerPipeline.UPDATE_CERT_CHANNEL);
		getProxy().registerChannel(VoiceService.CHANNEL);
		getProxy().registerChannel(EaglerPacketEventListener.FNAW_SKIN_ENABLE_CHANNEL);
		getProxy().registerChannel(EaglerPacketEventListener.GET_DOMAIN_CHANNEL);
		startListeners();
		if(closeInactiveConnections != null) {
			closeInactiveConnections.cancel();
			closeInactiveConnections = null;
		}
		if(skinServiceTasks != null) {
			skinServiceTasks.cancel();
			skinServiceTasks = null;
		}
		closeInactiveConnections = new Timer("EaglerXBungee: Network Tick Tasks");
		closeInactiveConnections.scheduleAtFixedRate(EaglerPipeline.closeInactive, 0l, 250l);
		boolean downloadSkins = conf.getDownloadVanillaSkins();
		if(downloadSkins) {
			if(skinService == null) {
				skinService = new SkinService();
			}else if(skinService instanceof SkinServiceOffline) {
				skinService.shutdown();
				skinService = new SkinService();
			}
		} else {
			if(skinService == null) {
				skinService = new SkinServiceOffline();
			}else if(skinService instanceof SkinService) {
				skinService.shutdown();
				skinService = new SkinServiceOffline();
			}
		}
		skinService.init(conf.getSkinCacheURI(), conf.getSQLiteDriverClass(), conf.getSQLiteDriverPath(),
				conf.getKeepObjectsDays(), conf.getKeepProfilesDays(), conf.getMaxObjects(), conf.getMaxProfiles());
		if(skinService instanceof SkinService) {
			skinServiceTasks = new Timer("EaglerXBungee: Skin Service Tasks");
			skinServiceTasks.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						skinService.flush();
					}catch(Throwable t) {
						logger().log(Level.SEVERE, "Error flushing skin cache!", t);
					}
				}
			}, 1000l, 1000l);
		}
		capeService = new CapeServiceOffline();
		if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
			try {
				defaultAuthSystem = DefaultAuthSystem.initializeAuthSystem(authConf);
			}catch(DefaultAuthSystem.AuthSystemException ex) {
				logger().log(Level.SEVERE, "Could not load authentication system!", ex);
			}
			if(defaultAuthSystem != null) {
				authServiceTasks = new Timer("EaglerXBungee: Auth Service Tasks");
				authServiceTasks.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							defaultAuthSystem.flush();
						}catch(Throwable t) {
							logger().log(Level.SEVERE, "Error flushing auth cache!", t);
						}
					}
				}, 60000l, 60000l);
			}
		}
		if(conf.getEnableVoiceChat()) {
			voiceService = new VoiceService(conf);
			logger().warning("Voice chat enabled, not recommended for public servers!");
		}else {
			logger().info("Voice chat disabled, add \"allow_voice: true\" to your listeners to enable");
		}
	}

	@Override
	public void onDisable() {
		PluginManager mgr = getProxy().getPluginManager();
		mgr.unregisterListeners(this);
		mgr.unregisterCommands(this);
		getProxy().unregisterChannel(SkinService.CHANNEL);
		getProxy().unregisterChannel(CapeServiceOffline.CHANNEL);
		getProxy().unregisterChannel(EaglerPipeline.UPDATE_CERT_CHANNEL);
		getProxy().unregisterChannel(VoiceService.CHANNEL);
		getProxy().unregisterChannel(EaglerPacketEventListener.FNAW_SKIN_ENABLE_CHANNEL);
		stopListeners();
		if(closeInactiveConnections != null) {
			closeInactiveConnections.cancel();
			closeInactiveConnections = null;
		}
		if(skinServiceTasks != null) {
			skinServiceTasks.cancel();
			skinServiceTasks = null;
		}
		skinService.shutdown();
		skinService = null;
		capeService.shutdown();
		capeService = null;
		if(defaultAuthSystem != null) {
			defaultAuthSystem.destroy();
			defaultAuthSystem = null;
			if(authServiceTasks != null) {
				authServiceTasks.cancel();
				authServiceTasks = null;
			}
		}
		voiceService = null;
		BinaryHttpClient.killEventLoop();
	}
	
	public void reload() {
		stopListeners();
		reloadConfig();
		startListeners();
	}
	
	private void reloadConfig() {
		try {
			conf = EaglerBungeeConfig.loadConfig(getDataFolder());
			if(conf == null) {
				throw new IOException("Config failed to parse!");
			}
			conf.setCracked(!BungeeCord.getInstance().getConfig().isOnlineMode() || !conf.getAuthConfig().isEnableAuthentication());
			HttpWebServer.regenerate404Pages();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void startListeners() {
		for(EaglerListenerConfig conf : conf.getServerListeners()) {
			if(conf.getAddress() != null) {
				makeListener(conf, conf.getAddress());
			}
			if(conf.getAddressV6() != null) {
				makeListener(conf, conf.getAddressV6());
			}
		}
	}
	
	private void makeListener(EaglerListenerConfig confData, InetSocketAddress addr) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.option(ChannelOption.SO_REUSEADDR, true)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.channel(PipelineUtils.getServerChannel(addr));
		if(eventLoopGroupBoss != null) {
			bootstrap.group(eventLoopGroupBoss, eventLoopGroup);
		}else {
			bootstrap.group(eventLoopGroup);
		}
		bootstrap.childAttr(EaglerPipeline.LISTENER, confData)
			.attr(EaglerPipeline.LOCAL_ADDRESS, addr)
			.localAddress(addr)
			.childHandler(EaglerPipeline.SERVER_CHILD)
			.bind().addListener(newChannelListener);
	}
	
	public void stopListeners() {
		synchronized(openChannels) {
			for(Channel c : openChannels) {
				c.close().syncUninterruptibly();
				EaglerXBungee.logger().info("Eaglercraft listener closed: " + c.attr(EaglerPipeline.LOCAL_ADDRESS).get().toString());
			}
			openChannels.clear();
		}
		synchronized(EaglerPipeline.openChannels) {
			EaglerPipeline.openChannels.clear();
		}
	}
	
	public EaglerBungeeConfig getConfig() {
		return conf;
	}
	
	public EventLoopGroup getEventLoopGroup() {
		return eventLoopGroup;
	}
	
	public ISkinService getSkinService() {
		return skinService;
	}
	
	public CapeServiceOffline getCapeService() {
		return capeService;
	}
	
	public DefaultAuthSystem getAuthService() {
		return defaultAuthSystem;
	}
	
	public VoiceService getVoiceService() {
		return voiceService;
	}
	
	public static EaglerXBungee getEagler() {
		return instance;
	}
	
	public static Logger logger() {
		return instance.getLogger();
	}

}
