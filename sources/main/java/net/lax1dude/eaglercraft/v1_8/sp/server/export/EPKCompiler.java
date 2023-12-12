package net.lax1dude.eaglercraft.v1_8.sp.server.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class EPKCompiler {

	private final ByteArrayOutputStream os;
	private final CRC32 checkSum = new CRC32();
	private int lengthIntegerOffset = 0;
	private int totalFileCount = 0;

	public EPKCompiler(String name, String owner, String type) {
		os = new ByteArrayOutputStream(0x200000);
		try {
			
			os.write(new byte[]{(byte)69,(byte)65,(byte)71,(byte)80,(byte)75,(byte)71,(byte)36,(byte)36}); // EAGPKG$$
			os.write(new byte[]{(byte)6,(byte)118,(byte)101,(byte)114,(byte)50,(byte)46,(byte)48}); // 6 + ver2.0
			Date d = new Date();
			
			byte[] filename = (name + ".epk").getBytes(StandardCharsets.UTF_8);
			os.write(filename.length);
			os.write(filename);
			
			byte[] comment = ("\n\n #  Eagler EPK v2.0 (c) " + EagRuntime.fixDateFormat(new SimpleDateFormat("yyyy")).format(d) + " " +
					owner + "\n #  export: on " + EagRuntime.fixDateFormat(new SimpleDateFormat("MM/dd/yyyy")).format(d) + " at " +
					EagRuntime.fixDateFormat(new SimpleDateFormat("hh:mm:ss aa")).format(d) + "\n\n #  world name: " + name + "\n\n")
					.getBytes(StandardCharsets.UTF_8);

			os.write((comment.length >> 8) & 255);
			os.write(comment.length & 255);
			os.write(comment);
			
			writeLong(d.getTime(), os);
			
			lengthIntegerOffset = os.size();
			os.write(new byte[]{(byte)255,(byte)255,(byte)255,(byte)255}); // this will be replaced with the file count
			
			os.write('0'); // compression type: none
			
			os.write(new byte[]{(byte)72,(byte)69,(byte)65,(byte)68}); // HEAD
			os.write(new byte[]{(byte)9,(byte)102,(byte)105,(byte)108,(byte)101,(byte)45,(byte)116,(byte)121,
					(byte)112,(byte)101}); // 9 + file-type
			
			byte[] typeBytes = type.getBytes(StandardCharsets.UTF_8);
			writeInt(typeBytes.length, os);
			os.write(typeBytes); // write type
			os.write('>');
			
			++totalFileCount;
			
			os.write(new byte[]{(byte)72,(byte)69,(byte)65,(byte)68}); // HEAD
			os.write(new byte[]{(byte)10,(byte)119,(byte)111,(byte)114,(byte)108,(byte)100,(byte)45,(byte)110,
					(byte)97,(byte)109,(byte)101}); // 10 + world-name
			
			byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
			writeInt(nameBytes.length, os);
			os.write(nameBytes); // write name
			os.write('>');
			
			++totalFileCount;
			
			os.write(new byte[]{(byte)72,(byte)69,(byte)65,(byte)68}); // HEAD
			os.write(new byte[]{(byte)11,(byte)119,(byte)111,(byte)114,(byte)108,(byte)100,(byte)45,(byte)111,
					(byte)119,(byte)110,(byte)101,(byte)114}); // 11 + world-owner
			
			byte[] ownerBytes = owner.getBytes(StandardCharsets.UTF_8);
			writeInt(ownerBytes.length, os);
			os.write(ownerBytes); // write owner
			os.write('>');
			
			++totalFileCount;
			
		}catch(IOException ex) {
			throw new RuntimeException("This happened somehow", ex);
		}
	}
	
	public void append(String name, byte[] dat) {
		try {
			
			checkSum.reset();
			checkSum.update(dat, 0, dat.length);
			long sum = checkSum.getValue();
			
			os.write(new byte[]{(byte)70,(byte)73,(byte)76,(byte)69}); // FILE
			
			byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);
			os.write(nameBytes.length);
			os.write(nameBytes);
			
			writeInt(dat.length + 5, os);
			writeInt((int)sum, os);
			
			os.write(dat);
			
			os.write(':');
			os.write('>');
			
			++totalFileCount;
			
		}catch(IOException ex) {
			throw new RuntimeException("This happened somehow", ex);
		}
	}
	
	public byte[] complete() {
		try {
			
			os.write(new byte[]{(byte)69,(byte)78,(byte)68,(byte)36}); // END$
			os.write(new byte[]{(byte)58,(byte)58,(byte)58,(byte)89,(byte)69,(byte)69,(byte)58,(byte)62}); // :::YEE:>
			
			byte[] ret = os.toByteArray();

			ret[lengthIntegerOffset] = (byte)((totalFileCount >> 24) & 0xFF);
			ret[lengthIntegerOffset + 1] = (byte)((totalFileCount >> 16) & 0xFF);
			ret[lengthIntegerOffset + 2] = (byte)((totalFileCount >> 8) & 0xFF);
			ret[lengthIntegerOffset + 3] = (byte)(totalFileCount & 0xFF);
			
			return ret;
			
		}catch(IOException ex) {
			throw new RuntimeException("This happened somehow", ex);
		}
	}
	
	public static void writeInt(int i, OutputStream os) throws IOException {
		os.write((i >> 24) & 0xFF);
		os.write((i >> 16) & 0xFF);
		os.write((i >> 8) & 0xFF);
		os.write(i & 0xFF);
	}
	
	public static void writeLong(long i, OutputStream os) throws IOException {
		os.write((int)((i >> 56) & 0xFF));
		os.write((int)((i >> 48) & 0xFF));
		os.write((int)((i >> 40) & 0xFF));
		os.write((int)((i >> 32) & 0xFF));
		os.write((int)((i >> 24) & 0xFF));
		os.write((int)((i >> 16) & 0xFF));
		os.write((int)((i >> 8) & 0xFF));
		os.write((int)(i & 0xFF));
	}

}
