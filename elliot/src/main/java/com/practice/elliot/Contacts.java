package com.practice.elliot;



import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

// The methods in this class are all STATIC
// it means you do not instantiate the class to use its methods
// i.e. you DO NOT do.. Contacts contacts = new Contacts();
// you call the methods with Contacts.methodName();
// PUBLIC methods are available to any class that imports this class (eg main)
// PRIVATE methods are only available to this class - they are helpers methods

@Component
public class Contacts {
    
    private static final String APP_DIR = "src";

    public static void saveToFile(Contact contact, String fileDir) {
        
        System.out.println("Contacts.saveToFile: Start");
        String fname = generateFileName();
        Path dir = getFileDirectory(fileDir);
        Path file = dir.resolve(fname);

        System.out.println("Contacts.saveToFile: Getting fname");
        while (Files.exists(file)) {
            fname = generateFileName();
            file = dir.resolve(fname);
        }

        System.out.println("Contacts.saveToFile: fname generated -- " + fname);

        String content = formatToWrite(contact);

        System.out.println("Contacts.saveToFile: content processed");

        try(BufferedWriter bw = Files.newBufferedWriter(file, Charset.forName("UTF-8"))) {
            System.out.println("Contacts.saveToFile: writing to file |" + content + "|");
            bw.write(content);
            bw.flush();
        } catch (IOException e) {System.out.println("error");}

        System.out.println("Contacts.saveToFile: file created in " + file.toString());
    }

    public static HashMap<String, Contact> getAllFiles(String fileDir) {
        Path dir = getFileDirectory(fileDir);
        Set<String> files = null;
        HashMap<String, Contact> names = new HashMap<>();

        try (Stream<Path> stream = Files.list(dir)) {   
            files =  stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {};
            
        for (String file : files) {
            Contact contact = getContact(file, fileDir);
            names.put(file, contact);
        }
        return names;
    }

    public static Contact getContact(String id, String fileDir) {
        Path dir = getFileDirectory(fileDir);
        Path file = dir.resolve(id);
        Contact contact = null;
        try {
            List<String> contents = Files.readAllLines(file);
            // System.out.println("contents" + contents);
            contact = new Contact();
            contact.setName(contents.get(0));
            contact.setEmail(contents.get(1));
            contact.setPhone(contents.get(2));
            contact.setDob(contents.get(3));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return contact;
    }

    public static boolean setupDirectory(String fileDir) {
        boolean res = false;
        Path dir = getFileDirectory(fileDir);

        if(dir.toFile().exists()) {
            res = true;
        } else {
            if(System.getProperty("os.name").contains("Windows")) {
                try {
                    Files.createDirectories(dir);
                } catch (IOException e) {
                    System.out.println("Failed to create directory");
                    res = false;
                }
            } else {
                try {
                    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwx---");
                    FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);
                    Files.createDirectories(dir, fileAttributes);
                    res = true;
                } catch (IOException e) {
                    System.out.println("Failed to create directory");
                    res = false;
                }
            }
        }
        return res;
    }

    private static Path getFileDirectory(String fileDir) {
        
        Path root = Paths.get(APP_DIR).toAbsolutePath();
        Path dir = Paths.get(root.toString() + fileDir);
        
        return dir;        
    }

    private static synchronized String generateFileName() {
    
        long max = Long.parseLong("ffffffff", 16);
        
        long randLong = ThreadLocalRandom.current().nextLong(0, max + 1);
    
        String fname = ("00000000" + Long.toHexString(randLong)).substring(8);
        return fname;
    };

    private static String formatToWrite(Contact contact) {
        String result = "";
        result += contact.getName() + "\n";
        result += contact.getEmail() + "\n";
        result += contact.getPhone() + "\n";
        result += contact.getDob() + "\n";
        return result;
    }
}
