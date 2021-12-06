package proyecto2021G03.appettit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.ejb.Singleton;

import org.jboss.logging.Logger;

@Singleton
public class FileManagement implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(FileManagement.class);
	
	public byte[] getFileAsByteArray(String path) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		byte[] array = null;
		InputStream is = null;

		URL resource = classLoader.getResource(path);
		File fileToByte = new File(resource.getFile());

		try {
			is = new FileInputStream(fileToByte);
			 array = new byte[is.available()];
		     is.read(array);
		     
		} catch (FileNotFoundException e) {
			logger.error(e.getLocalizedMessage());
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
       
        return array;

	}
	
}
