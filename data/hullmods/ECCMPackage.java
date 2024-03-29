package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ECCMPackage extends BaseHullMod {

	// original values from MissileSpecialization
//	public static final float MISSILE_SPEC_SPEED_BONUS = 25f;
//	public static final float MISSILE_SPEC_RANGE_MULT = 0.8f;
//	public static final float MISSILE_SPEC_ACCEL_BONUS = 50f;
//	public static final float MISSILE_TURN_RATE_BONUS = 50f;
//	public static final float MISSILE_TURN_ACCEL_BONUS = 100f;
	// original values from ECCM
//	stats.getMissileMaxSpeedBonus().modifyPercent(id, 10f);
//	stats.getMissileAccelerationBonus().modifyPercent(id, 100f);
//	stats.getMissileMaxTurnRateBonus().modifyPercent(id, 10f);
//	stats.getMissileTurnAccelerationBonus().modifyPercent(id, 50f);
	
	
	public static final float MISSILE_SPEED_BONUS = 25f;
	public static final float MISSILE_RANGE_MULT = 0.8f;
	public static final float MISSILE_ACCEL_BONUS = 150f;
	public static final float MISSILE_RATE_BONUS = 50f;
	public static final float MISSILE_TURN_ACCEL_BONUS = 150f;
	
	public static final float EW_PENALTY_MULT = 0.5f;
        public static final float EW_PENALTY_SMULT = 0f;
	public static final float EW_PENALTY_REDUCTION = 5f;
	//public static final float MAX_EW_PENALTY_MOD = 5f;
	
	public static final float ECCM_CHANCE = 0.5f;
	public static final float GUIDANCE_IMPROVEMENT = 1f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("eccm") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("eccm"))) {
			stats.getEccmChance().modifyFlat(id, 1f);
                        stats.getDynamic().getStat(Stats.ELECTRONIC_WARFARE_PENALTY_MULT).modifyMult(id, EW_PENALTY_SMULT);
			stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_PENALTY_MOD).modifyMult(id, EW_PENALTY_SMULT); ////Remove if this conflicts with any ECM changes!
		} else {
			stats.getEccmChance().modifyFlat(id, ECCM_CHANCE);
                        stats.getDynamic().getStat(Stats.ELECTRONIC_WARFARE_PENALTY_MULT).modifyMult(id, EW_PENALTY_MULT);
                        stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_PENALTY_MOD).modifyMult(id, EW_PENALTY_MULT); //Remove if this conflicts with any ECM changes!
		}
		
		stats.getMissileGuidance().modifyFlat(id, GUIDANCE_IMPROVEMENT);
		
//		stats.getMissileMaxSpeedBonus().modifyPercent(id, 10f);
//		stats.getMissileAccelerationBonus().modifyPercent(id, 100f);
//		stats.getMissileMaxTurnRateBonus().modifyPercent(id, 10f);
//		stats.getMissileTurnAccelerationBonus().modifyPercent(id, 50f);
		
		stats.getMissileMaxSpeedBonus().modifyPercent(id, MISSILE_SPEED_BONUS);
		stats.getMissileWeaponRangeBonus().modifyMult(id, MISSILE_RANGE_MULT);
		stats.getMissileAccelerationBonus().modifyPercent(id, MISSILE_ACCEL_BONUS);
		stats.getMissileMaxTurnRateBonus().modifyPercent(id, MISSILE_RATE_BONUS);
		stats.getMissileTurnAccelerationBonus().modifyPercent(id, MISSILE_TURN_ACCEL_BONUS);

		//stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_PENALTY_MOD).modifyFlat(id, -EW_PENALTY_REDUCTION);
		
		//stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_PENALTY_MAX_FOR_SHIP_MOD).modifyFlat(id, -MAX_EW_PENALTY_MOD);
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Fully negates the effect of ECM and flares on missiles fired by this ship.", Misc.getGrayColor(), 10f);
			return;
		} else if (ship.getVariant().getSMods().contains("eccm")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Fully negates the effect of ECM and flares on missiles fired by this ship.", Misc.getPositiveHighlightColor(), 10f);
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("eccm")) {
			tooltip.addSectionHeading("Built-in bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Fully negates the effect of ECM and flares on missiles fired by this ship.", Misc.getPositiveHighlightColor(), 10f);
        } else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Fully negates the effect of ECM and flares on missiles fired by this ship.", Misc.getGrayColor(), 10f);
		}
    }
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) (ECCM_CHANCE * 100f) + "%";
		if (index == 1) return "" + (int) (MISSILE_SPEED_BONUS) + "%";
		if (index == 2) return "" + (int) (MISSILE_RATE_BONUS) + "%";
		if (index == 3) return "" + (int) ((1f - EW_PENALTY_MULT) * 100f) + "%";
		//if (index == 3) return "" + (int) EW_PENALTY_REDUCTION + "";
		return null;
	}


}



