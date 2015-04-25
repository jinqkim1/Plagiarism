package com.kdars.HotCheetos.DocumentStructure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;

public class ObjectFileConverter<Type> {
	/**
	 * @author JS
	 * @param obj - target object
	 * @param filePath - savedfilename
	 * @return success : true, fail : false. if you want specific reason, view your printed-stack.
	 */
	public boolean object2File(Type obj, FSDataOutputStream fos){
		boolean ret = false;
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			
			ret = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * @author JS
	 * @param filePath - target file.
	 * @return if fail, return null. if you want specific reason, view your printed-stack.
	 */
	public Type file2Object(FSDataInputStream fis){
		Type ret = null;
		
		try {
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			ret = (Type)ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
}
