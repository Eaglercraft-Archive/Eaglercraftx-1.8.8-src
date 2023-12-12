package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.IOException;
import java.io.OutputStream;
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
public class ConsoleRedirector extends OutputStream {

	private final OutputStream stdout;
	private final boolean err;

	public ConsoleRedirector(boolean err) {
		stdout = err ? System.err : System.out;
		this.err = err;
	}

	@Override
	public void write(byte[] b, int o, int l) throws IOException {
		stdout.write(b, o, l);
		String append = new String(b, o, l, StandardCharsets.US_ASCII);
		if(err) {
			CompileLatestClientGUI.frame.logError(append);
		}else {
			CompileLatestClientGUI.frame.logInfo(append);
		}
	}

	@Override
	public void write(int b) throws IOException {
		stdout.write(b);
		write0(b);
	}

	private void write0(int b) throws IOException {
		char c = (char)b;
		if(c != '\r') {
			if(err && c != '\n') {
				CompileLatestClientGUI.frame.logError(new String(new char[] { c }));
			}else {
				CompileLatestClientGUI.frame.logInfo(new String(new char[] { c }));
			}
		}
	}

}
