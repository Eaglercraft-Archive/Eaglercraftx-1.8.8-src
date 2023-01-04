package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query;

import com.google.gson.JsonObject;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
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
