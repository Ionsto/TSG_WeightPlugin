package WeightPlugin;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
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
	float NormalSpeed = 0.5F;
	float Inventory = (36 * 64) + 4;//Estimate
	float Hev = 1;
	float MaxWeight = Inventory * Hev;
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
        System.out.println(CalculateWeight(inventory));
        float speed = NormalSpeed * (ZDiv(CalculateWeight(inventory),MaxWeight)-1);
        player.setWalkSpeed(speed);
    }
    public float GetWeight(ItemStack item)
    {
    	float weight = 1;
    	if(!Weights.containsKey(GetTagFromItem(item)))
    	{
    		float rep = GetWeightFromRecipe(item);
    		if(rep != -1)
    		{
    			weight = rep;
    		}
    		Weights.put(GetTagFromItem(item), weight);
    	}
    	weight = Weights.get(GetTagFromItem(item));
    	if(weight > Hev){Hev = weight;MaxWeight = Inventory * Hev;}
    	return weight;
    }
    public float GetWeightFromRecipe(ItemStack item)
    {
    	float weight = -1;
    	List<Recipe> recipes =  this.getServer().getRecipesFor(item);
    	for(Recipe r : recipes)
    	{
    		if(weight == -1){++weight;}
    		if(r instanceof ShapelessRecipe)
    		{
    			for ( ItemStack tems :((ShapelessRecipe)r).getIngredientList())
    			{
    				weight += GetWeight(tems) * tems.getAmount();
    			}
    		}
    		if(r instanceof ShapedRecipe)
    		{
    			for ( ItemStack tems :((ShapedRecipe)r).getIngredientMap().values())
    			{
    				weight += GetWeight(tems) * tems.getAmount();
    			}
    		}
    	}
    	return weight;
    }
    public String GetTagFromItem(ItemStack stack)
    {
		return stack.getType().name();
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
	    				Weight += GetWeight(armor);
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
	    				Weight += GetWeight(item) * item.getAmount();
	    			}
	    		}
	    	}
    	}
    	return Weight;
    }
    void SaveYAML(){
    	System.out.println("Saving YAML");
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
    	System.out.println("Loading YAML");
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
	    for(float weight : Weights.values())
	    {
	    	if(weight > Hev){Hev = weight;MaxWeight = Inventory * Hev;}
	    }
    }
}
