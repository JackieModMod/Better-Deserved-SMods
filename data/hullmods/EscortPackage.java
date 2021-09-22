package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.MilitarizedSubsystems;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class EscortPackage extends BaseHullMod {

	private static float SMANEUVER_PERCENT = 15f;
	private static float SPD_RANGE = 45;
	public static float SFIGHTER_DAMAGE_BONUS = 15f;
	public static float SMISSILE_DAMAGE_BONUS = 15f;
	private static float MANEUVER_PERCENT = 10;
	private static float PD_RANGE = 30;
	public static float FIGHTER_DAMAGE_BONUS = 10f;
	public static float MISSILE_DAMAGE_BONUS = 10f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		float mult = MilitarizedSubsystems.getEffectMult(stats);
		if (stats.getVariant().getSMods().contains("escort_package") || stats.getVariant().getHullSpec().isBuiltInMod("escort_package")) {
			stats.getDamageToFighters().modifyFlat(id, SFIGHTER_DAMAGE_BONUS / 100f * mult);
			stats.getDamageToMissiles().modifyFlat(id, SMISSILE_DAMAGE_BONUS / 100f * mult);
			
			stats.getBeamPDWeaponRangeBonus().modifyFlat(id, SPD_RANGE * mult);
			stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, SPD_RANGE * mult);

			stats.getAcceleration().modifyPercent(id, SMANEUVER_PERCENT * mult);
			stats.getDeceleration().modifyPercent(id, SMANEUVER_PERCENT * mult);
			stats.getTurnAcceleration().modifyPercent(id, SMANEUVER_PERCENT * 2f * mult);
			stats.getMaxTurnRate().modifyPercent(id, SMANEUVER_PERCENT * mult);
		} else {
			stats.getDamageToFighters().modifyFlat(id, FIGHTER_DAMAGE_BONUS / 100f * mult);
			stats.getDamageToMissiles().modifyFlat(id, MISSILE_DAMAGE_BONUS / 100f * mult);
			
			stats.getBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE * mult);
			stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE * mult);

			stats.getAcceleration().modifyPercent(id, MANEUVER_PERCENT * mult);
			stats.getDeceleration().modifyPercent(id, MANEUVER_PERCENT * mult);
			stats.getTurnAcceleration().modifyPercent(id, MANEUVER_PERCENT * 2f * mult);
			stats.getMaxTurnRate().modifyPercent(id, MANEUVER_PERCENT * mult);
		}

		
		stats.getDynamic().getMod(Stats.ACT_AS_COMBAT_SHIP).modifyFlat(id, 1f);
	}
	


	public String getDescriptionParam(int index, HullSize hullSize) {
		float mult = MilitarizedSubsystems.getEffectMult(null);
		if (index == 0) return "" + (int) Math.round(MANEUVER_PERCENT * mult) + "%";
		if (index == 1) return "" + (int) Math.round(PD_RANGE * mult);
		if (index == 2) return "" + (int)Math.round(FIGHTER_DAMAGE_BONUS * mult) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Maneuverability bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Point-defense weapon range bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("S-mod Bonus: Damage bonus to fighter and missile increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("escort_package")) {
			tooltip.addPara("S-mod Bonus: Maneuverability bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Point-defense weapon range bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("S-mod Bonus: Damage bonus to fighter and missile increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("escort_package")) {
			tooltip.addPara("Built-in Bonus: Maneuverability bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("Built-in Bonus: Point-defense weapon range bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("Built-in Bonus: Damage bonus to fighter and missile increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Maneuverability bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Point-defense weapon range bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "45");
			tooltip.addPara("S-mod Bonus: Damage bonus to fighter and missile increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
		}
    }

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		if (shipHasOtherModInCategory(ship, spec.getId(), HullMods.TAG_CIV_PACKAGE)) return false;
		if (!ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) {ship.getVariant().removePermaMod(HullMods.ESCORT_PACKAGE);}
		return ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS);
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		if (shipHasOtherModInCategory(ship, spec.getId(), HullMods.TAG_CIV_PACKAGE)) {
			return "Can only install one combat package on a civilian-grade hull";
		}
		if (!ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) {
			return "Can only be installed on civilian-grade hulls with Militarized Subsystems";
		}
		return super.getUnapplicableReason(ship);
	}
	
	
	
}

