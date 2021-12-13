package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ArmoredWeapons extends BaseHullMod {

	public static final float HEALTH_BONUS = 100f;
	public static final float ARMOR_BONUS = 10f;
	public static final float TURN_PENALTY = 25f;
	public static final float RECOIL_BONUS = 25f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("armoredweapons") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("armoredweapons"))) {
			stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS+5f);
			stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS+50f);
		} else {
			stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
			stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		}
		stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getWeaponTurnRateBonus().modifyMult(id, 1f - TURN_PENALTY * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 1) return "" + (int) TURN_PENALTY + "%";
		if (index == 2) return "" + (int) ARMOR_BONUS + "%";
		if (index == 3) return "" + (int) RECOIL_BONUS + "%";
		return null;
	}

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Weapon durability bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "150" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("armoredweapons")) {
			tooltip.addPara("S-mod Bonus: Weapon durability bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "150" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("armoredweapons")) {
			tooltip.addPara("Built-in Bonus: Weapon durability bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "150" + "%");
			tooltip.addPara("Built-in Bonus: Armor bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Weapon durability bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "150" + "%");
			tooltip.addPara("S-mod Bonus: Armor bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
		}
    }
	
}
