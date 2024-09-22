package net.lax1dude.eaglercraft.v1_8.notifications;

import java.util.List;

import net.minecraft.util.IChatComponent;

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
public class CachedNotifBadgeTexture {

	public final int glTexture;
	public final int scaleFactor;
	public final int width;
	public final int height;
	public final List<ClickEventZone> cursorEvents;
	public final IChatComponent rootClickEvent;
	public final boolean hasClickEvents;
	public final boolean hasHoverEvents;

	protected CachedNotifBadgeTexture(int glTexture, int scaleFactor, int width, int height,
			List<ClickEventZone> cursorEvents, IChatComponent rootClickEvent, boolean hasClickEvents,
			boolean hasHoverEvents) {
		this.glTexture = glTexture;
		this.scaleFactor = scaleFactor;
		this.width = width;
		this.height = height;
		this.cursorEvents = cursorEvents;
		this.rootClickEvent = rootClickEvent;
		this.hasClickEvents = hasClickEvents;
		this.hasHoverEvents = hasHoverEvents;
	}

}
