package net.lax1dude.eaglercraft.v1_8.sp.server.classes;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class ContextUtil {

	public static final Logger LOGGER = LogManager.getLogger("ContextUtil");

	private static boolean GUARD_CONTEXT = false; // disable guard, for stability

	public static void enterContext() {
		if(!GUARD_CONTEXT) {
			GUARD_CONTEXT = true;
			LOGGER.info("Entered context");
		}
	}

	public static void exitContext() {
		if(GUARD_CONTEXT) {
			GUARD_CONTEXT = false;
			LOGGER.info("Exited context");
		}
	}

	public static void __checkIntegratedContextValid(String id) {
		if(GUARD_CONTEXT) {
			throw new IllegalContextAccessException("Illegal integrated server class access: " + id);
		}
	}

}
