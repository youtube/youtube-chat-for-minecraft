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
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * An in-game command that will mock chat messages for testing. Usage:
 *
 * /ytcmock authorId message
 */
public class YouTubeChatMock implements ICommand {
  private static final String COMMAND_NAME = "ytcmock";

  private final List aliases;

  public YouTubeChatMock() {
    aliases = new ArrayList();
    aliases.add(COMMAND_NAME);
  }

  public static void register() {
    ClientCommandHandler.instance.registerCommand(new YouTubeChatMock());
  }

  @Override
  public String getCommandName() {
    return COMMAND_NAME;
  }

  @Override
  public String getCommandUsage(ICommandSender sender) {
    return COMMAND_NAME + " <mock author id> <mock input>";
  }

  @Override
  public List getCommandAliases() {
    return aliases;
  }

  @Override
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    StringBuilder builder = new StringBuilder();
    if (args.length < 2) {
      showMessage(getCommandUsage(sender), sender);
      return;
    }

    String author = args[0];
    for (int i = 1; i < args.length; i++) {
      if (i > 1) {
        builder.append(" ");
      }
      builder.append(args[i]);
    }
    String message = builder.toString();
    System.out.println(String.format("YouTubeChatMock received %1$s from %2$s", message, author));
    LiveChatMessageAuthorDetails authorDetails = new LiveChatMessageAuthorDetails();
    authorDetails.setDisplayName(author);
    authorDetails.setChannelId(author);
    ((ChatService) YouTubeChat.getService()).broadcastMessage(
        authorDetails, new LiveChatSuperChatDetails(), message);
  }

  @Override
  public boolean canCommandSenderUseCommand(ICommandSender sender) {
    return true;
  }

  @Override
  public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
    return null;
  }

  @Override
  public boolean isUsernameIndex(String[] args, int index) {
    return false;
  }

  @Override
  public int compareTo(Object o) {
    return 0;
  }

  private void showMessage(String message, ICommandSender sender) {
    sender.addChatMessage(new ChatComponentText(message));
  }
}
