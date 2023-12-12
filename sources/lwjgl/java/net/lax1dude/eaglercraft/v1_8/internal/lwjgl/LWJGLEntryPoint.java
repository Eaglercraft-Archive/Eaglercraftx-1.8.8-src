package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformANGLE;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderSource;
import net.minecraft.client.main.Main;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class LWJGLEntryPoint {

	public static Thread mainThread = null;

	public static void main_(String[] args) {
		mainThread = Thread.currentThread();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			System.err.println("Could not set system look and feel: " + e.toString());
		}
		
		LaunchRenderDocDialog lr = new LaunchRenderDocDialog();
		lr.setLocationRelativeTo(null);
		lr.setVisible(true);
		
		while(lr.isVisible()) {
			EagUtils.sleep(100l);
		}
		
		lr.dispose();
		
		getANGLEPlatformFromArgs(args);
		
		for(int i = 0; i < args.length; ++i) {
			if(args[i].equalsIgnoreCase("highp")) {
				ShaderSource.setHighP(true);
			}
		}
		
		EagRuntime.create();
		
		Main.appMain(new String[0]);
		
	}
	
	private static void getANGLEPlatformFromArgs(String[] args) {
		for(int i = 0; i < args.length; ++i) {
			EnumPlatformANGLE angle = EnumPlatformANGLE.fromId(args[i]);
			if(angle != EnumPlatformANGLE.DEFAULT) {
				PlatformRuntime.requestANGLE(angle);
				break;
			}
		}
	}

}
