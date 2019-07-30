/**  
All rights Reserved, Designed By www.aug.cloud
AttackValueLoreData.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019年7月25日 下午10:47:11   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.augcloud.arisa.saboteur.SetFiles;

/**
@author Arisa
@date 2019年7月25日 下午10:47:11*/
public class AttackValueLoreData {

	private HashMap<String, Integer> data = new HashMap<>();

	public AttackValueLoreData() {
		ConfigurationSection ConfigSection = SetFiles.getValueSetup().getConfigurationSection("ItemAttackValueLore");
		Set<String> Keys = ConfigSection.getKeys(false);
		this.data.clear();
		Iterator<String> _iter = Keys.iterator();
		while (_iter.hasNext()) {
			String v = _iter.next();
			this.data.put(v, new Integer(ConfigSection.getInt(v)));
		}
	}

	//获取武器攻击值
	public AttackQualityMeta AccordingtoAttackValue(ItemStack item) {
		if (item == null || ! item.hasItemMeta()) return null;
		ItemMeta id = item.getItemMeta();
		if (! id.hasLore()) return null;
		List<String> Lore = id.getLore();
		for (String str : Lore)
			if (this.data.containsKey(str)) {
				AttackQualityMeta _qualitymeta = new AttackQualityMeta();
				_qualitymeta.setHasAttackValue(true);
				_qualitymeta.setAttackValue(this.data.get(str));
				return _qualitymeta;
			}
		return null;
	}

	/**  
	getData 
	@Description: 外部成员通过getter访问getData字段
	@return: HashMap<String,Integer>*/
	public HashMap<String, Integer> getData() {
		return this.data;
	}

	/**  
	@Title:  setData
	@Description: 外部成员通过setter方法修改字段
	@return: HashMap<String,Integer>*/
	public void setData(HashMap<String, Integer> data) {
		this.data = data;
	}

	/**   
	hashCode   
	@Description:
	@return   
	@see java.lang.Object#hashCode()*/
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.data == null) ? 0 : this.data.hashCode());
		return result;
	}

	/**   
	equals   
	@Description:
	@param obj
	@return   
	@see java.lang.Object#equals(java.lang.Object)*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (! (obj instanceof AttackValueLoreData)) { return false; }
		AttackValueLoreData other = (AttackValueLoreData) obj;
		if (this.data == null) {
			if (other.data != null) { return false; }
		} else if (! this.data.equals(other.data)) { return false; }
		return true;
	}

	/**   
	toString   
	@Description:
	@return   
	@see java.lang.Object#toString()*/
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttackValueLoreData [");
		if (this.data != null) {
			builder.append("data=");
			builder.append(this.data);
		}
		builder.append("]");
		return builder.toString();
	}

}
