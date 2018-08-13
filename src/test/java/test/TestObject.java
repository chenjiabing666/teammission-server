package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.techwells.teammission.domain.ProjectImage;

public class TestObject {
	@Test
	public void test1(){
		ProjectImage projectImage=new ProjectImage();
		projectImage.setImageId(10);;
		Integer a=10;
		Integer b=10;
//		System.err.println(projectImage.getImageId()==a);
		System.out.println(a==b);
		System.out.println(a.equals(b));
	}
}
