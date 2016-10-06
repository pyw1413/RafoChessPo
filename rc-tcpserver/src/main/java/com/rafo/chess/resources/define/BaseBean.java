package com.rafo.chess.resources.define;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseBean {
	// 用来存储修改过的数据
//	private Map<String, UpdateData> oldData = new ConcurrentHashMap<String, UpdateData>();

	// 数据id
	private long id = 0;

	// 最后一次修改时间
	private long lastUpdateTime = 0;

//	public Map<String, UpdateData> getOldData() {
//		return oldData;
//	}
//
//	public void setOldData(Map<String, UpdateData> oldData) {
//		this.oldData = oldData;
//	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void parseEntity(ResultSet rest) throws SQLException {
	}

	public ByteBuffer checkCapacity(ByteBuffer buffer, int length) {
		if (buffer.position() + length > buffer.capacity()) {
			ByteBuffer tmp = buffer;
			if (length > 1024) {
				buffer = ByteBuffer.allocate(buffer.capacity() + 1024 + length);
			} else {
				buffer = ByteBuffer.allocate(buffer.capacity() + 1024);
			}
			// 还原原始数据
			buffer.put(tmp);
		}
		return buffer;
	}

	/**
	 * 将本对象变化的数据序列化
	 * 
	 * @return 返回序列化后的数据
	 */
	public abstract byte[] serialization();

	/**
	 * 反序列化
	 * 
	 * @param data
	 *            要反序列化的数据
	 * 
	 * @return 返回序列化出来的对象
	 */
	public abstract <T> T unSerialization(byte[] data);
}
