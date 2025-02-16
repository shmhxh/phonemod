package com.example.phone.client;

import com.example.phone.item.PhoneItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PhoneScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier("phonemod", "textures/gui/phone.png");
    private static final int GUI_WIDTH = 200;
    private static final int GUI_HEIGHT = 160;
    private final ItemStack phoneStack;

    public PhoneScreen(ItemStack phoneStack) {
        super(Text.translatable("gui.phonemod.title"));
        this.phoneStack = phoneStack;
    }

    @Override
    protected void init() {
        int x = (this.width - GUI_WIDTH) / 2 + 10;
        int y = (this.height - GUI_HEIGHT) / 2 + 20;

        // 天气控制
        addWeatherButton(x, y, "clear_weather", "weather clear 600");
        addWeatherButton(x, y + 30, "rain_weather", "weather rain 600");
        addWeatherButton(x, y + 60, "thunder_weather", "weather thunder 600");

        // 时间控制
        addTimeButton(x + 100, y, "day_time", 1000);
        addTimeButton(x + 100, y + 30, "noon_time", 6000);
        addTimeButton(x + 100, y + 60, "sunset_time", 12000);
        addTimeButton(x + 100, y + 90, "midnight_time", 18000);

        // 拍照功能
        addCameraButton(x, y + 120);

        // 短信功能
        addMessageButton(x + 100, y + 120);
    }

    private void addWeatherButton(int x, int y, String key, String command) {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.phonemod." + key), button -> {
            if (checkEnergy(5)) {
                PhoneItem.consumeEnergy(phoneStack, 5);
                executeCommand(command);
            }
        }).position(x, y).size(80, 20).build());
    }

    private void addTimeButton(int x, int y, String key, long time) {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.phonemod." + key), button -> {
            if (checkEnergy(5)) {
                PhoneItem.consumeEnergy(phoneStack, 5);
                executeCommand("time set " + time);
            }
        }).position(x, y).size(80, 20).build());
    }

    private void addCameraButton(int x, int y) {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.phonemod.take_photo"), button -> {
            if (checkEnergy(10)) {
                PhoneItem.consumeEnergy(phoneStack, 10);
                takePhoto();
            }
        }).position(x, y).size(80, 20).build());
    }

    private void addMessageButton(int x, int y) {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.phonemod.messages"), button -> {
            if (checkEnergy(5)) {
                PhoneItem.consumeEnergy(phoneStack, 5);
                client.setScreen(new MessageScreen());
            }
        }).position(x, y).size(80, 20).build());
    }

    private boolean checkEnergy(int cost) {
        int energy = PhoneItem.getEnergy(phoneStack);
        if (energy < cost) {
            if (client != null && client.player != null) {
                client.player.sendMessage(Text.literal("§c电量不足，无法操作！"));
            }
            return false;
        }
        return true;
    }

    private void executeCommand(String command) {
        if (client != null && client.player != null) {
            client.player.networkHandler.sendChatCommand(command);
        }
    }

    private void takePhoto() {
        if (client != null) {
            String fileName = "phone_photo_" + System.currentTimeMillis() + ".png";
            File screenshotDir = new File(client.runDirectory, "screenshots/phone");
            if (!screenshotDir.exists()) screenshotDir.mkdirs();
            client.takeRawScreenshot(screenshotDir, fileName);
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§a照片已保存: " + fileName));
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        context.drawTexture(TEXTURE, (width - GUI_WIDTH)/2, (height - GUI_HEIGHT)/2, 
            0, 0, GUI_WIDTH, GUI_HEIGHT, GUI_WIDTH, GUI_HEIGHT);
        super.render(context, mouseX, mouseY, delta);
    }
}