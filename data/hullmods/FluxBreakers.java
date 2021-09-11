package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class FluxBreakers extends BaseHullMod {

	public static final float FLUX_RESISTANCE = 50f;
	public static final float DISSIPATION_BONUS = 5f;
	public static final float VENT_RATE_BONUS = 25f;
	public static final float SVENT_RATE_BONUS = 20f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getEmpDamageTakenMult().modifyMult(id, 1f - FLUX_RESISTANCE * 0.01f);
		if (stats.getVariant().getSMods().contains("fluxbreakers") || stats.getVariant().getHullSpec().isBuiltInMod("fluxbreakers")) {
			stats.getFluxDissipation().modifyPercent(id,DISSIPATION_BONUS);
			stats.getVentRateMult().modifyPercent(id, SVENT_RATE_BONUS);
		} else {
			stats.getVentRateMult().modifyPercent(id, VENT_RATE_BONUS);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) FLUX_RESISTANCE + "%";
		if (index == 1) return "" + (int) VENT_RATE_BONUS + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: +%s Flux Dissipation.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Penalty: Flux Dissipation Bonus while venting reduced to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20" + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("fluxbreakers")) {
			tooltip.addPara("S-mod Bonus: +%s Flux Dissipation.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Penalty: Flux Dissipation Bonus while venting reduced to %s.", 10f, Misc.getNegativeHighlightColor(), Misc.getHighlightColor(), "20" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("fluxbreakers")) {
			tooltip.addPara("Built-in Bonus: +%s Flux Dissipation.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("Built-in Penalty: Flux Dissipation Bonus while venting reduced to %s.", 10f, Misc.getNegativeHighlightColor(), Misc.getHighlightColor(), "20" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: +%s Flux Dissipation.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Penalty: Flux Dissipation Bonus while venting reduced to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20" + "%");
		}
    }

}
