package kr.ac.uos.ai.annotator.taskarchiver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * This class is written by Chan Yeon, Cho
 * AI-Laboratory, Seoul, Korea
 * 2015. 9. 14.
 */

public class TaskPacker {

	private String packedJarPath;
	private FileOutputStream fout;
	private JarOutputStream jarOut;
	private byte[] data;
	private ArrayList<String> entryList;
	private String tempJarFilePath;

	public TaskPacker() {
	}



	public void init() {
		try {
			fout = new FileOutputStream(packedJarPath);
			jarOut = new JarOutputStream(fout);
		} catch (FileNotFoundException e) {
			System.out.println("Make FileOutputStream Failed");
		} catch (IOException e) {
			System.out.println("Make JarOutputStream Failed");
		}
	}

	public void addTaskElement(String path) {
        if(entryList == null){
			entryList = new ArrayList<String>();
        }
		entryList.add(path);
	}

    public void entryListClear() {
        entryList.clear();
    }

	public void addEntry(String classPath) {
			try {
				jarOut.putNextEntry(new ZipEntry(classPath));
				jarOut.write(classPath.getBytes());
			} catch (IOException e) {
				System.out.println("Package Path Added Failed");
			} finally {
				try {
					jarOut.closeEntry();
				} catch (IOException e) {
					System.out.println("Jar Out Close Failed");
				}
			}
	}


	public void packTask(String outputFilePath) {
        entryAnalysis();
		try {
			jarOut.close();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private void entryAnalysis() {
        for(String s : entryList) {
            addEntry(s);
        }
    }


    public byte[] file2Byte(String jarPath) {
		Path path = Paths.get(jarPath);
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

//	public void byte2File(String outputFilePath) {
//		byte[] byteAraay = data;
//		TaskUnpacker fm = new TaskUnpacker();
//		fm.init();
//		fm.makeFileFromByteArray(outputFilePath, byteAraay);
//	}

//	@Test
//	public void test3() {
//		byte[] byteArray = data;
//		TaskUnpacker fM = new TaskUnpacker();
//		fM.init();
//		fM.makeFileFromByteArray("F:/jartest/ClientTest/asdf.jar", byteArray);
//	}


	public void setPackedTaskPath(String packedJarFilePath) {
		this.packedJarPath = packedJarFilePath;
	}

	public void setTempJarFilePath(String tempJarFilePath) {
		this.tempJarFilePath = tempJarFilePath;
	}
}
