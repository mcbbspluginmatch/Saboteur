/**  
All rights Reserved, Designed By www.aug.cloud
Utils.java   
@Package net.augcloud.arisa.advancedquest   
@Description: 
@author: Arisa   
@date:   2018��7��17�� ����6:49:24   
@version V1.0 
@Copyright: 2018 
*/
package net.augcloud.arisa.saboteur;

import java.util.List;

/**
@author Arisa
@date 2018��7��17�� ����6:49:24*/
public class Utils {

	/**   
	isNumber   
	@Description: �ж��ַ����ǲ�������
	@param str
	@return     
	boolean*/
	public boolean isNumber(String str) {
		return str != null && str.equals("") ? str.matches("^[0-9]*$") : false;
	}

	/**   
	ReplaceColour   
	@Description: ����&��Ϊ����ַ���
	@param str
	@return     
	String*/
	public String ReplaceColour(String str) {
		return str.replaceAll("&", "��");
	}

	/**   
	ReplaceColour   
	@Description: ����&��Ϊ����ַ���
	@param str
	@return     
	String*/
	public static String _ReplaceColour(String str) {
		return str.replaceAll("&", "��");
	}

	/**   
	ReplaceColour   
	@Description: ����&��Ϊ��ļ���
	@param list
	@return     
	List<String>*/
	public List<String> ReplaceColour(List<String> list) {
		for (int i = 0, size = list.size(); i < size; i++ )
			list.set(i, list.get(i).replaceAll("&", "��"));
		return list;
	}

}
