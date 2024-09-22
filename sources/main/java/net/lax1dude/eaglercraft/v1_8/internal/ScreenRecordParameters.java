package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.recording.EnumScreenRecordingCodec;

/**
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
public class ScreenRecordParameters {

	public final EnumScreenRecordingCodec codec;
	public final int resolutionDivisior;
	public final int videoBitsPerSecond;
	public final int audioBitsPerSecond;
	public final int captureFrameRate;

	public ScreenRecordParameters(EnumScreenRecordingCodec codec, int resolutionDivisior, int videoBitsPerSecond,
			int audioBitsPerSecond, int captureFrameRate) {
		this.codec = codec;
		this.resolutionDivisior = resolutionDivisior;
		this.videoBitsPerSecond = videoBitsPerSecond;
		this.audioBitsPerSecond = audioBitsPerSecond;
		this.captureFrameRate = captureFrameRate;
	}

}
