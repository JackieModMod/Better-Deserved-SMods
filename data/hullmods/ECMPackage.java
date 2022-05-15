package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ECMPackage extends BaseHullMod {

	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 1f);
		mag.put(HullSize.DESTROYER, 2f);
		mag.put(HullSize.CRUISER, 3f);
		mag.put(HullSize.CAPITAL_SHIP, 4f);
	}
	
	public static final float EW_PENALTY_MULT = 0f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("ecm") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("ecm"))) {
			stats.getBallisticWeaponRangeBonus().modifyPercent(id, 5f);
			stats.getEnergyWeaponRangeBonus().modifyPercent(id, 5f);
			stats.getDynamic().getStat(Stats.ELECTRONIC_WARFARE_PENALTY_MULT).modifyMult(id, EW_PENALTY_MULT);
		}
		stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(id, (Float) mag.get(hullSize));
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue() + "%";
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Extends the range of ballistic and energy weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Bonus: Immune from weapon range reduction due to superior enemy Electronic Warfare.", Misc.getGrayColor(), 10f);
			return;
		} else if (ship.getVariant().getSMods().contains("ecm")) {
			tooltip.addPara("S-mod Bonus: Extends the range of ballistic and energy weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Bonus: Immune from weapon range reduction due to superior enemy Electronic Warfare.", Misc.getPositiveHighlightColor(), 10f);
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("ecm")) {
                        tooltip.addPara("Built-in Bonus: Extends the range of ballistic and energy weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("Built-in Bonus: Immune from weapon range reduction due to superior enemy Electronic Warfare.", Misc.getPositiveHighlightColor(), 10f);
                } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Extends the range of ballistic and energy weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5" + "%");
			tooltip.addPara("S-mod Bonus: Immune from weapon range reduction due to superior enemy Electronic Warfare.", Misc.getGrayColor(),10f);
		}
    }
}




