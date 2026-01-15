package data.missions.BUtilTestMission;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetGoal;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import com.fs.starfarer.api.mission.MissionDefinitionPlugin;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.base.BaseBackgroundEveryFramePlugin;
import org.boxutil.base.BaseShaderData;
import org.boxutil.base.BaseTemporaryCleanupPlugin;
import org.boxutil.base.SimpleParticleControlData;
import org.boxutil.base.api.InstanceDataAPI;
import org.boxutil.base.api.RenderDataAPI;
import org.boxutil.base.api.resource.TemporaryCleanupPlugin;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxEnum;
import org.boxutil.define.BoxGeometry;
import org.boxutil.define.InstanceType;
import org.boxutil.helper.legacy.LegacyNormalMapHelper;
import org.boxutil.manager.*;
import org.boxutil.units.standard.attribute.Instance2Data;
import org.boxutil.units.standard.attribute.NodeData;
import org.boxutil.units.standard.entity.*;
import org.boxutil.units.standard.misc.ArcObject;
import org.boxutil.units.standard.misc.NumberObject;
import org.boxutil.units.standard.misc.TextFieldObject;
import org.boxutil.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MissionDefinition implements MissionDefinitionPlugin {
    public void defineMission(MissionDefinitionAPI api) {
        api.initFleet(FleetSide.PLAYER, "BOX", FleetGoal.ATTACK, false);
        api.initFleet(FleetSide.ENEMY, "BOX", FleetGoal.ATTACK, true);

        api.setFleetTagline(FleetSide.PLAYER, "GL");
        api.setFleetTagline(FleetSide.ENEMY, "HF");
        api.addBriefingItem("Just test.");

        FleetMemberAPI fm = api.addToFleet(FleetSide.PLAYER, "onslaught_xiv_Elite", FleetMemberType.SHIP, "Test guy", true);
        fm.getVariant().addTag("TEST");

        api.addToFleet(FleetSide.ENEMY, "onslaught_xiv_Elite", FleetMemberType.SHIP, "Test guy", true);

        final float _MISSION_MAP_SIZE_HALF = 6400.0f;
        api.initMap(-_MISSION_MAP_SIZE_HALF, _MISSION_MAP_SIZE_HALF, -_MISSION_MAP_SIZE_HALF, _MISSION_MAP_SIZE_HALF);
        api.addPlugin(new Plugin());
    }

    private final static class Plugin extends BaseEveryFrameCombatPlugin {
        private CombatEngineAPI engine = null;
        private CommonEntity commonEntity = null;
        private DistortionEntity distortion = null;
        private FlareEntity flareEntity = null;
        private FlareEntity flareEntity2 = null;
        private CurveEntity curveEntity = null;
        private SegmentEntity segmentEntity = null;
        private TrailEntity trailEntity = null;
        private TextFieldEntity textFieldEntity = null;
        private NumberObject locVec = new NumberObject();
        private ArcObject arc = new ArcObject();
        private SpriteEntity spriteEntity = null;
        private SimpleParticleControlData particle = null;
        private float time = 0.0f;
        private float time2 = 0.0f;
        private float time3 = 0.0f;
        private float time4 = 0.0f;
        private float time5 = 0.0f;
//        private float time6 = 0.0f;
        private float hanabiTogTimer = 0.0f;
        private boolean tog = false;
        private boolean togHanabi = false;
        private TextFieldEntity textEntityDirect = null;

        public void init(CombatEngineAPI engine) {
            this.engine = engine;
            engine.addLayeredRenderingPlugin(new RenderingPlugin());
            this.locVec.setIntegerLength(4);
            this.locVec.setDecimalLength(2);
            this.locVec.setColorWithoutAlpha(Misc.getPositiveHighlightColor());
            this.arc.setColorWithoutAlpha(Misc.getPositiveHighlightColor());
            this.arc.setRingHardness(0.99f);
            this.arc.setInner(0.95f, 0.95f);
            this.arc.setInnerHardness(0.8f);
            this.arc.setArcDirect(-1.0f);
            if (BoxConfigs.isShaderEnable()) new LogicalCombatBackgroundThreadPluginExample();
        }

        public void advance(float amount, List<InputEventAPI> events) {
            if (!this.engine.isPaused()) this.time += amount;
            ShipAPI ship = this.engine.getPlayerShip();
            if (ship == null) return;

            String id = "BoxTestMission";
            ship.getMutableStats().getShieldDamageTakenMult().modifyMult(id, 0.1f);
            ship.getMutableStats().getHullDamageTakenMult().modifyMult(id, 0.1f);
            ship.getMutableStats().getWeaponDamageTakenMult().modifyMult(id, 0.1f);
            ship.getMutableStats().getEngineDamageTakenMult().modifyMult(id, 0.1f);
            ship.getMutableStats().getMaxSpeed().modifyMult(id, 4.0f);
            ship.getMutableStats().getAcceleration().modifyMult(id, 32.0f);
            ship.getMutableStats().getDeceleration().modifyMult(id, 32.0f);
            ship.getMutableStats().getMaxTurnRate().modifyMult(id, 16.0f);
            ship.getMutableStats().getTurnAcceleration().modifyMult(id, 32.0f);
            Vector2f mousePosScreenSpace = new Vector2f(Mouse.getX() / Global.getSettings().getScreenScaleMult(), Mouse.getY() / Global.getSettings().getScreenScaleMult());
            Vector2f mousePos = new Vector2f(this.engine.getViewport().convertScreenXToWorldX(mousePosScreenSpace.x), this.engine.getViewport().convertScreenYToWorldY(mousePosScreenSpace.y));

            if (!this.engine.isPaused()) {
                if (this.time2 < 0.5f) this.time2 += amount;
                if (this.time2 >= 0.5f && Keyboard.isKeyDown(Keyboard.KEY_B)) {
                    this.time2 -= 0.5f;
                    NodeData start = new NodeData();
                    NodeData end = new NodeData();
                    start.setTangentRight(200.0f, 0.0f);
                    end.setLocation(1200.0f, 0.0f);
                    float a = (float) Math.random() * TrigUtil.PI2_F, c, s, length = (float) (Math.random() * 420.0f);
                    c = (float) Math.cos(a);
                    s = TrigUtil.sinFormCosRadiansF(c, a);
                    end.setTangentLeft(c * length, s * length);
                    CurveUtil.spawnCurveBeam(this.engine, new Vector3f(ship.getLocation().x, ship.getLocation().y, ship.getFacing()), start, end, ship, 200.0f, DamageType.ENERGY, 20.0f, true, Global.getSettings().getSprite("graphics/fx/beam_rough2_core.png"), Color.WHITE, Global.getSettings().getSprite("graphics/fx/beam_rough2_fringe.png"), Color.ORANGE, 16.0f, 512.0f, 0.1f, 1.5f, 0.9f, CombatEngineLayers.ABOVE_SHIPS_LAYER);
                }
                if (this.time3 < 1.0f) this.time3 += amount;
                if (this.time3 >= 1.0f && Keyboard.isKeyDown(Keyboard.KEY_N)) {
                    this.time3 -= 0.5f;
                    NodeData seg;
                    Matrix2f _transform = new Matrix2f();
                    TransformUtil.createSimpleRotateMatrix(ship.getFacing(), _transform);
                    SegmentEntity segmentEntity = new SegmentEntity();
                    for (BoundsAPI.SegmentAPI bound : ship.getExactBounds().getOrigSegments()) {
                        seg = new NodeData(bound.getP1());
                        seg.setWidth(16.0f);
                        seg.setColor(Color.GREEN);
                        segmentEntity.addNode(seg);
                        seg = new NodeData(bound.getP2());
                        seg.setWidth(4.0f);
                        seg.setColor(Color.GREEN);
                        segmentEntity.addNode(seg);
                    }
                    segmentEntity.setLocation(ship.getLocation());
                    segmentEntity.getModelMatrix().m00 = _transform.m00;
                    segmentEntity.getModelMatrix().m01 = _transform.m01;
                    segmentEntity.getModelMatrix().m10 = _transform.m10;
                    segmentEntity.getModelMatrix().m11 = _transform.m11;
                    segmentEntity.setNodeRefreshAllFromCurrentIndex();
                    segmentEntity.submitNodes();
                    segmentEntity.getMaterialData().setDiffuse(Global.getSettings().getSprite("graphics/fx/beam_weave_fringe.png"));
                    segmentEntity.setTexturePixels(256.0f);
                    segmentEntity.setTextureSpeed(100.0f);
                    segmentEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                    segmentEntity.setAdditiveBlend();
                    segmentEntity.setGlobalTimer(0.0f, 1.5f, 0.5f);
                    CombatRenderingManager.addEntity(segmentEntity);

                    float a, c, s;
                    a = (float) Math.toRadians(ship.getFacing());
                    c = (float) Math.cos(a);
                    s = TrigUtil.sinFormCosRadiansF(c, a);
                    Vector2f beamTo = new Vector2f(c * 1500, s * 1500);
                    Vector2f.add(ship.getLocation(), beamTo, beamTo);
                    CurveUtil.spawnDirectBeam(this.engine, ship.getLocation(), beamTo, ship, 200.0f, DamageType.ENERGY, 20.0f, true, Global.getSettings().getSprite("graphics/fx/beam_rough2_core.png"), Color.WHITE, Global.getSettings().getSprite("graphics/fx/beam_rough2_fringe.png"), Color.ORANGE, 16.0f, 512.0f, 0.1f, 1.5f, 0.9f, CombatEngineLayers.ABOVE_SHIPS_LAYER);
                }

                if (this.togHanabi) {
                    if (this.time4 >= 0.5f) {
                        this.time4 -= 0.5f;
                        short count = 256;
                        List<InstanceDataAPI> particleList = new ArrayList<>(count);
                        Instance2Data instance;
                        float a, c, s, length;
                        for (short i = 0; i < count; i++) {
                            instance = new Instance2Data();
                            a = (float) Math.random() * TrigUtil.PI2_F;
                            c = (float) Math.cos(a);
                            s = TrigUtil.sinFormCosRadiansF(c, a);
                            length = (float) Math.random() * 128.0f;
                            instance.setLocation(c * length, s * length);
                            length = (float) Math.random() * 150.0f + 200.0f;
                            instance.setVelocity(c * length, s * length);
                            instance.setFacing((float) Math.random() * 360.0f);
                            instance.setTurnRate((float) Math.random() * 45.0f - 22.5f);
                            length = (float) Math.random() + 0.6f;
                            instance.setScale(length, length);
                            instance.setScaleRate(1.0f, 1.0f);
                            instance.setTimer(0.05f, 0.2f, (float) Math.random() + 0.5f);
                            particleList.add(instance);
                        }

                        SpriteEntity particleEntity = new SpriteEntity("graphics/portraits/characters/sebestyen.png");
                        particleEntity.getMaterialData().setColor(new Color(255, 220, 200, 180));
                        particleEntity.setBaseSizePerTiles(16.0f, 16.0f);
                        particleEntity.setAdditiveBlend();
                        particleEntity.setLocation(mousePos);
                        particleEntity.setInstanceData(particleList, 0.05f, 0.2f, 1.5f);
                        particleEntity.setInstanceDataRefreshAllFromCurrentIndex();
                        particleEntity.mallocInstance(InstanceType.DYNAMIC_2D, count);
                        particleEntity.submitInstance();
                        particleEntity.setRenderingCount(count);
                        particleEntity.setAlwaysRefreshInstanceData(true);

                        particleEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                        CombatRenderingManager.addEntity(particleEntity);

                    } else this.time4 += amount;
                }

                if (this.hanabiTogTimer >= 0.5f && Keyboard.isKeyDown(Keyboard.KEY_H)) {
                    this.hanabiTogTimer -= 0.5f;
                    this.togHanabi = !this.togHanabi;
                } else if (this.hanabiTogTimer < 0.5f) this.hanabiTogTimer += amount;

                if (BoxConfigs.isShaderEnable() && BoxConfigs.isBaseGL43Supported()) {
                    if (this.particle == null) {
                        this.particle = new SimpleParticleControlData(512, 2.0f, -5120.0f, false);

                        SpriteEntity particleEntity = new SpriteEntity("graphics/fx/nebula_colorless.png");
                        particleEntity.getMaterialData().setEmissive(Global.getSettings().getSprite("graphics/fx/fx_clouds01.png"));
                        particleEntity.getMaterialData().setColor(new Color(0xFF4640FF, true));
                        particleEntity.getMaterialData().setEmissiveColor(Misc.getHighlightColor());
                        particleEntity.getMaterialData().setColorAlpha(1.0f);
                        particleEntity.getMaterialData().setEmissiveColorAlpha(0.8f);
                        particleEntity.getMaterialData().setColorToEmissive(0.2f);
                        particleEntity.getMaterialData().setAlphaToEmissive(0.0f);
                        particleEntity.getMaterialData().setGlowPower(0.5f);
                        particleEntity.setTileSize(4, 4);
                        particleEntity.setBaseSizePerTiles(16.0f, 16.0f);
                        particleEntity.setRandomTile(true);
                        particleEntity.setRandomTileEachInstance(true);
                        particleEntity.setAdditiveBlend();
                        particleEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                        particleEntity.setControlData(this.particle);
                        CombatRenderingManager.addEntity(particleEntity);
                    } else {
                        if (this.time5 > 0.05f) {
                            this.time5 -= 0.05f;
                            for (byte i = 0; i < 5; i++) {
                                Instance2Data addedParticle = this.particle.addParticle();
                                if (addedParticle != null) {
                                    addedParticle.setLocation(mousePos);
                                    addedParticle.setScaleAll(1.0f + (float) Math.random() * 2.0f);
                                    addedParticle.setScaleRateAll(2.0f);
                                    addedParticle.setLowColor(Misc.getNegativeHighlightColor());
                                    addedParticle.setAlpha((float) Math.random() * 0.4f + 0.4f);
                                    addedParticle.setVelocity((float) Math.random() * 256.0f - 128.0f, (float) Math.random() * 256.0f - 128.0f);
                                    addedParticle.setTimer(0.1f, 0.8f, 1.1f);
                                }
                            }
                            this.particle.refreshRemainingTimeToReset(2.0f);
                        }
                        this.time5 += amount;
                    }

//                    if (this.time6 > 1.0f) {
//                        this.time6 -= 1.0f;
//                        RenderingUtil.VanillaFX.spawnExplosion(false,
//                                mousePos,                                         new Vector2f(), Color.GREEN, 300.0f, 0.45f);
//                        this.engine.spawnExplosion(
//                                new Vector2f(mousePos.x + 200.0f, mousePos.y), new Vector2f(), Color.GREEN, 300.0f, 0.45f);
//
//                        RenderingUtil.VanillaFX.addNegativeNebulaParticle(false,
//                                new Vector2f(mousePos.x,             mousePos.y - 300.0f), new Vector2f(), 128.0f, 1.5f,
//                                0.2f, 0.7f, 1.0f, Color.ORANGE);
//                        this.engine.addNegativeNebulaParticle(
//                                new Vector2f(mousePos.x + 200.0f, mousePos.y - 300.0f), new Vector2f(), 128.0f, 1.5f,
//                                0.2f, 0.7f, 1.0f, Color.ORANGE);
//                    }
//                    this.time6 += amount;
                }
            }

            if (this.flareEntity != null) {
                if (!this.flareEntity.hasDelete()) {
                    ((Instance2Data) this.flareEntity.getInstanceData().get(0)).setLocation(ship.getLocation());
                    this.flareEntity.submitInstance();
                }
//                this.flareEntity.appendToEntity(ship);
                if (this.flareEntity2 != null) this.flareEntity2.setStateVanilla(new Vector2f(ship.getLocation().x, ship.getLocation().y + 100.0f), 0.0f);
//                this.flareEntity.setLocation(ship.getLocation().x + 100.0f, ship.getLocation().y + 200.0f);
            } else {
                this.flareEntity = new FlareEntity();
                this.flareEntity.setSize(640, 16);
                this.flareEntity.setFlick(true);
                this.flareEntity.setFlickWhenPaused(false);
                this.flareEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                this.flareEntity.setSmoothDisc();
                this.flareEntity.setFringeColor(Misc.getNegativeHighlightColor());
                this.flareEntity.setCoreColor(Color.WHITE);
                this.flareEntity.setCoreAlpha(1.0f);
                this.flareEntity.setFringeAlpha(1.0f);
                this.flareEntity.setNormalBlend();
                this.flareEntity.setNoisePower(0.33f);
                this.flareEntity.autoAspect();
                Instance2Data fixedA = new Instance2Data();
                fixedA.setScaleAll(1.0f);
//                fixedA.setFixedInstanceAlpha(1.0f, BoxEnum.TIMER_FULL);
                this.flareEntity.addInstanceData(fixedA);
                this.flareEntity.mallocInstance(InstanceType.FIXED_2D, 1);
                this.flareEntity.setInstanceDataRefreshSize(1);
                this.flareEntity.setRenderingCount(1);
                this.flareEntity.setGlobalTimer(0.0f, 8192.0f, 2.0f);
                CombatRenderingManager.addEntity(this.flareEntity);

                Pair<FlareEntity, Byte> p = RenderingUtil.addCombatFlareField(ship.getLocation(), 128, ship.getFacing(), 360, 512.0f, new Vector4f(96, 5, 256, 12), Misc.getPositiveHighlightColor(), Color.WHITE, 10.0f, 3.0f, CombatEngineLayers.ABOVE_PARTICLES);
                this.flareEntity2 = p.one;
                this.flareEntity2.setAdditiveBlend();
                this.flareEntity2.setSmoothDisc();
                this.flareEntity2.setFringeAlpha(1.0f);
                this.flareEntity2.setCoreAlpha(1.0f);
                this.flareEntity2.setGlowPower(1.0f);
            }
            if (this.curveEntity != null) {
                this.curveEntity.setLocation(ship.getLocation().x + 100.0f, ship.getLocation().y - 200.0f);
            } else {
                this.curveEntity = new CurveEntity().initElliptic(null, 256, 128.0f, Misc.getNegativeHighlightColor(), new Color(15, 16, 17, 18), 12.0f);
                this.curveEntity.getMaterialData().setDiffuse(Global.getSettings().getSprite("graphics/fx/beam_weave_fringe.png"));
                this.curveEntity.setInterpolation((short) 64);
                this.curveEntity.setTexturePixels(256.0f);
                this.curveEntity.setTextureSpeed(100.0f);
                this.curveEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                this.curveEntity.setAdditiveBlend();
                this.curveEntity.setGlobalTimer(1.0f, 8192.0f, 1.0f);
                this.curveEntity.setFillStartAlpha(0.0f);
                this.curveEntity.setFillEndAlpha(0.0f);
                this.curveEntity.setFillStartFactor(0.95f);
                this.curveEntity.setFillEndFactor(0.25f);
                CombatRenderingManager.addEntity(this.curveEntity);
            }
            if (this.segmentEntity != null) {
                this.segmentEntity.setStateVanilla(new Vector2f(ship.getLocation().x, ship.getLocation().y), ship.getFacing() + 90.0f);
            } else {
                List<Vector2f> points = new ArrayList<>(5);
                points.add(new Vector2f(-100.0f, -100.0f));
                points.add(new Vector2f(100.0f, -100.0f));
                points.add(new Vector2f(100.0f, 100.0f));
                points.add(new Vector2f(-100.0f, 100.0f));
                points.add(new Vector2f(-150.0f, 0.0f));
                this.segmentEntity = new SegmentEntity().initLineStrip(points, Color.ORANGE, Color.WHITE, 16.0f, true);
                this.segmentEntity.getMaterialData().setDiffuse(Global.getSettings().getSprite("graphics/fx/beam_weave_fringe.png"));
                this.segmentEntity.getMaterialData().setEmissive(Global.getSettings().getSprite("graphics/fx/beam_rough2_core.png"));
                this.segmentEntity.getMaterialData().setEmissiveColorAlpha(0.7f);
                this.segmentEntity.setTexturePixels(256.0f);
                this.segmentEntity.setTextureSpeed(100.0f);
                this.segmentEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                this.segmentEntity.setAdditiveBlend();
                this.segmentEntity.setGlobalTimer(0.0f, 8192.0f, 2.0f);
                CombatRenderingManager.addEntity(this.segmentEntity);
            }
            if (this.trailEntity != null) {
                this.trailEntity.getNodes().get(1).set(mousePosScreenSpace);
                this.trailEntity.setNodeRefreshIndex(1);
                this.trailEntity.setNodeRefreshSize(3);
                this.trailEntity.submitNodes();
            } else {
                this.trailEntity = new TrailEntity();
                this.trailEntity.getMaterialData().setDiffuse(Global.getSettings().getSprite("graphics/fx/beam_weave_fringe.png"));
                this.trailEntity.getMaterialData().setEmissive(Global.getSettings().getSprite("graphics/fx/beam_rough2_core.png"));
                this.trailEntity.getMaterialData().setEmissiveColor(Color.ORANGE);
                this.trailEntity.getMaterialData().setEmissiveColorAlpha(0.7f);
                this.trailEntity.setEndColor(Color.RED);
                this.trailEntity.setStartWidth(16.0f);
                this.trailEntity.setEndWidth(32.0f);
                this.trailEntity.setJitterPower(0.2f);
                this.trailEntity.setFlick(true);
                this.trailEntity.setFlickMixValue(0.5f);
                this.trailEntity.setTexturePixels(256.0f);
                this.trailEntity.setTextureSpeed(100.0f);
                this.trailEntity.setFillStartAlpha(0.0f);
                this.trailEntity.setFillEndAlpha(0.0f);
                this.trailEntity.setFillStartFactor(0.9f);
                this.trailEntity.setFillEndFactor(0.9f);
                this.trailEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                this.trailEntity.setAdditiveBlend();
                this.trailEntity.setCustomPrimeMatrix();
                this.trailEntity.setPrimeMatrix(TransformUtil.createWindowOrthoMatrix(null));
                this.trailEntity.addNode(new Vector2f(512.0f, 128.0f));
                this.trailEntity.addNode(mousePosScreenSpace);
                this.trailEntity.addNode(new Vector2f(512.0f, 512.0f));
                this.trailEntity.addNode(new Vector2f(1024.0f, 256.0f));
                this.trailEntity.setStripLineMode(true); // but true for default
                this.trailEntity.setNodeRefreshAllFromCurrentIndex();
                this.trailEntity.submitNodes();
                this.trailEntity.setGlobalTimer(0.0f, 8192.0f, 2.0f);
                CombatRenderingManager.addEntity(this.trailEntity);
            }
            if (this.distortion != null) {
                if (Global.getCombatEngine().getPlayerShip() != null) this.distortion.setLocation(this.engine.getPlayerShip().getLocation());
            } else {
                this.distortion = new DistortionEntity();
                this.distortion.setGlobalTimer(5.0f, 3.0f, 5.0f);
                this.distortion.setInnerFull(0.7f, 0.2f);
                this.distortion.setInnerHardness(0.8f);
                this.distortion.setSizeIn(256, 256);
                this.distortion.setPowerIn(0.0f);
                this.distortion.setPowerFull(1.0f);
                this.distortion.setPowerOut(0.0f);
                this.distortion.setSizeFull(128, 128);
                this.distortion.setSizeOut(96, 96);
                CombatRenderingManager.addEntity(this.distortion);
            }
            if (this.commonEntity != null) {
                this.commonEntity.setModelMatrix(TransformUtil.createModelMatrixRotateOnly(TransformUtil.rotationZXY(
                                        ship.getFacing(), (this.engine.getTotalElapsedTime(false) * 60.0f) % 360.0f, 0.0f
                                ), null));
                this.commonEntity.getModelMatrix().m30 = ship.getLocation().x;
                this.commonEntity.getModelMatrix().m31 = ship.getLocation().y;
            } else {
                this.commonEntity = new CommonEntity(BoxGeometry.DEMO_BOX, true);
                this.commonEntity.setBaseSize3D(200, 200, 200);
                this.commonEntity.setLayer(CombatEngineLayers.ABOVE_SHIPS_LAYER);
                this.commonEntity.setGlobalTimer(1.0f, 8192.0f, 0.0f);
                CombatRenderingManager.addEntity(this.commonEntity);
            }
            if (this.spriteEntity != null) {
                this.spriteEntity.setLocation(this.engine.getViewport().getCenter());
            } else {
                this.spriteEntity = new SpriteEntity("graphics/fx/dust_clouds_colorless.png");
                this.spriteEntity.setEmissiveSprite("graphics/fx/fx_clouds01.png");
                this.spriteEntity.setAdditiveBlend();
                this.spriteEntity.setTileSize(4, 4);
                this.spriteEntity.setStartingFormIndex(4);
                this.spriteEntity.setRandomTile(true);
                this.spriteEntity.setBaseSizePerTiles(100, 100);
                this.spriteEntity.getMaterialData().setColor(1.0f, 0.2f, 0.0f, 1.0f);
                this.spriteEntity.getMaterialData().setEmissiveColor(1.0f, 0.8f, 0.3f, 1.0f);
                this.spriteEntity.setLayer(CombatEngineLayers.JUST_BELOW_WIDGETS);
                this.spriteEntity.setGlobalTimer(1.0f, 8192.0f, 0.0f);
                CombatRenderingManager.addEntity(this.spriteEntity);
            }
            if (this.textEntityDirect == null) {
                this.textEntityDirect = new TextFieldEntity("graphics/fonts/FiraCodeModified/Fira_Code_Regular_14.fnt");
                this.textEntityDirect.setFontMap("graphics/fonts/FiraCodeModified/Fira_Code_SemiBold_14.fnt", (byte) 1);
                this.textEntityDirect.addText("Text can rendering at everywhere :)" + TextFieldEntity.LINE_FEED_SYMBOL,
                        0.0f, Misc.getButtonTextColor(),
                        false, true, false, false, 1);
                this.textEntityDirect.addText("Add to 'Rendering manager' or 'directDraw' both ok." + TextFieldEntity.LINE_FEED_SYMBOL,
                        0.0f, Misc.getButtonTextColor(),
                        false, false, true, false, 0);
                this.textEntityDirect.addText("Press 'B' to spawn curve beam." + TextFieldEntity.LINE_FEED_SYMBOL,
                        5.0f, Misc.getButtonTextColor(),
                        false, false, true, false, 0);
                this.textEntityDirect.addText("Press 'N' to show your ship bounds for 2 second." + TextFieldEntity.LINE_FEED_SYMBOL,
                        5.0f, Misc.getButtonTextColor(),
                        false, false, true, false, 0);
                this.textEntityDirect.addText("Press 'H' to toggle switch for enjoy Hanabi.",
                        5.0f, Misc.getNegativeHighlightColor(),
                        false, false, true, true, 0);
                this.textEntityDirect.setFontSpace(-1.0f, 2.0f);
                this.textEntityDirect.setFieldSize(512.0f, 128.0f);
                this.textEntityDirect.setTextDataRefreshAllFromCurrentIndex();
                this.textEntityDirect.submitText();
                this.textEntityDirect.setCustomPrimeMatrix();
                this.textEntityDirect.setPrimeMatrix(TransformUtil.createWindowOrthoMatrix(null));
                this.textEntityDirect.setLocation(2.0f, (ShaderCore.getScreenHeight() + this.textEntityDirect.getCurrentVisualHeight()) * 0.5f);
                CombatRenderingManager.addCleanupPlugin(new CleanupData(this.textEntityDirect));
//                CombatRenderingManager.addEntity(BoxEnum.ENTITY_TEXT, textEntity);
            } else {
                textEntityDirect.directDraw();
                if (this.engine.getTotalElapsedTime(false) > 5.0f && !this.tog) {
                    this.tog = true;
                    TextFieldEntity.TextData a = this.textEntityDirect.getTextDataList().get(1);
                    a.text = "change." + TextFieldEntity.LINE_FEED_SYMBOL;
                    this.textEntityDirect.setTextDataRefreshIndex(1);
                    this.textEntityDirect.setTextDataRefreshAllFromCurrentIndex();
                    this.textEntityDirect.submitText();
                }
            }
            if (this.textFieldEntity == null) {
                this.textFieldEntity = new TextFieldEntity("graphics/fonts/orbitron20aabold.fnt");
                this.textFieldEntity.addText("#TEST-TEST-TEST#" + TextFieldEntity.LINE_FEED_SYMBOL,
                        0.0f, Misc.getButtonTextColor(),
                        false, false, true, true, 0);
                this.textFieldEntity.addText(" !@#$%^&*()_+-=1234567890`~[]{};':\",.<>/?|\\" + TextFieldEntity.LINE_FEED_SYMBOL,
                        0.0f, Misc.getPositiveHighlightColor(),
                        false, true, false, false, 0);
                this.textFieldEntity.addText("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz" + TextFieldEntity.LINE_FEED_SYMBOL,
                        0.0f, Misc.getPositiveHighlightColor(),
                        false, true, true, false, 0);
                this.textFieldEntity.addText("#test-test-test#",
                        10.0f, Misc.getNegativeHighlightColor(),
                        true, false, false, true, 0);
                this.textFieldEntity.setAlignment(TextFieldEntity.Alignment.MID);
                this.textFieldEntity.setFontSpace(8.0f, 5.0f);
                this.textFieldEntity.setFieldSize(350.0f, 512.0f);
                this.textFieldEntity.setTextDataRefreshAllFromCurrentIndex();
                this.textFieldEntity.submitText();
                this.textFieldEntity.setItalicFactor(90.0f);
                this.textFieldEntity.setGlobalTimer(0.0f, 8192.0f, 0.0f);
                this.textFieldEntity.setBlendBloomColor(true);
                this.textFieldEntity.setBloomColorStrength(0.5f);
                this.textFieldEntity.setLayer(CombatEngineLayers.ABOVE_PARTICLES_LOWER);
                CombatRenderingManager.addEntity(textFieldEntity);
            } else {
                if (engine.getPlayerShip() != null) this.textFieldEntity.setLocation(engine.getPlayerShip().getLocation());
            }
        }

        public void renderInUICoords(ViewportAPI viewport) {
            if (this.engine.getPlayerShip() == null) return;
            ShipAPI player = this.engine.getPlayerShip();
            this.locVec.setAlpha(viewport.getAlphaMult() * 0.8f);
            this.arc.setAlpha(viewport.getAlphaMult() * 0.5f);
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glTranslatef(ShaderCore.getScreenWidth() * 0.5f, ShaderCore.getScreenHeight() + 82.0f, 0.0f);
            this.arc.glDraw(225.0f, 225.0f);
            GL11.glTranslatef(-112.0f, -112.0f, 0.0f);
            this.locVec.glDraw(player.getLocation().x, 96.0f, 30.0f);
            GL11.glTranslatef(0.0f, -34.0f, 0.0f);
            this.locVec.glDraw(player.getLocation().y, 96.0f, 30.0f);
            GL11.glTranslatef(0.0f, -34.0f, 0.0f);
            this.locVec.setColorWithoutAlpha(Misc.getButtonTextColor());
            this.locVec.glDraw(player.getAngularVelocity(), 96.0f, 30.0f);
            GL11.glTranslatef(128.0f, 0.0f, 0.0f);
            this.locVec.glDraw(player.getFacing(), 96.0f, 30.0f);
            GL11.glTranslatef(0.0f, 34.0f, 0.0f);
            this.locVec.setColorWithoutAlpha(Misc.getPositiveHighlightColor());
            this.locVec.glDraw(player.getVelocity().y, 96.0f, 30.0f);
            GL11.glTranslatef(0.0f, 34.0f, 0.0f);
            this.locVec.glDraw(player.getVelocity().x, 96.0f, 30.0f);
            GL11.glPopMatrix();

            RenderingUtil.debugText("Elapsed combat time = " + String.format("%.5f", this.time) + 's');
        }
    }

    private final static class CleanupData extends BaseTemporaryCleanupPlugin {
        private final RenderDataAPI data;

        public CleanupData(RenderDataAPI data) {
            this.data = data;
        }

        public void cleanupCombatOnce() {
            if (this.data != null && !this.data.hasDelete()) this.data.delete();
        }
    }

    private final static class LogicalCombatBackgroundThreadPluginExample extends BaseBackgroundEveryFramePlugin implements TemporaryCleanupPlugin {
        private final TextFieldEntity text = new TextFieldEntity("graphics/fonts/FiraCodeModified/Fira_Code_Regular_20.fnt");
        private final int charLength;
        private boolean toggle = false;
        private volatile boolean _expired = false;
        private float timer = 0.0f;

        public LogicalCombatBackgroundThreadPluginExample() {
            this.text.addText("R",
                    0.0f, new Color(0xD55FDE),
                    false, true, false, false, 0);
            this.text.addText("\"",
                    0.0f, new Color(0x89CA78),
                    false, false, false, false, 0);
            this.text.addText("(",
                    0.0f, new Color(0x2BBAC5),
                    false, false, false, false, 0);
            this.text.addText("Hello world from background thread!",
                    0.0f, new Color(0x89CA78),
                    false, false, false, false, 0);
            this.text.addText(")",
                    0.0f, new Color(0x2BBAC5),
                    false, false, false, false, 0);
            this.text.addText("\"",
                    0.0f, new Color(0x89CA78),
                    false, false, false, false, 0);
            this.text.addText(";",
                    0.0f, Color.WHITE,
                    false, false, false, false, 0);
            this.text.addText("|",
                    0.0f, new Color(0x61AFEF),
                    false, false, false, false, 0);
            this.text.setAlignment(TextFieldEntity.Alignment.MID);
            this.text.setFieldSize(640.0f, 128.0f);
            this.text.setTextDataRefreshAllFromCurrentIndex();
            this.text.submitText();
            this.text.setGlobalTimer(0.0f, 8192.0f, 0.0f);
            this.text.setBlendBloomColor(true);
            this.text.setBloomColorStrength(0.5f);
            this.text.setLayer(CombatEngineLayers.ABOVE_PARTICLES);
            this.text.setCustomPrimeMatrix();
            this.text.setPrimeMatrix(TransformUtil.createWindowOrthoMatrix(null));
            this.charLength = this.text.getValidCharLength();
            CombatRenderingManager.addEntity(this.text);
            CombatRenderingManager.addBackgroundLogicalPlugin(this);
            CombatRenderingManager.addCleanupPlugin(this);
        }

        public boolean isAdvanceExpired() {
            return this._expired || this.text.hasDelete();
        }

        // it was thread safety.
        public void runBeginAdvance(float amount, boolean isPaused) {
            Vector2f mousePosScreenSpace = new Vector2f(Mouse.getX() / Global.getSettings().getScreenScaleMult(), Mouse.getY() / Global.getSettings().getScreenScaleMult());
            this.text.setLocation(mousePosScreenSpace.x - 320.0f, mousePosScreenSpace.y + 64.0f);
            if (this.timer > 0.5f) {
                this.timer -= 0.5f;
                this.toggle = !this.toggle;
            }
            this.timer += amount;
            this.text.setShouldRenderingCharCount(this.toggle ? this.charLength - 1 : this.charLength);
        }

        public void cleanupCombatOnce() {
            this._expired = true;
        }

        // ignored
        public void cleanupCampaignOnce() {}
    }

    private final static class RenderingPlugin extends BaseCombatLayeredRenderingPlugin {
        private TextFieldObject to = null;
//        private LegacyModelData mesh = null;

        public void render(CombatEngineLayers layer, ViewportAPI viewport) {
            if (Global.getCombatEngine() == null) return;
            float time = Global.getCombatEngine().getTotalElapsedTime(false);
            if (layer == RenderingUtil.getLowestCombatLayer()) {
                BaseShaderData program = ShaderCore.getTestMissionProgram();
                if (program == null || !program.isValid()) return;
                GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT | GL11.GL_VIEWPORT_BIT);
                GL11.glViewport(0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glPushMatrix();
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glPushMatrix();
                GL11.glLoadIdentity();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                program.active();
                GL20.glUniform1f(program.location[0], time);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2f(-1.0f, -1.0f);
                GL11.glVertex2f(-1.0f, 1.0f);
                GL11.glVertex2f(1.0f, 1.0f);
                GL11.glVertex2f(1.0f, -1.0f);
                GL11.glEnd();
                program.close();
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glPopMatrix();
                GL11.glPopAttrib();

                if (this.to == null) {
                    this.to = new TextFieldObject("graphics/fonts/FiraCodeModified/Fira_Code_Regular_20.fnt");
//                    this.to.setAlignment(TextFieldEntity.Alignment.LEFT);
                    this.to.setFontSpace(0.0f, 2.0f);
                    this.to.setFieldSize(800.0f, 512.0f);
                    this.to.addText("#include", 0, new Color(0xD55FDE), true);
                    this.to.addText(" <cstdint>\n", 0, new Color(0x89CA78));
                    this.to.addText("#include", 0, new Color(0xD55FDE), true);
                    this.to.addText(" <iostream>\n\n", 0, new Color(0x89CA78));

                    this.to.addText("// GL11 text, in C++ ...?\n", 0, new Color(0x5C6370));

                    this.to.addText("int", 0, new Color(0xD55FDE), true);
                    this.to.addText(" main", 0, new Color(0x61AFEF));
                    this.to.addText("() {\n", 0, Color.WHITE);

                    this.to.addText("\tconstexpr", 0, new Color(0xD55FDE), true);
                    this.to.addText(" uint32_t", 0, new Color(0xE5C07B));
                    this.to.addText(" limit = ", 0, Color.WHITE);
                    this.to.addText("UINT32_MAX", 0, new Color(0xEF596F));
                    this.to.addText(" >> ", 0, Color.WHITE);
                    this.to.addText("1", 0, new Color(0xE5C07B));
                    this.to.addText(", limitI = limit - ", 0, Color.WHITE);
                    this.to.addText("1", 0, new Color(0xE5C07B));
                    this.to.addText(";\n\n", 0, Color.WHITE);

                    this.to.addText("\tfor", 0, new Color(0xD55FDE), true);
                    this.to.addText(" (", 0, Color.WHITE);
                    this.to.addText("uint32_t", 0, new Color(0xE5C07B));
                    this.to.addText(" i = ", 0, Color.WHITE);
                    this.to.addText("0", 0, new Color(0xE5C07B));
                    this.to.addText("; i < limit; i++) {\n", 0, Color.WHITE);

                    this.to.addText("\t\tstd", 0, new Color(0xE5C07B));
                    this.to.addText("::", 0, Color.WHITE);
                    this.to.addText("cout", 0, new Color(0xEF596F));
                    this.to.addText(" << ", 0, Color.WHITE);
                    this.to.addText("\"I NEED POINTER!!!\"", 0, new Color(0x89CA78));
                    this.to.addText(";\n", 0, Color.WHITE);

                    this.to.addText("\t\tif", 0, new Color(0xD55FDE), true);
                    this.to.addText(" (i == limitI) ", 0, Color.WHITE);
                    this.to.addText("std", 0, new Color(0xE5C07B));
                    this.to.addText("::", 0, Color.WHITE);
                    this.to.addText("cout", 0, new Color(0xEF596F));
                    this.to.addText(" << ", 0, Color.WHITE);
                    this.to.addText("std", 0, new Color(0xE5C07B));
                    this.to.addText("::", 0, Color.WHITE);
                    this.to.addText("endl", 0, new Color(0x61AFEF));
                    this.to.addText(";\n", 0, Color.WHITE);
                    this.to.addText("\t\telse", 0, new Color(0xD55FDE), true);
                    this.to.addText(" std", 0, new Color(0xE5C07B));
                    this.to.addText("::", 0, Color.WHITE);
                    this.to.addText("cout", 0, new Color(0xEF596F));
                    this.to.addText(" << ", 0, Color.WHITE);
                    this.to.addText("'", 0, new Color(0x89CA78));
                    this.to.addText("\\n", 0, new Color(0x2BBAC5));
                    this.to.addText("'", 0, new Color(0x89CA78));
                    this.to.addText(";\n\t}\n\n", 0, Color.WHITE);

                    this.to.addText("\treturn", 0, new Color(0xD55FDE), true);
                    this.to.addText(" 0", 0, new Color(0xD19A66));

                    this.to.addText(";\n}", 0, Color.WHITE);
                    this.to.setTextDataRefreshAllFromCurrentIndex();
                    this.to.submitText();
                }

                Vector2f loc = new Vector2f(Global.getCombatEngine().getPlayerShip().getLocation());
                loc.x -= 300;
                loc.y -= 300;
                this.to.render(loc, (time % 2.0f) * 180.0f, false, null);

//                if (this.mesh == null) {
//                    this.mesh = ModelManager.tryLegacyModelData("graphics/objs/BUtil_box.obj");
//                } else {
//                    GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//                    this.mesh.glBindSpriteBeforeDraw(TextureManager.tryTexture("graphics/textures/diffuse/BUtil_box_Diffuse.png"));
//                    GL11.glPushMatrix();
//                    GL11.glTranslatef(loc.x - 200.0f, loc.y + 300.0f, 0.0f);
//                    GL11.glRotatef((time % 2.0f) * 180.0f, 1.0f, 0.0f, 0.0f);
//                    GL11.glRotatef((time % 4.0f) * 90.0f, 0.0f, 1.0f, 0.0f);
//                    GL11.glRotatef((time % 3.0f) * 120.0f, 0.0f, 0.0f, 1.0f);
//                    GL11.glScalef(256.0f, 256.0f, 256.0f);
//                    GL11.glEnable(GL11.GL_DEPTH_TEST);
//                    GL11.glEnable(GL32.GL_DEPTH_CLAMP);
//                    GL11.glEnable(GL11.GL_CULL_FACE);
//                    GL11.glEnable(GL11.GL_BLEND);
//                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//                    this.mesh.glDraw(false, true);
//                    GL11.glPopMatrix();
//                    GL11.glPopAttrib();
//                }
            }
        }

        public float getRenderRadius() {
            return Float.MAX_VALUE;
        }

        public EnumSet<CombatEngineLayers> getActiveLayers() {
            return EnumSet.of(RenderingUtil.getLowestCombatLayer());
        }
    }
}