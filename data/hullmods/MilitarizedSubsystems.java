package data.hullmods;

import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class MilitarizedSubsystems extends BaseLogisticsHullMod {

	private static int BURN_LEVEL_BONUS = 1;
	private static float MAINTENANCE_PERCENT = 100;
	private static float SFLUX_PERCENT = 15;
	private static float SARMOR_BONUS = 15;
	private static float FLUX_DISSIPATION_PERCENT = 10;
	private static float ARMOR_BONUS = 10;
	
	//private static final float DEPLOY_COST_MULT = 0.7f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getSensorStrength().unmodify(HullMods.CIVGRADE);
		stats.getSensorProfile().unmodify(HullMods.CIVGRADE);
		
		stats.getMaxBurnLevel().modifyFlat(id, BURN_LEVEL_BONUS);

		float mult = getEffectMult(stats);
		if (stats.getVariant().getSMods().contains("militarized_subsystems") || stats.getVariant().getHullSpec().isBuiltInMod("militarized_subsystems")) {
			stats.getFluxDissipation().modifyPercent(id, SFLUX_PERCENT * mult);
			stats.getEffectiveArmorBonus().modifyFlat(id, SARMOR_BONUS * mult);
		} else {
			stats.getFluxDissipation().modifyPercent(id, FLUX_DISSIPATION_PERCENT * mult);
			stats.getEffectiveArmorBonus().modifyFlat(id, ARMOR_BONUS * mult);
		}
		
		//stats.getSuppliesPerMonth().modifyPercent(id, MAINTENANCE_PERCENT);
		stats.getMinCrewMod().modifyPercent(id, MAINTENANCE_PERCENT);
		
		//stats.getDynamic().getMod(Stats.ACT_AS_COMBAT_SHIP).modifyFlat(id, 1f);
		
	}
	
	public static float getEffectMult(MutableShipStatsAPI stats) {
		float bonus = getBonusPercent(stats);
		return 1f + bonus / 100f;
	}
	
	//public static float getBonusPercent(ShipAPI ship) {
	public static float getBonusPercent(MutableShipStatsAPI stats) {
		if (Global.getSettings().getCurrentState() == GameState.TITLE) return 0f;
		//FleetMemberAPI member = ship.getFleetMember();
		MutableCharacterStatsAPI cStats = null;
		if (stats == null) {
			cStats = Global.getSector().getPlayerStats();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return 0f;
			PersonAPI commander = member.getFleetCommanderForStats();
			if (commander == null) {
				commander = member.getFleetCommander();
			}
			if (commander == null) return 0f;
			cStats = commander.getStats();
		}
		float bonus = cStats.getDynamic().getMod(Stats.AUXILIARY_EFFECT_ADD_PERCENT).computeEffective(0f);
		return Math.round(bonus);
	}
	
//	@Override
//	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
//		if (ship == null) return;
//		if (Global.getSettings().getCurrentState() == GameState.TITLE) return;
//		
//		float bonus = getBonusPercent(ship);
//		if(bonus <= 0) return;
//		
//		float opad = 10f;
//		tooltip.addSectionHeading("Auxiliary Support", Alignment.MID, opad);
//	}



	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + BURN_LEVEL_BONUS;
		//if (index == 1) return "" + (int)Math.round((1f - DEPLOY_COST_MULT) * 100f) + "%";
		float mult = getEffectMult(null);
		if (index == 1) return "" + (int) Math.round(FLUX_DISSIPATION_PERCENT * mult) + "%";
		if (index == 2) return "" + (int) Math.round(ARMOR_BONUS * mult);
		if (index == 3) return "" + (int)Math.round(MAINTENANCE_PERCENT) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Flux dissipation bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus for the damage reduction calculation increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15");
			return;
		} else if (ship.getVariant().getSMods().contains("militarized_subsystems")) {
			tooltip.addPara("S-mod Bonus: Flux dissipation bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus for the damage reduction calculation increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15");
		} else if (ship.getHullSpec().isBuiltInMod("militarized_subsystems")) {
			tooltip.addPara("Built-in Bonus: Flux dissipation bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("Built-in Bonus: Armor bonus for the damage reduction calculation increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Flux dissipation bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus for the damage reduction calculation increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15");
		}
    }

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().hasHullMod(HullMods.CIVGRADE) && super.isApplicableToShip(ship);
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		if (!ship.getVariant().hasHullMod(HullMods.CIVGRADE)) {
			return "Can only be installed on civilian-grade hulls";
		}
		return super.getUnapplicableReason(ship);
	}
	
	
	
	
}

