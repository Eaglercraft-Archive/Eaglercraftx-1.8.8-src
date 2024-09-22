package net.lax1dude.eaglercraft.v1_8.internal;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public enum EnumPlatformANGLE {

	DEFAULT(225281 /* GLFW_ANGLE_PLATFORM_TYPE_NONE */, "default", "Default"),
	D3D9(225284 /* GLFW_ANGLE_PLATFORM_TYPE_D3D9 */, "d3d9", "Direct3D9"),
	D3D11(225285 /* GLFW_ANGLE_PLATFORM_TYPE_D3D11 */, "d3d11", "Direct3D11"),
	OPENGL(225282 /* GLFW_ANGLE_PLATFORM_TYPE_OPENGL */, "opengl", "OpenGL"),
	OPENGLES(225283 /* GLFW_ANGLE_PLATFORM_TYPE_OPENGLES */, "opengles", "OpenGL ES"),
	METAL(225288 /* GLFW_ANGLE_PLATFORM_TYPE_METAL */, "metal", "Metal"),
	VULKAN(225287 /* GLFW_ANGLE_PLATFORM_TYPE_VULKAN */, "vulkan", "Vulkan");
	
	public final int eglEnum;
	public final String id;
	public final String name;

	private EnumPlatformANGLE(int eglEnum, String id, String name) {
		this.eglEnum = eglEnum;
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return id;
	}
	
	public static EnumPlatformANGLE fromId(String id) {
		if(id.equals("d3d11") || id.equals("d3d") || id.equals("dx11")) {
			return D3D11;
		}else if(id.equals("d3d9") || id.equals("dx9")) {
			return D3D9;
		}else if(id.equals("opengl")) {
			return OPENGL;
		}else if(id.equals("opengles")) {
			return OPENGLES;
		}else if(id.equals("metal")) {
			return METAL;
		}else if(id.equals("vulkan")) {
			return VULKAN;
		}else {
			return DEFAULT;
		}
	}
	
	public static EnumPlatformANGLE fromGLRendererString(String str) {
		str = str.toLowerCase();
		if(str.contains("direct3d11") || str.contains("d3d11")) {
			return D3D11;
		}else if(str.contains("direct3d9") || str.contains("d3d9")) {
			return D3D9;
		}else if(str.contains("opengl es")) {
			return OPENGLES;
		}else if(str.contains("opengl")) {
			return OPENGL;
		}else if(str.contains("metal")) {
			return METAL;
		}else if(str.contains("vulkan")) {
			return VULKAN;
		}else {
			return DEFAULT;
		}
	}
	
}
