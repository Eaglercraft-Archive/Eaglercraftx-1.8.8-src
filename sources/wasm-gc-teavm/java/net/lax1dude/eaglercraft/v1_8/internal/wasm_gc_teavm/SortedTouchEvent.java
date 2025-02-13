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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArrayReader;

import net.lax1dude.eaglercraft.v1_8.internal.EnumTouchEvent;

public class SortedTouchEvent {

	public interface ITouchUIDMapper {
		int call(int uidIn);
	}

	public interface JSTouchPoint extends JSObject {

		@JSProperty
		int getPointX();

		@JSProperty
		int getPointY();

		@JSProperty
		float getRadius();

		@JSProperty
		float getForce();

		@JSProperty
		int getPointUID();

	}

	public static class TouchPoint {

		public final int pointX;
		public final int pointY;
		public final float radius;
		public final float force;
		public final int uid;

		public TouchPoint(int pointX, int pointY, float radius, float force, int uid) {
			this.pointX = pointX;
			this.pointY = pointY;
			this.radius = radius;
			this.force = force;
			this.uid = uid;
		}

	}

	public static final Comparator<TouchPoint> touchSortingComparator = (t1, t2) -> {
		return t1.uid - t2.uid;
	};

	private static List<TouchPoint> convertTouchList(JSArrayReader<JSTouchPoint> jsArray, ITouchUIDMapper uidMapper,
			int windowHeight, float windowDPI) {
		int l = jsArray.getLength();
		List<TouchPoint> ret = new ArrayList<>(l);
		for(int i = 0; i < l; ++i) {
			JSTouchPoint p = jsArray.get(i);
			ret.add(new TouchPoint((int)(p.getPointX() * windowDPI), windowHeight - (int)(p.getPointY() * windowDPI) - 1,
					p.getRadius() * windowDPI, p.getForce(), uidMapper.call(p.getPointUID())));
		}
		Collections.sort(ret, touchSortingComparator);
		return ret;
	}

	public static SortedTouchEvent createTouchEvent(EnumTouchEvent type, JSArrayReader<JSTouchPoint> changedTouches,
			JSArrayReader<JSTouchPoint> targetTouches, ITouchUIDMapper uidMapper, int windowHeight, float windowDPI) {
		List<TouchPoint> changedTouchesList = convertTouchList(changedTouches, uidMapper, windowHeight, windowDPI);
		List<TouchPoint> targetTouchesList = convertTouchList(targetTouches, uidMapper, windowHeight, windowDPI);
		List<TouchPoint> eventTouchesList;
		switch(type) {
		case TOUCHSTART:
			eventTouchesList = changedTouchesList;
			break;
		case TOUCHMOVE:
			eventTouchesList = targetTouchesList;
			break;
		case TOUCHEND:
		default:
			eventTouchesList = changedTouchesList;
			break;
		}
		return new SortedTouchEvent(type, changedTouchesList, targetTouchesList, eventTouchesList);
	}

	public final EnumTouchEvent type;
	private final List<TouchPoint> changedTouchesList;
	private final List<TouchPoint> targetTouchesList;
	private final List<TouchPoint> eventTouchesList;
	
	public SortedTouchEvent(EnumTouchEvent type, List<TouchPoint> changedTouchesList,
			List<TouchPoint> targetTouchesList, List<TouchPoint> eventTouchesList) {
		this.type = type;
		this.changedTouchesList = changedTouchesList;
		this.targetTouchesList = targetTouchesList;
		this.eventTouchesList = eventTouchesList;
	}

	public int getChangedTouchesSize() {
		return changedTouchesList.size();
	}

	public List<TouchPoint> getChangedTouches() {
		return changedTouchesList;
	}

	public int getTargetTouchesSize() {
		return targetTouchesList.size();
	}

	public List<TouchPoint> getTargetTouches() {
		return targetTouchesList;
	}

	public int getEventTouchesSize() {
		return eventTouchesList.size();
	}

	public List<TouchPoint> getEventTouches() {
		return eventTouchesList;
	}

}