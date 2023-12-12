package net.lax1dude.eaglercraft.v1_8.internal.vfs;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.AbstractResourcePack;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class FolderResourcePack extends AbstractResourcePack {
	private final String prefix;

	public FolderResourcePack(String resourcePackFileIn, String prefix) {
		super(resourcePackFileIn);
		this.prefix = prefix;
	}

	protected InputStream getInputStreamByName(String name) {
		return SYS.VFS.getFile(prefix + this.resourcePackFile + "/" + name).getInputStream();
	}

	protected boolean hasResourceName(String name) {
		return SYS.VFS.fileExists(prefix + this.resourcePackFile + "/" + name);
	}

	public Set<String> getResourceDomains() {
		Set<String> set = Sets.<String>newHashSet();
		String pfx = prefix + this.resourcePackFile + "/assets/";
		List<String> files = SYS.VFS.listFiles(pfx);

		for (String file : files) {
			String s = file.substring(pfx.length());
			int ind = s.indexOf('/');
			if (ind != -1) s = s.substring(0, ind);
			if (!s.equals(s.toLowerCase())) {
				this.logNameNotLowercase(s);
			} else {
				set.add(s);
			}
		}

		return set;
	}
}