package com.changic.rh.db;

import com.changic.rh.dao.*;
import com.changic.rh.dao.impl.*;

public class DaoMgr
{
	private static UserInfoDao userInfoDao = new UserInfoDaoImpl();

	public static UserInfoDao getUserInfoDao()
	{
		return userInfoDao;
	}

	private static PlayerInfoDao playerInfoDao = new PlayerInfoDaoImpl();

	public static PlayerInfoDao getPlayerInfoDao()
	{
		return playerInfoDao;
	}

	private static CastleInfoDao castleInfoDao = new CastleInfoDaoImpl();

	public static CastleInfoDao getCasteInfoDao()
	{
		return castleInfoDao;
	}

	private static BuildingInfoDao buildingInfoDao = new BuildingInfoDaoImpl();

	public static BuildingInfoDao getBuildingInfoDao()
	{
		return buildingInfoDao;
	}

	private static BuildingTempInfoDao buildingTempInfoDao = new BuildingTempInfoDaoImpl();

	public static BuildingTempInfoDao getBuildingTempInfoDao()
	{
		return buildingTempInfoDao;
	}

	private static CdOrderInfoDao cdOrderInfoDao = new CdOrderInfoDaoImpl();

	public static CdOrderInfoDao getCdOrderInfoDao()
	{
		return cdOrderInfoDao;
	}

	private static SkillTempInfoDao skillTempInfoDao = new SkillTempInfoDaoImpl();

	public static SkillTempInfoDao getSkillTempInfoDao()
	{
		return skillTempInfoDao;
	}

	private static LivingTempInfoDao livingTempInfoDao = new LivingTempInfoDaoImpl();

	public static LivingTempInfoDao getLivingTempInfoDao()
	{
		return livingTempInfoDao;
	}

	private static UpgradeTempInfoDao upgradeTempInfoDao = new UpgradeTempInfoDaoImpl();

	public static UpgradeTempInfoDao getUpgradeTempInfoDao()
	{
		return upgradeTempInfoDao;
	}

	private static PropTempInfoDao propTempInfoDao = new PropTempInfoDaoImpl();

	public static PropTempInfoDao getPropTempInfoDao()
	{
		return propTempInfoDao;
	}

	private static RoleInfoDao roleInfoDao = new RoleInfoDaoImpl();

	public static RoleInfoDao getRoleInfoDao()
	{
		return roleInfoDao;
	}

	private static ItemTempInfoDao itemTempInfoDao = new ItemTempInfoDaoImpl();

	public static ItemTempInfoDao getItemTempInfoDao()
	{
		return itemTempInfoDao;
	}

	private static ItemInfoDao itemInfoDao = new ItemInfoDaoImpl();

	public static ItemInfoDao getItemInfoDao()
	{
		return itemInfoDao;
	}

	private static UniqueIdDao uniqueIdDao = new UniqueIdDaoImpl();

	public static UniqueIdDao getUniqueIdDao()
	{
		return uniqueIdDao;
	}

	private static HeroTempInfoDao heroTempInfoDao = new HeroTempInfoDaoImpl();

	public static HeroTempInfoDao getHeroTempInfoDao()
	{
		return heroTempInfoDao;
	}

	private static HeroInfoDao heroInfoDao = new HeroInfoDaoImpl();

	public static HeroInfoDao getHeroInfoDao()
	{
		return heroInfoDao;
	}

	private static DropTempInfoDao dropTempInfoDao = new DropTempInfoDaoImpl();

	public static DropTempInfoDao getDropTempInfoDao()
	{
		return dropTempInfoDao;
	}

	private static DropGroupTempInfoDao dropGroupTempInfoDao = new DropGroupTempInfoDaoImpl();

	public static DropGroupTempInfoDao getDropGroupTempInfoDao()
	{
		return dropGroupTempInfoDao;
	}

	private static CampNodeAiTempInfoDao campNodeAiTempDao = new CampNodeAiTempInfoDaoImpl();

	public static CampNodeAiTempInfoDao getCampNodeAiTempDao()
	{
		return campNodeAiTempDao;
	}

	private static CampTempInfoDao campTempInfoDao = new CampTempInfoDaoImpl();

	public static CampTempInfoDao getCampTempInfoDao()
	{
		return campTempInfoDao;
	}

	private static CampMapTempInfoDao campMapInfoDao = new CampMapTempInfoDaoImpl();

	public static CampMapTempInfoDao getCampMapInfoDao()
	{
		return campMapInfoDao;
	}

	private static CampNodeTempInfoDao campNodeTempInfoDao = new CampNodeTempInfoDaoImpl();

	public static CampNodeTempInfoDao getCampNodeTempInfoDao()
	{
		return campNodeTempInfoDao;
	}

	private static ChapterTempInfoDao chapterTempInfoDao = new ChapterTempInfoDaoImpl();

	public static ChapterTempInfoDao getChapterTempInfoDao()
	{
		return chapterTempInfoDao;
	}

	private static GroupTempInfoDao groupTempInfoDao = new GroupTempInfoDaoImpl();

	public static GroupTempInfoDao getGroupTempInfoDao()
	{
		return groupTempInfoDao;
	}

	private static CampaignDataInfoDao campaignDataInfoDao = new CampaignDataInfoDaoImpl();

	public static CampaignDataInfoDao getCampaignDataInfoDao()
	{
		return campaignDataInfoDao;
	}

	private static CampaignInfoDao campaignInfoDao = new CampaignInfoDaoImpl();

	public static CampaignInfoDao getCampaignInfoDao()
	{
		return campaignInfoDao;
	}

	private static QuestTempInfoDao questTempInfoDao = new QuestTempInfoDaoImpl();

	public static QuestTempInfoDao getQuestTempInfoDao()
	{
		return questTempInfoDao;
	}

	private static QuestConditionTempInfoDao questConditionTempInfoDao = new QuestConditionTempInfoDaoImpl();

	public static QuestConditionTempInfoDao getQuestConditionTempInfoDao()
	{
		return questConditionTempInfoDao;
	}

	private static QuestInfoDao questInfoDao = new QuestInfoDaoImpl();

	public static QuestInfoDao getQuestInfoDao()
	{
		return questInfoDao;
	}

	private static GameConfigInfoDao gameConfigInfoDao = new GameConfigInfoDaoImpl();

	public static GameConfigInfoDao getGameConfigInfoDao()
	{
		return gameConfigInfoDao;
	}

	private static ShopInfoDao shopInfoDao = new ShopInfoDaoImpl();

	public static ShopInfoDao getShopInfoDao()
	{
		return shopInfoDao;
	}

	private static PetInfoDao petInfoDao = new PetInfoDaoImpl();

	public static PetInfoDao getPetInfoDao()
	{
		return petInfoDao;
	}

	private static ManorInfoDao manorInfoDao = new ManorInfoDaoImpl();

	public static ManorInfoDao getManorInfoDao()
	{
		return manorInfoDao;
	}

	private static ManorNodeInfoDao manorNodeInfoDao = new ManorNodeInfoDaoImpl();

	public static ManorNodeInfoDao getManorNodeInfoDao()
	{
		return manorNodeInfoDao;
	}

	private static ManorRankTempInfoDao manorRankTempInfoDao = new ManorRankTempInfoDaoImpl();

	public static ManorRankTempInfoDao getManorRankTempInfoDao()
	{
		return manorRankTempInfoDao;
	}

	private static ManorMatchDao manorMatchDao = new ManorMatchDaoImpl();

	public static ManorMatchDao getManorMatchDao()
	{
		return manorMatchDao;
	}

	private static GemCallTempInfoDao gemCallTempInfoDao = new GemCallTempInfoDaoImpl();

	public static GemCallTempInfoDao getGemCallTempInfoDao()
	{
		return gemCallTempInfoDao;
	}

	private static GemCallInfoDao gemCallInfoDao = new GemCallInfoDaoImpl();

	public static GemCallInfoDao getGemCallInfoDao()
	{
		return gemCallInfoDao;
	}

	private static SocialInfoDao socialInfoDao = new SocialInfoDaoImpl();

	public static SocialInfoDao getSocialInfoDao()
	{
		return socialInfoDao;
	}

	private static FriendInfoDao friendInfoDao = new FriendInfoDaoImpl();

	public static FriendInfoDao getFriendInfoDao()
	{
		return friendInfoDao;
	}

	private static CampNodePosTempInfoDao campNodePosTempInfoDao = new CampNodePosTempInfoDaoImpl();

	public static CampNodePosTempInfoDao getCampNodePosTempInfoDao()
	{
		return campNodePosTempInfoDao;
	}

	private static ManorRanNodeTempInfoDao manorRanNodeTempInfoDao = new ManorRanNodeTempInfoDaoImpl();

	public static ManorRanNodeTempInfoDao getManorRanNodeTempInfoDao()
	{
		return manorRanNodeTempInfoDao;
	}

	private static EffectTempInfoDao effectTempInfoDao = new EffectTempInfoDaoImpl();

	public static EffectTempInfoDao getEffectTempInfoDao()
	{
		return effectTempInfoDao;
	}

	private static ProductTempInfoDao productTempInfoDao = new ProductTempInfoDaoImpl();

	public static ProductTempInfoDao getProductTempInfoDao()
	{
		return productTempInfoDao;
	}

	private static DropElementTempInfoDao dropElementTempInfoDao = new DropElementTempInfoDaoImpl();

	public static DropElementTempInfoDao getDropElementTempInfoDao()
	{
		return dropElementTempInfoDao;
	}

	private static BufferTempInfoDao bufferTempInfoDao = new BufferTempInfoDaoImpl();

	public static BufferTempInfoDao getBufferTempInfoDao()
	{
		return bufferTempInfoDao;
	}

	private static MallItemInfoDao mallItemInfoDao = new MallItemInfoDaoImpl();

	public static MallItemInfoDao getMallItemInfoDao()
	{
		return mallItemInfoDao;
	}

	private static MazeTempInfoDao mazeTempInfoDao = new MazeTempInfoDaoImpl();

	public static MazeTempInfoDao getMazeTempInfoDao()
	{
		return mazeTempInfoDao;
	}

	private static MazeModeTempInfoDao mazeModeTempInfoDao = new MazeModeTempInfoDaoImpl();

	public static MazeModeTempInfoDao getMazeModeTempInfoDao()
	{
		return mazeModeTempInfoDao;
	}

	private static MazeLivingsTempInfoDao mazeLivingsTempInfoDao = new MazeLivingsTempInfoDaoImpl();

	public static MazeLivingsTempInfoDao getMazeLivingsTempInfoDao()
	{
		return mazeLivingsTempInfoDao;
	}

	private static MazeInfoDao mazeInfoDao = new MazeInfoDaoImpl();

	public static MazeInfoDao getMazeInfoDao()
	{
		return mazeInfoDao;
	}

	private static MatchInfoDao matchInfoDao = new MatchInfoDaoImpl();

	public static MatchInfoDao getMatchInfoDao()
	{
		return matchInfoDao;
	}

	private static MatchRecordInfoDao matchRecordInfoDao = new MatchRecordInfoDaoImpl();

	public static MatchRecordInfoDao getMatchRecordInfoDao()
	{
		return matchRecordInfoDao;
	}

	private static MatchRankingInfoDao matchRankingInfoDao = new MatchRankingInfoDaoImpl();

	public static MatchRankingInfoDao getMatchRankingInfoDao()
	{
		return matchRankingInfoDao;
	}

	private static MailInfoDao mailInfoDao = new MailInfoDaoImpl();

	public static MailInfoDao getMailInfoDao()
	{
		return mailInfoDao;
	}

	private static TreeInfoDao treeInfoDao = new TreeInfoDaoImpl();

	public static TreeInfoDao getTreeInfoDao()
	{
		return treeInfoDao;
	}

	private static TreeRecordInfoDao treeRecordInfoDao = new TreeRecordInfoDaoImpl();

	public static TreeRecordInfoDao getTreeRecordInfoDao()
	{
		return treeRecordInfoDao;
	}

	private static PetTalentInfoDao petTalentInfoDao = new PetTalentInfoDaoImpl();

	public static PetTalentInfoDao getPetTalentInfoDao()
	{
		return petTalentInfoDao;
	}

	private static TimerJobInfoDao timerJobInfoDao = new TimerJobInfoDaoImpl();

	public static TimerJobInfoDao getTimerJobInfoDao()
	{
		return timerJobInfoDao;
	}

	private static TechInfoDao techInfoDao = new TechInfoDaoImpl();

	public static TechInfoDao getTechInfoDao()
	{
		return techInfoDao;
	}

	private static DrugTempInfoDao drugTempInfoDao = new DrugTempInfoDaoImpl();

	public static DrugTempInfoDao getDrugTempInfoDao()
	{
		return drugTempInfoDao;
	}

	private static DrugInfoDao drugInfoDao = new DrugInfoDaoImpl();

	public static DrugInfoDao getDrugInfoDao()
	{
		return drugInfoDao;
	}

	private static MatchRewardTempInfoDao matchRewardTempInfoDao = new MatchRewardTempInfoDaoImpl();

	public static MatchRewardTempInfoDao getMatchRewardTempInfoDao()
	{
		return matchRewardTempInfoDao;
	}

	private static RobotTempInfoDao robotTempInfoDao = new RobotTempInfoDaoImpl();

	public static RobotTempInfoDao getRobotTempInfoDao()
	{
		return robotTempInfoDao;
	}

	private static OrderDao orderDao = new OrderDaoImpl();

	public static OrderDao getOrderDao()
	{
		return orderDao;
	}

	private static ActivityTempInfoDao activityTempInfoDao = new ActivityTempInfoDaoImpl();

	public static ActivityTempInfoDao getActivityTempInfoDao()
	{
		return activityTempInfoDao;
	}

	private static ActivityConditionInfoDao activityConditionInfoDao = new ActivityConditionInfoDaoImpl();

	public static ActivityConditionInfoDao getActivityConditionInfoDao()
	{
		return activityConditionInfoDao;
	}

	private static ActivityGiftPackInfoDao activityGiftPackInfoDao = new ActivityGiftPackInfoDaoImpl();

	public static ActivityGiftPackInfoDao getActivityGiftPackInfoDao()
	{
		return activityGiftPackInfoDao;
	}

	private static ActivityConditionTempInfoDao activityConditionTempInfoDao = new ActivityConditionTempInfoDaoImpl();

	public static ActivityConditionTempInfoDao getActivityConditionTempInfoDao()
	{
		return activityConditionTempInfoDao;
	}

	private static ActivityGiftPackTempInfoDao activityGiftPackTempInfoDao = new ActivityGiftPackTempInfoDaoImpl();

	public static ActivityGiftPackTempInfoDao getActivityGiftPackTempInfoDao()
	{
		return activityGiftPackTempInfoDao;
	}

	private static ActivityRewardTempInfoDao activityRewardTempInfoDao = new ActivityRewardTempInfoDaoImpl();

	public static ActivityRewardTempInfoDao getActivityRewardTempInfoDao()
	{
		return activityRewardTempInfoDao;
	}

	private static ManorSkinTempInfoDao manorSkinTempInfoDao = new ManorSkinTempInfoDaoImpl();

	public static ManorSkinTempInfoDao getManorSkinTempInfoDao()
	{
		return manorSkinTempInfoDao;
	}

	private static StoryCampTempInfoDao storyCampTempInfoDao = new StoryCampTempInfoDaoImpl();

	public static StoryCampTempInfoDao getStoryCampTempInfoDao()
	{
		return storyCampTempInfoDao;
	}

	private static MailAllInfoDao mailAllInfoDao = new MailAllInfoDaoImpl();

	public static MailAllInfoDao getMailAllInfoDao()
	{
		return mailAllInfoDao;
	}

	private static SignTempInfoDao signTempInfoDao = new SignTempInfoDaoImpl();

	public static SignTempInfoDao getSignTempInfoDao()
	{
		return signTempInfoDao;
	}

	private static SignInfoDao signInfoDao = new SignInfoDaoImpl();

	public static SignInfoDao getSignInfoDao()
	{
		return signInfoDao;
	}

	private static ManorMineInfoDao manorMineInfoDao = new ManorMineInfoDaoImpl();

	public static ManorMineInfoDao getManorMineInfoDao()
	{
		return manorMineInfoDao;
	}

	private static ManorMineTempInfoDao manorMineTempInfoDao = new ManorMineTempInfoDaoImpl();

	public static ManorMineTempInfoDao getManorMineTempInfoDao()
	{
		return manorMineTempInfoDao;
	}

	private static ChargeTempInfoDao chargeTempInfoDao = new ChargeTempInfoDaoImpl();

	public static ChargeTempInfoDao getChargeTempInfoDao()
	{
		return chargeTempInfoDao;
	}

	private static ManorReportInfoDao manorReportInfoDao = new ManorReportInfoDaoImpl();

	public static ManorReportInfoDao getManorReportInfoDao()
	{
		return manorReportInfoDao;
	}

	private static ManorSkillTempInfoDao manorSkillTempInfoDao = new ManorSkillTempInfoDaoImpl();

	public static ManorSkillTempInfoDao getManorSkillTempInfoDao()
	{
		return manorSkillTempInfoDao;
	}

	private static ManorBufferTempInfoDao manorBufferTempInfoDao = new ManorBufferTempInfoDaoImpl();

	public static ManorBufferTempInfoDao getManorBufferTempInfoDao()
	{
		return manorBufferTempInfoDao;
	}

	private static ManorLivingTempInfoDao manorLivingTempInfoDao = new ManorLivingTempInfoDaoImpl();

	public static ManorLivingTempInfoDao getManorLivingTempInfoDao()
	{
		return manorLivingTempInfoDao;
	}

	private static ShopItemTempInfoDao shopItemTempInfoDao = new ShopItemTempInfoDaoImpl();

	public static ShopItemTempInfoDao getShopItemTempInfoDao()
	{
		return shopItemTempInfoDao;
	}

	private static ChipInfoDao chipInfoDao = new ChipInfoDaoImpl();

	public static ChipInfoDao getChipInfoDao()
	{
		return chipInfoDao;
	}

	private static TreeCreamInfoDao treeCreamInfoDao = new TreeCreamInfoDaoImpl();

	public static TreeCreamInfoDao getTreeCreamInfoDao()
	{
		return treeCreamInfoDao;
	}

	private static RobotTreeInfoDao robotTreeInfoDao = new RobotTreeInfoDaoImpl();

	public static RobotTreeInfoDao getRobotTreeInfoDao()
	{
		return robotTreeInfoDao;
	}

	private static MazeNodeTempInfoDao mazeNodeTempInfoDao = new MazeNodeTempInfoDaoImpl();

	public static MazeNodeTempInfoDao getMazeNodeTempInfoDao()
	{
		return mazeNodeTempInfoDao;
	}

	private static MazeAiTempInfoDao mazeAiTempInfoDao = new MazeAiTempInfoDaoImpl();

	public static MazeAiTempInfoDao getMazeAiTempInfoDao()
	{
		return mazeAiTempInfoDao;
	}

	private static StoneInfoDao stoneInfoDao = new StoneInfoDaoImpl();

	public static StoneInfoDao getStoneInfoDao()
	{
		return stoneInfoDao;
	}

	private static QteAiTempInfoDao qteAiTempInfoDao = new QteAiTempInfoDaoImpl();

	public static QteAiTempInfoDao getQteAiTempInfoDao()
	{
		return qteAiTempInfoDao;
	}

	private static ManorSeasonRankInfoDao manorSeasonRankInfoDao = new ManorSeasonRankInfoDaoImpl();

	public static ManorSeasonRankInfoDao getManorSeasonRankInfoDao()
	{
		return manorSeasonRankInfoDao;
	}

	private static ActivityRecInfoDao activityRecInfoDao = new ActivityRecInfoDaoImpl();

	public static ActivityRecInfoDao getActivityRecInfoDao()
	{
		return activityRecInfoDao;
	}

	private static TimeGiftTempInfoDao timeGiftTempInfoDao = new TimeGiftTempInfoDaoImpl();

	public static TimeGiftTempInfoDao getTimeGiftTempInfoDao()
	{
		return timeGiftTempInfoDao;
	}

	private static TimeGiftElemTempInfoDao timeGiftElemTempInfoDao = new TimeGiftElemTempInfoDaoImpl();

	public static TimeGiftElemTempInfoDao getTimeGiftElemTempInfoDao()
	{
		return timeGiftElemTempInfoDao;
	}

	private static SkillPoolInfoDao skillPoolInfoDao = new SkillPoolInfoDaoImpl();

	public static SkillPoolInfoDao getSkillPoolInfoDao()
	{
		return skillPoolInfoDao;
	}

	private static SuitTempInfoDao suitTempInfoDao = new SuitTempInfoDaoImpl();

	public static SuitTempInfoDao getSuitTempInfoDao()
	{
		return suitTempInfoDao;
	}

	private static VersionTempInfoDao versionTempInfoDao = new VersionTempInfoDaoImpl();

	public static VersionTempInfoDao getVersionTempInfoDao()
	{
		return versionTempInfoDao;
	}

	private static VersionInfoDao versionInfoDao = new VersionInfoDaoImpl();

	public static VersionInfoDao getVersionInfoDao()
	{
		return versionInfoDao;
	}

	private static BillInfoDao billInfoDao = new BillInfoDaoImpl();

	public static BillInfoDao getBillInfoDao()
	{
		return billInfoDao;
	}

	private static QuestDataInfoDao questDataInfoDao = new QuestDataInfoDaoImpl();

	public static QuestDataInfoDao getQuestDataInfoDao()
	{
		return questDataInfoDao;
	}

	private static GameIdInfoDao gameIdInfoDao = new GameIdInfoDaoImpl();

	public static GameIdInfoDao getGameIdInfoDao()
	{
		return gameIdInfoDao;
	}

	private static ChapterBoxTempInfoDao chapterBoxTempInfoDao = new ChapterBoxTempInfoDaoImpl();

	public static ChapterBoxTempInfoDao getChapterBoxTempInfoDao()
	{
		return chapterBoxTempInfoDao;
	}

	private static ChapterBoxInfoDao chapterBoxInfoDao = new ChapterBoxInfoDaoImpl();

	public static ChapterBoxInfoDao getChapterBoxInfoDao()
	{
		return chapterBoxInfoDao;
	}

	private static PokedexInfoDao pokedexInfoDao = new PokedexInfoDaoImpl();

	public static PokedexInfoDao getPokedexInfoDao()
	{
		return pokedexInfoDao;
	}

	private static ShopLimitInfoDao shopLimitInfoDao = new ShopLimitInfoDaoImpl();

	public static ShopLimitInfoDao getShopLimitInfoDao()
	{
		return shopLimitInfoDao;
	}

	private static PetCallInfoDao petCallInfoDao = new PetCallInfoDaoImpl();

	public static PetCallInfoDao getPetCallInfoDao()
	{
		return petCallInfoDao;
	}

	private static StarSignTempInfoDao starSignTempInfoDao = new StarSignTempInfoDaoImpl();

	public static StarSignTempInfoDao getStarSignTempInfoDao()
	{
		return starSignTempInfoDao;
	}

	private static StarSignNodeTempInfoDao starSignNodeTempInfoDao = new StarSignNodeTempInfoDaoImpl();

	public static StarSignNodeTempInfoDao getStarSignNodeTempInfoDao()
	{
		return starSignNodeTempInfoDao;
	}

	private static StarSignPageTempInfoDao starSignPageTempInfoDao = new StarSignPageTempInfoDaoImpl();

	public static StarSignPageTempInfoDao getStarSignPageTempInfoDao()
	{
		return starSignPageTempInfoDao;
	}

	private static StarSignInfoDao starSignInfoDao = new StarSignInfoDaoImpl();

	public static StarSignInfoDao getStarSignInfoDao()
	{
		return starSignInfoDao;
	}

	private static ActivityClassifyTempInfoDao activityClassifyTempInfoDao = new ActivityClassifyTempInfoDaoImpl();

	public static ActivityClassifyTempInfoDao getActivityClassifyTempInfoDao()
	{
		return activityClassifyTempInfoDao;
	}

	private static SwitchDao switchDao = new SwitchDaoImpl();

	public static SwitchDao getSwitchDao()
	{
		return switchDao;
	}

	private static VipTempInfoDao vipTempInfoDao = new VipTempInfoDaoImpl();

	public static VipTempInfoDao getVipTempInfoDao()
	{
		return vipTempInfoDao;
	}

	private static VipBonusTempInfoDao vipBonusTempInfoDao = new VipBonusTempInfoDaoImpl();

	public static VipBonusTempInfoDao getVipBonusTempInfoDao()
	{
		return vipBonusTempInfoDao;
	}

	private static VipInfoDao vipInfoDao = new VipInfoDaoImpl();

	public static VipInfoDao getVipInfoDao()
	{
		return vipInfoDao;
	}

	private static PlayerExtendInfoDao playerExtendInfoDao = new PlayerExtendInfoDaoImpl();

	public static PlayerExtendInfoDao getPlayerExtendInfoDao()
	{
		return playerExtendInfoDao;
	}

	private static VipExchangeInfoDao vipExchangeInfoDao = new VipExchangeInfoDaoImpl();

	public static VipExchangeInfoDao getVipExchangeInfoDao()
	{
		return vipExchangeInfoDao;
	}

	private static StarSignMapTempInfoDao starSignMapTempInfoDao = new StarSignMapTempInfoDaoImpl();

	public static StarSignMapTempInfoDao getStarSignMapTempInfoDao()
	{
		return starSignMapTempInfoDao;
	}

	private static EliteCampTempInfoDao eliteCampTempInfoDao = new EliteCampTempInfoDaoImpl();

	public static EliteCampTempInfoDao getEliteCampTempInfoDao()
	{
		return eliteCampTempInfoDao;
	}

	private static EliteCampDataInfoDao eliteCampDatOInfoDao = new EliteCampDataInfoDaoImpl();

	public static EliteCampDataInfoDao getEliteCampDataInfoDao()
	{
		return eliteCampDatOInfoDao;
	}

	private static ChargeInfoDao chargeInfoDao = new ChargeInfoDaoImpl();

	public static ChargeInfoDao getChargeInfoDao()
	{
		return chargeInfoDao;
	}

	private static CenterConfigTempInfoDao centerConfigTempInfoDao = new CenterConfigTempInfoDaoImpl();

	public static CenterConfigTempInfoDao getCenterConfigTempInfoDao()
	{
		return centerConfigTempInfoDao;
	}

	private static FunctionShowTempInfoDao functionShowTempInfoDao = new FunctionShowTempInfoDaoImpl();

	public static FunctionShowTempInfoDao getFunctionShowTempInfoDao()
	{
		return functionShowTempInfoDao;
	}

	private static RankInfoDao rankInfoDao = new RankInfoDaoImpl();

	public static RankInfoDao getRankInfoDao()
	{
		return rankInfoDao;
	}

	private static FlowerAcceptInfoDao flowerAcceptInfoDao = new FlowerAcceptInfoDaoImpl();

	public static FlowerAcceptInfoDao getFlowerAcceptInfoDao()
	{
		return flowerAcceptInfoDao;
	}

	private static GuildInfoDao guildInfoDao = new GuildInfoDaoImpl();

	public static GuildInfoDao getGuildInfoDao()
	{
		return guildInfoDao;
	}
	
	private static GuildBattleDefenceDao guildBattleDefenceDao = new GuildBattleDefenceDaoImpl();

	public static GuildBattleDefenceDao getGuildBattleDefenceDao()
	{
		return guildBattleDefenceDao;
	}

	private static HeroPropTempInfoDao heroPropTempInfoDao = new HeroPropTempInfoDaoImpl();

	public static HeroPropTempInfoDao getHeroPropTempInfoDao()
	{
		return heroPropTempInfoDao;
	}

	private static RandomNameTempInfoDao randomNameTempInfoDao = new RandomNameTempInfoDaoImpl();

	public static RandomNameTempInfoDao getRandomNameTempInfoDao()
	{
		return randomNameTempInfoDao;
	}

	private static GuildAidTempInfoDao guildAidTempInfoDao = new GuildAidTempInfoDaoImpl();

	public static GuildAidTempInfoDao getGuildAidTempInfoDao()
	{
		return guildAidTempInfoDao;
	}
	
	private static GuildRewardTempInfoDao guildRewardTempInfoDao = new GuildRewardTempInfoDaoImpl();

	public static GuildRewardTempInfoDao getGuildRewardTempInfoDao()
	{
		return guildRewardTempInfoDao;
	}

	private static GuildLevelTempInfoDao guildLevelTempInfoDao = new GuildLevelTempInfoDaoImpl();

	public static GuildLevelTempInfoDao getGuildLevelTempInfoDao()
	{
		return guildLevelTempInfoDao;
	}

	private static GuildBuildingInfoDao guildBuildingInfoDao = new GuildBuildingInfoDaoImpl();

	public static GuildBuildingInfoDao getGuildBuildingInfoDao()
	{
		return guildBuildingInfoDao;
	}

	private static GuildMemberInfoDao guildMemberInfoDao = new GuildMemberInfoDaoImpl();

	public static GuildMemberInfoDao getGuildMemberInfoDao()
	{
		return guildMemberInfoDao;
	}

	private static GuildTechInfoDao guildTechInfoDao = new GuildTechInfoDaoImpl();

	public static GuildTechInfoDao getGuildTechInfoDao()
	{
		return guildTechInfoDao;
	}

	private static GuildMemberAuthorityTempInfoDao guildMemberAuthorityTempInfoDao = new GuildMemberAuthorityTempInfoDaoImpl();

	public static GuildMemberAuthorityTempInfoDao getGuildMemberAuthorityTempInfoDao()
	{
		return guildMemberAuthorityTempInfoDao;
	}

	private static EliteCampNodeTempInfoDao eliteCampNodeTempInfoDao = new EliteCampNodeTempInfoDaoImpl();

	public static EliteCampNodeTempInfoDao getEliteCampNodeTempInfoDao()
	{
		return eliteCampNodeTempInfoDao;
	}

	private static CampTargetInfoDao campTargetInfoDao = new CampTargetInfoDaoImpl();

	public static CampTargetInfoDao getCampTargetInfoDao()
	{
		return campTargetInfoDao;
	}

	private static WishInfoDao wishInfoDao = new WishInfoDaoImpl();

	public static WishInfoDao getWishInfoDao()
	{
		return wishInfoDao;
	}

	private static XingeInfoDao xingeInfoDao = new XingeInfoDaoImpl();

	public static XingeInfoDao getXingeInfoDao()
	{
		return xingeInfoDao;
	}

	private static XingeTempInfoDao xingeTempInfoDao = new XingeTempInfoDaoImpl();

	public static XingeTempInfoDao getXingeTempInfoDao()
	{
		return xingeTempInfoDao;
	}

	private static ResRecoveryTempInfoDao resRecoveryTempInfoDao = new ResRecoveryTempInfoDaoImpl();

	public static ResRecoveryTempInfoDao getResRecoveryTempInfoDao()
	{
		return resRecoveryTempInfoDao;
	}

	private static ResRecoveryInfoDao resRecoveryInfoDao = new ResRecoveryInfoDaoImpl();

	public static ResRecoveryInfoDao getResRecoveryInfoDao()
	{
		return resRecoveryInfoDao;
	}

	private static GuildBattleInfoDao guildBattleInfoDao = new GuildBattleInfoDaoImpl();

	public static GuildBattleInfoDao getGuildBattleInfoDao()
	{
		return guildBattleInfoDao;
	}

	private static SacristyTempInfoDao sacristyTempInfoDao = new SacristyTempInfoDaoImpl();

	public static SacristyTempInfoDao getSacristyTempInfoDao()
	{
		return sacristyTempInfoDao;
	}

	private static SacristyInfoDao sacristyInfoDao = new SacristyInfoDaoImpl();

	public static SacristyInfoDao getSacristyInfoDao()
	{
		return sacristyInfoDao;
	}

	private static GuildBattleMemberInfoDao guildBattleMemberInfoDao = new GuildBattleMemberInfoDaoImpl();

	public static GuildBattleMemberInfoDao getGuildBattleMemberInfoDao()
	{
		return guildBattleMemberInfoDao;
	}

	private static RecordInfoDao recordInfoDao = new RecordInfoDaoImpl();

	public static RecordInfoDao getRecordInfoDao()
	{
		return recordInfoDao;
	}

	private static CenterNoticeInfoDao centerNoticeInfoDao = new CenterNoticeInfoDaoImpl();

	public static CenterNoticeInfoDao getCenterNoticeInfoDao()
	{
		return centerNoticeInfoDao;
	}

	private static GuildMessageInfoDao guildMessageInfoDao = new GuildMessageInfoDaoImpl();

	public static GuildMessageInfoDao getGuildMessageInfoDao()
	{
		return guildMessageInfoDao;
	}

	private static AlchemypotTempInfoDao alchemypotTempInfoDao = new AlchemypotTempInfoDaoImpl();

	public static AlchemypotTempInfoDao getAlchemypotTempInfoDao()
	{
		return alchemypotTempInfoDao;
	}

	private static AlchemypotInfoDao alchemypotInfoDao = new AlchemypotInfoDaoImpl();

	public static AlchemypotInfoDao getAlchemypotInfoDao()
	{
		return alchemypotInfoDao;
	}

	private static RandomCampTempInfoDao randomCampTempInfoDao = new RandomCampTempInfoDaoImpl();

	public static RandomCampTempInfoDao getRandomCampTempInfoDao()
	{
		return randomCampTempInfoDao;
	}

	private static RewardQuestInfoDao rewardQuestInfoDao = new RewardQuestInfoDaoImpl();

	public static RewardQuestInfoDao getRewardQuestInfoDao()
	{
		return rewardQuestInfoDao;
	}

	private static RewardQuestDateInfoDao rewardQuestDateInfoDao = new RewardQuestDateInfoDaoImpl();

	public static RewardQuestDateInfoDao getRewardQuestDateInfoDao()
	{
		return rewardQuestDateInfoDao;
	}

	private static RewardQuestRecordInfoDao rewardQuestRecordInfoDao = new RewardQuestRecordInfoDaoImpl();

	public static RewardQuestRecordInfoDao getRewardQuestRecordInfoDao()
	{
		return rewardQuestRecordInfoDao;
	}

	private static BillOverseasInfoDao billOverseasInfoDao = new BillOverseasInfoDaoImpl();

	public static BillOverseasInfoDao getBillOverseasInfoDao()
	{
		return billOverseasInfoDao;
	}

	private static ActivityInfoDao activityInfoDao = new ActivityInfoDaoImpl();

	public static ActivityInfoDao getActivityInfoDao()
	{
		return activityInfoDao;
	}

	private static TransferlogInfoDao transferlogInfoDao = new TransferlogInfoDaoImpl();

	public static TransferlogInfoDao getTransferlogInfoDao()
	{
		return transferlogInfoDao;
	}

	private static FightRoomTempInfoDao fightRoomTempInfoDao = new FightRoomTempInfoDaoImpl();

	public static FightRoomTempInfoDao getFightRoomTempInfoDao()
	{
		return fightRoomTempInfoDao;
	}

	private static FightRewardTempInfoDao fightRewardTempInfoDao = new FightRewardTempInfoDaoImpl();

	public static FightRewardTempInfoDao getFightRewardTempInfoDao()
	{
		return fightRewardTempInfoDao;
	}

	private static FightInfoDao fightInfoDao = new FightInfoDaoImpl();

	public static FightInfoDao getFightInfoDao()
	{
		return fightInfoDao;
	}
}