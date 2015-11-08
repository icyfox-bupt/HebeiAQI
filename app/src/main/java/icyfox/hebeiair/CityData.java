package icyfox.hebeiair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CityData implements Serializable{
	
	/**
	 * 继承Serializable是为了在两个不同的界面中进行值的传递
	 */
	private static final long serialVersionUID = -8473485404751986234L;
	//城市类包含了一个城市的数据
	public String name,dataTime,aqi,level,maxPoll,color,intro,tips;
	public List<Pointer> pointerList;

	public CityData(Node cityNode) {
		super();
		//按标签挨个取出相应标签的内容
		this.name = getByTag(cityNode, "name");
		this.dataTime = getByTag(cityNode, "datatime");
		this.aqi = getByTag(cityNode, "aqi");
		this.level = getByTag(cityNode, "level");
		this.maxPoll = getByTag(cityNode, "maxpoll");
		String tmp = getByTag(cityNode, "color");
		this.color = tmp.replace("0x", "#");
		this.intro = getByTag(cityNode, "intro");
		this.tips = getByTag(cityNode, "tips");
		
		Element city = (Element)cityNode;
		NodeList pointers = city.getElementsByTagName("Pointer");
		
		//向city的pointer列表中添加监测点
		pointerList = new ArrayList<>();
		for (int i=0;i<pointers.getLength();i++){
			Node pNode = pointers.item(i);
			pointerList.add(new Pointer(pNode));
		}
	}

	//从XML的node中取出相应标签中内容的function
	private String getByTag(Node node,String tag) {
		for (int i=0;i<node.getChildNodes().getLength();i++){
			if (tag.equalsIgnoreCase(node.getChildNodes().item(i).getNodeName()))
				return node.getChildNodes().item(i).getTextContent();
		}
		return null;
	}
}
