package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

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
public class EaglerAuthConfig {

	static EaglerAuthConfig loadConfig(Configuration config) {
		boolean enableAuthentication = config.getBoolean("enable_authentication_system");
		boolean useBuiltInAuthentication = config.getBoolean("use_onboard_eaglerx_system");
		String databaseURI = config.getString("auth_db_uri");
		String driverClass = config.getString("sql_driver_class", "internal");
		String driverPath = config.getString("sql_driver_path", null);
		String passwordPromptScreenText = ChatColor.translateAlternateColorCodes('&', config.getString("password_prompt_screen_text", ""));
		String notRegisteredScreenText = ChatColor.translateAlternateColorCodes('&', config.getString("not_registered_screen_text", ""));
		String wrongPasswordScreenText = ChatColor.translateAlternateColorCodes('&', config.getString("wrong_password_screen_text", ""));
		String eaglerCommandName = config.getString("eagler_command_name");
		String useRegisterCommandText = ChatColor.translateAlternateColorCodes('&', config.getString("use_register_command_text", ""));
		String useChangeCommandText = ChatColor.translateAlternateColorCodes('&', config.getString("use_change_command_text", ""));
		String commandSuccessText = ChatColor.translateAlternateColorCodes('&', config.getString("command_success_text", ""));
		String lastEaglerLoginMessage = ChatColor.translateAlternateColorCodes('&', config.getString("last_eagler_login_message", ""));
		String tooManyRegistrationsMessage = ChatColor.translateAlternateColorCodes('&', config.getString("too_many_registrations_message", ""));
		String needVanillaToRegisterMessage = ChatColor.translateAlternateColorCodes('&', config.getString("need_vanilla_to_register_message", ""));
		boolean overrideEaglerToVanillaSkins = config.getBoolean("override_eagler_to_vanilla_skins");
		int maxRegistrationsPerIP = config.getInt("max_registration_per_ip", -1);
		return new EaglerAuthConfig(enableAuthentication, useBuiltInAuthentication, databaseURI, driverClass,
				driverPath, passwordPromptScreenText, wrongPasswordScreenText, notRegisteredScreenText,
				eaglerCommandName, useRegisterCommandText, useChangeCommandText, commandSuccessText,
				lastEaglerLoginMessage, tooManyRegistrationsMessage, needVanillaToRegisterMessage,
				overrideEaglerToVanillaSkins, maxRegistrationsPerIP);
	}

	private boolean enableAuthentication;
	private boolean useBuiltInAuthentication;
	
	private final String databaseURI;
	private final String driverClass;
	private final String driverPath;
	private final String passwordPromptScreenText;
	private final String wrongPasswordScreenText;
	private final String notRegisteredScreenText;
	private final String eaglerCommandName;
	private final String useRegisterCommandText;
	private final String useChangeCommandText;
	private final String commandSuccessText;
	private final String lastEaglerLoginMessage;
	private final String tooManyRegistrationsMessage;
	private final String needVanillaToRegisterMessage;
	private final boolean overrideEaglerToVanillaSkins;
	private final int maxRegistrationsPerIP;
	
	private EaglerAuthConfig(boolean enableAuthentication, boolean useBuiltInAuthentication, String databaseURI,
			String driverClass, String driverPath, String passwordPromptScreenText, String wrongPasswordScreenText,
			String notRegisteredScreenText, String eaglerCommandName, String useRegisterCommandText,
			String useChangeCommandText, String commandSuccessText, String lastEaglerLoginMessage,
			String tooManyRegistrationsMessage, String needVanillaToRegisterMessage,
			boolean overrideEaglerToVanillaSkins, int maxRegistrationsPerIP) {
		this.enableAuthentication = enableAuthentication;
		this.useBuiltInAuthentication = useBuiltInAuthentication;
		this.databaseURI = databaseURI;
		this.driverClass = driverClass;
		this.driverPath = driverPath;
		this.passwordPromptScreenText = passwordPromptScreenText;
		this.wrongPasswordScreenText = wrongPasswordScreenText;
		this.notRegisteredScreenText = notRegisteredScreenText;
		this.eaglerCommandName = eaglerCommandName;
		this.useRegisterCommandText = useRegisterCommandText;
		this.useChangeCommandText = useChangeCommandText;
		this.commandSuccessText = commandSuccessText;
		this.lastEaglerLoginMessage = lastEaglerLoginMessage;
		this.tooManyRegistrationsMessage = tooManyRegistrationsMessage;
		this.needVanillaToRegisterMessage = needVanillaToRegisterMessage;
		this.overrideEaglerToVanillaSkins = overrideEaglerToVanillaSkins;
		this.maxRegistrationsPerIP = maxRegistrationsPerIP;
	}

	public boolean isEnableAuthentication() {
		return enableAuthentication;
	}

	public boolean isUseBuiltInAuthentication() {
		return useBuiltInAuthentication;
	}

	public void triggerOnlineModeDisabled() {
		enableAuthentication = false;
		useBuiltInAuthentication = false;
	}

	public String getDatabaseURI() {
		return databaseURI;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public String getPasswordPromptScreenText() {
		return passwordPromptScreenText;
	}

	public String getWrongPasswordScreenText() {
		return wrongPasswordScreenText;
	}

	public String getNotRegisteredScreenText() {
		return notRegisteredScreenText;
	}

	public String getEaglerCommandName() {
		return eaglerCommandName;
	}

	public String getUseRegisterCommandText() {
		return useRegisterCommandText;
	}

	public String getUseChangeCommandText() {
		return useChangeCommandText;
	}

	public String getCommandSuccessText() {
		return commandSuccessText;
	}

	public String getLastEaglerLoginMessage() {
		return lastEaglerLoginMessage;
	}

	public String getTooManyRegistrationsMessage() {
		return tooManyRegistrationsMessage;
	}

	public String getNeedVanillaToRegisterMessage() {
		return needVanillaToRegisterMessage;
	}

	public boolean getOverrideEaglerToVanillaSkins() {
		return overrideEaglerToVanillaSkins;
	}

	public int getMaxRegistrationsPerIP() {
		return maxRegistrationsPerIP;
	}

}
