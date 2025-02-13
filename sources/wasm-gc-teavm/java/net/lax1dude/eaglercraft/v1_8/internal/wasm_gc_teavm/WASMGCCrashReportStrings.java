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

package net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm;

import org.teavm.interop.Import;
import org.teavm.jso.core.JSString;

import net.lax1dude.eaglercraft.v1_8.EaglercraftVersion;

public class WASMGCCrashReportStrings {

	private static final int CRASH_REPORT_EAGLER_VERSION = 0;
	private static final int CRASH_REPORT_EAGLER_VENDOR = 1;
	private static final int CRASH_REPORT_MINECRAFT_VERSION = 2;

	@Import(module = "platformRuntime", name = "setCrashReportString")
	private static native void setCrashReportString(int id, JSString str);

	public static void setCrashReportStrings() {
		setCrashReportString(CRASH_REPORT_EAGLER_VERSION, JSString.valueOf(EaglercraftVersion.projectForkVersion));
		setCrashReportString(CRASH_REPORT_EAGLER_VENDOR, JSString.valueOf(EaglercraftVersion.projectForkVendor));
		setCrashReportString(CRASH_REPORT_MINECRAFT_VERSION, JSString.valueOf("1.8.8"));
	}

}