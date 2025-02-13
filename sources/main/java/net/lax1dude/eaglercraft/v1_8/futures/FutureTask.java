/*
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

package net.lax1dude.eaglercraft.v1_8.futures;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FutureTask<V> implements RunnableFuture<V> {

	private boolean cancelled;
	private boolean completed;
	private V result;
	private Callable<V> callable;
	
	public FutureTask(Callable<V> callable) {
		this.callable = callable;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		if(!cancelled) {
			cancelled = true;
			if(!completed) {
				done();
			}
		}
		return true;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean isDone() {
		return cancelled || completed;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		if(!completed) {
			if(!cancelled) {
				try {
					result = callable.call();
				}catch(Throwable t) {
					throw new ExecutionException(t);
				}finally {
					completed = true;
					done();
				}
			}
		}
		return result;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
		return get();
	}

	@Override
	public void run() {
		try {
			get();
		} catch (ExecutionException t) {
			throw t;
		} catch (Throwable t) {
			throw new ExecutionException(t);
		}
	}
	
	protected void done() {
	}
	
}