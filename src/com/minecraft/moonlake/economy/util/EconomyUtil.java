package com.minecraft.moonlake.economy.util;

import com.minecraft.moonlake.economy.Economy;
import com.minecraft.moonlake.economy.api.MoonLakeEconomy;
import com.minecraft.moonlake.economy.api.event.PlayerMoneyChangeEvent;
import com.minecraft.moonlake.economy.api.event.PlayerPointChangeEvent;
import com.minecraft.moonlake.economy.api.event.PlayerScoreChangeEvent;
import com.minecraft.moonlake.economy.data.PlayerEconomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.math.BigDecimal;
import java.sql.*;

public class EconomyUtil implements MoonLakeEconomy {

	private final Economy main;
	private final String database;
	private final String host;
	private final String port;
	private final String username;
	private final String password;
	private final String tableName;
	private final String id;
	private final String name;
	private final String money;
	private final String point;
	private final String score;
	private final String url = "jdbc:mysql://host:port/database?characterEncoding=utf-8";
	private final PluginDescriptionFile pdf;
	
	@SuppressWarnings("resource")
	public EconomyUtil(Economy main) {
		// 构造函数
		this.main = main;
		this.pdf = main.getDescription();
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "config.yml"));
		this.database = yc.getString("MySQL.mySQLDatabase");
		this.host = yc.getString("MySQL.mySQLHost");
		this.port = yc.getString("MySQL.mySQLPort");
		this.username = yc.getString("MySQL.mySQLUserName");
		this.password = yc.getString("MySQL.mySQLPassword");
		this.tableName = yc.getString("MySQL.mySQLTableName");
		this.id = yc.getString("MySQL.mySQLColumnId");
		this.name = yc.getString("MySQL.mySQLColumnName");
		this.money = yc.getString("MySQL.mySQLColumnMoney");
		this.point = yc.getString("MySQL.mySQLColumnPoint");
		this.score = yc.getString("MySQL.mySQLColumnScore");
		
		Connection con = null;
		Statement st = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			main.log("初始化数据库驱动完毕.");
			con = this.getConnection("mysql");
			st = con.createStatement();
			st.executeUpdate("create database if not exists " + this.database);
			close(st);
			con = this.getConnection(this.database);
			st = con.createStatement();
			st.executeUpdate("create table if not exists " + this.tableName + " (" 
								+ this.id 	 + " integer not null auto_increment," 
								+ this.name  + " varchar(20) not null unique," 
								+ this.money + " double not null default '0.0'," 
								+ this.point + " integer not null default '0'," 
								+ this.score + " integer not null default '0'," 
											  + " primary key (" + this.id + "));");
		}
		catch (ClassNotFoundException e) {
			// 初始化驱动异常
			main.log("初始化数据库驱动异常: " + e.getMessage());
		} 
		catch (SQLException e) {
			// 数据库异常
			main.log("连接错误,数据库异常: " + e.getMessage());
		} 
		catch (Exception e) {
			// 获取数据库连接异常
			main.log(e.getMessage());
		}
		finally {
			close(st);
			close(con);
		}
	}

	public synchronized Connection getConnection(String database) throws Exception {
		// 
		Connection con = null;
		String target = url.replace("host", this.host).replace("port", this.port).replace("database", database);
		try {
			con = DriverManager.getConnection(target, this.username, this.password);
		}
		catch (SQLException e) {
			// 数据库异常
			throw new Exception("连接错误,数据库异常: " + e.getMessage());
		}
		return con;
	}
	
	@SuppressWarnings("resource")
	@Override
	public synchronized boolean initialization(String name) {
		// 
		Connection con = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    
	    try {
	    	con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			
			if(!rs.next()) {
				// 没有普通数据则初始化
				pst = con.prepareStatement("insert into " + this.tableName + "(" + this.name + "," + this.money + "," + this.point + "," + this.score + ") values (?,?,?,?);");
				pst.setString(1, name);
				pst.setDouble(2, main.getSetting().getDefaultMoney());
				pst.setInt(3, main.getSetting().getDefaultPoint());
				pst.setInt(4, main.getSetting().getDefaultScore());
				pst.executeUpdate();
			}
			return true;
		} 
	    catch (SQLException e) {
	    	main.log("初始化数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		} 
	    catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
	    finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@Override
	public synchronized boolean hasPlayer(String name) {
		// 
		Connection con = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    
	    try {
	    	con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			return rs.next();
		} 
	    catch (SQLException e) {
	    	main.log("查找数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		} 
	    catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
	    finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@Override
	public synchronized double getMoney(String name) {
		// 
		Connection con = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    
	    try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			double money = 0;
			
			if(rs.next()) {
				// 具有数据
				money = rs.getDouble(this.money);
			}
			return money;
		} 
	    catch (SQLException e) {
	    	main.log("查找数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return 0.0d;
		}
	    catch (Exception e) {
	    	main.log(e.getMessage());
			return 0.0d;
		}
	    finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@Override
	public synchronized int getPoint(String name) {
		// 
		Connection con = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    
	    try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int point = 0;
			
			if(rs.next()) {
				// 具有数据
				point = rs.getInt(this.point);
			}
			return point;
		} 
	    catch (SQLException e) {
	    	main.log("查找数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return 0;
		}
	    catch (Exception e) {
	    	main.log(e.getMessage());
			return 0;
		}
	    finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@Override
	public synchronized int getScore(String name) {
		// 
		Connection con = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    
	    try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int score = 0;
			
			if(rs.next()) {
				// 具有数据
				score = rs.getInt(this.score);
			}
			return score;
		} 
	    catch (SQLException e) {
	    	main.log("查找数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return 0;
		}
	    catch (Exception e) {
	    	main.log(e.getMessage());
			return 0;
		}
	    finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean setMoney(String name, double money) {
		// 
		money = rounding(money, main.getSetting().getMoneyBit());
		
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			double old = 0.0d;
			if(rs.next()) {
				old = rs.getDouble(this.money);
			}
			
			PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, old, money);
			Bukkit.getServer().getPluginManager().callEvent(pmce);
			
			if(!pmce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.money + "=? where " + this.name + "=?;");
				pst.setDouble(1, money);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		}
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean setPoint(String name, int point) {
		// 
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int old = 0;
			if(rs.next()) {
				old = rs.getInt(this.point);
			}
			
			PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, old, point);
			Bukkit.getServer().getPluginManager().callEvent(ppce);
			
			if(!ppce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.point + "=? where " + this.name + "=?;");
				pst.setInt(1, point);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		}
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean setScore(String name, int score) {
		// 
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int old = 0;
			if(rs.next()) {
				old = rs.getInt(this.score);
			}
			
			PlayerScoreChangeEvent psce = new PlayerScoreChangeEvent(name, old, score);
			Bukkit.getServer().getPluginManager().callEvent(psce);
			
			if(!psce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.score + "=? where " + this.name + "=?;");
				pst.setInt(1, score);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		}
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean giveMoney(String name, double money) {
		// 
		money = rounding(money, main.getSetting().getMoneyBit());
		
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			double sql_money = 0.0;
			
			if(rs.next()) {
				// 具有数据
				sql_money = rs.getDouble(this.money);
			}
			
			PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, sql_money, sql_money + money);
			Bukkit.getServer().getPluginManager().callEvent(pmce);
			
			if(!pmce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.money + "=? where " + this.name + "=?;");
				pst.setDouble(1, sql_money + money);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		} 
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean givePoint(String name, int point) {
		// 
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int sql_point = 0;
			
			if(rs.next()) {
				// 具有数据
				sql_point = rs.getInt(this.point);
			}

			PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, sql_point, sql_point + point);
			Bukkit.getServer().getPluginManager().callEvent(ppce);
			
			if(!ppce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.point + "=? where " + this.name + "=?;");
				pst.setInt(1, sql_point + point);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		} 
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean giveScore(String name, int score) {
		// 
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int sql_score = 0;
			
			if(rs.next()) {
				// 具有数据
				sql_score = rs.getInt(this.score);
			}

			PlayerScoreChangeEvent psce = new PlayerScoreChangeEvent(name, sql_score, sql_score + score);
			Bukkit.getServer().getPluginManager().callEvent(psce);
			
			if(!psce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.score + "=? where " + this.name + "=?;");
				pst.setInt(1, sql_score + score);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		} 
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean takeMoney(String name, double money) {
		// 
		money = rounding(money, main.getSetting().getMoneyBit());
		
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			double sql_money = 0.0;
			
			if(rs.next()) {
				// 具有数据
				sql_money = rs.getDouble(this.money);
			}
			
			PlayerMoneyChangeEvent pmce = new PlayerMoneyChangeEvent(name, sql_money, sql_money - money);
			Bukkit.getServer().getPluginManager().callEvent(pmce);
			
			if(!pmce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.money + "=? where " + this.name + "=?;");
				pst.setDouble(1, sql_money - money);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		} 
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean takePoint(String name, int point) {
		// 
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int sql_point = 0;
			
			if(rs.next()) {
				// 具有数据
				sql_point = rs.getInt(this.point);
			}

			PlayerPointChangeEvent ppce = new PlayerPointChangeEvent(name, sql_point, sql_point - point);
			Bukkit.getServer().getPluginManager().callEvent(ppce);
			
			if(!ppce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.point + "=? where " + this.name + "=?;");
				pst.setInt(1, sql_point - point);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		} 
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean takeScore(String name, int score) {
		// 
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			int sql_score = 0;
			
			if(rs.next()) {
				// 具有数据
				sql_score = rs.getInt(this.score);
			}

			PlayerScoreChangeEvent psce = new PlayerScoreChangeEvent(name, sql_score, sql_score - score);
			Bukkit.getServer().getPluginManager().callEvent(psce);
			
			if(!psce.isCancelled()) {
				// 没有阻止
				pst = con.prepareStatement("update " + this.tableName + " set " + this.score + "=? where " + this.name + "=?;");
				pst.setInt(1, sql_score - score);
				pst.setString(2, name);
				pst.executeUpdate();
			}
			return true;
		} 
		catch (SQLException e) {
	    	main.log("更新数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return false;
		}
		catch (Exception e) {
			main.log(e.getMessage());
			return false;
		}
		finally {
			close(rs);
			close(pst);
			close(con);
		}
	}
	
	@Override
	public synchronized PlayerEconomy getData(String name) {
		// 
		Connection con = null;
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    PlayerEconomy pe = null;
	    
	    try {
			con = this.getConnection(this.database);
			pst = con.prepareStatement("select * from " + this.tableName + " where " + this.name + "=?;");
			pst.setString(1, name);
			rs = pst.executeQuery();
			
			if(rs.next()) {
				// 具有数据
				pe = new PlayerEconomy(name, rs.getDouble(this.money), rs.getInt(this.point), rs.getInt(this.score));
			}
			return pe;
		} 
	    catch (SQLException e) {
	    	main.log("查找数据名为 '" + name + "' 发生异常: " + e.getMessage());
	    	return null;
		}
	    catch (Exception e) {
	    	main.log(e.getMessage());
			return null;
		}
	    finally {
			close(rs);
			close(pst);
			close(con);
		}
	}
	
	private void close(Connection con) {
		// 关闭声明
		if(con != null) {
			// 不为空
			try {
				con.close();
			} 
			catch (SQLException e) {
				main.log("关闭数据库的 Connection 异常: " + e.getMessage());
			}
		}
	}
	
	private void close(Statement st) {
		// 关闭声明
		if(st != null) {
			// 不为空
			try {
				st.close();
			} 
			catch (SQLException e) {
				main.log("关闭数据库的 Statement 异常: " + e.getMessage());
			}
		}
	}
	
	private void close(ResultSet rs) {
		// 关闭声明
		if(rs != null) {
			// 不为空
			try {
				rs.close();
			} 
			catch (SQLException e) {
				main.log("关闭数据库的 ResultSet 异常: " + e.getMessage());
			}
		}
	}
	
	@Override
	public String getAuthor() {
		// 
		return pdf.getAuthors().get(0);
	}
	
	@Override
	public String getVersion() {
		// 
		return pdf.getVersion();
	}
	
	@Override
	public String getWebsite() {
		// 
		return pdf.getWebsite();
	}
	
	@Override
	public double rounding(double num, int bit) {
        // 四舍五入保留小数位数
        return new BigDecimal(num).setScale(bit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
