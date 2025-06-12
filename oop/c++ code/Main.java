

import java.io.File;

public class Main {
    static void print(String s) {
        System.out.println(s);
    }

    public static void main(String[] args) {
        File f1 = new File("/", "tmp");
        if (f1.exists()) {
            print(f1.getName() + " exists");
        } else {
            print(f1.getName() + " does not exist");
        }
        File f2 = new File("/");
        print("FileName: " + f1.getName());
        print("Path: " + f1.getPath());
        print("Absolute Path: " + f1.getAbsolutePath());
        print("Parent: " + f1.getParent());
        print("Size: " + f1.length());
        print("Last Modified: " + f1.lastModified());
        print("Is Directory: " + f1.isDirectory());
        print("Is File: " + f1.isFile());
        print("FreeSpace: " + f1.getFreeSpace());
        print("Usable Space: " + f1.getUsableSpace());
        String[] files = f2.list();
        for (String file : files) {
            print(file);
        }
    }
}