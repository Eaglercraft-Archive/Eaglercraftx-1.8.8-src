/*
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ForwardRenderCallbackHandler {

	public final List<ShadersRenderPassFuture> renderPassList = new ArrayList<>(1024);

	public void push(ShadersRenderPassFuture f) {
		renderPassList.add(f);
	}

	public void reset() {
		renderPassList.clear();
	}

	public void sort(float x, float y, float z) {
		if(renderPassList.size() == 0) return;
		ShadersRenderPassFuture rp;
		float dx, dy, dz;
		for(int i = 0, l = renderPassList.size(); i < l; ++i) {
			rp = renderPassList.get(i);
			dx = rp.getX() - x;
			dy = rp.getY() - y;
			dz = rp.getZ() - z;
			rp.tmpValue()[0] = dx * dx + dy * dy + dz * dz;
		}
		Collections.sort(renderPassList, new Comparator<ShadersRenderPassFuture>() {
			@Override
			public int compare(ShadersRenderPassFuture o1, ShadersRenderPassFuture o2) {
				float a = o1.tmpValue()[0], b = o2.tmpValue()[0];
				return a < b ? 1 : (a > b ? -1 : 0);
			}
		});
	}
}