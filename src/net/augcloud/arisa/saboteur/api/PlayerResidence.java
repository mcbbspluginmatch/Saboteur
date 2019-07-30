/**
All rights Reserved, Designed By www.aug.cloud
PlayerResidence.java
@Package net.augcloud.arisa.saboteur.behavior_instruction
@Description:
@author: Arisa
@date:   2019��7��30�� ����12:59:04
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.api;

import org.bukkit.Location;

/**
@author Arisa
@date 2019��7��30�� ����12:59:04*/
public class PlayerResidence {

	protected Location loc;
	protected String name;

	/**
	PlayerResidence
	@Description:*/
	public PlayerResidence(Location _loc, String _name) {
		this.loc = _loc;
		this.name = _name;
	}

	/**
	getLoc
	@Description: �ⲿ��Աͨ��getter����getLoc�ֶ�
	@return: Location*/
	public Location getLoc() {
		return this.loc;
	}

	/**
	@Title:  setLoc
	@Description: �ⲿ��Աͨ��setter�����޸��ֶ�
	@return: Location*/
	public void setLoc(Location loc) {
		this.loc = loc;
	}

	/**
	getName
	@Description: �ⲿ��Աͨ��getter����getName�ֶ�
	@return: String*/
	public String getName() {
		return this.name;
	}

	/**
	@Title:  setName
	@Description: �ⲿ��Աͨ��setter�����޸��ֶ�
	@return: String*/
	public void setName(String name) {
		this.name = name;
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
		result = (prime * result) + ((this.loc == null) ? 0 : this.loc.hashCode());
		result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
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
		if (! (obj instanceof PlayerResidence)) return false;
		PlayerResidence other = (PlayerResidence) obj;
		if (this.loc == null) {
			if (other.loc != null) return false;
		} else if (! this.loc.equals(other.loc)) return false;
		if (this.name == null) {
			if (other.name != null) return false;
		} else if (! this.name.equals(other.name)) return false;
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
		builder.append("PlayerResidence [");
		if (this.loc != null) {
			builder.append("loc=");
			builder.append(this.loc);
			builder.append(", ");
		}
		if (this.name != null) {
			builder.append("name=");
			builder.append(this.name);
		}
		builder.append("]");
		return builder.toString();
	}

}
