package edu.warbot.FSMEditor;

import java.util.ArrayList;

public class IntrospectionFSM {
	
	public static ArrayList<Class> getAllClassStateFSM(){
		ArrayList<Class> listClass = new ArrayList<Class>();
		
		for (int i = 0; i < Configuration.PLAN.length; i++) {
			String className = Configuration.FSM_CLASS_PATH + "." + Configuration.PLAN[i];
			try {
				listClass.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.err.println("Impossible to find class " + className + ". Check Configuration class");
			}
		}
		return listClass;
	}

	public static ArrayList<String> getAllClassStateFSMSimpleName() {
		ArrayList<String> className = new ArrayList<String>();
		
		for (int i = 0; i < Configuration.PLAN.length; i++) {
			className.add(Configuration.PLAN[i]);
		}
		return className;
	}

}
