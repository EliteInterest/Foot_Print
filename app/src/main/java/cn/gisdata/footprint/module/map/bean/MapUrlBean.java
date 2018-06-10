package cn.gisdata.footprint.module.map.bean;

import java.io.Serializable;

/**
 * Created by Xiangb on 2018/5/31.
 * 功能：
 */
public class MapUrlBean implements Serializable {

    /**
     * Number : 2
     * Name : 影像底图
     * IconUrl : null
     * MapUrl : http://www.cqzhsq.cn:8070/services/RES_SGHJH/zqsyxt/SfX9OrRJmFf1oU4uoMXlOw
     * LabelUrl : http://www.cqzhsq.cn:8070/services/RES_SGHJYY/zqsdtzj/SfX9OrRJmFf1oU4uoMXlOw
     * Type : 2
     */

    private int Number;
    private String Name;
    private Object IconUrl;
    private String MapUrl;
    private String LabelUrl;
    private int Type;

    public int getNumber() {
        return Number;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Object getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(Object IconUrl) {
        this.IconUrl = IconUrl;
    }

    public String getMapUrl() {
        return MapUrl;
    }

    public void setMapUrl(String MapUrl) {
        this.MapUrl = MapUrl;
    }

    public String getLabelUrl() {
        return LabelUrl;
    }

    public void setLabelUrl(String LabelUrl) {
        this.LabelUrl = LabelUrl;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }
}
