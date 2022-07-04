package com.cout970.magneticraft.compat.nei;


import codechicken.nei.NEIClientConfig;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.cout970.magneticraft.Magneticraft;
import com.cout970.magneticraft.api.access.MgRecipeRegister;
import com.cout970.magneticraft.api.access.RecipeOilDistillery;
import com.cout970.magneticraft.api.access.RecipePolymerizer;
import com.cout970.magneticraft.util.RenderUtil;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.*;

public class CraftingDistillery extends TemplateRecipeHandler {

    private static ResourceLocation tank = new ResourceLocation(Magneticraft.NAME.toLowerCase() + ":textures/gui/tank.png");
    private static ResourceLocation heat = new ResourceLocation(Magneticraft.NAME.toLowerCase() + ":textures/gui/heatbar.png");

    public class CachedDistilleryRecipe extends CachedRecipe
    {
        FluidStack fluidIn;
        double minTemperature;
        FluidStack fluidOut;

        public CachedDistilleryRecipe(RecipeOilDistillery recipe)
        {

            fluidIn = recipe.getInput();
            fluidOut = recipe.getOutput();
        }

        @Override
        public PositionedStack getIngredient()
        {
            return null;
        }

        @Override
        public PositionedStack getResult()
        {
            return null;
        }

    }

    @Override
    public String getRecipeName() {
        return "Oil Distillery";
    }

    @Override
    public String getGuiTexture() {
        return "magneticraft:textures/gui/nei/distillery.png";
    }


    @Override
    public void loadTransferRects() {

        transferRects.add(new RecipeTransferRect(new Rectangle(73, 16, 24, 15), getOverlayIdentifier()));
    }

    @Override
    public String getOverlayIdentifier()
    {
        return "mg_distillery";
    }


    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if(outputId==getOverlayIdentifier())
            for(RecipeOilDistillery r : MgRecipeRegister.oilDistillery)
                if(r!=null)
                    this.arecipes.add(new CachedDistilleryRecipe(r));

        FluidStack fs = null;
        if(outputId == "liquid" && results!=null && results.length>0 && results[0] instanceof FluidStack)
            fs = (FluidStack)results[0];
        if(outputId == "item" && results!=null && results.length>0 && results[0] instanceof ItemStack && FluidContainerRegistry.isFilledContainer((ItemStack) results[0]))
            fs = FluidContainerRegistry.getFluidForFilledItem((ItemStack)results[0]);

        if(fs!=null)
            for(RecipeOilDistillery r : MgRecipeRegister.oilDistillery)
                if(r!=null && r.getOutput().isFluidEqual(fs))
                    this.arecipes.add(new CachedDistilleryRecipe(r));
    }


    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients)
    {
        FluidStack fs = null;
        if(inputId == "liquid" && ingredients!=null && ingredients.length>0 && ingredients[0] instanceof FluidStack)
            fs = (FluidStack)ingredients[0];
        if(inputId == "item" && ingredients!=null && ingredients.length>0 && ingredients[0] instanceof ItemStack && FluidContainerRegistry.isFilledContainer((ItemStack) ingredients[0]))
            fs = FluidContainerRegistry.getFluidForFilledItem((ItemStack)ingredients[0]);

        if(fs!=null)
            for(RecipeOilDistillery r : MgRecipeRegister.oilDistillery)
                if(r!=null && (r.getInput().isFluidEqual(fs)))
                    this.arecipes.add(new CachedDistilleryRecipe(r));
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



    @Override
    public void drawBackground(int recipe)
    {
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        CachedDistilleryRecipe r = (CachedDistilleryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {



            //Draw the fluid on the gui
            int timer = 30;
            int step = cycleticks%(timer*6)/timer;
            int fluidHeight = 39-(step*6)-(step>0?1:0);
            RenderUtil.bindTexture(TextureMap.locationBlocksTexture);
            NEIutil.drawTexturedModelRectFromIcon(48, 43-fluidHeight, r.fluidIn.getFluid().getIcon(), 18, fluidHeight);
            //ClientUtils.drawRepeatedFluidIcon(r.fluid.getFluid(), 31,54-fluidHeight, 18,fluidHeight);

            int fluidHeight2 = 39-fluidHeight;
            NEIutil.drawTexturedModelRectFromIcon(99, 43-fluidHeight2, r.fluidOut.getFluid().getIcon(), 18, fluidHeight2);

            //draw the fluid tank on top of the fluid
            changeTexture(tank);
            RenderUtil.drawTexturedModalRectScaled(47, 3, 0, 0, 20, 41, 20, 41);
            RenderUtil.drawTexturedModalRectScaled(98, 3, 0, 0, 20, 41, 20, 41);



            GL11.glColor4f(1, 1, 1, 1);
            changeTexture("textures/gui/container/furnace.png");
            drawTexturedModalRect(73,16, 82,35, 20,16);


            drawTexturedModalRect(73,16, 179,14, (int)((cycleticks%timer)/(float)timer*20),16);
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

        CachedDistilleryRecipe r = (CachedDistilleryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            if(new Rectangle(47,3, 20,41).contains(relMouse))
            {
                if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe"))
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidIn}))
                        return true;
                }
                else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage"))
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidIn}))
                        return true;
                }
            } else if(new Rectangle(98,3, 20,41).contains(relMouse))
            {
                if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe"))
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut}))
                        return true;
                }
                else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage"))
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut}))
                        return true;
                }
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

        CachedDistilleryRecipe r = (CachedDistilleryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            if(new Rectangle(47,3, 20,41).contains(relMouse))
            {
                if(button==0)
                {
                    if(GuiCraftingRecipe.openRecipeGui("liquid", new Object[] { r.fluidIn }))
                        return true;
                }
                else if(button==1)
                {
                    if(GuiUsageRecipe.openRecipeGui("liquid", new Object[] { r.fluidIn }))
                        return true;
                }
            }else if(new Rectangle(98,3, 20,41).contains(relMouse))
            {
                if (button == 0)
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut}))
                        return true;
                }
                else if (button == 1)
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut}))
                        return true;
                }
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
        CachedDistilleryRecipe r = (CachedDistilleryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null && r.fluidIn!=null)
        {
            if(new Rectangle(47,3, 20,41).contains(relMouse))
            {
                currenttip.add(r.fluidIn.getLocalizedName());
                currenttip.add(EnumChatFormatting.GRAY.toString()+r.fluidIn.amount+" mB");
            }else if(new Rectangle(98,3, 20,41).contains(relMouse))
            {
                currenttip.add(r.fluidOut.getLocalizedName());
                currenttip.add(EnumChatFormatting.GRAY.toString()+r.fluidOut.amount+" mB");
            }

        }
        return currenttip;
    }





}