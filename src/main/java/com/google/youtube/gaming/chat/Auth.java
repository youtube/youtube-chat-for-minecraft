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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

/**
 * Contains methods for authorizing a user and caching credentials.
 */
public class Auth {

  /** Define a global instance of the HTTP transport. */
  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  /** Define a global instance of the JSON factory. */
  public static final JsonFactory JSON_FACTORY = new JacksonFactory();

  /**
   * This is the directory that will be used under the user's home directory where OAuth tokens will
   * be stored.
   */
  private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";

  /**
   * Authorizes the installed application to access user's protected data.
   *
   * @param scopes list of scopes needed to run youtube upload.
   * @param clientSecret the client secret from Google API console
   * @param credentialDatastore name of the credential datastore to cache OAuth tokens
   */
  public static Credential authorize(
      Collection<String> scopes, String clientSecret, String credentialDatastore)
      throws IOException {
    // Load client secrets
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new StringReader(clientSecret));

    // This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
    FileDataStoreFactory fileDataStoreFactory =
        new FileDataStoreFactory(new File(getCredentialsDirectory()));
    DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
            .setCredentialDataStore(datastore)
            .build();

    // authorize
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  public static void clearCredentials() throws IOException {
    File directory = new File(getCredentialsDirectory());
    if (directory.exists()) {
      deleteDirectory(directory);
    }
  }

  private static boolean deleteDirectory(File path) {
    if (path.exists()) {
      File[] files = path.listFiles();
      for(int i=0; i < files.length; i++) {
        if(files[i].isDirectory()) {
          deleteDirectory(files[i]);
        } else {
          files[i].delete();
        }
      }
    }
    return path.delete();
  }

  private static String getCredentialsDirectory() {
    return System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY;
  }
}
