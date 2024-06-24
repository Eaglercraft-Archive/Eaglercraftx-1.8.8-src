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

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.lwjgl.MainMenuCreditsDialog;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class PlatformApplication {
	
	private static long win = 0l;
	
	static void initHooks(long glfwWindow) {
		win = glfwWindow;
	}

	public static void openLink(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Throwable var5) {
			EagRuntime.debugPrintStackTrace(var5);
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
		setLocalStorage(name, data, true);
	}

	public static void setLocalStorage(String name, byte[] data, boolean hooks) {
		if(data == null) {
			(new File("_eagstorage."+name+".dat")).delete();
		}else {
			try(FileOutputStream f = new FileOutputStream(new File("_eagstorage."+name+".dat"))) {
				f.write(data);
			} catch (IOException e) {
				EagRuntime.debugPrintStackTrace(e);
			}
		}
	}

	public static byte[] getLocalStorage(String data) {
		return getLocalStorage(data, true);
	}

	public static byte[] getLocalStorage(String data, boolean hooks) {
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

	public static void clearFileChooserResult() {
		fileChooserHasResult = false;
		fileChooserResultObject = null;
	}

	private static MainMenuCreditsDialog creditsDialog = null;

	public static void openCreditsPopup(String text) {
		if(creditsDialog == null) {
			creditsDialog = new MainMenuCreditsDialog();
		}
		creditsDialog.setCreditsText(text);
		creditsDialog.setLocationRelativeTo(null);
		creditsDialog.setVisible(true);
	}

	private static final File downloadsDirectory = new File("downloads");
	private static final Logger downloadsLogger = LogManager.getLogger("DownloadsFolder");

	public static void downloadFileWithName(String fileName, byte[] fileContents) {
		if(!downloadsDirectory.isDirectory() && !downloadsDirectory.mkdirs()) {
			throw new RuntimeException("Could not create directory: " + downloadsDirectory.getAbsolutePath());
		}

		File f = new File(downloadsDirectory, fileName);
		if(f.exists()) { 
			String name = fileName;
			String ext = "";
			int i = fileName.lastIndexOf('.');
			if(i != -1) {
				name = fileName.substring(0, i);
				ext = fileName.substring(i);
			}

			i = 0;
			do {
				f = new File(downloadsDirectory, name + " (" + (++i) + ")" + ext);
			}while(f.exists());
		}

		try(FileOutputStream fos = new FileOutputStream(f)) {
			fos.write(fileContents);
		}catch(IOException ex) {
			throw new RuntimeException("Could not save file: " + f.getAbsolutePath());
		}

		downloadsLogger.info("Saved {} byte file to: {}", fileContents.length, f.getAbsolutePath());

		try {
			Desktop.getDesktop().open(downloadsDirectory);
		}catch(Throwable t) {
		}
	}

	public static void addLogMessage(String logMessage, boolean isError) {
		
	}

	public static boolean isShowingDebugConsole() {
		return false;
	}

	public static void showDebugConsole() {
		
	}

	public static void setMCServerWindowGlobal(String str) {
		
	}

}
