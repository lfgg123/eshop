package com.eshop.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("shop_agent", "id", Agent.class);
		arp.addMapping("shop_banner", "id", Banner.class);
		arp.addMapping("shop_bbs", "id", Bbs.class);
		arp.addMapping("shop_bbs_return", "id", BbsReturn.class);
		arp.addMapping("shop_cart", "id", Cart.class);
		arp.addMapping("shop_cart_item", "id", CartItem.class);	
		arp.addMapping("shop_choice", "id", Choice.class);
		arp.addMapping("shop_class", Clazz.class);
		arp.addMapping("shop_enough_cut", "id", EnoughCut.class);
		arp.addMapping("shop_goods", "id", Goods.class);
		arp.addMapping("shop_good_eva", "id", GoodEva.class);
		arp.addMapping("shop_log", "id", Log.class);
		arp.addMapping("shop_member", "id", Member.class);
		arp.addMapping("shop_member_address", "id", MemberAddress.class);
		arp.addMapping("shop_member_evalue", "id", MemberEvalue.class);
		arp.addMapping("shop_member_grade", "id", MemberGrade.class);
		arp.addMapping("shop_member_message", "id", MemberMessage.class);
		arp.addMapping("shop_member_publish", "id", MemberPublish.class);
		arp.addMapping("shop_member_return", "id", MemberReturn.class);
		arp.addMapping("shop_member_wine", "id", MemberWine.class);
		arp.addMapping("shop_module", "id", Module.class);
		arp.addMapping("shop_order", "id", Order.class);
		arp.addMapping("shop_order_item", "id", OrderItem.class);
		arp.addMapping("shop_order_return", "id", OrderReturn.class);
		arp.addMapping("shop_product", "id", Product.class);
		arp.addMapping("shop_role", "id", Role.class);
		arp.addMapping("shop_run", "id", Run.class);
		arp.addMapping("shop_spec", "id", Spec.class);
		arp.addMapping("shop_spec_value", "id", SpecValue.class);
		arp.addMapping("shop_store", "id", Store.class);
		arp.addMapping("shop_sys_info", "guid", SysInfo.class);
		arp.addMapping("shop_user", "id", User.class);
	}
}

