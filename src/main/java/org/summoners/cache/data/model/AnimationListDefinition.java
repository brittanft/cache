package org.summoners.cache.data.model;

import java.io.*;
import java.util.*;

import org.summoners.cache.*;

public class AnimationListDefinition {
	
	public AnimationListDefinition(int modelSkin, RiotFile file) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			while (reader.ready()) {
				String line = reader.readLine();
				System.out.println(line);
				char[] chars = line.toCharArray();
				if (chars.length <= 0 || chars[0] == ';' || chars[0] == '\n' || chars[0] == '\r' || chars[0] == ' ')
					continue;
				
				if (chars[0] == '[') {
					int skinNumber = -1;
					if (chars[6] == ']')
						skinNumber = Integer.parseInt(new String(new char[] { chars[6] }));
					else
						skinNumber = Integer.parseInt(new String(new char[] { chars[5], chars[6] }));
					
					while (reader.ready()) {
						String recentLine = reader.readLine();
						if (recentLine.length() <= 0)
							return;
						
						char[] recent = recentLine.toCharArray();
						if (recent[0] == ';' || recent[0] == '\n' || recent[0] == '\r' || recent[0] == ' ')
							return;
						
						if (modelSkin == skinNumber)
							parseAnimations(true, line);
					}
				} else
					parseAnimations(false, line);
			}
		} finally {
			reader.close();
		}
	}
	
	private static void parseAnimations(boolean replace, String line) {
		String[] raw = line.split("\t");
		LinkedList<String> data = new LinkedList<>();
		for (String string : raw) {
			string.trim();
			if (string.length() > 0)
				data.add(string);
		}
		
		if (data.size() > 1) {
			String animation = data.get(0).trim();
			String animationFile = data.get(1).trim().toLowerCase();
			if (replace) {
				if (animations.containsKey(animation))
					animations.remove(animation);
			
				animations.put(animation, animationFile);
			} else
				animations.computeIfAbsent(animation, a -> animationFile);
		}
	}
	
	private static LinkedHashMap<String, String> animations = new LinkedHashMap<>();
	
	public static LinkedHashMap<String, String> getAnimations() {
		return animations;
	}
	
}
