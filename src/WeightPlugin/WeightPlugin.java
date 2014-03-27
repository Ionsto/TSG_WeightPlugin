package WeightPlugin;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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
	HashMap<String,Float> Weights = new HashMap<String,Float>();
	
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
    public float ZDiv(float x,float y)
    {
    	if(x == 0 || y == 0)
    	{
    		return 0;
    	}
    	return x/y;
    }
    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent evt) {
        Player player = evt.getPlayer(); // The player who joined
        org.bukkit.inventory.PlayerInventory inventory = player.getInventory(); // The player's inventory
        float speed = NormalSpeed * (1- ZDiv(MaxWeight,CalculateWeight(inventory)));
        player.setWalkSpeed(speed);
        System.out.println(speed);
    }
    public float GetWeightFromList(String tag)
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
    	if(inventory.getArmorContents() != null)
    	{
	    	for(ItemStack armor : inventory.getArmorContents())
	    	{
	    		if(armor != null)
	    		{
	    			if(armor.getItemMeta() != null)
	    			{
	    				Weight += GetWeightFromList(armor.getItemMeta().getDisplayName());
	    			}
	    		}
	    	}
    	}
    	if(inventory.getContents() != null)
    	{
	    	for(ItemStack item : inventory.getContents())
	    	{
	    		if(item != null)
	    		{
	    			if(item.getItemMeta() != null)
	    			{
	    				Weight += GetWeightFromList(item.getItemMeta().getDisplayName()) * item.getAmount();
	    			}
	    		}
	    	}
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
	           	Weights = (HashMap<String, Float>) yaml.load(fw);
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
    	}
    }

}
