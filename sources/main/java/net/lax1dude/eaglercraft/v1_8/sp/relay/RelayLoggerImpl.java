package net.lax1dude.eaglercraft.v1_8.sp.relay;

import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.IRelayLogger;

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
public class RelayLoggerImpl implements IRelayLogger {

	private final Logger impl;

	public RelayLoggerImpl(Logger impl) {
		this.impl = impl;
	}

	@Override
	public void debug(String msg, Object... args) {
		impl.debug(msg, args);
	}

	@Override
	public void info(String msg, Object... args) {
		impl.debug(msg, args);
	}

	@Override
	public void warn(String msg, Object... args) {
		impl.warn(msg, args);
	}

	@Override
	public void error(String msg, Object... args) {
		impl.error(msg, args);
	}

	@Override
	public void error(Throwable th) {
		impl.error(th);
	}

}
