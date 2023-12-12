package net.lax1dude.eaglercraft.v1_8.buildtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
public class LicensePrompt {

	public static void main(String[] args) {
		System.out.println();
		display();
	}

	public static void display() {
		System.out.println("WARNING: You must agree to the LICENSE before running this command");
		System.out.println();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(LicensePrompt.class.getResourceAsStream("/lang/LICENSE_console_wrapped.txt"), StandardCharsets.UTF_8))) {
			String line;
			while((line = reader.readLine()) != null) {
				if(line.equals("<press enter>")) {
					pressEnter();
				}else {
					System.out.println(line);
				}
			}
		}catch(IOException ex) {
			System.err.println();
			System.err.println("ERROR: could not display license text");
			System.err.println("Please read the \"LICENSE\" file before using this software");
			System.err.println();
			pressEnter();
		}
	}

	private static void pressEnter() {
		System.out.println();
		System.out.println("(press ENTER to continue)");
		while(true) {
			try {
				if(System.in.read() == '\n') {
					break;
				}
			}catch(IOException ex) {
				throw new RuntimeException("Unexpected IOException reading STDIN", ex);
			}
		}
	}

}
