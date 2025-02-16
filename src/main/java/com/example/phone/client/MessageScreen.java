package com.example.phone.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MessageScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier("phonemod", "textures/gui/message.png");
    private TextFieldWidget messageInput;

    public MessageScreen() {
        super(Text.translatable("gui.phonemod.messages"));
    }

    @Override
    protected void init() {
        this.messageInput = new TextFieldWidget(textRenderer, width/2 - 75, height/2 - 10, 150, 20, Text.literal(""));
        this.addDrawableChild(messageInput);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.phonemod.send"), button -> {
            String message = messageInput.getText();
            if (!message.isEmpty()) {
                executeCommand("msg @a " + message);
                close();
            }
        }).position(width/2 - 40, height/2 + 20).size(80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        context.drawTexture(TEXTURE, width/2 - 100, height/2 - 50, 0, 0, 200, 100, 200, 100);
        super.render(context, mouseX, mouseY, delta);
    }

    private void executeCommand(String command) {
        if (client != null && client.player != null) {
            client.player.networkHandler.sendChatCommand(command);
        }
    }
}