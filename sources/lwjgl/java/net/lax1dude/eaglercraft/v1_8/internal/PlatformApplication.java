package net.lax1dude.eaglercraft.v1_8.internal;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

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
public class PlatformApplication {
	
	private static long win = 0l;
	
	static void initHooks(long glfwWindow) {
		win = glfwWindow;
	}

	public static void openLink(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Throwable var5) {
			var5.printStackTrace();
		}
	}

	public static void setClipboard(String text) {
		glfwSetClipboardString(win, text);
	}
	
	public static String getClipboard() {
		String str = glfwGetClipboardString(win);
		return str == null ? "" : str;
	}
	
	public static void setLocalStorage(String name, byte[] data) {
		try(FileOutputStream f = new FileOutputStream(new File("_eagstorage."+name+".dat"))) {
			f.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] getLocalStorage(String data) {
		File f = new File("_eagstorage."+data+".dat");
		if(!f.isFile()) {
			return null;
		}
		byte[] b = new byte[(int)f.length()];
		try(FileInputStream s = new FileInputStream(f)) {
			s.read(b);
			return b;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static String saveScreenshot() {
		return "nothing";
	}
	
	public static void showPopup(String msg) {
		JOptionPane pane = new JOptionPane(msg, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null,
				new Object[] { "OK" }, "OK");
		pane.setInitialValue("OK");
		JDialog dialog = pane.createDialog("EaglercraftX Runtime");
		pane.selectInitialValue();
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage("icon32.png"));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setAlwaysOnTop(true);
		dialog.setModal(true);
		dialog.setLocationByPlatform(true);
		dialog.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		dialog.setModalityType(ModalityType.TOOLKIT_MODAL);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	private static volatile boolean fileChooserOpen = false;
	private static volatile boolean fileChooserHasResult = false;
	private static volatile FileChooserResult fileChooserResultObject = null;

	public static void displayFileChooser(final String mime, final String ext) {
		if(!fileChooserOpen) {
			fileChooserOpen = true;
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					runDisplayFileChooser(mime, ext);
				}
			});
		}
	}

	private static void runDisplayFileChooser(String mime, String ext) {
		try {
			JFileChooser fc = new FileChooserAlwaysOnTop((new File(".")).getAbsoluteFile());
			fc.setDialogTitle("select a file");
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setFileFilter(new FileFilterExt(ext));
			if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				if(f != null) {
					String name = f.getName();
					byte[] bytes = new byte[(int)f.length()];
					try(FileInputStream is = new FileInputStream(f)) {
						is.read(bytes);
					}
					fileChooserResultObject = new FileChooserResult(name, bytes);
				}else {
					fileChooserResultObject = null;
				}
			}
		}catch(Throwable t) {
			fileChooserResultObject = null;
		}
		fileChooserOpen = false;
		fileChooserHasResult = true;
	}

	private static class FileChooserAlwaysOnTop extends JFileChooser {
		
		private FileChooserAlwaysOnTop(File file) {
			super(file);
		}
		
		protected JDialog createDialog(Component parent) throws HeadlessException {
			JDialog dialog = super.createDialog(parent);
			dialog.setAlwaysOnTop(true);
			return dialog;
		}
		
	}

	private static class FileFilterExt extends FileFilter {

		private final String extension;

		private FileFilterExt(String ext) {
			extension = ext;
		}

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith("." + extension);
		}

		@Override
		public String getDescription() {
			return extension + " files";
		}

	}

	public static boolean fileChooserHasResult() {
		return fileChooserHasResult;
	}

	public static FileChooserResult getFileChooserResult() {
		fileChooserHasResult = false;
		FileChooserResult res = fileChooserResultObject;
		fileChooserResultObject = null;
		return res;
	}

	public static void openCreditsPopup(String text) {
		
	}

}
