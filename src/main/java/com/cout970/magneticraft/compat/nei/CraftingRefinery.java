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
import com.cout970.magneticraft.api.access.RecipeRefinery;
import com.cout970.magneticraft.util.RenderUtil;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.*;

public class CraftingRefinery extends TemplateRecipeHandler {

    private static ResourceLocation tank = new ResourceLocation(Magneticraft.NAME.toLowerCase() + ":textures/gui/tank.png");
    int inputTankX = 28;
    int inputTankY = 3;
    int fluidWidth = 18;
    int tankInsideHeight = 39;
    int tankHeight = 41;
    int tankWidth = 20;
    int arrowWidth = 20;
    int arrowHeight = 16;
    int componentSpacing=5;
    int arrowX = inputTankX+componentSpacing+tankWidth;
    int arrowY = inputTankY+(int)((tankHeight*0.5)-(arrowHeight*0.5));
    int outputTank1X = arrowX+arrowWidth+componentSpacing;
    int outputTank2X = outputTank1X + componentSpacing + tankWidth;
    int outputTank3X = outputTank2X + componentSpacing + tankWidth;
    public class CachedRefineryRecipe extends CachedRecipe
    {
        FluidStack fluidIn;
        double minTemperature;
        FluidStack fluidOut0;
        FluidStack fluidOut1;
        FluidStack fluidOut2;


        public CachedRefineryRecipe(RecipeRefinery recipe)
        {

            fluidIn = recipe.getInput();
            fluidOut0 = recipe.getOut0();
            fluidOut1 = recipe.getOut1();
            fluidOut2 = recipe.getOut2();
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
        return "Refinery";
    }

    @Override
    public String getGuiTexture() {
        return "magneticraft:textures/gui/nei/refinery.png";
    }


    @Override
    public void loadTransferRects() {

        transferRects.add(new RecipeTransferRect(new Rectangle(53, 14, 24, 15), getOverlayIdentifier()));
    }

    @Override
    public String getOverlayIdentifier()
    {
        return "mg_refinery";
    }


    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if(outputId==getOverlayIdentifier())
            for(RecipeRefinery r : MgRecipeRegister.refinery)
                if(r!=null)
                    this.arecipes.add(new CachedRefineryRecipe(r));

        FluidStack fs = null;
        if(outputId == "liquid" && results!=null && results.length>0 && results[0] instanceof FluidStack)
            fs = (FluidStack)results[0];
        if(outputId == "item" && results!=null && results.length>0 && results[0] instanceof ItemStack && FluidContainerRegistry.isFilledContainer((ItemStack) results[0]))
            fs = FluidContainerRegistry.getFluidForFilledItem((ItemStack)results[0]);

        if(fs!=null)
            for(RecipeRefinery r : MgRecipeRegister.refinery)
                if(r!=null && (r.getOut0().isFluidEqual(fs) || r.getOut1().isFluidEqual(fs) || r.getOut2().isFluidEqual(fs)))
                    this.arecipes.add(new CachedRefineryRecipe(r));
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
            for(RecipeRefinery r : MgRecipeRegister.refinery)
                if(r!=null && (r.getInput().isFluidEqual(fs)))
                    this.arecipes.add(new CachedRefineryRecipe(r));
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
        CachedRefineryRecipe r = (CachedRefineryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            int fluidOffset =inputTankY+tankHeight;


            //Draw the fluid on the gui
            int timer = 30;
            int step = cycleticks%(timer*6)/timer;
            int fluidHeight = tankInsideHeight-(step*6)-(step>0?1:0);
            RenderUtil.bindTexture(TextureMap.locationBlocksTexture);
            NEIutil.drawTexturedModelRectFromIcon(inputTankX+1, fluidOffset-fluidHeight, r.fluidIn.getFluid().getIcon(), fluidWidth, fluidHeight);
            //ClientUtils.drawRepeatedFluidIcon(r.fluid.getFluid(), 31,54-fluidHeight, 18,fluidHeight);

            int fluidHeight2 = tankInsideHeight-fluidHeight;
            int outFluidY = fluidOffset-fluidHeight2;
            NEIutil.drawTexturedModelRectFromIcon(outputTank1X+1, outFluidY, r.fluidOut0.getFluid().getIcon(), fluidWidth, fluidHeight2);
            NEIutil.drawTexturedModelRectFromIcon(outputTank2X+1, outFluidY, r.fluidOut1.getFluid().getIcon(), fluidWidth, fluidHeight2);
            NEIutil.drawTexturedModelRectFromIcon(outputTank3X+1, outFluidY, r.fluidOut2.getFluid().getIcon(), fluidWidth, fluidHeight2);

            //draw the fluid tank on top of the fluid
            changeTexture(tank);
            RenderUtil.drawTexturedModalRectScaled(inputTankX, inputTankY, 0, 0, tankWidth, tankHeight, tankWidth, tankHeight);
            RenderUtil.drawTexturedModalRectScaled(outputTank1X, inputTankY, 0, 0, tankWidth, tankHeight, tankWidth, tankHeight);
            RenderUtil.drawTexturedModalRectScaled(outputTank2X, inputTankY, 0, 0, tankWidth, tankHeight, tankWidth, tankHeight);
            RenderUtil.drawTexturedModalRectScaled(outputTank3X, inputTankY, 0, 0, tankWidth, tankHeight, tankWidth, tankHeight);



            GL11.glColor4f(1, 1, 1, 1);
            changeTexture("textures/gui/container/furnace.png");
            drawTexturedModalRect(arrowX,arrowY, 82,35, arrowWidth,arrowHeight);


            drawTexturedModalRect(arrowX,arrowY, 179,14, (int)((cycleticks%timer)/(float)timer*arrowWidth),arrowHeight);
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

        CachedRefineryRecipe r = (CachedRefineryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            if(new Rectangle(inputTankX,inputTankY, tankWidth,tankHeight).contains(relMouse))
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
            } else if(new Rectangle(outputTank1X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe"))
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut0}))
                        return true;
                }
                else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage"))
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut0}))
                        return true;
                }
            }else if(new Rectangle(outputTank2X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe"))
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut1}))
                        return true;
                }
                else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage"))
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut1}))
                        return true;
                }
            }else if(new Rectangle(outputTank3X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe"))
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut2}))
                        return true;
                }
                else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage"))
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut2}))
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

        CachedRefineryRecipe r = (CachedRefineryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null)
        {
            if(new Rectangle(inputTankX,inputTankY, tankWidth,tankHeight).contains(relMouse))
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
            }else if(new Rectangle(outputTank1X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                if (button == 0)
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut0}))
                        return true;
                }
                else if (button == 1)
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut0}))
                        return true;
                }
            }else if(new Rectangle(outputTank2X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                if (button == 0)
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut1}))
                        return true;
                }
                else if (button == 1)
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut1}))
                        return true;
                }
            }else if(new Rectangle(outputTank3X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                if (button == 0)
                {
                    if (GuiCraftingRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut2}))
                        return true;
                }
                else if (button == 1)
                {
                    if (GuiUsageRecipe.openRecipeGui("liquid", new Object[]{r.fluidOut2}))
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
        CachedRefineryRecipe r = (CachedRefineryRecipe) this.arecipes.get(recipe%arecipes.size());
        if(r!=null && r.fluidIn!=null)
        {
            if(new Rectangle(inputTankX,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                currenttip.add(r.fluidIn.getLocalizedName());
                currenttip.add(EnumChatFormatting.GRAY.toString()+r.fluidIn.amount+" mB");
            }else if(new Rectangle(outputTank1X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                currenttip.add(r.fluidOut0.getLocalizedName());
                currenttip.add(EnumChatFormatting.GRAY.toString()+r.fluidOut0.amount+" mB");
            }else if(new Rectangle(outputTank2X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                currenttip.add(r.fluidOut1.getLocalizedName());
                currenttip.add(EnumChatFormatting.GRAY.toString()+r.fluidOut1.amount+" mB");
            }else if(new Rectangle(outputTank3X,inputTankY, tankWidth,tankHeight).contains(relMouse))
            {
                currenttip.add(r.fluidOut2.getLocalizedName());
                currenttip.add(EnumChatFormatting.GRAY.toString()+r.fluidOut2.amount+" mB");
            }

        }
        return currenttip;
    }





}