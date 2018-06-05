package com.eshop.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * 描述：
 *
 * @author chentianlong
 * @create 2017/11/30 11:53
 */
public class BaseGoodSpec<M extends BaseGoodSpec<M>> extends Model<M> implements IBean {

    public String getGoodGuid() {
        return getStr("good_guid");
    }

    public M setGoodGuid(String goodGuid) {
        set("good_guid", goodGuid);
        return (M)this;
    }

    public String getGoodName() {
        return getStr("good_name");
    }

    public M setGoodName(String goodName) {
        set("good_name", goodName);
        return (M)this;
    }

    public String getSpecGuid() {
        return getStr("spec_guid");
    }

    public M setSpecGuid(String specGuid) {
        set("spec_guid", specGuid);
        return (M)this;
    }

    public String getSpecName() {
        return getStr("spec_name");
    }

    public M setSpecName(String specName) {
        set("spec_name", specName);
        return (M)this;
    }

    public String getSpecvalueGuid() {
        return getStr("specvalue_guid");
    }

    public M setSpecvalueGuid(String specvalueGuid) {
        set("specvalue_guid", specvalueGuid);
        return (M)this;
    }

    public String getSpecValue() {
        return getStr("spec_value");
    }

    public M setSpecValue(String specValue) {
        set("spec_value", specValue);
        return (M)this;
    }
}
