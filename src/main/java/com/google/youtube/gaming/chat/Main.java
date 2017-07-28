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

import com.google.api.services.youtube.YouTubeScopes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      showUsage();
      return;
    }

    switch (args[0]) {
      case "login":
        System.out.print("Paste the client ID JSON from the Google API console:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String clientSecret = br.readLine();
        List<String> scopes = new ArrayList<String>();
        scopes.add(YouTubeScopes.YOUTUBE_FORCE_SSL);
        scopes.add(YouTubeScopes.YOUTUBE);
        Auth.authorize(scopes, clientSecret, YouTubeChat.MODID);
        break;
      case "logout":
        Auth.clearCredentials();
        break;
      default:
        showUsage();
    }
  }

  private static void showUsage() {
    System.err.println("Supported arguments: login | logout");
  }
}
