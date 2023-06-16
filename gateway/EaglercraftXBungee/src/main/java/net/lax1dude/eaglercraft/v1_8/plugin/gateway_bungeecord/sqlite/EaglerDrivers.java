package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.sqlite;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.codehaus.plexus.util.FileUtils;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;

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
public class EaglerDrivers {

	private static Driver initializeDriver(String address, String driverClass) {
		URLClassLoader classLoader = driversJARs.get(address);
		if(classLoader == null) {
			File driver;
			if(address.equalsIgnoreCase("internal")) {
				driver = new File(EaglerXBungee.getEagler().getDataFolder(), "drivers/sqlite-jdbc.jar");
				driver.getParentFile().mkdirs();
				if(!driver.exists()) {
					try {
						FileUtils.copyURLToFile(EaglerDrivers.class.getResource("sqlite-jdbc.jar"), driver);
					} catch (IOException ex) {
						EaglerXBungee.logger().severe("Could not extract sqlite-jdbc.jar!");
						throw new ExceptionInInitializerError(ex);
					}
				}
			}else {
				driver = new File(address);
			}
			URL driverURL;
			try {
				driverURL = driver.toURI().toURL();
			}catch(MalformedURLException ex) {
				EaglerXBungee.logger().severe("Invalid JDBC driver path: " + address);
				throw new ExceptionInInitializerError(ex);
			}
			classLoader = new URLClassLoader(new URL[] { driverURL }, ClassLoader.getSystemClassLoader());
			driversJARs.put(address, classLoader);
		}
		
		Class loadedDriver;
		try {
			loadedDriver = classLoader.loadClass(driverClass);
		}catch(ClassNotFoundException ex) {
			try {
				classLoader.close();
			} catch (IOException e) {
			}
			EaglerXBungee.logger().severe("Could not find JDBC driver class: " + driverClass);
			throw new ExceptionInInitializerError(ex);
		}
		Driver sqlDriver = null;
		try {
			sqlDriver = (Driver) loadedDriver.newInstance();
		}catch(Throwable ex) {
			try {
				classLoader.close();
			} catch (IOException e) {
			}
			EaglerXBungee.logger().severe("Could not initialize JDBC driver class: " + driverClass);
			throw new ExceptionInInitializerError(ex);
		}
		
		return sqlDriver;
	}

	private static final Map<String, URLClassLoader> driversJARs = new HashMap();
	private static final Map<String, Driver> driversDrivers = new HashMap();

	public static Connection connectToDatabase(String address, String driverClass, String driverPath, Properties props)
			throws SQLException {
		if(driverClass.equalsIgnoreCase("internal")) {
			driverClass = "org.sqlite.JDBC";
		}
		if(driverPath == null) {
			try {
				Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				throw new SQLException("Driver class not found in JRE: " + driverClass, e);
			}
			return DriverManager.getConnection(address, props);
		}else {
			String driverMapPath = "" + driverPath + "?" + driverClass;
			Driver dv = driversDrivers.get(driverMapPath);
			if(dv == null) {
				dv = initializeDriver(driverPath, driverClass);
				driversDrivers.put(driverMapPath, dv);
			}
			return dv.connect(address, props);
		}
	}

}
