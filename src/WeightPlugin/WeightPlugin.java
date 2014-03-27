package WeightPlugin;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import net.minecraft.server.v1_6_R3.ItemStack;
import net.minecraft.server.v1_6_R3.PlayerInventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

public class WeightPlugin extends JavaPlugin implements Listener{
	float NormalSpeed = 1;
	float MaxWeight = 1;
	HashMap<Integer,Float> Weights = new HashMap<Integer,Float>();
	
    @Override
    public void onEnable() {
    	this.getServer().getPluginManager().registerEvents(this, this);
    	LoadYAML();
        // TODO Insert logic to be performed when the plugin is enabled
    	
    }
 
    @Override
    public void onDisable() {
    	SaveYAML();
        // TODO Insert logic to be performed when the plugin is disabled
    } 
    
    @EventHandler(priority = EventPriority.LOW)
    public void whenThePlayerMoves(final PlayerMoveEvent event) {
    }
     
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer(); // The player who joined
        PlayerInventory inventory = (PlayerInventory) player.getInventory(); // The player's inventory
        player.setWalkSpeed(NormalSpeed * (1- (MaxWeight/CalculateWeight(inventory)) ));
    }
    public float GetWeightFromList(int tag)
    {
    	float weight = 1;
    	if(!Weights.containsKey(tag))
    	{
    		Weights.put(tag, weight);
    	}
    	weight = Weights.get(tag);
    	return weight;
    }
    public float CalculateWeight(PlayerInventory inventory)
    {
    	float Weight = 0;
    	for(ItemStack armor : inventory.armor)
    	{
    		Weight += Weights.get(armor.id);
    	}
    	for(ItemStack item : inventory.items)
    	{
    		Weight += GetWeightFromList(item.id) * item.count;
    	}
    	return Weight;
    }
    void SaveYAML(){
        Yaml yaml = new Yaml();
        String output = yaml.dump(Weights);
        if(!getDataFolder().exists())
    	{
    		this.getDataFolder().mkdirs();
    	}
        File weightfile = new File(getDataFolder(), "weightconfig.txt");
    	if(!weightfile.exists())
    	{
	    	try {
	    		weightfile.createNewFile();
	            FileWriter fw = new FileWriter(weightfile, true);
	            
	            PrintWriter pw = new PrintWriter(fw);
	             
	            pw.println(output);
	             
	            pw.flush();
	             
	            pw.close();
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
    	}
    }
    @SuppressWarnings("unchecked")
	void LoadYAML()
    {
    	if(!getDataFolder().exists())
    	{
    		this.getDataFolder().mkdirs();
    	}
    	File weightfile = new File(getDataFolder(), "data.txt");
    	if(weightfile.exists())
    	{
	    	try {
	            Yaml yaml = new Yaml();
	    		weightfile.createNewFile();
	            FileReader fw = new FileReader(weightfile);
	           	Weights = (HashMap<Integer, Float>) yaml.load(fw);
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
    	}
    }

}
