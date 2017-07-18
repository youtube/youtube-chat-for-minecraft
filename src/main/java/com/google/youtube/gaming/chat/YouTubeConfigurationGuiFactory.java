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

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * Configuration factory for YouTube Chat.
 */
public class YouTubeConfigurationGuiFactory implements IModGuiFactory {

  @Override
  public void initialize(Minecraft minecraftInstance) {}

  @Override
  public boolean hasConfigGui() {
    return true;
  }

  @Override
  public GuiScreen createConfigGui(GuiScreen parentScreen) {
    return new YouTubeConfigurationGui(parentScreen);
  }

  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }
}
