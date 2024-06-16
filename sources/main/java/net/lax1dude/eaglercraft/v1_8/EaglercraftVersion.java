package net.lax1dude.eaglercraft.v1_8;

import java.math.BigInteger;

public class EaglercraftVersion {
	
	
	//////////////////////////////////////////////////////////////////////
	
	/// Customize these to fit your fork:
	
	public static final String projectForkName = "EaglercraftX";
	public static final String projectForkVersion = "u35";
	public static final String projectForkVendor = "lax1dude";
	
	public static final String projectForkURL = "https://gitlab.com/lax1dude/eaglercraftx-1.8";
	
	//////////////////////////////////////////////////////////////////////
	
	public static final String projectOriginName = "EaglercraftX";
	public static final String projectOriginAuthor = "lax1dude";
	public static final String projectOriginRevision = "1.8";
	public static final String projectOriginVersion = "u35";
	
	public static final String projectOriginURL = "https://gitlab.com/lax1dude/eaglercraftx-1.8"; // rest in peace
	
	
	
	// Updating configuration
	
	public static final boolean enableUpdateService = true;

	public static final String updateBundlePackageName = "net.lax1dude.eaglercraft.v1_8.client";
	public static final int updateBundlePackageVersionInt = 35;

	public static final String updateLatestLocalStorageKey = "latestUpdate_" + updateBundlePackageName;

	// public key modulus for official 1.8 updates
	public static final BigInteger updateSignatureModulus = new BigInteger("14419476194820052109078379102436982757438300194194974078260570958862225232043861026588258585967060437391326494976080031137298500457111529693806931143421725626747051503616606418909609840275122831550688481329699012469742002429706330734797679859799085213517354399295425740214330234086361416936984593337389989505613123225737002654977194421571825036717017788527234114501215218715499682638139386636103589791643964827904791195488978835113700772208317974307363542114867750505953323167521731238542123593257269990619007858952216110012513121779359926747737258698347806747854986471035713105133999027704095451858121831297923962641");
	
	
	
	// Miscellaneous variables:

	public static final String mainMenuStringA = "Minecraft 1.8.8";
	public static final String mainMenuStringB = projectOriginName + " " +
			projectOriginRevision + "-" + projectOriginVersion + " ultimate";
	public static final String mainMenuStringC = "";
	public static final String mainMenuStringD = "Resources Copyright Mojang AB";

	public static final String mainMenuStringE = projectForkName + " " + projectForkVersion;
	public static final String mainMenuStringF = "Made by " + projectForkVendor;

	public static final String mainMenuStringG = "Collector's Edition";
	public static final String mainMenuStringH = "PBR Shaders";

	public static final long demoWorldSeed = (long) "North Carolina".hashCode();

	public static final boolean mainMenuEnableGithubButton = false;

	public static final boolean forceDemoMode = false;

	public static final String localStorageNamespace = "_eaglercraftX";

}
