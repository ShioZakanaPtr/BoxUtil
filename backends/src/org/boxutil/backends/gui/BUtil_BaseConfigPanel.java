package org.boxutil.backends.gui;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.boxutil.base.BaseShaderPacksContext;
import org.boxutil.config.BoxConfigGUI;
import org.boxutil.config.BoxConfigs;
import org.boxutil.define.BoxDatabase;
import org.boxutil.manager.KernelCore;
import org.boxutil.manager.ShaderCore;
import org.boxutil.units.standard.misc.UIBorderObject;
import org.boxutil.util.CalculateUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class BUtil_BaseConfigPanel implements CustomUIPanelPlugin {
    private final static float _SCREEN_SCALE_DIV = 1.0f / Global.getSettings().getScreenScaleMult();
    private final static float _SPACE_BOTTOM = 30.0f;
    private final static float _SPACE_PAGE_BOTTOM = 26.0f;
    private final static float _BORDER_WIDTH = 8.0f;
    private final static float _ITEM_SPACE = 5.0f;
    private final static float _ITEM_SPLIT_WIDTH = 2.0f;
    private final static float _MAIN_BUTTON_SPACE = 5.0f;
    private final static float _TIPS_WIDTH_FACTOR = 0.33333f;
    private final static float _SHADERPACKS_ITEM_ICON_WIDTH = 256.0f;
    private final static float _SHADERPACKS_ITEM_ICON_HEIGHT = 128.0f;

    private final static byte _BUTTON_CANCEL = 100;
    private final static byte _BUTTON_SAVE = 101;
    private final static byte _BUTTON_DEFAULTS = 102;
    private final static byte _BUTTON_UNDO = 103;
    private final static byte _BUTTON_TIPS_EXIT = 104;
    private final static byte _BUTTON_TIPS_RETURN = 105;
    private final static byte _BUTTON_SHADERPACKS_SELECT = 106;
    private final static byte _BUTTON_PAGE_CORE = 110;
    private final static byte _BUTTON_PAGE_SHADERPACKS = 111;

    private final static float _ITEM_HEIGHT = 64.0f;
    private final static float _TRACKBAR_NUM_SPACES = 84.0f;
    private final static float _TRACKBAR_HEIGHT = 50.0f;
    private final static float _ITEM_BUTTON_WIDTH = 64.0f;
    private final static float _ITEM_BUTTON_HEIGHT = 32.0f;

    private final static Color _BUTTON_COLOR_BG = Global.getSettings().getColor("buttonBgDark");
    private final static float _EVENT_CD = 0.05f;
    private final float[] _ITEM_COLOR_BG = new float[4];
    private final float[] _ITEM_SPLIT_COLOR = new float[4];
    private final float[] _ITEM_POS_COLOR = new float[4];
    private final float[] _ITEM_NEG_COLOR = new float[4];
    private CustomPanelAPI panel = null;
    private final BoxConfigGUI host;
    private final UIBorderObject backgroundObject;
    private CloseTips tipsPlugin = null;
    private CustomPanelAPI tipsPanel = null;
    private final CustomPanelAPI[] pageBody = new CustomPanelAPI[4]; // core, shaderpacks, shaderpacksDetails, shaderpacksList
    private final TooltipMakerAPI[] mainConfigMakers = new TooltipMakerAPI[8]; // pageButton, title, configButton, coreBody, shaderpacksHeading, shaderpacksList, shaderpacksDesc, shaderpacksSelect
    private final float[] state = new float[10];
    private LabelAPI _heading = null;
    private LabelAPI _shaderpacksDetails_heading = null;
    private LabelAPI _shaderpacksInitTips = null;
    private ButtonAPI _cancelButton = null;
    private ButtonAPI _saveButton = null;
    private ButtonAPI _defaultButton = null;
    private ButtonAPI _undoButton = null;
    private ButtonAPI _shaderPacksSelectButton = null;
    private ButtonAPI _pageCoreButton = null;
    private ButtonAPI _pageShaderPacksButton = null;
    private boolean _configChanged = false;
    private final List<Item> _items = new LinkedList<>();
    private final List<ShaderPacksItem> _shaderPacksItems = new LinkedList<>();
    private final LabelAPI[] _itemTitle = new LabelAPI[3];
    private boolean panelOn = false;
    private int backgroundTex = 0;
    private float[] backgroundTexUV = new float[2];

    private static String _shaderPacksInitInfo = null;
    private static boolean _shaderpacksListChanged = true;
    private BaseShaderPacksContext selectedShaderPacks = null;
    private BaseShaderPacksContext nextSelectedShaderPacks = null;
    private final float[] _shaderPacksDetailsPos = new float[3];
    private final float[] _shaderPacksListPos = new float[3];
    private byte _pageFlag = 0;
    private byte _pageSwitchFlag = 0;

    public BUtil_BaseConfigPanel(BoxConfigGUI host, float width, float height, float BLx, float BLy) {
        this.host = host;
        this.backgroundObject = new UIBorderObject(false, true);
        this.state[0] = width;
        this.state[1] = height;
        this.state[2] = BLx;
        this.state[3] = BLy;
        this.state[4] = this.state[2] - _BORDER_WIDTH;
        this.state[5] = this.state[3] - _BORDER_WIDTH;
        this.state[6] = width + _BORDER_WIDTH + _BORDER_WIDTH;
        this.state[7] = height + _BORDER_WIDTH + _BORDER_WIDTH;
        this.backgroundObject.setSize(this.state[6], this.state[7]);

        final float[] _itemSplitColorTmp = Global.getSettings().getColor("widgetBorderColorDark").getComponents(new float[4]);
        this._ITEM_SPLIT_COLOR[0] = _itemSplitColorTmp[0];
        this._ITEM_SPLIT_COLOR[1] = _itemSplitColorTmp[1];
        this._ITEM_SPLIT_COLOR[2] = _itemSplitColorTmp[2];
        this._ITEM_SPLIT_COLOR[3] = _itemSplitColorTmp[3] * 0.42f;
        final float[] _itemColorTmp = _BUTTON_COLOR_BG.getComponents(new float[4]);
        this._ITEM_COLOR_BG[0] = _itemColorTmp[0];
        this._ITEM_COLOR_BG[1] = _itemColorTmp[1];
        this._ITEM_COLOR_BG[2] = _itemColorTmp[2];
        this._ITEM_COLOR_BG[3] = _itemColorTmp[3] * 0.42f;
        final float[] _itemPosColorTmp = Misc.getPositiveHighlightColor().getComponents(new float[4]);
        this._ITEM_POS_COLOR[0] = _itemPosColorTmp[0];
        this._ITEM_POS_COLOR[1] = _itemPosColorTmp[1];
        this._ITEM_POS_COLOR[2] = _itemPosColorTmp[2];
        this._ITEM_POS_COLOR[3] = _itemPosColorTmp[3] * 0.42f;
        final float[] _itemNegColorTmp = Misc.getNegativeHighlightColor().getComponents(new float[4]);
        this._ITEM_NEG_COLOR[0] = _itemNegColorTmp[0];
        this._ITEM_NEG_COLOR[1] = _itemNegColorTmp[1];
        this._ITEM_NEG_COLOR[2] = _itemNegColorTmp[2];
        this._ITEM_NEG_COLOR[3] = _itemNegColorTmp[3] * 0.42f;
    }

    private void addCoreComponent() {
        this.panel.addComponent(this.pageBody[0]);
    }

    private void removeCoreComponent() {
        this.panel.removeComponent(this.pageBody[0]);
    }

    private void addShaderPacksComponent() {
        this.panel.addComponent(this.pageBody[1]);
        this.panel.addComponent(this.pageBody[2]);
        this.panel.addComponent(this.pageBody[3]);
    }

    private void removeShaderPacksComponent() {
        this.panel.removeComponent(this.pageBody[1]);
        this.panel.removeComponent(this.pageBody[2]);
        this.panel.removeComponent(this.pageBody[3]);
    }

    private void switchCorePage() {
        if (this._pageFlag == 1) {
            this.removeShaderPacksComponent();
            this.addCoreComponent();
            this._shaderPacksSelectButton.setEnabled(false);
            if (this._heading != null) this._heading.setText(BoxConfigs.getString("BUtil_ConfigPanel_Title_Core"));
            this.selectedShaderPacks = null;
            this._pageFlag = 0;
        }
    }

    private void switchShaderPacksPage() {
        if (this._pageFlag == 0) {
            this.removeCoreComponent();
            this.addShaderPacksComponent();
            if (this._heading != null) this._heading.setText(BoxConfigs.getString("BUtil_ConfigPanel_Title_ShaderPacks"));
            this.refreshShaderPacksDetailsComponent(null);
            this._pageFlag = 1;
        }
    }

    private void refreshShaderPacksListComponent() {
        Set<BaseShaderPacksContext> oriSet = BoxConfigs.getLoadedShaderPacksSetCopy();
        if (this.pageBody[3] != null) {
            for (ShaderPacksItem item : this._shaderPacksItems) {
                item.destroy(this.pageBody[3]);
            }
            this.panel.removeComponent(this.pageBody[3]);
            this._shaderPacksItems.clear();
        }

        this.pageBody[3] = Global.getSettings().createCustom(this._shaderPacksListPos[0], this._shaderPacksListPos[1], new CoreBody());
        this.pageBody[3].getPosition().inTL(0.0f, this._shaderPacksListPos[2]);
        this.mainConfigMakers[5] = this.pageBody[3].createUIElement(this._shaderPacksListPos[0], this._shaderPacksListPos[1], true);
        this.mainConfigMakers[5].getPosition().inTL(0.0f, 0.0f);
        if (!oriSet.isEmpty()) {
            ShaderPacksItem item;
            int index = 0;
            for (BaseShaderPacksContext context : oriSet) {
                item = new ShaderPacksItem(this, context, this._shaderPacksListPos[0], index != oriSet.size() - 1);
                item.add(this.mainConfigMakers[5]);
                this._shaderPacksItems.add(item);
                ++index;
            }
        }
        this.pageBody[3].addUIElement(this.mainConfigMakers[5]);
        this.panel.addComponent(this.pageBody[3]);
    }

    private void refreshShaderPacksDetailsComponent(BaseShaderPacksContext context) {
        if (this.pageBody[2] != null) this.panel.removeComponent(this.pageBody[2]);
        this.selectedShaderPacks = context;
        final ShaderPacksDetailsBody detailsBody = new ShaderPacksDetailsBody(this, this._shaderPacksDetailsPos[0], this._shaderPacksDetailsPos[1]);
        this.pageBody[2] = Global.getSettings().createCustom(this._shaderPacksDetailsPos[0], this._shaderPacksDetailsPos[1], detailsBody);
        this.pageBody[2].getPosition().inTR(_ITEM_SPACE, this._shaderPacksDetailsPos[2]);
        this.mainConfigMakers[6] = this.pageBody[2].createUIElement(this._shaderPacksDetailsPos[0], this._shaderPacksDetailsPos[1], context != null);
        this.mainConfigMakers[6].getPosition().inTR(0.0f, 0.0f);
        detailsBody.setPosition(this.pageBody[2].getPosition(), this.mainConfigMakers[6].getPosition());

        if (context == null) {
            final float posY = this._shaderPacksDetailsPos[1] * 0.5f;
            float heightA, heightB;
            String[] str = BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Details").split("\n");
            String strHL = BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Details_HL0");
            LabelAPI paraA = this.mainConfigMakers[6].addPara(str[0], 0.0f);
            paraA.setAlignment(Alignment.MID);
            heightA = paraA.computeTextHeight(str[0]);
            LabelAPI paraB = this.mainConfigMakers[6].addPara(str[1], 0, Misc.getHighlightColor(), strHL);
            paraB.setAlignment(Alignment.MID);
            heightB = paraB.computeTextHeight(str[1].replace("%s", strHL));
            paraA.getPosition().inBMid(-posY + heightA + heightB);
            paraB.getPosition().inBMid(-posY + heightB);
        } else {
            this.mainConfigMakers[6].addSpacer(5.0f);
            context.createDetailsPanel(this.mainConfigMakers[6], this.pageBody[2].getPosition(), this.mainConfigMakers[6].getPosition());
        }

        this.pageBody[2].addUIElement(this.mainConfigMakers[6]);
        this.panel.addComponent(this.pageBody[2]);
    }

    private static ButtonAPI configButton(TooltipMakerAPI host, String id, byte type, float width, float height, int hotkey, CutStyle style) {
        ButtonAPI button = host.addButton(BoxConfigs.getString(id), type, Misc.getButtonTextColor(), _BUTTON_COLOR_BG, Alignment.MID, style, width, height, 0);
        button.setMouseOverSound("BUtil_button_in");
        button.setButtonPressedSound("BUtil_button_down");
        if (hotkey != -1024) button.setShortcut(hotkey, false);
        return button;
    }

    public void init(CustomPanelAPI panel) {
        this.panel = panel;

        TooltipMakerAPI tooltipTMP = this.panel.createUIElement(this.state[0], _SPACE_BOTTOM, false);
        tooltipTMP.getPosition().inTMid(-this.state[3] + _ITEM_SPACE);
        tooltipTMP.setParaInsigniaVeryLarge();
        this._shaderpacksInitTips = tooltipTMP.addPara(BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Falied").replace("%s", "null"), Misc.getNegativeHighlightColor(), 0.0f);
        this._shaderpacksInitTips.setAlignment(Alignment.TMID);
        this._shaderpacksInitTips.getPosition().inTMid(0.0f);
        this._shaderpacksInitTips.setOpacity(0.0f);
        this.panel.addUIElement(tooltipTMP);

        this.mainConfigMakers[0] = this.panel.createUIElement(this.state[0], _SPACE_PAGE_BOTTOM, false);
        this.mainConfigMakers[0].getPosition().inTMid(-_SPACE_PAGE_BOTTOM - _SPACE_PAGE_BOTTOM - _BORDER_WIDTH);
        this.mainConfigMakers[0].setButtonFontOrbitron20();
        final float buttonWidth = Math.max(this.state[0] * 0.15f, 180.0f);
        this._pageCoreButton = configButton(this.mainConfigMakers[0], "BUtil_ConfigPanel_Page_Core", _BUTTON_PAGE_CORE, buttonWidth, _SPACE_PAGE_BOTTOM, Keyboard.KEY_1, CutStyle.TOP);
        this._pageCoreButton.getPosition().inBL(_MAIN_BUTTON_SPACE, 0.0f);
        this._pageShaderPacksButton = configButton(this.mainConfigMakers[0], "BUtil_ConfigPanel_Page_ShaderPacks", _BUTTON_PAGE_SHADERPACKS, buttonWidth, _SPACE_PAGE_BOTTOM, Keyboard.KEY_2, CutStyle.TOP);
        this._pageShaderPacksButton.getPosition().inBL(buttonWidth + _MAIN_BUTTON_SPACE + _MAIN_BUTTON_SPACE, 0.0f);

        this.mainConfigMakers[1] = this.panel.createUIElement(this.state[0], _SPACE_BOTTOM, false);
        this.mainConfigMakers[1].getPosition().inTMid(0.0f);
        this._heading = this.mainConfigMakers[1].addSectionHeading(BoxConfigs.getString("BUtil_ConfigPanel_Title_Core"), Misc.getTextColor(), Misc.getDarkPlayerColor(), Alignment.MID, 0.0f);
        this._heading.getPosition().inTMid(0.0f);

        this.mainConfigMakers[2] = this.panel.createUIElement(this.state[0], _SPACE_BOTTOM, false);
        this.mainConfigMakers[2].getPosition().inBMid(-_SPACE_BOTTOM - _BORDER_WIDTH);
        this.mainConfigMakers[2].setButtonFontOrbitron20();
        final float halfWidth = this.state[0] * 0.5f;
        this._cancelButton = configButton(this.mainConfigMakers[2], "BUtil_ConfigPanel_Cancel", _BUTTON_CANCEL, buttonWidth, _SPACE_BOTTOM, Keyboard.KEY_ESCAPE, CutStyle.BOTTOM);
        this._cancelButton.getPosition().inBR(halfWidth - buttonWidth * 2.0f - _MAIN_BUTTON_SPACE * 1.5f, 0.0f);
        this._saveButton = configButton(this.mainConfigMakers[2], "BUtil_ConfigPanel_Save", _BUTTON_SAVE, buttonWidth, _SPACE_BOTTOM, Keyboard.KEY_RETURN, CutStyle.BOTTOM);
        this._saveButton.getPosition().inBR(halfWidth - buttonWidth - _MAIN_BUTTON_SPACE * 0.5f, 0.0f);
        this._saveButton.setEnabled(false);
        this._defaultButton = configButton(this.mainConfigMakers[2], "BUtil_ConfigPanel_Default", _BUTTON_DEFAULTS, buttonWidth, _SPACE_BOTTOM, Keyboard.KEY_D, CutStyle.BOTTOM);
        this._defaultButton.getPosition().inBL(halfWidth - buttonWidth - _MAIN_BUTTON_SPACE * 0.5f, 0.0f);
        this._undoButton = configButton(this.mainConfigMakers[2], "BUtil_ConfigPanel_Undo", _BUTTON_UNDO, buttonWidth, _SPACE_BOTTOM, Keyboard.KEY_U, CutStyle.BOTTOM);
        this._undoButton.getPosition().inBL(halfWidth - buttonWidth * 2.0f - _MAIN_BUTTON_SPACE * 1.5f, 0.0f);
        this._undoButton.setEnabled(false);

        final float panelTitleHeight = this._heading.getPosition().getHeight(), bodyHeight = this.state[1] - panelTitleHeight;
        this.pageBody[0] = Global.getSettings().createCustom(this.state[0], bodyHeight, new CoreBody());
        this.pageBody[0].getPosition().inBL(0.0f, 0.0f);
        this.pageBody[1] = Global.getSettings().createCustom(this.state[0], bodyHeight, new ShaderPacksSelectCoreBody(this));
        this.pageBody[1].getPosition().inBL(0.0f, 0.0f);

        this.mainConfigMakers[3] = this.pageBody[0].createUIElement(this.state[0], bodyHeight, true);
        this.mainConfigMakers[3].getPosition().inBMid(0.0f);
        this.mainConfigMakers[3].setParaInsigniaVeryLarge();
        this._itemTitle[0] = this.initItemGlobal(this.mainConfigMakers[3]);
        this.mainConfigMakers[3].addSpacer(_SPACE_BOTTOM * 1.5f);
        this._itemTitle[1] = this.initItemCommon(this.mainConfigMakers[3]);
        this.mainConfigMakers[3].addSpacer(_SPACE_BOTTOM * 1.5f);
        this._itemTitle[2] = this.initItemMisc(this.mainConfigMakers[3]);
        this.pageBody[0].addUIElement(this.mainConfigMakers[3]);

        final float listWidth = 480.0f, detailsWidth = this.state[0] - listWidth - _BORDER_WIDTH - _ITEM_SPACE;
        this.mainConfigMakers[4] = this.pageBody[1].createUIElement(detailsWidth, _SPACE_BOTTOM, false);
        this.mainConfigMakers[4].getPosition().inTR(_ITEM_SPACE, _MAIN_BUTTON_SPACE * 2.0f);
        this._shaderpacksDetails_heading = this.mainConfigMakers[4].addSectionHeading(BoxConfigs.getString("BUtil_ConfigPanel_Heading_ShaderPacks_Details"), Misc.getTextColor(), Misc.getDarkPlayerColor(), Alignment.MID, 0.0f);
        this._shaderpacksDetails_heading.getPosition().inTMid(0.0f);

        this.mainConfigMakers[7] = this.pageBody[1].createUIElement(detailsWidth, _SPACE_BOTTOM, false);
        this.mainConfigMakers[7].getPosition().inBR(_ITEM_SPACE, _MAIN_BUTTON_SPACE);
        this.mainConfigMakers[7].setButtonFontOrbitron20();
        this._shaderPacksSelectButton = configButton(this.mainConfigMakers[7], "BUtil_ConfigPanel_ShaderPacksSelect", _BUTTON_SHADERPACKS_SELECT, detailsWidth * 0.25f, _SPACE_BOTTOM, -1024, CutStyle.ALL);
        this._shaderPacksSelectButton.getPosition().inBMid(0.0f);
        this._shaderPacksSelectButton.setEnabled(false);

        {
            this._shaderPacksListPos[0] = listWidth;
            this._shaderPacksListPos[1] = bodyHeight;
            this._shaderPacksListPos[2] = panelTitleHeight;
            this.pageBody[3] = Global.getSettings().createCustom(this._shaderPacksListPos[0], this._shaderPacksListPos[1], new CoreBody());
            this.pageBody[3].getPosition().inTL(0.0f, this._shaderPacksListPos[2]);
            this.mainConfigMakers[5] = this.pageBody[3].createUIElement(this._shaderPacksListPos[0], this._shaderPacksListPos[1], true);
            this.mainConfigMakers[5].getPosition().inTL(0.0f, 0.0f);
            this.pageBody[3].addUIElement(this.mainConfigMakers[5]);

            final float shaderpacksDetailsHeight = panelTitleHeight + this._shaderpacksDetails_heading.getPosition().getHeight();
            this._shaderPacksDetailsPos[0] = detailsWidth;
            this._shaderPacksDetailsPos[1] = this.state[1] - shaderpacksDetailsHeight - _ITEM_SPACE * 2.0f - _MAIN_BUTTON_SPACE * 2.0f - _SPACE_BOTTOM;
            this._shaderPacksDetailsPos[2] = shaderpacksDetailsHeight + _ITEM_SPACE * 2.0f;
            final ShaderPacksDetailsBody detailsBody = new ShaderPacksDetailsBody(this, this._shaderPacksDetailsPos[0], this._shaderPacksDetailsPos[1]);
            this.pageBody[2] = Global.getSettings().createCustom(this._shaderPacksDetailsPos[0], this._shaderPacksDetailsPos[1], detailsBody);
            this.pageBody[2].getPosition().inTR(_ITEM_SPACE, this._shaderPacksDetailsPos[2]);
            this.mainConfigMakers[6] = this.pageBody[2].createUIElement(this._shaderPacksDetailsPos[0], this._shaderPacksDetailsPos[1], true);
            this.mainConfigMakers[6].getPosition().inTR(0.0f, 0.0f);
            this.pageBody[1].addUIElement(this.mainConfigMakers[4]);
            this.pageBody[1].addUIElement(this.mainConfigMakers[7]);
            this.pageBody[2].addUIElement(this.mainConfigMakers[6]);
            detailsBody.setPosition(this.pageBody[2].getPosition(), this.mainConfigMakers[6].getPosition());
        }

        this.panel.addUIElement(this.mainConfigMakers[0]);
        this.panel.addUIElement(this.mainConfigMakers[1]);
        this.panel.addUIElement(this.mainConfigMakers[2]);
        if (this._pageFlag == 0) this.addCoreComponent(); else this.addShaderPacksComponent();

        final float[] mainPanelSize = new float[]{ShaderCore.getScreenWidth() * _TIPS_WIDTH_FACTOR, 160.0f / _SCREEN_SCALE_DIV, 0.0f, 0.0f};
        mainPanelSize[2] = (this.state[0] - mainPanelSize[0]) * 0.5f;
        mainPanelSize[3] = (this.state[1] - mainPanelSize[1]) * 0.5f;
        this.tipsPlugin = new CloseTips(this, mainPanelSize[0], mainPanelSize[1], mainPanelSize[2], mainPanelSize[3]);
        this.tipsPanel = Global.getSettings().createCustom(mainPanelSize[0], mainPanelSize[1], this.tipsPlugin);
        this.tipsPanel.getPosition().inBL(mainPanelSize[2], mainPanelSize[3]);
        this.tipsPlugin.init(this.tipsPanel);
        refreshShaderPacksList();
    }

    private static String indexFill(byte index) {
        return index < 10 ? ("0" + index) : Byte.toString(index);
    }

    private LabelAPI initItemGlobal(TooltipMakerAPI maker) {
        LabelAPI titleGlobal = maker.addPara(BoxConfigs.getString("BUtil_ConfigPanel_Global"), Misc.getButtonTextColor(), _ITEM_SPACE);
        titleGlobal.setAlignment(Alignment.MID);
        maker.addSpacer(_ITEM_SPACE * 2.0f);
        for (byte i = 0; i < 3; i++) {
            Item item = new Item(this, this.state[0], i == 2, (byte) 0, i);
            item.add("BUtil_ConfigPanel_Global_" + indexFill(i), maker, true, false);
            this._items.add(item);
        }
        return titleGlobal;
    }

    private LabelAPI initItemCommon(TooltipMakerAPI maker) {
        LabelAPI titleCommon = maker.addPara(BoxConfigs.getString("BUtil_ConfigPanel_Common"), Misc.getButtonTextColor(), _ITEM_SPACE);
        titleCommon.setAlignment(Alignment.MID);
        maker.addSpacer(_ITEM_SPACE * 2.0f);
        for (byte i = 0; i < 4; i++) {
            boolean isBar = i == 0 || i == 1 || i == 2;
            Item item = new Item(this, this.state[0], i == 3, (byte) 1, i);
            item.add("BUtil_ConfigPanel_Common_" + indexFill(i), maker, false, isBar);
            this._items.add(item);
        }
        return titleCommon;
    }

    private LabelAPI initItemMisc(TooltipMakerAPI maker) {
        LabelAPI titleCommon = maker.addPara(BoxConfigs.getString("BUtil_ConfigPanel_Misc"), Misc.getButtonTextColor(), _ITEM_SPACE);
        titleCommon.setAlignment(Alignment.MID);
        maker.addSpacer(_ITEM_SPACE * 2.0f);
        boolean isGlobal;
        for (byte i = 0; i < 2; i++) {
            Item item = new Item(this, this.state[0], i == 1, (byte) 2, i);
            if (i == 0) isGlobal = false; else isGlobal = true;
            item.add("BUtil_ConfigPanel_Misc_" + indexFill(i), maker, isGlobal, false);
            this._items.add(item);
        }
        return titleCommon;
    }

    private void refreshItems() {
        for (Item item : this._items) item.refresh(true);
        for (ShaderPacksItem shaderPacksItem : this._shaderPacksItems) shaderPacksItem.refresh();
    }

    private void refreshItemsLanguage() {
        for (Item item : this._items) item.refreshLanguage();
        this.tipsPlugin.refresh();
        if (this._heading != null) this._heading.setText(BoxConfigs.getString(this._pageFlag == 0 ? "BUtil_ConfigPanel_Title_Core" : "BUtil_ConfigPanel_Title_ShaderPacks"));
        if (this._shaderpacksDetails_heading != null) this._shaderpacksDetails_heading.setText(BoxConfigs.getString("BUtil_ConfigPanel_Heading_ShaderPacks_Details"));
        if (this._cancelButton != null) {
            this._cancelButton.setText(BoxConfigs.getString("BUtil_ConfigPanel_Cancel"));
            this._cancelButton.setShortcut(Keyboard.KEY_ESCAPE, false);
        }
        if (this._saveButton != null) {
            this._saveButton.setText(BoxConfigs.getString("BUtil_ConfigPanel_Save"));
            this._saveButton.setShortcut(Keyboard.KEY_RETURN, false);
        }
        if (this._defaultButton != null) {
            this._defaultButton.setText(BoxConfigs.getString("BUtil_ConfigPanel_Default"));
            this._defaultButton.setShortcut(Keyboard.KEY_D, false);
        }
        if (this._undoButton != null) {
            this._undoButton.setText(BoxConfigs.getString("BUtil_ConfigPanel_Undo"));
            this._undoButton.setShortcut(Keyboard.KEY_U, false);
        }
        if (this._shaderPacksSelectButton != null) this._shaderPacksSelectButton.setText(BoxConfigs.getString("BUtil_ConfigPanel_ShaderPacksSelect"));
        if (this._pageCoreButton != null) {
            this._pageCoreButton.setText(BoxConfigs.getString("BUtil_ConfigPanel_Page_Core"));
            this._pageCoreButton.setShortcut(Keyboard.KEY_1, false);
        }
        if (this._pageShaderPacksButton != null) {
            this._pageShaderPacksButton.setText(BoxConfigs.getString("BUtil_ConfigPanel_Page_ShaderPacks"));
            this._pageShaderPacksButton.setShortcut(Keyboard.KEY_2, false);
        }
        if (this._itemTitle[0] != null) this._itemTitle[0].setText(BoxConfigs.getString("BUtil_ConfigPanel_Global"));
        if (this._itemTitle[1] != null) this._itemTitle[1].setText(BoxConfigs.getString("BUtil_ConfigPanel_Common"));
        if (this._itemTitle[2] != null) this._itemTitle[2].setText(BoxConfigs.getString("BUtil_ConfigPanel_Misc"));
    }

    private void refreshShaderPacksDetails(BaseShaderPacksContext context) {
        this.refreshShaderPacksDetailsComponent(context);
        if (context.isUsable(GLContext.getCapabilities()) && context != BoxConfigs.getCurrShaderPacksContext()) {
            if (!this._shaderPacksSelectButton.isEnabled()) {
                this._shaderPacksSelectButton.setEnabled(true);
                this._shaderPacksSelectButton.flash(false, 0.0f, 0.5f);
            }
        } else if (this._shaderPacksSelectButton.isEnabled()) {
            this._shaderPacksSelectButton.setEnabled(false);
        }
    }

    private final static class Tooltip extends BaseTooltipCreator {
        public String textID;
        public final boolean global;
        public final byte[] _ID = new byte[2];

        public Tooltip(String textID, boolean isGlobal, byte master, byte item) {
            this.textID = textID;
            this.global = isGlobal;
            this._ID[0] = master;
            this._ID[1] = item;
        }

        public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
            String mainText = BoxConfigs.getString(this.textID);
            if (this.global) {
                String tips = mainText + BoxConfigs.getString("BUtil_ConfigPanel_RebootTips_Value");
                LabelAPI label = tooltip.addPara(tips, 0.0f, Misc.getButtonTextColor(), this.getRebootValue(), BoxConfigs.getString("BUtil_ConfigPanel_RebootTips"));
                label.setHighlightColors(Misc.getButtonTextColor(), Misc.getNegativeHighlightColor());
            }
            else tooltip.addPara(mainText, 0);
        }

        private String getRebootValue() {
            return BoxConfigs.getRebootValueRealString(this._ID[0], this._ID[1]);
        }
    }

    private final static class Item extends BaseCustomUIPanelPlugin {
        private final BUtil_BaseConfigPanel _itemHost;
        private String id = null;
        private PositionAPI _pos = null;
        private LabelAPI _info = null;
        private LabelAPI _title = null;
        private BUtil_BaseTrackbar _trackbar = null;
        private final byte[] _ID = new byte[2];
        private final boolean _bottom;
        private boolean _pickSound = false;
        private boolean _buttonEnabled = true;
        private final float[] state = new float[7];

        public Item(BUtil_BaseConfigPanel itemHost, float width, boolean bottom, byte master, byte item) {
            this._itemHost = itemHost;
            this.state[5] = width - _ITEM_SPACE - _ITEM_SPACE;
            this._bottom = bottom;
            this._ID[0] = master;
            this._ID[1] = item;
        }

        public UIComponentAPI add(String id, TooltipMakerAPI maker, boolean isGlobal, boolean isTrackbar) {
            this.id = id;
            CustomPanelAPI itemPanel = Global.getSettings().createCustom(this.state[5], _ITEM_HEIGHT, this);
            itemPanel.getPosition().inTL(_ITEM_SPACE, 0.0f);

            TooltipMakerAPI makerFixed = itemPanel.createUIElement(this.state[5], _ITEM_HEIGHT, false);
            makerFixed.getPosition().inTL(0.0f, 0.0f);
            makerFixed.setParaOrbitronLarge();
            makerFixed.setTitleOrbitronLarge();
            makerFixed.setButtonFontOrbitron24Bold();
            makerFixed.setParaFontColor(Misc.getTextColor());
            makerFixed.setTitleFontColor(Misc.getTextColor());
            this._title = makerFixed.addTitle(BoxConfigs.getString(this.id));
            this._title.getPosition().inTL(_ITEM_SPACE * 4.0f, (_ITEM_HEIGHT - this._title.getPosition().getHeight()) * 0.5f);

            Pair<String, Boolean> value = BoxConfigs.getValueString(this._ID[0], this._ID[1]);
            if (isTrackbar) {
                int valueI = Integer.parseInt(value.one);
                if (valueI == -1) {
                    value.one = BoxConfigs.getString("BUtil_ConfigPanel_ValueLimit");
                }
                float trackbarWidth = this.state[5] * 0.42f - _ITEM_SPACE * 3.0f - _TRACKBAR_NUM_SPACES + _ITEM_BUTTON_WIDTH;
                byte masStep = 10; // index 0 and default
                if (this._ID[0] == 1 && this._ID[1] == 1) masStep = 5;
                if (this._ID[0] == 1 && this._ID[1] == 2) masStep = 7;
                this._trackbar = new BUtil_BaseTrackbar(makerFixed, value.one, trackbarWidth, _TRACKBAR_HEIGHT, _TRACKBAR_NUM_SPACES, masStep, 0.0f);
                if (valueI > 0) {
                    byte exponent = CalculateUtil.getExponentPOTMin(valueI);
                    if (this._ID[0] == 1 && this._ID[1] == 0) exponent -= 7;
                    if (this._ID[0] == 1 && this._ID[1] == 1) exponent -= 2;
                    if (this._ID[0] == 1 && this._ID[1] == 2) exponent -= 3;
                    this._trackbar.setCurrStep(exponent);
                } else if (valueI == -1) this._trackbar.setCurrStep(this._trackbar.getMaxStep());
                else this._trackbar.setCurrStep(0);
                this._trackbar.getPosition().inTR(_ITEM_SPACE * 5.0f, (_ITEM_HEIGHT - _TRACKBAR_HEIGHT) * 0.5f);
                this._info = this._trackbar.getValue();

                this.state[2] = _TRACKBAR_NUM_SPACES;
            } else {
                this._buttonEnabled = this._ID[0] == 0 && this._ID[1] == 2;
                if (this._buttonEnabled) this._buttonEnabled = !KernelCore.isValid() || KernelCore.getAllCLDevice().size() < 2;
                this._buttonEnabled = !this._buttonEnabled;
                ButtonAPI buttonLeft = makerFixed.addButton("<", false, Misc.getButtonTextColor(), _BUTTON_COLOR_BG, Alignment.MID, CutStyle.ALL, _ITEM_BUTTON_WIDTH, _ITEM_BUTTON_HEIGHT, 0);
                buttonLeft.setMouseOverSound("BUtil_button_in");
                buttonLeft.setButtonPressedSound("BUtil_button_down");
                buttonLeft.getPosition().inTR(_ITEM_SPACE * 2.0f + this.state[5] * 0.42f, (_ITEM_HEIGHT - _ITEM_BUTTON_HEIGHT) * 0.5f);
                buttonLeft.setEnabled(this._buttonEnabled);
                ButtonAPI buttonRight = makerFixed.addButton(">", true, Misc.getButtonTextColor(), _BUTTON_COLOR_BG, Alignment.MID, CutStyle.ALL, _ITEM_BUTTON_WIDTH, _ITEM_BUTTON_HEIGHT, 0);
                buttonRight.setMouseOverSound("BUtil_button_in");
                buttonRight.setButtonPressedSound("BUtil_button_down");
                buttonRight.getPosition().inTR(_ITEM_SPACE * 2.0f, (_ITEM_HEIGHT - _ITEM_BUTTON_HEIGHT) * 0.5f);
                buttonRight.setEnabled(this._buttonEnabled);
                this.state[2] = _ITEM_BUTTON_WIDTH;

                this._info = makerFixed.addPara(value.one, 0.0f);
                this._info.setColor(value.two ? Misc.getTextColor() : Misc.getNegativeHighlightColor());
                this._info.setAlignment(Alignment.MID);
                this._info.getPosition().inTR(_ITEM_SPACE * 2.0f + _ITEM_BUTTON_WIDTH, (_ITEM_HEIGHT - this._info.getPosition().getHeight()) * 0.5f);
                this._info.getPosition().setSize(this.state[5] * 0.42f - _ITEM_BUTTON_WIDTH, this._info.getPosition().getHeight());
            }
            this.state[0] = this.state[5] * 0.58f - _ITEM_SPACE * 2.0f;
            this.state[2] = this.state[5] - _ITEM_SPACE * 2.0f - this.state[2];
            this.state[4] = (this.state[0] + this.state[2]) * 0.5f;
            this.state[1] = (_ITEM_HEIGHT - _ITEM_BUTTON_HEIGHT) * 0.5f;
            this.state[3] = this.state[1] + _ITEM_BUTTON_HEIGHT;

            makerFixed.addTooltipTo(new Tooltip(this.id + "P", isGlobal, this._ID[0], this._ID[1]), makerFixed, TooltipMakerAPI.TooltipLocation.BELOW);
            makerFixed.setHeightSoFar(_ITEM_HEIGHT);
            itemPanel.addUIElement(makerFixed);
            this._pos = itemPanel.getPosition();
            if (isTrackbar) this._trackbar.setInputCheck(this._pos);
            this.refresh(isTrackbar);
            return maker.addCustom(itemPanel, _ITEM_SPACE);
        }

        public void advance(float amount) {
            float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
            float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
            boolean within = mouseX >= this._pos.getX() && mouseY >= this._pos.getY() && mouseX <= this._pos.getX() + this._pos.getWidth() && mouseY <= this._pos.getY() + this._pos.getHeight();
            within &= !this._itemHost.tipsPlugin.panelOn;
            boolean barActive = this._trackbar != null && this._trackbar.isActive();
            float a5 = amount * 5.0f;
            if ((within || barActive) && this.state[6] < 1.0f) {
                this.state[6] = Math.min(this.state[6] + a5 + a5, 1.0f);
                if (!this._pickSound) {
                    this._pickSound = true;
                    Global.getSoundPlayer().playUISound("BUtil_ui_pick", 1.0f, 1.0f);
                }
            } else if (!within && !barActive && this.state[6] > 0.0f) {
                if (this._pickSound) this._pickSound = false;
                this.state[6] -= a5;
            }
            if (this._trackbar != null) {
                if (this._trackbar.haveChanged()) {
                    BoxConfigs.setValue(this._ID[0], this._ID[1], false, this._trackbar);
                    this.refresh(false);
                    this.callHostRefresh();
                }
                this._trackbar.setBreakAnimation(this._itemHost.tipsPlugin.panelOn);
            }
        }

        public void renderBelow(float alphaMult) {
            GL11.glPushAttrib(GL11.GL_LINE_BIT);
            GL11.glLineWidth(_ITEM_SPLIT_WIDTH);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], 0.0f);
            final float widthPos = this._pos.getX() + this._pos.getWidth();
            final float heightPos = this._pos.getY() + this._pos.getHeight();
            final float splitHalf = this._pos.getWidth() * 0.5f;
            final float splitBottom = this._pos.getY() - _ITEM_SPACE * 0.5f;
            if (this._bottom) {
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glVertex2f(this._pos.getX(), splitBottom);
                GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], this._itemHost._ITEM_SPLIT_COLOR[3] * alphaMult);
                GL11.glVertex2f(this._pos.getX() + splitHalf, splitBottom);
                GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], 0.0f);
                GL11.glVertex2f(widthPos, splitBottom);
                GL11.glEnd();
            }
            final float splitTop = heightPos + _ITEM_SPACE * 0.5f;
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex2f(this._pos.getX(), splitTop);
            GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], this._itemHost._ITEM_SPLIT_COLOR[3] * alphaMult);
            GL11.glVertex2f(this._pos.getX() + splitHalf, splitTop);
            GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], 0.0f);
            GL11.glVertex2f(widthPos, splitTop);
            GL11.glEnd();
            GL11.glPopAttrib();

            if (this.state[6] > 0.0f) {
                float alpha = (float) Math.sqrt(this.state[6]);
                float heightSpace = 2.0f + this._pos.getHeight() * 0.1f;
                float rectX = this._pos.getX() + 3.0f;
                float rectY = this._pos.getY() + heightSpace;
                float rectYZero = this._pos.getY() + this._pos.getHeight() * 0.5f;
                float rectWidthPos = widthPos - 3.0f;
                float rectHeightPos = heightPos - heightSpace;
                GL11.glColor4f(this._itemHost._ITEM_COLOR_BG[0], this._itemHost._ITEM_COLOR_BG[1], this._itemHost._ITEM_COLOR_BG[2], alpha * alphaMult * 0.35f);
                GL11.glRectf(CalculateUtil.mix(rectX + 16.0f, rectX, this.state[6]), CalculateUtil.mix(rectYZero, rectY, this.state[6]), CalculateUtil.mix(rectWidthPos - 16.0f, rectWidthPos, this.state[6]), CalculateUtil.mix(rectYZero, rectHeightPos, this.state[6]));
            }

            float bgAlpha = (this._itemHost._ITEM_COLOR_BG[3] + this.state[6] * 0.25f) * alphaMult;
            if (this._trackbar != null) bgAlpha = CalculateUtil.mix(bgAlpha, 0.0f, this._trackbar.getAnimationProgress());
            if (bgAlpha > 0.0f) {
                float bgXA = this._pos.getX() + this.state[0];
                float bgXB = this._pos.getX() + this.state[4];
                float bgXC = this._pos.getX() + this.state[2];
                float bgYA = this._pos.getY() + this.state[1];
                float bgYB = this._pos.getY() + this.state[3];
                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glColor4f(this._itemHost._ITEM_COLOR_BG[0], this._itemHost._ITEM_COLOR_BG[1], this._itemHost._ITEM_COLOR_BG[2], 0.0f);
                GL11.glVertex2f(bgXA, bgYA);
                GL11.glVertex2f(bgXA, bgYB);
                GL11.glColor4f(this._itemHost._ITEM_COLOR_BG[0], this._itemHost._ITEM_COLOR_BG[1], this._itemHost._ITEM_COLOR_BG[2], bgAlpha);
                GL11.glVertex2f(bgXB, bgYA);
                GL11.glVertex2f(bgXB, bgYB);
                GL11.glColor4f(this._itemHost._ITEM_COLOR_BG[0], this._itemHost._ITEM_COLOR_BG[1], this._itemHost._ITEM_COLOR_BG[2], 0.0f);
                GL11.glVertex2f(bgXC, bgYA);
                GL11.glVertex2f(bgXC, bgYB);
                GL11.glEnd();
            }
        }

        public void buttonPressed(Object buttonId) {
            if (!this._buttonEnabled) return;
            if (this._itemHost.state[8] >= _EVENT_CD) this._itemHost.state[8] = 0.0f; else return;
            if (this._itemHost.tipsPlugin.panelOn || !this._itemHost.panelOn || !(buttonId instanceof Boolean)) return;
            BoxConfigs.setValue(this._ID[0], this._ID[1], (boolean) buttonId, this._trackbar);
            this.refresh(false);
            this.callHostRefresh();
        }

        public void callHostRefresh() {
            this._itemHost._configChanged = true;
            if (!this._itemHost._saveButton.isEnabled()) {
                this._itemHost._saveButton.setEnabled(true);
                this._itemHost._saveButton.flash(false, 0.0f, 0.5f);
            }
            if (!this._itemHost._undoButton.isEnabled()) {
                this._itemHost._undoButton.setEnabled(true);
            }
        }

        public void refresh(boolean setTrackbar) {
            if (this._info == null) return;
            Pair<String, Boolean> value = BoxConfigs.getValueString(this._ID[0], this._ID[1]);
            if (this._trackbar != null) {
                int valueI = Integer.parseInt(value.one);
                if (valueI == -1) value.one = BoxConfigs.getString("BUtil_ConfigPanel_ValueLimit");
                if (setTrackbar) {
                    if (valueI > 0) {
                        byte exponent = CalculateUtil.getExponentPOTMin(valueI);
                        if (this._ID[1] == 4) exponent -= 7;
                        if (this._ID[1] == 5) exponent -= 2;
                        if (this._ID[1] == 6) exponent -= 3;
                        this._trackbar.setCurrStep(exponent);
                    } else if (valueI == -1) this._trackbar.setCurrStep(this._trackbar.getMaxStep());
                    else this._trackbar.setCurrStep(0);
                }
            }
            this._info.setText(value.one);
            this._info.setColor(value.two ? Misc.getTextColor() : Misc.getNegativeHighlightColor());
        }

        public void refreshLanguage() {
            if (this._title == null) return;
            this._title.setText(BoxConfigs.getString(this.id));
        }

        public void processInput(List<InputEventAPI> events) {
            if (!this._buttonEnabled) return;
            float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
            float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
            boolean within = mouseX >= this._pos.getX() && mouseY >= this._pos.getY() && mouseX <= this._pos.getX() + this._pos.getWidth() && mouseY <= this._pos.getY() + this._pos.getHeight();

            if (!within || this._itemHost.tipsPlugin.panelOn || this._itemHost.state[8] < _EVENT_CD) return;
            boolean rightKey;
            for (InputEventAPI event : events) {
                if (event.isKeyDownEvent()) {
                    rightKey = event.getEventValue() == Keyboard.KEY_RIGHT;
                    if (event.getEventValue() == Keyboard.KEY_LEFT || rightKey) {
                        if (this._trackbar != null) {
                            if (this._trackbar.isEnabled()) {
                                this._trackbar.setCurrStep(this._trackbar.getCurrStep() + (rightKey ? this._trackbar.getKeyStep() : -this._trackbar.getKeyStep()));
                                Global.getSoundPlayer().playUISound(this._trackbar.getDraggingSoundID(), 1.0f, 1.0f);
                            } else Global.getSoundPlayer().playUISound(this._trackbar.getDisabledDraggingSoundID(), 1.0f, 0.5f);
                        } else Global.getSoundPlayer().playUISound("BUtil_button_down", 1.0f, 1.0f);
                        BoxConfigs.setValue(this._ID[0], this._ID[1], rightKey, this._trackbar);
                        this.refresh(false);
                        this.callHostRefresh();
                        this._itemHost.state[8] = 0.0f;
                        event.consume();
                        break;
                    }
                }
            }
        }
    }

    private final static class ShaderPacksTooltip extends BaseTooltipCreator {
        public final String _id;
        public final int _iconID;
        public final String _iconColor;
        public final boolean[] _features = new boolean[7];

        public ShaderPacksTooltip(BaseShaderPacksContext context) {
            this._id = context.getID();
            this._iconID = context.getIconTextureID();
            this._iconColor = "0x" + Integer.toHexString(context.getIconTextureColor().getRGB());
            this._features[0] = context.isCombatIlluminationSupported();
            this._features[1] = context.isCampaignIlluminationSupported();
            this._features[2] = context.isBloomSupported();
            this._features[3] = context.isAASupported();
            this._features[4] = context.isBaseAnisotropySupported();
            this._features[5] = context.isTexturedAreaLightSupported();
            this._features[6] = context.isCompleteAnisotropySupported();
        }

        public void createTooltip(TooltipMakerAPI tooltip, boolean expanded, Object tooltipParam) {
            tooltip.addSectionHeading(BoxConfigs.getString("BUtil_ConfigPanel_FeaturesTips_ShaderPacks_Title"), Alignment.MID, 0.0f);

            final float itemIconHeight = 24.0f;
            final Color pos = Misc.getPositiveHighlightColor(), neg = Misc.getNegativeHighlightColor();
            final String validIcon = "graphics/icons/intel/log_checkmark.png", invalidIcon = "graphics/icons/intel/log_x_mark.png";
            TooltipMakerAPI item;

            for (byte i = 0; i < this._features.length; ++i) {
                item = tooltip.beginImageWithText(this._features[i] ? validIcon : invalidIcon, itemIconHeight);
                item.addPara(BoxConfigs.getString("BUtil_ConfigPanel_FeaturesTips_ShaderPacks_F" + i), this._features[i] ? pos : neg, 0.0f);
                tooltip.addImageWithText(_ITEM_SPACE);
            }

            if (Global.getSettings().isDevMode()) {
                tooltip.addPara("ID: " + this._id, Misc.getGrayColor(), 12.0f);
                tooltip.addPara("Icon: " + this._iconID, Misc.getGrayColor(), _ITEM_SPLIT_WIDTH);
                tooltip.addPara("Icon color: " + this._iconColor, Misc.getGrayColor(), _ITEM_SPLIT_WIDTH);
            }
        }

        public float getTooltipWidth(Object tooltipParam) {
            return 256.0f;
        }
    }

    private final static class ShaderPacksItem extends BaseCustomUIPanelPlugin {
        private final BUtil_BaseConfigPanel _itemHost;
        private final BaseShaderPacksContext _shaderpacks;
        private CustomPanelAPI _itemPanel = null;
        private PositionAPI _pos = null;
        private LabelAPI nameText = null;
        private LabelAPI versionText = null;
        private LabelAPI stateText = null;
        private boolean _bottom;
        private final boolean _shaderpacksUsable;
        private boolean _pickSound = false;
        private boolean _selectChanged = false;
        private final float[] state = new float[2];

        public ShaderPacksItem(BUtil_BaseConfigPanel itemHost, BaseShaderPacksContext context, float width, boolean bottom) {
            this._itemHost = itemHost;
            this._shaderpacks = context;
            this.state[0] = width - _ITEM_SPACE - _ITEM_SPACE - _ITEM_SPACE;
            this._bottom = bottom;
            this._shaderpacksUsable = context.isUsable(GLContext.getCapabilities());
        }

        public UIComponentAPI add(TooltipMakerAPI maker) {
            CustomPanelAPI itemPanel = Global.getSettings().createCustom(this.state[0], _SHADERPACKS_ITEM_ICON_HEIGHT + _ITEM_SPACE, this);
            itemPanel.getPosition().inTL(0.0f, 0.0f);

            final float height = _SHADERPACKS_ITEM_ICON_HEIGHT - _ITEM_SPACE * 6.0f;
            TooltipMakerAPI makerFixed = itemPanel.createUIElement(this.state[0] - _SHADERPACKS_ITEM_ICON_WIDTH - _ITEM_SPACE * 4.0f, height, false);
            makerFixed.getPosition().inTL(_SHADERPACKS_ITEM_ICON_WIDTH + _ITEM_SPACE * 2.0f, _ITEM_SPACE * 3.0f);
            makerFixed.setTitleOrbitronLarge();
            final String nameStr = this._shaderpacks.getDisplayName(), versionStr = this._shaderpacks.getDisplayVersion();
            this.nameText = makerFixed.addTitle(nameStr == null ? this._shaderpacks.getID() : nameStr);
            this.versionText = makerFixed.addPara(versionStr == null ? "" : versionStr, Misc.getTextColor().darker(), 5.0f);
            this.stateText = makerFixed.addPara("", 5.0f);
            this.refresh();
            makerFixed.addTooltipTo(new ShaderPacksTooltip(this._shaderpacks), itemPanel, TooltipMakerAPI.TooltipLocation.BELOW);
            makerFixed.setHeightSoFar(_SHADERPACKS_ITEM_ICON_HEIGHT + _ITEM_SPACE);
            itemPanel.addUIElement(makerFixed);

            this._pos = itemPanel.getPosition();
            this._itemPanel = itemPanel;
            return maker.addCustom(itemPanel, _ITEM_SPACE + _ITEM_SPACE);
        }

        public void destroy(CustomPanelAPI host) {
            host.removeComponent(this._itemPanel);
        }

        public void advance(float amount) {
            float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
            float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
            boolean within = mouseX >= this._pos.getX() && mouseY >= this._pos.getY() && mouseX <= this._pos.getX() + this._pos.getWidth() && mouseY <= this._pos.getY() + this._pos.getHeight();
            within &= !this._itemHost.tipsPlugin.panelOn;
            float a5 = amount * 5.0f;
            if (within && this.state[1] < 1.0f) {
                this.state[1] = Math.min(this.state[1] + a5 + a5, 1.0f);
                if (!this._pickSound) {
                    this._pickSound = true;
                    Global.getSoundPlayer().playUISound("BUtil_ui_pick", 1.0f, 1.0f);
                }
            } else if (!within && this.state[1] > 0.0f) {
                if (this._pickSound) this._pickSound = false;
                this.state[1] -= a5;
            }

            if (BoxConfigs.getCurrShaderPacksContext() == this._shaderpacks) {
                if (!this._selectChanged) {
                    this._selectChanged = true;
                    this.refresh();
                }
            } else if (this._selectChanged) {
                this._selectChanged = false;
                this.refresh();
            }
        }

        public void renderBelow(float alphaMult) {
            final float widthPos = this._pos.getX() + this._pos.getWidth();
            final float heightPos = this._pos.getY() + this._pos.getHeight();
            final float splitHalf = this._pos.getWidth() * 0.5f;
            final float splitBottom = this._pos.getY() - _ITEM_SPACE;
            GL11.glPushAttrib(GL11.GL_LINE_BIT | GL11.GL_ENABLE_BIT);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (this._bottom) {
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glLineWidth(_ITEM_SPLIT_WIDTH);
                GL11.glBegin(GL11.GL_LINE_STRIP);
                GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], 0.0f);
                GL11.glVertex2f(this._pos.getX(), splitBottom);
                GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], this._itemHost._ITEM_SPLIT_COLOR[3] * alphaMult);
                GL11.glVertex2f(this._pos.getX() + splitHalf, splitBottom);
                GL11.glColor4f(this._itemHost._ITEM_SPLIT_COLOR[0], this._itemHost._ITEM_SPLIT_COLOR[1], this._itemHost._ITEM_SPLIT_COLOR[2], 0.0f);
                GL11.glVertex2f(widthPos, splitBottom);
                GL11.glEnd();
            }

            if (this.state[1] > 0.0f) {
                float alpha = (float) Math.sqrt(this.state[1]);
                float rectX = this._pos.getX() + 2.0f;
                float rectY = this._pos.getY() + 3.0f;
                float rectYStart = this._pos.getY() + this._pos.getHeight() * 0.5f;
                float rectWidthPos = widthPos - 3.0f;
                float rectHeightPos = heightPos - 3.0f;
                float[] bgColorPicker;
                if (BoxConfigs.getCurrShaderPacksContext() == this._shaderpacks) bgColorPicker = this._itemHost._ITEM_POS_COLOR;
                else bgColorPicker = this._shaderpacksUsable ? this._itemHost._ITEM_COLOR_BG : this._itemHost._ITEM_NEG_COLOR;
                GL11.glColor4f(bgColorPicker[0], bgColorPicker[1], bgColorPicker[2], alpha * alphaMult * 0.35f);
                GL11.glRectf(CalculateUtil.mix(rectX + 16.0f, rectX, this.state[1]), CalculateUtil.mix(rectYStart, rectY, this.state[1]), CalculateUtil.mix(rectWidthPos - 16.0f, rectWidthPos, this.state[1]), CalculateUtil.mix(rectYStart, rectHeightPos, this.state[1]));
            }

            final float iconPosX = this._pos.getX() + _ITEM_SPACE, iconPoxWidth = iconPosX + _SHADERPACKS_ITEM_ICON_WIDTH,
                    iconPosY = this._pos.getY() + _ITEM_SPACE * 0.5f, iconPosHeight = iconPosY + _SHADERPACKS_ITEM_ICON_HEIGHT;
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            GL11.glRectf(iconPosX, iconPosY, iconPoxWidth, iconPosHeight);
            Misc.setColor(Global.getSettings().getColor("standardGridColor"), alphaMult);
            GL11.glLineWidth(1.0f);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2f(iconPosX - 1.0f, iconPosY - 1.0f);
            GL11.glVertex2f(iconPoxWidth + 1.0f, iconPosY - 1.0f);
            GL11.glVertex2f(iconPoxWidth + 1.0f, iconPosHeight + 1.0f);
            GL11.glVertex2f(iconPosX - 1.0f, iconPosHeight + 1.0f);
            GL11.glEnd();
            int texID = this._shaderpacks.getIconTextureID();
            if (texID == 0) texID = BoxDatabase.BUtil_DefaultShaderPacksIcon.getTextureId();
            if (texID > 0) {
                Color iconColor = this._shaderpacks.getIconTextureColor();
                if (iconColor == null) iconColor = Color.WHITE;
                Misc.setColor(iconColor, alphaMult);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glTexCoord2f(0.0f, 0.0f);
                GL11.glVertex2f(iconPosX, iconPosY);
                GL11.glTexCoord2f(1.0f, 0.0f);
                GL11.glVertex2f(iconPoxWidth, iconPosY);
                GL11.glTexCoord2f(0.0f, 1.0f);
                GL11.glVertex2f(iconPosX, iconPosHeight);
                GL11.glTexCoord2f(1.0f, 1.0f);
                GL11.glVertex2f(iconPoxWidth, iconPosHeight);
                GL11.glEnd();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            if (this._itemHost.selectedShaderPacks == this._shaderpacks) {
                final float arrowPosX = this._pos.getX() + this._pos.getWidth() * 0.8f, arrowPosX2 = this._pos.getX() + this._pos.getWidth() * 0.9f, arrowPosMidY = this._pos.getY() + this._pos.getHeight() * 0.5f;
                Misc.setColor(Global.getSettings().getColor("standardUIIconColor"), alphaMult * 0.5f);
                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glVertex2f(arrowPosX, this._pos.getY());
                GL11.glVertex2f(widthPos, this._pos.getY());
                GL11.glVertex2f(arrowPosX2, arrowPosMidY);
                GL11.glVertex2f(widthPos, arrowPosMidY);
                GL11.glVertex2f(arrowPosX, heightPos);
                GL11.glVertex2f(widthPos, heightPos);
                GL11.glEnd();
            }
            GL11.glPopAttrib();
        }

        public void processInput(List<InputEventAPI> events) {
            float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
            float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
            boolean within = mouseX >= this._pos.getX() && mouseY >= this._pos.getY() && mouseX <= this._pos.getX() + this._pos.getWidth() && mouseY <= this._pos.getY() + this._pos.getHeight();

            if (!within || this._itemHost.tipsPlugin.panelOn || this._itemHost.state[8] < _EVENT_CD) return;
            for (InputEventAPI event : events) {
                if (event.isLMBUpEvent()) {
                    if (this._itemHost.selectedShaderPacks != this._shaderpacks) {
                        if (this._itemHost._configChanged) {
                            this._itemHost.nextSelectedShaderPacks = this._shaderpacks;
                            this._itemHost.panel.addComponent(this._itemHost.tipsPanel);
                            this._itemHost.tipsPlugin.panelOn = true;
                        } else this._itemHost.refreshShaderPacksDetails(this._shaderpacks);
                    }
                    this._itemHost.state[8] = 0.0f;
                    event.consume();
                    break;
                }
            }
        }

        public void refresh() {
            final String nameStr = this._shaderpacks.getDisplayName(), versionStr = this._shaderpacks.getDisplayVersion();
            this.nameText.setText(nameStr == null ? this._shaderpacks.getID() : nameStr);
            this.versionText.setText(versionStr == null ? "" : versionStr);

            String text = BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Available");
            Color color = Misc.getTextColor().darker();
            if (!this._shaderpacksUsable) {
                text = BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Unavailable");
                color = Misc.getNegativeHighlightColor().darker();
            } else if (BoxConfigs.getCurrShaderPacksContext() == this._shaderpacks) {
                text = BoxConfigs.getString("BUtil_ConfigPanel_Tips_ShaderPacks_Enabled");
                color = Misc.getPositiveHighlightColor();
            }
            this.stateText.setText(text);
            this.stateText.setColor(color);
        }

        public void setHavaBottom(boolean value) {
            this._bottom = value;
        }
    }

    public void initBackgroundTex(int texture, float[] uvs) {
        this.backgroundTex = texture;
        this.backgroundTexUV = uvs;
    }

    public void renderBelow(float alphaMult) {
        if (this.backgroundTex != 0) {
            GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT | GL11.GL_VIEWPORT_BIT);
            GL11.glViewport(0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTex);
            GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex2f(-1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, this.backgroundTexUV[1]);
            GL11.glVertex2f(-1.0f, 1.0f);
            GL11.glTexCoord2f(this.backgroundTexUV[0], 0.0f);
            GL11.glVertex2f(1.0f, -1.0f);
            GL11.glTexCoord2f(this.backgroundTexUV[0], this.backgroundTexUV[1]);
            GL11.glVertex2f(1.0f, 1.0f);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
        this.backgroundObject.setAlpha(alphaMult);
        this.backgroundObject.render(this.state[4], this.state[5]);
    }

    private void closePanel() {
        this.state[9] = -1024.0f;
        this._shaderpacksInitTips.setOpacity(0.0f);
        this.host.closePanel();
        this.switchCorePage();
        this.panelOn = false;
    }

    public void processInput(List<InputEventAPI> events) {
        if (!this.panelOn || events == null || events.isEmpty() || this.tipsPlugin.panelOn) return;
        boolean shouldBreak = false;
        float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
        float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
        boolean inWidget = mouseX < this.state[4] || mouseY < this.state[5] || mouseX > this.state[4] + this.state[6] || mouseY > this.state[5] + this.state[7];
        for (InputEventAPI event : events) {
            if (event.isRMBDownEvent() && inWidget) {
                if (this._configChanged) {
                    this._pageSwitchFlag = 0;
                    this.panel.addComponent(this.tipsPanel);
                    this.tipsPlugin.panelOn = true;
                } else this.closePanel();
                this.state[8] = 0.0f;
                shouldBreak = true;
            }
            event.consume();
            if (shouldBreak) break;
        }
    }

    public void buttonPressed(Object buttonId) {
        if (this.state[8] >= _EVENT_CD) this.state[8] = 0.0f; else return;
        if (this.tipsPlugin.panelOn || !this.panelOn) return;
        byte id = (byte) buttonId;
        switch (id) {
            case _BUTTON_CANCEL: {
                if (this._configChanged) {
                    this._pageSwitchFlag = 0;
                    this.panel.addComponent(this.tipsPanel);
                    this.tipsPlugin.panelOn = true;
                } else this.closePanel();
                break;
            }
            case _BUTTON_SAVE: {
                this._saveButton.setEnabled(false);
                this._undoButton.setEnabled(false);
                this._configChanged = false;
                if (this._pageFlag == 0) {
                    BoxConfigs.check();
                    BoxConfigs.save();
                    this.refreshItems();
                    this.refreshItemsLanguage();
                } else {
                    if (this.selectedShaderPacks != null) BoxConfigs.saveShaderPacksConfig(this.selectedShaderPacks);
                }
                break;
            }
            case _BUTTON_DEFAULTS: {
                this._configChanged = true;
                if (!this._saveButton.isEnabled()) {
                    this._saveButton.setEnabled(true);
                    this._saveButton.flash(false, 0.0f, 0.5f);
                }
                if (!this._undoButton.isEnabled()) {
                    this._undoButton.setEnabled(true);
                }
                if (this._pageFlag == 0) {
                    BoxConfigs.setDefault();
                    BoxConfigs.check();
                    this.refreshItems();
                    this.refreshItemsLanguage();
                } else {
                    if (this.selectedShaderPacks != null) this.selectedShaderPacks.configSetDefault();
                }
                break;
            }
            case _BUTTON_UNDO: {
                this._saveButton.setEnabled(false);
                this._undoButton.setEnabled(false);
                this._configChanged = false;
                if (this._pageFlag == 0) {
                    BoxConfigs.load();
                    BoxConfigs.check();
                    this.refreshItems();
                } else {
                    if (this.selectedShaderPacks != null) BoxConfigs.loadShaderPacksConfig(this.selectedShaderPacks);
                }
                break;
            }
            case _BUTTON_PAGE_CORE: {
                if (this._configChanged) {
                    this._pageSwitchFlag = 1;
                    this.panel.addComponent(this.tipsPanel);
                    this.tipsPlugin.panelOn = true;
                } else this.switchCorePage();
                break;
            }
            case _BUTTON_PAGE_SHADERPACKS: {
                if (this._configChanged) {
                    this._pageSwitchFlag = 2;
                    this.panel.addComponent(this.tipsPanel);
                    this.tipsPlugin.panelOn = true;
                } else this.switchShaderPacksPage();
            }
        }
    }

    public boolean isOn() {
        return this.panelOn;
    }

    public void stateSwitch(boolean on) {
        this.panelOn = on;
    }

    public void advance(float amount) {
        if (this.state[8] < _EVENT_CD) this.state[8] += amount;
        if (_shaderPacksInitInfo != null) {
            this._shaderpacksInitTips.setText(_shaderPacksInitInfo);
            this.state[9] = 5.0f;
            _shaderPacksInitInfo = null;
        }
        if (this.state[9] > 0.0f) {
            this._shaderpacksInitTips.setOpacity(Math.min(this.state[9], 2.0f) * 0.5f);
            this.state[9] -= amount;
        } else if (this.state[9] > -1000.0f) {
            this.state[9] = -1024.0f;
            this._shaderpacksInitTips.setOpacity(0.0f);
        }

        if (_shaderpacksListChanged && this._pageFlag == 1) {
            _shaderpacksListChanged = false;
            this.refreshShaderPacksListComponent();
        }
    }

    public void render(float alphaMult) {}

    public void positionChanged(PositionAPI position) {}

    private static ButtonAPI tipsButton(TooltipMakerAPI host, String id, byte type, float width) {
        ButtonAPI button = host.addButton(BoxConfigs.getString(id), type, Misc.getButtonTextColor(), _BUTTON_COLOR_BG, Alignment.MID, CutStyle.TL_BR, width, _SPACE_BOTTOM, 0);
        button.setMouseOverSound("BUtil_button_in");
        button.setButtonPressedSound("BUtil_button_down");
        return button;
    }

    private final static class CloseTips implements CustomUIPanelPlugin {
        private CustomPanelAPI tipsPanel = null;
        private final UIBorderObject backgroundObject;
        private final BUtil_BaseConfigPanel tipsHost;
        private LabelAPI _text = null;
        private final ButtonAPI[] _button = new ButtonAPI[2];
        private final float[] state = new float[9];
        private boolean panelOn = false;

        public CloseTips(BUtil_BaseConfigPanel tipsHost, float width, float height, float BLx, float BLy) {
            this.tipsHost = tipsHost;
            this.backgroundObject = new UIBorderObject(false, true);
            this.state[0] = width;
            this.state[1] = height;
            this.state[2] = BLx;
            this.state[3] = BLy;
            this.state[4] = ShaderCore.getScreenWidth() * (1.0f - _TIPS_WIDTH_FACTOR) * 0.5f - _BORDER_WIDTH;
            this.state[5] = (ShaderCore.getScreenHeight() - height) * 0.5f - _BORDER_WIDTH;
            this.state[6] = width + _BORDER_WIDTH + _BORDER_WIDTH;
            this.state[7] = height + _BORDER_WIDTH + _BORDER_WIDTH;
            this.backgroundObject.setSize(this.state[6], this.state[7]);
        }

        public void init(CustomPanelAPI panel) {
            final float buttonWidth = Math.max(this.state[0] * 0.2f, 120.0f);
            final float buttonOffset = buttonWidth * 0.2f;
            final float textMakerHeight = this.state[1] - _SPACE_BOTTOM - _BORDER_WIDTH - _BORDER_WIDTH;
            this.tipsPanel = panel;
            TooltipMakerAPI makerFixed = this.tipsPanel.createUIElement(this.state[0], textMakerHeight, false);
            makerFixed.setParaOrbitronVeryLarge();
            makerFixed.setParaFontColor(Misc.getNegativeHighlightColor());
            this._text = makerFixed.addPara(BoxConfigs.getString("BUtil_ConfigPanel_CancelTips"), 0.0f);
            this._text.setAlignment(Alignment.MID);
            makerFixed.getPosition().inTMid((textMakerHeight - this._text.getPosition().getHeight()) * 0.5f);
            TooltipMakerAPI makerButton = this.tipsPanel.createUIElement(this.state[0], _SPACE_BOTTOM, false);
            makerButton.getPosition().inBMid(_BORDER_WIDTH);
            makerButton.setButtonFontOrbitron20();
            this._button[0] = tipsButton(makerButton, "BUtil_ConfigPanel_CancelTipsY", _BUTTON_TIPS_EXIT, buttonWidth);
            this._button[0].getPosition().inBL(buttonOffset, _BORDER_WIDTH);
            this._button[1] = tipsButton(makerButton, "BUtil_ConfigPanel_CancelTipsN", _BUTTON_TIPS_RETURN, buttonWidth);
            this._button[1].getPosition().inBR(buttonOffset, _BORDER_WIDTH);
            this.tipsPanel.addUIElement(makerFixed);
            this.tipsPanel.addUIElement(makerButton);
        }

        public void renderBelow(float alphaMult) {
            GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT | GL11.GL_VIEWPORT_BIT);
            GL11.glViewport(0, 0, ShaderCore.getScreenScaleWidth(), ShaderCore.getScreenScaleHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            GL11.glRectf(-1.0f, -1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glPopMatrix();
            GL11.glPopAttrib();
            this.backgroundObject.setAlpha(alphaMult);
            this.backgroundObject.render(this.state[4], this.state[5]);
        }

        public void processInput(List<InputEventAPI> events) {
            if (!this.panelOn || events == null || events.isEmpty()) return;
            boolean shouldBreak = false;
            float mouseX = Mouse.getX() * _SCREEN_SCALE_DIV;
            float mouseY = Mouse.getY() * _SCREEN_SCALE_DIV;
            boolean inWidget = mouseX < this.state[4] || mouseY < this.state[5] || mouseX > this.state[4] + this.state[6] || mouseY > this.state[5] + this.state[7];
            for (InputEventAPI event : events) {
                if ((event.isKeyDownEvent() && event.getEventValue() == Keyboard.KEY_ESCAPE && this.state[8] >= _EVENT_CD) || (event.isRMBDownEvent() && inWidget)) {
                    this.tipsHost.panel.removeComponent(this.tipsHost.tipsPanel);
                    this.state[8] = 0.0f;
                    this.panelOn = false;
                    shouldBreak = true;
                }
                event.consume();
                if (shouldBreak) break;
            }
        }

        public void buttonPressed(Object buttonId) {
            if (this.state[8] >= _EVENT_CD) this.state[8] = 0.0f; else return;
            if (!this.panelOn) return;
            byte id = (byte) buttonId;
            switch (id) {
                case _BUTTON_TIPS_EXIT: {
                    if (this.tipsHost.nextSelectedShaderPacks != null) {
                        if (this.tipsHost.selectedShaderPacks != null) BoxConfigs.loadShaderPacksConfig(this.tipsHost.selectedShaderPacks);
                        this.tipsHost.selectedShaderPacks = this.tipsHost.nextSelectedShaderPacks;
                        this.tipsHost.nextSelectedShaderPacks = null;
                        this.tipsHost.refreshShaderPacksDetails(this.tipsHost.selectedShaderPacks);
                    } else {
                        switch (this.tipsHost._pageSwitchFlag) {
                            case 0: {
                                if (this.tipsHost._pageFlag == 0) {
                                    BoxConfigs.load();
                                    BoxConfigs.check();
                                    this.tipsHost.refreshItems();
                                } else {
                                    if (this.tipsHost.selectedShaderPacks != null) BoxConfigs.loadShaderPacksConfig(this.tipsHost.selectedShaderPacks);
                                }
                                this.tipsHost.closePanel();
                                break;
                            }
                            case 1: {
                                if (this.tipsHost.selectedShaderPacks != null) BoxConfigs.loadShaderPacksConfig(this.tipsHost.selectedShaderPacks);
                                this.tipsHost.switchCorePage();
                                break;
                            }
                            case 2: {
                                BoxConfigs.load();
                                BoxConfigs.check();
                                this.tipsHost.refreshItems();
                                this.tipsHost.switchShaderPacksPage();
                            }
                        }
                        this.tipsHost._pageSwitchFlag = 0;
                    }
                    this.tipsHost._saveButton.setEnabled(false);
                    this.tipsHost._undoButton.setEnabled(false);
                    this.tipsHost.panel.removeComponent(this.tipsPanel);
                    this.panelOn = false;
                    this.state[8] = 0.0f;
                    this.tipsHost._configChanged = false;
                    break;
                }
                case _BUTTON_TIPS_RETURN: {
                    this.tipsHost.nextSelectedShaderPacks = null;
                    this.tipsHost._pageSwitchFlag = 0;
                    this.tipsHost.panel.removeComponent(this.tipsPanel);
                    this.panelOn = false;
                    this.state[8] = 0.0f;
                }
            }
        }

        public void advance(float amount) {
            if (this.state[8] < _EVENT_CD) this.state[8] += amount;
        }

        public void render(float alphaMult) {}

        public void positionChanged(PositionAPI position) {}

        public void refresh() {
            if (this._text != null) this._text.setText(BoxConfigs.getString("BUtil_ConfigPanel_CancelTips"));
            if (this._button[0] != null) this._button[0].setText(BoxConfigs.getString("BUtil_ConfigPanel_CancelTipsY"));
            if (this._button[1] != null) this._button[1].setText(BoxConfigs.getString("BUtil_ConfigPanel_CancelTipsN"));
        }
    }

    // fuck the shit
    private final static class CoreBody extends BaseCustomUIPanelPlugin {}

    private final static class ShaderPacksSelectCoreBody extends BaseCustomUIPanelPlugin {
        private final BUtil_BaseConfigPanel _itemHost;
        private float timer = 0.0f;

        public ShaderPacksSelectCoreBody(BUtil_BaseConfigPanel host) {
            this._itemHost = host;
        }

        public void advance(float amount) {
            if (this.timer < _EVENT_CD) this.timer += amount;
        }

        public void buttonPressed(Object buttonId) {
            if (this.timer >= _EVENT_CD) this.timer = 0.0f; else return;
            if (this._itemHost.tipsPlugin.panelOn || !this._itemHost.panelOn) return;
            byte id = (byte) buttonId;
            if (id == _BUTTON_SHADERPACKS_SELECT) {
                this._itemHost._shaderPacksSelectButton.setEnabled(false);
                BoxConfigs.setShaderPacksContext(this._itemHost.selectedShaderPacks);
            }
        }
    }

    private final static class ShaderPacksDetailsBody extends BaseCustomUIPanelPlugin {
        private final BUtil_BaseConfigPanel _itemHost;
        private final float[] state = new float[5];
        private final PositionAPI[] _pos = new PositionAPI[2];

        public ShaderPacksDetailsBody(BUtil_BaseConfigPanel itemHost, float width, float height) {
            this._itemHost = itemHost;
            this.state[1] = width;
            this.state[2] = height;
        }

        public void setPosition(PositionAPI planePos, PositionAPI elementPos) {
            this._pos[0] = planePos;
            this._pos[1] = elementPos;
        }

        public void render(float alphaMult) {
            if (this._itemHost.selectedShaderPacks != null) this._itemHost.selectedShaderPacks.detailsPanelRender(alphaMult, this._pos[0], this._pos[1]);
        }

        public void renderBelow(float alphaMult) {
            final float TRx = this._pos[0].getX() + this._pos[0].getWidth(), TRy = this._pos[0].getY() + this._pos[0].getHeight();
            GL11.glPushAttrib(GL11.GL_LINE_BIT);
            GL11.glLineWidth(1.0f);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            GL11.glRectf(this._pos[0].getX(), this._pos[0].getY(), TRx, TRy);
            Misc.setColor(Global.getSettings().getColor("standardGridColor"), alphaMult);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2f(this._pos[0].getX(), this._pos[0].getY());
            GL11.glVertex2f(TRx, this._pos[0].getY());
            GL11.glVertex2f(TRx, TRy);
            GL11.glVertex2f(this._pos[0].getX(), TRy);
            GL11.glEnd();
            GL11.glPopAttrib();
            if (this._itemHost.selectedShaderPacks != null) this._itemHost.selectedShaderPacks.detailsPanelRenderBelow(alphaMult, this._pos[0], this._pos[1]);
        }

        public void advance(float amount) {
            if (this._itemHost.selectedShaderPacks != null && this._itemHost.selectedShaderPacks.detailsPanelAdvance(amount, this._pos[0], this._pos[1])) this.callHostRefresh();
        }

        public void processInput(List<InputEventAPI> events) {
            if (this._itemHost.selectedShaderPacks != null && this._itemHost.selectedShaderPacks.detailsPanelProcessInput(events, this._pos[0], this._pos[1])) this.callHostRefresh();
        }

        public void buttonPressed(Object buttonId) {
            if (this._itemHost.selectedShaderPacks != null && this._itemHost.selectedShaderPacks.detailsPanelButtonPressed(buttonId, this._pos[0], this._pos[1])) this.callHostRefresh();
        }

        public void callHostRefresh() {
            if (!this._itemHost._configChanged) {
                this._itemHost._configChanged = true;
                if (!this._itemHost._saveButton.isEnabled()) {
                    this._itemHost._saveButton.setEnabled(true);
                    this._itemHost._saveButton.flash(false, 0.0f, 0.5f);
                }
                if (!this._itemHost._undoButton.isEnabled()) {
                    this._itemHost._undoButton.setEnabled(true);
                }
            }
        }
    }

    public static void refreshShaderPacksList() {
        _shaderpacksListChanged = true;
    }

    public static void callShaderPacksInitFailed(String info) {
        _shaderPacksInitInfo = info;
    }
}
