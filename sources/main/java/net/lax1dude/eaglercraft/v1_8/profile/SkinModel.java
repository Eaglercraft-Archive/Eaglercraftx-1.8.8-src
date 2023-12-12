package net.lax1dude.eaglercraft.v1_8.profile;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public enum SkinModel {
	STEVE(0, 64, 64, "default", false), ALEX(1, 64, 64, "slim", false), ZOMBIE(2, 64, 64, "zombie", true);
	
	public final int id;	
	public final int width;
	public final int height;
	public final String profileSkinType;
	public final boolean sanitize;
	
	public static final SkinModel[] skinModels = new SkinModel[3];
	private static final Map<String, SkinModel> skinModelsByName = new HashMap();
	
	private SkinModel(int id, int w, int h, String profileSkinType, boolean sanitize) {
		this.id = id;
		this.width = w;
		this.height = h;
		this.profileSkinType = profileSkinType;
		this.sanitize = sanitize;
	}

	public static SkinModel getModelFromId(String str) {
		SkinModel mdl = skinModelsByName.get(str.toLowerCase());
		if(mdl == null) {
			return skinModels[0];
		}
		return mdl;
	}

	public static SkinModel getModelFromId(int id) {
		SkinModel s = null;
		if(id >= 0 && id < skinModels.length) {
			s = skinModels[id];
		}
		if(s != null) {
			return s;
		}else {
			return STEVE;
		}
	}
	
	static {
		SkinModel[] arr = values();
		for(int i = 0; i < arr.length; ++i) {
			skinModels[arr[i].id] = arr[i];
			skinModelsByName.put(arr[i].profileSkinType, arr[i]);
		}
	}

}