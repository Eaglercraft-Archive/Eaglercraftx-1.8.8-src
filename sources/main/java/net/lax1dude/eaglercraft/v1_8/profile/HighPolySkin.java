package net.lax1dude.eaglercraft.v1_8.profile;

import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public enum HighPolySkin {

	LONG_ARMS(
			new ResourceLocation("eagler:mesh/longarms.png"),
			new ResourceLocation("eagler:mesh/longarms0.mdl"),
			null,
			new ResourceLocation("eagler:mesh/longarms2.mdl"),
			new ResourceLocation[] {
				new ResourceLocation("eagler:mesh/longarms1.mdl")
			},
			new float[] {
				1.325f
			},
			0.0f,
			new ResourceLocation("eagler:mesh/longarms.fallback.png")
	),
	
	WEIRD_CLIMBER_DUDE(
			new ResourceLocation("eagler:mesh/weirdclimber.png"),
			new ResourceLocation("eagler:mesh/weirdclimber0.mdl"),
			null,
			new ResourceLocation("eagler:mesh/weirdclimber2.mdl"),
			new ResourceLocation[] {
				new ResourceLocation("eagler:mesh/weirdclimber1.mdl")
			},
			new float[] {
				2.62f
			},
			-90.0f,
			new ResourceLocation("eagler:mesh/weirdclimber.fallback.png")
	),
	
	LAXATIVE_DUDE(
			new ResourceLocation("eagler:mesh/laxativedude.png"),
			new ResourceLocation("eagler:mesh/laxativedude0.mdl"),
			null,
			new ResourceLocation("eagler:mesh/laxativedude3.mdl"),
			new ResourceLocation[] {
				new ResourceLocation("eagler:mesh/laxativedude1.mdl"),
				new ResourceLocation("eagler:mesh/laxativedude2.mdl")
			},
			new float[] {
				2.04f
			},
			0.0f,
			new ResourceLocation("eagler:mesh/laxativedude.fallback.png")
	),
	
	BABY_CHARLES(
			new ResourceLocation("eagler:mesh/charles.png"),
			new ResourceLocation("eagler:mesh/charles0.mdl"),
			new ResourceLocation("eagler:mesh/charles1.mdl"),
			new ResourceLocation("eagler:mesh/charles2.mdl"),
			new ResourceLocation[] {},
			new float[] {},
			0.0f,
			new ResourceLocation("eagler:mesh/charles.fallback.png")
	),
	
	BABY_WINSTON(
			new ResourceLocation("eagler:mesh/winston.png"),
			new ResourceLocation("eagler:mesh/winston0.mdl"),
			null,
			new ResourceLocation("eagler:mesh/winston1.mdl"),
			new ResourceLocation[] {},
			new float[] {},
			0.0f,
			new ResourceLocation("eagler:mesh/winston.fallback.png")
	);

	public static float highPolyScale = 0.5f;

	public final ResourceLocation texture;
	public final ResourceLocation bodyModel;
	public final ResourceLocation headModel;
	public final ResourceLocation eyesModel;
	public final ResourceLocation[] limbsModel;
	public final float[] limbsOffset;
	public final float limbsInitialRotation;
	public final ResourceLocation fallbackTexture;
	
	HighPolySkin(ResourceLocation texture, ResourceLocation bodyModel, ResourceLocation headModel, ResourceLocation eyesModel,
			ResourceLocation[] limbsModel, float[] limbsOffset, float limbsInitialRotation, ResourceLocation fallbackTexture) {
		this.texture = texture;
		this.bodyModel = bodyModel;
		this.headModel = headModel;
		this.eyesModel = eyesModel;
		this.limbsModel = limbsModel;
		this.limbsOffset = limbsOffset;
		this.limbsInitialRotation = limbsInitialRotation;
		this.fallbackTexture = fallbackTexture;
	}

}
