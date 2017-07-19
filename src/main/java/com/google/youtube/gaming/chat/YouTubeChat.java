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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Main entry point for YouTube Chat. Provides the chat service API to other mods, e.g.
 *
 * YouTubeChatService youTubeChatService = YouTubeChat.getService();
 */
@Mod(modid = YouTubeChat.MODID, version = YouTubeChat.VERSION, guiFactory = YouTubeChat.GUI_FACTORY)
public class YouTubeChat {
  public static final String MODID = "ytchat";
  public static final String APPNAME = "YouTube Chat";
  public static final String VERSION = "1.0.2";
  public static final String GUI_FACTORY =
      "com.google.youtube.gaming.chat.YouTubeConfigurationGuiFactory";

  private static YouTubeChatService service;

  public static synchronized YouTubeChatService getService() {
    if (service == null) {
      service = new ChatService();
    }

    return service;
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    YouTubeConfiguration.initialize(event.getSuggestedConfigurationFile());
  }

  @EventHandler
  public void serverLoad(FMLServerStartingEvent event) {
    YouTubeChatMock.register();
    YouTubeCommand command = new YouTubeCommand((ChatService) YouTubeChat.getService());
    event.registerServerCommand(command);
  }
}
