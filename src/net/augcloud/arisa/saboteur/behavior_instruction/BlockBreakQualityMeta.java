/**  
All rights Reserved, Designed By www.aug.cloud
AttackQualityMeta.java   
@Package net.augcloud.arisa.saboteur.behavior_instruction   
@Description: 
@author: Arisa   
@date:   2019年7月26日 上午12:05:46   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import org.bukkit.Material;

/**
@author Arisa
@date 2019年7月26日 上午12:05:46*/
public class BlockBreakQualityMeta {

	//全局设定
	private boolean canbebreken = false;

	private boolean hasAntiAttackValue = false;
	private Material id = null;
	private int AntiAttackValue = 0;

	public BlockBreakQualityMeta() {

	}

	/**  
	isCanbebreken 
	@Description: 外部成员通过getter访问isCanbebreken字段
	@return: boolean*/
	public boolean isCanbebreken() {
		return this.canbebreken;
	}

	/**  
	@Title:  setCanbebreken
	@Description: 外部成员通过setter方法修改字段
	@return: boolean*/
	public void setCanbebreken(boolean canbebreken) {
		this.canbebreken = canbebreken;
	}

	/**  
	isHasAntiAttackValue 
	@Description: 外部成员通过getter访问isHasAntiAttackValue字段
	@return: boolean*/
	public boolean isHasAntiAttackValue() {
		return this.hasAntiAttackValue;
	}

	/**  
	@Title:  setHasAntiAttackValue
	@Description: 外部成员通过setter方法修改字段
	@return: boolean*/
	public void setHasAntiAttackValue(boolean hasAntiAttackValue) {
		this.hasAntiAttackValue = hasAntiAttackValue;
	}

	/**  
	getAntiAttackValue 
	@Description: 外部成员通过getter访问getAntiAttackValue字段
	@return: int*/
	public int getAntiAttackValue() {
		return this.AntiAttackValue;
	}

	/**  
	@Title:  setAntiAttackValue
	@Description: 外部成员通过setter方法修改字段
	@return: int*/
	public void setAntiAttackValue(int antiAttackValue) {
		this.AntiAttackValue = antiAttackValue;
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
		result = prime * result + this.AntiAttackValue;
		result = prime * result + (this.canbebreken ? 1231 : 1237);
		result = prime * result + (this.hasAntiAttackValue ? 1231 : 1237);
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
		if (! (obj instanceof BlockBreakQualityMeta)) { return false; }
		BlockBreakQualityMeta other = (BlockBreakQualityMeta) obj;
		if (this.AntiAttackValue != other.AntiAttackValue) { return false; }
		if (this.canbebreken != other.canbebreken) { return false; }
		if (this.hasAntiAttackValue != other.hasAntiAttackValue) { return false; }
		if (this.id != other.id) { return false; }
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
		builder.append("BlockBreakQualityMeta [canbebreken=");
		builder.append(this.canbebreken);
		builder.append(", hasAntiAttackValue=");
		builder.append(this.hasAntiAttackValue);
		builder.append(", ");
		if (this.id != null) {
			builder.append("id=");
			builder.append(this.id);
			builder.append(", ");
		}
		builder.append("AntiAttackValue=");
		builder.append(this.AntiAttackValue);
		builder.append("]");
		return builder.toString();
	}

	/**  
	getId 
	@Description: 外部成员通过getter访问getId字段
	@return: Material*/
	public Material getId() {
		return this.id;
	}

	/**  
	@Title:  setId
	@Description: 外部成员通过setter方法修改字段
	@return: Material*/
	public void setId(Material id) {
		this.id = id;
	}

}
