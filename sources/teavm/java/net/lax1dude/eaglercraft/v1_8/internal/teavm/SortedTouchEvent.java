package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.EnumTouchEvent;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

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
public class SortedTouchEvent {

	public static interface ITouchUIDMapper {
		int call(int uidIn);
	}

	public final TouchEvent event;
	public final EnumTouchEvent type;
	private final List<OffsetTouch> targetTouches;
	private final List<OffsetTouch> changedTouches;
	private final List<OffsetTouch> eventTouches;

	public SortedTouchEvent(TouchEvent event, ITouchUIDMapper mapper) {
		changedTouches = TeaVMUtils.toSortedTouchList(event.getChangedTouches(), mapper, PlatformInput.touchOffsetXTeaVM, PlatformInput.touchOffsetYTeaVM);
		targetTouches = TeaVMUtils.toSortedTouchList(event.getTargetTouches(), mapper, PlatformInput.touchOffsetXTeaVM, PlatformInput.touchOffsetYTeaVM);
		this.event = event;
		switch(event.getType()) {
		case "touchstart":
			type = EnumTouchEvent.TOUCHSTART;
			eventTouches = changedTouches;
			break;
		case "touchmove":
			type = EnumTouchEvent.TOUCHMOVE;
			eventTouches = targetTouches;
			break;
		case "touchend":
		case "touchcancel":
		default:
			type = EnumTouchEvent.TOUCHEND;
			eventTouches = changedTouches;
			break;
		}
	}

	public int getTouchesSize() {
		return event.getTouches().getLength();
	}

	public int getChangedTouchesSize() {
		return event.getChangedTouches().getLength();
	}

	public List<OffsetTouch> getChangedTouches() {
		return changedTouches;
	}

	public int getTargetTouchesSize() {
		return event.getTargetTouches().getLength();
	}

	public List<OffsetTouch> getTargetTouches() {
		return targetTouches;
	}

	public int getEventTouchesSize() {
		return eventTouches.size();
	}

	public List<OffsetTouch> getEventTouches() {
		return eventTouches;
	}

}
