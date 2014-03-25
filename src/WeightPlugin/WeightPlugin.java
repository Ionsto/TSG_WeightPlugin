package WeightPlugin;

import net.minecraft.server.v1_6_R3.ItemStack;
import net.minecraft.server.v1_6_R3.PlayerInventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WeightPlugin extends JavaPlugin implements Listener{
    @Override
    public void onEnable() {
    	this.getServer().getPluginManager().registerEvents(this, this);
        // TODO Insert logic to be performed when the plugin is enabled
    }
 
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }   
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer(); // The player who joined
        PlayerInventory inventory = (PlayerInventory) player.getInventory(); // The player's inventory
        
        /*ItemStack itemstack = new ItemStack(Material.DIAMOND, 64); // A stack of diamonds
     
        if (inventory.contains(itemstack)) {
            inventory.addItem(itemstack); // Adds a stack of diamonds to the player's inventory
            player.sendMessage("Welcome! You seem to be reeeally rich, so we gave you some more diamonds!");
        }*/
    }
    public float CalculateWeight(PlayerInventory inventory)
    {
    	float Weight = 0;
    	for(ItemStack armor : inventory.armor)
    	{
    		Weight += armor.
    	}
    	return Weight;
    }

}
