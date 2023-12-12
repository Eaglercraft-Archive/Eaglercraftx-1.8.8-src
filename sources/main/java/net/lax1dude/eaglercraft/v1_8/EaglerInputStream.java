package net.lax1dude.eaglercraft.v1_8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class EaglerInputStream extends ByteArrayInputStream {

	public EaglerInputStream(byte[] buf) {
		super(buf);
	}

	public EaglerInputStream(byte[] buf, int off, int len) {
		super(buf, off, len);
	}
	
	public static byte[] inputStreamToBytesQuiet(InputStream is) {
		if(is == null) {
			return null;
		}
		try {
			return inputStreamToBytes(is);
		}catch(IOException ex) {
			return null;
		}
	}
	
	public static byte[] inputStreamToBytes(InputStream is) throws IOException {
		if(is instanceof EaglerInputStream) {
			return ((EaglerInputStream) is).getAsArray();
		}else if(is instanceof ByteArrayInputStream) {
			byte[] ret = new byte[is.available()];
			is.read(ret);
			return ret;
		}else {
			ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
			byte[] buf = new byte[1024];
			int i;
			while((i = is.read(buf)) != -1) {
				os.write(buf, 0, i);
			}
			return os.toByteArray();
		}
	}

	public byte[] getAsArray() {
		if(pos == 0 && count == buf.length) {
			return buf;
		}else {
			byte[] ret = new byte[count];
			System.arraycopy(buf, pos, ret, 0, count);
			return ret;
		}
	}
	
	public boolean canUseArrayDirectly() {
		return pos == 0 && count == buf.length;
	}
	
	public int getPosition() {
		return pos;
	}
	
	public int getMark() {
		return mark;
	}
	
	public int getCount() {
		return count;
	}

}
