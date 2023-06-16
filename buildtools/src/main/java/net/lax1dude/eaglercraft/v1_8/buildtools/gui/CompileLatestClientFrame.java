package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.awt.event.ActionEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

import net.lax1dude.eaglercraft.v1_8.buildtools.gui.TeaVMBinaries.MissingJARsException;
import net.lax1dude.eaglercraft.v1_8.buildtools.task.init.FFMPEG;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.BoxLayout;
import java.awt.ComponentOrientation;
import javax.swing.JTextArea;

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
public class CompileLatestClientFrame {

	public JFrame frmCompileLatestClient;
	public JTextField textField_ModCoderPack;
	public JTextField textField_JarFilePath;
	public JTextField textField_AssetsIndexJSON;
	public JTextField textField_MavenRepoCustomURL;
	public JTextField textField_MavenRepoLocal;
	public JTextField textField_OutputDirectory;
	public JTextField textField_RepositoryPath;
	public JRadioButton rdbtnMavenRepoLocal;
	public JRadioButton rdbtnMavenRepoCustom;
	public JRadioButton rdbtnMavenRepoSonatype;
	public JRadioButton rdbtnMavenRepoBintray;
	public JRadioButton rdbtnMavenRepoCentral;
	public JTextPane txtpnLogOutput;
	public JLabel lblProgressState;
	public JButton btnBack;
	public JButton btnNext;
	public JPanel pagesRoot;
	public JScrollPane scrollPane;
	public JTextPane txtpnfuckOffreview;
	public JCheckBox chckbxOutputOfflineDownload;
	public JCheckBox chckbxKeepTemporaryFiles;
	public JTextField textField_pathToFFmpeg;
	public JButton btnBrowsePathToFFmpeg;
	public JCheckBox chckbxUsePathFFmpeg;
	public JCheckBox chckbxAgreeLicense;
	public JTextArea txtrLicenseText;
	public JScrollPane scrollPane_LicenseText;

	/**
	 * Create the application.
	 */
	public CompileLatestClientFrame() {
		initialize();
	}

	public int page = 0;
	public boolean compiling;
	public boolean finished;

	public void setPage(int i) {
		if(i < 0) i = 0;
		if(i > 10) i = 10;
		if(page != i) {
			if(page < i) {
				String pp;
				switch(page) {
				case 1:
					if(!chckbxAgreeLicense.isSelected()) {
						JOptionPane.showMessageDialog(frmCompileLatestClient, "You must agree to the license to continue!", "Permission Denied", JOptionPane.ERROR_MESSAGE);
						return;
					}
					break;
				case 2:
					pp = textField_RepositoryPath.getText().trim();
					if(pp.length() == 0) {
						JOptionPane.showMessageDialog(frmCompileLatestClient, "Please select a folder!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}else if(!(new File(pp)).isDirectory()){
						JOptionPane.showMessageDialog(frmCompileLatestClient, "The path \"" + pp + "\" is not a folder!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}
					break;
				case 3:
					pp = textField_ModCoderPack.getText().trim();
					if(pp.length() == 0) {
						JOptionPane.showMessageDialog(frmCompileLatestClient, "Please select a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}else if(!(new File(pp)).isFile()){
						JOptionPane.showMessageDialog(frmCompileLatestClient, "The path \"" + pp + "\" is not a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}
					break;
				case 4:
					pp = textField_JarFilePath.getText().trim();
					if(pp.length() == 0) {
						JOptionPane.showMessageDialog(frmCompileLatestClient, "Please select a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}else if(!(new File(pp)).isFile()){
						JOptionPane.showMessageDialog(frmCompileLatestClient, "The path \"" + pp + "\" is not a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}
					break;
				case 5:
					pp = textField_AssetsIndexJSON.getText().trim();
					if(pp.length() == 0) {
						JOptionPane.showMessageDialog(frmCompileLatestClient, "Please select a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}else if(!(new File(pp)).isFile()){
						JOptionPane.showMessageDialog(frmCompileLatestClient, "The path \"" + pp + "\" is not a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}
					break;
				case 6:
					if(rdbtnMavenRepoLocal.isSelected()) {
						pp = textField_MavenRepoLocal.getText().trim();
						if(pp.length() == 0) {
							JOptionPane.showMessageDialog(frmCompileLatestClient, "Please select a folder!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
							return;
						}else {
							File f = new File(pp);
							if(!f.isDirectory()){
								JOptionPane.showMessageDialog(frmCompileLatestClient, "The path \"" + pp + "\" is not a folder!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
								return;
							}else {
								try {
									TeaVMBinaries.loadFromDirectory(f);
								}catch(MissingJARsException ex) {
									JOptionPane.showMessageDialog(frmCompileLatestClient, "The following JARs are missing:\n - " + String.join("\n - ", ex.jars), "Invalid Path", JOptionPane.ERROR_MESSAGE);
									return;
								}
							}
						}
					}else if(rdbtnMavenRepoCustom.isSelected()) {
						pp = textField_MavenRepoCustomURL.getText().trim();
						if(pp.length() == 0) {
							JOptionPane.showMessageDialog(frmCompileLatestClient, "Please enter a URL!", "Invalid URL", JOptionPane.ERROR_MESSAGE);
							return;
						}else {
							try {
								URL u = new URL(pp);
							}catch(MalformedURLException ex) {
								JOptionPane.showMessageDialog(frmCompileLatestClient, "MalformedURLException: " + ex.getMessage(), "Invalid URL", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					}
					break;
				case 7:
					if(!chckbxUsePathFFmpeg.isSelected()) {
						pp = textField_pathToFFmpeg.getText().trim();
						if(pp.length() == 0) {
							JOptionPane.showMessageDialog(frmCompileLatestClient, "Please select a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
							return;
						}else if(!(new File(pp)).isFile()){
							JOptionPane.showMessageDialog(frmCompileLatestClient, "The path \"" + pp + "\" is not a file!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}else if(!isFFmpegOnPath()) {
						JOptionPane.showMessageDialog(frmCompileLatestClient, "FFmpeg is not on your system's PATH!", "Error", JOptionPane.ERROR_MESSAGE);
						chckbxUsePathFFmpeg.setSelected(false);
						chckbxUsePathFFmpeg.setEnabled(false);
						textField_pathToFFmpeg.setEnabled(true);
						btnBrowsePathToFFmpeg.setEnabled(true);
						return;
					}
					break;
				case 8:
					pp = textField_OutputDirectory.getText().trim();
					if(pp.length() == 0) {
						JOptionPane.showMessageDialog(frmCompileLatestClient, "Please select a folder!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
						return;
					}else {
						File f = new File(pp);
						if(!f.isDirectory()){
							JOptionPane.showMessageDialog(frmCompileLatestClient, "The path \"" + pp + "\" is not a folder!", "Invalid Path", JOptionPane.ERROR_MESSAGE);
							return;
						}else if(f.list().length > 0) {
							if(JOptionPane.showConfirmDialog(frmCompileLatestClient, "The directory \"" + pp + "\" is not empty, would you like to continue?\nThe existing files will be deleted.", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
								return;
							}
						}
					}
					break;
				default:
					break;
				}
			}
			CardLayout pagesLayout = (CardLayout)pagesRoot.getLayout();
			switch(i) {
			case 0:
			default:
				pagesLayout.show(pagesRoot, "pageHome");
				break;
			case 1:
				pagesLayout.show(pagesRoot, "pageLicense");
				break;
			case 2:
				pagesLayout.show(pagesRoot, "pageBrowseRepositoryPath");
				break;
			case 3:
				pagesLayout.show(pagesRoot, "pageModCoderPack");
				break;
			case 4:
				pagesLayout.show(pagesRoot, "pageBrowseJarFile");
				break;
			case 5:
				pagesLayout.show(pagesRoot, "pageBrowseAssetsIndexJSON");
				break;
			case 6:
				pagesLayout.show(pagesRoot, "pageMavenRepo");
				break;
			case 7:
				pagesLayout.show(pagesRoot, "pageFFmpeg");
				if(isFFmpegOnPath()) {
					chckbxUsePathFFmpeg.setEnabled(true);
					if(!hasAskedFFmpegOnPath) {
						hasAskedFFmpegOnPath = true;
						EventQueue.invokeLater(new Runnable() {
							@Override
							public void run() {
								if(JOptionPane.showConfirmDialog(frmCompileLatestClient, "FFmpeg was found on your system's PATH!\nwould you like to select it automatically?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
									chckbxUsePathFFmpeg.setSelected(true);
									textField_pathToFFmpeg.setEnabled(false);
									btnBrowsePathToFFmpeg.setEnabled(false);
									setPage(page + 1);
								}
							}
						});
					}
				}else {
					chckbxUsePathFFmpeg.setEnabled(false);
				}
				break;
			case 8:
				pagesLayout.show(pagesRoot, "pageOutputDirectory");
				break;
			case 9:
				txtpnfuckOffreview.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;margin:7px 0px;\">Are these correct?</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; Repository Path:</span> " + htmlentities((new File(textField_RepositoryPath.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; Mod Coder Pack:</span> " + htmlentities((new File(textField_ModCoderPack.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; 1.8.8.jar path:</span> " + htmlentities((new File(textField_JarFilePath.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; 1.8.json path:</span> " + htmlentities((new File(textField_AssetsIndexJSON.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; TeaVM Maven:</span> " + htmlentities(rdbtnMavenRepoLocal.isSelected() ? (new File(textField_MavenRepoLocal.getText())).getAbsolutePath() : getRepositoryURL()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; FFmpeg:</span> " + (isFFmpegOnPath() ? "<i>(Already Installed)</i>" : htmlentities((new File(textField_pathToFFmpeg.getText())).getAbsolutePath())) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; Output Directory:</span> " + htmlentities((new File(textField_OutputDirectory.getText())).getAbsolutePath()) + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; Make Offline Download:</span> " + (chckbxOutputOfflineDownload.isSelected() ? "Yes" : "No") + "</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\">"
						+ "<span style=\"font-weight:bold;\">&emsp;&bull; Keep Temporary Files:</span> " + (chckbxKeepTemporaryFiles.isSelected() ? "Yes" : "No") + "</p>\r\n"
						+ "<p style=\"font-size:13px;margin-top:6px;\">&nbsp;Press the \"Compile >>\" button to confirm</p>\r\n</body>\r\n</html>");
				pagesLayout.show(pagesRoot, "pageConfirmSettings");
				break;
			case 10:
				pagesLayout.show(pagesRoot, "pageLogOutput");
				lblProgressState.setText("Compiling, Please Wait...");
				compiling = true;
				Point p = frmCompileLatestClient.getLocation();
				frmCompileLatestClient.setLocation(p.x - 150, p.y - 150);
				Dimension d = frmCompileLatestClient.getSize();
				frmCompileLatestClient.setSize(d.width + 300, d.height + 300);
				frmCompileLatestClient.setResizable(true);
				(new Thread(new Runnable() {
					public void run() {
						CompileLatestClientGUI.runCompiler();
					}
				}, "Compiler Thread")).start();
				break;
			}
			page = i;
			btnBack.setEnabled(i > 0 && i != 10);
			btnNext.setEnabled(i != 10 && !(i == 1 && !chckbxAgreeLicense.isSelected()));
			btnNext.setText(i != 10 ? (i != 9 ? "Next >>" : "Compile >>") : "Finish");
		}
	}

	public static File getMinecraftDir() {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win")) {
			String home = System.getenv("APPDATA");
			if(home == null) {
				home = System.getProperty("user.home", ".");
			}
			return new File(home, ".minecraft");
		}else if(os.contains("osx") || os.contains("macos")) {
			return new File(System.getProperty("user.home", "."), "Library/Application Support/.minecraft");
		}else {
			return new File(System.getProperty("user.home", "."), ".minecraft");
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCompileLatestClient = new JFrame();
		frmCompileLatestClient.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(compiling && !finished) {
					if(JOptionPane.showConfirmDialog(frmCompileLatestClient, "Compilier is still running, do you really want to exit?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
						System.exit(0);
					}
				}else {
					System.exit(0);
				}
			}
		});
		frmCompileLatestClient.setIconImage(Toolkit.getDefaultToolkit().getImage(CompileLatestClientFrame.class.getResource("/icon/icon32.png")));
		frmCompileLatestClient.setTitle("Compile Latest Client");
		frmCompileLatestClient.setResizable(false);
		frmCompileLatestClient.getContentPane().setBackground(Color.WHITE);
		frmCompileLatestClient.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		frmCompileLatestClient.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_35 = new JPanel();
		panel_35.setBackground(Color.WHITE);
		FlowLayout flowLayout = (FlowLayout) panel_35.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel.add(panel_35, BorderLayout.EAST);
		
		btnBack = new JButton("<< Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPage(page - 1);
			}
		});
		panel_35.add(btnBack);
		btnBack.setEnabled(false);
		
		btnNext = new JButton("Next >>");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(finished) {
					System.exit(0);
				}else {
					setPage(page + 1);
				}
			}
		});
		panel_35.add(btnNext);
		
		lblProgressState = new JLabel("");
		lblProgressState.setFont(new Font("Dialog", Font.BOLD, 16));
		lblProgressState.setBorder(new EmptyBorder(0, 10, 0, 10));
		panel.add(lblProgressState, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		frmCompileLatestClient.getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_1.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("EaglercraftX 1.8 Client Compiler");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel_1.setPreferredSize(new Dimension(152, 24));
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 14));
		panel_2.add(lblNewLabel_1, BorderLayout.NORTH);
		
		JLabel lblNewLabel_2 = new JLabel("Copyright (c) 2022-2023 lax1dude");
		lblNewLabel_2.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_2.setPreferredSize(new Dimension(27, 24));
		lblNewLabel_2.setFont(new Font("Dialog", Font.PLAIN, 14));
		panel_2.add(lblNewLabel_2, BorderLayout.SOUTH);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBorder(new EmptyBorder(8, 8, 8, 16));
		lblNewLabel.setIcon(new ImageIcon(CompileLatestClientFrame.class.getResource("/icon/eagler.png")));
		panel_1.add(lblNewLabel, BorderLayout.WEST);
		
		JPanel panel_23 = new JPanel();
		panel_23.setPreferredSize(new Dimension(10, 1));
		panel_23.setBackground(Color.DARK_GRAY);
		panel_1.add(panel_23, BorderLayout.NORTH);
		panel_23.setLayout(null);
		
		JPanel panel_28 = new JPanel();
		panel_28.setBackground(Color.DARK_GRAY);
		panel_28.setPreferredSize(new Dimension(10, 1));
		panel_1.add(panel_28, BorderLayout.SOUTH);
		panel_28.setLayout(null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		frmCompileLatestClient.getContentPane().add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		pagesRoot = new JPanel();
		pagesRoot.setBackground(Color.WHITE);
		panel_3.add(pagesRoot, BorderLayout.CENTER);
		pagesRoot.setLayout(new CardLayout(0, 0));
		
		JPanel pageHome = new JPanel();
		pageHome.setBackground(Color.WHITE);
		pagesRoot.add(pageHome, "pageHome");
		pageHome.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffwelcome = new JTextPane();
		txtpnfuckOffwelcome.setEditable(false);
		txtpnfuckOffwelcome.setMargin(new Insets(10, 10, 10, 10));
		txtpnfuckOffwelcome.setContentType("text/html");
		txtpnfuckOffwelcome.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Welcome to the EaglercraftX 1.8 Client Compiler</p>\r\n<p style=\"font-size:11px;\">This tool will allow you to automatically compile the latest version of the EaglercraftX 1.8 client using the files in this repository.</p>\r\n<p style=\"font-size:11px;\">You are required to download several required files manually in order to better respect the Microsoft/Mojang TOS. The links to these files will be provided.</p>\r\n<p style=\"font-size:11px;\">To view or modify portions of the EaglercraftX 1.8 source code directly, please use the other batch files to generate a gradle project instead of compiling the javascript files directly</p>\r\n<p style=\"font-size:11px;\">If you are from Microsoft/Mojang or the developer of MCP trying to get dirt on me, please just let me live my life, the repository does not contain your intellectual property. Using this code to play your game for free is not the default behavior of the gateway plugin or this compiler utility and is not encouraged by the documentation</p>\r\n</body>\r\n</html>");
		pageHome.add(txtpnfuckOffwelcome, BorderLayout.CENTER);
		
		JPanel pageLicense = new JPanel();
		pageLicense.setBackground(Color.WHITE);
		pagesRoot.add(pageLicense, "pageLicense");
		pageLicense.setLayout(new BorderLayout(0, 0));
		
		scrollPane_LicenseText = new JScrollPane();
		scrollPane_LicenseText.setBorder(null);
		scrollPane_LicenseText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_LicenseText.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pageLicense.add(scrollPane_LicenseText, BorderLayout.CENTER);
		
		txtrLicenseText = new JTextArea();
		txtrLicenseText.setMargin(new Insets(10, 10, 10, 10));
		txtrLicenseText.setAutoscrolls(false);
		txtrLicenseText.setText(loadLicense());
		txtrLicenseText.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtrLicenseText.setLineWrap(true);
		txtrLicenseText.setEditable(false);
		txtrLicenseText.setWrapStyleWord(true);
		scrollPane_LicenseText.setViewportView(txtrLicenseText);
		
		JPanel panel_36 = new JPanel();
		pageLicense.add(panel_36, BorderLayout.SOUTH);
		panel_36.setLayout(new BorderLayout(0, 0));
		
		chckbxAgreeLicense = new JCheckBox("I agree to these terms and conditions");
		chckbxAgreeLicense.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				btnNext.setEnabled(chckbxAgreeLicense.isSelected());
			}
		});
		panel_36.add(chckbxAgreeLicense);
		chckbxAgreeLicense.setMargin(new Insets(7, 7, 7, 7));
		chckbxAgreeLicense.setFont(new Font("Dialog", Font.PLAIN, 14));
		chckbxAgreeLicense.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		chckbxAgreeLicense.setBackground(Color.WHITE);
		
		JPanel panel_37 = new JPanel();
		panel_37.setBackground(Color.DARK_GRAY);
		panel_37.setPreferredSize(new Dimension(10, 1));
		panel_36.add(panel_37, BorderLayout.NORTH);
		
		JPanel pageBrowseRepositoryPath = new JPanel();
		pageBrowseRepositoryPath.setBackground(Color.WHITE);
		pagesRoot.add(pageBrowseRepositoryPath, "pageBrowseRepositoryPath");
		pageBrowseRepositoryPath.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffeaglercraftx_1 = new JTextPane();
		txtpnfuckOffeaglercraftx_1.setEditable(false);
		txtpnfuckOffeaglercraftx_1.setContentType("text/html");
		txtpnfuckOffeaglercraftx_1.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">EaglercraftX 1.8 Source Code</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">It is assumed that you are running this tool in the root directory in your copy of the EaglercraftX 1.8 source code repository</p>\r\n<p style=\"font-size:11px;\">If this is not the case, please use the file chooser below to select where the repository is located</p>\r\n</body>\r\n</html>");
		txtpnfuckOffeaglercraftx_1.setMargin(new Insets(10, 10, 10, 10));
		pageBrowseRepositoryPath.add(txtpnfuckOffeaglercraftx_1, BorderLayout.CENTER);
		
		JPanel panel_24 = new JPanel();
		panel_24.setBorder(new EmptyBorder(0, 20, 40, 20));
		panel_24.setBackground(Color.WHITE);
		pageBrowseRepositoryPath.add(panel_24, BorderLayout.SOUTH);
		panel_24.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_25 = new JPanel();
		panel_25.setPreferredSize(new Dimension(10, 40));
		panel_25.setBackground(Color.WHITE);
		panel_24.add(panel_25, BorderLayout.CENTER);
		panel_25.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_7 = new JLabel("EaglercraftX 1.8 Repository Path:");
		lblNewLabel_7.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_7.setPreferredSize(new Dimension(162, 20));
		panel_25.add(lblNewLabel_7, BorderLayout.NORTH);
		
		JPanel panel_26 = new JPanel();
		panel_26.setPreferredSize(new Dimension(10, 20));
		panel_26.setBackground(Color.WHITE);
		panel_25.add(panel_26, BorderLayout.SOUTH);
		panel_26.setLayout(new BorderLayout(0, 0));
		
		JButton btnBrowseRepositoryPath = new JButton("Browse...");
		btnBrowseRepositoryPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current = textField_RepositoryPath.getText().trim();
				if(current.length() == 0) {
					current = (new File("")).getAbsolutePath();
				}
				JFileChooser fileChooser = new JFileChooser(new File(current));
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileHidingEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fileChooser.showOpenDialog(frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
					textField_RepositoryPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		panel_26.add(btnBrowseRepositoryPath, BorderLayout.EAST);
		
		JPanel panel_27 = new JPanel();
		panel_27.setBackground(Color.WHITE);
		panel_27.setBorder(new EmptyBorder(0, 0, 0, 10));
		panel_26.add(panel_27, BorderLayout.CENTER);
		panel_27.setLayout(new BorderLayout(0, 0));
		
		textField_RepositoryPath = new JTextField();
		panel_27.add(textField_RepositoryPath, BorderLayout.CENTER);
		textField_RepositoryPath.setText((new File("")).getAbsolutePath());
		textField_RepositoryPath.setColumns(10);
		
		JPanel pageModCoderPack = new JPanel();
		pageModCoderPack.setBackground(Color.WHITE);
		pagesRoot.add(pageModCoderPack, "pageModCoderPack");
		pageModCoderPack.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffwelcome_1 = new JTextPane();
		txtpnfuckOffwelcome_1.setMargin(new Insets(10, 10, 10, 10));
		txtpnfuckOffwelcome_1.setEditable(false);
		txtpnfuckOffwelcome_1.setContentType("text/html");
		txtpnfuckOffwelcome_1.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Mod Coder Pack</p>\r\n<p style=\"font-size:11px;\">A copy of Mod Coder Pack v9.18 is required to compile the EaglercraftX 1.8 client</p>\r\n<p style=\"font-size:11px;\">According to the Mod Coder Pack LICENSE.txt, \"You are NOT allowed to: release modified or unmodified versions of MCP anywhere\" so a copy of the files are not included in this repository, you're gonna have to download them separately</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">The official download link is at: <a href=\"http://www.modcoderpack.com/\">http://www.modcoderpack.com/</a></p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Visit the link and download \"mcp918.zip\" and select it below</p>\r\n</body>\r\n</html>");
		txtpnfuckOffwelcome_1.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (Throwable t) {
					}
				}
			}
		});
		pageModCoderPack.add(txtpnfuckOffwelcome_1, BorderLayout.CENTER);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new EmptyBorder(0, 20, 40, 20));
		panel_7.setAlignmentY(Component.TOP_ALIGNMENT);
		panel_7.setBackground(Color.WHITE);
		pageModCoderPack.add(panel_7, BorderLayout.SOUTH);
		panel_7.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_14 = new JPanel();
		panel_14.setBackground(Color.WHITE);
		panel_14.setPreferredSize(new Dimension(10, 40));
		panel_7.add(panel_14, BorderLayout.SOUTH);
		panel_14.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_3 = new JLabel("path to mcp918.zip:");
		lblNewLabel_3.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_3.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_3.setPreferredSize(new Dimension(46, 20));
		panel_14.add(lblNewLabel_3, BorderLayout.NORTH);
		
		JPanel panel_8 = new JPanel();
		panel_8.setPreferredSize(new Dimension(10, 20));
		panel_8.setBackground(Color.WHITE);
		panel_14.add(panel_8, BorderLayout.SOUTH);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new EmptyBorder(0, 0, 0, 10));
		panel_9.setBackground(Color.WHITE);
		panel_8.add(panel_9, BorderLayout.CENTER);
		panel_9.setLayout(new BorderLayout(0, 0));
		
		textField_ModCoderPack = new JTextField();
		panel_9.add(textField_ModCoderPack, BorderLayout.CENTER);
		textField_ModCoderPack.setColumns(10);
		
		JButton btnBrowseModCoderPack = new JButton("Browse...");
		btnBrowseModCoderPack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current = textField_ModCoderPack.getText().trim();
				if(current.length() == 0) {
					File f = new File(System.getProperty("user.home", "."), "Downloads");
					if(!f.exists()) {
						f = new File(System.getProperty("user.home", "."));
					}
					current = f.getAbsolutePath();
				}
				JFileChooser fileChooser = new JFileChooser(new File(current));
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileHidingEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(fileChooser.showOpenDialog(frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
					textField_ModCoderPack.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		panel_8.add(btnBrowseModCoderPack, BorderLayout.EAST);
		
		JPanel pageBrowseJarFile = new JPanel();
		pageBrowseJarFile.setBackground(Color.WHITE);
		pagesRoot.add(pageBrowseJarFile, "pageBrowseJarFile");
		pageBrowseJarFile.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffwelcome_1_1 = new JTextPane();
		txtpnfuckOffwelcome_1_1.setContentType("text/html");
		txtpnfuckOffwelcome_1_1.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Minecraft 1.8 JAR File</p>\r\n<p style=\"font-size:11px;\">Obviously the JAR file containing the original Minecraft 1.8 bytecode is required to compile EaglercraftX 1.8, but it must again be downloaded separately from this repository because Microsoft/Mojang does not allow it to be redistributed without permission</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">To download it, BUY MINECRAFT, install the Minecraft launcher, make a new launcher profile for MINECRAFT 1.8.8, and launch it at least once.</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Use the file chooser below to navigate to your \".minecraft\" folder, open the folder named \"versions\", then open the folder within it called \"1.8.8\", and then within that folder select the JAR file called \"1.8.8.jar\"</p>\r\n<p style=\"font-size:11px;\">You can also download \"Client Jar\" from this link: <a href=\"https://mcversions.net/download/1.8.8/\">https://mcversions.net/download/1.8.8/</a></p>\r\n</body>\r\n</html>");
		txtpnfuckOffwelcome_1_1.setMargin(new Insets(10, 10, 10, 10));
		txtpnfuckOffwelcome_1_1.setEditable(false);
		txtpnfuckOffwelcome_1_1.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (Throwable t) {
					}
				}
			}
		});
		pageBrowseJarFile.add(txtpnfuckOffwelcome_1_1, BorderLayout.CENTER);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new EmptyBorder(0, 20, 10, 20));
		panel_11.setBackground(Color.WHITE);
		pageBrowseJarFile.add(panel_11, BorderLayout.SOUTH);
		panel_11.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_12 = new JPanel();
		panel_12.setPreferredSize(new Dimension(10, 40));
		panel_12.setBackground(Color.WHITE);
		panel_11.add(panel_12, BorderLayout.CENTER);
		panel_12.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_4 = new JLabel("path to 1.8.8.jar:");
		lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_4.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel_4.setPreferredSize(new Dimension(46, 20));
		panel_12.add(lblNewLabel_4, BorderLayout.NORTH);
		
		JPanel panel_13 = new JPanel();
		panel_13.setBackground(Color.WHITE);
		panel_13.setPreferredSize(new Dimension(10, 20));
		panel_12.add(panel_13, BorderLayout.SOUTH);
		panel_13.setLayout(new BorderLayout(0, 0));
		
		JButton btnBrowseJarFilePath = new JButton("Browse...");
		btnBrowseJarFilePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current = textField_JarFilePath.getText().trim();
				if(current.length() == 0) {
					File mc = getMinecraftDir();
					File f = new File(mc, "versions/1.8.8");
					if(!f.exists()) {
						f = mc;
					}
					if(!mc.exists()) {
						f = new File("");
					}
					current = f.getAbsolutePath();
				}
				JFileChooser fileChooser = new JFileChooser(new File(current));
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileHidingEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(fileChooser.showOpenDialog(frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
					textField_JarFilePath.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		panel_13.add(btnBrowseJarFilePath, BorderLayout.EAST);
		
		JPanel panel_15 = new JPanel();
		panel_15.setBorder(new EmptyBorder(0, 0, 0, 10));
		panel_15.setBackground(Color.WHITE);
		panel_13.add(panel_15, BorderLayout.CENTER);
		panel_15.setLayout(new BorderLayout(0, 0));
		
		textField_JarFilePath = new JTextField();
		textField_JarFilePath.setText("");
		panel_15.add(textField_JarFilePath, BorderLayout.CENTER);
		textField_JarFilePath.setColumns(10);
		frmCompileLatestClient.setBounds(100, 100, 640, 480);
		frmCompileLatestClient.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JPanel pageBrowseAssetsIndexJSON = new JPanel();
		pageBrowseAssetsIndexJSON.setBackground(Color.WHITE);
		pagesRoot.add(pageBrowseAssetsIndexJSON, "pageBrowseAssetsIndexJSON");
		pageBrowseAssetsIndexJSON.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffminecraft = new JTextPane();
		txtpnfuckOffminecraft.setEditable(false);
		txtpnfuckOffminecraft.setMargin(new Insets(10, 10, 10, 10));
		txtpnfuckOffminecraft.setContentType("text/html");
		txtpnfuckOffminecraft.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Minecraft 1.8 Assets Index JSON</p>\r\n<p style=\"font-size:11px;\">Some of Minecraft 1.8's assets are not included in the 1.8.8.jar file, they are downloaded separately, but are identified by their SHA-1 checksums. An additional JSON file must also be downloaded in order to map the checksums to their internal filenames</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Complete the previous step, then use the file chooser below to navigate to your \".minecraft\" folder, open the folder named \"assets\", then open the folder within it called \"indexes\", and then within that folder select the JSON file called \"1.8.json\"</p>\r\n<p style=\"font-size:11px;\">You can also download the JSON file from Mojang here:<br /><a style=\"font-size:9px;\" href=\"https://launchermeta.mojang.com/v1/packages/f6ad102bcaa53b1a58358f16e376d548d44933ec/1.8.json\">https://launchermeta.mojang.com/v1/packages/f6ad102bcaa53b1a58358f16e376d548d44933ec/1.8.json</a></p>\r\n</body>\r\n</html>");
		txtpnfuckOffminecraft.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (Throwable t) {
					}
				}
			}
		});
		pageBrowseAssetsIndexJSON.add(txtpnfuckOffminecraft, BorderLayout.CENTER);
		
		JPanel panel_17 = new JPanel();
		panel_17.setBorder(new EmptyBorder(0, 20, 30, 20));
		panel_17.setBackground(Color.WHITE);
		pageBrowseAssetsIndexJSON.add(panel_17, BorderLayout.SOUTH);
		panel_17.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_18 = new JPanel();
		panel_18.setPreferredSize(new Dimension(10, 40));
		panel_18.setBackground(Color.WHITE);
		panel_17.add(panel_18, BorderLayout.CENTER);
		panel_18.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_5 = new JLabel("path to 1.8.json:");
		lblNewLabel_5.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_5.setPreferredSize(new Dimension(46, 20));
		panel_18.add(lblNewLabel_5, BorderLayout.NORTH);
		
		JPanel panel_19 = new JPanel();
		panel_19.setBackground(Color.WHITE);
		panel_19.setPreferredSize(new Dimension(10, 20));
		panel_18.add(panel_19, BorderLayout.SOUTH);
		panel_19.setLayout(new BorderLayout(0, 0));
		
		JButton btnBrowseAssetsIndexJSON = new JButton("Browse...");
		btnBrowseAssetsIndexJSON.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current = textField_AssetsIndexJSON.getText().trim();
				if(current.length() == 0) {
					File mc = getMinecraftDir();
					File f = new File(mc, "assets/indexes");
					if(!f.exists()) {
						f = mc;
					}
					if(!mc.exists()) {
						f = new File("");
					}
					current = f.getAbsolutePath();
				}
				JFileChooser fileChooser = new JFileChooser(new File(current));
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileHidingEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(fileChooser.showOpenDialog(frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
					textField_AssetsIndexJSON.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		panel_19.add(btnBrowseAssetsIndexJSON, BorderLayout.EAST);
		
		JPanel panel_20 = new JPanel();
		panel_20.setBackground(Color.WHITE);
		panel_20.setBorder(new EmptyBorder(0, 0, 0, 10));
		panel_19.add(panel_20, BorderLayout.CENTER);
		panel_20.setLayout(new BorderLayout(0, 0));
		
		textField_AssetsIndexJSON = new JTextField();
		panel_20.add(textField_AssetsIndexJSON, BorderLayout.CENTER);
		textField_AssetsIndexJSON.setColumns(10);
		
		JPanel pageMavenRepo = new JPanel();
		pageMavenRepo.setBackground(Color.WHITE);
		pagesRoot.add(pageMavenRepo, "pageMavenRepo");
		pageMavenRepo.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffeaglercraftx = new JTextPane();
		txtpnfuckOffeaglercraftx.setContentType("text/html");
		txtpnfuckOffeaglercraftx.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">TeaVM Java to JavaScript Compiler</p>\r\n<p style=\"font-size:11px;\">EaglercraftX 1.8 uses TeaVM 0.6.1 to compile java to javascript. It's not included in the eagler repository to save space, so it must be downloaded from a public maven repository via HTTP, or loaded from a temporary local directory</p>\r\n</body>\r\n</html>");
		txtpnfuckOffeaglercraftx.setEditable(false);
		txtpnfuckOffeaglercraftx.setMargin(new Insets(10, 10, 10, 10));
		pageMavenRepo.add(txtpnfuckOffeaglercraftx, BorderLayout.NORTH);
		
		JPanel panel_22 = new JPanel();
		panel_22.setBackground(Color.WHITE);
		pageMavenRepo.add(panel_22, BorderLayout.CENTER);
		panel_22.setLayout(null);
		
		JLabel lblNewLabel_6 = new JLabel("Download via HTTP:");
		lblNewLabel_6.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_6.setBounds(12, 0, 329, 20);
		panel_22.add(lblNewLabel_6);
		
		ButtonGroup repoButtonGroup = new ButtonGroup();
		
		rdbtnMavenRepoCentral = new JRadioButton("https://repo1.maven.org/maven2/");
		rdbtnMavenRepoCentral.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnMavenRepoCentral.setSelected(true);
		rdbtnMavenRepoCentral.setBackground(Color.WHITE);
		rdbtnMavenRepoCentral.setBounds(28, 27, 474, 23);
		repoButtonGroup.add(rdbtnMavenRepoCentral);
		panel_22.add(rdbtnMavenRepoCentral);
		
		rdbtnMavenRepoBintray = new JRadioButton("(DEPRECATED) https://jcenter.bintray.com/");
		rdbtnMavenRepoBintray.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnMavenRepoBintray.setBackground(Color.WHITE);
		rdbtnMavenRepoBintray.setBounds(28, 52, 456, 23);
		repoButtonGroup.add(rdbtnMavenRepoBintray);
		panel_22.add(rdbtnMavenRepoBintray);
		
		rdbtnMavenRepoSonatype = new JRadioButton("https://oss.sonatype.org/content/repositories/releases/");
		rdbtnMavenRepoSonatype.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnMavenRepoSonatype.setBackground(Color.WHITE);
		rdbtnMavenRepoSonatype.setBounds(28, 78, 456, 23);
		repoButtonGroup.add(rdbtnMavenRepoSonatype);
		panel_22.add(rdbtnMavenRepoSonatype);
		
		rdbtnMavenRepoCustom = new JRadioButton("");
		rdbtnMavenRepoCustom.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textField_MavenRepoCustomURL.setEnabled(rdbtnMavenRepoCustom.isSelected());
			}
		});
		rdbtnMavenRepoCustom.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnMavenRepoCustom.setBackground(Color.WHITE);
		rdbtnMavenRepoCustom.setBounds(28, 104, 21, 23);
		repoButtonGroup.add(rdbtnMavenRepoCustom);
		panel_22.add(rdbtnMavenRepoCustom);
		
		textField_MavenRepoCustomURL = new JTextField();
		textField_MavenRepoCustomURL.setEnabled(false);
		textField_MavenRepoCustomURL.setBounds(51, 106, 195, 20);
		panel_22.add(textField_MavenRepoCustomURL);
		textField_MavenRepoCustomURL.setColumns(10);
		
		JLabel lblNewLabel_6_1 = new JLabel("Local directory:");
		lblNewLabel_6_1.setFont(new Font("Dialog", Font.BOLD, 12));
		lblNewLabel_6_1.setBounds(12, 134, 329, 20);
		panel_22.add(lblNewLabel_6_1);
		
		rdbtnMavenRepoLocal = new JRadioButton("");
		rdbtnMavenRepoLocal.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnMavenRepoLocal.setBackground(Color.WHITE);
		rdbtnMavenRepoLocal.setBounds(28, 162, 21, 23);
		repoButtonGroup.add(rdbtnMavenRepoLocal);
		panel_22.add(rdbtnMavenRepoLocal);
		
		textField_MavenRepoLocal = new JTextField();
		textField_MavenRepoLocal.setEnabled(false);
		textField_MavenRepoLocal.setColumns(10);
		textField_MavenRepoLocal.setBounds(51, 163, 195, 20);
		panel_22.add(textField_MavenRepoLocal);
		
		final JButton btnBrowseMavenRepoLocal = new JButton("Browse...");
		btnBrowseMavenRepoLocal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current = textField_MavenRepoLocal.getText().trim();
				if(current.length() == 0) {
					current = (new File("")).getAbsolutePath();
				}
				JFileChooser fileChooser = new JFileChooser(new File(current));
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileHidingEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fileChooser.showOpenDialog(frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
					textField_MavenRepoLocal.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnBrowseMavenRepoLocal.setEnabled(false);
		btnBrowseMavenRepoLocal.setBounds(256, 162, 89, 23);
		panel_22.add(btnBrowseMavenRepoLocal);
		
		rdbtnMavenRepoLocal.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				textField_MavenRepoLocal.setEnabled(rdbtnMavenRepoLocal.isSelected());
				btnBrowseMavenRepoLocal.setEnabled(rdbtnMavenRepoLocal.isSelected());
			}
		});
		
		JPanel pageFFmpeg = new JPanel();
		pageFFmpeg.setBackground(Color.WHITE);
		pagesRoot.add(pageFFmpeg, "pageFFmpeg");
		pageFFmpeg.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffteavm_1 = new JTextPane();
		txtpnfuckOffteavm_1.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					URL url = e.getURL();
					if(url.getHost().equals("check_ffmpeg")) {
						if(isFFmpegOnPath()) {
							JOptionPane.showMessageDialog(frmCompileLatestClient, "FFmpeg was found on your system's path!\nIt will be selected automatically", "Success", JOptionPane.INFORMATION_MESSAGE);
							chckbxUsePathFFmpeg.setEnabled(true);
							chckbxUsePathFFmpeg.setSelected(true);
							textField_pathToFFmpeg.setEnabled(false);
							btnBrowsePathToFFmpeg.setEnabled(false);
							hasAskedFFmpegOnPath = true;
							setPage(page + 1);
						}else {
							JOptionPane.showMessageDialog(frmCompileLatestClient, "FFmpeg was not found on your system's path!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}else {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (Throwable t) {
						}
					}
				}
			}
		});
		txtpnfuckOffteavm_1.setEditable(false);
		txtpnfuckOffteavm_1.setMargin(new Insets(10, 10, 10, 10));
		txtpnfuckOffteavm_1.setContentType("text/html");
		txtpnfuckOffteavm_1.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">FFmpeg Audio Encoder</p>\r\n<p style=\"font-size:11px;\">A tool called FFmpeg is required to compress minecraft's audio so it's smaller, the tool is an excessively large single standalone executable file so it is not included in this repository and must be downloaded externally</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">Download the FFmpeg executable here: <a href=\"https://ffmpeg.org/download.html\">https://ffmpeg.org/download.html</a></p>\r\n<p style=\"font-size:11px;\">Select where you downloaded the file below</p>\r\n<p style=\"font-size:11px;font-weight:bold;\">If you are on linux and have an FFmpeg package available, install it and click <a href=\"https://check_ffmpeg/\">here</a> to detect it on your path automatically</p>\r\n</body>\r\n</html>");
		pageFFmpeg.add(txtpnfuckOffteavm_1, BorderLayout.CENTER);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(0, 20, 40, 20));
		panel_4.setBackground(Color.WHITE);
		pageFFmpeg.add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel_5.setPreferredSize(new Dimension(10, 40));
		panel_5.setBackground(Color.WHITE);
		panel_4.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_9 = new JLabel(FFMPEG.windows ? "path to ffmpeg.exe:" : "path to ffmpeg:");
		lblNewLabel_9.setPreferredSize(new Dimension(76, 20));
		lblNewLabel_9.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_5.add(lblNewLabel_9, BorderLayout.NORTH);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		panel_6.setPreferredSize(new Dimension(10, 20));
		panel_5.add(panel_6, BorderLayout.SOUTH);
		panel_6.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new EmptyBorder(0, 0, 0, 10));
		panel_10.setBackground(Color.WHITE);
		panel_6.add(panel_10, BorderLayout.CENTER);
		panel_10.setLayout(new BorderLayout(0, 0));
		
		textField_pathToFFmpeg = new JTextField();
		textField_pathToFFmpeg.setText("");
		panel_10.add(textField_pathToFFmpeg, BorderLayout.CENTER);
		textField_pathToFFmpeg.setColumns(10);
		
		JPanel panel_34 = new JPanel();
		panel_6.add(panel_34, BorderLayout.EAST);
		panel_34.setLayout(new BorderLayout(0, 0));
		
		btnBrowsePathToFFmpeg = new JButton("Browse...");
		panel_34.add(btnBrowsePathToFFmpeg);
		
		chckbxUsePathFFmpeg = new JCheckBox("Use FFmpeg on PATH");
		chckbxUsePathFFmpeg.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(chckbxUsePathFFmpeg.isSelected()) {
					textField_pathToFFmpeg.setEnabled(false);
					btnBrowsePathToFFmpeg.setEnabled(false);
				}else {
					textField_pathToFFmpeg.setEnabled(true);
					btnBrowsePathToFFmpeg.setEnabled(true);
				}
			}
		});
		chckbxUsePathFFmpeg.setFont(new Font("Dialog", Font.BOLD, 12));
		chckbxUsePathFFmpeg.setBackground(Color.WHITE);
		chckbxUsePathFFmpeg.setMargin(new Insets(2, 10, 2, 2));
		panel_34.add(chckbxUsePathFFmpeg, BorderLayout.EAST);
		btnBrowsePathToFFmpeg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current = textField_pathToFFmpeg.getText().trim();
				if(current.length() == 0) {
					File f = new File(System.getProperty("user.home", "."), "Downloads");
					if(!f.exists()) {
						f = new File(System.getProperty("user.home", "."));
					}
					current = f.getAbsolutePath();
				}
				JFileChooser fileChooser = new JFileChooser(new File(current));
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileHidingEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(fileChooser.showOpenDialog(frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
					textField_pathToFFmpeg.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		JPanel pageOutputDirectory = new JPanel();
		pageOutputDirectory.setBackground(Color.WHITE);
		pagesRoot.add(pageOutputDirectory, "pageOutputDirectory");
		pageOutputDirectory.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnfuckOffteavm = new JTextPane();
		txtpnfuckOffteavm.setEditable(false);
		txtpnfuckOffteavm.setContentType("text/html");
		txtpnfuckOffteavm.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;\">Output Directory</p>\r\n<p style=\"font-size:11px;\">Select the destination directory where you would like this tool to save the compiled files to once this tool is finished</p>\r\n<p style=\"font-size:11px;\">The the tool will generate an \"index.html\" file, a \"classes.js\" file, a \"classes.js.map\" file, an \"assets.epk\" file, a \"lang\" directory to hold additional .lang files, and optionally an offline download version of the client that does not require an HTTP server</p>\r\n</body>\r\n</html>");
		txtpnfuckOffteavm.setMargin(new Insets(10, 10, 10, 10));
		pageOutputDirectory.add(txtpnfuckOffteavm, BorderLayout.CENTER);
		
		JPanel panel_30 = new JPanel();
		panel_30.setBorder(new EmptyBorder(0, 20, 40, 20));
		panel_30.setBackground(Color.WHITE);
		pageOutputDirectory.add(panel_30, BorderLayout.SOUTH);
		panel_30.setLayout(new BoxLayout(panel_30, BoxLayout.Y_AXIS));
		
		JPanel panel_31 = new JPanel();
		panel_31.setPreferredSize(new Dimension(10, 40));
		panel_31.setBackground(Color.WHITE);
		panel_30.add(panel_31);
		panel_31.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_8 = new JLabel("compiler output directory:");
		lblNewLabel_8.setPreferredSize(new Dimension(124, 20));
		lblNewLabel_8.setFont(new Font("Dialog", Font.BOLD, 12));
		panel_31.add(lblNewLabel_8, BorderLayout.NORTH);
		
		JPanel panel_32 = new JPanel();
		panel_32.setBackground(Color.WHITE);
		panel_32.setPreferredSize(new Dimension(10, 20));
		panel_31.add(panel_32, BorderLayout.SOUTH);
		panel_32.setLayout(new BorderLayout(0, 0));
		
		JButton btnBrowseOutputDirectory = new JButton("Browse...");
		btnBrowseOutputDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String current = textField_OutputDirectory.getText().trim();
				if(current.length() == 0) {
					current = (new File("")).getAbsolutePath();
				}
				JFileChooser fileChooser = new JFileChooser(new File(current));
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileHidingEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fileChooser.showOpenDialog(frmCompileLatestClient) == JFileChooser.APPROVE_OPTION) {
					textField_OutputDirectory.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		panel_32.add(btnBrowseOutputDirectory, BorderLayout.EAST);
		
		JPanel panel_33 = new JPanel();
		panel_33.setBorder(new EmptyBorder(0, 0, 0, 10));
		panel_33.setBackground(Color.WHITE);
		panel_32.add(panel_33, BorderLayout.CENTER);
		panel_33.setLayout(new BorderLayout(0, 0));
		
		textField_OutputDirectory = new JTextField();
		panel_33.add(textField_OutputDirectory, BorderLayout.CENTER);
		textField_OutputDirectory.setColumns(10);
		
		JPanel panel_16 = new JPanel();
		panel_16.setBackground(Color.WHITE);
		panel_30.add(panel_16);
		panel_16.setLayout(new BorderLayout(0, 0));
		
		chckbxOutputOfflineDownload = new JCheckBox("Generate Offline Download");
		chckbxOutputOfflineDownload.setSelected(true);
		panel_16.add(chckbxOutputOfflineDownload);
		chckbxOutputOfflineDownload.setBackground(Color.WHITE);
		chckbxOutputOfflineDownload.setPreferredSize(new Dimension(97, 30));
		
		JPanel panel_21 = new JPanel();
		panel_21.setBackground(Color.WHITE);
		panel_30.add(panel_21);
		panel_21.setLayout(new BorderLayout(0, 0));
		
		chckbxKeepTemporaryFiles = new JCheckBox("Keep Temporary Files");
		chckbxKeepTemporaryFiles.setBackground(Color.WHITE);
		chckbxKeepTemporaryFiles.setPreferredSize(new Dimension(129, 30));
		panel_21.add(chckbxKeepTemporaryFiles, BorderLayout.NORTH);
		
		JPanel pageConfirmSettings = new JPanel();
		pageConfirmSettings.setBackground(Color.WHITE);
		pagesRoot.add(pageConfirmSettings, "pageConfirmSettings");
		pageConfirmSettings.setLayout(new BorderLayout(0, 0));
		
		txtpnfuckOffreview = new JTextPane();
		txtpnfuckOffreview.setEditable(false);
		txtpnfuckOffreview.setContentType("text/html");
		txtpnfuckOffreview.setText("<html>\r\n<head><title>fuck off</title></head>\r\n<body style=\"font-family:sans-serif;margin:0px;\">\r\n<p style=\"font-size:17px;margin:7px 0px;\">Are these correct?</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Repository Path:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Mod Coder Pack:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; 1.8.8.jar path:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; 1.8.json path:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; TeaVM Maven:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; FFmpeg:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Output Directory:</span> path here</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Make Offline Download:</span> yes/no</p>\r\n<p style=\"font-size:11px;margin:3px 0px;\"><span style=\"font-weight:bold;\">&emsp;&bull; Keep Temporary Files:</span> yes/no</p>\r\n<p style=\"font-size:13px;margin-top:6px;\">&nbsp;Press the \"Compile >>\" button to confirm</p>\r\n</body>\r\n</html>");
		txtpnfuckOffreview.setMargin(new Insets(10, 10, 10, 10));
		pageConfirmSettings.add(txtpnfuckOffreview, BorderLayout.CENTER);
		
		JPanel pageLogOutput = new JPanel();
		pageLogOutput.setBackground(Color.WHITE);
		pagesRoot.add(pageLogOutput, "pageLogOutput");
		pageLogOutput.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		pageLogOutput.add(scrollPane, BorderLayout.CENTER);
		
		txtpnLogOutput = new JTextPane();
		txtpnLogOutput.setAutoscrolls(false);
		txtpnLogOutput.setMargin(new Insets(10, 10, 10, 10));
		txtpnLogOutput.setContentType("text/html");
		txtpnLogOutput.setText("<html><head><title>shit</title><style type=\"text/css\">pre{font-family:\"Consolas\",\"Andale Mono\",monospace;}</style></head><body id=\"logContainer\" style=\"margin:0px;\"><pre></pre></body></html>");
		txtpnLogOutput.setEditable(false);
		scrollPane.setViewportView(txtpnLogOutput);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(txtpnLogOutput, popupMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Select All");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtpnLogOutput.selectAll();
			}
		});
		popupMenu.add(mntmNewMenuItem);
		
		JSeparator separator_3 = new JSeparator();
		popupMenu.add(separator_3);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Copy");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtpnLogOutput.getSelectionStart() == txtpnLogOutput.getSelectionEnd()) {
					txtpnLogOutput.selectAll();
				}
				txtpnLogOutput.copy();
			}
		});
		mntmNewMenuItem_1.setFont(mntmNewMenuItem_1.getFont().deriveFont(Font.BOLD));
		popupMenu.add(mntmNewMenuItem_1);
		
		JPanel panel_29 = new JPanel();
		panel_29.setBackground(Color.DARK_GRAY);
		panel_29.setPreferredSize(new Dimension(10, 1));
		panel_3.add(panel_29, BorderLayout.SOUTH);
		panel_29.setLayout(null);
	}

	private StringBuilder logAccumPrev = new StringBuilder();
	private StringBuilder logAccum = new StringBuilder();
	private Element logAccumBody = null;
	private volatile boolean logDirty = false;
	private volatile boolean isError = false;

	public void logInfo(String line) {
		line = htmlentities2(line);
		synchronized(logAccum) {
			if(logAccum.length() > 0) {
				if(isError) {
					isError = false;
					logAccum.append("</pre><pre>");
				}
				logAccum.append(line);
			}else {
				if(isError) {
					isError = false;
					logAccum.append("<pre>");
					logAccum.append(line);
				}else {
					logAccumPrev.append(line);
				}
			}
			logDirty = true;
		}
	}

	public void logError(String line) {
		line = htmlentities2(line);
		synchronized(logAccum) {
			if(logAccum.length() > 0) {
				if(!isError) {
					isError = true;
					logAccum.append("</pre><pre style=\"color:#BB0000;\">");
				}
				logAccum.append(line);
			}else {
				if(!isError) {
					isError = true;
					logAccum.append("<pre style=\"color:#BB0000;\">");
					logAccum.append(line);
				}else {
					logAccumPrev.append(line);
				}
			}
			logDirty = true;
		}
	}

	public void launchLogUpdateThread() {
		Thread lazyLogUpdateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(100l);
						synchronized(logAccum) {
							if(logDirty) {
								EventQueue.invokeAndWait(new Runnable() {
									public void run() {
										HTMLDocument ee = ((HTMLDocument)txtpnLogOutput.getDocument());
										if(logAccumBody == null) {
											logAccumBody = ee.getElement("logContainer");
										}
										if(logAccumPrev.length() > 0) {
											try {
												ee.insertString(logAccumBody.getElement(logAccumBody.getElementCount() - 1).getEndOffset() - 1, logAccumPrev.toString(), null);
											} catch (BadLocationException e) {
											}
											logAccumPrev = new StringBuilder();
										}
										if(logAccum.length() > 0) {
											logAccum.append("</pre>");
											try {
												ee.insertBeforeEnd(logAccumBody, logAccum.toString());
											} catch (BadLocationException e) {
											} catch (IOException e) {
											}
											logAccum = new StringBuilder();
										}
									}
								});
								EventQueue.invokeAndWait(new Runnable() {
									public void run() {
										JScrollBar bar = scrollPane.getVerticalScrollBar();
										bar.setValue(bar.getMaximum());
										scrollPane.getHorizontalScrollBar().setValue(0);
									}
								});
								logDirty = false;
							}
						}
					}catch(Throwable t) {
					}
				}
			}
		}, "LazyLogUpdateThread");
		lazyLogUpdateThread.setDaemon(true);
		lazyLogUpdateThread.start();
	}

	public void finishCompiling(final boolean failed, final String reason) {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					if(!finished) {
						lblProgressState.setText(failed ? "Compilation Failed!" : "Compilation Succeeded!");
						lblProgressState.setForeground(failed ? new Color(0x88, 0, 0) : new Color(0, 0x88, 0));
						btnNext.setEnabled(true);
						finished = true;
						if(failed) {
							JOptionPane.showMessageDialog(frmCompileLatestClient, wordWrap("Failed to Compile Client!\nReason: " + reason), "Error", JOptionPane.ERROR_MESSAGE);
						}else {
							JOptionPane.showMessageDialog(frmCompileLatestClient, "Finished Compiling", "Success", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	public String getRepositoryURL() {
		if(rdbtnMavenRepoCustom.isSelected()) {
			return textField_MavenRepoCustomURL.getText();
		}else if(rdbtnMavenRepoSonatype.isSelected()) {
			return "https://oss.sonatype.org/content/repositories/releases/";
		}else if(rdbtnMavenRepoBintray.isSelected()) {
			return "https://jcenter.bintray.com/";
		}else if(rdbtnMavenRepoCentral.isSelected()) {
			return "https://repo1.maven.org/maven2/";
		}else {
			return null;
		}
	}

	private boolean knowFoundFFmpeg = false;
	private boolean foundFFmpeg = false;
	private boolean hasAskedFFmpegOnPath = false;

	private boolean isFFmpegOnPath() {
		if(!knowFoundFFmpeg) {
			if(foundFFmpeg = FFMPEG.checkFFMPEGOnPath()) {
				knowFoundFFmpeg = true;
			}
		}
		return foundFFmpeg;
	}

	private static String htmlentities(String strIn) {
		return strIn.replace("<", "&lt;").replace(">", "&gt;");
	}

	private static String htmlentities2(String strIn) {
		return strIn.replace("</pre>", "[/pre]");
	}

	private static String wordWrap(String str) {
		StringBuilder ret = new StringBuilder();
		while(str.length() > 100) {
			ret.append(str.substring(0, 100)).append('\n');
			str = str.substring(100);
		}
		ret.append(str);
		return ret.toString();
	}

	private static String loadLicense() {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(CompileLatestClientFrame.class.getResourceAsStream("/lang/LICENSE.txt"), StandardCharsets.UTF_8))) {
			StringBuilder ret = new StringBuilder();
			char[] copyBuffer = new char[4096];
			int i;
			while((i = reader.read(copyBuffer)) != -1) {
				ret.append(copyBuffer, 0, i);
			}
			return ret.toString();
		} catch (IOException e) {
			return "Could not load LICENSE text!\n\nPlease read the file named \"LICENSE\" in the root directory of the repository before continuing";
		}
	}

}
