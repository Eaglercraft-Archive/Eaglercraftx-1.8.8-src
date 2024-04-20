package net.lax1dude.eaglercraft.v1_8.opengl;

import net.lax1dude.eaglercraft.v1_8.internal.IBufferArrayGL;
import net.lax1dude.eaglercraft.v1_8.internal.IBufferGL;
import net.lax1dude.eaglercraft.v1_8.opengl.FixedFunctionShader.FixedFunctionState;

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
public class HighPolyMesh {

	IBufferArrayGL vertexArray;
	IBufferGL vertexBuffer;
	IBufferGL indexBuffer;

	int vertexCount;
	int indexCount;

	boolean hasTexture;

	public HighPolyMesh(IBufferArrayGL vertexArray, IBufferGL vertexBuffer, IBufferGL indexBuffer, int vertexCount,
			int indexCount, boolean hasTexture) {
		this.vertexArray = vertexArray;
		this.vertexBuffer = vertexBuffer;
		this.indexBuffer = indexBuffer;
		this.vertexCount = vertexCount;
		this.indexCount = indexCount;
		this.hasTexture = hasTexture;
	}

	HighPolyMesh() {
		
	}

	public boolean isNull() {
		return vertexArray == null;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public boolean getHasTexture() {
		return hasTexture;
	}

	public int getAttribBits() {
		return hasTexture ? (FixedFunctionState.STATE_HAS_ATTRIB_TEXTURE | FixedFunctionState.STATE_HAS_ATTRIB_NORMAL) : FixedFunctionState.STATE_HAS_ATTRIB_NORMAL; 
	}
}
