package com.minecraft.moonlake.economy.api;

import com.minecraft.moonlake.economy.sql.DataSource;

public interface MoonLakeEconomy extends DataSource {

	/**
	 * 获取插件的版本号
	 * @return 版本
	 */
	String getVersion();
	
	/**
	 * 获取插件的作者
	 * @return 作者
	 */
	String getAuthor();
	
	/**
	 * 获取插件的网站
	 * @return 网站
	 */
	String getWebsite();
	
	/**
	 * 四舍五入保留指定小数位数
	 * @param num 双精度浮点数
	 * @param bit 保留位数
	 * @return 四舍五入后的双精度浮点数
	 */
	double rounding(double num, int bit);
}
