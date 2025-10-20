package de.larsensmods.mythocraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SatyrModel<T extends SatyrEntity> extends HierarchicalModel<T> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "satyr"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public SatyrModel(ModelPart root) {
        this.body = root.getChild("Body");
        this.head = this.body.getChild("Head");
        this.rightArm = this.body.getChild("RightArm");
        this.leftArm = this.body.getChild("LeftArm");
        this.rightLeg = this.body.getChild("RightLeg");
        this.leftLeg = this.body.getChild("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -28.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(24, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -28.0F, 0.0F));

        PartDefinition HornTopRight_r1 = head.addOrReplaceChild("HornTopRight_r1", CubeListBuilder.create().texOffs(12, 21).mirror().addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.574F, -9.3858F, -3.0F, 0.0F, -0.48F, -0.3927F));

        PartDefinition HornTopLeft_r1 = head.addOrReplaceChild("HornTopLeft_r1", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.574F, -9.3858F, -3.0F, 0.0F, 0.48F, 0.3927F));

        PartDefinition HornBottomLeft_r1 = head.addOrReplaceChild("HornBottomLeft_r1", CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.4849F, -7.192F, -2.5F, 0.4597F, 0.4176F, 0.8845F));

        PartDefinition HornBottomRight_r1 = head.addOrReplaceChild("HornBottomRight_r1", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.5F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.4849F, -7.192F, -2.5F, 0.4597F, -0.4176F, -0.8845F));

        PartDefinition RightArm = body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(20, 16).addBox(-6.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -27.0F, 0.0F));

        PartDefinition LeftArm = body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(36, 16).addBox(2.0F, -1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -27.0F, 0.0F));

        PartDefinition RightLeg = body.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(20, 32).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 49).addBox(-1.0F, 14.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -17.0F, 0.0F));

        PartDefinition SlantFrontRight_r1 = RightLeg.addOrReplaceChild("SlantFrontRight_r1", CubeListBuilder.create().texOffs(28, 41).addBox(-5.0F, -5.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 14.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition SlantBackRight_r1 = RightLeg.addOrReplaceChild("SlantBackRight_r1", CubeListBuilder.create().texOffs(20, 41).addBox(-5.0F, -5.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 10.0F, 3.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition LeftLeg = body.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(36, 32).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 49).addBox(-1.0F, 14.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -17.0F, 0.0F));

        PartDefinition SlantFrontLeft_r1 = LeftLeg.addOrReplaceChild("SlantFrontLeft_r1", CubeListBuilder.create().texOffs(36, 41).addBox(-1.0F, -5.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.0F, 0.0F, -0.6109F, 0.0F, 0.0F));

        PartDefinition SlantBackLeft_r1 = LeftLeg.addOrReplaceChild("SlantBackLeft_r1", CubeListBuilder.create().texOffs(44, 41).addBox(-1.0F, -5.5F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.0F, 3.0F, 0.5672F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull SatyrEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(SatyrAnimations.ANIM_SATYR_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, SatyrAnimations.ANIM_SATYR_IDLE, ageInTicks, 1f);
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
