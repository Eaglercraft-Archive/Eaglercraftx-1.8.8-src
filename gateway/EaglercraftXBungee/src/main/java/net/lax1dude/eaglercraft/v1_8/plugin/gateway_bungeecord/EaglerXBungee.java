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
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.BinaryHttpClient;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.ISkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SkinServiceOffline;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.BungeeCord;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class EaglerXBungee extends Plugin {
	
	private static EaglerXBungee instance = null;
	private EaglerBungeeConfig conf = null;
	private EventLoopGroup eventLoopGroup;
	private Collection<Channel> openChannels;
	private final Timer closeInactiveConnections;
	private Timer skinServiceTasks = null;
	private Timer authServiceTasks = null;
	private final ChannelFutureListener newChannelListener;
	private ISkinService skinService;
	private DefaultAuthSystem defaultAuthSystem;
	
	public EaglerXBungee() {
		instance = this;
		openChannels = new LinkedList();
		closeInactiveConnections = new Timer("EaglerXBungee: Close Inactive");
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
			} catch (IllegalAccessException | NoSuchFieldException ex) {
				throw new RuntimeException(ex);
			}
		}
		reloadConfig();
		closeInactiveConnections.scheduleAtFixedRate(EaglerPipeline.closeInactive, 0l, 250l);
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
		startListeners();
		if(skinServiceTasks != null) {
			skinServiceTasks.cancel();
			skinServiceTasks = null;
		}
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
	}

	@Override
	public void onDisable() {
		PluginManager mgr = getProxy().getPluginManager();
		mgr.unregisterListeners(this);
		mgr.unregisterCommands(this);
		getProxy().unregisterChannel(SkinService.CHANNEL);
		stopListeners();
		if(skinServiceTasks != null) {
			skinServiceTasks.cancel();
			skinServiceTasks = null;
		}
		skinService.shutdown();
		if(defaultAuthSystem != null) {
			defaultAuthSystem.destroy();
			defaultAuthSystem = null;
			if(authServiceTasks != null) {
				authServiceTasks.cancel();
				authServiceTasks = null;
			}
		}
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
			.channel(PipelineUtils.getServerChannel(addr))
			.group(eventLoopGroup)
			.childAttr(EaglerPipeline.LISTENER, confData)
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
	
	public DefaultAuthSystem getAuthService() {
		return defaultAuthSystem;
	}
	
	public static EaglerXBungee getEagler() {
		return instance;
	}
	
	public static Logger logger() {
		return instance.getLogger();
	}

}
