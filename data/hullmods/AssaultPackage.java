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

public class AssaultPackage extends BaseHullMod {

	public static float SFLUX_PERCENT = 15f;
	public static float SHULL_PERCENT = 15f;
	public static float SARMOR_PERCENT = 7.5f;
	public static float FLUX_CAPACITY_PERCENT = 10f;
	public static float HULL_PERCENT = 10f;
	public static float ARMOR_PERCENT = 5f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		float mult = MilitarizedSubsystems.getEffectMult(stats);
		if (stats.getVariant().getSMods().contains("assault_package") || stats.getVariant().getHullSpec().isBuiltInMod("assault_package")) {
			stats.getHullBonus().modifyPercent(id, SHULL_PERCENT * mult);
			stats.getArmorBonus().modifyPercent(id, SARMOR_PERCENT * mult);
			stats.getFluxCapacity().modifyPercent(id, SFLUX_PERCENT * mult);
		} else {
			stats.getHullBonus().modifyPercent(id, HULL_PERCENT * mult);
			stats.getArmorBonus().modifyPercent(id, ARMOR_PERCENT * mult);
			stats.getFluxCapacity().modifyPercent(id, FLUX_CAPACITY_PERCENT * mult);
		}
		
		stats.getDynamic().getMod(Stats.ACT_AS_COMBAT_SHIP).modifyFlat(id, 1f);
	}
	


	public String getDescriptionParam(int index, HullSize hullSize) {
		float mult = MilitarizedSubsystems.getEffectMult(null);
		if (index == 0) return "" + (int) Math.round(HULL_PERCENT * mult) + "%";
		if (index == 1) return "" + (int) Math.round(ARMOR_PERCENT * mult) + "%";
		if (index == 2) return "" + (int)Math.round(FLUX_CAPACITY_PERCENT * mult) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Hull integrity bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "7.5" + "%");
			tooltip.addPara("S-mod Bonus: Flux capacity bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("assault_package")) {
			tooltip.addPara("S-mod Bonus: Hull integrity bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "7.5" + "%");
			tooltip.addPara("S-mod Bonus: Flux capacity bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("assault_package")) {
			tooltip.addPara("Built-in Bonus: Hull integrity bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("Built-in Bonus: Armor bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "7.5" + "%");
			tooltip.addPara("Built-in Bonus: Flux capacity bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Hull integrity bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "7.5" + "%");
			tooltip.addPara("S-mod Bonus:Flux capacity bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
		}
    }
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		if (shipHasOtherModInCategory(ship, spec.getId(), HullMods.TAG_CIV_PACKAGE)) return false;
		if (!ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) {ship.getVariant().removePermaMod(HullMods.ASSAULT_PACKAGE);}
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

