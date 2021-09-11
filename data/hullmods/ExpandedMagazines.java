package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.Map;

public class ExpandedMagazines extends BaseHullMod {

	/*Thank you PureTilt!*/
	public static final float AMMO_BONUS = 50f;
	public static final float fireRate = 50f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBallisticAmmoBonus().modifyPercent(id, AMMO_BONUS);
		stats.getEnergyAmmoBonus().modifyPercent(id, AMMO_BONUS);
		if (stats.getVariant().getSMods().contains("magazines") || stats.getVariant().getHullSpec().isBuiltInMod("magazines")) {
			stats.getDynamic().getStat("ENERGY_AMMO_REGEN").modifyMult("ExpandedMagazines", 1 + fireRate * 0.01f);
			stats.getDynamic().getStat("BALLISTIC_AMMO_REGEN").modifyMult("ExpandedMagazines", 1 + fireRate * 0.01f);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) AMMO_BONUS + "%";
		return null;
	}

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addPara("S-mod Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50" + "%");
			return;
		} else if (ship.getMutableStats().getVariant().getSMods().contains("magazines")) {
			tooltip.addPara("S-mod Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "50" + "%");
		} else if (ship.getHullSpec().isBuiltInMod("magazines")) {
 			tooltip.addPara("Built-in Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "50" + "%");
        } else if (!isForModSpec) {
			tooltip.addPara("S-mod Bonus: If applicable, increase the reload speed for ballistic and energy weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50" + "%");
		}
    }
	
    public void advanceInCombat(ShipAPI ship, float amount) {
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
    }

}
