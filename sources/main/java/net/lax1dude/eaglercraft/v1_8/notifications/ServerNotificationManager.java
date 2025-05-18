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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerSkinTexture;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeHideV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeShowV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifIconsRegisterV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifIconsReleaseV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.IChatComponent;

public class ServerNotificationManager {

	private static final Logger logger = LogManager.getLogger("ServerNotificationManager");

	private final Map<EaglercraftUUID,NotificationIcon> activeIcons = new HashMap<>();
	private final Map<EaglercraftUUID,NotificationBadge> activeNotifications = new HashMap<>();
	private List<NotificationBadge> sortedNotifList = new ArrayList<>(0);
	private List<NotificationBadge> sortedDisplayNotifList = new ArrayList<>(0);
	private int updateCounter = 0;
	private long lastCleanup = EagRuntime.steadyTimeMillis();
	private final TextureManager textureMgr;
	protected int unreadCounter = 0;

	public ServerNotificationManager(TextureManager textureMgr) {
		this.textureMgr = textureMgr;
	}

	public void processPacketAddIcons(SPacketNotifIconsRegisterV4EAG packet) {
		for(SPacketNotifIconsRegisterV4EAG.CreateIcon icn : packet.iconsToCreate) {
			if(icn.uuidMost == 0 && icn.uuidLeast == 0) {
				logger.error("Skipping notification icon with UUID 0!");
				continue;
			}
			EaglercraftUUID uuid = new EaglercraftUUID(icn.uuidMost, icn.uuidLeast);
			PacketImageData imageData = icn.imageData;
			NotificationIcon existing = activeIcons.get(uuid);
			if(existing != null) {
				if (existing.texture.getWidth() != imageData.width
						|| existing.texture.getHeight() != imageData.height) {
					logger.error("Error: server tried to change the dimensions of icon {}!", uuid);
				}else if(!Arrays.equals(existing.texture.getData(), imageData.rgba)) {
					existing.texture.copyPixelsIn(ImageData.swapRB(imageData.rgba));
				}
				existing.serverRegistered = true;
				continue;
			}
			NotificationIcon newIcon = new NotificationIcon(uuid,
					new EaglerSkinTexture(ImageData.swapRB(imageData.rgba), imageData.width, imageData.height));
			textureMgr.loadTexture(newIcon.resource, newIcon.texture);
			activeIcons.put(uuid, newIcon);
		}
	}

	public void processPacketRemIcons(SPacketNotifIconsReleaseV4EAG packet) {
		for(SPacketNotifIconsReleaseV4EAG.DestroyIcon icn : packet.iconsToDestroy) {
			NotificationIcon existing = activeIcons.get(new EaglercraftUUID(icn.uuidMost, icn.uuidLeast));
			if(existing != null) {
				existing.serverRegistered = false;
			}
		}
	}

	public void processPacketShowBadge(SPacketNotifBadgeShowV4EAG packet) {
		EaglercraftUUID newUuid = new EaglercraftUUID(packet.badgeUUIDMost, packet.badgeUUIDLeast);
		NotificationBadge existing = activeNotifications.get(newUuid);
		if(existing != null) {
			logger.error("Duplicate notification UUID {}, all notifications should have unique UUIDs!", newUuid);
			return;
		}
		NotificationBadge newBadge = new NotificationBadge(this, newUuid,
				!StringUtils.isAllBlank(packet.bodyComponent) ? IChatComponent.Serializer.jsonToComponent(packet.bodyComponent) : null,
				!StringUtils.isAllBlank(packet.titleComponent) ? IChatComponent.Serializer.jsonToComponent(packet.titleComponent) : null,
				!StringUtils.isAllBlank(packet.sourceComponent) ? IChatComponent.Serializer.jsonToComponent(packet.sourceComponent) : null,
				EagRuntime.steadyTimeMillis(), packet.originalTimestampSec * 1000l, packet.silent, packet.priority,
				getIcon(packet.mainIconUUIDMost, packet.mainIconUUIDLeast),
				getIcon(packet.titleIconUUIDMost, packet.titleIconUUIDLeast), packet.hideAfterSec, packet.expireAfterSec,
				packet.backgroundColor, packet.bodyTxtColor, packet.titleTxtColor, packet.sourceTxtColor);
		++unreadCounter;
		addNotifToActiveList(newBadge);
	}

	private NotificationIcon getIcon(long uuidMost, long uuidLeast) {
		if(uuidMost == 0l && uuidLeast == 0l) {
			return null;
		}
		return activeIcons.get(new EaglercraftUUID(uuidMost, uuidLeast));
	}

	public void processPacketHideBadge(SPacketNotifBadgeHideV4EAG packet) {
		removeNotifFromActiveList(new EaglercraftUUID(packet.badgeUUIDLeast, packet.badgeUUIDMost));
	}

	public int getNotifListUpdateCount() {
		return updateCounter;
	}

	public List<NotificationBadge> getNotifBadgesToDisplay() {
		return sortedDisplayNotifList;
	}

	public List<NotificationBadge> getNotifLongHistory() {
		return sortedNotifList;
	}

	protected void addNotifToActiveList(NotificationBadge badge) {
		NotificationBadge exists = activeNotifications.put(badge.badgeUUID, badge);
		if(exists != null) {
			exists.decrIconRefcounts();
		}
		badge.incrIconRefcounts();
		resortLists();
	}

	protected void removeNotifFromActiveList(EaglercraftUUID badge) {
		NotificationBadge exists = activeNotifications.remove(badge);
		if(exists != null) {
			exists.decrIconRefcounts();
			resortLists();
		}
	}

	protected void removeAllNotifFromActiveList(Collection<NotificationBadge> badges) {
		boolean resort = false;
		for(NotificationBadge badge : badges) {
			NotificationBadge exists = activeNotifications.remove(badge.badgeUUID);
			if(exists != null) {
				exists.decrIconRefcounts();
				resort = true;
			}
		}
		if(resort) {
			resortLists();
		}
	}

	protected static final Comparator<NotificationBadge> clientAgeComparator = (a, b) -> {
		return (int)(b.clientTimestamp - a.clientTimestamp);
	};

	private void resortLists() {
		updateCounter++;
		int ll = activeNotifications.size();
		if(!sortedNotifList.isEmpty()) sortedNotifList = new ArrayList<>(ll);
		if(!sortedDisplayNotifList.isEmpty()) sortedDisplayNotifList = new ArrayList<>(Math.min(ll, 4));
		if(ll > 0) {
			sortedNotifList.addAll(activeNotifications.values());
			Collections.sort(sortedNotifList, clientAgeComparator);
			long millis = EagRuntime.steadyTimeMillis();
			for(int i = 0, l = sortedNotifList.size(); i < l; ++i) {
				NotificationBadge bd = sortedNotifList.get(i);
				if(millis - bd.clientTimestamp < (long)(bd.hideAfterSec * 1000)) {
					sortedDisplayNotifList.add(bd);
				}else {
					bd.deleteGLTexture();
				}
			}
		}
	}

	public void runTick() {
		long millis = EagRuntime.steadyTimeMillis();
		if(millis - lastCleanup > 2500l) {
			lastCleanup = millis;
			int len = sortedNotifList.size();
			if(len > 128) {
				removeAllNotifFromActiveList(new ArrayList<NotificationBadge>(sortedNotifList.subList(128, len)));
			}
			Iterator<NotificationIcon> itr = activeIcons.values().iterator();
			while(itr.hasNext()) {
				NotificationIcon icn = itr.next();
				if(!icn.isValid()) {
					itr.remove();
					textureMgr.deleteTexture(icn.resource);
				}
			}
			if(!sortedDisplayNotifList.isEmpty()) {
				Iterator<NotificationBadge> itr2 = sortedDisplayNotifList.iterator();
				while(itr2.hasNext()) {
					NotificationBadge bd = itr2.next();
					if(bd.hideAtMillis != -1l) {
						if(millis - bd.hideAtMillis > 500l) {
							bd.deleteGLTexture();
							itr2.remove();
						}
					}else {
						long age = millis - bd.clientTimestamp;
						if(age > (long)(bd.hideAfterSec * 1000) || age > (long)(bd.expireAfterSec * 1000)) {
							bd.deleteGLTexture();
							itr2.remove();
						}
					}
				}
			}
			if(!activeNotifications.isEmpty()) {
				Iterator<NotificationBadge> itr3 = activeNotifications.values().iterator();
				List<NotificationBadge> toDelete = null;
				while(itr3.hasNext()) {
					NotificationBadge bd = itr3.next();
					long age = millis - bd.clientTimestamp;
					if(age > (long)(bd.expireAfterSec * 1000)) {
						if(toDelete == null) {
							toDelete = new ArrayList<>();
						}
						toDelete.add(bd);
					}
				}
				if(toDelete != null) {
					removeAllNotifFromActiveList(toDelete);
				}
			}
		}
	}

	public int getUnread() {
		if(unreadCounter < 0) unreadCounter = 0;
		return unreadCounter;
	}

	public void commitUnreadFlag() {
		for(NotificationBadge badge : activeNotifications.values()) {
			badge.unreadFlagRender = badge.unreadFlag;
		}
	}

	public void markRead() {
		for(NotificationBadge badge : activeNotifications.values()) {
			badge.unreadFlag = false;
			badge.unreadFlagRender = false;
		}
		unreadCounter = 0;
	}

	public void destroy() {
		for(NotificationIcon icn : activeIcons.values()) {
			textureMgr.deleteTexture(icn.resource);
		}
		activeIcons.clear();
		activeNotifications.clear();
		sortedNotifList = null;
		sortedDisplayNotifList = null;
	}

}