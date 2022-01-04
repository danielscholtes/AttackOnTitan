package com.aotmc.attackontitan.titans;

import com.aotmc.attackontitan.AttackOnTitan;
import com.aotmc.attackontitan.blades.BladeType;
import com.aotmc.attackontitan.materials.MaterialData;
import com.aotmc.attackontitan.odmgear.ODMData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.aotmc.attackontitan.general.util.Utils;
import com.codeitforyou.lib.api.item.ItemUtil;

public class TitanEvents implements Listener {
	
	private TitanData titanData;
	private ODMData odmData;
	private MaterialData materialData;
	
	public TitanEvents(TitanData titanData, ODMData odmData, MaterialData materialData) {
		this.titanData = titanData;
		this.odmData = odmData;
		this.materialData = materialData;
	}

	@EventHandler
	public void onTarget(EntityTargetLivingEntityEvent event) {
		if (!(event.getTarget() instanceof Player) && event.getEntity() instanceof Giant) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onTitanHit(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Slime)) {
			return;
		}
		
		if (!titanData.getTitans().containsKey(event.getEntity().getEntityId())) {
			return;
		}
		event.setCancelled(true);
		
		if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
			Player player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
			
			if (titanData.getGrabbedPlayers() != null && titanData.getGrabbedPlayers().containsKey(player.getUniqueId())) {
				event.setCancelled(true);
				return;
			}

			if (!Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getItemInMainHand(), "blade"))) {
				return;
			}
			
			double damage = BladeType.find(Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getItemInMainHand(), "tier"))).get().getDamage();
			if (Boolean.valueOf(ItemUtil.getNBTString(player.getInventory().getItemInOffHand(), "blade"))) {
				damage += BladeType.find(Integer.parseInt(ItemUtil.getNBTString(player.getInventory().getItemInMainHand(), "tier"))).get().getDamage();
			}
			
			Slime slime = (Slime) event.getEntity();
			player.getWorld().spawnParticle(Particle.BLOCK_CRACK, slime.getLocation().add(0D, 1.25D, 0D), 100, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
			player.getWorld().playSound(player.getLocation(), "blade", 1, 1);
			if (slime.getHealth() <= damage) {
				Bukkit.broadcastMessage(Utils.color("&7A &2" + titanData.getTitans().get(event.getEntity().getEntityId()).getSize() + "-Meter &7Titan has been slain by &2" + player.getName()));
				Titan titan = titanData.getTitans().get(slime.getEntityId());
				if (titan.getType() == TitanType.SMALL) {
					titan.getGiant().getWorld().dropItemNaturally(titan.getGiant().getLocation(), new ItemStack(Material.ROTTEN_FLESH));
				}
				if (titan.getType() == TitanType.MEDIUM) {
					titan.getGiant().getWorld().dropItemNaturally(titan.getGiant().getLocation(), new ItemStack(Material.IRON_INGOT));
				}
				if (titan.getType() == TitanType.LARGE) {
					titan.getGiant().getWorld().dropItemNaturally(titan.getGiant().getLocation(), new ItemStack(Material.DIAMOND));
				}

				giveMaterialReward(titan, player);

				titan.remove();
				titanData.getTitans().remove(slime.getEntityId());
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation(), 80);
				player.getWorld().spawnParticle(Particle.BLOCK_CRACK, slime.getLocation().add(0D, 1.25D, 0D), 250, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				player.getWorld().spawnParticle(Particle.BLOCK_CRACK, slime.getLocation().add(0D, -3D, 0D), 60, Bukkit.createBlockData(Material.REDSTONE_BLOCK));
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation().add(0D, -2D, 0D), 80);
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation().add(0D, -4D, 0D), 80);
				player.getWorld().spawnParticle(Particle.CLOUD, slime.getLocation().add(0D, -6D, 0D), 80);
				player.getWorld().playSound(slime.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.5f, 0.4f);


			} else {
				slime.setHealth(slime.getHealth() - damage);
			}
		}
	}

	// who needs configs anyway?
	private void giveMaterialReward(Titan titan, Player player) {
		if (titan.getType() == TitanType.SMALL) {
			int minSmallCrystals = 1;
			int maxSmallCrystals = 1;
			if (titan.getSize() > 3) {
				minSmallCrystals = (-3 + titan.getSize());
				maxSmallCrystals = (-2 + titan.getSize());
			}

			if (titan.getSize() == 6) {
				maxSmallCrystals = 5;
			}

			int smallCrystals = AttackOnTitan.getInstance().getRandom().nextInt((maxSmallCrystals - minSmallCrystals) + 1) + minSmallCrystals;
			materialData.getPlayerMaterials().get(player.getUniqueId()).addSmallCrystals(smallCrystals);
			if (Math.random() < 0.25 && titan.getSize() == 6) {
				materialData.getPlayerMaterials().get(player.getUniqueId()).addSmallCrystals(1);
			}

		}

		if (titan.getType() == TitanType.MEDIUM) {
			int minCrystals = (-6 + titan.getSize());
			int maxCrystals = 1;

			if (titan.getSize() == 10) {
				maxCrystals = 6;
			} else if (titan.getSize() > 7) {
				maxCrystals = (-5 + titan.getSize());
			}

			int smallCrystals = 2;
			if (titan.getSize() > 8) {
				smallCrystals = (-6 + titan.getSize());
			}
			int crystals = AttackOnTitan.getInstance().getRandom().nextInt((maxCrystals - minCrystals) + 1) + minCrystals;
			materialData.getPlayerMaterials().get(player.getUniqueId()).addSmallCrystals(smallCrystals);
			materialData.getPlayerMaterials().get(player.getUniqueId()).addCrystals(crystals);
			if ((Math.random() < 0.05 && titan.getSize() == 9) || (Math.random() < 0.1 && titan.getSize() == 10)) {
				materialData.getPlayerMaterials().get(player.getUniqueId()).addLargeCrystals(1);
			}

		}

		if (titan.getType() == TitanType.LARGE) {
			int minLargeCrystals;
			int maxLargeCrystals;
			int crystals = 2;
			if (titan.getSize() < 13 || titan.getSize() == 15) {
				minLargeCrystals = (-10 + titan.getSize());
				maxLargeCrystals = minLargeCrystals;
			} else {
				crystals = (-10 + titan.getSize());
				minLargeCrystals = (-11 + titan.getSize());
				maxLargeCrystals = (-10 + titan.getSize());
			}

			int smallCrystals = (-9 + titan.getSize());
			int largeCrystals = AttackOnTitan.getInstance().getRandom().nextInt((maxLargeCrystals - minLargeCrystals) + 1) + minLargeCrystals;
			materialData.getPlayerMaterials().get(player.getUniqueId()).addSmallCrystals(smallCrystals);
			materialData.getPlayerMaterials().get(player.getUniqueId()).addCrystals(crystals);
			materialData.getPlayerMaterials().get(player.getUniqueId()).addLargeCrystals(largeCrystals);

		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Giant) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamagePlayer(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Giant) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEgg(EntityDropItemEvent event) {
		if (event.getItemDrop().getItemStack() == null || event.getItemDrop().getItemStack().getType() != Material.EGG) {
			return;
		}
		if (event.getEntity().getType() != EntityType.CHICKEN) {
			return;
		}

		Chicken chicken = (Chicken) event.getEntity();

		if (!chicken.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onDamageChicken(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Chicken)) {
			return;
		}
		for (int id : titanData.getGrabbedPlayers().values()) {
			if (id != event.getEntity().getEntityId()) {
				continue;
			}

			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onTitanAttack(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Giant)) {
			return;
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (titanData.getGrabbedPlayers() != null && titanData.getGrabbedPlayers().containsKey(event.getEntity().getUniqueId())) {
			titanData.getGrabbedPlayers().remove(event.getEntity().getUniqueId());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDismount(EntityDismountEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if (titanData.getGrabbedPlayers() == null || !titanData.getGrabbedPlayers().containsKey(event.getEntity().getUniqueId())) {
			return;
		}


		if (odmData.getWearingODM() != null && odmData.getWearingODM().containsKey(event.getEntity().getUniqueId())) {
			odmData.getWearingODM().get(event.getEntity().getUniqueId()).remove();
			odmData.getWearingODM().remove(event.getEntity().getUniqueId());
		}

		int id = titanData.getGrabbedPlayers().get(event.getEntity().getUniqueId());
		titanData.getGrabbedPlayers().remove(event.getEntity().getUniqueId());
		event.getDismounted().eject();
		Bukkit.getScheduler().runTaskLater(AttackOnTitan.getInstance(), () -> {
			titanData.getGrabbedPlayers().put(event.getEntity().getUniqueId(), id);
			event.getDismounted().addPassenger(event.getEntity());
		}, 3L);
	}


}
