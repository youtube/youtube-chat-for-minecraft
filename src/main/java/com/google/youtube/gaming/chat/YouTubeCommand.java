/**
 * Copyright 2017 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.google.youtube.gaming.chat;

import com.google.api.services.youtube.model.LiveChatMessageAuthorDetails;
import com.google.api.services.youtube.model.LiveChatSuperChatDetails;
import com.google.youtube.gaming.chat.YouTubeChatService.YouTubeChatMessageListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * An in-game command for managing the YouTube Chat service. Usage:
 *
 * /ytchat [start|stop|logout|echoStart|echoStop]
 */
public class YouTubeCommand implements ICommand, YouTubeChatMessageListener {
  private final List aliases;
  private ChatService service;

  public YouTubeCommand(ChatService service) {
    this.service = service;
    aliases = new ArrayList();
    aliases.add("ytchat");
    aliases.add("ytc");
  }

  @Override
  public String getName() {
    return "ytchat";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return getName() + " [start|stop|logout|echoStart|echoStop]";
  }

  @Override
  public List<String> getAliases() {
    return this.aliases;
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    World world = sender.getEntityWorld();
    if (!world.isRemote) {
      if (args.length == 0) {
        showUsage(sender);
        return;
      }

      if (args[0].equalsIgnoreCase("start")) {
        YouTubeConfiguration configuration = YouTubeConfiguration.getInstance();
        String clientSecret = configuration.getClientSecret();
        if (clientSecret == null || clientSecret.isEmpty()) {
          showMessage("No client secret configurated", sender);
          return;
        }
        service.start(configuration.getVideoId(), clientSecret, sender);
      } else if (args[0].equalsIgnoreCase("stop")) {
        service.stop(sender);
      } else if (args[0].equalsIgnoreCase("logout")) {
        service.stop(sender);
        try {
          Auth.clearCredentials();
        } catch(IOException e) {
          showMessage(e.getMessage(), sender);
        }
      } else {
        if (args[0].equalsIgnoreCase("echoStart")) {
          if (!service.isInitialized()) {
            showMessage("Service is not initialized", sender);
            showUsage(sender);
          } else {
            service.subscribe(this);
          }
        } else if (args[0].equalsIgnoreCase("echoStop")) {
          service.unsubscribe(this);
        } else {
          showUsage(sender);
        }
      }
    }
  }

  private void showUsage(ICommandSender sender) {
    showMessage("Usage: " + getUsage(sender), sender);
  }

  private void showMessage(String message, ICommandSender sender) {
    sender.sendMessage(new TextComponentString(message));
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return true;
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
    return null;
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return false;
  }

  @Override
  public int compareTo(ICommand o) {
    return 0;
  }

  @Override
  public void onMessageReceived(
      LiveChatMessageAuthorDetails author,
      LiveChatSuperChatDetails superChatDetails,
      String message) {
    showMessage(message, Minecraft.getMinecraft().player);
    if (superChatDetails != null
          && superChatDetails.getAmountMicros() != null
          && superChatDetails.getAmountMicros().longValue() > 0) {
      showMessage("Received "
              + superChatDetails.getAmountDisplayString()
              + " from "
              + author.getDisplayName(), Minecraft.getMinecraft().player);
    }
  }
}
