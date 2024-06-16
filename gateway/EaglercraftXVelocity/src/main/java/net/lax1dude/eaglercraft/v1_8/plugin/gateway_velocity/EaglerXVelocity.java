package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.proxy.VelocityServer;
import com.velocitypowered.proxy.network.ConnectionManager;
import com.velocitypowered.proxy.network.TransportType;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.auth.DefaultAuthSystem;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command.CommandConfirmCode;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command.CommandDomain;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command.CommandEaglerPurge;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command.CommandEaglerRegister;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command.CommandRatelimit;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.command.EaglerCommand;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerAuthConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.handlers.EaglerPacketEventListener;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.web.HttpWebServer;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.shit.CompatWarning;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.BinaryHttpClient;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.CapeServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.ISkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinService;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SkinServiceOffline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice.VoiceService;

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
@Plugin(
		id = EaglerXVelocityVersion.PLUGIN_ID,
		name = EaglerXVelocityVersion.NAME,
		description = EaglerXVelocityVersion.DESCRIPTION,
		version = EaglerXVelocityVersion.VERSION,
		authors = {
			"lax1dude",
			"ayunami2000"
		}
)
public class EaglerXVelocity {

	static {
		CompatWarning.displayCompatWarning();
	}
	
	private static EaglerXVelocity instance = null;
	private final VelocityServer proxy;
	private final Logger logger;
	private final Path dataDirAsPath;
	private final File dataDir;
	private ConnectionManager cm;
	private EaglerVelocityConfig conf = null;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private TransportType transportType;
	private ChannelFactory<? extends ServerSocketChannel> serverSocketChannelFactory;
	private ChannelFactory<? extends SocketChannel> socketChannelFactory;
	private ChannelFactory<? extends DatagramChannel> datagramChannelFactory;
	private Collection<Channel> openChannels;
	private Timer closeInactiveConnections;
	private Timer skinServiceTasks = null;
	private Timer authServiceTasks = null;
	private final ChannelFutureListener newChannelListener;
	private ISkinService skinService;
	private CapeServiceOffline capeService;
	private VoiceService voiceService;
	private DefaultAuthSystem defaultAuthSystem;
	
	@Inject
	public EaglerXVelocity(ProxyServer proxyIn, Logger loggerIn, @DataDirectory Path dataDirIn) {
		instance = this;
		proxy = (VelocityServer)proxyIn;
		logger = loggerIn;
		dataDirAsPath = dataDirIn;
		dataDir = dataDirIn.toFile();
		
		openChannels = new LinkedList();
		newChannelListener = new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture ch) throws Exception {
				synchronized(openChannels) { // synchronize whole block to preserve logging order
					if(ch.isSuccess()) {
						EaglerXVelocity.logger().info("Eaglercraft is listening on: {}", ch.channel().attr(EaglerPipeline.LOCAL_ADDRESS).get().toString());
						openChannels.add(ch.channel());
					}else {
						EaglerXVelocity.logger().error("Eaglercraft could not bind port: {}", ch.channel().attr(EaglerPipeline.LOCAL_ADDRESS).get().toString());
						EaglerXVelocity.logger().error("Reason: {}", ch.cause().toString());
					}
				}
			}
		};
	}

    @Subscribe
    public void onProxyInit(ProxyInitializeEvent e) {
		logger.warn("This plugin is still in development, certain features may not be functional!");
		logger.warn("Not recommended for public servers yet, please check for updates");
		try {
			Field f = VelocityServer.class.getDeclaredField("cm");
			f.setAccessible(true);
			cm = (ConnectionManager)f.get(proxy);
			f = ConnectionManager.class.getDeclaredField("bossGroup");
			f.setAccessible(true);
			bossGroup = (EventLoopGroup)f.get(cm);
			f = ConnectionManager.class.getDeclaredField("workerGroup");
			f.setAccessible(true);
			workerGroup = (EventLoopGroup)f.get(cm);
			f = ConnectionManager.class.getDeclaredField("transportType");
			f.setAccessible(true);
			transportType = (TransportType)f.get(cm);
			f = TransportType.class.getDeclaredField("serverSocketChannelFactory");
			f.setAccessible(true);
			serverSocketChannelFactory = (ChannelFactory<? extends ServerSocketChannel>)f.get(transportType);
			f = TransportType.class.getDeclaredField("socketChannelFactory");
			f.setAccessible(true);
			socketChannelFactory = (ChannelFactory<? extends SocketChannel>)f.get(transportType);
			f = TransportType.class.getDeclaredField("datagramChannelFactory");
			f.setAccessible(true);
			datagramChannelFactory = (ChannelFactory<? extends DatagramChannel>)f.get(transportType);
		} catch(Throwable t) {
			throw new RuntimeException("Accessing private fields failed!", t);
		}
		reloadConfig();
		proxy.getEventManager().register(this, new EaglerPacketEventListener(this));
		EaglerCommand.register(this, new CommandRatelimit());
		EaglerCommand.register(this, new CommandConfirmCode());
		EaglerCommand.register(this, new CommandDomain());
		EaglerAuthConfig authConf = conf.getAuthConfig();
		conf.setCracked(!proxy.getConfiguration().isOnlineMode() || !authConf.isEnableAuthentication());
		if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
			if(!proxy.getConfiguration().isOnlineMode()) {
				logger.error("Online mode is set to false! Authentication system has been disabled");
				authConf.triggerOnlineModeDisabled();
			}else {
				EaglerCommand.register(this, new CommandEaglerRegister(authConf.getEaglerCommandName()));
				EaglerCommand.register(this, new CommandEaglerPurge(authConf.getEaglerCommandName()));
			}
		}
		proxy.getChannelRegistrar().register(SkinService.CHANNEL, CapeServiceOffline.CHANNEL,
				EaglerPipeline.UPDATE_CERT_CHANNEL, VoiceService.CHANNEL,
				EaglerPacketEventListener.FNAW_SKIN_ENABLE_CHANNEL, EaglerPacketEventListener.GET_DOMAIN_CHANNEL);

		if(closeInactiveConnections != null) {
			closeInactiveConnections.cancel();
			closeInactiveConnections = null;
		}
		closeInactiveConnections = new Timer(EaglerXVelocityVersion.ID + ": Network Tick Tasks");
		closeInactiveConnections.scheduleAtFixedRate(EaglerPipeline.closeInactive, 0l, 250l);
		
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
			skinServiceTasks = new Timer(EaglerXVelocityVersion.ID + ": Skin Service Tasks");
			skinServiceTasks.schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						skinService.flush();
					}catch(Throwable t) {
						logger.error("Error flushing skin cache!", t);
					}
				}
			}, 1000l, 1000l);
		}
		capeService = new CapeServiceOffline();
		if(authConf.isEnableAuthentication() && authConf.isUseBuiltInAuthentication()) {
			try {
				defaultAuthSystem = DefaultAuthSystem.initializeAuthSystem(authConf);
			}catch(DefaultAuthSystem.AuthSystemException ex) {
				logger.error("Could not load authentication system!", ex);
			}
			if(defaultAuthSystem != null) {
				authServiceTasks = new Timer(EaglerXVelocityVersion.ID + ": Auth Service Tasks");
				authServiceTasks.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							defaultAuthSystem.flush();
						}catch(Throwable t) {
							logger.error("Error flushing auth cache!", t);
						}
					}
				}, 60000l, 60000l);
			}
		}
		if(conf.getEnableVoiceChat()) {
			voiceService = new VoiceService(conf);
			logger.warn("Voice chat enabled, not recommended for public servers!");
		}else {
			logger.info("Voice chat disabled, add \"allow_voice: true\" to your listeners to enable");
		}
	}

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e) {
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
			conf = EaglerVelocityConfig.loadConfig(dataDir);
			if(conf == null) {
				throw new IOException("Config failed to parse!");
			}
			conf.setCracked(!proxy.getConfiguration().isOnlineMode() || !conf.getAuthConfig().isEnableAuthentication());
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
			.channelFactory(serverSocketChannelFactory)
			.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, EaglerPipeline.SERVER_WRITE_MARK)
			.group(bossGroup, workerGroup)
			.childAttr(EaglerPipeline.LISTENER, confData)
			.attr(EaglerPipeline.LOCAL_ADDRESS, addr)
			.childOption(ChannelOption.IP_TOS, 24)
			.localAddress(addr)
			.childHandler(EaglerPipeline.SERVER_CHILD);
		if (this.proxy.getConfiguration().useTcpFastOpen()) {
			bootstrap.option(ChannelOption.TCP_FASTOPEN, 3);
		}
		bootstrap.bind().addListener(newChannelListener);
	}
	
	public void stopListeners() {
		synchronized(openChannels) {
			for(Channel c : openChannels) {
				c.close().syncUninterruptibly();
				EaglerXVelocity.logger().info("Eaglercraft listener closed: " + c.attr(EaglerPipeline.LOCAL_ADDRESS).get().toString());
			}
			openChannels.clear();
		}
		synchronized(EaglerPipeline.openChannels) {
			EaglerPipeline.openChannels.clear();
		}
	}
	
	public EaglerVelocityConfig getConfig() {
		return conf;
	}
	
	public EventLoopGroup getBossEventLoopGroup() {
		return bossGroup;
	}
	
	public EventLoopGroup getWorkerEventLoopGroup() {
		return workerGroup;
	}
	
	public TransportType getTransportType() {
		return transportType;
	}
	
	public ChannelFactory<? extends SocketChannel> getChannelFactory() {
		return socketChannelFactory;
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
	
	public static EaglerXVelocity getEagler() {
		return instance;
	}
	
	public VelocityServer getProxy() {
		return proxy;
	}
	
	public Logger getLogger() {
		return logger;
	}

	public File getDataFolder() {
		return dataDir;
	}
	
	public static VelocityServer proxy() {
		return instance.getProxy();
	}
	
	public static Logger logger() {
		return instance.getLogger();
	}

}
