package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.xml.Element;

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
public abstract class Touch implements JSObject {

	@JSProperty
	public abstract int getIdentifier();

	@JSProperty
	public abstract double getScreenX();

	@JSProperty
	public abstract double getScreenY();

	@JSProperty
	public abstract double getClientX();

	@JSProperty
	public abstract double getClientY();

	@JSProperty
	public abstract double getPageX();

	@JSProperty
	public abstract double getPageY();

	@JSProperty
	public abstract double getRadiusX();

	@JSBody(params = { "defVal" }, script = "return (typeof this.radiusX === \"number\") ? this.radiusX : defVal;")
	public abstract double getRadiusXSafe(double defaultVal);

	@JSProperty
	public abstract double getRadiusY();

	@JSBody(params = { "defVal" }, script = "return (typeof this.radiusY === \"number\") ? this.radiusY : defVal;")
	public abstract double getRadiusYSafe(double defaultVal);

	@JSProperty
	public abstract double getForce();

	@JSBody(params = { "defVal" }, script = "return (typeof this.force === \"number\") ? this.force : defVal;")
	public abstract double getForceSafe(double defaultVal);

	@JSProperty
	public abstract Element getTarget();

}
