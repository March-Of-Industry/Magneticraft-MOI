// Date: 08/11/2014 17:12:24
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX


package com.cout970.magneticraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFluidHopper extends ModelBase {
    //fields
    ModelRenderer Base;
    ModelRenderer Base_2;
    ModelRenderer outDown;
    ModelRenderer outFront;
    ModelRenderer outBack;
    ModelRenderer outLeft;
    ModelRenderer outRight;

    public ModelFluidHopper() {
        textureWidth = 64;
        textureHeight = 48;

        Base = new ModelRenderer(this, 0, 0);
        Base.addBox(-8F, -8F, -8F, 16, 6, 16);
        Base.setRotationPoint(0F, 16F, 0F);
        Base.setTextureSize(64, 48);
        Base.mirror = true;
        setRotation(Base, 0F, 0F, 0F);
        Base_2 = new ModelRenderer(this, 0, 22);
        Base_2.addBox(-4F, -2F, -4F, 8, 6, 8);
        Base_2.setRotationPoint(0F, 16F, 0F);
        Base_2.setTextureSize(64, 48);
        Base_2.mirror = true;
        setRotation(Base_2, 0F, 0F, 0F);
        outDown = new ModelRenderer(this, 32, 22);
        outDown.addBox(-2F, 4F, -2F, 4, 4, 4);
        outDown.setRotationPoint(0F, 16F, 0F);
        outDown.setTextureSize(64, 48);
        outDown.mirror = true;
        setRotation(outDown, 0F, 0F, 0F);
        outFront = new ModelRenderer(this, 48, 22);
        outFront.addBox(-2F, 0F, -8F, 4, 4, 4);
        outFront.setRotationPoint(0F, 16F, 0F);
        outFront.setTextureSize(64, 48);
        outFront.mirror = true;
        setRotation(outFront, 0F, 0F, 0F);
        outBack = new ModelRenderer(this, 32, 30);
        outBack.addBox(-2F, 0F, 4F, 4, 4, 4);
        outBack.setRotationPoint(0F, 16F, 0F);
        outBack.setTextureSize(64, 48);
        outBack.mirror = true;
        setRotation(outBack, 0F, 0F, 0F);
        outLeft = new ModelRenderer(this, 48, 30);
        outLeft.addBox(4F, 0F, -2F, 4, 4, 4);
        outLeft.setRotationPoint(0F, 16F, 0F);
        outLeft.setTextureSize(64, 48);
        outLeft.mirror = true;
        setRotation(outLeft, 0F, 0F, 0F);
        outRight = new ModelRenderer(this, 0, 36);
        outRight.addBox(-8F, 0F, -2F, 4, 4, 4);
        outRight.setRotationPoint(0F, 16F, 0F);
        outRight.setTextureSize(64, 48);
        outRight.mirror = true;
        setRotation(outRight, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Base.render(f5);
        Base_2.render(f5);
        outDown.render(f5);
        outFront.render(f5);
        outBack.render(f5);
        outLeft.render(f5);
        outRight.render(f5);
    }

    public void renderStatic(float f5, int i) {
        Base.render(f5);
        Base_2.render(f5);
        if (i == 0) outDown.render(f5);
        if (i == 2) outFront.render(f5);
        if (i == 3) outBack.render(f5);
        if (i == 4) outLeft.render(f5);
        if (i == 5) outRight.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

}
