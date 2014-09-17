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

import git4idea.GitLocalBranch;
import git4idea.GitRemoteBranch;
import git4idea.update.GitUpdateResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Result of pushing one repository.
 * <p/>
 * Includes information about the number of pushed commits (or -1 if undefined),
 * and tells whether the repository was updated after the push was rejected.
 *
 * @see git4idea.push.GitPushNativeResult
 */
class GitPushRepoResult {

  enum Type {
    SUCCESS,
    NEW_BRANCH,
    FORCED,
    REJECTED,
    ERROR,
    NOT_PUSHED;
  }

  static Comparator<Type> TYPE_COMPARATOR = new Comparator<Type>() {
    @Override
    public int compare(Type o1, Type o2) {
      return o1.ordinal() - o2.ordinal();
    }
  };

  @NotNull private final Type myType;
  private final int myCommits;
  @NotNull private final String mySourceBranch;
  @NotNull private final String myTargetBranch;
  @Nullable private final String myError;
  @Nullable private final GitUpdateResult myUpdateResult;

  @NotNull
  static GitPushRepoResult convertFromNative(@NotNull GitPushNativeResult result,
                                             int commits, @NotNull GitLocalBranch source, @NotNull GitRemoteBranch target) {
    return new GitPushRepoResult(convertType(result.getType()), commits, source.getFullName(), target.getFullName(),
                                 result.getErrorOutput(), null);
  }

  @NotNull
  static GitPushRepoResult notPushed(GitLocalBranch source, GitRemoteBranch target) {
    return new GitPushRepoResult(Type.NOT_PUSHED, -1, source.getFullName(), target.getFullName(), null, null);
  }

  @NotNull
  static GitPushRepoResult addUpdateResult(GitPushRepoResult original, GitUpdateResult updateResult) {
    return new GitPushRepoResult(original.getType(), original.getNumberOfPushedCommits(), original.getSourceBranch(),
                                 original.getTargetBranch(), original.getError(), updateResult);
  }

  private GitPushRepoResult(@NotNull Type type, int pushedCommits, @NotNull String sourceBranch, @NotNull String targetBranch,
                    @Nullable String error, @Nullable GitUpdateResult result) {
    myType = type;
    myCommits = pushedCommits;
    mySourceBranch = sourceBranch;
    myTargetBranch = targetBranch;
    myError = error;
    myUpdateResult = result;
  }

  @NotNull
  Type getType() {
    return myType;
  }

  @Nullable
  GitUpdateResult getUpdateResult() {
    return myUpdateResult;
  }

  int getNumberOfPushedCommits() {
    return myCommits;
  }

  /**
   * Returns the branch we were pushing from, in the full-name format, e.g. {@code refs/heads/master}.
   */
  @NotNull
  String getSourceBranch() {
    return mySourceBranch;
  }

  /**
   * Returns the branch we were pushing to, in the full-name format, e.g. {@code refs/remotes/origin/master}.
   */
  @NotNull
  String getTargetBranch() {
    return myTargetBranch;
  }

  @Nullable
  String getError() {
    return myError;
  }

  @NotNull
  private static Type convertType(@NotNull GitPushNativeResult.Type nativeType) {
    switch (nativeType) {
      case SUCCESS:
        return Type.SUCCESS;
      case FORCED_UPDATE:
        return Type.FORCED;
      case NEW_REF:
        return Type.NEW_BRANCH;
      case REJECTED:
        return Type.REJECTED;
      case UP_TO_DATE:
        return Type.SUCCESS;
      case ERROR:
        return Type.ERROR;
      case DELETED:
      default:
        throw new IllegalArgumentException("Conversion is not supported: " + nativeType);
    }
  }

  @Override
  public String toString() {
    return String.format("%s (%d, '%s'), update: %s}", myType, myCommits, mySourceBranch, myUpdateResult);
  }

}
