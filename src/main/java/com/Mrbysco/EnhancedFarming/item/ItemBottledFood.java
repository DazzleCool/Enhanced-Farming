package com.Mrbysco.EnhancedFarming.item;

import com.Mrbysco.EnhancedFarming.EnhancedFarming;
import com.Mrbysco.EnhancedFarming.Reference;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemBottledFood extends ItemFood{
	
	public boolean eatAction;

	private int useTime;
	private boolean directheal; 
	private boolean cure;
	private int amount;
	
	public ItemBottledFood(int amount, float saturation, int stacksize, int useTime, boolean directHeal, boolean cureEffects, String registryName) {
		super(amount, saturation, false);
		this.maxStackSize=stacksize;
		setCreativeTab(EnhancedFarming.tabFarming);
        this.setCreativeTab(CreativeTabs.FOOD);

		this.setUnlocalizedName(Reference.MOD_PREFIX + registryName.replaceAll("_", ""));
		this.setRegistryName(registryName);
		
		this.amount = amount;
		this.useTime = useTime;
		this.directheal = directHeal;
		this.cure = cureEffects;
		this.eatAction = false;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
		ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
		
        if (!worldIn.isRemote && cure) entityLiving.curePotionEffects(stack);
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
        	InventoryPlayer playerInv = entityplayer.inventory;
            entityplayer.getFoodStats().addStats(this, stack);
            worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            
            if (this.directheal)
            {
            	entityplayer.heal(this.amount);
            }
            else
            {
                this.onFoodEaten(stack, worldIn, entityplayer);	
            }

            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
            
            if(playerInv.getFirstEmptyStack() == -1)
			{
				entityplayer.entityDropItem(bottle, 0F);
			}
			else
			{
				playerInv.addItemStackToInventory(bottle);
			}
        }

        stack.shrink(1);
        return stack;
    }
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return this.useTime;
	}
	
	public ItemBottledFood setEatAction(){
		this.eatAction = true;
		return this;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
		if(this.eatAction = true)
		{
			return EnumAction.EAT;
		}
		else
		{
			return EnumAction.DRINK;
		}
    }
	
	@Override
	public ItemFood setAlwaysEdible() {
		return super.setAlwaysEdible();
	}
}