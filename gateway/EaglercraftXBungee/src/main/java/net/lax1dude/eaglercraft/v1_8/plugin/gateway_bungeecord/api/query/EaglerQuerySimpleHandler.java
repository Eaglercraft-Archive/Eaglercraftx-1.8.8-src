/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query;

import com.google.gson.JsonObject;

public abstract class EaglerQuerySimpleHandler extends EaglerQueryHandler {

	@Override
	protected void processString(String str) {
		throw new UnexpectedDataException();
	}

	@Override
	protected void processJson(JsonObject obj) {
		throw new UnexpectedDataException();
	}

	@Override
	protected void processBytes(byte[] bytes) {
		throw new UnexpectedDataException();
	}

	@Override
	protected void acceptText() {
		throw new UnsupportedOperationException("EaglerQuerySimpleHandler does not support duplex");
	}

	@Override
	protected void acceptText(boolean bool) {
		throw new UnsupportedOperationException("EaglerQuerySimpleHandler does not support duplex");
	}

	@Override
	protected void acceptBinary() {
		throw new UnsupportedOperationException("EaglerQuerySimpleHandler does not support duplex");
	}

	@Override
	protected void acceptBinary(boolean bool) {
		throw new UnsupportedOperationException("EaglerQuerySimpleHandler does not support duplex");
	}

	@Override
	protected void closed() {
	}

}