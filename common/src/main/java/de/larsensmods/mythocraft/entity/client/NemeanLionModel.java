package de.larsensmods.mythocraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class NemeanLionModel<T extends NemeanLionEntity> extends HierarchicalModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "nemean_lion"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart snoutBottom;
    private final ModelPart legLeftFront;
    private final ModelPart legRightFront;
    private final ModelPart legLeftBack;
    private final ModelPart legRightBack;
    private final ModelPart tail;

    public NemeanLionModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.snoutBottom = this.head.getChild("snoutBottom");
        this.legLeftFront = this.body.getChild("legLeftFront");
        this.legRightFront = this.body.getChild("legRightFront");
        this.legLeftBack = this.body.getChild("legLeftBack");
        this.legRightBack = this.body.getChild("legRightBack");
        this.tail = this.body.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -14.0F, -11.0F, 12.0F, 9.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(0, 31).addBox(-7.0F, -14.5F, 3.0F, 14.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-7.0F, -14.5F, -12.0F, 14.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(84, 20).addBox(-4.0F, -6.0F, -9.0F, 8.0F, 7.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(106, 20).addBox(-2.5F, -3.0F, -12.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(79, 33).addBox(-3.0F, -9.0F, -5.5F, 6.0F, 14.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(74, 52).addBox(-5.0F, -1.0F, -5.0F, 10.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(74, 62).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, -11.0F));

        PartDefinition earRight_r1 = head.addOrReplaceChild("earRight_r1", CubeListBuilder.create().texOffs(98, 15).mirror().addBox(-2.0F, -4.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -4.75F, -7.25F, -0.219F, 0.3286F, -0.6037F));

        PartDefinition earLeft_r1 = head.addOrReplaceChild("earLeft_r1", CubeListBuilder.create().texOffs(90, 15).addBox(-1.0F, -4.0F, 0.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -4.75F, -7.25F, -0.219F, -0.3286F, 0.6037F));

        PartDefinition headManeSides_r1 = head.addOrReplaceChild("headManeSides_r1", CubeListBuilder.create().texOffs(67, 72).addBox(-7.0F, -4.0F, -1.0F, 15.0F, 4.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -0.5F, -4.25F, -0.3054F, 0.0F, 0.0F));

        PartDefinition snoutBottom = head.addOrReplaceChild("snoutBottom", CubeListBuilder.create().texOffs(106, 14).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -9.0F));

        PartDefinition legLeftFront = body.addOrReplaceChild("legLeftFront", CubeListBuilder.create().texOffs(48, 65).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(50, 73).addBox(-2.0F, 3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(50, 79).addBox(-2.0F, 6.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.0F, -9.0F));

        PartDefinition legRightFront = body.addOrReplaceChild("legRightFront", CubeListBuilder.create().texOffs(32, 65).mirror().addBox(-2.0F, -1.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 73).mirror().addBox(-1.0F, 3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 79).mirror().addBox(-1.0F, 6.0F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -4.0F, -9.0F));

        PartDefinition legLeftBack = body.addOrReplaceChild("legLeftBack", CubeListBuilder.create().texOffs(16, 65).addBox(-2.0F, -1.0F, -2.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 73).addBox(-2.0F, 3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(18, 79).addBox(-2.0F, 6.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.0F, 7.5F));

        PartDefinition legRightBack = body.addOrReplaceChild("legRightBack", CubeListBuilder.create().texOffs(0, 65).mirror().addBox(-2.0F, -1.0F, -2.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(2, 73).mirror().addBox(-1.0F, 3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(2, 79).mirror().addBox(-1.0F, 6.0F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, -4.0F, 7.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -11.5093F, 11.0926F));

        PartDefinition tailTip_r1 = tail.addOrReplaceChild("tailTip_r1", CubeListBuilder.create().texOffs(3, 15).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5093F, 12.4074F, 0.6109F, 0.0F, 0.0F));

        PartDefinition tailStage3_r1 = tail.addOrReplaceChild("tailStage3_r1", CubeListBuilder.create().texOffs(10, 11).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, 8.7593F, 8.9074F, 0.2182F, 0.0F, 0.0F));

        PartDefinition tailStage2_r1 = tail.addOrReplaceChild("tailStage2_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.2593F, 4.1574F, -0.1745F, 0.0F, 0.0F));

        PartDefinition tailStage1_r1 = tail.addOrReplaceChild("tailStage1_r1", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(0.0F, 6.0093F, 2.1574F, 0.9163F, 0.0F, 0.0F));

        PartDefinition tailLink_r1 = tail.addOrReplaceChild("tailLink_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 1.5093F, -0.0926F, 0.2182F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@NotNull NemeanLionEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(NemeanLionAnimations.WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, NemeanLionAnimations.IDLE, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30.0f, 30.0f);
        headPitch = Mth.clamp(headPitch, -35.0f, 35.0f);

        this.head.yRot = headYaw * ((float) Math.PI / 180f);
        this.head.xRot = headPitch * ((float) Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public @NotNull ModelPart root() {
        return body;
    }
}