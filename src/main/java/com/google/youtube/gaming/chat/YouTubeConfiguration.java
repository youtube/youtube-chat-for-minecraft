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

import java.io.File;
import net.minecraftforge.common.config.Configuration;

/**
 * Configuration settings for YouTube Chat.
 */
public class YouTubeConfiguration {
  private static YouTubeConfiguration instance;
  private static Configuration config;
  private String clientSecret;
  private String videoId;

  public static void initialize(File path) {
    instance = new YouTubeConfiguration(path);
  }

  public static YouTubeConfiguration getInstance() {
    return instance;
  }

  private YouTubeConfiguration(File path) {
    config = new Configuration(path);
    config.load();
    addGeneralConfig();
    if (config.hasChanged()) {
      config.save();
    }
  }

  private void addGeneralConfig() {
    clientSecret =
        config
            .get(
                Configuration.CATEGORY_GENERAL,
                "Client Secret",
                "",
                "The client secret from Google API console")
            .getString();
    videoId =
        config
            .get(Configuration.CATEGORY_GENERAL, "videoId", "", "The id of the live video")
            .getString();
  }

  public void reset() {
    if (config != null) {
      config.save();
    }
    addGeneralConfig();
  }

  public String getVideoId() {
    return videoId;
  }

  public Configuration getConfig() {
    return config;
  }

  public String getClientSecret() {
    return clientSecret;
  }
}
