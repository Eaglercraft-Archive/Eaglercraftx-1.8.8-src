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

package net.lax1dude.eaglercraft.v1_8.notifications;

import net.minecraft.util.IChatComponent;

public class ClickEventZone {

	public final int posX;
	public final int posY;
	public final int width;
	public final int height;
	public final IChatComponent chatComponent;
	public final boolean hasHoverEvent;
	public final boolean hasClickEvent;

	public ClickEventZone(int posX, int posY, int width, int height, IChatComponent chatComponent,
			boolean hasHoverEvent, boolean hasClickEvent) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.chatComponent = chatComponent;
		this.hasHoverEvent = hasHoverEvent;
		this.hasClickEvent = hasClickEvent;
	}

}