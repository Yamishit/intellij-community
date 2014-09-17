/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package git4idea.push;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Push result as reported by {@code git push} command.
 *
 * @see GitPushNativeResultParser
 * @see GitPushRepoResult
 */
class GitPushNativeResult {

  enum Type {
    SUCCESS,
    FORCED_UPDATE,
    NEW_REF,
    REJECTED,
    DELETED,
    UP_TO_DATE,
    ERROR
  }

  @NotNull private final Type myType;
  @Nullable private final String myRange;
  @Nullable private final String myErrorOutput;

  private GitPushNativeResult(@NotNull Type type, @Nullable String range, @Nullable String errorOutput) {
    myType = type;
    myRange = range;
    myErrorOutput = errorOutput;
  }

  public GitPushNativeResult(@NotNull Type type, @Nullable String range) {
    this(type, range, null);
  }

  @NotNull
  public static GitPushNativeResult error(@NotNull String errorOutput) {
    return new GitPushNativeResult(Type.ERROR, null, errorOutput);
  }

  @NotNull
  public Type getType() {
    return myType;
  }

  @Nullable
  public String getRange() {
    return myRange;
  }

  @Nullable
  public String getErrorOutput() {
    return myErrorOutput;
  }

  @Override
  public String toString() {
    return String.format("%s: '%s', stderr:[%s]", myType, myRange, myErrorOutput);
  }
}
