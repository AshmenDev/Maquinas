package eventos;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ashmen.ashmaquinas.Main;

public class MaquinaDiamond implements Listener {
	
	public static ArrayList<Player> csDiamond = new ArrayList<>();
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		if(e.getPlayer() instanceof Player){
			Player p = e.getPlayer();
			ItemStack hand = p.getItemInHand();
			if(hand.hasItemMeta()){
				if(hand.getItemMeta().hasDisplayName()){
					if(hand.getItemMeta().getDisplayName().equals("§f§k!!!!! §b§lMAQUINA DIAMOND §f§k!!!!!")){
						Block bloco = e.getBlockPlaced();
						bloco.setMetadata("MaquinaDiamond", new FixedMetadataValue(Main.pl, 10));
						p.sendMessage("§b§lMAQUINA §3Você colocou a maquina diamond.");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Player p = e.getPlayer();
		ItemStack hand = p.getItemInHand();
		Block bloco = p.getTargetBlock(null, 10);
		if(bloco.hasMetadata("MaquinaDiamond")){
			if(hand.hasItemMeta()){
				if(hand.getItemMeta().hasDisplayName()){
					if(hand.getItemMeta().getDisplayName().equals("§a§lCOMBUSTIVEL")){
						if(csDiamond.contains(p)){
							p.sendMessage("§cMaquina de diamond já esta ativada.");
							return;
						}else{
							iniciarMaquina(bloco.getLocation().add(+5.0, 0, +5.0), p, bloco);
							csDiamond.add(p);
						}
					}
				}
			}
		}
	}
	
	public static void iniciarMaquina(Location l, Player p, Block b){
		Villager vill = (Villager) p.getLocation().getWorld().spawnEntity(l, EntityType.VILLAGER);
		vill.setBaby();
		vill.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99*99*99, 127));
		vill.setCustomNameVisible(true);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.pl, new Runnable() {
			int segundos = 60;
			public void run() {
				if(segundos >= 0){
					vill.setCustomName("§bRestam §f" + segundos + " §bsegundo(s)");
					b.getWorld().dropItem(b.getLocation(), new ItemStack(Material.DIAMOND));
					segundos--;
				}
				if(segundos == 0){
					p.sendMessage("§b§lMAQUINA §3Maquina de diamond expirada.");
					vill.remove();
					csDiamond.remove(p);
				}
			}
		}, 0, 20);
	}

}
