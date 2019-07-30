/**  
All rights Reserved, Designed By www.aug.cloud
BlockState.java   
@Package net.augcloud.arisa.saboteur.sqlite   
@Description: 
@author: Arisa   
@date:   2019年7月27日 上午8:03:21   
@version V1.0 
@Copyright: 2019 
*/
package net.augcloud.arisa.saboteur.sqlite;

/**
@author Arisa
@date 2019年7月27日 上午8:03:21*/
public class BlockState {

	private boolean exist = false;
	private boolean beplunder = false;

	/**   
	BlockState   
	@Description:*/
	public BlockState() {
	}

	/**  
	isExist 
	@Description: 外部成员通过getter访问isExist字段
	@return: boolean*/
	public boolean isExist() {
		return this.exist;
	}

	/**  
	@Title:  setExist
	@Description: 外部成员通过setter方法修改字段
	@return: boolean*/
	public void setExist(boolean exist) {
		this.exist = exist;
	}

	/**  
	isBeplunder 
	@Description: 外部成员通过getter访问isBeplunder字段
	@return: boolean*/
	public boolean isBeplunder() {
		return this.beplunder;
	}

	/**  
	@Title:  setBeplunder
	@Description: 外部成员通过setter方法修改字段
	@return: boolean*/
	public void setBeplunder(boolean beplunder) {
		this.beplunder = beplunder;
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
		result = prime * result + (this.beplunder ? 1231 : 1237);
		result = prime * result + (this.exist ? 1231 : 1237);
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
		if (! (obj instanceof BlockState)) { return false; }
		BlockState other = (BlockState) obj;
		if (this.beplunder != other.beplunder) { return false; }
		if (this.exist != other.exist) { return false; }
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
		builder.append("BlockState [exist=");
		builder.append(this.exist);
		builder.append(", beplunder=");
		builder.append(this.beplunder);
		builder.append("]");
		return builder.toString();
	}

}
