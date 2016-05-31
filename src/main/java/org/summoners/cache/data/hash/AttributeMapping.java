package org.summoners.cache.data.hash;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import java.util.stream.*;

import org.summoners.util.*;

/**
 * An attribute value mapped to a specific hash.
 * @author Brittan Thomas
 */
public interface AttributeMapping {
	
	/**
	 * Gets the hashed value for this attribute name.
	 *
	 * @return the hashed value for this attribute name
	 */
	public long getHash();
	
	/**
	 * Gets the description of this attribute.
	 *
	 * @return the description of this attribute
	 */
	public String getAttribute();
	
	/**
	 * Gets the category this attribute belongs to.
	 *
	 * @return the category this attribute belongs to.
	 */
	public String getCategory();
	
	/**
	 * The map of hashes to their corresponding attribute mappings.
	 */
	public static HashMap<Long, AttributeMapping> MAPPINGS = new HashMap<>();
	
	/**
	 * Populates the map of attributes.
	 */
	public static void populateMap() {
		AttributeMapping[][] mappings = new AttributeMapping[][] { BuildsMapping.values(), CategoriesMapping.values(), DataMapping.values(),
					ImagesMapping.values(), InfoMapping.values(), ItemInclusionListMapping.values(), ItemSet1Mapping.values(),
					ItemSet2Mapping.values(), MeshSkin1Mapping.values(), MeshSkin2Mapping.values(), MeshSkin3Mapping.values(),
					MeshSkin4Mapping.values(), MeshSkin5Mapping.values(), MeshSkin6Mapping.values(), MeshSkin7Mapping.values(),
					MeshSkin8Mapping.values(), MeshSkin9Mapping.values(), MeshSkin10Mapping.values(), MeshSkin11Mapping.values(),
					MeshSkin12Mapping.values(), MeshSkin13Mapping.values(), MeshSkin14Mapping.values(), MeshSkin15Mapping.values(),
					MeshSkin16Mapping.values(), MeshSkin17Mapping.values(), MeshSkin18Mapping.values(), MeshSkin19Mapping.values() };
		for (AttributeMapping[] category : mappings)
			for (AttributeMapping mapping : category)
				MAPPINGS.put(mapping.getHash(), mapping);
	}
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) throws IOException {
		Map<String, Map<String, Double>> map = JsonIO.load(new File("./mappings.json"), Map.class);
		File file = new File("../summoners-io/src/main/java/org/summoners/io/cache/data/hash/");
		LinkedList<File> files = Stream.of(file.listFiles((d, n) -> !n.contains("AttributeMapping"))).collect(Collectors.toCollection(() -> new LinkedList<>()));
		for (File javaFile : files) {
			String name = javaFile.getName().replaceAll(".java", "");
			Map<String, Double> subMap = map.get(name.substring(0, name.length() - 7));
			if (subMap == null)
				subMap = map.get(name.toLowerCase().substring(0, name.length() - 7));
			
			if (subMap == null) {
				System.out.println("Map " + name.substring(0, name.length() - 7) + " doesn't exist!");
				continue;
			}
			
			try (FileWriter writer = new FileWriter(javaFile)) {
				writer.write("package org.summoners.io.cache.data.hash;" 									+ "\r\n\r\n");

				writer.write("/**" 																				+ "\r\n");
				writer.write(" * Representation of an attribute value mapped to a specific hash."				+ "\r\n");
				writer.write(" * @author Brittan Thomas"														+ "\r\n");
				writer.write(" */" 																				+ "\r\n");
				writer.write("public enum " + name + " implements AttributeMapping {" 							+ "\r\n");
				
				int index = 0; for (Entry<String, Double> entry : subMap.entrySet()) {
					index++; writer.write("\t" + capsify(entry.getKey()) + "(" + entry.getValue().longValue() + "L)" + 
																			(index == subMap.size() ? ";" : ",") +"\r\n");
				}
				
				writer.write("\r\n\t" + "/**" 																	+ "\r\n");
				writer.write("\t" + 	" * Instantiates a new attribute mapping."								+ "\r\n");
				writer.write("\t" + 	" * " 																	+ "\r\n");
				writer.write("\t" + 	" * @param hash"														+ "\r\n");
				writer.write("\t" + 	" *            the hashed value of the attribute"						+ "\r\n");
				writer.write("\t" + 	" */" 																	+ "\r\n");
				writer.write("\t" + 	"private " + name + "(long hash) {" 									+ "\r\n");
				writer.write("\t\t" +		"this.hash = hash;" 												+ "\r\n");
				writer.write("\t\t" +		"this.attribute = AttributeMapping.properify(name());"				+ "\r\n");
				writer.write("\t" + 	"}" 																+ "\r\n\r\n");
				
				writer.write("\t" + 	"/**" 																	+ "\r\n");
				writer.write("\t" + 	" * The hashed value of this mapped attribute."							+ "\r\n");
				writer.write("\t" + 	" */" 																	+ "\r\n");
				writer.write("\t" + 	"private long hash;" 												+ "\r\n\r\n");

				writer.write("\t" + 	"/* (non-Javadoc)"														+ "\r\n");
				writer.write("\t" + 	" * @see org.summoners.io.cache.data.hash.AttributeMapping#getHash()" 	+ "\r\n");
				writer.write("\t" + 	" */" 																	+ "\r\n");
				writer.write("\t" + 	"@Override"																+ "\r\n");
				writer.write("\t" + 	"public long getHash() {"												+ "\r\n");
				writer.write("\t\t" + 		"return hash;"														+ "\r\n");
				writer.write("\t" + 	"}"																	+ "\r\n\r\n");
				
				writer.write("\t" + 	"/**" 																	+ "\r\n");
				writer.write("\t" + 	" * The properly formatted description of this attribute."				+ "\r\n");
				writer.write("\t" + 	" */" 																	+ "\r\n");
				writer.write("\t" + 	"private String attribute;"											+ "\r\n\r\n");

				writer.write("\t" + 	"/* (non-Javadoc)"														+ "\r\n");
				writer.write("\t" + 	" * @see org.summoners.io.cache.data.hash.AttributeMapping#getAttribute()  \r\n");
				writer.write("\t" + 	" */" 																	+ "\r\n");
				writer.write("\t" + 	"@Override"																+ "\r\n");
				writer.write("\t" + 	"public String getAttribute() {"										+ "\r\n");
				writer.write("\t\t" + 		"return attribute;"													+ "\r\n");
				writer.write("\t" + 	"}"																	+ "\r\n\r\n");
				
				writer.write("\t" + 	"/* (non-Javadoc)"														+ "\r\n");
				writer.write("\t" + 	" * @see org.summoners.io.cache.data.hash.AttributeMapping#getCategory()"+"\r\n");
				writer.write("\t" + 	" */" 																	+ "\r\n");
				writer.write("\t" + 	"@Override"																+ "\r\n");
				writer.write("\t" + 	"public String getCategory() {"											+ "\r\n");
				writer.write("\t\t" + 		"return \"" + name.substring(0, name.length() - 7) + "\";"			+ "\r\n");
				writer.write("\t" + 	"}"																	+ "\r\n\r\n");
				
				writer.write("}");
				
			}
		}
	}
	
	/**
	 * Properly formats a string for enum conventions.
	 *
	 * @param string
	 *            the string of the enum needing formatting
	 * @return the formatted string for enum conventions
	 */
	public static String capsify(String string) {
		StringBuilder builder = new StringBuilder();
		for (char c : string.toCharArray()) {
			if (c == '-')
				c = '_';
			if (builder.length() != 0 && c >= 'A' && c <= 'Z')
				builder.append("_");
			builder.append(c);
		}
		return builder.toString().toUpperCase();
	}
	
	/**
	 * Turns an enum name into a properly formatted String name.
	 * @param string
	 * 			the string to be formatted properly
	 * @return the formatted string
	 */
	public static String properify(String string) {
		StringBuilder builder = new StringBuilder();
		boolean upper = true;
		for (char c : string.toLowerCase().toCharArray()) {
			if (c == '_') {
				upper = true;
			} else if (upper) {
				builder.append(Character.toUpperCase(c));
				upper = false;
			} else
				builder.append(c);
		}
		return builder.toString();
	}
	
}
