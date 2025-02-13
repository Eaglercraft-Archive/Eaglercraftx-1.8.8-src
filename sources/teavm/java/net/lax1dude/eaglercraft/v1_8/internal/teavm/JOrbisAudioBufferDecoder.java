/*
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.teavm.jso.webaudio.AudioBuffer;
import org.teavm.jso.webaudio.AudioContext;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAudio;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.EaglerArrayBufferAllocator;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

public class JOrbisAudioBufferDecoder {

	private EaglerInputStream inputStream;
	private boolean endOfStream = false;
	private byte[] buffer = null;
	private int bufferSize;
	private int count = 0;
	private int index = 0;
	private float[][] convertedBuffer = null;
	private float[][][] pcmInfo;
	private int[] pcmIndex;
	private Packet joggPacket = new Packet();
	private Page joggPage = new Page();
	private StreamState joggStreamState = new StreamState();
	private SyncState joggSyncState = new SyncState();
	private DspState jorbisDspState = new DspState();
	private Block jorbisBlock;
	private Comment jorbisComment;
	private Info jorbisInfo;
	private String errorString;

	private static final Logger logger = LogManager.getLogger("JOrbisAudioBufferDecoder");

	private static final JOrbisAudioBufferDecoder instance = new JOrbisAudioBufferDecoder();

	public static final int LOAD_VIA_AUDIOBUFFER = 0;
	public static final int LOAD_VIA_WAV32F = 1;
	public static final int LOAD_VIA_WAV16 = 2;

	public static AudioBuffer decodeAudioJOrbis(AudioContext ctx, byte[] data, String errorString, int loadVia) {
		JOrbisAudioBufferDecoder dec = instance;
		synchronized(dec) {
			if(!dec.init(data, errorString)) {
				logger.error("[{}]: Invalid header detected", errorString);
				return null;
			}
			int ch = -1;
			int len = 0;
			List<float[][]> lst = new LinkedList<>();
			float[][] b;
			while((b = dec.readBytes()) != null) {
				if(ch == -1) {
					ch = b.length;
				}
				len += b[0].length;
				lst.add(b);
			}
			if(dec.jorbisInfo.channels != ch) {
				logger.warn("[{}]: Number of channels in header does not match the stream", errorString);
			}
			if(ch == -1 || len == 0) {
				logger.error("[{}]: Empty file", errorString);
				return null;
			}
			switch(loadVia) {
			case LOAD_VIA_AUDIOBUFFER: {
				AudioBuffer buffer = ctx.createBuffer(ch, len, dec.jorbisInfo.rate);
				int len2 = 0;
				for(float[][] fl : lst) {
					for(int i = 0; i < ch; ++i) {
						buffer.copyToChannel(TeaVMUtils.unwrapFloatArray(fl[i]), i, len2);
					}
					len2 += fl[0].length;
				}
				return buffer;
			}
			case LOAD_VIA_WAV32F: {
				int len2 = PCMToWAVLoader.getWAVLen(lst, true);
				if(len2 == 0 || len2 == 44) {
					logger.error("[{}]: Invalid length for WAV calculated", errorString);
					return null;
				}
				ByteBuffer buf = PlatformRuntime.allocateByteBuffer(len2);
				try {
					PCMToWAVLoader.createWAV32F(lst, ch, dec.jorbisInfo.rate, buf);
					buf.flip();
					return PlatformAudio.decodeAudioBrowserAsync(
							EaglerArrayBufferAllocator.getDataView8(buf).getBuffer(), errorString + ".wav");
				}finally {
					PlatformRuntime.freeByteBuffer(buf);
				}
			}
			case LOAD_VIA_WAV16: {
				int len2 = PCMToWAVLoader.getWAVLen(lst, false);
				if(len2 == 0 || len2 == 44) {
					logger.error("[{}]: Invalid length for WAV calculated", errorString);
					return null;
				}
				ByteBuffer buf = PlatformRuntime.allocateByteBuffer(len2);
				try {
					PCMToWAVLoader.createWAV16(lst, ch, dec.jorbisInfo.rate, buf);
					buf.flip();
					return PlatformAudio.decodeAudioBrowserAsync(
							EaglerArrayBufferAllocator.getDataView8(buf).getBuffer(), errorString + ".wav");
				}finally {
					PlatformRuntime.freeByteBuffer(buf);
				}
			}
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	private JOrbisAudioBufferDecoder() {
		this.jorbisBlock = new Block(this.jorbisDspState);
		this.jorbisComment = new Comment();
		this.jorbisInfo = new Info();
	}

	private boolean init(byte[] data, String errorString) {
		this.inputStream = new EaglerInputStream(data);
		this.errorString = errorString;

		if (this.joggStreamState != null) {
			this.joggStreamState.clear();
		}

		if (this.jorbisBlock != null) {
			this.jorbisBlock.clear();
		}

		if (this.jorbisDspState != null) {
			this.jorbisDspState.clear();
		}

		if (this.jorbisInfo != null) {
			this.jorbisInfo.clear();
		}

		if (this.joggSyncState != null) {
			this.joggSyncState.clear();
		}

		if (this.inputStream != null) {
			try {
				this.inputStream.close();
			} catch (IOException var7) {
			}
		}

		this.bufferSize = 8192;
		this.buffer = null;
		this.count = 0;
		this.index = 0;
		this.joggStreamState = new StreamState();
		this.jorbisBlock = new Block(this.jorbisDspState);
		this.jorbisDspState = new DspState();
		this.jorbisInfo = new Info();
		this.joggSyncState = new SyncState();
		
		this.endOfStream = false;
		this.joggSyncState.init();
		this.joggSyncState.buffer(this.bufferSize);
		this.buffer = this.joggSyncState.data;
		
		vigg: {
			this.index = this.joggSyncState.buffer(this.bufferSize);
			int bytes = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
			if (bytes < 0) {
				bytes = 0;
			}
	
			this.joggSyncState.wrote(bytes);
			if (this.joggSyncState.pageout(this.joggPage) != 1) {
				if (bytes < this.bufferSize) {
					break vigg;
				} else {
					logger.error("[{}]: Ogg header not recognized in method 'readHeader'.", errorString);
					return false;
				}
			} else {
				this.joggStreamState.init(this.joggPage.serialno());
				this.jorbisInfo.init();
				this.jorbisComment.init();
				if (this.joggStreamState.pagein(this.joggPage) < 0) {
					logger.error("[{}]: Problem with first Ogg header page in method 'readHeader'.", errorString);
					return false;
				} else if (this.joggStreamState.packetout(this.joggPacket) != 1) {
					logger.error("[{}]: Problem with first Ogg header packet in method 'readHeader'.", errorString);
					return false;
				} else if (this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket) < 0) {
					logger.error("[{}]: File does not contain Vorbis header in method 'readHeader'.", errorString);
					return false;
				} else {
					int i = 0;
	
					while (i < 2) {
						label73: while (true) {
							int result;
							do {
								if (i >= 2) {
									break label73;
								}
	
								result = this.joggSyncState.pageout(this.joggPage);
								if (result == 0) {
									break label73;
								}
							} while (result != 1);
	
							this.joggStreamState.pagein(this.joggPage);
	
							while (i < 2) {
								result = this.joggStreamState.packetout(this.joggPacket);
								if (result == 0) {
									break;
								}
	
								if (result == -1) {
									logger.error("[{}]: Secondary Ogg header corrupt in method 'readHeader'.", errorString);
									return false;
								}
	
								this.jorbisInfo.synthesis_headerin(this.jorbisComment, this.joggPacket);
								++i;
							}
						}
	
						this.index = this.joggSyncState.buffer(this.bufferSize);
						bytes = this.inputStream.read(this.joggSyncState.data, this.index, this.bufferSize);
						if (bytes < 0) {
							bytes = 0;
						}
	
						if (bytes == 0 && i < 2) {
							logger.error(
									"[{}]: End of file reached before finished reading Ogg header in method 'readHeader'",
									errorString);
							return false;
						}
	
						this.joggSyncState.wrote(bytes);
					}
	
					this.index = this.joggSyncState.buffer(this.bufferSize);
					this.buffer = this.joggSyncState.data;
				}
			}
		}

		this.jorbisDspState.synthesis_init(this.jorbisInfo);
		this.jorbisBlock.init(this.jorbisDspState);
		int channels = this.jorbisInfo.channels;
		int rate = this.jorbisInfo.rate;
		this.pcmInfo = new float[1][][];
		this.pcmIndex = new int[channels];
		if(convertedBuffer == null || convertedBuffer.length != this.jorbisInfo.channels || (convertedBuffer.length > 0 && convertedBuffer[0].length != this.bufferSize)) {
			this.convertedBuffer = new float[this.jorbisInfo.channels][this.bufferSize];
		}
		
		return true;
	}

	private float[][] readBytes() {
		if (this.endOfStream) {
			return null;
		} else {
			float[][] returnBuffer = null;
			switch (this.joggSyncState.pageout(this.joggPage)) {
			default:
				this.joggStreamState.pagein(this.joggPage);
				if (this.joggPage.granulepos() == 0L) {
					this.endOfStream = true;
					return null;
				} else {
					label99: {
						while (true) {
							switch (this.joggStreamState.packetout(this.joggPacket)) {
							case -1:
								break;
							case 0:
								if (this.joggPage.eos() != 0) {
									this.endOfStream = true;
								}
								break label99;
							default:
								if (this.jorbisBlock.synthesis(this.joggPacket) == 0) {
									this.jorbisDspState.synthesis_blockin(this.jorbisBlock);
								}

								int samples;
								while ((samples = this.jorbisDspState.synthesis_pcmout(this.pcmInfo,
										this.pcmIndex)) > 0) {
									float[][] pcmf = this.pcmInfo[0];
									int bout = samples < bufferSize ? samples : this.bufferSize;

									for (int i = 0; i < this.jorbisInfo.channels; ++i) {
										float[] f1 = convertedBuffer[i];
										float[] f2 = pcmf[i];
										int mono = this.pcmIndex[i];
										for (int j = 0; j < bout; ++j) {
											f1[j] = f2[mono + j];
										}
									}

									this.jorbisDspState.synthesis_read(bout);
									returnBuffer = appendFloatArrays(returnBuffer, this.convertedBuffer, bout);
								}
							}
						}
					}
				}
			case -1:
			case 0:
				if (!this.endOfStream) {
					this.index = this.joggSyncState.buffer(this.bufferSize);
					this.buffer = this.joggSyncState.data;

					try {
						this.count = this.inputStream.read(this.buffer, this.index, this.bufferSize);
					} catch (Exception var11) {
						return null;
					}

					if (this.count == -1) {
						return returnBuffer;
					}

					this.joggSyncState.wrote(this.count);
					if (this.count == 0) {
						this.endOfStream = true;
					}
				}

				return returnBuffer;
			}
		}
	}

	private static float[][] appendFloatArrays(float[][] arrayOne, float[][] arrayTwo, int arrayTwoBytes) {
		int bytes = arrayTwoBytes;
		int l;
		if (arrayTwo != null && (l = arrayTwo[0].length) != 0) {
			if (l < arrayTwoBytes) {
				bytes = l;
			}
		} else {
			bytes = 0;
		}

		if ((arrayOne != null || arrayTwo != null) && bytes > 0) {
			float[][] newArray;
			
			if (arrayOne == null) {
				int ch = arrayTwo.length;
				int len1 = arrayTwo[0].length;
				newArray = new float[ch][bytes];
				for(int i = 0; i < ch; ++i) {
					System.arraycopy(arrayTwo[i], 0, newArray[i], 0, bytes);
				}
				arrayTwo = null;
			} else {
				int ch = arrayOne.length;
				int len1 = arrayOne[0].length;
				if (arrayTwo != null && bytes > 0) {
					newArray = new float[ch][len1 + bytes];
					for(int i = 0; i < ch; ++i) {
						System.arraycopy(arrayOne[i], 0, newArray[i], 0, len1);
						System.arraycopy(arrayTwo[i], 0, newArray[i], len1, bytes);
					}
					arrayOne = null;
					arrayTwo = null;
				} else {
					newArray = new float[ch][len1];
					for(int i = 0; i < ch; ++i) {
						System.arraycopy(arrayOne[i], 0, newArray[i], 0, len1);
					}
					arrayOne = null;
				}
			}

			return newArray;
		} else {
			return null;
		}
	}

}