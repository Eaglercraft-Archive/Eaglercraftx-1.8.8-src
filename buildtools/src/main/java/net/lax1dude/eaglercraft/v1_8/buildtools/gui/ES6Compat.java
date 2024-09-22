package net.lax1dude.eaglercraft.v1_8.buildtools.gui;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

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
public class ES6Compat {

	/**
	 * TODO: remove this when we update TeaVM to 0.10+ (ES6 is impossible)
	 */
	public static boolean patchClassesJS(File classesJS, File shimJS) {
		try {
			String dest = FileUtils.readFileToString(classesJS, StandardCharsets.UTF_8);
			int i = dest.substring(0, dest.indexOf("=$rt_globals.Symbol('jsoClass');")).lastIndexOf("let ");
			dest = dest.substring(0, i) + "var" + dest.substring(i + 3);
			int j = dest.indexOf("function($rt_globals,$rt_exports){");
			dest = dest.substring(0, j + 34) + "\n" + FileUtils.readFileToString(shimJS, StandardCharsets.UTF_8) + "\n" + dest.substring(j + 34);
			FileUtils.writeStringToFile(classesJS, dest, StandardCharsets.UTF_8);
			return true;
		}catch(Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

}
