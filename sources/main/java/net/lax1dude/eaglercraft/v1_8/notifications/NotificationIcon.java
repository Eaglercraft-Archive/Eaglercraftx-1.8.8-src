package net.lax1dude.eaglercraft.v1_8.notifications;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerSkinTexture;
import net.minecraft.util.ResourceLocation;

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
public class NotificationIcon {

	private static int notifIconTmpId = 0;

	protected int refCount = 0;
	protected boolean serverRegistered = true;

	public final EaglercraftUUID iconUUID;
	public final EaglerSkinTexture texture;
	public final ResourceLocation resource;

	protected NotificationIcon(EaglercraftUUID iconUUID, EaglerSkinTexture texture) {
		this.iconUUID = iconUUID;
		this.texture = texture;
		this.resource = new ResourceLocation("eagler:gui/server/notifs/tex_" + notifIconTmpId++);
	}

	public void retain() {
		++refCount;
	}

	public void release() {
		--refCount;
	}

	public boolean isValid() {
		return serverRegistered || refCount > 0;
	}

}
