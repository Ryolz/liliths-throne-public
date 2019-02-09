package com.lilithsthrone.game.inventory.clothing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lilithsthrone.controller.xmlParsing.XMLLoadException;
import com.lilithsthrone.utils.Util;

/**
 * @since 0.3.1
 * @version 0.3.1
 * @author Innoxia
 */
public enum OutfitType {

	/** To be used for characters that attack the player. Might be used for other similar criminal activities. */
	MUGGER,

	/** To be used for characters who sell their bodies. Might be used for other similar activities, such as strippers. */
	PROSTITUTE,

	/** To be used for characters in a casual setting. Either at home, going out, or non-uniformed casual work. */
	CASUAL,

	/** To be used for formal business. Suits and such. */
	BUSINESS,

	/** To be used when going clubbing. */
	CLUBBING,
	
	/** To be used on casual dates, such as to a restaurant. */
	CASUAL_DATE,
	
	/** To be used on formal occasions, such as fancy balls or cocktail parties. */
	FORMAL_DATE,
	
	/** Sportswear for exercise. */
	ATHLETIC,
	
	/** Clothing to be worn to bed. */
	NIGHTWEAR,

	/** Sexy clothing to be worn to bed. */
	NIGHTWEAR_SEXY;
	
	

	private static List<AbstractOutfit> allOutfits;
	private static List<AbstractOutfit> moddedOutfits;
	
	private static Map<AbstractOutfit, String> outfitsToIdMap = new HashMap<>();
	private static Map<String, AbstractOutfit> idToOutfitMap = new HashMap<>();
	
	public static AbstractOutfit getOutfitTypeFromId(String id) {
		id = Util.getClosestStringMatch(id, idToOutfitMap.keySet());
		return idToOutfitMap.get(id);
	}
	
	public static String getIdFromOutfitType(AbstractOutfit outfitsType) {
		return outfitsToIdMap.get(outfitsType);
	}
	
	public static List<AbstractOutfit> getAllOutfits() {
		return allOutfits;
	}

	public static List<AbstractOutfit> getModdedOutfits() {
		return moddedOutfits;
	}

	static {
		
		allOutfits = new ArrayList<>();
		
		// Load in modded outfits:
		moddedOutfits = new ArrayList<>();
		File dir = new File("res/mods");
		
		if (dir.exists() && dir.isDirectory()) {
			File[] modDirectoryListing = dir.listFiles();
			if (modDirectoryListing != null) {
				for (File modAuthorDirectory : modDirectoryListing) {
					File modAuthorOutfitDirectory = new File(modAuthorDirectory.getAbsolutePath()+"/outfits");
					
//					File[] outfitsDirectoriesListing = modAuthorOutfitDirectory.listFiles();
//					if (outfitsDirectoriesListing != null) {
//						for (File outfitsDirectory : outfitsDirectoriesListing) {
							if (modAuthorOutfitDirectory.isDirectory()){
								File[] innerDirectoryListing = modAuthorOutfitDirectory.listFiles((path, filename) -> filename.endsWith(".xml"));
								if (innerDirectoryListing != null) {
									for (File innerChild : innerDirectoryListing) {
										try{
											AbstractOutfit ct = new AbstractOutfit(innerChild) {};
											moddedOutfits.add(ct);
											String id = modAuthorDirectory.getName()+"_"+innerChild.getParentFile().getName()+"_"+innerChild.getName().split("\\.")[0];
//											System.out.println(id);
											outfitsToIdMap.put(ct, id);
											idToOutfitMap.put(id, ct);
											
										} catch(XMLLoadException ex){ // we want to catch any errors here; we shouldn't want to load any mods that are invalid as that may cause severe bugs
											System.err.println(ex);
											System.out.println(ex); // temporary, I think mod loading failure should be displayed to player on screen
										}
									}
//								}
//							}
						}
					}
				}
			}
		}
		
		allOutfits.addAll(moddedOutfits);
		
		
		// Add in external res outfits:
		
		dir = new File("res/outfits");
		
		if (dir.exists() && dir.isDirectory()) {
			File[] authorDirectoriesListing = dir.listFiles();
			if (authorDirectoriesListing != null) {
				for (File authorDirectory : authorDirectoriesListing) {
					if (authorDirectory.isDirectory()){
						File[] innerDirectoryListing = authorDirectory.listFiles((path, filename) -> filename.endsWith(".xml"));
						if (innerDirectoryListing != null) {
							for (File innerChild : innerDirectoryListing) {
								try {
									AbstractOutfit ct = new AbstractOutfit(innerChild) {};
									allOutfits.add(ct);
									String id = authorDirectory.getName()+"_"+innerChild.getParentFile().getName()+"_"+innerChild.getName().split("\\.")[0];
//									System.out.println(id);
									outfitsToIdMap.put(ct, id);
									idToOutfitMap.put(id, ct);
								} catch(Exception ex) {
									System.err.println("Loading modded outfits failed at 'OutfitType' Code 2. File path: "+innerChild.getAbsolutePath());
									System.err.println("Actual exception: ");
									ex.printStackTrace(System.err);
								}
							}
						}
					}
				}
			}
		}
		
	}
}
