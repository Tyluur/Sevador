package com.sevador.utility.test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Packs the seperate NPC definition XML files into one binary file.
 *
 * @author Emperor
 */
public class NPCDefinitionPacker {

    /**
     * The document builder instance.
     */
    private static DocumentBuilder builder;

    /**
     * The document builder factory instance.
     */
    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory
            .newInstance();

    /**
     * Runs the NPC definition packer manually.
     * @param args The arguments cast on runtime.
     */
    public static final void main(String... args) {
    	System.out.println("Packing NPC definitions...");
    	if (!pack()) {
    		System.out.println("Failed to pack NPC definitions [cause=File already existed: ./data/nodes/NPCDefinitions.bin].");
    		return;
    	}
    	System.out.println("Packed NPC definitions.");
    }
    
    /**
     * Packs the NPC definitions into one NPCDefinitions.bin file.
     * @return {@code True} if succesful, {@code false} if not.
     */
    public static boolean pack() {
        File file = new File("./data/nodes/NPCDefinitions.bin");
        if (file.exists()) {
            return false;
        }
        String directory = "C:/Users/Emperor/Dropbox/NDE/data/NPCs";
        RandomAccessFile raf = null;
        int count = 0;
        try {
            file.createNewFile();
            raf = new RandomAccessFile(file, "rw");
            for (int i = 0; i < 14362; i++) {
                File defs = new File(new StringBuilder(directory).append("/NPCDefinition").append(i).append(".xml").toString());
                if (!defs.exists()) {
                    raf.writeShort(-1);
                    continue;
                }
                raf.writeShort(i);
                writeBinary(raf, defs);
                count++;
            }
            raf.close();
            raf = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (raf != null) {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Packed " + count + " functional npc definitions.");
        return true;
    }

    /**
     * Reads data from an NPCDefinition xml file, and writes it on the NPCDefinitions.bin file.
     * @param raf The random access file instance used.
     * @param defs The npc definitions XML file.
     */
    private static void writeBinary(RandomAccessFile raf, File defs) {
        try {
            builder = FACTORY.newDocumentBuilder();
            Document doc = builder.parse(defs);
            NodeList dialogueList = doc.getDocumentElement().getChildNodes();
            raf.writeShort(Short.parseShort(dialogueList.item(1).getTextContent()));
            raf.writeBytes(dialogueList.item(3).getTextContent());
            raf.writeByte(0);
            for (int i = 5; i < 32; i += 2) {
                raf.writeShort(Short.parseShort(dialogueList.item(i).getTextContent()));
            }
            raf.writeShort(Short.parseShort(dialogueList.item(33).getTextContent()));
            raf.writeByte(Byte.parseByte(dialogueList.item(35).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(37).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(39).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(41).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(43).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(45).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(47).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(49).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(51).getTextContent()));
            raf.writeByte(Byte.parseByte(dialogueList.item(53).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(55).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(57).getTextContent()));
            raf.writeShort(Short.parseShort(dialogueList.item(59).getTextContent()));
            raf.writeByte(Boolean.parseBoolean(dialogueList.item(61).getTextContent()) ? 1 : 0);
            raf.writeByte(Boolean.parseBoolean(dialogueList.item(63).getTextContent()) ? 1 : 0);
            raf.writeByte(Boolean.parseBoolean(dialogueList.item(65).getTextContent()) ? 1 : 0);
            raf.writeByte(Boolean.parseBoolean(dialogueList.item(67).getTextContent()) ? 1 : 0);
            raf.writeByte(Boolean.parseBoolean(dialogueList.item(69).getTextContent()) ? 1 : 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}