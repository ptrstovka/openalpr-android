/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.openalpr.jni.utils;

import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    private Utils() {}

    /**
     * Copies the assets folder.
     *
     * @param assetManager The assets manager.
     * @param fromAssetPath The from assets path.
     * @param toPath The to assets path.
     *
     * @return A boolean indicating if the process went as expected.
     */
    public static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);

            new File(toPath).mkdirs();

            boolean res = true;

            for (String file : files)

                if (file.contains(".")) {
                    res &= copyAsset(assetManager, fromAssetPath + File.separator + file, toPath + File.separator + file);
                } else {
                    res &= copyAssetFolder(assetManager, fromAssetPath + File.separator + file, toPath + File.separator + file);
                }

            return res;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Copies an asset to the application folder.
     *
     * @param assetManager The asset manager.
     * @param fromAssetPath The from assets path.
     * @param toPath The to assests path.
     *
     * @return A boolean indicating if the process went as expected.
     */
    private static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = assetManager.open(fromAssetPath);

            new File(toPath).createNewFile();

            out = new FileOutputStream(toPath);

            copyFile(in, out);
            in.close();

            in = null;

            out.flush();
            out.close();

            out = null;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copies a file.
     *
     * @param in The input stream.
     * @param out The output stream.
     *
     * @throws IOException
     */
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];

        int read;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
