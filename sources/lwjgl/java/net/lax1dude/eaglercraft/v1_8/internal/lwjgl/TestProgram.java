package net.lax1dude.eaglercraft.v1_8.internal.lwjgl;

import net.lax1dude.eaglercraft.v1_8.Display;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;

public class TestProgram {
	
	private static boolean grab = false;

	public static void main_(String[] args) {
		
		while(!Display.isCloseRequested()) {
			
			Keyboard.enableRepeatEvents(true);
			Display.update();
			
			while(Keyboard.next()) {
				if(Keyboard.getEventKey() == KeyboardConstants.KEY_E && Keyboard.getEventKeyState()) {
					grab = !grab;
					Mouse.setGrabbed(grab);
				}
			}
			
			System.out.println("" + Mouse.getDX() + ", " + Mouse.getDY());
			
			EagUtils.sleep(100l);
		}
		
		EagRuntime.destroy();
		
	}

}
