package net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util;

import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.CPacketRPCNotifBadgeShow;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.CPacketRPCNotifBadgeShow.EnumBadgePriority;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

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
public class NotificationBadgeBuilder {

	public static enum BadgePriority {
		LOW, NORMAL, HIGHER, HIGHEST;
	}

	private UUID badgeUUID = null;
	private BaseComponent bodyComponent = null;
	private BaseComponent titleComponent = null;
	private BaseComponent sourceComponent = null;
	private long originalTimestampSec = 0l;
	private boolean silent = false;
	private BadgePriority priority = BadgePriority.NORMAL;
	private UUID mainIconUUID = null;
	private UUID titleIconUUID = null;
	private int hideAfterSec = 10;
	private int expireAfterSec = 3600;
	private int backgroundColor = 0xFFFFFF;
	private int bodyTxtColor = 0xFFFFFF;
	private int titleTxtColor = 0xFFFFFF;
	private int sourceTxtColor = 0xFFFFFF;

	private CPacketRPCNotifBadgeShow packetCache = null;
	private boolean packetDirty = true;

	public NotificationBadgeBuilder() {
		originalTimestampSec = System.currentTimeMillis() / 1000l;
	}

	public NotificationBadgeBuilder(NotificationBadgeBuilder builder) {
		badgeUUID = builder.badgeUUID;
		bodyComponent = builder.bodyComponent;
		titleComponent = builder.titleComponent;
		sourceComponent = builder.sourceComponent;
		originalTimestampSec = builder.originalTimestampSec;
		silent = builder.silent;
		priority = builder.priority;
		mainIconUUID = builder.mainIconUUID;
		titleIconUUID = builder.titleIconUUID;
		hideAfterSec = builder.hideAfterSec;
		backgroundColor = builder.backgroundColor;
		bodyTxtColor = builder.bodyTxtColor;
		titleTxtColor = builder.titleTxtColor;
		sourceTxtColor = builder.sourceTxtColor;
		packetCache = !builder.packetDirty ? builder.packetCache : null;
		packetDirty = builder.packetDirty;
	}

	private static BaseComponent fixParsedComponent(BaseComponent[] comps) {
		if(comps.length == 0) {
			return null;
		}else if(comps.length == 1) {
			return comps[0];
		}else {
			TextComponent cmp = new TextComponent("");
			for(int i = 0; i < comps.length; ++i) {
				cmp.addExtra(comps[i]);
			}
			return cmp;
		}
	}

	public NotificationBadgeBuilder(CPacketRPCNotifBadgeShow packet) {
		badgeUUID = packet.badgeUUID;
		try {
			bodyComponent = fixParsedComponent(ComponentSerializer.parse(packet.bodyComponent));
		}catch(Throwable t) {
			bodyComponent = new TextComponent(packet.bodyComponent);
		}
		try {
			titleComponent = fixParsedComponent(ComponentSerializer.parse(packet.titleComponent));
		}catch(Throwable t) {
			titleComponent = new TextComponent(packet.titleComponent);
		}
		try {
			sourceComponent = fixParsedComponent(ComponentSerializer.parse(packet.sourceComponent));
		}catch(Throwable t) {
			sourceComponent = new TextComponent(packet.sourceComponent);
		}
		originalTimestampSec = packet.originalTimestampSec;
		silent = packet.silent;
		switch(packet.priority) {
		case LOW:
		default:
			priority = BadgePriority.LOW;
			break;
		case NORMAL:
			priority = BadgePriority.NORMAL;
			break;
		case HIGHER:
			priority = BadgePriority.HIGHER;
			break;
		case HIGHEST:
			priority = BadgePriority.HIGHEST;
			break;
		}
		mainIconUUID = packet.mainIconUUID;
		titleIconUUID = packet.titleIconUUID;
		hideAfterSec = packet.hideAfterSec;
		backgroundColor = packet.backgroundColor;
		bodyTxtColor = packet.bodyTxtColor;
		titleTxtColor = packet.titleTxtColor;
		sourceTxtColor = packet.sourceTxtColor;
		packetCache = packet;
		packetDirty = false;
	}

	public UUID getBadgeUUID() {
		return badgeUUID;
	}

	public NotificationBadgeBuilder setBadgeUUID(UUID badgeUUID) {
		this.badgeUUID = badgeUUID;
		this.packetDirty = true;
		return this;
	}

	public NotificationBadgeBuilder setBadgeUUIDRandom() {
		this.badgeUUID = UUID.randomUUID();
		this.packetDirty = true;
		return this;
	}

	public BaseComponent getBodyComponent() {
		return bodyComponent;
	}

	public NotificationBadgeBuilder setBodyComponent(BaseComponent bodyComponent) {
		this.bodyComponent = bodyComponent;
		this.packetDirty = true;
		return this;
	}

	public NotificationBadgeBuilder setBodyComponent(String bodyText) {
		this.bodyComponent = new TextComponent(bodyText);
		this.packetDirty = true;
		return this;
	}

	public BaseComponent getTitleComponent() {
		return titleComponent;
	}

	public NotificationBadgeBuilder setTitleComponent(BaseComponent titleComponent) {
		this.titleComponent = titleComponent;
		this.packetDirty = true;
		return this;
	}

	public NotificationBadgeBuilder setTitleComponent(String titleText) {
		this.titleComponent = new TextComponent(titleText);
		this.packetDirty = true;
		return this;
	}

	public BaseComponent getSourceComponent() {
		return sourceComponent;
	}

	public NotificationBadgeBuilder setSourceComponent(BaseComponent sourceComponent) {
		this.sourceComponent = sourceComponent;
		this.packetDirty = true;
		return this;
	}

	public NotificationBadgeBuilder setSourceComponent(String sourceText) {
		this.sourceComponent = new TextComponent(sourceText);
		this.packetDirty = true;
		return this;
	}

	public long getOriginalTimestampSec() {
		return originalTimestampSec;
	}

	public NotificationBadgeBuilder setOriginalTimestampSec(long originalTimestampSec) {
		this.originalTimestampSec = originalTimestampSec;
		this.packetDirty = true;
		return this;
	}

	public boolean isSilent() {
		return silent;
	}

	public NotificationBadgeBuilder setSilent(boolean silent) {
		this.silent = silent;
		this.packetDirty = true;
		return this;
	}

	public BadgePriority getPriority() {
		return priority;
	}

	public NotificationBadgeBuilder setPriority(BadgePriority priority) {
		this.priority = priority;
		this.packetDirty = true;
		return this;
	}

	public UUID getMainIconUUID() {
		return mainIconUUID;
	}

	public NotificationBadgeBuilder setMainIconUUID(UUID mainIconUUID) {
		this.mainIconUUID = mainIconUUID;
		this.packetDirty = true;
		return this;
	}

	public UUID getTitleIconUUID() {
		return titleIconUUID;
	}

	public NotificationBadgeBuilder setTitleIconUUID(UUID titleIconUUID) {
		this.titleIconUUID = titleIconUUID;
		this.packetDirty = true;
		return this;
	}

	public int getHideAfterSec() {
		return hideAfterSec;
	}

	public NotificationBadgeBuilder setHideAfterSec(int hideAfterSec) {
		this.hideAfterSec = hideAfterSec;
		this.packetDirty = true;
		return this;
	}

	public int getExpireAfterSec() {
		return expireAfterSec;
	}

	public NotificationBadgeBuilder setExpireAfterSec(int expireAfterSec) {
		this.expireAfterSec = expireAfterSec;
		this.packetDirty = true;
		return this;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public NotificationBadgeBuilder setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.packetDirty = true;
		return this;
	}

	public int getBodyTxtColorRGB() {
		return bodyTxtColor;
	}

	public NotificationBadgeBuilder setBodyTxtColorRGB(int colorRGB) {
		this.bodyTxtColor = colorRGB;
		this.packetDirty = true;
		return this;
	}

	public NotificationBadgeBuilder setBodyTxtColorRGB(int colorR, int colorG, int colorB) {
		this.bodyTxtColor = (colorR << 16) | (colorG << 8) | colorB;
		this.packetDirty = true;
		return this;
	}

	public int getTitleTxtColorRGB() {
		return titleTxtColor;
	}

	public NotificationBadgeBuilder setTitleTxtColorRGB(int colorRGB) {
		this.titleTxtColor = colorRGB;
		this.packetDirty = true;
		return this;
	}

	public NotificationBadgeBuilder setTitleTxtColorRGB(int colorR, int colorG, int colorB) {
		this.titleTxtColor = (colorR << 16) | (colorG << 8) | colorB;
		this.packetDirty = true;
		return this;
	}

	public int getSourceTxtColorRGB() {
		return sourceTxtColor;
	}

	public NotificationBadgeBuilder setSourceTxtColorRGB(int colorRGB) {
		this.sourceTxtColor = colorRGB;
		this.packetDirty = true;
		return this;
	}

	public NotificationBadgeBuilder setSourceTxtColorRGB(int colorR, int colorG, int colorB) {
		this.sourceTxtColor = (colorR << 16) | (colorG << 8) | colorB;
		this.packetDirty = true;
		return this;
	}

	public Object clone() {
		return new NotificationBadgeBuilder(this);
	}

	public CPacketRPCNotifBadgeShow buildPacket() {
		if(packetDirty || packetCache == null) {
			if(badgeUUID == null) {
				badgeUUID = UUID.randomUUID();
			}else if(badgeUUID.getMostSignificantBits() == 0l && badgeUUID.getLeastSignificantBits() == 0l) {
				throw new IllegalStateException("Badge UUID cannot be 0!");
			}
			EnumBadgePriority internalPriority;
			switch(priority) {
			case LOW:
			default:
				internalPriority = EnumBadgePriority.LOW;
				break;
			case NORMAL:
				internalPriority = EnumBadgePriority.NORMAL;
				break;
			case HIGHER:
				internalPriority = EnumBadgePriority.HIGHER;
				break;
			case HIGHEST:
				internalPriority = EnumBadgePriority.HIGHEST;
				break;
			}
			String bodyComp = bodyComponent != null ? ComponentSerializer.toString(bodyComponent) : "";
			if(bodyComp.length() > 32767) {
				throw new IllegalStateException("Body component is longer than 32767 chars serialized!");
			}
			String titleComp = titleComponent != null ? ComponentSerializer.toString(titleComponent) : "";
			if(titleComp.length() > 255) {
				throw new IllegalStateException("Title component is longer than 255 chars serialized!");
			}
			String sourceComp = sourceComponent != null ? ComponentSerializer.toString(sourceComponent) : "";
			if(sourceComp.length() > 255) {
				throw new IllegalStateException("Body component is longer than 255 chars serialized!");
			}
			packetCache = new CPacketRPCNotifBadgeShow(badgeUUID, bodyComp, titleComp, sourceComp, originalTimestampSec,
					mainIconUUID, titleIconUUID, silent, internalPriority, hideAfterSec, expireAfterSec,
					backgroundColor, bodyTxtColor, titleTxtColor, sourceTxtColor);
			packetDirty = false;
		}
		return packetCache;
	}

}
