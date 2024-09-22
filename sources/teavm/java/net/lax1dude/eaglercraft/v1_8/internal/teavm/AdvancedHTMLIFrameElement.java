package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.html.HTMLIFrameElement;
import org.teavm.jso.dom.types.DOMTokenList;

import com.google.common.collect.Iterators;

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
public abstract class AdvancedHTMLIFrameElement implements HTMLIFrameElement {

	@JSProperty
	public abstract void setAllow(String str);

	@JSProperty
	public abstract String getAllow();

	public void setAllowSafe(String requiredValue) {
		setAllow(requiredValue);
		if(!requiredValue.equals(getAllow())) {
			throw new IFrameSafetyException("Could not set allow attribute to: " + requiredValue);
		}
	}

	@JSProperty
	public abstract void setAllowFullscreen(boolean en);

	@JSProperty
	public abstract void setCredentialless(boolean en);

	@JSProperty
	public abstract void setLoading(String str);

	@JSProperty
	public abstract void setReferrerPolicy(String str);

	@JSProperty("csp")
	public abstract void setCSP(String str);

	@JSProperty
	public abstract void setSandbox(String str);

	@JSProperty
	public abstract DOMTokenList getSandbox();

	public void assertSafetyFeaturesSupported() {
		if(!checkSafetyFeaturesSupported()) {
			throw new IFrameSafetyException("Some required security features are not supported on this browser!");
		}
	}

	public void setSandboxSafe(Collection<String> requiredTokens) {
		setSandboxSafe(new HashSet<>(requiredTokens));
	}

	public void setSandboxSafe(Set<String> requiredTokens) {
		setSandbox(String.join(" ", requiredTokens));
		DOMTokenList theSandbox = getSandbox();
		for(String s : requiredTokens) {
			if(!theSandbox.contains(s)) {
				throw new IFrameSafetyException("Failed to set sandbox attribute: " + s);
			}
		}
		int l = theSandbox.getLength();
		for(int i = 0; i < l; ++i) {
			String s = theSandbox.item(i);
			if(!requiredTokens.contains(s)) {
				throw new IFrameSafetyException("Unknown sandbox attribute detected: " + s);
			}
		}
	}

	public void setSandboxSafe(Collection<String> requiredTokens, Collection<String> optionalTokens) {
		setSandboxSafe(new HashSet<>(requiredTokens), new HashSet<>(optionalTokens));
	}

	public void setSandboxSafe(Set<String> requiredTokens, Set<String> optionalTokens) {
		setSandbox(StringUtils.join(Iterators.concat(requiredTokens.iterator(), optionalTokens.iterator()), " "));
		DOMTokenList theSandbox = getSandbox();
		for(String s : requiredTokens) {
			if(!theSandbox.contains(s)) {
				throw new IFrameSafetyException("Failed to set sandbox attribute: " + s);
			}
		}
		int l = theSandbox.getLength();
		for(int i = 0; i < l; ++i) {
			String s = theSandbox.item(i);
			if(!requiredTokens.contains(s) && !optionalTokens.contains(s)) {
				throw new IFrameSafetyException("Unknown sandbox attribute detected: " + s);
			}
		}
	}

	@JSBody(params = {}, script = "return (typeof this.allow === \"string\") && (typeof this.sandbox === \"object\");")
	public native boolean checkSafetyFeaturesSupported();

	@JSBody(params = {}, script = "return (typeof this.csp === \"string\");")
	public native boolean checkCSPSupported();

}
