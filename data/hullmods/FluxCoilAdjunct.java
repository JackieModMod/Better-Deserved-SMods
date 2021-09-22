package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

@SuppressWarnings("unchecked")
public class FluxCoilAdjunct extends BaseHullMod {

	public static final float OVERLOAD_REDUCTION = 20f;
	
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 600f);
		mag.put(HullSize.DESTROYER, 1200f);
		mag.put(HullSize.CRUISER, 1800f);
		mag.put(HullSize.CAPITAL_SHIP, 3000f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("fluxcoil") || stats.getVariant().getHullSpec().isBuiltInMod("fluxcoil")) {
			stats.getOverloadTimeMod().modifyMult(id, 1f - OVERLOAD_REDUCTION / 100f);
		}
		stats.getFluxCapacity().modifyFlat(id, (Float) mag.get(hullSize));
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s overload duration.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("fluxcoil")) {
			tooltip.addPara("S-mod Bonus: %s overload duration.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-20" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("fluxcoil")) {
			tooltip.addPara("Built-in Bonus: %s overload duration.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-20" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: %s overload duration.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20" + "%");
		}
    }

}
