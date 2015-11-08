package icyfox.hebeiair;

import java.io.Serializable;

import org.w3c.dom.Node;

public class Pointer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String city,region,name,dataTime,aqi,level,maxPoll,color,intro,tips;

	public Pointer(Node node) {
		super();
		this.city = getByTag(node, "city");
		this.region = getByTag(node, "region");
		this.name = getByTag(node, "name");
		this.dataTime = getByTag(node, "datatime");
		this.aqi = getByTag(node, "aqi");
		this.level = getByTag(node, "level");
		this.maxPoll = getByTag(node, "maxpoll");
		this.color = getByTag(node, "color").replace("0x", "#");
		this.intro = getByTag(node, "intro");
		this.tips = getByTag(node, "tips");
	}
	
	
	@Override
	public String toString() {
		return "Pointer [city=" + city + ", region=" + region + ", name="
				+ name + ", dataTime=" + dataTime + ", aqi=" + aqi + ", level="
				+ level + ", maxPoll=" + maxPoll + ", color=" + color
				+ ", intro=" + intro + ", tips=" + tips + "]";
	}



	private String getByTag(Node node,String tag) {
		for (int i=0;i<node.getChildNodes().getLength();i++){
			if (tag.equalsIgnoreCase(node.getChildNodes().item(i).getNodeName()))
				return node.getChildNodes().item(i).getTextContent();
		}
		return null;
	}
}
