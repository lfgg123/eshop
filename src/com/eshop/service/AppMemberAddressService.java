package com.eshop.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.eshop.model.MemberAddress;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.sun.org.apache.xerces.internal.impl.dv.DVFactoryException;

public class AppMemberAddressService {
	private static MemberAddress dao = MemberAddress.dao;

	/**
	 * 获取当前会员的地址列表
	 * 
	 * @param String
	 *            member 会员唯一标识
	 * @return List<MemberAddress> addressList
	 * @throws IllegalArgumentException
	 */
	public List<MemberAddress> getMemberAddressList(String member) {
		if (StrKit.isBlank(member)) {
			throw new IllegalArgumentException("会员guid不能");
		}
		return dao.find("select * from shop_member_address where member = ?", member);

	}

	/**
	 * 添加地址
	 * 
	 * @param MemberAddress
	 *            address
	 * @return boolean result
	 * @throws IllegalArgumentException
	 */
	public Integer addMemberAddress(MemberAddress address) {
		Integer result = null;
		if (address.setGuid(UUID.randomUUID().toString()).setCreateTime(new Date()).setModifyTime(new Date()).save()) {
			result = address.getId();
		}
		return result;

	}

	/**
	 * 修改地址
	 * 
	 * @param MemberAddress
	 *            address
	 * @return boolean result
	 * @throws Exception
	 * @throws IllegalArgumentException
	 * 
	 */
	public Integer modifyMemberAddress(final MemberAddress address) throws Exception {
		Integer result = null;
		if (address.getId() == null) {
			throw new Exception("收货人地址信息Id不能为空");
		}

		boolean saveResult = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {
				boolean groupResult = true;
				if (address.getIsuse() == 1) {
					// 检查数据库中有没有 设为默认
					List<MemberAddress> addresses = dao.find(
							"select * from shop_member_address where member =? and isuse =1 ", address.getMember());
					if (addresses != null && !addresses.isEmpty()) {
						for (MemberAddress lAddress : addresses) {
							if (!lAddress.setIsuse(0).update())
								groupResult = false;
						}
					}
				}
				boolean result1 = address.setModifyTime(new Date()).update();
				return groupResult && result1;
			}
		});
		if (saveResult) {	
			result = address.getId();
		}
		return result;
	}

	/**
	 * 删除地址
	 * 
	 * @param Integer
	 *            id
	 * @throws Exception
	 */
	public boolean deleteAddress(Integer id) throws Exception {
		if (id == null) {
			throw new Exception("地址id 不能为空");
		}
		MemberAddress address = dao.findById(id);
		if (address == null) {
			return true;
		}
		return dao.deleteById(id);
	}

}
