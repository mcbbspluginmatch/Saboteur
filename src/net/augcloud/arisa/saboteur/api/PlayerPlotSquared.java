/**
All rights Reserved, Designed By www.aug.cloud
PlayerPlotSquared.java
@Package net.augcloud.arisa.saboteur.api
@Description:
@author: Arisa
@date:   2019年7月30日 下午4:18:30
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.api;

/**
@author Arisa
@date 2019年7月30日 下午4:18:30*/
public class PlayerPlotSquared implements APIOfPlayer {

	/**
	PlayerPlotSquared
	@Description:*/
	public PlayerPlotSquared(String _name, com.github.intellectualsites.plotsquared.plot.object.Location location) {
		this.name = _name;
		this.loc = location;
	}

	protected com.github.intellectualsites.plotsquared.plot.object.Location loc;
	protected String name;

	/**
	getLoc
	@Description: 外部成员通过getter访问getLoc字段
	@return: com.github.intellectualsites.plotsquared.plot.object.Location*/
	public com.github.intellectualsites.plotsquared.plot.object.Location getLoc() {
		return this.loc;
	}

	/**
	@Title:  setLoc
	@Description: 外部成员通过setter方法修改字段
	@return: com.github.intellectualsites.plotsquared.plot.object.Location*/
	public void setLoc(com.github.intellectualsites.plotsquared.plot.object.Location loc) {
		this.loc = loc;
	}

	/**
	getName
	@Description: 外部成员通过getter访问getName字段
	@return: String*/
	public String getName() {
		return this.name;
	}

	/**
	@Title:  setName
	@Description: 外部成员通过setter方法修改字段
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
		if (! (obj instanceof PlayerPlotSquared)) return false;
		PlayerPlotSquared other = (PlayerPlotSquared) obj;
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
		builder.append("PlayerPlotSquared [");
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