package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ExpandedMagazines extends BaseHullMod {

	/*Thank you PureTilt!*/
	public static final float AMMO_BONUS = 50f;
	public static final float fireRate = 33f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("magazines") || (Global.getSettings().getBoolean("BuiltInSMod") && stats.getVariant().getHullSpec().isBuiltInMod("magazines"))) {
			//stats.getDynamic().getStat("ENERGY_AMMO_REGEN").modifyMult("ExpandedMagazines", 1 + fireRate * 0.01f);
			//stats.getDynamic().getStat("BALLISTIC_AMMO_REGEN").modifyMult("ExpandedMagazines", 1 + fireRate * 0.01f);
                        stats.getEnergyAmmoRegenMult().modifyPercent(id, fireRate);
                        stats.getBallisticAmmoRegenMult().modifyPercent(id, fireRate);
			stats.getBallisticAmmoBonus().modifyPercent(id, AMMO_BONUS+16f);
			stats.getEnergyAmmoBonus().modifyPercent(id, AMMO_BONUS+16f);
		} else {
			stats.getBallisticAmmoBonus().modifyPercent(id, AMMO_BONUS);
			stats.getEnergyAmmoBonus().modifyPercent(id, AMMO_BONUS);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) AMMO_BONUS + "%";
		return null;
	}

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: Ammo capacity bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("S-mod Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("magazines")) {
			tooltip.addPara("S-mod Bonus: Ammo capacity bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("S-mod Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
		} else if (Global.getSettings().getBoolean("BuiltInSMod") && ship.getHullSpec().isBuiltInMod("magazines")) {
			tooltip.addPara("Built-in Bonus: Ammo capacity bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "66" + "%");
 			tooltip.addPara("Built-in Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
                } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: Ammo capacity bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("S-mod Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
		}
    }
	
    /*public void advanceInCombat(ShipAPI ship, float amount) {
        if (!ship.isAlive()) return;
        if (ship.getFullTimeDeployed() >= 0.5f) return;

        Map<String, Object> customCombatData = Global.getCombatEngine().getCustomData();
        String id = ship.getId();

        if (customCombatData.get("ExpandedMagazines" + id) instanceof Boolean) return;

        MutableShipStatsAPI stats = ship.getMutableStats();

        for (WeaponAPI w : ship.getAllWeapons()) {
            float reloadRate = w.getAmmoPerSecond();
            float ammoRegen = 1;
            if (w.isBeam()) ammoRegen *= stats.getDynamic().getStat("BALLISTIC_AMMO_REGEN").getModifiedValue();
            if (w.getType() == WeaponType.ENERGY && w.usesAmmo() && reloadRate > 0) {
                ammoRegen *= stats.getDynamic().getStat("ENERGY_AMMO_REGEN").getModifiedValue();
                w.getAmmoTracker().setAmmoPerSecond(w.getAmmoTracker().getAmmoPerSecond() * ammoRegen);
            } else if (w.getType() == WeaponType.BALLISTIC && w.usesAmmo() && reloadRate > 0){
                ammoRegen *= stats.getDynamic().getStat("BALLISTIC_AMMO_REGEN").getModifiedValue();
                w.getAmmoTracker().setAmmoPerSecond(w.getAmmoTracker().getAmmoPerSecond() * ammoRegen);
            }
        }
        customCombatData.put("ExpandedMagazines" + id, true);
    }*/

}
