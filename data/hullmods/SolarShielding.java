package data.hullmods;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class SolarShielding extends BaseLogisticsHullMod {

	public static final float CORONA_EFFECT_REDUCTION = 0.25f;
	public static final float ENERGY_DAMAGE_REDUCTION = 0.8f;
	public static final float SENERGY_DAMAGE_REDUCTION = 0.7f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("solar_shielding") || stats.getVariant().getHullSpec().isBuiltInMod("solar_shielding")) {
			stats.getEnergyDamageTakenMult().modifyMult(id, SENERGY_DAMAGE_REDUCTION);
			stats.getEnergyShieldDamageTakenMult().modifyMult(id, SENERGY_DAMAGE_REDUCTION);
		} else {
			stats.getEnergyDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);
			stats.getEnergyShieldDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);
			//stats.getBeamDamageTakenMult().modifyMult(id, BEAM_DAMAGE_REDUCTION);
		}
		stats.getDynamic().getStat(Stats.CORONA_EFFECT_MULT).modifyMult(id, CORONA_EFFECT_REDUCTION);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) Math.round((1f - CORONA_EFFECT_REDUCTION) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round((1f - ENERGY_DAMAGE_REDUCTION) * 100f) + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Energy damage reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("solar_shielding")) {
			tooltip.addPara("S-mod Bonus: Energy damage reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "30" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("solar_shielding")) {
			tooltip.addPara("Built-in Bonus: Energy damage reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "30" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Energy damage reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30" + "%");
		}
    }

}
