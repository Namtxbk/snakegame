package com.handler;

import java.io.*;
import javax.swing.*;

public class TxtReader {
	private String[] str = new String[10];
	
	public TxtReader() {
		str[0] = getClass().getResource("/image/Food/food.png").toString();
		str[1] = getClass().getResource("/image/Food/poison5.png").toString();
		str[2] = getClass().getResource("/image/Other/portal1.png").toString();
		str[3] = getClass().getResource("/image/Beam/Hfirep5.png").toString();
		str[4] = getClass().getResource("/image/Obstacles/block5.png").toString();
	}
	
	public void helpReader(JLabel helpDisplay) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/document/help.txt"), "UTF-8"));
		String s = "";
		while(true) {
			String temp = br.readLine();
			if(temp == null)
				break;
			s += replaceString(temp);
		}
		helpDisplay.setText(s);
		br.close();
	}
	
	public void loreReader(JLabel loreDisplay) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/document/lore.txt"), "UTF-8"));
		while(true) {
			String s = br.readLine();
			if(s == null)
				break;
			loreDisplay.setText(loreDisplay.getText()+s);
		}
		br.close();
	}
	
	public void aboutUsReader(JLabel aboutUsDisplay) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/document/aboutus.txt"), "UTF-8"));
		while(true) {
			String s = br.readLine();
			if(s == null)
				break;
			aboutUsDisplay.setText(aboutUsDisplay.getText()+s);
		}
		br.close();
	}
	
	public String replaceString(String s) {
		int i = 0;
		while(str[i] != null) {
			s = s.replace("{name".concat(Integer.toString(i)).concat("}"), str[i]);
			i++;
			System.out.println("Check");
		}
		return s;
	}
}
