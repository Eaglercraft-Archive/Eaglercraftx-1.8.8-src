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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class ListenableFutureTask<V> extends FutureTask<V> implements ListenableFuture<V> {
	
	private final List<Runnable> listeners = new ArrayList<>();

	public ListenableFutureTask(Callable<V> callable) {
		super(callable);
	}

	@Override
	public void addListener(final Runnable listener, final Executor executor) {
		listeners.add(new Runnable() {

			@Override
			public void run() {
				executor.execute(listener); // so dumb
			}
			
		});
	}
	
	protected void done() {
		for(int i = 0, l = listeners.size(); i < l; ++i) {
			Runnable r = listeners.get(i);
			try {
				r.run();
			}catch(Throwable t) {
				ListenableFuture.futureExceptionLogger.error("Exception caught running future listener!");
				ListenableFuture.futureExceptionLogger.error(t);
			}
		}
		listeners.clear();
	}

	public static <V> ListenableFutureTask<V> create(Callable<V> callableToSchedule) {
		return new ListenableFutureTask<>(callableToSchedule);
	}
	
}