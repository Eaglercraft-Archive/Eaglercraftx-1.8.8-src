package net.lax1dude.eaglercraft.v1_8.voice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright (c) 2022 ayunami2000. All Rights Reserved.
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
public class ExpiringSet<T> extends HashSet<T> {
    private final long expiration;
    private final ExpiringEvent<T> event;

    private final Map<T, Long> timestamps = new HashMap<>();

    public ExpiringSet(long expiration) {
        this.expiration = expiration;
        this.event = null;
    }

    public ExpiringSet(long expiration, ExpiringEvent<T> event) {
        this.expiration = expiration;
        this.event = event;
    }

    public interface ExpiringEvent<T> {
        void onExpiration(T item);
    }

    public void checkForExpirations() {
        Iterator<T> iterator = this.timestamps.keySet().iterator();
        long now = System.currentTimeMillis();
        while (iterator.hasNext()) {
            T element = iterator.next();
            if (super.contains(element)) {
                if (this.timestamps.get(element) + this.expiration < now) {
                    if (this.event != null) this.event.onExpiration(element);
                    iterator.remove();
                    super.remove(element);
                }
            } else {
                iterator.remove();
                super.remove(element);
            }
        }
    }

    public boolean add(T o) {
        checkForExpirations();
        boolean success = super.add(o);
        if (success) timestamps.put(o, System.currentTimeMillis());
        return success;
    }

    public boolean remove(Object o) {
        checkForExpirations();
        boolean success = super.remove(o);
        if (success) timestamps.remove(o);
        return success;
    }

    public void clear() {
        this.timestamps.clear();
        super.clear();
    }

    public boolean contains(Object o) {
        checkForExpirations();
        return super.contains(o);
    }
}
