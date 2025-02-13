/*
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

package net.lax1dude.eaglercraft.v1_8.log4j;

import java.io.PrintStream;

public enum Level {
	
	TRACE(0, "TRACE", false), DEBUG(1, "DEBUG", false), INFO(2, "INFO", false),
	WARN(3, "WARN", false), ERROR(4, "ERROR", true), FATAL(5, "FATAL", true),
	OFF(Integer.MAX_VALUE, "DISABLED", false);

	public final int levelInt;
	public final String levelName;
	public final PrintStream stdout;
	public final boolean isErr;
	
	private Level(int levelInt, String levelName, boolean stderr) {
		this.levelInt = levelInt;
		this.levelName = levelName;
		this.stdout = stderr ? System.err : System.out;
		this.isErr = stderr;
	}
	
	PrintStream getPrintStream() {
		return stdout;
	}
	
}