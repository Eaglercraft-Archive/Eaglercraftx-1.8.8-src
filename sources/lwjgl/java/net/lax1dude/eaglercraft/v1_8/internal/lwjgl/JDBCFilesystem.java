package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Properties;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem.IFilesystemProvider;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem;
import net.lax1dude.eaglercraft.v1_8.internal.VFSFilenameIterator;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.EaglerFileSystemException;
import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFSIterator2;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

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
public class JDBCFilesystem implements IFilesystemProvider {

	public static final Logger logger = LogManager.getLogger("JDBCFilesystem");

	private boolean newFilesystem = true;

	private static volatile boolean cleanupThreadStarted = false;
	private static final Collection<JDBCFilesystem> jdbcFilesystems = new LinkedList();

	private final String jdbcUri;
	private final String jdbcDriver;

	private final Connection conn;
	private final PreparedStatement createStatement;
	private final PreparedStatement updateStatement;
	private final PreparedStatement readStatement;
	private final PreparedStatement existsStatement;
	private final PreparedStatement sizeStatement;
	private final PreparedStatement deleteStatement;
	private final PreparedStatement renameStatement;
	private final PreparedStatement iterateNonRecursive;
	private final PreparedStatement iterateRecursive;
	private boolean hasClosed = false;

	private final Object mutex = new Object();

	public static IFilesystemProvider initialize(String jdbcUri, String jdbcDriver) {
		Class driver;
		try {
			driver = Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			throw new EaglerFileSystemException("JDBC driver class not found in JRE: " + jdbcDriver, e);
		}
		Driver driverObj = null;
		Enumeration<Driver> registeredDriversItr =  DriverManager.getDrivers();
		while(registeredDriversItr.hasMoreElements()) {
			Driver drv = registeredDriversItr.nextElement();
			if(drv.getClass().equals(driver)) {
				driverObj = drv;
				break;
			}
		}
		if(driverObj == null) {
			logger.warn("The class \"{}\" is not a registered JDBC driver, eaglercraft will try all registered drivers...", jdbcDriver);
		}
		Properties props = new Properties();
		for(Entry<Object, Object> etr : System.getProperties().entrySet()) {
			if(etr.getKey() instanceof String) {
				String str = (String)etr.getKey();
				if(str.startsWith("eagler.jdbc.opts.")) {
					props.put(str.substring(17), etr.getValue());
				}
			}
		}
		logger.info("Connecting to database: \"{}\"", jdbcUri);
		Connection conn;
		try {
			if(driverObj != null) {
				conn = driverObj.connect(jdbcUri, props);
			}else {
				conn = DriverManager.getConnection(jdbcUri, props);
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("Failed to connect to database: \"" + jdbcUri + "\"", ex);
		}
		try {
			return new JDBCFilesystem(conn, jdbcUri, jdbcDriver);
		} catch (SQLException ex) {
			try {
				conn.close();
			}catch(SQLException ex2) {
			}
			throw new EaglerFileSystemException("Failed to initialize database: \"" + jdbcUri + "\"", ex);
		}
	}

	private JDBCFilesystem(Connection conn, String jdbcUri, String jdbcDriver) throws SQLException {
		this.conn = conn;
		this.jdbcUri = jdbcUri;
		this.jdbcDriver = jdbcDriver;
		try(Statement stmt = conn.createStatement()) {
			stmt.execute("CREATE TABLE IF NOT EXISTS "
					+ "\"eaglercraft_desktop_runtime_filesystem\" ("
					+ "\"FileName\" VARCHAR(1024) NOT NULL,"
					+ "\"FileSize\" INT NOT NULL,"
					+ "\"FileData\" BLOB NOT NULL,"
					+ "PRIMARY KEY(\"FileName\"))");
			
			int totalFiles = 0;
			try(ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) AS total_files FROM eaglercraft_desktop_runtime_filesystem")) {
				if(resultSet.next()) {
					totalFiles = resultSet.getInt(1);
				}
			}
			logger.info("Loaded JDBC filesystem with {} files: \"{}\"", totalFiles, jdbcUri);
			if(totalFiles > 0) {
				newFilesystem = false;
			}
		}
		this.createStatement = conn.prepareStatement("INSERT INTO eaglercraft_desktop_runtime_filesystem (FileName, FileSize, FileData) VALUES(?,?,?)");
		this.updateStatement = conn.prepareStatement("UPDATE eaglercraft_desktop_runtime_filesystem SET FileSize = ?, FileData = ? WHERE FileName = ?");
		this.readStatement = conn.prepareStatement("SELECT FileData FROM eaglercraft_desktop_runtime_filesystem WHERE FileName = ? LIMIT 1");
		this.existsStatement = conn.prepareStatement("SELECT COUNT(FileName) AS has_object FROM eaglercraft_desktop_runtime_filesystem WHERE FileName = ? LIMIT 1");
		this.sizeStatement = conn.prepareStatement("SELECT FileSize FROM eaglercraft_desktop_runtime_filesystem WHERE FileName = ? LIMIT 1");
		this.deleteStatement = conn.prepareStatement("DELETE FROM eaglercraft_desktop_runtime_filesystem WHERE FileName = ?");
		this.renameStatement = conn.prepareStatement("UPDATE eaglercraft_desktop_runtime_filesystem SET FileName = ? WHERE FileName = ?");
		this.iterateNonRecursive = conn.prepareStatement("SELECT FileName FROM eaglercraft_desktop_runtime_filesystem WHERE FileName LIKE ? AND NOT FileName LIKE ?");
		this.iterateRecursive = conn.prepareStatement("SELECT FileName FROM eaglercraft_desktop_runtime_filesystem WHERE FileName LIKE ?");
		startCleanupThread();
		synchronized(jdbcFilesystems) {
			jdbcFilesystems.add(this);
		}
	}

	public boolean isNewFilesystem() {
		return newFilesystem;
	}

	private static void startCleanupThread() {
		if(!cleanupThreadStarted) {
			cleanupThreadStarted = true;
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				synchronized(jdbcFilesystems) {
					if(!jdbcFilesystems.isEmpty()) {
						for(JDBCFilesystem fs : jdbcFilesystems) {
							fs.shutdown0();
						}
						jdbcFilesystems.clear();
					}
				}
			}, "JDBCFilesystemCleanup"));
		}
	}

	public void shutdown() {
		shutdown0();
		synchronized(jdbcFilesystems) {
			jdbcFilesystems.remove(this);
		}
	}

	private void shutdown0() {
		synchronized(mutex) {
			if(!hasClosed) {
				hasClosed = true;
				logger.info("Disconnecting from database: \"{}\"", jdbcUri);
				try {
					shutdown1();
				}catch(Throwable t) {
					logger.error("Failed to disconnect from database: \"{}\"");
					logger.error(t);
				}
			}
		}
	}

	private void shutdown1() throws SQLException {
		if(!conn.isClosed()) {
			quietClose(createStatement);
			quietClose(updateStatement);
			quietClose(readStatement);
			quietClose(existsStatement);
			quietClose(sizeStatement);
			quietClose(deleteStatement);
			quietClose(renameStatement);
			quietClose(iterateNonRecursive);
			quietClose(iterateRecursive);
			conn.close();
		}
	}

	private static void quietClose(Statement stmt) {
		try {
			stmt.close();
		}catch(Throwable t) {
		}
	}

	@Override
	public boolean eaglerDelete(String pathName) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				deleteStatement.setString(1, pathName);
				int ret = deleteStatement.executeUpdate();
				if(ret == 0) {
					PlatformFilesystem.logger.warn("Tried to delete file that doesn't exist: \"{}\"", pathName);
				}
				return ret > 0;
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing delete!", ex);
		}
	}

	@Override
	public ByteBuffer eaglerRead(String pathName) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				readStatement.setString(1, pathName);
				byte[] has = null;
				try(ResultSet resultSet = readStatement.executeQuery()) {
					if(resultSet.next()) {
						has = resultSet.getBytes(1);
					}
				}
				if(has == null) {
					PlatformFilesystem.logger.warn("Tried to read file that doesn't exist: \"{}\"", pathName);
					return null;
				}
				ByteBuffer byteBuf = PlatformRuntime.allocateByteBuffer(has.length);
				byteBuf.put(has);
				byteBuf.flip();
				return byteBuf;
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing read!", ex);
		}
	}

	@Override
	public void eaglerWrite(String pathName, ByteBuffer data) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				existsStatement.setString(1, pathName);
				boolean exists;
				try(ResultSet resultSet = existsStatement.executeQuery()) {
					if(resultSet.next()) {
						exists = resultSet.getInt(1) > 0;
					}else {
						exists = false;
					}
				}
				byte[] cp = new byte[data.remaining()];
				data.get(cp);
				if(exists) {
					updateStatement.setInt(1, cp.length);
					updateStatement.setBytes(2, cp);
					updateStatement.setString(3, pathName);
					if(updateStatement.executeUpdate() == 0) {
						throw new EaglerFileSystemException("SQL file update query did not update any rows!");
					}
				}else {
					createStatement.setString(1, pathName);
					createStatement.setInt(2, cp.length);
					createStatement.setBytes(3, cp);
					createStatement.executeUpdate();
				}
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing write!", ex);
		}
	}

	@Override
	public boolean eaglerExists(String pathName) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				existsStatement.setString(1, pathName);
				try(ResultSet resultSet = existsStatement.executeQuery()) {
					if(resultSet.next()) {
						return resultSet.getInt(1) > 0;
					}else {
						return false;
					}
				}
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing exists!", ex);
		}
	}

	@Override
	public boolean eaglerMove(String pathNameOld, String pathNameNew) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				renameStatement.setString(1, pathNameNew);
				renameStatement.setString(2, pathNameOld);
				return renameStatement.executeUpdate() > 0;
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing move!", ex);
		}
	}

	@Override
	public int eaglerCopy(String pathNameOld, String pathNameNew) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				readStatement.setString(1, pathNameOld);
				try(ResultSet resultSet = readStatement.executeQuery()) {
					byte[] has = null;
					if(resultSet.next()) {
						has = resultSet.getBytes(1);
					}
					if(has == null) {
						return -1;
					}
					existsStatement.setString(1, pathNameNew);
					boolean exists;
					try(ResultSet resultSet2 = existsStatement.executeQuery()) {
						if(resultSet2.next()) {
							exists = resultSet2.getInt(1) > 0;
						}else {
							exists = false;
						}
					}
					if(exists) {
						updateStatement.setInt(1, has.length);
						updateStatement.setBytes(2, has);
						updateStatement.setString(3, pathNameNew);
						if(updateStatement.executeUpdate() == 0) {
							throw new EaglerFileSystemException("SQL file update query did not update any rows!");
						}
					}else {
						createStatement.setString(1, pathNameNew);
						createStatement.setInt(2, has.length);
						createStatement.setBytes(3, has);
						createStatement.executeUpdate();
					}
					return has.length;
				}
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing copy!", ex);
		}
	}

	@Override
	public int eaglerSize(String pathName) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				sizeStatement.setString(1, pathName);
				try(ResultSet resultSet = sizeStatement.executeQuery()) {
					if(resultSet.next()) {
						return resultSet.getInt(1);
					}else {
						return -1;
					}
				}
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing size!", ex);
		}
	}

	@Override
	public void eaglerIterate(String pathName, VFSFilenameIterator itr, boolean recursive) {
		try {
			synchronized(mutex) {
				if(hasClosed || conn.isClosed()) {
					throw new SQLException("Filesystem database connection is closed!");
				}
				PreparedStatement stmt;
				if(recursive) {
					stmt = iterateRecursive;
					stmt.setString(1, pathName + (!pathName.endsWith("/") ? "/%" : "%"));;
				}else {
					stmt = iterateNonRecursive;
					if(!pathName.endsWith("/")) {
						pathName += "/";
					}
					stmt.setString(1, pathName + "%");
					stmt.setString(2, pathName + "%/%");
				}
				try(ResultSet resultSet = stmt.executeQuery()) {
					while(resultSet.next()) {
						try {
							itr.next(resultSet.getString(1));
						}catch(VFSIterator2.BreakLoop exx) {
							break;
						}
					}
				}
			}
		}catch(SQLException ex) {
			throw new EaglerFileSystemException("JDBC exception thrown while executing iterate!", ex);
		}
	}

}
