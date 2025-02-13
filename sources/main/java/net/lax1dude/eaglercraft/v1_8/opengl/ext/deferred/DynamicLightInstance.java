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

import net.lax1dude.eaglercraft.v1_8.EagRuntime;

class DynamicLightInstance {

	public final String lightName;
	public final boolean shadow;
	long lastCacheHit = 0l;

	double posX;
	double posY;
	double posZ;
	float red;
	float green;
	float blue;
	float radius;

	public DynamicLightInstance(String lightName, boolean shadow) {
		this.lightName = lightName;
		this.shadow = shadow;
	}

	public void updateLight(double posX, double posY, double posZ, float red, float green, float blue) {
		this.lastCacheHit = EagRuntime.steadyTimeMillis();
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.radius = (float)(Math.sqrt(red + green + blue) * 3.0 + 0.5);
	}

	public void destroy() {
		
	}

	public float getRadiusInWorld() {
		return radius;
	}

}