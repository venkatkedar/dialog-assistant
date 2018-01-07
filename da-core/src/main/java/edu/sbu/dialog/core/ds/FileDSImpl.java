package edu.sbu.dialog.core.ds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * this class performs all the file IO operations
 * @author Kedar
 *
 */
public class FileDSImpl implements FileDS {

	public File[] getDirectories(File path) {
		File[] directories = path.listFiles(new FileFilter() {
			  
			public boolean accept(File pathname) {

				return pathname.isDirectory();
			}
		});
		return directories;
	}

	public File[] getFiles(File location, final String extension) {
		File[] files = location.listFiles(new FileFilter() {
			  
			public boolean accept(File pathname) {
				String ext=pathname.getName().substring(pathname.getName().lastIndexOf("."));
				return ext!=null&&!"".equals(ext)&&ext.contains(extension);
			}
		});
		return files;
	}

	public String[] getFileNames(File location, final String extension) {
		String[] files = location.list(new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				String ext=name.substring(name.lastIndexOf("."));
				return ext!=null&&!"".equals(ext)&&ext.contains(extension);
			}
		});
		return files;
	}

	public List<String> getAllFileNames(File path,final String extension) {
		File[] directories=getDirectories(path);
		List<String> filenames=new ArrayList<String>(17600);
		for(File dir:directories) {
			String[] files=getFileNames(dir, extension);
			filenames.addAll(Arrays.asList(files));
		}
		return filenames;
	}

	
	public String readFile(File location,String file) {
		String contents=null;
		BufferedReader br=null;
		File f=searchFile(location, file);
		if(f.exists()) {
			try {
				br=new BufferedReader(new FileReader(f));
				String line=null;
				StringBuilder sb=new StringBuilder();
				while((line=br.readLine())!=null) 
					sb.append(line);
				contents=sb.toString();
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(br!=null)
					try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		return contents;
	}
	
	public File searchFile(File location,String file) {
		
		File[] dirs=getDirectories(location);
		File f=null;
		for(File dir:dirs) {
			File[] files=dir.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.substring(0,name.lastIndexOf(".")).equals(file);
				}
			});
			if(files.length>0) {
				f=files[0];
				System.out.println("file found");
				break;
			}	
		}
		return f;
	}
	
}
