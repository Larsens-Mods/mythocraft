package de.larsensmods.mythocraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.monster.MinotaurEntity;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class MinotaurModel<T extends MinotaurEntity> extends HierarchicalModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "minotaur"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart leftArm;
    private final ModelPart rightArm;

    public MinotaurModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.leftLeg = this.body.getChild("left_leg");
        this.rightLeg = this.body.getChild("right_leg");
        this.leftArm = this.body.getChild("left_arm");
        this.rightArm = this.body.getChild("right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-6.0F, -17.0F, -3.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.5F, -23.0F, -3.5F, 13.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 25).addBox(-6.0F, -13.0F, -3.0F, 12.0F, 4.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(52, 32).addBox(-4.0F, -7.5698F, -2.3963F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(59, 47).addBox(-2.0F, -3.5698F, -6.3963F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(75, 32).addBox(3.0F, -8.5698F, -2.4963F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(43, 32).mirror().addBox(-7.0F, -8.5698F, -2.4963F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(76, 25).addBox(5.0F, -8.5698F, -7.4963F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(44, 25).mirror().addBox(-7.0F, -8.5698F, -7.4963F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -22.4302F, -2.6037F));

        PartDefinition bullring_r1 = head.addOrReplaceChild("bullring_r1", CubeListBuilder.create().texOffs(64, 55).addBox(-1.5F, -1.2786F, 0.0056F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.7912F, -6.6519F, -0.3491F, 0.0F, 0.0F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(20, 35).mirror().addBox(-2.5F, -0.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.5F, -10.5F, 0.0F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 35).addBox(-2.5F, -0.5F, -2.5F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -10.5F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(62, 0).addBox(-0.5F, -0.75F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(64, 11).addBox(0.0F, 4.25F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -22.25F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 0).mirror().addBox(-4.5F, -0.75F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(42, 11).mirror().addBox(-4.0F, 4.25F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, -22.25F, 0.0F));

        return LayerDefinition.create(meshdefinition, 96, 64);
    }

    @Override
    public void setupAnim(@NotNull MinotaurEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(MinotaurAnimations.WALK, limbSwing, limbSwingAmount, 1f, 1f); //TODO: CHECK ANIM SPEEDS
        this.animate(entity.idleAnimationState, MinotaurAnimations.IDLE, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -25.0f, 25.0f);
        headPitch = Mth.clamp(headPitch, -40.0f, 12.5f); //TODO: CHECK CLAMPING VALUES

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