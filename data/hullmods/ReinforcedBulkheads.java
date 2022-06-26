package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import java.util.Collection;
import java.util.HashSet;

public class ReinforcedBulkheads extends BaseHullMod {
	
	public static float DMOD_EFFECT_MULT = 0.5f;
	public static final float HULL_BONUS = 40f;
    private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 250f);
		mag.put(HullSize.DESTROYER, 400f);
		mag.put(HullSize.CRUISER, 500f);
		mag.put(HullSize.CAPITAL_SHIP, 1000f);
	}

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("reinforcedhull") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("reinforcedhull"))) {
			stats.getDynamic().getStat(Stats.DMOD_EFFECT_MULT).modifyMult(id, DMOD_EFFECT_MULT);
                        if (stats.getVariant().hasDMods()) {
                            Collection<String> hullmodsort = new HashSet();
                            for (String hullmod : stats.getVariant().getHullMods()) {
                                HullModSpecAPI hullmodspec = Global.getSettings().getHullModSpec(hullmod);
                                if (hullmodspec.hasTag("dmod") && hullmodspec.isHidden()) {
                                    hullmodsort.add(hullmod);
                                }
                            }
                            stats.getVariant().getHullMods().removeAll(hullmodsort);
                            stats.getVariant().getHullMods().addAll(hullmodsort);
                        }
			stats.getHullBonus().modifyFlat(id, (Float) mag.get(hullSize));
		}
        stats.getHullBonus().modifyPercent(id, HULL_BONUS);
		stats.getDynamic().getMod(Stats.INDIVIDUAL_SHIP_RECOVERY_MOD).modifyFlat(id, 1000f);
		stats.getBreakProb().modifyMult(id, 0f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HULL_BONUS + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the ship hull integrity by an additional %s/%s/%s/%s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "250", "400", "500", "1000");
			tooltip.addPara("S-mod Bonus: Reduces most negative effects of d-mods by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50%");
			return;
		} else if (ship.getVariant().getSMods().contains("reinforcedhull")) {
			tooltip.addPara("S-mod Bonus: Increases the ship hull integrity by an additional %s/%s/%s/%s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "250", "400", "500", "1000");
			tooltip.addPara("S-mod Bonus: Reduces most negative effects of d-mods by %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "50%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("reinforcedhull")) {
			tooltip.addPara("Built-in Bonus: Increases the ship hull integrity by an additional %s/%s/%s/%s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "250", "400", "500", "1000");
			tooltip.addPara("Built-in Bonus: Reduces most negative effects of d-mods by %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "50%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Increases the ship hull integrity by an additional %s/%s/%s/%s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "250", "400", "500", "1000");
			tooltip.addPara("S-mod Bonus: Reduces most negative effects of d-mods by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50%");
		}
    }
}



