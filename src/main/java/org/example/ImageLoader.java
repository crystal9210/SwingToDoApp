package org.example;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static java.net.URLEncoder.encode;

class ImageLoader {
    public static Image loadImage(String path) throws IOException {
        String jarDir = getJarDir(ImageLoader.class);
        System.out.println("Jar directory: " + jarDir);

        File file = new File(jarDir, path);
        System.out.println("File: " + file);

        if (file.exists()) {
            System.out.println("File exists.");
        } else {
            System.out.println("File does not exist.");
        }

        Image image = ImageIO.read(file);
        return image;
    }

    public static String getJarDir(Class aclass) {
        URL url;
        String extURL;

        url = aclass.getProtectionDomain().getCodeSource().getLocation();
        extURL = url.toExternalForm();

        if (extURL.endsWith(".jar"))
            extURL = extURL.substring(0, extURL.lastIndexOf("/"));

        try {
            URI uri = new URI(extURL);
            return uri.getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}


