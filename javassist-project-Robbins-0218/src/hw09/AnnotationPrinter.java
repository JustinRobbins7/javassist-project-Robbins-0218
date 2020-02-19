package hw09;

import java.lang.reflect.Proxy;
import java.util.Iterator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;
import util.UtilMenu;

public class AnnotationPrinter {

	public static void main(String[] args0) {
		String[] input;
		boolean repeat = false;
		do {
			System.out.println("===================================================================");
			System.out.println("HW09 - Please enter a class, and two annotations.                  ");
			System.out.println("Separate these values with commas.                                 ");
			System.out.println("===================================================================");

			input = UtilMenu.getArguments();
			repeat = false;

			if (input.length != 3) {
				repeat = true;
				System.out.println("[WRN] Invalid Input!!");
			}
		} while (repeat);

		String classname = "target." + input[0];
		String anno1 = input[1];
		String anno2 = input[2];

		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass ct = pool.get(classname);
			CtField[] fields = ct.getFields();
			for (int i = 0; i < fields.length; i++) {
				Object[] annoList = fields[i].getAnnotations();
				process(annoList, "target." + anno1, "target." + anno2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void process(Object[] annoList, String anno1, String anno2) {
		
		boolean printA2 = false;
		for(int i = 0; i < annoList.length; i++) {
			Annotation annotation = getAnnotation(annoList[i]);
			if (annotation.getTypeName().contentEquals(anno1)) {
				printA2 = true;
			}
		}
		
		if(printA2) {
			for(int i = 0; i < annoList.length; i++) {
				Annotation annotation = getAnnotation(annoList[i]);
				if (annotation.getTypeName().contentEquals(anno2)) {
					showAnnotation(annotation);
				}
			}
		}
	}

	static Annotation getAnnotation(Object obj) {
		// Get the underlying type of a proxy object in java
		AnnotationImpl annotationImpl = //
				(AnnotationImpl) Proxy.getInvocationHandler(obj);
		return annotationImpl.getAnnotation();
	}

	static void showAnnotation(Annotation annotation) {
		Iterator<?> iterator = annotation.getMemberNames().iterator();
		while (iterator.hasNext()) {
			Object keyObj = (Object) iterator.next();
			MemberValue value = annotation.getMemberValue(keyObj.toString());
			System.out.print(keyObj + ": " + value);
			
			if(iterator.hasNext()) {
				System.out.print(", ");
			}
			else {
				System.out.println();
			}
		}
	}
}
