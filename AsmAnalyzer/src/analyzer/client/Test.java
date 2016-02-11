package analyzer.client;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import analyzer.common.ClassNameStandardizer;

public class Test {

	public static void main(String[] args) {
	   ArrayList<String> test = new ArrayList<String>();
	   test.add("test");
	   String test2 = "test";
	   Type ret = null;
	   System.out.println(isCollectionOfObject(test, 
	   test2.getClass().toString()));
	}

	
	public static <T> boolean isCollectionOfObject(Collection<T> c, String className){
		for(Object co : c){
			if(!(ClassNameStandardizer.standardize(co.getClass().toString()).equals(className)))
				return false;
		}
		return true;
	}
}
