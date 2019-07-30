/**
All rights Reserved, Designed By www.aug.cloud
LocationSerialize.java
@Package net.augcloud.arisa.saboteur.behavior_instruction
@Description:
@author: Arisa
@date:   2019年7月26日 下午1:53:55
@version V1.0
@Copyright: 2019
*/
package net.augcloud.arisa.saboteur.behavior_instruction;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.google.gson.Gson;

/**
@author Arisa
@date 2019年7月26日 下午1:53:55*/
public class LocationSerialize {

	private Map<String, Object> beSerializedata = new HashMap<>();

	public LocationSerialize(Location loc) {
		this.beSerializedata = loc.serialize();
	}

	public String Serializedata2String() {
		return new Gson().toJson(this.beSerializedata);
	}

	public static Location String2Location(String json) {
		return Location.deserialize((new Gson().fromJson(json, Map.class)));
	}

}
