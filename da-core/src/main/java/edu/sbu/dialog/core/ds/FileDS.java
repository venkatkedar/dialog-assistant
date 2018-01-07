package edu.sbu.dialog.core.ds;

import java.io.File;
import java.util.List;

public interface FileDS {
	File[] getDirectories(File path);
	File[] getFiles(File path, String extension);
	String[] getFileNames(File path,String extension);
	List<String> getAllFileNames(File path,String extension);
	String readFile(File location, String f);
}
