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

/**
@author Arisa
@date 2019年7月26日 上午12:05:46*/
public class AttackQualityMeta {

	private boolean hasAttackValue = false;
	private int AttackValue = 0;

	public AttackQualityMeta() {

	}

	/**  
	isHasAttackValue 
	@Description: 外部成员通过getter访问isHasAttackValue字段
	@return: boolean*/
	public boolean isHasAttackValue() {
		return this.hasAttackValue;
	}

	/**  
	@Title:  setHasAttackValue
	@Description: 外部成员通过setter方法修改字段
	@return: boolean*/
	public void setHasAttackValue(boolean hasAttackValue) {
		this.hasAttackValue = hasAttackValue;
	}

	/**  
	getAttackValue 
	@Description: 外部成员通过getter访问getAttackValue字段
	@return: int*/
	public int getAttackValue() {
		return this.AttackValue;
	}

	/**  
	@Title:  setAttackValue
	@Description: 外部成员通过setter方法修改字段
	@return: int*/
	public void setAttackValue(int attackValue) {
		this.AttackValue = attackValue;
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
		result = prime * result + this.AttackValue;
		result = prime * result + (this.hasAttackValue ? 1231 : 1237);
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
		if (! (obj instanceof AttackQualityMeta)) { return false; }
		AttackQualityMeta other = (AttackQualityMeta) obj;
		if (this.AttackValue != other.AttackValue) { return false; }
		if (this.hasAttackValue != other.hasAttackValue) { return false; }
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
		builder.append("AttackQualityMeta [hasAttackValue=");
		builder.append(this.hasAttackValue);
		builder.append(", AttackValue=");
		builder.append(this.AttackValue);
		builder.append("]");
		return builder.toString();
	}

}
