package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.shit;

import java.awt.GraphicsEnvironment;

import javax.swing.JOptionPane;

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
public class MainClass {

	public static void main(String[] args) {
		System.err.println();
		System.err.println("ERROR: The EaglerXBungee 1.8 jar file is a PLUGIN intended to be used with BungeeCord!");
		System.err.println("Place this file in the \"plugins\" directory of your BungeeCord installation");
		System.err.println();
		try {
			tryShowPopup();
		}catch(Throwable t) {
		}
		System.exit(0);
	}

	private static void tryShowPopup() throws Throwable {
		if(!GraphicsEnvironment.isHeadless()) {
			JOptionPane.showMessageDialog(null, "ERROR: The EaglerXBungee 1.8 jar file is a PLUGIN intended to be used with BungeeCord!\nPlace this file in the \"plugins\" directory of your BungeeCord installation", "EaglerXBungee", JOptionPane.ERROR_MESSAGE);
		}
	}
}
