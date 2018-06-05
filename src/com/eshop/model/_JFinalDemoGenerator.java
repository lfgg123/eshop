package com.eshop.model;

import javax.sql.DataSource;
import com.eshop.config.Config;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class _JFinalDemoGenerator {
	
	public static DataSource getDataSource() {
		PropKit.use("jdbc_config.txt");
		C3p0Plugin plugin = Config.createDBPlugin();
		plugin.start();
		return plugin.getDataSource();
	}
	
	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "com.eshop.model.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/../src/com/eshop/model/base";
		
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.eshop.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		
		// 创建生成器
		Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 设置是否生成链式 setter 方法
		generator.setGenerateChainSetter(false);
		// 添加不需要生成的表名
		generator.addExcludedTable("shop_goods_spec");
		generator.addExcludedTable("shop_member_coll");
		generator.addExcludedTable("shop_member_history");
		generator.addExcludedTable("shop_role_module");
		generator.addExcludedTable("shop_time_buy");
		generator.addExcludedTable("shop_class");
		generator.addExcludedTable("shop_bbs");
		generator.addExcludedTable("shop_con");
		generator.addExcludedTable("shop_good_eva");
//		generator.addExcludedTable("shop_member_return");
		generator.addExcludedTable("shop_time_buy");
		generator.addExcludedTable("shop_agent_order");
//		generator.addExcludedTable("shop_member_publish");
		// 设置是否在 Model 中生成 dao 对象
		generator.setGenerateDaoInModel(true);
		// 设置是否生成链式 setter 方法
		generator.setGenerateChainSetter(true);
		// 设置是否生成字典文件
		generator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		generator.setRemovedTableNamePrefixes("shop_");
		// 生成
		generator.generate();
	}
}




