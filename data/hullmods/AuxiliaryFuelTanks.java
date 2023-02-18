package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AuxiliaryFuelTanks extends BaseLogisticsHullMod {

	public static final float MIN_FRACTION = 0.3f;
	
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 30f);
		mag.put(HullSize.DESTROYER, 60f);
		mag.put(HullSize.CRUISER, 100f);
		mag.put(HullSize.CAPITAL_SHIP, 200f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		float mod = (Float) mag.get(hullSize);
		if (stats.getVariant() != null) {
			mod = Math.max(stats.getVariant().getHullSpec().getFuel() * MIN_FRACTION, mod);
		}
                if (stats.getVariant().getSMods().contains("auxiliary_fuel_tanks") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("auxiliary_fuel_tanks"))) {
                    mod *= 2;
                }
		stats.getFuelMod().modifyFlat(id, mod);
		
		if (stats.getVariant().getSMods().contains("auxiliary_fuel_tanks") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("auxiliary_fuel_tanks"))) {
		} else if (stats.getVariant() != null && stats.getVariant().hasHullMod(HullMods.CIVGRADE) && !stats.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) {
			stats.getSuppliesPerMonth().modifyPercent(id, AdditionalBerthing.MAINTENANCE_PERCENT);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 4) return "" + (int) Math.round(MIN_FRACTION * 100f) + "%";
		if (index == 5) return "" + (int)Math.round(AdditionalBerthing.MAINTENANCE_PERCENT) + "%";
		return null;
	}
        
        public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
                     if (isForModSpec) {
                             tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
                             tooltip.addPara("Doubles the fuel capacity increase and, for civilian hulls, negates the maintenance cost increase.", Misc.getGrayColor(), 10f);
                             return;
                     } else if (ship.getVariant().getSMods().contains("auxiliary_fuel_tanks")) {
                             tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);	
                             tooltip.addPara("Doubles the fuel capacity increase and, for civilian hulls, negates the maintenance cost increase.", Misc.getPositiveHighlightColor(), 10f);
                     } else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("auxiliary_fuel_tanks")) {
                             tooltip.addSectionHeading("Built-in bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
                             tooltip.addPara("Doubles the fuel capacity increase and, for civilian hulls, negates the maintenance cost increase.", Misc.getPositiveHighlightColor(), 10f);
             } else if (!isForModSpec) {
                             tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
                             tooltip.addPara("Doubles the fuel capacity increase and, for civilian hulls, negates the maintenance cost increase.", Misc.getGrayColor(), 10f);
                     }
         }

}




