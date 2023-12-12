
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  5  @  5 : 6

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 5

> CHANGE  1 : 2  @  1 : 2

~ import com.google.common.collect.Lists;

> CHANGE  1 : 3  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglerZLIB;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.export.RandomAccessMemoryFile;

> CHANGE  1 : 2  @  1 : 5

~ public class RegionFile {

> CHANGE  1 : 2  @  1 : 3

~ 	private RandomAccessMemoryFile dataFile;

> DELETE  4  @  4 : 5

> CHANGE  1 : 2  @  1 : 3

~ 	public RegionFile(RandomAccessMemoryFile dataFile) {

> CHANGE  3 : 5  @  3 : 9

~ 			this.dataFile = dataFile;
~ 			if (this.dataFile.getLength() < 4096) {

> CHANGE  11 : 13  @  11 : 13

~ 			if ((this.dataFile.getLength() & 4095L) != 0L) {
~ 				for (int j1 = 0; (long) j1 < (this.dataFile.getLength() & 4095L); ++j1) {

> CHANGE  4 : 5  @  4 : 5

~ 			int k1 = (int) this.dataFile.getLength() / 4096;

> CHANGE  8 : 9  @  8 : 9

~ 			this.dataFile.seek(0);

> CHANGE  16 : 17  @  16 : 17

~ 			throw new RuntimeException("Could not initialize RegionFile!", ioexception);

> CHANGE  18 : 19  @  18 : 19

~ 						this.dataFile.seek(j * 4096);

> CHANGE  10 : 12  @  10 : 12

~ 								return new DataInputStream(new BufferedInputStream(
~ 										EaglerZLIB.newGZIPInputStream(new ByteArrayInputStream(abyte1))));

> CHANGE  4 : 5  @  4 : 5

~ 										EaglerZLIB.newInflaterInputStream(new ByteArrayInputStream(abyte))));

> CHANGE  12 : 13  @  12 : 13

~ 	public DataOutputStream getChunkDataOutputStream(int x, int z) throws IOException {

> CHANGE  1 : 2  @  1 : 2

~ 				: new DataOutputStream(EaglerZLIB.newDeflaterOutputStream(new RegionFile.ChunkBuffer(x, z)));

> CHANGE  50 : 51  @  50 : 51

~ 					this.dataFile.seek(this.dataFile.getLength());

> CHANGE  13 : 14  @  13 : 14

~ 			this.setChunkTimestamp(x, z, (int) (System.currentTimeMillis() / 1000L));

> CHANGE  1 : 2  @  1 : 2

~ 			throw new RuntimeException("Could not write chunk to RegionFile!", ioexception);

> CHANGE  5 : 6  @  5 : 6

~ 		this.dataFile.seek(sectorNumber * 4096);

> CHANGE  19 : 20  @  19 : 20

~ 		this.dataFile.seek((x + z * 32) * 4);

> CHANGE  5 : 6  @  5 : 6

~ 		this.dataFile.seek(4096 + (x + z * 32) * 4);

> CHANGE  3 : 5  @  3 : 8

~ 	public RandomAccessMemoryFile getFile() {
~ 		return dataFile;

> EOF
