/**
All rights Reserved, Designed By www.aug.cloud
AntiAttackValueBlockData.java
@Package net.augcloud.arisa.saboteur.behavior_instruction
@Description:
@author: Arisa
@date:   2019年7月25日 下午10:48:28
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import net.augcloud.arisa.saboteur.SetFiles;

/**
@author Arisa
@date 2019年7月25日 下午10:48:28*/
public class AntiAttackValueBlockData {

	private HashMap<Material, Integer> data = new HashMap<>();
	private int DefaultValue = 50;

	public AntiAttackValueBlockData() {
		ConfigurationSection ConfigSection = SetFiles.getValueSetup().getConfigurationSection("BlockAntiAttackValue");
		Set<String> Keys = ConfigSection.getKeys(false);
		this.data.clear();
		Iterator<String> _iter = Keys.iterator();
		while (_iter.hasNext()) {
			String v = _iter.next();
			this.data.put(Material.getMaterial(v), new Integer(ConfigSection.getInt(v)));
		}
		this.DefaultValue = SetFiles.getValueSetup().getInt("BlockDefaultValue");
	}

	//获取方块防御力
	public BlockBreakQualityMeta AccordingtoBreakQuality(Block block) {
		if (block == null) return null;
		Material id = block.getBlockData().getMaterial();
		if (this.data.containsKey(id)) {
			BlockBreakQualityMeta _qualitymeta = new BlockBreakQualityMeta();
			_qualitymeta.setHasAntiAttackValue(true);
			_qualitymeta.setCanbebreken(true);
			_qualitymeta.setId(id);
			_qualitymeta.setAntiAttackValue(this.data.get(id).intValue());
			return _qualitymeta;
		}

		BlockBreakQualityMeta _qualitymeta = new BlockBreakQualityMeta();
		_qualitymeta.setHasAntiAttackValue(true);
		_qualitymeta.setCanbebreken(true);
		_qualitymeta.setId(id);
		_qualitymeta.setAntiAttackValue(this.DefaultValue);
		return _qualitymeta;
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
		result = (prime * result) + this.DefaultValue;
		result = (prime * result) + ((this.data == null) ? 0 : this.data.hashCode());
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
		if (this == obj) return true;
		if (! (obj instanceof AntiAttackValueBlockData)) return false;
		AntiAttackValueBlockData other = (AntiAttackValueBlockData) obj;
		if (this.DefaultValue != other.DefaultValue) return false;
		if (this.data == null) {
			if (other.data != null) return false;
		} else if (! this.data.equals(other.data)) return false;
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
		builder.append("AntiAttackValueBlockData [");
		if (this.data != null) {
			builder.append("data=");
			builder.append(this.data);
			builder.append(", ");
		}
		builder.append("DefaultValue=");
		builder.append(this.DefaultValue);
		builder.append("]");
		return builder.toString();
	}

	/**
	getData
	@Description: 外部成员通过getter访问getData字段
	@return: HashMap<Material,Integer>*/
	public HashMap<Material, Integer> getData() {
		return this.data;
	}

	/**
	@Title:  setData
	@Description: 外部成员通过setter方法修改字段
	@return: HashMap<Material,Integer>*/
	public void setData(HashMap<Material, Integer> data) {
		this.data = data;
	}

	/**
	getDefaultValue
	@Description: 外部成员通过getter访问getDefaultValue字段
	@return: int*/
	public int getDefaultValue() {
		return this.DefaultValue;
	}

	/**
	@Title:  setDefaultValue
	@Description: 外部成员通过setter方法修改字段
	@return: int*/
	public void setDefaultValue(int defaultValue) {
		this.DefaultValue = defaultValue;
	}

}
