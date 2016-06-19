package com.minecraft.moonlake.economy.data;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import com.minecraft.moonlake.economy.Economy;

public class Setting {

	private final Economy main;
	
	private String moneyChar = "";
	private int moneyBit = 0;
	private double defaultMoney = 0.0d;
	private int defaultPoint = 0;
	private int defaultScore = 0;
	
	/*
	private int moneyToScoreA = 0;
	private int pointToMoneyA = 0;
	private int pointToScoreA = 0;
	private int scoreToMoneyA = 0;
	private int moneyToScoreB = 0;
	private int pointToMoneyB = 0;
	private int pointToScoreB = 0;
	private int scoreToMoneyB = 0;*/
	
	public Setting(Economy main) {
		// 构造函数
		this.main = main;
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "config.yml"));
		this.moneyChar = yc.getString("Money.MoneyChar");
		this.moneyBit = yc.getInt("Money.MoneyBit");
		this.defaultMoney = yc.getDouble("Money.DefaultMoney");
		this.defaultPoint = yc.getInt("Point.DefaultPoint");
		this.defaultScore = yc.getInt("Score.DefaultScore");
		
		/*String[] mts = null;
		
		try {
			mts = yc.getString("Money.MoneyToScore").split("\\:");
			this.moneyToScoreA = Integer.parseInt(mts[0]);
			this.moneyToScoreB = Integer.parseInt(mts[1]);
			mts = yc.getString("Point.PointToMoney").split("\\:");
			this.pointToMoneyA = Integer.parseInt(mts[0]);
			this.pointToMoneyB = Integer.parseInt(mts[1]);
			mts = yc.getString("Point.PointToScore").split("\\:");
			this.pointToScoreA = Integer.parseInt(mts[0]);
			this.pointToScoreB = Integer.parseInt(mts[1]);
			mts = yc.getString("Score.ScoreToMoney").split("\\:");
			this.scoreToMoneyA = Integer.parseInt(mts[0]);
			this.scoreToMoneyB = Integer.parseInt(mts[1]);
		}
		catch(Exception e) {
			// 初始化异常
			main.log("字符串序列化异常: " + e.getMessage());
		}*/
	}
	
	public Economy getMain() {
		// 
		return this.main;
	}
	
	public int getMoneyBit() {
		return this.moneyBit;
	}
	
	public double getDefaultMoney() {
		return this.defaultMoney;
	}
	
	public int getDefaultPoint() {
		return this.defaultPoint;
	}
	
	public int getDefaultScore() {
		return this.defaultScore;
	}

	public String getMoneyChar() {
		return this.moneyChar;
	}
}
