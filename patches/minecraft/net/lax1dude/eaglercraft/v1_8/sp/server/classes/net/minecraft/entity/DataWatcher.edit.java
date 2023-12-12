
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  8 : 10  @  8 : 13

~ import net.minecraft.crash.CrashReport;
~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  20 : 21  @  20 : 21

~ 	// private ReadWriteLock lock = new ReentrantReadWriteLock();

> CHANGE  16 : 17  @  16 : 17

~ 			// this.lock.writeLock().lock();

> CHANGE  1 : 2  @  1 : 2

~ 			// this.lock.writeLock().unlock();

> CHANGE  7 : 8  @  7 : 8

~ 		// this.lock.writeLock().lock();

> CHANGE  1 : 2  @  1 : 2

~ 		// this.lock.writeLock().unlock();

> CHANGE  28 : 29  @  28 : 29

~ 		// this.lock.readLock().lock();

> CHANGE  11 : 12  @  11 : 12

~ 		// this.lock.readLock().unlock();

> CHANGE  41 : 42  @  41 : 42

~ 			// this.lock.readLock().lock();

> CHANGE  12 : 13  @  12 : 13

~ 			// this.lock.readLock().unlock();

> CHANGE  7 : 8  @  7 : 8

~ 		// this.lock.readLock().lock();

> CHANGE  5 : 6  @  5 : 6

~ 		// this.lock.readLock().unlock();

> CHANGE  5 : 6  @  5 : 6

~ 		// this.lock.readLock().lock();

> CHANGE  9 : 10  @  9 : 10

~ 		// this.lock.readLock().unlock();

> CHANGE  25 : 26  @  25 : 26

~ 			buffer.writeItemStackToBuffer_server(itemstack);

> CHANGE  45 : 47  @  45 : 46

~ 				datawatcher$watchableobject = new DataWatcher.WatchableObject(i, j,
~ 						buffer.readItemStackFromBuffer_server());

> CHANGE  21 : 22  @  21 : 22

~ 		// this.lock.writeLock().lock();

> CHANGE  10 : 11  @  10 : 11

~ 		// this.lock.writeLock().unlock();

> EOF
