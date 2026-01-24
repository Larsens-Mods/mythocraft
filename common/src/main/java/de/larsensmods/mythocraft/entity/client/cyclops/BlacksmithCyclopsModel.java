package de.larsensmods.mythocraft.entity.client.cyclops;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.BlacksmithCyclopsEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BlacksmithCyclopsModel<T extends BlacksmithCyclopsEntity> extends HierarchicalModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "blacksmith_cyclops"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public BlacksmithCyclopsModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.right_arm = this.body.getChild("right_arm");
        this.left_arm = this.body.getChild("left_arm");
        this.right_leg = this.body.getChild("right_leg");
        this.left_leg = this.body.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 21).addBox(-9.0F, -2.0F, -6.0F, 18.0F, 17.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(47, 45).addBox(-9.0F, 11.0F, -6.0F, 18.0F, 8.0F, 11.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.0F, -2.5F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -4.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(76, 0).addBox(-4.0F, -3.0F, -3.0F, 4.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 1.0F, -1.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(96, 0).addBox(0.0F, -3.0F, -3.0F, 4.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 1.0F, -1.0F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-3.5F, 0.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 15.0F, 0.0F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(54, 0).addBox(-2.5F, 0.0F, -3.0F, 6.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 15.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(@NotNull BlacksmithCyclopsEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(BlacksmithCyclopsAnimations.WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, BlacksmithCyclopsAnimations.IDLE, ageInTicks, 1f);
        this.animate(entity.attackAnimationState, BlacksmithCyclopsAnimations.ATTACK, ageInTicks, 1f);
        this.animate(entity.smithingAnimationState, BlacksmithCyclopsAnimations.SMITHING, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -20.0f, 20.0f);
        headPitch = Mth.clamp(headPitch, -15.0f, 15.0f);

        this.head.yRot = headYaw * ((float) Math.PI / 180f);
        this.head.xRot = headPitch * ((float) Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.body;
    }

}
