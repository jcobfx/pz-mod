package pl.pzmod.client.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class TeslaHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {
    public TeslaHelmetModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshDefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f);
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition head = partDefinition.getChild("head");
        head.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -4.0F, 12.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 11).addBox(5.0F, -5.0F, -3.0F, 1.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(20, 11).addBox(-6.0F, -5.0F, -3.0F, 1.0F, 5.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-5.0F, -5.0F, 5.0F, 10.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 25).addBox(-6.0F, -4.0F, -4.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 31).addBox(-7.0F, -4.0F, -4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 27).addBox(-4.0F, -3.0F, -4.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 37).addBox(-4.0F, -11.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 37).addBox(3.0F, -11.0F, 0.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 35).addBox(-4.0F, -12.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 37).addBox(2.0F, -12.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 37).addBox(-5.0F, -12.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 37).addBox(-4.0F, -13.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 39).addBox(-7.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 39).addBox(-7.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 39).addBox(-7.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 39).addBox(-7.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 39).addBox(-7.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 11).addBox(6.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 41).addBox(6.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 41).addBox(6.0F, -3.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 41).addBox(6.0F, -3.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 41).addBox(6.0F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 25).addBox(6.0F, -6.0F, -4.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 27).addBox(6.0F, -5.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 27).addBox(-7.0F, -5.0F, 0.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 31).addBox(-7.0F, -6.0F, -4.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(12, 31).addBox(-7.0F, -4.0F, 1.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 31).addBox(6.0F, -4.0F, 1.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(40, 13).addBox(4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 15).addBox(3.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 17).addBox(0.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 39).addBox(-1.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 19).addBox(-4.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 21).addBox(-5.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(40, 23).addBox(-5.0F, -3.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 39).addBox(3.0F, -13.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(3.0F, -12.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        return LayerDefinition.create(meshDefinition, 64, 64);
    }



}
