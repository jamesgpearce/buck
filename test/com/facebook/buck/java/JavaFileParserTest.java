/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.java;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSortedSet;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

public class JavaFileParserTest {

  static String exampleJavaCode =
      "package com.example;\n" +
      "\n" +
      "public class Example {\n" +
      "  public static int variablesNotCaptured, maybeLater;\n" +
      "\n" +
      "  private Example() {}\n" +
      "\n" +
      "  public static void functionsNotCapturedEither() {\n" +
      "  }\n" +
      "\n" +
      "  public enum InnerEnum {\n" +
      "    foo;\n" +
      "\n" +
      "    public class InnerClass {\n" +
      "    }\n" +
      "  }\n" +
      "}\n" +
      "\n" +
      "class AnotherOuterClass {\n" +
      "}\n";

  @Test
  public void testJavaFileParsing() throws IOException {

    JavaCompilerEnvironment env = new JavaCompilerEnvironment(
        Optional.<Path>absent(),
        Optional.<JavacVersion>absent(),
        "7",
        "7"
    );

    JavaFileParser parser = JavaFileParser.createJavaFileParser(env);

    ImmutableSortedSet<String> symbols = parser.getExportedSymbolsFromString(exampleJavaCode);

    assertEquals("JavaFileParser didn't find the symbols we expected.",
        ImmutableSortedSet.of(
            "com.example.AnotherOuterClass",
            "com.example.Example",
            "com.example.Example.InnerEnum",
            "com.example.Example.InnerEnum.InnerClass"),
        symbols);
  }
}
