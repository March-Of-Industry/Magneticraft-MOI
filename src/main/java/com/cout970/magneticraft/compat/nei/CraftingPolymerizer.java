package com.cout970.magneticraft.compat.nei;

//import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.ClientUtils;
import buildcraft.api.core.Position;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.cout970.magneticraft.Magneticraft;
import com.cout970.magneticraft.api.access.MgRecipeRegister;
import com.cout970.magneticraft.api.access.RecipePolymerizer;
import com.cout970.magneticraft.api.util.MgUtils;
import com.cout970.magneticraft.tileentity.multiblock.controllers.TilePolymerizer;
import com.cout970.magneticraft.util.RenderUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static codechicken.lib.gui.GuiDraw.*;

public class CraftingPolymerizer extends TemplateRecipeHandler {

    private static ResourceLocation tank = new ResourceLocation(Magneticraft.NAME.toLowerCase() + ":textures/gui/tank.png");
    private static ResourceLocation heat = new ResourceLocation(Magneticraft.NAME.toLowerCase() + ":textures/gui/heatbar.png");

    public class CachedPolymerizerRecipe extends CachedRecipe
    {
        FluidStack fluid;
        double minTemperature;
        PositionedStack input;
        PositionedStack output;

        public CachedPolymerizerRecipe(RecipePolymerizer recipe)
        {
            Object in = recipe.getInput();
            input = new PositionedStack(in,63, 25);
            output = new PositionedStack(recipe.getOutput(),119, 25 );
            fluid = recipe.getFluid();
            minTemperature = recipe.getTemperature();
        }
        @Override
        public List<PositionedStack> getIngredients()
        {
            return getCycledIngredients(cycleticks/20, Arrays.asList(input));
        }
        @Override
        public PositionedStack getResult()
        {
            return output;
        }

    }

    @Override
    public String getRecipeName() {
        return "Polymerizer";
    }

    @Override
    public String getGuiTexture() {
        return "magneticraft:textures/gui/nei/polimerizer.png";
    }


    @Override
    public void loadTransferRects() {

        transferRects.add(new RecipeTransferRect(new Rectangle(88, 26, 24, 15), getOverlayIdentifier()));
    }

    @Override
    public String getOverlayIdentifier()
    {
        return "mg_polymerizer";
    }


    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if(outputId == getOverlayIdentifier())
            for(RecipePolymerizer r : MgRecipeRegister.polymerizer)
                if(r!=null)
                    this.arecipes.add(new CachedPolymerizerRecipe(r));
        super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        if(result!=null)
            for(RecipePolymerizer r : MgRecipeRegister.polymerizer)
                if(r!=null && (stackMatchesObject(result, r.getOutput())))
                    this.arecipes.add(new CachedPolymerizerRecipe(r));
    }


    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        FluidStack fs = null;
        if(inputId == "liquid" && ingredients!=null && ingredients.length>0 && ingredients[0] instanceof FluidStack)
            fs = (FluidStack)ingredients[0];
        if(inputId == "item" && ingredients!=null && ingredients.length>0 && ingredients[0] instanceof ItemStack && FluidContainerRegistry.isFilledContainer((ItemStack) ingredients[0]))
            fs = FluidContainerRegistry.getFluidForFilledItem((ItemStack)ingredients[0]);

        if(fs!=null)
            for(RecipePolymerizer r : MgRecipeRegister.polymerizer)
                if(r!=null && r.getFluid().isFluidEqual(fs))
                    this.arecipes.add(new CachedPolymerizerRecipe(r));
        super.loadUsageRecipes(inputId, ingredients);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        if(ingredient!=null)
        {
            for(RecipePolymerizer r : MgRecipeRegister.polymerizer)
                if(r!=null && stackMatchesObject(ingredient, r.getInput()))
                    this.arecipes.add(new CachedPolymerizerRecipe(r));
        }
    }


    @Override
    public int recipiesPerPage()
    {
        return 2;
    }
    /*
    @Override
    public void drawExtras(int recipe) {
        RecipePolymerizer rec = recipes.get(recipe);
        RenderUtil.drawString(rec.getFluid().getLocalizedName(), 45, 60, RenderUtil.fromRGB(255, 255, 255), true);
        RenderUtil.bindTexture(TextureMap.locationBlocksTexture);
        drawTexturedModelRectFromIcon(36, 14, rec.getFluid().getFluid().getIcon(), 18, 39);
        RenderUtil.bindTexture(tank);
        RenderUtil.drawTexturedModalRectScaled(35, 13, 0, 0, 18, 39, 20, 41);
        RenderUtil.bindTexture(heat);
        int scale = Math.min(44, (int) (rec.getTemperature() * 44f / 1400f));
        RenderUtil.drawTexturedModalRectScaled(15, 9 + (44 - scale), 0, 44 - scale, 6, scale, 12, 45);
    }
    */
    public void drawTexturedModelRectFromIcon(int p_94065_1_, int p_94065_2_, IIcon p_94065_3_, int p_94065_4_, int p_94065_5_) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (p_94065_1_), (double) (p_94065_2_ + p_94065_5_), (double) 0, (double) p_94065_3_.getMinU(), (double) p_94065_3_.getMaxV());
        tessellator.addVertexWithUV((double) (p_94065_1_ + p_94065_4_), (double) (p_94065_2_ + p_94065_5_), (double) 0, (double) p_94065_3_.getMaxU(), (double) p_94065_3_.getMaxV());
        tessellator.addVertexWithUV((double) (p_94065_1_ + p_94065_4_), (double) (p_94065_2_), (double) 0, (double) p_94065_3_.getMaxU(), (double) p_94065_3_.getMinV());
        tessellator.addVertexWithUV((double) (p_94065_1_), (double) (p_94065_2_), (double) 0, (double) p_94065_3_.getMinU(), (double) p_94065_3_.getMinV());
        tessellator.draw();
    }


    @Override
    public void drawBackground(int recipe)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        CachedPolymerizerRecipe r = (CachedPolymerizerRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            //Draw the input and output slots
            drawSlot(r.input.relx,r.input.rely, 16,16);
            drawSlot(r.output.relx,r.output.rely, 20,20);

            //Change to using the gui texture and draw the background heat gauge
            changeTexture(getGuiTexture());
            GL11.glColor4f(1f, 1f, 1f, 1);
            drawTexturedModalRect(20,10,20,20,6,45);
            drawTexturedModalRect(30,4,40,14,20,7);

            //Draw the fluid on the gui
            int timer = 30;
            int step = cycleticks%(timer*7)/timer;
            int fluidHeight = 39-(step*7)-(step>0?1:0);
            RenderUtil.bindTexture(TextureMap.locationBlocksTexture);
            drawTexturedModelRectFromIcon(31, 54-fluidHeight, r.fluid.getFluid().getIcon(), 18, fluidHeight);
            //ClientUtils.drawRepeatedFluidIcon(r.fluid.getFluid(), 31,54-fluidHeight, 18,fluidHeight);

            //draw the fluid tank on top of the fluid
            changeTexture(tank);
            RenderUtil.drawTexturedModalRectScaled(30, 14, 0, 0, 20, 41, 20, 41);


            changeTexture(heat);
            int scale = Math.min(44, (int) (r.minTemperature * 44f / 1400f));
            RenderUtil.drawTexturedModalRectScaled(20, 10 + (44 - scale), 0, 44 - scale, 6, scale, 12, 45);


            GL11.glColor4f(1, 1, 1, 1);
            changeTexture("textures/gui/container/furnace.png");
            drawTexturedModalRect(88,26, 82,35, 20,16);


            drawTexturedModalRect(88,26, 179,14, (int)((cycleticks%timer)/(float)timer*20),16);
            GL11.glTranslatef(89, 50, 100);
            GL11.glRotatef(-45, 1, 0, 0);
            GL11.glRotatef(180, 0, 1, 0);
            GL11.glScalef(12, -12, 12);

        }
        GL11.glPopMatrix();
    }

    @Override
    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe)
    {
        Point mouse = getMousePosition();
        Point offset = gui.getRecipePosition(recipe);
        Point relMouse = new Point(mouse.x -(gui.width- 176)/2-offset.x, mouse.y-(gui.height-166)/2-offset.y);

        CachedPolymerizerRecipe r = (CachedPolymerizerRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            if(new Rectangle(31,14, 20,41).contains(relMouse))
                if(keyCode== NEIClientConfig.getKeyBinding("gui.recipe"))
                {
                    if(GuiCraftingRecipe.openRecipeGui("liquid", new Object[] { r.fluid }))
                        return true;
                }
                else if(keyCode==NEIClientConfig.getKeyBinding("gui.usage"))
                {
                    if(GuiUsageRecipe.openRecipeGui("liquid", new Object[] { r.fluid }))
                        return true;
                }
        }
        return super.keyTyped(gui, keyChar, keyCode, recipe);
    }
    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe)
    {
        Point mouse = getMousePosition();
        Point offset = gui.getRecipePosition(recipe);
        Point relMouse = new Point(mouse.x -(gui.width- 176)/2-offset.x, mouse.y-(gui.height-166)/2-offset.y);

        CachedPolymerizerRecipe r = (CachedPolymerizerRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            if(new Rectangle(31,14, 21,41).contains(relMouse))
                if(button==0)
                {
                    if(GuiCraftingRecipe.openRecipeGui("liquid", new Object[] { r.fluid }))
                        return true;
                }
                else if(button==1)
                {
                    if(GuiUsageRecipe.openRecipeGui("liquid", new Object[] { r.fluid }))
                        return true;
                }
        }
        return super.mouseClicked(gui, button, recipe);
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
    {
        Point mouse = getMousePosition();
        Point offset = gui.getRecipePosition(recipe);
        Point relMouse = new Point(mouse.x -(gui.width- 176)/2-offset.x, mouse.y-(gui.height-166)/2-offset.y);
        CachedPolymerizerRecipe r = (CachedPolymerizerRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null && r.fluid!=null)
        {
            if(new Rectangle(31,14, 20,41).contains(relMouse))
            {
                currenttip.add(r.fluid.getLocalizedName());
                currenttip.add(EnumChatFormatting.GRAY.toString()+r.fluid.amount+" mB");
            }

            if(new Rectangle(20,10, 6,45).contains(relMouse))
            {
                currenttip.add(String.valueOf(r.minTemperature)+"C");
            }
        }
        return currenttip;
    }


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



}