package net.lax1dude.eaglercraft.v1_8.internal;

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
public class VFSFilenameIteratorNonRecursive implements VFSFilenameIterator {

	private final VFSFilenameIterator child;
	private final int pathCount;

	public VFSFilenameIteratorNonRecursive(VFSFilenameIterator child, int pathCount) {
		this.child = child;
		this.pathCount = pathCount;
	}

	@Override
	public void next(String entry) {
		int i = countSlashes(entry);
		if(i == pathCount) {
			child.next(entry);
		}
	}

	public static int countSlashes(String str) {
		if(str.length() == 0) return -1;
		int j = 0;
		for(int i = 0, l = str.length(); i < l; ++i) {
			if(str.charAt(i) == '/') {
				++j;
			}
		}
		return j;
	}

}
