package org.summoners.cache.data.model;

import java.util.*;

import org.summoners.cache.*;
import org.summoners.cache.data.*;
import org.summoners.cache.data.hash.*;

public class ModelDefinition {
	
	public static LinkedList<ModelDefinition> getModelDefinitions(RiotInibin inibin) {
		LinkedList<ModelDefinition> list = new LinkedList<>();
		
		String animListKey = getAnimationList(inibin.getFile());
		
		ModelDefinition def = new ModelDefinition();
		def.animationListKey = animListKey;
		
		boolean modelStringsResult = getModelStrings(def, inibin, MeshSkinMapping.CHAMPION_SKIN_NAME.getHash(), MeshSkinMapping.SIMPLE_SKIN.getHash(),
																	MeshSkinMapping.SKELETON.getHash(), MeshSkinMapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(1);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin1Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin1Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin1Mapping.SKELETON.getHash(), MeshSkin1Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(2);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin2Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin2Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin2Mapping.SKELETON.getHash(), MeshSkin2Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(3);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin3Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin3Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin3Mapping.SKELETON.getHash(), MeshSkin3Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(4);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin4Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin4Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin4Mapping.SKELETON.getHash(), MeshSkin4Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(5);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin5Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin5Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin5Mapping.SKELETON.getHash(), MeshSkin5Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(6);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin6Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin6Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin6Mapping.SKELETON.getHash(), MeshSkin6Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(7);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin7Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin7Mapping.SIMPLE_SKIN.getHash(), 
													MeshSkin7Mapping.SKELETON.getHash(), MeshSkin7Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(8);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin8Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin8Mapping.SIMPLE_SKIN.getHash(), 
													MeshSkin8Mapping.SKELETON.getHash(), MeshSkin8Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(9);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin9Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin9Mapping.SIMPLE_SKIN.getHash(), 
													MeshSkin9Mapping.SKELETON.getHash(), MeshSkin9Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(10);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin10Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin10Mapping.SIMPLE_SKIN.getHash(), 
													MeshSkin10Mapping.SKELETON.getHash(), MeshSkin10Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(11);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin11Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin11Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin11Mapping.SKELETON.getHash(), MeshSkin11Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(12);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin12Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin12Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin12Mapping.SKELETON.getHash(), MeshSkin12Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(13);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin13Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin13Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin13Mapping.SKELETON.getHash(), MeshSkin13Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(14);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin14Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin14Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin14Mapping.SKELETON.getHash(), MeshSkin14Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(15);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin15Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin15Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin15Mapping.SKELETON.getHash(), MeshSkin15Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(16);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin16Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin16Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin16Mapping.SKELETON.getHash(), MeshSkin16Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(17);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin17Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin17Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin17Mapping.SKELETON.getHash(), MeshSkin17Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(18);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin18Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin18Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin18Mapping.SKELETON.getHash(), MeshSkin18Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(19);
			list.add(def);
		}
		
		def = new ModelDefinition();
		def.animationListKey = animListKey;
		modelStringsResult = getModelStrings(def, inibin, MeshSkin19Mapping.CHAMPION_SKIN_NAME.getHash(), MeshSkin19Mapping.SIMPLE_SKIN.getHash(), 
														MeshSkin19Mapping.SKELETON.getHash(), MeshSkin19Mapping.TEXTURE.getHash());
		if (modelStringsResult) {
			def.setSkn(20);
			list.add(def);
		}
		
		return list;
	}
	
	private static String getAnimationList(RiotFile file) {
		if (!file.getName().toLowerCase().contains("skin") && !file.getName().toLowerCase().contains("base"))
			return file.getName();
		
		String path = file.getDirectory();
		String[] split = path.split("/");
		if (split.length > 2)
			return split[split.length - 3];
		
		return "";
	}
	
	private static boolean getModelStrings(ModelDefinition def, RiotInibin ini, long name, long index, long skeleton, long texture) {
		boolean result = false;
		if (ini.contains(name))
			def.setName((String) ini.get(name));
		
		if (ini.contains(index) && ini.contains(skeleton) && ini.contains(texture)) {
			def.setSkin((String) ini.get(index));
			def.setSkin(def.getSkin().toLowerCase());
			def.setSkeleton((String) ini.get(skeleton));
			def.setSkeleton(def.getSkeleton().toLowerCase());
			def.setTexture((String) ini.get(texture));
			def.setTexture(def.getTexture().toLowerCase());
			result = true;
		}
		return result;
	}
	
	private int skn = -1;

	public int getSkn() {
		return skn;
	}

	public void setSkn(int skn) {
		this.skn = skn;
	}

	private String animationListKey = "";

	public String getAnimationListKey() {
		return animationListKey;
	}

	public void setAnimationListKey(String animationListKey) {
		this.animationListKey = animationListKey;
	}

	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String skin = "";

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	private String skeleton = "";

	public String getSkeleton() {
		return skeleton;
	}

	public void setSkeleton(String skeleton) {
		this.skeleton = skeleton;
	}

	private String texture = "";

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}
}
