/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.shadebob.skineditor;

import java.io.File;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * To use: please put fonts into the a folder called "fonts" in the resource folder
 * @author Yanick Bourbeau
 * @author billzo
 */
public class FontFinder {

	private Array<String> fontPaths = new Array<String>();
	public HashMap<String, File> fonts = new HashMap<String, File>();
	
	/**
	 * 
	 */
	public FontFinder() {
	
		String osName = System.getProperty("os.name");
		if (osName.equals("Linux")) {
			
			fontPaths.add("/usr/share/fonts/");
			fontPaths.add("~/.fonts/");
			fontPaths.add("resources/fonts/");
			
		} else if (osName.contains("Windows")) { 
			
			fontPaths.add("c:\\Windows\\Fonts\\");
			fontPaths.add("resources\\fonts\\");
			
		} else if (osName.contains("Mac")) {

			fontPaths.add("/Network/Library/Fonts/");
			fontPaths.add("~/Library/Fonts/");
			fontPaths.add("resources/fonts/");
			
		}
		
	}
	
	/**
	 * Perform a search for fonts in the default folders
	 */
	private void searchForFonts(String fontPath) {
		File[] files = new File(fontPath).listFiles();
		if (files == null) {
			return;
		}
		for(File file : files) {
			System.out.println(file.getAbsolutePath());
			if (file.isDirectory() == true) {
				searchForFonts(file.getAbsolutePath());
			} else {
				String filename = file.getName();
				if (filename.toLowerCase().endsWith(".ttf")) {
					filename = filename.substring(0, filename.length() - 4);
					
					String newFilename = "";
					boolean previousIsUpper = false;
					for (int i=0;i<filename.length();i++) {
						
						if ((i != 0) && (Character.isUpperCase(filename.charAt(i)))) {
							if (previousIsUpper == false) {
								newFilename += " ";
								previousIsUpper = true;
							}
						} else {
							previousIsUpper = false;
						}
						newFilename += filename.charAt(i);
					}
					
					// Filter font name
					newFilename = newFilename.replace("/", "_");
					newFilename = newFilename.replace("\\", "_");
					newFilename = newFilename.replace(".", "_");

					fonts.put(newFilename, file);
				}
			}
		}
		
	}
	
	/**
	 * 	Refresh fonts by performing a search again
	 */
	public void refreshFonts() {
		
		fonts.clear();
		for(String fontPath : fontPaths) {
			
			searchForFonts(fontPath);
		}

		
		Gdx.app.log("SystemFonts","TTF Fonts found: " + fonts.size());
	}
	
	
}