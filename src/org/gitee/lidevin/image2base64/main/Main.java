package org.gitee.lidevin.image2base64.main;

import java.awt.EventQueue;

import org.gitee.lidevin.image2base64.view.MainWindow;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				new MainWindow().display();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	}
	
}

