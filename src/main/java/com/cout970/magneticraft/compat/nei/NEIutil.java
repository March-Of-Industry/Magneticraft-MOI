package com.cout970.magneticraft.compat.nei;

import codechicken.lib.gui.GuiDraw;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class NEIutil
{
    public static boolean stackMatchesObject(ItemStack stack, Object o)
    {
        return stackMatchesObject(stack, o, false);
    }
    public static boolean stackMatchesObject(ItemStack stack, Object o, boolean checkNBT)
    {
        if(o instanceof ItemStack)
            return OreDictionary.itemMatches((ItemStack)o, stack, false) && (!checkNBT || ((ItemStack)o).getItemDamage()==OreDictionary.WILDCARD_VALUE || ItemStack.areItemStackTagsEqual((ItemStack)o, stack));
        else if(o instanceof ArrayList)
        {
            for(Object io : (ArrayList)o)
                if(io instanceof ItemStack && OreDictionary.itemMatches((ItemStack)io, stack, false) && (!checkNBT || ((ItemStack)io).getItemDamage()==OreDictionary.WILDCARD_VALUE || ItemStack.areItemStackTagsEqual((ItemStack)io, stack)))
                    return true;
        }
        else if(o instanceof ItemStack[])
        {
            for(ItemStack io : (ItemStack[])o)
                if(OreDictionary.itemMatches(io, stack, false) && (!checkNBT || io.getItemDamage()== OreDictionary.WILDCARD_VALUE || ItemStack.areItemStackTagsEqual(io, stack)))
                    return true;
        }

        return false;
    }

    public static void drawSlot(int x, int y, int w, int h)
    {
        drawSlot(x,y, w,h, 0xff);
    }
    public static void drawSlot(int x, int y, int w, int h, int alpha)
    {
        GuiDraw.drawRect(x+8-w/2  , y+8-h/2-1, w,1, (alpha<<24)+0x373737);
        GuiDraw.drawRect(x+8-w/2-1, y+8-h/2-1, 1,h+1, (alpha<<24)+0x373737);
        GuiDraw.drawRect(x+8-w/2  , y+8-h/2  , w,h, (alpha<<24)+0x8b8b8b);
        GuiDraw.drawRect(x+8-w/2  , y+8+h/2  , w+1,1, (alpha<<24)+0xffffff);
        GuiDraw.drawRect(x+8+w/2  , y+8-h/2  , 1,h, (alpha<<24)+0xffffff);
    }

    public static void drawTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (x), (double) (y + height), (double) 0, (double) icon.getMinU(), (double) icon.getMaxV());
        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), (double) 0, (double) icon.getMaxU(), (double) icon.getMaxV());
        tessellator.addVertexWithUV((double) (x + width), (double) (y), (double) 0, (double) icon.getMaxU(), (double) icon.getMinV());
        tessellator.addVertexWithUV((double) (x), (double) (y), (double) 0, (double) icon.getMinU(), (double) icon.getMinV());
        tessellator.draw();
    }
}
