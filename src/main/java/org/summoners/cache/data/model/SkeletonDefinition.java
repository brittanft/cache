package org.summoners.cache.data.model;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.summoners.nio.*;

public class SkeletonDefinition {
	
	public SkeletonDefinition(ByteBuffer buffer) throws IOException {
		id = BufferUtil.getString(buffer, ID_LENGTH);
		version = buffer.getInt() & 0xFFFFFFFFL;
		if (version == 1 || version == 2) {
			designerId = buffer.getInt() & 0xFFFFFFFFL;
			boneCount = buffer.getInt() & 0xFFFFFFFFL;
			for (int i = 0; i != boneCount; ++i) {
				SkeletonBone bone = new SkeletonBone();
				
				bone.setName(BufferUtil.getString(buffer, SkeletonBone.BONE_NAME_LENGTH));
				bone.setId(i);
				bone.setParentId(buffer.getInt());
				bone.setScale(buffer.getFloat());
				
				float[] orientation = bone.getOrientation();
				for (int transformation = 0; transformation != SkeletonBone.ORIENTATION_SIZE; ++transformation)
					orientation[transformation] = buffer.getFloat();
				
				bone.getPosition()[0] = orientation[3];
				bone.getPosition()[1] = orientation[7];
				bone.getPosition()[1] = orientation[11];
				bones.add(bone);
			}
			
			if (version == 2) {
				boneIdCount = buffer.getInt() & 0xFFFFFFFFL;
				for (int bone = 0; bone != boneIdCount; ++bone)
					boneIds.add(buffer.getInt() & 0xFFFFFFFFL);
			}
		} else if (version == 0) {
			buffer.getShort();
			boneCount = buffer.getShort();
			boneIdCount = buffer.getInt() & 0xFFFFFFFFL;
			int vertexOffset = buffer.getShort();
			
			buffer.getShort();
			int skinOffset = buffer.getInt();
			int animationOffset = buffer.getInt();
			buffer.getInt();
			buffer.getInt();
			int stringOffset = buffer.getInt();
			
			buffer.position(buffer.position() + 20);
			
			buffer.position(vertexOffset);
			for (int i = 0; i != boneCount; ++i) {
				SkeletonBone bone = new SkeletonBone();
				bone.setScale(0.1F);
				buffer.getShort();
				bone.setId(buffer.getShort());
				bone.setParentId(buffer.getShort());
				buffer.getShort();
				
				buffer.getInt();
				buffer.getFloat();
				
				for (int axis = 0; axis != 3; ++axis)
					bone.getPosition()[axis] = buffer.getFloat();
				
				for (int idx = 0; idx != 3; ++idx)
					buffer.getFloat();
				
				float[] orientation = bone.getOrientation();
				for (int idx = 0; idx != 4; ++idx)
					orientation[idx] = buffer.getFloat();
				
				for (int idx = 0; idx != 3; ++idx)
					buffer.getFloat();
				
				bones.add(bone);
			}
			
			buffer.position(skinOffset);
			for (int i = 0; i != boneCount; ++i) {
				long boneId = buffer.getInt() & 0xFFFFFFFFL;
				long animationId = buffer.getInt() & 0xFFFFFFFFL;
				boneIdMap.put(animationId, boneId);
			}
			
			buffer.position(animationOffset);
			for (int i = 0; i != boneCount; ++i)
				boneIds.add(buffer.getShort() & 0xFFFFL);
			
			buffer.position(stringOffset);
			for (int i = 0; i != boneCount; ++i) {
				String name = ""; char c;
				while ((c = (char) buffer.get()) != 0)
					name += c;
				
				bones.get(i).setName(name.toLowerCase());
			}
		} else
			throw new IOException("Skeleton version not supported: " + version);
	}

	public static class SkeletonBone {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		private int parentId;

		public int getParentId() {
			return parentId;
		}

		public void setParentId(int parentId) {
			this.parentId = parentId;
		}

		private float scale;

		public float getScale() {
			return scale;
		}

		public void setScale(float scale) {
			this.scale = scale;
		}

		private float[] orientation = new float[ORIENTATION_SIZE];

		public float[] getOrientation() {
			return orientation;
		}

		public void setOrientation(float[] orientation) {
			this.orientation = orientation;
		}

		private float[] position = new float[3];

		public float[] getPosition() {
			return position;
		}

		public void setPosition(float[] position) {
			this.position = position;
		}
		
		public static final int BONE_NAME_LENGTH = 32;
		public static final int ORIENTATION_SIZE = 12;
	}
	
	private String id;
	
	public String getId() {
		return id;
	}
	
	private long version;
	
	public long getVersion() {
		return version;
	}
	
	private long designerId;
	
	public long getDesignerId() {
		return designerId;
	}
	
	private long boneCount;
	
	public long getBoneCount() {
		return boneCount;
	}
	
	private LinkedList<SkeletonBone> bones = new LinkedList<>();
	
	public LinkedList<SkeletonBone> getBones() {
		return bones;
	}
	
	private long boneIdCount;
	
	public long getBoneIdCount() {
		return boneIdCount;
	}
	
	private LinkedList<Long> boneIds = new LinkedList<>();
	
	public LinkedList<Long> getBoneIds() {
		return boneIds;
	}

	/**
	 * A map of skeleton bone ids to .anm (v4) bone ids.
	 */
	private LinkedHashMap<Long, Long> boneIdMap = new LinkedHashMap<>();
	
	public LinkedHashMap<Long, Long> getBoneIdMap() {
		return boneIdMap;
	}
	
	public static final int ID_LENGTH = 8;
	
}
