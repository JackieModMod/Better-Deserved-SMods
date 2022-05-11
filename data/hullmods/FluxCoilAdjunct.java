package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

@SuppressWarnings("unchecked")
public class FluxCoilAdjunct extends BaseHullMod {
	
	public static final float VENT_BONUS = 2f;
	private static Map mag = new HashMap();
	private static Map smag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 600f);
		mag.put(HullSize.DESTROYER, 1200f);
		mag.put(HullSize.CRUISER, 1800f);
		mag.put(HullSize.CAPITAL_SHIP, 3000f);
		smag.put(HullSize.FRIGATE, 1000f);
		smag.put(HullSize.DESTROYER, 2000f);
		smag.put(HullSize.CRUISER, 3000f);
		smag.put(HullSize.CAPITAL_SHIP, 5000f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("fluxcoil") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("fluxcoil"))) {
			stats.getFluxCapacity().modifyFlat(id, (Float) smag.get(hullSize));
		} else {
			stats.getFluxCapacity().modifyFlat(id, (Float) mag.get(hullSize));
		}
	}
	
	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		if ((ship.getVariant().getSMods().contains("fluxcoil") || (Global.getSettings().getBoolean("BuiltInSMod") && ship.getVariant().getHullSpec().isBuiltInMod("fluxcoil"))) && ship.getFluxTracker().isOverloaded()) {
			stats.getFluxDissipation().modifyMult("FluxCoilAdjunct", VENT_BONUS);
			CombatEngineAPI engine = Global.getCombatEngine();
			if (engine.getPlayerShip() == ship) {
				engine.maintainStatusForPlayerShip("FluxCoilAdjunct", "graphics/icons/tactical/overloaded.png",
                            "Flux Coil Adjunct", "+100% Flux Dissipation", false);
			}
		} else {
			stats.getFluxDissipation().unmodifyMult("FluxCoilAdjunct");
		}
		
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
			tooltip.addPara("S-mod Bonus: Increases the ship's flux capacity by an additional %s/%s/%s/%s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "400", "800", "1200", "2000");
			tooltip.addPara("S-mod Bonus: Increases the ship's flux dissipation rate while overloaded by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100%");
			return;
		} else if (ship.getVariant().getSMods().contains("fluxcoil")) {
			tooltip.addPara("S-mod Bonus: Increases the ship's flux capacity by an additional %s/%s/%s/%s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "400", "800", "1200", "2000");
			tooltip.addPara("S-mod Bonus: Increases the ship's flux dissipation rate while overloaded by %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("fluxcoil")) {
			tooltip.addPara("Built-in Bonus: Increases the ship's flux capacity by an additional %s/%s/%s/%s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "400", "800", "1200", "2000");
			tooltip.addPara("Built-in Bonus: Increases the ship's flux dissipation rate while overloaded by %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the ship's flux capacity by an additional %s/%s/%s/%s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "400", "800", "1200", "2000");
			tooltip.addPara("S-mod Bonus: Increases the ship's flux dissipation rate while overloaded by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100%");
		}
    }

}
