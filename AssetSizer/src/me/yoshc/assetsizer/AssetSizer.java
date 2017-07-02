package me.yoshc.assetsizer;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AssetSizer {
	
	private static final String[][] map = {
			{"xxxhdpi", "192"},
			{"xxhdpi", "144"},
			{"xhdpi", "96"},
			{"hdpi", "72"},
			{"mdpi", "48"},
	};
	
	public static void main(String[] args) {
		if(args.length < 1) {
			System.err.println("Please specify a file name.");
			System.err.println("Usage: java -jar AssetSizer [filename]");
			return;
		}
		String filename = args[0];
		File file = new File(filename);
		if(!file.exists()) {
			System.err.println("File does not exist. Please specify a valid file name.");
			return;
		}
		if(file.isDirectory()) {
			System.err.println("File is a directory. Please specify a valid file name.");
			return;
		}
		
		BufferedImage srcImage = null;
		
		try {
			srcImage = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("IOException!");
			return;
		}
		
		if(srcImage == null) {
			System.err.println("Failed to load image.");
			return;
		}
		
		for(int i = 0; i < map.length; i++) {
			String[] rule = map[i];
			String dirName = rule[0];
			int resolution = Integer.valueOf(rule[1]);
			
			System.out.println("Directory: " + dirName);
			System.out.println("Resolution: " + resolution);
			System.out.println("> Creating .. ");
			
			File dir = new File(dirName);
			dir.mkdir();
			
			File newFile = new File(dir.getAbsolutePath() + "//" + file.getName());
			BufferedImage newImage = resizeImg(srcImage, resolution, resolution);
			
			String extension = "";
			int index = filename.lastIndexOf('.');
			extension = filename.substring(index+1);
			if(extension.length() < 1) {
				System.err.println("Failed parsing file extension.");
				return;
			}
			try {
				newFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				System.err.println("Failed to create new file.");
			}
			try {
				ImageIO.write(newImage, extension.toUpperCase(), newFile);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to write to new file.");
			}
			
			
			System.out.println("> Done!");
			
		}
		
	}
	
	// https://stackoverflow.com/questions/15558202/how-to-resize-image-in-java
	public static BufferedImage resizeImg(BufferedImage img, int newW, int newH) {
	    int w = img.getWidth();
	    int h = img.getHeight();
	    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
	    Graphics2D g = dimg.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
	    g.dispose();
	    return dimg;      
   }
	
}