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

import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

/**
 * Gui configuration for YouTube Chat.
 */
public class YouTubeConfigurationGui extends GuiConfig {
  public YouTubeConfigurationGui(GuiScreen parentScreen) {
    super(
        parentScreen,
        new ConfigElement<Object>(
                YouTubeConfiguration.getInstance()
                    .getConfig()
                    .getCategory(Configuration.CATEGORY_GENERAL))
            .getChildElements(),
        YouTubeChat.MODID,
        false,
        false,
        "YouTube Chat");
  }

  @Override
  public void onGuiClosed() {
    YouTubeConfiguration.getInstance().reset();
  }
}
