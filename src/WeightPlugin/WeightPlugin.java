package WeightPlugin;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.FurnaceRecipe;
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
	float NormalSpeed = 5;
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
        float speed = NormalSpeed * ZDiv(MaxWeight-(CalculateWeight(inventory)*1.0F),MaxWeight);
        System.out.println(speed);
        player.setWalkSpeed(Math.abs(speed));
    }
    public float GetWeight(ItemStack item)
    {
    	float weight = 1F;
    	if(!Weights.containsKey(GetTagFromItem(item)))
    	{
    		float rep = GetWeightFromRecipe(item);
    		if(rep != -1)
    		{
    			weight = rep;
    		}
    		Weights.put(GetTagFromItem(item), weight);
    	}
    	weight = Weights.get(GetTagFromItem(item)).floatValue();
    	if(weight > Hev){Hev = weight;MaxWeight = Inventory * Hev;}
    	return weight;
    }
    public float GetWeightFromRecipe(ItemStack item)
    {
    	float weight = -1F;
    	if(item != null)
    	{
	    	List<Recipe> recipes =  this.getServer().getRecipesFor(item);
	    	for(int i = 0;i < recipes.size();++i)
	    	{
	    		if(weight == -1){++weight;}
	    		if(recipes.get(i) instanceof ShapelessRecipe)
	    		{
	    			for ( ItemStack tems :((ShapelessRecipe)recipes.get(i)).getIngredientList())
	    			{
	    				if(tems != null)
	    				{
	    					weight += GetWeight(tems) * tems.getAmount() / recipes.get(i).getResult().getAmount();
	    				}
	    			}
	    		}
	    		if(recipes.get(i) instanceof ShapedRecipe)
	    		{
	    			for ( ItemStack tems :((ShapedRecipe)recipes.get(i)).getIngredientMap().values())
	    			{
	    				if(tems != null)
	    				{
	    					weight += GetWeight(tems) * tems.getAmount() / recipes.get(i).getResult().getAmount();
	    				}
	    			}
	    		}
	    		if(recipes.get(i) instanceof FurnaceRecipe)
	    		{
    				weight += GetWeight(((FurnaceRecipe)recipes.get(i)).getInput());
	    		}
	    	}
    	}
    	return weight;
    }
    public String GetTagFromItem(ItemStack stack)
    {
    	if(stack != null)
    	{
    		return stack.getType().name();
    	}
    	return "";
    }
    public float CalculateWeight(PlayerInventory inventory)
    {
    	float Weight = 0F;
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
        output = output.replace(",", ",\n");
        if(!getDataFolder().exists())
    	{
    		this.getDataFolder().mkdirs();
    	}
        File weightfile = new File(getDataFolder(), "weightconfig.yml");
        if(!weightfile.exists())
        {
    		try {
				weightfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	try {
            FileWriter fw = new FileWriter(weightfile,false);
            
            PrintWriter pw = new PrintWriter(fw);
            pw.println(output);
             
            pw.flush();
             
            pw.close();
    	} catch (IOException e) {
    	e.printStackTrace();
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
    	File weightfile = new File(getDataFolder(), "weightconfig.yml");
    	if(weightfile.exists())
    	{
	    	try {
	            Yaml yaml = new Yaml();
	    		weightfile.createNewFile();
	            FileReader fw = new FileReader(weightfile);
	            HashMap<String,Float> newWeights = (HashMap<String, Float>) yaml.load(fw);
		    	for(String key : newWeights.keySet())
		    	{
		    		Weights.put(key, Float.valueOf(String.valueOf(newWeights.get(key))).floatValue());
		    	}
	    	} catch (IOException e) {
	    	e.printStackTrace();
	    	}
    	}
		System.out.println(Weights.size());
    	for(int i = 0;i< Weights.size();++i)
    	{
    		String keys = (String) Weights.keySet().toArray()[i];
	    	
    		float weight = Weights.get(keys);//Float.valueOf(String.valueOf(Weights.get(keys))).floatValue();
	    	ItemStack stack = new ItemStack(Enum.valueOf(Material.class, keys));
	    	//Just recalculate it for eddited changes
	    	float w = GetWeightFromRecipe(stack);
	    	if(w != -1)
	    	{
	    		Weights.put(keys, w);
	    	}
	    	if( weight> Hev){Hev = weight;MaxWeight = Inventory * Hev;}
    	}
    }
}
