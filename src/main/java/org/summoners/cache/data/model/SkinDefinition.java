package org.summoners.cache.data.model;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.summoners.nio.*;

public class SkinDefinition {
	
	public SkinDefinition(ByteBuffer buf) throws IOException {
		magic = buf.getInt();
		version = buf.getShort();
		objectCount = buf.getShort();
		
		if (version == 1 || version == 2) {
			materialCount = buf.getInt();
			for (int i = 0; i != materialCount; ++i) {
				SkinMaterial material = new SkinMaterial();
				material.setName(BufferUtil.getString(buf, SkinMaterial.MATERIAL_NAME_LENGTH));
				material.setStartVertex(buf.getInt());
				material.setVertexCount(buf.getInt());
				material.setStartIndex(buf.getInt());
				material.setIndexCount(buf.getInt());
				materials.add(material);
			}
			
			indexCount = buf.getInt();
			vertexCount = buf.getInt();
			
			for (int i = 0; i != indexCount; ++i)
				indices.add((int) buf.getShort());
			
			for (int i = 0; i != vertexCount; ++i) {
				SkinVertex vertex = new SkinVertex();
				
				for (int axis = 0; axis != 3; ++axis)
					vertex.getPosition()[axis] = buf.getFloat();
				
				for (int bone = 0; bone != SkinVertex.BONE_INDEX_SIZE; ++bone)
					vertex.getBoneIndex()[bone] = buf.get();
				
				for (int weight = 0; weight != 4; ++weight)
					vertex.getWeights()[weight] = buf.getFloat();
				
				for (int normal = 0; normal != 3; ++normal)
					vertex.getNormal()[normal] = buf.getFloat();
				
				vertex.getTexCoords()[0] = buf.getFloat();
				vertex.getTexCoords()[1] = buf.getFloat();
			}
			
			if (version == 2)
				for (int i = 0; i != 3; ++i)
					endTab.add(buf.getInt());
		} else
			throw new IOException("Skin version not supported: " + version);
	}
	
	private int magic = 0;
	
	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	private int version = 0;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	private int objectCount = 0;

	public int getObjectCount() {
		return objectCount;
	}

	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}
	
	private int materialCount = 0;

	public int getMaterialCount() {
		return materialCount;
	}

	public void setMaterialCount(int materialCount) {
		this.materialCount = materialCount;
	}
	
	private LinkedList<SkinMaterial> materials = new LinkedList<>();

	public LinkedList<SkinMaterial> getMaterials() {
		return materials;
	}

	public void setMaterials(LinkedList<SkinMaterial> materials) {
		this.materials = materials;
	}
	
	private int indexCount = 0;

	public int getIndexCount() {
		return indexCount;
	}

	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}
	
	private int vertexCount = 0;

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}
	
	private LinkedList<Integer> indices = new LinkedList<>();

	public LinkedList<Integer> getIndices() {
		return indices;
	}

	public void setIndices(LinkedList<Integer> indices) {
		this.indices = indices;
	}
	
	private LinkedList<SkinVertex> vertices = new LinkedList<>();

	public LinkedList<SkinVertex> getVertices() {
		return vertices;
	}

	public void setVertices(LinkedList<SkinVertex> vertices) {
		this.vertices = vertices;
	}
	
	private LinkedList<Integer> endTab = new LinkedList<>();

	public LinkedList<Integer> getEndTab() {
		return endTab;
	}

	public void setEndTab(LinkedList<Integer> endTab) {
		this.endTab = endTab;
	}
	
	public static class SkinMaterial {
		
		public SkinMaterial() { }

		private String name = "";

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private int startVertex = 0;

		public int getStartVertex() {
			return startVertex;
		}

		public void setStartVertex(int startVertex) {
			this.startVertex = startVertex;
		}

		private int vertexCount = 0;

		public int getVertexCount() {
			return vertexCount;
		}

		public void setVertexCount(int vertexCount) {
			this.vertexCount = vertexCount;
		}

		private int startIndex = 0;

		public int getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		private int indexCount = 0;

		public int getIndexCount() {
			return indexCount;
		}

		public void setIndexCount(int indexCount) {
			this.indexCount = indexCount;
		}
		
		public static final int MATERIAL_NAME_LENGTH = 64;
	}
	
	public static class SkinVertex {
		
		public SkinVertex() { }
		
		private float[] position = new float[3];
		
		public float[] getPosition() {
			return position;
		}
		
		public void setPosition(float[] position) {
			this.position = position;
		}
		
		private int[] boneIndex = new int[BONE_INDEX_SIZE];
		
		public int[] getBoneIndex() {
			return boneIndex;
		}
		
		public void setBoneIndex(int[] boneIndex) {
			this.boneIndex = boneIndex;
		}
		
		private float[] weights = new float[4];
		
		public float[] getWeights() {
			return weights;
		}
		
		public void setWeights(float[] weights) {
			this.weights = weights;
		}
		
		private float[] normal = new float[4];
		
		public float[] getNormal() {
			return normal;
		}
		
		public void setNormal(float[] normal) {
			this.normal = normal;
		}
		
		private float[] texCoords = new float[2];

		public float[] getTexCoords() {
			return texCoords;
		}
		
		public void setTexCoords(float[] texCoords) {
			this.texCoords = texCoords;
		}
		
		public static final int BONE_INDEX_SIZE = 4;
	
	}
}
